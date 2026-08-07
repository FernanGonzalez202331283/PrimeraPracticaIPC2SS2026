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
import javax.swing.JOptionPane;
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
    
    public static final String OBTENER_ESTADO =
        """
        SELECT estado
        FROM empleado
        WHERE dpi = ?
        """;
    
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
    
    public static final String CAMBIAR_ESTADO =
        """
        UPDATE empleado
        SET estado = ?
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
    public void actualizarEmpleado(Empleado empleado) {

    try {

        PreparedStatement updateStatement =
                connection.prepareStatement(ACTUALIZAR_EMPLEADO);

        updateStatement.setString(1, empleado.getNombre());
        updateStatement.setString(2, empleado.getRol());
        updateStatement.setString(3, empleado.getJornada());
        updateStatement.setDouble(4, empleado.getSalario());
        updateStatement.setString(5, empleado.getFechaContratacion());
        updateStatement.setString(6, empleado.getDpi());

        int filas = updateStatement.executeUpdate();

        if (filas > 0) {

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Empleado actualizado correctamente."
            );

        } else {

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "No existe un empleado con ese DPI."
            );

        }

    } catch (SQLException e) {

        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Error al actualizar el empleado."
        );

    }

}
    
    public void cambiarEstadoEmpleado(String dpi, boolean estado) {

    try {
        PreparedStatement ps = connection.prepareStatement(CAMBIAR_ESTADO);

        ps.setBoolean(1, estado);
        ps.setString(2, dpi);

        int filas = ps.executeUpdate();

        if (filas > 0) {

            String mensaje = estado
                    ? "Empleado habilitado correctamente."
                    : "Empleado deshabilitado correctamente.";

            JOptionPane.showMessageDialog(null, mensaje);

        } else {

            JOptionPane.showMessageDialog(null,
                    "No existe un empleado con ese DPI.");

        }

    } catch (SQLException e) {

        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
                "Error al cambiar el estado del empleado.");

    }

}
    public void deshabilitarEmpleado(String dpi) {

    try {

        PreparedStatement updateStatement =
                connection.prepareStatement(DESHABILITAR_EMPLEADO);

        updateStatement.setString(1, dpi);

        int filas = updateStatement.executeUpdate();

        if (filas > 0) {

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Empleado deshabilitado correctamente."
            );

        } else {

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "No existe un empleado con ese DPI."
            );

        }

    } catch (SQLException e) {

        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Error al deshabilitar el empleado."
        );

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
    
    public boolean obtenerEstadoEmpleado(String dpi) {

    try {

        PreparedStatement ps =
                connection.prepareStatement(OBTENER_ESTADO);

        ps.setString(1, dpi);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            return rs.getBoolean("estado");

        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

    return false;
}
}
