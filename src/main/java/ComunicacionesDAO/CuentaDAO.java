/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComunicacionesDAO;

import conexion.ConexionDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import modelo.Cuenta;

/**
 *
 * @author fernan
 */
public class CuentaDAO {
     private Connection connection;

    public CuentaDAO() {

        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }
    
    public static final String INSERTAR_CUENTA =
            """
            INSERT INTO cuenta
            (numero_mesa,dpi_mesero,fecha,hora_ocupacion,
            hora_liberacion,estado,propina,total)
            VALUES(?,?,?,?,?,?,?,?)
            """;


    public static final String CONSULTAR_CUENTAS =
            """
            SELECT *
            FROM cuenta
            ORDER BY id_cuenta
            """;


    public static final String PAGAR_CUENTA =
            """
            UPDATE cuenta
            SET estado = 'PAGADA',
            hora_liberacion = ?,
            propina = ?,
            total = ?
            WHERE id_cuenta = ?
            """;


    public static final String LIBERAR_MESA =
            """
            UPDATE mesa
            SET estado = 'LIBRE'
            WHERE numero_mesa = ?
            """;
    
    public static final String OBTENER_NUMERO_MESA =
            """
            SELECT numero_mesa
            FROM cuenta
            WHERE id_cuenta = ?
            """;
    
    public void abrirCuenta() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese número de mesa:");
    int numeroMesa = scanner.nextInt();
    scanner.nextLine();

    System.out.println("Ingrese DPI del mesero:");
    String dpiMesero = scanner.nextLine();

    System.out.println("Ingrese fecha (AAAA-MM-DD):");
    String fecha = scanner.nextLine();

    System.out.println("Ingrese hora de ocupación (HH:MM:SS):");
    String hora = scanner.nextLine();

    Cuenta cuenta = new Cuenta(
            0,
            numeroMesa,
            dpiMesero,
            fecha,
            hora,
            null,
            "ABIERTA",
            0,
            0
    );

    try {

        PreparedStatement statement =
                connection.prepareStatement(INSERTAR_CUENTA);

        statement.setInt(1, cuenta.getNumeroMesa());
        statement.setString(2, cuenta.getDpiMesero());
        statement.setString(3, cuenta.getFecha());
        statement.setString(4, cuenta.getHoraOcupacion());
        statement.setString(5, cuenta.getHoraLiberacion());
        statement.setString(6, cuenta.getEstado());
        statement.setDouble(7, cuenta.getPropina());
        statement.setDouble(8, cuenta.getTotal());

        int filas = statement.executeUpdate();

        if (filas > 0) {

            System.out.println("Cuenta creada correctamente.");

        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    public void listarCuentas() {

    try {

        PreparedStatement statement =
                connection.prepareStatement(CONSULTAR_CUENTAS);

        ResultSet resultado = statement.executeQuery();

        while (resultado.next()) {

            System.out.println("--------------------------------");

            System.out.println(
                    "ID Cuenta: "
                    + resultado.getInt("id_cuenta")
            );

            System.out.println(
                    "Mesa: "
                    + resultado.getInt("numero_mesa")
            );

            System.out.println(
                    "Mesero: "
                    + resultado.getString("dpi_mesero")
            );

            System.out.println(
                    "Fecha: "
                    + resultado.getDate("fecha")
            );

            System.out.println(
                    "Hora ocupación: "
                    + resultado.getTime("hora_ocupacion")
            );

            System.out.println(
                    "Hora liberación: "
                    + resultado.getTime("hora_liberacion")
            );

            System.out.println(
                    "Estado: "
                    + resultado.getString("estado")
            );

            System.out.println(
                    "Propina: Q"
                    + resultado.getDouble("propina")
            );

            System.out.println(
                    "Total: Q"
                    + resultado.getDouble("total")
            );

        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    public void pagarCuenta() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese el ID de la cuenta:");
    int idCuenta = scanner.nextInt();

    System.out.println("Ingrese la hora de liberación (HH:MM:SS):");
    scanner.nextLine();
    String horaLiberacion = scanner.nextLine();

    System.out.println("Ingrese la propina:");
    double propina = scanner.nextDouble();

    System.out.println("Ingrese el total de la cuenta:");
    double total = scanner.nextDouble();

    try {

        connection.setAutoCommit(false);
        PreparedStatement obtenerMesa =
                connection.prepareStatement(OBTENER_NUMERO_MESA);

        obtenerMesa.setInt(1, idCuenta);

        ResultSet resultado = obtenerMesa.executeQuery();

        if (!resultado.next()) {

            throw new SQLException("La cuenta no existe.");

        }

        int numeroMesa = resultado.getInt("numero_mesa");
        PreparedStatement pagarCuenta =
                connection.prepareStatement(PAGAR_CUENTA);

        pagarCuenta.setString(1, horaLiberacion);
        pagarCuenta.setDouble(2, propina);
        pagarCuenta.setDouble(3, total);
        pagarCuenta.setInt(4, idCuenta);

        int filasCuenta = pagarCuenta.executeUpdate();

        if (filasCuenta == 0) {

            throw new SQLException("No se pudo actualizar la cuenta.");

        }

        PreparedStatement liberarMesa =
                connection.prepareStatement(LIBERAR_MESA);

        liberarMesa.setInt(1, numeroMesa);

        int filasMesa = liberarMesa.executeUpdate();

        if (filasMesa == 0) {

            throw new SQLException("No se pudo liberar la mesa.");

        }

        connection.commit();

        System.out.println("Cuenta pagada correctamente.");
        System.out.println("La mesa quedó disponible.");

    } catch (SQLException e) {

        try {

            connection.rollback();

            System.out.println("Ocurrió un error.");
            System.out.println("Se canceló toda la operación.");

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

        e.printStackTrace();

    } finally {

        try {

            connection.setAutoCommit(true);

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}
}
