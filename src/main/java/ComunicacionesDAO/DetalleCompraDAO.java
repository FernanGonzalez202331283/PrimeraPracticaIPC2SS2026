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
public class DetalleCompraDAO {
    
    
    private Connection connection;


    public DetalleCompraDAO(){

        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();

    }


    public static final String CONSULTAR_DETALLE =
    """
    SELECT 
        id_detalle,
        codigo_insumo,
        cantidad,
        precio_unitario,
        subtotal
    FROM detalle_compra
    WHERE id_compra = ?
    """;



    public void listarDetalleCompra(){

        Scanner scanner = new Scanner(System.in);


        System.out.println("Ingrese el ID de la compra:");

        int idCompra = scanner.nextInt();



        try{


            PreparedStatement statement =
                    connection.prepareStatement(CONSULTAR_DETALLE);



            statement.setInt(1, idCompra);



            ResultSet resultado =
                    statement.executeQuery();



            boolean existe = false;



            while(resultado.next()){


                existe = true;


                System.out.println("-------------------------");

                System.out.println(
                    "Código insumo: "
                    + resultado.getInt("codigo_insumo")
                );


                System.out.println(
                    "Cantidad: "
                    + resultado.getDouble("cantidad")
                );


                System.out.println(
                    "Precio unitario: Q"
                    + resultado.getDouble("precio_unitario")
                );


                System.out.println(
                    "Subtotal: Q"
                    + resultado.getDouble("subtotal")
                );

            }



            if(!existe){

                System.out.println(
                    "No existen detalles para esa compra."
                );

            }



        }catch(SQLException e){

            e.printStackTrace();

        }


    }
}
