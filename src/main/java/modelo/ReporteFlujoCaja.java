/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author fernan
 */
public class ReporteFlujoCaja {
    private double totalIngresos;
    private double totalEgresos;
    private double balance;
    private String resultado;

    public ReporteFlujoCaja(double totalIngresos, double totalEgresos, double balance, String resultado) {
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
        this.balance = balance;
        this.resultado = resultado;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public double getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(double totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
