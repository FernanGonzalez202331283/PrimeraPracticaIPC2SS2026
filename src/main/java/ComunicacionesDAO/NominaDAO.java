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
import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;
import modelo.Nomina;

/**
 *
 * @author fernan
 */
public class NominaDAO {
    private Connection connection;
    public NominaDAO(){
        ConexionDAO conexionDAO = new ConexionDAO();
        connection = conexionDAO.Connectar();
    }
    
    public static final String INSERTAR_NOMINA =
        """
        INSERT INTO nomina
        (dpi_empleado, fecha_emision, tipo_de_pago, estado_pago, monto)
        VALUES (?, ?, ?, ?, ?)
        """;
    
   public static final String CONSULTAR_NOMINAS =
        """
        SELECT * FROM nomina
        """;
    
   public static final String PAGAR_NOMINA =
        """
        UPDATE nomina
        SET estado_pago = 'PAGADO'
        WHERE codigo_nomina = ?
        """;
   
   public static final String EMPLEADOS_ACTIVOS =
        """
        SELECT dpi, salario
        FROM empleado
        WHERE estado = TRUE
        """;

public static final String EXISTE_NOMINA =
        """
        SELECT COUNT(*)
        FROM nomina
        WHERE dpi_empleado = ?
        AND fecha_emision = ?
        AND tipo_de_pago = ?
        """;
    
    public void registrarNomina() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese DPI del empleado:");
    String dpi = scanner.nextLine();

    System.out.println("Ingrese fecha de emisión (AAAA-MM-DD):");
    String fecha = scanner.nextLine();

    System.out.println("Ingrese tipo de pago (QUINCENA o FIN_DE_MES):");
    String tipo = scanner.nextLine();

    System.out.println("Ingrese monto:");
    double monto = scanner.nextDouble();

    Nomina nomina = new Nomina(
            0,
            dpi,
            fecha,
            tipo,
            "PENDIENTE",
            monto
    );
    try {

    PreparedStatement statement =
            connection.prepareStatement(INSERTAR_NOMINA);

    statement.setString(1, nomina.getDpiEmpleado());
    statement.setString(2, nomina.getFechaEmision());
    statement.setString(3, nomina.getTipoPago());
    statement.setString(4, nomina.getEstadoPago());
    statement.setDouble(5, nomina.getMonto());

    int filas = statement.executeUpdate();

    System.out.println("Nómina registrada.");
    System.out.println("Filas insertadas: " + filas);

} catch (SQLException e) {

    e.printStackTrace();

}

}
    public void listarNominas() {

    try {

        PreparedStatement statement =
        connection.prepareStatement(CONSULTAR_NOMINAS);

ResultSet resultado = statement.executeQuery();

        while (resultado.next()) {

            System.out.println("----------------------------");
            System.out.println("Código: "
                    + resultado.getInt("codigo_nomina"));

            System.out.println("Empleado: "
                    + resultado.getString("dpi_empleado"));

            System.out.println("Fecha: "
                    + resultado.getDate("fecha_emision"));

            System.out.println("Tipo: "
                    + resultado.getString("tipo_de_pago"));

            System.out.println("Estado: "
                    + resultado.getString("estado_pago"));

            System.out.println("Monto: "
                    + resultado.getDouble("monto"));

        }

    } catch (SQLException e) {

        e.printStackTrace();

    }

}
    
    public void pagarNomina() {

    Scanner scanner = new Scanner(System.in);

    System.out.println("Ingrese código de nómina:");

    int codigo = scanner.nextInt();
    try {

    PreparedStatement statement =
            connection.prepareStatement(PAGAR_NOMINA);

    statement.setInt(1, codigo);

    int filas = statement.executeUpdate();

    if (filas > 0) {
        System.out.println("Pago realizado.");
        System.out.println("Filas afectadas: " + filas);
    } else {
        System.out.println("No existe una nómina con ese código.");
    }

} catch (SQLException e) {

    e.printStackTrace();

}

}
    public void generarNominasAutomaticamente() {

    LocalDate hoy = LocalDate.now();

    int dia = hoy.getDayOfMonth();

    String tipoPago;
    double porcentaje;
    if (dia == 10) {
        tipoPago = "QUINCENA";
        porcentaje = 0.30;
    } else if (dia == hoy.lengthOfMonth() - 5) {
        tipoPago = "FIN_DE_MES";
        porcentaje = 0.70;
    } else {
        System.out.println("Hoy no corresponde generar nóminas.");
        return;
    }
    try {

        PreparedStatement consultaEmpleados =
                connection.prepareStatement(EMPLEADOS_ACTIVOS);
        ResultSet empleados = consultaEmpleados.executeQuery();
        int generadas = 0;
        while (empleados.next()) {
            String dpi = empleados.getString("dpi");
            double salario = empleados.getDouble("salario");
            double monto = salario * porcentaje;
            PreparedStatement verificar =
                    connection.prepareStatement(EXISTE_NOMINA);
            verificar.setString(1, dpi);
            verificar.setDate(2, java.sql.Date.valueOf(hoy));
            verificar.setString(3, tipoPago);
            ResultSet existe = verificar.executeQuery();
            existe.next();
            if (existe.getInt(1) == 0) {
                PreparedStatement insertar =
                        connection.prepareStatement(INSERTAR_NOMINA);
                insertar.setString(1, dpi);
                insertar.setDate(2, java.sql.Date.valueOf(hoy));
                insertar.setString(3, tipoPago);
                insertar.setString(4, "PENDIENTE");
                insertar.setDouble(5, monto);
                insertar.executeUpdate();
                generadas++;
            }
        }
        System.out.println("Nóminas generadas: " + generadas);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
