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
    public static final String INSERTAR_PRODUCTO =
            """
            INSERT INTO producto
            (nombre, categoria, precio, fotografia)
            VALUES(?,?,?,?)
            """;

    public static final String CONSULTAR_PRODUCTOS = 
            """
            SELECT *
            FROM producto
            ORDER BY codigo_producto
            """;

    public static final String ACTUALIZAR_PRODUCTO = 
            """
            UPDATE producto
            SET nombre = ?,
            categoria = ?,
            precio = ?,
            fotografia = ?
            WHERE codigo_producto = ?
            """;
    
   public void registrarProducto(){

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese nombre del producto:");

    String nombre = scanner.nextLine();

    System.out.println("Ingrese categoría:");
    System.out.println("BEBIDA_CALIENTE");
    System.out.println("BEBIDA_FRIA");
    System.out.println("POSTRE");
    System.out.println("COMIDA");

    String categoria = scanner.nextLine();

    System.out.println("Ingrese precio:");

    double precio = scanner.nextDouble();
    scanner.nextLine();

    System.out.println("Ingrese ruta de la fotografía:");

    String fotografia = scanner.nextLine();


    Producto producto = new Producto(
            0,
            nombre,
            categoria,
            precio,
            fotografia
    );


    try{

        PreparedStatement statement =
                connection.prepareStatement(INSERTAR_PRODUCTO);

        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getCategoria());
        statement.setDouble(3, producto.getPrecio());
        statement.setString(4, producto.getFotografia());

        int filas = statement.executeUpdate();

        if(filas > 0){

            System.out.println("Producto registrado correctamente.");

        }

    }catch(SQLException e){

        e.printStackTrace();

    }

}
   public void listarProductos(){

    try{

        PreparedStatement statement =
                connection.prepareStatement(CONSULTAR_PRODUCTOS);

        ResultSet resultado = statement.executeQuery();

        while(resultado.next()){

            System.out.println("--------------------------------");

            System.out.println(
                    "Código: "
                    + resultado.getInt("codigo_producto")
            );

            System.out.println(
                    "Nombre: "
                    + resultado.getString("nombre")
            );

            System.out.println(
                    "Categoría: "
                    + resultado.getString("categoria")
            );

            System.out.println(
                    "Precio: Q"
                    + resultado.getDouble("precio")
            );

            System.out.println(
                    "Fotografía: "
                    + resultado.getString("fotografia")
            );

        }

    }catch(SQLException e){

        e.printStackTrace();

    }

}
   public void actualizarProducto(){

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese el código del producto:");

    int codigo = scanner.nextInt();
    scanner.nextLine();

    System.out.println("Nuevo nombre:");

    String nombre = scanner.nextLine();

    System.out.println("Nueva categoría:");

    String categoria = scanner.nextLine();

    System.out.println("Nuevo precio:");

    double precio = scanner.nextDouble();
    scanner.nextLine();

    System.out.println("Nueva fotografía:");

    String fotografia = scanner.nextLine();

    try{

        PreparedStatement statement =
                connection.prepareStatement(ACTUALIZAR_PRODUCTO);

        statement.setString(1, nombre);
        statement.setString(2, categoria);
        statement.setDouble(3, precio);
        statement.setString(4, fotografia);
        statement.setInt(5, codigo);

        int filas = statement.executeUpdate();

        if(filas > 0){

            System.out.println("Producto actualizado correctamente.");

        }else{

            System.out.println("No existe un producto con ese código.");

        }

    }catch(SQLException e){

        e.printStackTrace();

    }

}
}

