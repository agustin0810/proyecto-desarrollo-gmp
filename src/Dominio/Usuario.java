package Dominio;

public class Usuario {
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
    private String correo;
    private String contrasena;
    private String domicilio;
    private String nroPuerta;
    private String apto;
    private String estado;
    private Perfil perfil; // Relación con Perfil (1:N)

    // Constructor
    public Usuario(int idUsuario, String nombres, String apellidos, String tipoDocumento, String numeroDocumento,
                   String correo, String contrasena, String domicilio, String nroPuerta, String apto, String estado, Perfil perfil) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.correo = correo;
        this.contrasena = contrasena;
        this.domicilio = domicilio;
        this.nroPuerta = nroPuerta;
        this.apto = apto;
        this.estado = estado;
        this.perfil = perfil;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getNroPuerta() {
        return nroPuerta;
    }

    public void setNroPuerta(String nroPuerta) {
        this.nroPuerta = nroPuerta;
    }

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", nroPuerta='" + nroPuerta + '\'' +
                ", apto='" + apto + '\'' +
                ", estado='" + estado + '\'' +
                ", perfil=" + perfil +
                '}';
    }
}
