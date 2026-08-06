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
import modelo.Receta;

/**
 *
 * @author fernan
 */
public class RecetaDAO {
    private Connection connection;

    public RecetaDAO(){

        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();

    }
    
    public static final String INSERTAR_RECETA =
            """
            INSERT INTO receta
            (codigo_producto,codigo_insumo,cantidad)
            VALUES(?,?,?)
            """;


    public static final String CONSULTAR_RECETAS =
            """
            SELECT *
            FROM receta
            ORDER BY codigo_producto
            """;


    public static final String ACTUALIZAR_RECETA =
            """
            UPDATE receta
            SET codigo_insumo = ?,
            cantidad = ?
            WHERE id_receta = ?
            """;
    
    public void registrarReceta(){

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese código del producto:");

    int codigoProducto = scanner.nextInt();

    System.out.println("Ingrese código del insumo:");

    int codigoInsumo = scanner.nextInt();

    System.out.println("Ingrese cantidad necesaria:");

    double cantidad = scanner.nextDouble();

    Receta receta = new Receta(
            0,
            codigoProducto,
            codigoInsumo,
            cantidad
    );

    try{

        PreparedStatement statement =
                connection.prepareStatement(INSERTAR_RECETA);

        statement.setInt(1, receta.getCodigoProducto());
        statement.setInt(2, receta.getCodigoInsumo());
        statement.setDouble(3, receta.getCantidad());

        int filas = statement.executeUpdate();

        if(filas > 0){

            System.out.println("Receta registrada correctamente.");

        }

    }catch(SQLException e){

        e.printStackTrace();

    }

}
    public void listarRecetas(){

    try{

        PreparedStatement statement =
                connection.prepareStatement(CONSULTAR_RECETAS);

        ResultSet resultado = statement.executeQuery();

        while(resultado.next()){

            System.out.println("---------------------------");

            System.out.println(
                    "ID Receta: "
                    + resultado.getInt("id_receta")
            );

            System.out.println(
                    "Producto: "
                    + resultado.getInt("codigo_producto")
            );

            System.out.println(
                    "Insumo: "
                    + resultado.getInt("codigo_insumo")
            );

            System.out.println(
                    "Cantidad: "
                    + resultado.getDouble("cantidad")
            );

        }

    }catch(SQLException e){

        e.printStackTrace();

    }

}
    public void actualizarReceta(){

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese ID de la receta:");

    int id = scanner.nextInt();

    System.out.println("Nuevo código de insumo:");

    int codigoInsumo = scanner.nextInt();

    System.out.println("Nueva cantidad:");

    double cantidad = scanner.nextDouble();

    try{

        PreparedStatement statement =
                connection.prepareStatement(ACTUALIZAR_RECETA);

        statement.setInt(1, codigoInsumo);
        statement.setDouble(2, cantidad);
        statement.setInt(3, id);

        int filas = statement.executeUpdate();

        if(filas > 0){

            System.out.println("Receta actualizada correctamente.");

        }else{

            System.out.println("No existe una receta con ese ID.");

        }

    }catch(SQLException e){

        e.printStackTrace();

    }

}
}
