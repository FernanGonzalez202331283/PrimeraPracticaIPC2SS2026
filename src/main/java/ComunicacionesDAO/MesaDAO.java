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
import modelo.Mesa;

/**
 *
 * @author fernan
 */
public class MesaDAO {
    
     private Connection connection;
    
    public MesaDAO(){
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }
    public static final String INSERTAR_MESA =
            """
            INSERT INTO mesa
            (numero_mesa, capacidad, estado)
            VALUES (?, ?, ?)
            """;

    public static final String CONSULTAR_MESAS =
            """
            SELECT *
            FROM mesa
            ORDER BY numero_mesa
            """;

    public static final String CAMBIAR_ESTADO =
            """
            UPDATE mesa
            SET estado = ?
            WHERE numero_mesa = ?
            """;

    public static final String ACTUALIZAR_MESA =
            """
            UPDATE mesa
            SET capacidad = ?
            WHERE numero_mesa = ?
            """;
    
    public boolean insertarMesa(Mesa mesa) {
        try {
            PreparedStatement statement =  connection.prepareStatement( INSERTAR_MESA);
            statement.setInt(1, mesa.getNumeroMesa());
            statement.setInt( 2, mesa.getCapacidad());
            statement.setString( 3, mesa.getEstado());
            int filas = statement.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet consultarMesas() {
        try {

            PreparedStatement statement = connection.prepareStatement( CONSULTAR_MESAS );
            return statement.executeQuery();
        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }

    public boolean cambiarEstado(
            int numeroMesa,
            String estado) {
        try {
            PreparedStatement statement = connection.prepareStatement( CAMBIAR_ESTADO);
            statement.setString(1, estado);
            statement.setInt(2, numeroMesa);

            int filas = statement.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarMesa(Mesa mesa) {
        try {
            PreparedStatement statement = connection.prepareStatement( ACTUALIZAR_MESA);
            statement.setInt(1, mesa.getCapacidad());
            statement.setInt(2, mesa.getNumeroMesa());

            int filas = statement.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}
