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
import java.time.LocalDate;

/**
 *
 * @author fernan
 */
public class NominaDAO {

    private Connection connection;

    public NominaDAO() {
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }

    public static final String INSERTAR_NOMINA
            = """
            INSERT INTO nomina
            (dpi_empleado, fecha_emision, tipo_de_pago, estado_pago, monto)
            VALUES (?, ?, ?, ?, ?)
            """;

    public static final String CONSULTAR_NOMINAS
            = """
            SELECT * FROM nomina
            """;

    public static final String PAGAR_NOMINA
            = """
            UPDATE nomina
            SET estado_pago = 'PAGADO'
            WHERE codigo_nomina = ?
            """;

    public static final String EMPLEADOS_ACTIVOS
            = """
            SELECT dpi, salario, fecha_contratacion
            FROM empleado
            WHERE estado = TRUE
            """;

    public static final String EXISTE_NOMINA
            = """
            SELECT COUNT(*)
            FROM nomina
            WHERE dpi_empleado = ?
            AND fecha_emision = ?
            AND tipo_de_pago = ?
            """;

    public static final String OBTENER_SALARIO
            = """
            SELECT salario
            FROM empleado
            WHERE dpi = ?
            AND estado = TRUE
            """;

    public static final String CONSULTAR_PROPINAS
            = """
            SELECT COALESCE(SUM(propina), 0) AS total_propinas
            FROM cuenta
            WHERE dpi_mesero = ?
            AND estado = 'PAGADA'
            AND fecha BETWEEN ? AND ?
            """;
    public static final String ACTUALIZAR_NOMINA_PENDIENTE
            = """
            UPDATE nomina
            SET monto = ?
            WHERE dpi_empleado = ?
            AND fecha_emision = ?
            AND tipo_de_pago = ?
            AND estado_pago = 'PENDIENTE'
            """;

    public boolean registrarNomina(
            String dpi,
            java.sql.Date fecha,
            String tipoPago,
            double monto) {

        try {

            PreparedStatement statement
                    = connection.prepareStatement(INSERTAR_NOMINA);

            statement.setString(1, dpi);
            statement.setDate(2, fecha);
            statement.setString(3, tipoPago);
            statement.setString(4, "PENDIENTE");
            statement.setDouble(5, monto);

            int filas
                    = statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    public boolean pagarNomina(int codigoNomina) {
        try {
            PreparedStatement statement = connection.prepareStatement( PAGAR_NOMINA);
            statement.setInt(1, codigoNomina);
            int filas = statement.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void generarNominasAutomaticamente() {
        LocalDate hoy = LocalDate.now();
        int dia = hoy.getDayOfMonth();

        String tipoPago;
        double porcentaje;

        LocalDate fechaInicio;
        LocalDate fechaFin;
        // QUINCENA
        if (dia == 10) {

            tipoPago = "QUINCENA";
            porcentaje = 0.30;

            fechaInicio = hoy.withDayOfMonth(1);
            fechaFin = hoy.withDayOfMonth(15);
            // FIN DE MES
        } else if (dia == hoy.lengthOfMonth() - 5) {
            tipoPago = "FIN_DE_MES";
            porcentaje = 0.70;

            fechaInicio = hoy.withDayOfMonth(16);
            fechaFin = hoy.withDayOfMonth(
                    hoy.lengthOfMonth()
            );

        } else {

            return;
        }

        try {
            ResultSet empleados = consultarEmpleadosActivos();
            
            int generadas = 0;
            int actualizadas = 0;

            while (empleados.next()) {

                String dpi = empleados.getString("dpi");

                double salario = empleados.getDouble("salario");
                // FECHA DE CONTRATACIÓN
                
                LocalDate fechaContratacion = empleados.getDate( "fecha_contratacion").toLocalDate();
                // No generar nómina antes
                
                if (fechaContratacion.isAfter(hoy)) {
                    continue;
                }
                java.sql.Date fechaEmision = java.sql.Date.valueOf(hoy);
                
                // CONSULTAR PROPINAS
                
                double propinas = consultarPropinas(
                                dpi,
                                fechaInicio,
                                fechaFin
                        );

                // calcular monto
                
                double monto = (salario * porcentaje)+ propinas;
                // verifica si ya existe momina
                if (existeNomina(
                        dpi,
                        fechaEmision,
                        tipoPago)) {

                    // si esta se actualiza solo si esta pendiente
                    boolean actualizada
                            = actualizarNominaPendiente(
                                    dpi,
                                    fechaEmision,
                                    tipoPago,
                                    monto
                            );
                    if (actualizada) {
                        actualizadas++;
                    }
                } else {
                    // CREAR NUEVA NÓMINA
                    boolean registrada = registrarNomina(
                                    dpi,
                                    fechaEmision,
                                    tipoPago,
                                    monto
                            );
                    if (registrada) {
                        generadas++;
                    }
                }
            }
            System.out.println(
                    "Nóminas nuevas generadas: "
                    + generadas
            );
            System.out.println(
                    "Nóminas pendientes actualizadas: "
                    + actualizadas
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet consultarNominas() {
        try {
            PreparedStatement statement = connection.prepareStatement( CONSULTAR_NOMINAS);
            return statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public ResultSet consultarEmpleadosActivos() {
        try {
            PreparedStatement statement = connection.prepareStatement( EMPLEADOS_ACTIVOS);

            return statement.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
        }
    }

    public double obtenerSalario(String dpi) {
        try {
            PreparedStatement statement = connection.prepareStatement(OBTENER_SALARIO);
            statement.setString(1, dpi);
            ResultSet resultado
                    = statement.executeQuery();

            if (resultado.next()) {
                return resultado.getDouble("salario");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return -1;
    }

    public boolean existeNomina(
            String dpi,
            java.sql.Date fecha,
            String tipoPago) {

        try {

            PreparedStatement statement
                    = connection.prepareStatement(EXISTE_NOMINA);

            statement.setString(1, dpi);
            statement.setDate(2, fecha);
            statement.setString(3, tipoPago);

            ResultSet resultado
                    = statement.executeQuery();

            if (resultado.next()) {

                return resultado.getInt(1) > 0;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

    public double consultarPropinas(
            String dpiMesero,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        double propinas = 0;

        try {
            PreparedStatement statement = connection.prepareStatement( CONSULTAR_PROPINAS);
            statement.setString(1, dpiMesero);
            statement.setDate( 2, java.sql.Date.valueOf(fechaInicio));
            statement.setDate(3,java.sql.Date.valueOf(fechaFin));
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                propinas = resultado.getDouble("total_propinas");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return propinas;
    }

    public boolean actualizarNominaPendiente(
            String dpi,
            java.sql.Date fecha,
            String tipoPago,
            double monto) {

        try {

            PreparedStatement statement
                    = connection.prepareStatement(
                            ACTUALIZAR_NOMINA_PENDIENTE
                    );

            statement.setDouble(1, monto);
            statement.setString(2, dpi);
            statement.setDate(3, fecha);
            statement.setString(4, tipoPago);

            int filas
                    = statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}
