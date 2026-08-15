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
public class ReporteDAO {

    private Connection connection;

    public ReporteDAO() {
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }

    public static final String INGRESOS
            = """
            SELECT COALESCE(SUM(total), 0) AS ingresos
            FROM cuenta
            WHERE estado = 'PAGADA'
            AND fecha BETWEEN ? AND ?
            """;

    public static final String EGRESOS_COMPRAS
            = """
            SELECT COALESCE(SUM(total), 0) AS egresos_compras
            FROM compra_insumo
            WHERE fecha BETWEEN ? AND ?
            """;

    public static final String EGRESOS_NOMINAS
            = """
            SELECT COALESCE(SUM(monto), 0) AS egresos_nominas
            FROM nomina
            WHERE estado_pago = 'PAGADO'
            AND fecha_emision BETWEEN ? AND ?
            """;

    public static final String REPORTE_FLUJO_CAJA
            = """
            SELECT
                COALESCE(
                    (
                        SELECT SUM(total)
                        FROM cuenta
                        WHERE estado = 'PAGADA'
                        AND fecha BETWEEN ? AND ?
                    ),
                    0
                ) AS ingresos,
                COALESCE(
                    (
                        SELECT SUM(total)
                        FROM compra_insumo
                        WHERE fecha BETWEEN ? AND ?
                    ),
                    0
                ) AS egresos_compras,
                COALESCE(
                    (
                        SELECT SUM(monto)
                        FROM nomina
                        WHERE estado_pago = 'PAGADO'
                        AND fecha_emision BETWEEN ? AND ?
                    ),
                    0
                ) AS egresos_nominas
            """;

    public static final String PRODUCTOS_MAS_VENDIDOS
            = """
            SELECT
                p.codigo_producto,
                p.nombre,
                SUM(dc.cantidad) AS cantidad_vendida,
                SUM(dc.subtotal) AS total_vendido
            FROM detalle_cuenta dc
            INNER JOIN cuenta c
                ON dc.id_cuenta = c.id_cuenta
            INNER JOIN producto p
                ON dc.codigo_producto = p.codigo_producto
            WHERE c.estado = 'PAGADA'
            AND c.fecha BETWEEN ? AND ?
            GROUP BY
                p.codigo_producto,
                p.nombre
            ORDER BY
                cantidad_vendida DESC
            """;

    public static final String INSUMOS_BAJO_STOCK
            = """
            SELECT
                codigo_insumo,
                nombre,
                unidad_medida,
                stock_actual,
                stock_minimo,
                costo
            FROM insumo
            WHERE stock_actual <= stock_minimo
            ORDER BY stock_actual ASC
            """;

    public ResultSet reporteFlujoCaja(
            java.sql.Date fechaInicial,
            java.sql.Date fechaFinal) {

        try {

            PreparedStatement statement = connection.prepareStatement( REPORTE_FLUJO_CAJA);
            statement.setDate(1, fechaInicial);
            statement.setDate(2, fechaFinal);
            statement.setDate(3, fechaInicial);
            statement.setDate(4, fechaFinal);
            statement.setDate(5, fechaInicial);
            statement.setDate(6, fechaFinal);

            return statement.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
        }
    }

    public ResultSet reporteProductosMasVendidos(
            java.sql.Date fechaInicial,
            java.sql.Date fechaFinal) {

        try {

            PreparedStatement statement = connection.prepareStatement( PRODUCTOS_MAS_VENDIDOS);
            statement.setDate(1, fechaInicial);
            statement.setDate(2, fechaFinal);
            return statement.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
        }
    }

    public ResultSet reporteInsumosBajoStock() {
        try {

            PreparedStatement statement = connection.prepareStatement( INSUMOS_BAJO_STOCK);
            return statement.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
        }
    }
}
