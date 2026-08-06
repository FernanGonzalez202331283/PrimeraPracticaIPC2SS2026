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
public class CompraInsumoDAO {
    private Connection connection;
    public CompraInsumoDAO(){
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }
    
   public static final String INSERTAR_COMPRA =
            """
            INSERT INTO compra_insumo
            (fecha,total)
            VALUES (?,0)
            """;

    public static final String INSERTAR_DETALLE =
            """
            INSERT INTO detalle_compra
            (id_compra,codigo_insumo,cantidad,precio_unitario,subtotal)
            VALUES(?,?,?,?,?)
            """;

    public static final String ACTUALIZAR_STOCK =
        """
        UPDATE insumo
        SET stock_actual = stock_actual + ?
        WHERE codigo_insumo = ?
        """;

        public static final String ACTUALIZAR_TOTAL =
        """
        UPDATE compra_insumo
        SET total = ?
        WHERE id_compra = ?
        """;

    public static final String CONSULTAR_COMPRAS =
        """
        SELECT *
        FROM compra_insumo
        ORDER BY id_compra
        """;
    
   public void registrarCompra() {

    Scanner scanner = new Scanner(System.in);

    try {
        connection.setAutoCommit(false);
        System.out.println("Ingrese la fecha de la compra (AAAA-MM-DD):");
        String fecha = scanner.nextLine();
        PreparedStatement insertarCompra =
                connection.prepareStatement(INSERTAR_COMPRA);
        insertarCompra.setString(1, fecha);
        insertarCompra.executeUpdate();
        int idCompra = obtenerUltimoIdCompra();


        if(idCompra == -1){

            throw new SQLException(
                    "No se pudo obtener el codigo de compra"
            );

        }
        System.out.println("¿Cuantos insumos desea registrar?");

        int cantidadInsumos = scanner.nextInt();
        double totalCompra = 0;
        for(int i = 1; i <= cantidadInsumos; i++){
            System.out.println("---------------------------");
            System.out.println("Insumo número: " + i);
            System.out.println("Ingrese código del insumo:");
            int codigoInsumo = scanner.nextInt();
            System.out.println("Ingrese cantidad:");
            double cantidad = scanner.nextDouble();
            System.out.println("Ingrese precio unitario:");
            double precio = scanner.nextDouble();
            double subtotal = cantidad * precio;
            totalCompra += subtotal;
            PreparedStatement insertarDetalle =
                    connection.prepareStatement(INSERTAR_DETALLE);
            insertarDetalle.setInt(1, idCompra);
            insertarDetalle.setInt(2, codigoInsumo);
            insertarDetalle.setDouble(3, cantidad);
            insertarDetalle.setDouble(4, precio);
            insertarDetalle.setDouble(5, subtotal);
            insertarDetalle.executeUpdate();
            PreparedStatement actualizarStock =
                    connection.prepareStatement(ACTUALIZAR_STOCK);
            actualizarStock.setDouble(1, cantidad);
            actualizarStock.setInt(2, codigoInsumo);
            actualizarStock.executeUpdate();
        }
        PreparedStatement actualizarTotal =
                connection.prepareStatement(ACTUALIZAR_TOTAL);
        actualizarTotal.setDouble(1, totalCompra);
        actualizarTotal.setInt(2, idCompra);
        actualizarTotal.executeUpdate();
        connection.commit();
        System.out.println(
                "Compra registrada correctamente."
        );
        System.out.println(
                "Total de la compra: Q" + totalCompra
        );
    } catch(SQLException e){
        try {
            connection.rollback();
            System.out.println(
                    "Error, la compra fue cancelada."
            );
        } catch(SQLException ex){
            ex.printStackTrace();

        }
        e.printStackTrace();
    } finally {
        try {
            connection.setAutoCommit(true);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}

    public void listarCompras() {

        try {

            PreparedStatement statement =
                    connection.prepareStatement(CONSULTAR_COMPRAS);

            ResultSet resultado = statement.executeQuery();

            while (resultado.next()) {

                System.out.println("----------------------------");
                System.out.println("ID Compra: "
                        + resultado.getInt("id_compra"));
                System.out.println("Fecha: "
                        + resultado.getDate("fecha"));
                System.out.println("Total: "
                        + resultado.getDouble("total"));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

   public int obtenerUltimoIdCompra() {

    try {

        PreparedStatement statement =
                connection.prepareStatement(
                "SELECT MAX(id_compra) AS id FROM compra_insumo"
                );

        ResultSet resultado = statement.executeQuery();

        if(resultado.next()){

            int id = resultado.getInt("id");

            if(resultado.wasNull()){

                return -1;

            }

            return id;
        }

    } catch(SQLException e){

        e.printStackTrace();

    }

    return -1;
}
}
