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
import modelo.Receta;

/**
 *
 * @author fernan
 */
public class RecetaDAO {

    private Connection connection;

    public RecetaDAO() {

        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();

    }

    public static final String INSERTAR_RECETA
            = """
            INSERT INTO receta
            (codigo_producto,codigo_insumo,cantidad)
            VALUES(?,?,?)
            """;

    public static final String CONSULTAR_RECETAS
            = """
            SELECT
                r.id_receta,
                p.nombre AS producto,
                i.nombre AS insumo,
                r.cantidad
            FROM receta r
            INNER JOIN producto p
                ON r.codigo_producto = p.codigo_producto
            INNER JOIN insumo i
                ON r.codigo_insumo = i.codigo_insumo
            ORDER BY r.id_receta
            """;

    public static final String VERIFICAR_RECETA
            = """
            SELECT id_receta
            FROM receta
            WHERE codigo_producto = ?
            AND codigo_insumo = ?
            """;

    public static final String CONSULTAR_RECETA_PRODUCTO
            = """
            SELECT codigo_insumo, cantidad
            FROM receta
            WHERE codigo_producto = ?
            """;

    public static final String ACTUALIZAR_RECETA
            = """
            UPDATE receta
            SET codigo_producto = ?,
                codigo_insumo = ?,
                cantidad = ?
            WHERE id_receta = ?
            """;

    public static final String ELIMINAR_RECETA
            = """
            DELETE FROM receta
            WHERE id_receta = ?
            """;

    public boolean insertarReceta(Receta receta) {
        try {
            PreparedStatement statement = connection.prepareStatement( INSERTAR_RECETA);
            statement.setInt( 1, receta.getCodigoProducto());
            statement.setInt( 2, receta.getCodigoInsumo());
            statement.setDouble( 3, receta.getCantidad());

            int filas = statement.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    public ResultSet consultarRecetas() {

        try {

            PreparedStatement statement = connection.prepareStatement( CONSULTAR_RECETAS);
            return statement.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
        }
    }

    public boolean actualizarReceta(Receta receta) {
        try {
            PreparedStatement statement = connection.prepareStatement( ACTUALIZAR_RECETA );
            statement.setInt(1, receta.getCodigoProducto());
            statement.setInt( 2,receta.getCodigoInsumo());
            statement.setDouble( 3, receta.getCantidad());
            statement.setInt( 4, receta.getIdReceta());

            int filas = statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean existeReceta(int codigoProducto, int codigoInsumo) {

        try {

            PreparedStatement statement = connection.prepareStatement(VERIFICAR_RECETA);

            statement.setInt(1, codigoProducto);
            statement.setInt(2, codigoInsumo);

            ResultSet resultado
                    = statement.executeQuery();

            return resultado.next();

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarReceta(int idReceta) {

        try {
            PreparedStatement statement = connection.prepareStatement(ELIMINAR_RECETA);
            statement.setInt(1, idReceta);

            int filas
                    = statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    public ResultSet consultarRecetaProducto(int codigoProducto) {

        try {
            PreparedStatement statement = connection.prepareStatement( CONSULTAR_RECETA_PRODUCTO );
            statement.setInt(1, codigoProducto);
            return statement.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
        }
    }

}
