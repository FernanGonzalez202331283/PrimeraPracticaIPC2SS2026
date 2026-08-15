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
import modelo.DetalleCuenta;

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
            SELECT COALESCE(SUM(subtotal), 0) AS total
            FROM detalle_cuenta
            WHERE id_cuenta = ?
            """;

    public static final String ACTUALIZAR_TOTAL
            = """
            UPDATE cuenta
            SET total = ?
            WHERE id_cuenta = ?
            """;
    
    public static final String CONSULTAR_DETALLES
            = """
            SELECT
                dc.id_detalle,
                dc.id_cuenta,
                dc.codigo_producto,
                p.nombre AS producto,
                dc.cantidad,
                dc.precio,
                dc.subtotal
            FROM detalle_cuenta dc
            INNER JOIN producto p
                ON dc.codigo_producto = p.codigo_producto
            WHERE dc.id_cuenta = ?
            ORDER BY dc.id_detalle
            """;
    
    public boolean insertarDetalle(DetalleCuenta detalle) {
        try {
            PreparedStatement statement  = connection.prepareStatement( INSERTAR_DETALLE);
            statement.setInt(1,detalle.getIdCuenta());
            statement.setInt(2,detalle.getCodigoProducto());
            statement.setInt(3, detalle.getCantidad());
            statement.setDouble(4, detalle.getPrecio());
            statement.setDouble(5, detalle.getSubtotal());
            int filas = statement.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet consultarDetalles(int idCuenta) {
        try {
            PreparedStatement statement = connection.prepareStatement( CONSULTAR_DETALLES);
            statement.setInt( 1,idCuenta);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double consultarTotal(int idCuenta) {
        try {
            PreparedStatement statement = connection.prepareStatement( CONSULTAR_TOTAL);
            statement.setInt(1, idCuenta);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                return resultado.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean agregarProductoTransaccion(DetalleCuenta detalle) {
        try {
            connection.setAutoCommit(false);
            int codigoProducto = detalle.getCodigoProducto();
            int cantidadProducto = detalle.getCantidad();
            PreparedStatement precioStatement = connection.prepareStatement(CONSULTAR_PRECIO);
            precioStatement.setInt(1, codigoProducto);
            ResultSet precioResultado = precioStatement.executeQuery();
            if (!precioResultado.next()) {
                throw new SQLException(
                        "El producto no existe."
                );
            }
            
            double precio = precioResultado.getDouble("precio");
            double subtotal = precio * cantidadProducto;
            
            PreparedStatement recetaStatement = connection.prepareStatement(CONSULTAR_RECETA);
            recetaStatement.setInt(1, codigoProducto);
            
            ResultSet recetaResultado = recetaStatement.executeQuery();
            
            while (recetaResultado.next()) {
                int codigoInsumo = recetaResultado.getInt("codigo_insumo");
                double cantidadReceta = recetaResultado.getDouble("cantidad");
                double cantidadNecesaria = cantidadReceta * cantidadProducto;
                PreparedStatement stockStatement = connection.prepareStatement(CONSULTAR_STOCK);
                stockStatement.setInt(1, codigoInsumo);
                ResultSet stockResultado = stockStatement.executeQuery();
                
                if (!stockResultado.next()) {
                    throw new SQLException(
                            "El insumo no existe."
                    );
                }
                
                double stockActual = stockResultado.getDouble("stock_actual");
                
                if (stockActual < cantidadNecesaria) {
                    throw new SQLException(
                            "Stock insuficiente para el insumo "
                            + codigoInsumo
                    );
                }
            }

            PreparedStatement recetaDescuento = connection.prepareStatement(CONSULTAR_RECETA);
            recetaDescuento.setInt(1, codigoProducto);
            ResultSet recetaDescuentoResultado = recetaDescuento.executeQuery();
            
            while (recetaDescuentoResultado.next()) {
                
                int codigoInsumo = recetaDescuentoResultado.getInt("codigo_insumo");
                
                double cantidadReceta = recetaDescuentoResultado.getDouble("cantidad");
                
                double cantidadNecesaria = cantidadReceta * cantidadProducto;
                
                PreparedStatement descontarStatement = connection.prepareStatement(DESCONTAR_STOCK);
                descontarStatement.setDouble(1, cantidadNecesaria);
                descontarStatement.setInt(2, codigoInsumo);
                int filas = descontarStatement.executeUpdate();
                
                if (filas == 0) {
                    throw new SQLException(
                            "No se pudo descontar el stock."
                    );
                }
            }

            PreparedStatement detalleStatement = connection.prepareStatement(INSERTAR_DETALLE);
            detalleStatement.setInt(1, detalle.getIdCuenta());
            detalleStatement.setInt(2, codigoProducto);
            detalleStatement.setInt(3, cantidadProducto);
            detalleStatement.setDouble(4, precio);
            detalleStatement.setDouble(5, subtotal);
           
            int filasDetalle = detalleStatement.executeUpdate();
            
            if (filasDetalle == 0) {
                throw new SQLException(
                        "No se pudo insertar el detalle."
                );
            }
            
            PreparedStatement totalStatement = connection.prepareStatement(CONSULTAR_TOTAL);
            totalStatement.setInt(1, detalle.getIdCuenta());
            ResultSet totalResultado = totalStatement.executeQuery();
            
            double totalActual = 0;
            
            if (totalResultado.next()) {
                totalActual = totalResultado.getDouble("total");
            }

            PreparedStatement actualizarTotal = connection.prepareStatement(ACTUALIZAR_TOTAL);
            actualizarTotal.setDouble(1, totalActual);
            actualizarTotal.setInt(2, detalle.getIdCuenta());
            
            int filasTotal = actualizarTotal.executeUpdate();
            
            if (filasTotal == 0) {
                throw new SQLException(
                        "No se pudo actualizar el total."
                );
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
