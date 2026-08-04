/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComunicacionesDAO;

import conexion.ConexionDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Scanner;
import modelo.Nomina;

/**
 *
 * @author fernan
 */
public class NominaDAO {
    private Connection connection;
    public NominaDAO(){
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }
    
    public static final String INSERTAR_NOMINA =
            """
            INSERT INTO nomina
            (dpi_empleado, fecha_emision, tipo_de_pago, estado_pago, monto)
            VALUES ('%s', '%s', '%s', '%s', %.2f)
            """;
    
    public static final String CONSULTAR_NOMINAS = 
            """
            SELECT * FROM nomina;
            """;
    
    public static final String PAGAR_NOMINA = 
            """
            UPDATE nomina
            SET estado_pago = 'PAGADO'
            WHERE codigo_nomina = %d
            """;
    
    public void registrarNomina() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese DPI del empleado:");
    String dpi = scanner.nextLine();

    System.out.println("Ingrese fecha de emisión (AAAA-MM-DD):");
    String fecha = scanner.nextLine();

    System.out.println("Ingrese tipo de pago (QUINCENA o FIN_DE_MES):");
    String tipo = scanner.nextLine();

    System.out.println("Ingrese monto:");
    double monto = scanner.nextDouble();

    Nomina nomina = new Nomina(
            0,
            dpi,
            fecha,
            tipo,
            "PENDIENTE",
            monto
    );

    String insert = String.format(
            Locale.US,
            INSERTAR_NOMINA,
            nomina.getDpiEmpleado(),
            nomina.getFechaEmision(),
            nomina.getTipoPago(),
            nomina.getEstadoPago(),
            nomina.getMonto()
    );

    try {

        Statement statement = connection.createStatement();

        System.out.println(insert);

        int filas = statement.executeUpdate(insert);

        System.out.println("Nómina registrada.");
        System.out.println("Filas insertadas: " + filas);

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    public void listarNominas() {

    try {

        Statement statement = connection.createStatement();

        ResultSet resultado = statement.executeQuery(CONSULTAR_NOMINAS);

        while (resultado.next()) {

            System.out.println("----------------------------");
            System.out.println("Código: "
                    + resultado.getInt("codigo_nomina"));

            System.out.println("Empleado: "
                    + resultado.getString("dpi_empleado"));

            System.out.println("Fecha: "
                    + resultado.getDate("fecha_emision"));

            System.out.println("Tipo: "
                    + resultado.getString("tipo_de_pago"));

            System.out.println("Estado: "
                    + resultado.getString("estado_pago"));

            System.out.println("Monto: "
                    + resultado.getDouble("monto"));

        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    
    public void pagarNomina() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese código de nómina:");

    int codigo = scanner.nextInt();

    String update = String.format(
            PAGAR_NOMINA,
            codigo
    );

    try {

        Statement statement = connection.createStatement();

        System.out.println(update);

        int filas = statement.executeUpdate(update);

        System.out.println("Pago realizado.");

        System.out.println("Filas afectadas: " + filas);

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
}
