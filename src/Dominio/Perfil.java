package Dominio;

import java.util.List;

public class Perfil {
    private int idPerfil;
    private String nombrePerfil;
    private String estado;
    private String descripcion;
    private List<Funcionalidad> funcionalidades; // Asociación con Funcionalidad (N:N)

    // Constructor
    public Perfil(int idPerfil, String nombrePerfil, String estado, String descripcion) {
        this.idPerfil = idPerfil;
        this.nombrePerfil = nombrePerfil;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Funcionalidad> getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(List<Funcionalidad> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "idPerfil=" + idPerfil +
                ", nombrePerfil='" + nombrePerfil + '\'' +
                ", estado='" + estado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", funcionalidades=" + funcionalidades +
                '}';
    }
}
