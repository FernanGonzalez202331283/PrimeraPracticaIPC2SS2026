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
import modelo.Mesa;

/**
 *
 * @author fernan
 */
public class MesaDAO {
    
     private Connection connection;
    
    public MesaDAO(){
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }
    public static final String INSERTAR_MESA =
            """
            INSERT INTO mesa
            (numero_mesa, capacidad, estado)
            VALUES(?,?,?)
            """;

    public static final String CONSULTAR_MESAS =
            """
            SELECT *
            FROM mesa
            ORDER BY numero_mesa
            """;

    public static final String ACTUALIZAR_ESTADO =
            """
            UPDATE mesa
            SET estado = ?
            WHERE numero_mesa = ?
            """;
    
    
    public void registrarMesa() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese el número de mesa:");
    int numeroMesa = scanner.nextInt();

    System.out.println("Ingrese la capacidad:");
    int capacidad = scanner.nextInt();
    scanner.nextLine();

    Mesa mesa = new Mesa(
            numeroMesa,
            capacidad,
            "LIBRE"
    );

    try {

        PreparedStatement statement =
                connection.prepareStatement(INSERTAR_MESA);

        statement.setInt(1, mesa.getNumeroMesa());
        statement.setInt(2, mesa.getCapacidad());
        statement.setString(3, mesa.getEstado());

        int filas = statement.executeUpdate();

        if (filas > 0) {

            System.out.println("Mesa registrada correctamente.");

        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    
    public void listarMesas() {

    try {

        PreparedStatement statement =
                connection.prepareStatement(CONSULTAR_MESAS);

        ResultSet resultado = statement.executeQuery();

        while (resultado.next()) {

            System.out.println("----------------------------");

            System.out.println(
                    "Número de mesa: "
                    + resultado.getInt("numero_mesa")
            );

            System.out.println(
                    "Capacidad: "
                    + resultado.getInt("capacidad")
            );

            System.out.println(
                    "Estado: "
                    + resultado.getString("estado")
            );

        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    
    public void actualizarEstadoMesa() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese el número de mesa:");

    int numeroMesa = scanner.nextInt();
    scanner.nextLine();

    System.out.println("Nuevo estado (LIBRE u OCUPADA):");

    String estado = scanner.nextLine();

    try {

        PreparedStatement statement =
                connection.prepareStatement(ACTUALIZAR_ESTADO);

        statement.setString(1, estado);
        statement.setInt(2, numeroMesa);

        int filas = statement.executeUpdate();

        if (filas > 0) {

            System.out.println("Estado de la mesa actualizado.");

        } else {

            System.out.println("No existe una mesa con ese número.");

        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
}
