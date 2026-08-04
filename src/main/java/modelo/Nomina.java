/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author fernan
 */
public class Nomina {
    private int codigoNomina;
    private String dpiEmpleado;
    private String fechaEmision;
    private String tipoPago;
    private String estadoPago;
    private Double monto;

    public Nomina(int codigoNomina, String dpiEmpleado, String fechaEmision, String tipoPago, String estadoPago, Double monto) {
        this.codigoNomina = codigoNomina;
        this.dpiEmpleado = dpiEmpleado;
        this.fechaEmision = fechaEmision;
        this.tipoPago = tipoPago;
        this.estadoPago = estadoPago;
        this.monto = monto;
    }

    public int getCodigoNomina() {
        return codigoNomina;
    }

    public void setCodigoNomina(int codigoNomina) {
        this.codigoNomina = codigoNomina;
    }

    public String getDpiEmpleado() {
        return dpiEmpleado;
    }

    public void setDpiEmpleado(String dpiEmpleado) {
        this.dpiEmpleado = dpiEmpleado;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
    
    
}
