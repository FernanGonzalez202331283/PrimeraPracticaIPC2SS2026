/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComunicacionesDAO;

import conexion.ConexionDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    
    public void RegistrarProducto(){
        Scanner scanner  = new Scanner(System.in);
        
        System.out.println("Ingrese nombre del producto: ");
        String nombre = scanner.nextLine();
        
        System.out.println("Ingrese Categoria: ");
        System.out.println("BEBIDA_CALIENTE");
        System.out.println("BEBIDA_FRIA");
        System.out.println("POSTRE");
        System.out.println("COMIDA");
        
        String categoria = scanner.nextLine();
        
        System.out.println("ingrese precio: ");
        double precio = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.println("ingrese Fotografia: ");
        String fotografia = scanner.nextLine();
        
        Producto producto = new Producto(
          0, nombre, categoria, precio, fotografia
        );
        try {
            PreparedStatement statement = connection.prepareStatement(INSERTAR_PRODUCTO);
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getCategoria());
            statement.setDouble(3, producto.getPrecio());
            statement.setString(4, producto.getFotografia());
        } catch ( e) {
        }
    }
}

