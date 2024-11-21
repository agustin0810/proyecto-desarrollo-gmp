package Dominio;

public class Cliente extends Usuario {

    private int nroSocio;
    private int totalAnualCuotas;
    private int pagoCuotas;
    private boolean lenguajeSenas;
    private boolean dificultadAuditiva;

    // Constructor adaptado a la estructura de Usuario
    public Cliente(int idUsuario, String nombres, String apellidos, String tipoDocumento, String numeroDocumento,
                   String correo, String contrasena, String domicilio, String nroPuerta, String apto, String estado,
                   Perfil perfil, int nroSocio, int totalAnualCuotas, int pagoCuotas, boolean lenguajeSenas,
                   boolean dificultadAuditiva) {
        super(idUsuario, nombres, apellidos, tipoDocumento, numeroDocumento, correo, contrasena, domicilio, nroPuerta,
                apto, estado, perfil);
        this.nroSocio = nroSocio;
        this.totalAnualCuotas = totalAnualCuotas;
        this.pagoCuotas = pagoCuotas;
        this.lenguajeSenas = lenguajeSenas;
        this.dificultadAuditiva = dificultadAuditiva;
    }

    // Getters y Setters
    public int getNroSocio() {
        return nroSocio;
    }

    public void setNroSocio(int nroSocio) {
        this.nroSocio = nroSocio;
    }

    public int getTotalAnualCuotas() {
        return totalAnualCuotas;
    }

    public void setTotalAnualCuotas(int totalAnualCuotas) {
        this.totalAnualCuotas = totalAnualCuotas;
    }

    public int getPagoCuotas() {
        return pagoCuotas;
    }

    public void setPagoCuotas(int pagoCuotas) {
        this.pagoCuotas = pagoCuotas;
    }

    public boolean isLenguajeSenas() {
        return lenguajeSenas;
    }

    public void setLenguajeSenas(boolean lenguajeSenas) {
        this.lenguajeSenas = lenguajeSenas;
    }

    public boolean isDificultadAuditiva() {
        return dificultadAuditiva;
    }

    public void setDificultadAuditiva(boolean dificultadAuditiva) {
        this.dificultadAuditiva = dificultadAuditiva;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nroSocio=" + nroSocio +
                ", totalAnualCuotas=" + totalAnualCuotas +
                ", pagoCuotas=" + pagoCuotas +
                ", lenguajeSenas=" + lenguajeSenas +
                ", dificultadAuditiva=" + dificultadAuditiva +
                ", perfil=" + (getPerfil() != null ? getPerfil().getNombrePerfil() : "Sin perfil asignado") +
                "} " + super.toString();
    }
}
