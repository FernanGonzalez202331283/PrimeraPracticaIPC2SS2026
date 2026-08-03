/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author fernan
 */
public class ConexionDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/cafeteria";
    private static final String USUARIO = "rootbd";
    private static final String PASSWORD = "Fernan16@2026";
    private Connection conexion;
    
    public Connection Connectar(){
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexion realizada Correctamente");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos.");
            
        }
        return conexion;
    }
}
