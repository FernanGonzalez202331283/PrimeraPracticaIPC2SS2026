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
import modelo.Cuenta;

/**
 *
 * @author fernan
 */
public class CuentaDAO {

    private Connection connection;

    public CuentaDAO() {

        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }

    public static final String INSERTAR_CUENTA
            = """
              INSERT INTO cuenta
              (numero_mesa,dpi_mesero,fecha,hora_ocupacion,
              hora_liberacion,estado,propina,total)
              VALUES(?,?,?,?,?,?,?,?)
              """;

    public static final String CONSULTAR_CUENTAS
            = """
              SELECT *
              FROM cuenta
              ORDER BY id_cuenta
              """;

    public static final String VERIFICAR_MESA
            = """
              SELECT estado
              FROM mesa
              WHERE numero_mesa = ?
              """;

    public static final String OCUPAR_MESA
            = """
              UPDATE mesa
              SET estado = 'OCUPADA'
              WHERE numero_mesa = ?
              """;

    public static final String CONSULTAR_MESAS_LIBRES
            = """
              SELECT numero_mesa
              FROM mesa
              WHERE estado = 'LIBRE'
              ORDER BY numero_mesa
              """;

    public static final String PAGAR_CUENTA
            = """
              UPDATE cuenta
              SET estado = 'PAGADA',
              hora_liberacion = ?,
              propina = ?,
              total = ?
              WHERE id_cuenta = ?
              """;

    public static final String LIBERAR_MESA
            = """
            UPDATE mesa
            SET estado = 'LIBRE'
            WHERE numero_mesa = ?
            """;

    public static final String OBTENER_NUMERO_MESA
            = """
            SELECT numero_mesa
            FROM cuenta
            WHERE id_cuenta = ?
            """;

    public static final String CONSULTAR_MESEROS
            = """
            SELECT dpi, nombre
            FROM empleado
            WHERE rol = 'MESERO'
            AND estado = TRUE
            ORDER BY nombre
            """;
    
    public boolean pagarCuenta(int idCuenta, String horaLiberacion,double propina,double total) {
        try {
            connection.setAutoCommit(false);
            // Obtener la mesa de la cuenta
            PreparedStatement obtenerMesa = connection.prepareStatement(OBTENER_NUMERO_MESA);
            obtenerMesa.setInt(1, idCuenta);
            ResultSet resultado = obtenerMesa.executeQuery();
            if (!resultado.next()) {
                connection.rollback();
                return false;
            }
            int numeroMesa = resultado.getInt("numero_mesa");
            // Actualizar la cuenta
            PreparedStatement pagarCuenta = connection.prepareStatement(PAGAR_CUENTA);
            pagarCuenta.setString(1, horaLiberacion);
            pagarCuenta.setDouble(2,propina);
            pagarCuenta.setDouble(3,total);
            pagarCuenta.setInt(4,idCuenta);
            int filasCuenta = pagarCuenta.executeUpdate();
            if (filasCuenta == 0) {
                connection.rollback();
                return false;
            }
            // Liberar la mesa
            PreparedStatement liberarMesa = connection.prepareStatement(LIBERAR_MESA);
            liberarMesa.setInt(1,numeroMesa);
            int filasMesa = liberarMesa.executeUpdate();
            if (filasMesa == 0) {
                connection.rollback();
                return false;
            }
            // Confirmar operaciones
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet consultarMesasLibres() {

        try {

            PreparedStatement statement = connection.prepareStatement(CONSULTAR_MESAS_LIBRES);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet consultarMeseros() {
        try {
            PreparedStatement statement = connection.prepareStatement( CONSULTAR_MESEROS);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int insertarCuenta(Cuenta cuenta) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(INSERTAR_CUENTA,
            java.sql.Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,cuenta.getNumeroMesa());
            statement.setString(2,cuenta.getDpiMesero());
            statement.setString(3,cuenta.getFecha());
            statement.setString(4,cuenta.getHoraOcupacion());
            statement.setString(5, cuenta.getHoraLiberacion());
            statement.setString(6,cuenta.getEstado());
            statement.setDouble(7,cuenta.getPropina());
            statement.setDouble(8,cuenta.getTotal());
            int filasCuenta = statement.executeUpdate();
            if (filasCuenta == 0) {
                connection.rollback();
                return 0;
            }
            ResultSet claves = statement.getGeneratedKeys();
            if (!claves.next()) {
                connection.rollback();
                return 0;
            }
            int idCuenta = claves.getInt(1);
            PreparedStatement ocuparMesa = connection.prepareStatement(OCUPAR_MESA);
            ocuparMesa.setInt(1, cuenta.getNumeroMesa());
            int filasMesa = ocuparMesa.executeUpdate();
            if (filasMesa == 0) {
                connection.rollback();
                return 0;
            }
            connection.commit();
            return idCuenta;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean abrirCuenta(Cuenta cuenta) {
        try {
            connection.setAutoCommit(false);
            String verificarMesa = """
                SELECT estado
                FROM mesa
                WHERE numero_mesa = ?
                """;
            PreparedStatement consultaMesa = connection.prepareStatement(verificarMesa);
            consultaMesa.setInt(1, cuenta.getNumeroMesa());
            ResultSet resultado = consultaMesa.executeQuery();
            if (!resultado.next()) {
                connection.rollback();
                return false;
            }
            String estadoMesa = resultado.getString("estado");
            if (!estadoMesa.equals("LIBRE")) {
                connection.rollback();
                return false;
            }
            PreparedStatement insertar = connection.prepareStatement(INSERTAR_CUENTA);
            insertar.setInt(1, cuenta.getNumeroMesa());
            insertar.setString(2, cuenta.getDpiMesero());
            insertar.setString(3, cuenta.getFecha());
            insertar.setString(4, cuenta.getHoraOcupacion());
            insertar.setNull(5, java.sql.Types.TIME);
            insertar.setString(6, "ABIERTA");
            insertar.setDouble(7, 0);
            insertar.setDouble(8, 0);
            int filasCuenta = insertar.executeUpdate();
            if (filasCuenta == 0) {
                connection.rollback();
                return false;
            }
            String ocuparMesa
                    = """
                    UPDATE mesa
                    SET estado = 'OCUPADA'
                    WHERE numero_mesa = ?
                    """;
            PreparedStatement actualizarMesa = connection.prepareStatement(ocuparMesa);
            actualizarMesa.setInt(1, cuenta.getNumeroMesa());
            int filasMesa = actualizarMesa.executeUpdate();
            if (filasMesa == 0) {
                connection.rollback();
                return false;
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet consultarCuentas() {
        try {
            PreparedStatement statement = connection.prepareStatement( CONSULTAR_CUENTAS);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
