/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.primerapracticaipc2ss2026;

import ComunicacionesDAO.EmpleadoDAO;
import ComunicacionesDAO.NominaDAO;
import conexion.ConexionDAO;
import java.util.Scanner;

/**
 *
 * @author fernan
 */
public class PrimeraPracticaIPC2SS2026 {

    public static void main(String[] args) {
       EmpleadoDAO empleadoDAO = new EmpleadoDAO();
       NominaDAO nominaDAO = new NominaDAO();

Scanner scanner = new Scanner(System.in);

int opcion;

do {

   System.out.println("\n===== SISTEMA CAFETERIA =====");
System.out.println("----- EMPLEADOS -----");
System.out.println("1. Registrar empleado");
System.out.println("2. Listar empleados");
System.out.println("3. Actualizar empleado");
System.out.println("4. Deshabilitar empleado");

System.out.println("\n----- NOMINAS -----");
System.out.println("5. Registrar nómina");
System.out.println("6. Listar nóminas");
System.out.println("7. Marcar nómina como pagada");

System.out.println("\n0. Salir");
System.out.print("Seleccione una opción: ");

    opcion = scanner.nextInt();
    scanner.nextLine();

    switch (opcion) {

    case 1:
        empleadoDAO.insertarEmpleado();
        break;

    case 2:
        empleadoDAO.listarEmpleados();
        break;

    case 3:
        empleadoDAO.actualizarEmpleado();
        break;

    case 4:
        empleadoDAO.deshabilitarEmpleado();
        break;

    case 5:
        nominaDAO.registrarNomina();
        break;

    case 6:
        nominaDAO.listarNominas();
        break;

    case 7:
        nominaDAO.pagarNomina();
        break;

    case 0:
        System.out.println("Saliendo...");
        break;

    default:
        System.out.println("Opción inválida.");
    }

} while (opcion != 5);
        
    }
}
