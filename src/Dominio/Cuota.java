package Dominio;

import java.util.Date;

public class Cuota {
    private int idCuota;
    private int numeroCuota;
    private Date fechaCuota;
    private double valorCuota;
    private Cliente cliente; // Relación con el cliente

    // Constructor
    public Cuota(int idCuota, int numeroCuota, Date fechaCuota, double valorCuota, Cliente cliente) {
        this.idCuota = idCuota;
        this.numeroCuota = numeroCuota;
        this.fechaCuota = fechaCuota;
        this.valorCuota = valorCuota;
        this.cliente = cliente;
    }

    // Getters y Setters
    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public Date getFechaCuota() {
        return fechaCuota;
    }

    public void setFechaCuota(Date fechaCuota) {
        this.fechaCuota = fechaCuota;
    }

    public double getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(double valorCuota) {
        this.valorCuota = valorCuota;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Cuota{" +
                "idCuota=" + idCuota +
                ", numeroCuota=" + numeroCuota +
                ", fechaCuota=" + fechaCuota +
                ", valorCuota=" + valorCuota +
                ", cliente=" + (cliente != null ? cliente.getNroSocio() : "Sin cliente asociado") +
                '}';
    }
}
