package Dominio;

public class Administrador extends Usuario {

    public Administrador(int idUsuario, String nombres, String apellidos, String tipoDocumento, String numeroDocumento,
                         String correo, String contrasena, String domicilio, String nroPuerta, String apto,
                         String estado, Perfil perfil) {
        super(idUsuario, nombres, apellidos, tipoDocumento, numeroDocumento, correo, contrasena, domicilio, nroPuerta,
                apto, estado, perfil);
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "perfil=" + (getPerfil() != null ? getPerfil().getNombrePerfil() : "Sin perfil asignado") +
                "} " + super.toString();
    }
}
