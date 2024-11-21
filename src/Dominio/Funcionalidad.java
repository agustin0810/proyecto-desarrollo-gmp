package Dominio;

public class Funcionalidad {
    private int idFuncionalidad;
    private String nombreFuncionalidad;
    private String descripcionFuncionalidad;

    // Constructor
    public Funcionalidad(int idFuncionalidad, String nombreFuncionalidad, String descripcionFuncionalidad) {
        this.idFuncionalidad = idFuncionalidad;
        this.nombreFuncionalidad = nombreFuncionalidad;
        this.descripcionFuncionalidad = descripcionFuncionalidad;
    }

    // Getters y Setters
    public int getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(int idFuncionalidad) {
        this.idFuncionalidad = idFuncionalidad;
    }

    public String getNombreFuncionalidad() {
        return nombreFuncionalidad;
    }

    public void setNombreFuncionalidad(String nombreFuncionalidad) {
        this.nombreFuncionalidad = nombreFuncionalidad;
    }

    public String getDescripcionFuncionalidad() {
        return descripcionFuncionalidad;
    }

    public void setDescripcionFuncionalidad(String descripcionFuncionalidad) {
        this.descripcionFuncionalidad = descripcionFuncionalidad;
    }

    @Override
    public String toString() {
        return "Funcionalidad{" +
                "idFuncionalidad=" + idFuncionalidad +
                ", nombreFuncionalidad='" + nombreFuncionalidad + '\'' +
                ", descripcionFuncionalidad='" + descripcionFuncionalidad + '\'' +
                '}';
    }
}
