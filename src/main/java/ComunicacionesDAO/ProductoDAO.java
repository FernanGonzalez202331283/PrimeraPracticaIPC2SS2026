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
import modelo.Producto;

/**
 *
 * @author fernan
 */
public class ProductoDAO {

    private Connection connection;

    public ProductoDAO() {
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }

    //querys
    public static final String INSERTAR_PRODUCTO
            = """
            INSERT INTO producto
            (nombre, categoria, precio, fotografia)
            VALUES (?, ?, ?, ?)
            """;

    public static final String CONSULTAR_PRODUCTO
            = """
            SELECT *
            FROM producto
            ORDER BY codigo_producto
            """;

    public static final String ACTUALIZAR_PRODUCTO
            = """
            UPDATE producto
            SET nombre = ?,
                categoria = ?,
                precio = ?,
                fotografia = ?
            WHERE codigo_producto = ?
            """;

    public boolean insertarProducto(Producto producto) {
        try {
            PreparedStatement statement = connection.prepareStatement( INSERTAR_PRODUCTO );
            statement.setString( 1,producto.getNombre());
            statement.setString( 2, producto.getCategoria());
            statement.setDouble( 3, producto.getPrecio());
            statement.setString( 4, producto.getFotografia());
            int filas = statement.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    public ResultSet consultarProductos() {
        try {
            PreparedStatement statement
                    = connection.prepareStatement(CONSULTAR_PRODUCTO);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public boolean actualizarProducto(Producto producto) {

        try {
            PreparedStatement statement = connection.prepareStatement( ACTUALIZAR_PRODUCTO);
            statement.setString(1,producto.getNombre());
            statement.setString( 2, producto.getCategoria());
            statement.setDouble( 3, producto.getPrecio());
            statement.setString( 4, producto.getFotografia());
            statement.setInt( 5, producto.getCodigoProducto());
            int filas = statement.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}
