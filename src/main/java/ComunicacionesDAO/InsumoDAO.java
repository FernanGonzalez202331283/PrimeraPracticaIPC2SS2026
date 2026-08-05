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
import java.sql.Statement;
import java.util.Scanner;
import modelo.Insumo;

/**
 *
 * @author fernan
 */
public class InsumoDAO {
    private Connection connection;
    
    public InsumoDAO(){
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }
    
    
   public static final String INSERTAR_INSUMO =
        """
        INSERT INTO insumo
        (nombre, unidad_medida, stock_actual, stock_minimo, costo)
        VALUES (?, ?, ?, ?, ?)
        """;
    
    public static final String CONSULTAR_INSUMO = 
            "SELECT * FROM insumo";
    
    public static final String ACTUALIZAR_INSUMO =
            """
            UPDATE insumo
            SET nombre = ?,
                unidad_medida = ?,
                stock_actual = ?,
                stock_minimo = ?,
                costo = ?
            WHERE codigo_insumo = ?
            """;
    
     public static final String CONSULTAR_BAJO_STOCK =
            """
            SELECT * FROM insumo
            WHERE stock_actual <= stock_minimo
            """;
    
    public void insertarInsumo(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Ingrese nombre del insumo: ");
        String nombre = scanner.nextLine();
        
        System.out.println("Ingrese unidad de medida");
        String unidad = scanner.nextLine();
        
        System.out.println("Ingrese stock actual: ");
        double stockActual = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.println("Ingrese stock minimo: ");
        double stockMinimo = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Ingrese costo: ");
        double costo = scanner.nextDouble();
        scanner.nextLine();
        
        Insumo insumo = new Insumo(
                nombre,
                unidad,
                stockActual,
                stockMinimo,
                costo
        );
        
       try {

            PreparedStatement statement =
                    connection.prepareStatement(INSERTAR_INSUMO);

            statement.setString(1, insumo.getNombre());
            statement.setString(2, insumo.getUnidadMedida());
            statement.setDouble(3, insumo.getStockActual());
            statement.setDouble(4, insumo.getStockMinimo());
            statement.setDouble(5, insumo.getCosto());

            int filas = statement.executeUpdate();

            System.out.println("Insumo registrado correctamente.");
            System.out.println("Filas insertadas: " + filas);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void listarInsumos(){
    try{
        Statement consulta = connection.createStatement();
        ResultSet resultado = consulta.executeQuery(CONSULTAR_INSUMO); 
        while(resultado.next()){
            System.out.println("--------------------------");
            System.out.println(
                "Codigo: "
                + resultado.getInt("codigo_insumo")
            );
            System.out.println(
                "Nombre: "
                + resultado.getString("nombre")
            );
            System.out.println(
                "Unidad: "
                + resultado.getString("unidad_medida")
            );
            System.out.println(
                "Stock actual: "
                + resultado.getDouble("stock_actual")
            );
            System.out.println(
                "Stock minimo: "
                + resultado.getDouble("stock_minimo")
            );
            System.out.println(
                "Costo: "
                + resultado.getDouble("costo")
            );
        }
    }catch(SQLException e){

        e.printStackTrace();

    }
}
    
    public void listarBajoStock(){

    try{

        Statement consulta = connection.createStatement();


        ResultSet resultado =
                consulta.executeQuery(CONSULTAR_BAJO_STOCK);


        while(resultado.next()){


            System.out.println("ALERTA: STOCK BAJO");


            System.out.println(
                resultado.getString("nombre")
            );


            System.out.println(
                "Actual: "
                + resultado.getDouble("stock_actual")
            );


            System.out.println(
                "Minimo: "
                + resultado.getDouble("stock_minimo")
            );


        }


    }catch(SQLException e){

        e.printStackTrace();

    }

}
    
    public void actualizarInsumo() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese el código del insumo:");
    int codigo = scanner.nextInt();
    scanner.nextLine();

    System.out.println("Ingrese el nuevo nombre:");
    String nombre = scanner.nextLine();

    System.out.println("Ingrese la nueva unidad de medida:");
    String unidad = scanner.nextLine();

    System.out.println("Ingrese el nuevo stock actual:");
    double stockActual = scanner.nextDouble();

    System.out.println("Ingrese el nuevo stock mínimo:");
    double stockMinimo = scanner.nextDouble();

    System.out.println("Ingrese el nuevo costo:");
    double costo = scanner.nextDouble();
    try {

    PreparedStatement statement =
            connection.prepareStatement(ACTUALIZAR_INSUMO);

    statement.setString(1, nombre);
    statement.setString(2, unidad);
    statement.setDouble(3, stockActual);
    statement.setDouble(4, stockMinimo);
    statement.setDouble(5, costo);
    statement.setInt(6, codigo);

    int filas = statement.executeUpdate();

    if (filas > 0) {
        System.out.println("Insumo actualizado correctamente.");
    } else {
        System.out.println("No existe un insumo con ese código.");
    }

} catch (SQLException e) {
    e.printStackTrace();
}

}
}

