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
    
    public static final String INSERTAR_EMPLEADO = """
        INSERT INTO empleado
        (dpi, nombre, rol, jornada, salario, fecha_contratacion, estado)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
    
   public static final String CONSULTAR_EMPLEADO =
        "SELECT * FROM empleado";
    
    public static final String ACTUALIZAR_EMPLEADO =
        """
        UPDATE empleado
        SET nombre = ?,
            rol = ?,
            jornada = ?,
            salario = ?,
            fecha_contratacion = ?
        WHERE dpi = ?
        """;
    
    public static final String DESHABILITAR_EMPLEADO =
        """
        UPDATE empleado
        SET estado = false
        WHERE dpi = ?
        """;
    
    public void insertarEmpleado(Empleado empleado) {

    try {

        PreparedStatement insertStatement =
                connection.prepareStatement(INSERTAR_EMPLEADO);

        insertStatement.setString(1, empleado.getDpi());
        insertStatement.setString(2, empleado.getNombre());
        insertStatement.setString(3, empleado.getRol());
        insertStatement.setString(4, empleado.getJornada());
        insertStatement.setDouble(5, empleado.getSalario());
        insertStatement.setString(6, empleado.getFechaContratacion());
        insertStatement.setBoolean(7, empleado.isEstado());

        int filas = insertStatement.executeUpdate();

        if (filas > 0) {
            System.out.println("Empleado registrado correctamente.");
        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    public void listarEmpleados() {

    try {
        PreparedStatement consulta =
        connection.prepareStatement(CONSULTAR_EMPLEADO);
ResultSet resultado = consulta.executeQuery();

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
    try {

    PreparedStatement updateStatement =
            connection.prepareStatement(ACTUALIZAR_EMPLEADO);

    updateStatement.setString(1, nombre);
    updateStatement.setString(2, rol);
    updateStatement.setString(3, jornada);
    updateStatement.setDouble(4, salario);
    updateStatement.setString(5, fecha);
    updateStatement.setString(6, dpi);

    int filas = updateStatement.executeUpdate();

    if (filas > 0) {
        System.out.println("Empleado actualizado correctamente.");
        System.out.println("Filas actualizadas: " + filas);
    } else {
        System.out.println("No existe un empleado con ese DPI.");
    }

} catch (SQLException e) {

    e.printStackTrace();

}

}
    public void deshabilitarEmpleado() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese el DPI del empleado:");

    String dpi = scanner.nextLine();
    try {

    PreparedStatement updateStatement =
            connection.prepareStatement(DESHABILITAR_EMPLEADO);

    updateStatement.setString(1, dpi);

    int filas = updateStatement.executeUpdate();

    if (filas > 0) {
        System.out.println("Empleado deshabilitado correctamente.");
        System.out.println("Filas afectadas: " + filas);
    } else {
        System.out.println("No existe un empleado con ese DPI.");
    }

} catch (SQLException e) {

    e.printStackTrace();

}

}
    public ResultSet obtenerEmpleados() {

    try {

        PreparedStatement consulta =
                connection.prepareStatement(CONSULTAR_EMPLEADO);

        return consulta.executeQuery();

    } catch (SQLException e) {

        e.printStackTrace();

    }

    return null;

}
}
