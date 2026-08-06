/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author fernan
 */
public class Cuenta {
    private int idCuenta;
    private int numeroMesa;
    private String dpiMesero;
    private String fecha;
    private String horaOcupacion;
    private String horaLiberacion;
    private String estado;
    private double propina;
    private double total;

    public Cuenta(int idCuenta, int numeroMesa, String dpiMesero, String fecha, String horaOcupacion, String horaLiberacion, String estado, double propina, double total) {
        this.idCuenta = idCuenta;
        this.numeroMesa = numeroMesa;
        this.dpiMesero = dpiMesero;
        this.fecha = fecha;
        this.horaOcupacion = horaOcupacion;
        this.horaLiberacion = horaLiberacion;
        this.estado = estado;
        this.propina = propina;
        this.total = total;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getDpiMesero() {
        return dpiMesero;
    }

    public void setDpiMesero(String dpiMesero) {
        this.dpiMesero = dpiMesero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraOcupacion() {
        return horaOcupacion;
    }

    public void setHoraOcupacion(String horaOcupacion) {
        this.horaOcupacion = horaOcupacion;
    }

    public String getHoraLiberacion() {
        return horaLiberacion;
    }

    public void setHoraLiberacion(String horaLiberacion) {
        this.horaLiberacion = horaLiberacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getPropina() {
        return propina;
    }

    public void setPropina(double propina) {
        this.propina = propina;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
}
