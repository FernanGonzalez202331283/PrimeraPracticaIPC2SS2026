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
/**
 *
 * @author fernan
 */
public class CompraInsumoDAO {

    private Connection connection;

    public CompraInsumoDAO() {
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }

    public static final String INSERTAR_COMPRA
            = """
            INSERT INTO compra_insumo
            (fecha,total)
            VALUES (?,?)
            """;
    
    public static final String INSERTAR_DETALLE
            = """
            INSERT INTO detalle_compra
            (id_compra,codigo_insumo,cantidad,precio_unitario,subtotal)
            VALUES(?,?,?,?,?)
            """;

    public static final String ACTUALIZAR_STOCK
            = """
            UPDATE insumo
            SET stock_actual = stock_actual + ?
            WHERE codigo_insumo = ?
            """;

    public static final String ACTUALIZAR_TOTAL
            = """
            UPDATE compra_insumo
            SET total = ?
            WHERE id_compra = ?
            """;

    public static final String CONSULTAR_COMPRAS
            = """
            SELECT *
            FROM compra_insumo
            ORDER BY id_compra
            """;

    public boolean registrarCompra(String fecha,double total,int cantidadFilas,int[] codigos,
            double[] cantidades,
            double[] precios,
            double[] subtotales) {
        try {
            connection.setAutoCommit(false);
            // Registrar compra
            PreparedStatement insertarCompra = connection.prepareStatement(INSERTAR_COMPRA);
            insertarCompra.setString(1, fecha);
            insertarCompra.setDouble(2, 0);
            int filasCompra = insertarCompra.executeUpdate();
            if (filasCompra == 0) {
                connection.rollback();
                return false;
            }
            
            int idCompra = obtenerUltimoIdCompra();
            if (idCompra == -1) {
                connection.rollback();
                return false;
            }
            for (int i = 0; i < cantidadFilas; i++) {
                PreparedStatement detalle = connection.prepareStatement( INSERTAR_DETALLE);
                detalle.setInt(1, idCompra);
                detalle.setInt(2, codigos[i]);
                detalle.setDouble(3, cantidades[i]);
                detalle.setDouble(4, precios[i]);
                detalle.setDouble(5, subtotales[i]);
                int filasDetalle = detalle.executeUpdate();
                if (filasDetalle == 0) {
                    connection.rollback();
                    return false;
                }
                
                PreparedStatement stock = connection.prepareStatement(ACTUALIZAR_STOCK);
                stock.setDouble(1, cantidades[i]);
                stock.setInt(2, codigos[i]);
                int filasStock= stock.executeUpdate();
                if (filasStock == 0) {
                    connection.rollback();
                    return false;
                }
            }
            
            PreparedStatement actualizarTotal = connection.prepareStatement(ACTUALIZAR_TOTAL);
            actualizarTotal.setDouble(1, total);
            actualizarTotal.setInt(2, idCompra);
            int filasTotal = actualizarTotal.executeUpdate();
            if (filasTotal == 0) {
                connection.rollback();
                return false;
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
    
    public int obtenerUltimoIdCompra() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                            "SELECT MAX(id_compra) AS id FROM compra_insumo"
                    );
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                int id = resultado.getInt("id");
                if (resultado.wasNull()) {
                    return -1;
                }
                return id;
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return -1;
    }
}
