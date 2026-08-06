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

/**
 *
 * @author fernan
 */
public class DetalleCuentaDAO {

    private Connection connection;

    public DetalleCuentaDAO() {

        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();

    }
    public static final String INSERTAR_DETALLE
            = """
            INSERT INTO detalle_cuenta
            (id_cuenta,codigo_producto,cantidad,precio,subtotal)
            VALUES(?,?,?,?,?)
            """;

    public static final String CONSULTAR_DETALLE
            = """
            SELECT *
            FROM detalle_cuenta
            WHERE id_cuenta = ?
            """;

    public static final String CONSULTAR_PRECIO
            = """
            SELECT precio
            FROM producto
            WHERE codigo_producto = ?
            """;

    public static final String CONSULTAR_RECETA
            = """
            SELECT codigo_insumo,cantidad
            FROM receta
            WHERE codigo_producto = ?
            """;

    public static final String CONSULTAR_STOCK
            = """
            SELECT stock_actual
            FROM insumo
            WHERE codigo_insumo = ?
            """;

    public static final String DESCONTAR_STOCK
            = """
            UPDATE insumo
            SET stock_actual = stock_actual - ?
            WHERE codigo_insumo = ?
            """;

    public static final String CONSULTAR_TOTAL
            = """
            SELECT total
            FROM cuenta
            WHERE id_cuenta = ?
            """;

    public static final String ACTUALIZAR_TOTAL
            = """
        UPDATE cuenta
        SET total = ?
        WHERE id_cuenta = ?
        """;

    public void agregarProductoCuenta() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID de la cuenta:");
        int idCuenta = scanner.nextInt();
        System.out.println("Ingrese el código del producto:");
        int codigoProducto = scanner.nextInt();
        System.out.println("Ingrese la cantidad:");
        int cantidadProducto = scanner.nextInt();
        try {
            connection.setAutoCommit(false);
            // Obtener el precio del producto
            PreparedStatement precioStatement
                    = connection.prepareStatement(CONSULTAR_PRECIO);
            precioStatement.setInt(1, codigoProducto);
            ResultSet precioResultado = precioStatement.executeQuery();
            if (!precioResultado.next()) {
                throw new SQLException("El producto no existe.");
            }
            double precio = precioResultado.getDouble("precio");
            double subtotal = precio * cantidadProducto;
            // Obtener la receta del producto
            PreparedStatement recetaStatement
                    = connection.prepareStatement(CONSULTAR_RECETA);
            recetaStatement.setInt(1, codigoProducto);
            ResultSet recetaResultado = recetaStatement.executeQuery();
            while (recetaResultado.next()) {
                int codigoInsumo
                        = recetaResultado.getInt("codigo_insumo");
                double cantidadReceta
                        = recetaResultado.getDouble("cantidad");
                double cantidadNecesaria
                        = cantidadReceta * cantidadProducto;
                // Consultar stock
                PreparedStatement stockStatement
                        = connection.prepareStatement(CONSULTAR_STOCK);
                stockStatement.setInt(1, codigoInsumo);
                ResultSet stockResultado
                        = stockStatement.executeQuery();
                if (!stockResultado.next()) {
                    throw new SQLException("El insumo no existe.");
                }
                double stockActual
                        = stockResultado.getDouble("stock_actual");
                if (stockActual < cantidadNecesaria) {
                    throw new SQLException(
                            "Stock insuficiente para el insumo "
                            + codigoInsumo
                    );
                }
                // Descontar stock
                PreparedStatement descontarStatement
                        = connection.prepareStatement(DESCONTAR_STOCK);
                descontarStatement.setDouble(1, cantidadNecesaria);
                descontarStatement.setInt(2, codigoInsumo);
                descontarStatement.executeUpdate();
            }
            PreparedStatement detalleStatement
                    = connection.prepareStatement(INSERTAR_DETALLE);
            detalleStatement.setInt(1, idCuenta);
            detalleStatement.setInt(2, codigoProducto);
            detalleStatement.setInt(3, cantidadProducto);
            detalleStatement.setDouble(4, precio);
            detalleStatement.setDouble(5, subtotal);
            detalleStatement.executeUpdate();
        
            PreparedStatement totalStatement
                    = connection.prepareStatement(CONSULTAR_TOTAL);
            totalStatement.setInt(1, idCuenta);

            ResultSet totalResultado
                    = totalStatement.executeQuery();

            double totalActual = 0;

            if (totalResultado.next()) {

                totalActual
                        = totalResultado.getDouble("total");
            }
            // Actualizar total
            PreparedStatement actualizarTotal
                    = connection.prepareStatement(ACTUALIZAR_TOTAL);
            actualizarTotal.setDouble(1,
                    totalActual + subtotal);
            actualizarTotal.setInt(2, idCuenta);
            actualizarTotal.executeUpdate();
            connection.commit();
            System.out.println(
                    "Producto agregado correctamente."
            );
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println(
                        "La operación fue cancelada."
                );
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

    public void listarDetalleCuenta() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID de la cuenta:");

        int idCuenta = scanner.nextInt();

        try {

            PreparedStatement statement
                    = connection.prepareStatement(CONSULTAR_DETALLE);

            statement.setInt(1, idCuenta);

            ResultSet resultado = statement.executeQuery();

            while (resultado.next()) {

                System.out.println("--------------------------------");

                System.out.println(
                        "Producto: "
                        + resultado.getInt("codigo_producto")
                );

                System.out.println(
                        "Cantidad: "
                        + resultado.getInt("cantidad")
                );

                System.out.println(
                        "Precio: Q"
                        + resultado.getDouble("precio")
                );

                System.out.println(
                        "Subtotal: Q"
                        + resultado.getDouble("subtotal")
                );

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }
}
