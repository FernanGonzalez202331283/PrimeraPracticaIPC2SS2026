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
import java.util.Scanner;
import modelo.Empleado;

/**
 *
 * @author fernan
 */
public class EmpleadoDAO {
   private Connection connection;

    public EmpleadoDAO() {

        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();

    }
    
    public static final String INSERTAR_EMPLEADO="""
        INSERT INTO empleado
        (dpi, nombre, rol, jornada, salario, fecha_contratacion, estado)
        VALUES('%s', '%s','%s','%s', %.2f, '%s',%b)
        """;
    
    public static final String CONSULTAR_EMPLEADO =
            "SELECT * FROM empleado";
    
    public static final String ACTUALIZAR_EMPLEADO = 
            """
            UPDATE empleado
            SET nombre = '%s',
            rol = '%s',
            jornada = '%s',
            salario = %.2f,
            fecha_contratacion = '%s'
            WHERE dpi = '%s'
            """;
    
    public static final String DESHABILITAR_EMPLEADO = 
            """
            UPDATE empleado
            SET estado = false
            WHERE dpi = '%s'
            """;
    
    public void insertarEmpleado(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese DPI: ");
        String dpi = scanner.nextLine();
        
        System.out.println("Ingrese nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.println("Ingrese rol: ");
        String rol = scanner.nextLine();
        
        System.out.println("Ingrese jornada: ");
        String jornada = scanner.nextLine();
        
        System.out.println("Ingrese salario: ");
        Double salario = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.println("Ingrese fecha de contratacion (AA-MM-DD): ");
        String fecha = scanner.nextLine();
        
        Empleado empleado = new Empleado(
                dpi,
                nombre,
                rol,
                jornada,
                salario,
                fecha,
                true
        );
        
    String insert = String.format(
            java.util.Locale.US,
            INSERTAR_EMPLEADO,
            empleado.getDpi(),
            empleado.getNombre(),
            empleado.getRol(),
            empleado.getJornada(),
            empleado.getSalario(),
            empleado.getFechaContratacion(),
            empleado.isEstado()
    );

    try {

        Statement insertStatement = connection.createStatement();
        System.out.println(insert);
        int filas = insertStatement.executeUpdate(insert);
        System.out.println("Empleado registrado correctamente.");
        System.out.println("Filas insertadas: " + filas);

    } catch (SQLException e) {

        e.printStackTrace();

    }
        
    }
    public void listarEmpleados() {

    try {

        Statement consulta = connection.createStatement();

        ResultSet resultado = consulta.executeQuery(CONSULTAR_EMPLEADO);

        while (resultado.next()) {

            System.out.println("--------------------------------");
            System.out.println("DPI: " + resultado.getString("dpi"));
            System.out.println("Nombre: " + resultado.getString("nombre"));
            System.out.println("Rol: " + resultado.getString("rol"));
            System.out.println("Jornada: " + resultado.getString("jornada"));
            System.out.println("Salario: " + resultado.getDouble("salario"));
            System.out.println("Fecha contratación: " + resultado.getDate("fecha_contratacion"));
            System.out.println("Estado: " + resultado.getBoolean("estado"));

        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    public void actualizarEmpleado() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese DPI del empleado a actualizar:");
    String dpi = scanner.nextLine();

    System.out.println("Ingrese nuevo nombre:");
    String nombre = scanner.nextLine();

    System.out.println("Ingrese nuevo rol:");
    String rol = scanner.nextLine();

    System.out.println("Ingrese nueva jornada:");
    String jornada = scanner.nextLine();

    System.out.println("Ingrese nuevo salario:");
    Double salario = scanner.nextDouble();
    scanner.nextLine();

    System.out.println("Ingrese nueva fecha de contratación (AAAA-MM-DD):");
    String fecha = scanner.nextLine();

    String update = String.format(
            java.util.Locale.US,
            ACTUALIZAR_EMPLEADO,
            nombre,
            rol,
            jornada,
            salario,
            fecha,
            dpi
    );

    try {

        Statement updateStatement = connection.createStatement();

        System.out.println(update);

        int filas = updateStatement.executeUpdate(update);

        System.out.println("Empleado actualizado correctamente.");
        System.out.println("Filas actualizadas: " + filas);

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    public void deshabilitarEmpleado() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese el DPI del empleado:");

    String dpi = scanner.nextLine();

    String update = String.format(
            DESHABILITAR_EMPLEADO,
            dpi
    );

    try {

        Statement updateStatement = connection.createStatement();

        System.out.println(update);

        int filas = updateStatement.executeUpdate(update);

        System.out.println("Empleado deshabilitado correctamente.");
        System.out.println("Filas afectadas: " + filas);

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
}
