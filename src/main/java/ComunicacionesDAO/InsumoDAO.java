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
import modelo.Insumo;

/**
 *
 * @author fernan
 */
public class InsumoDAO {

    private Connection connection;

    public InsumoDAO() {
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }

    public static final String INSERTAR_INSUMO
            = """
        INSERT INTO insumo
        (nombre, unidad_medida, stock_actual, stock_minimo, costo)
        VALUES (?, ?, ?, ?, ?)
        """;

    public static final String CONSULTAR_INSUMO
            = "SELECT * FROM insumo";

    public static final String ACTUALIZAR_INSUMO
            = """
            UPDATE insumo
            SET nombre = ?,
                unidad_medida = ?,
                stock_actual = ?,
                stock_minimo = ?,
                costo = ?
            WHERE codigo_insumo = ?
            """;

    public static final String CONSULTAR_BAJO_STOCK
            = """
            SELECT * FROM insumo
            WHERE stock_actual <= stock_minimo
            """;

    public static final String CONSULTAR_COSTO_INSUMO
            = """
        SELECT costo
        FROM insumo
        WHERE codigo_insumo = ?
        """;

    public void insertarInsumo(Insumo insumo) {

        try {

            PreparedStatement statement
                    = connection.prepareStatement(INSERTAR_INSUMO);

            statement.setString(1, insumo.getNombre());
            statement.setString(2, insumo.getUnidadMedida());
            statement.setDouble(3, insumo.getStockActual());
            statement.setDouble(4, insumo.getStockMinimo());
            statement.setDouble(5, insumo.getCosto());

            int filas = statement.executeUpdate();

            if (filas > 0) {

                JOptionPane.showMessageDialog(
                        null,
                        "Insumo registrado correctamente."
                );

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo registrar el insumo."
                );

            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error al registrar el insumo."
            );

        }

    }

    public ResultSet listarInsumos() {

        try {

            PreparedStatement consulta
                    = connection.prepareStatement(CONSULTAR_INSUMO);

            return consulta.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;
    }

    public void listarBajoStock() {

        try {

            Statement consulta = connection.createStatement();

            ResultSet resultado
                    = consulta.executeQuery(CONSULTAR_BAJO_STOCK);

            while (resultado.next()) {

                System.out.println("ALERTA: STOCK BAJO");

                System.out.println(
                        resultado.getString("nombre")
                );

                System.out.println(
                        "Actual: "
                        + resultado.getDouble("stock_actual")
                );

                System.out.println(
                        "Minimo: "
                        + resultado.getDouble("stock_minimo")
                );

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public void actualizarInsumo(Insumo insumo) {

        try {

            PreparedStatement statement
                    = connection.prepareStatement(ACTUALIZAR_INSUMO);

            statement.setString(1, insumo.getNombre());
            statement.setString(2, insumo.getUnidadMedida());
            statement.setDouble(3, insumo.getStockActual());
            statement.setDouble(4, insumo.getStockMinimo());
            statement.setDouble(5, insumo.getCosto());
            statement.setInt(6, insumo.getCodigoInsumo());

            int filas = statement.executeUpdate();

            if (filas > 0) {

                JOptionPane.showMessageDialog(
                        null,
                        "Insumo actualizado correctamente."
                );

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "No existe el insumo seleccionado."
                );

            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error al actualizar el insumo."
            );

        }
    }

    public double obtenerCostoInsumo(int codigoInsumo) {

        try {

            PreparedStatement statement
                    = connection.prepareStatement(CONSULTAR_COSTO_INSUMO);

            statement.setInt(1, codigoInsumo);

            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {

                return resultado.getDouble("costo");

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;
    }

    public ResultSet consultarInsumos() {

        try {

            PreparedStatement statement
                    = connection.prepareStatement(CONSULTAR_INSUMO);

            return statement.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
        }
    }
}
