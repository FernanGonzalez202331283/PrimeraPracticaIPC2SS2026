/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author fernan
 */
public class DetalleCuenta {
    private int idDetalle;
    private int idCuenta;
    private int codigoProducto;
    private int cantidad;
    private double precio;
    private double subtotal;

    public DetalleCuenta(int idDetalle, int idCuenta, int codigoProducto,
            int cantidad, double precio, double subtotal) {

        this.idDetalle = idDetalle;
        this.idCuenta = idCuenta;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;

    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    

}
