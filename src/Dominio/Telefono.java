package Dominio;

public class Telefono {
    private String numero;
    private String tipoTelefono;

    public Telefono(String numero, String tipoTelefono) {
        this.numero = numero;
        this.tipoTelefono = tipoTelefono;
    }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getTipoTelefono() { return tipoTelefono; }
    public void setTipoTelefono(String tipoTelefono) { this.tipoTelefono = tipoTelefono; }

    @Override
    public String toString() {
        return "Telefono{" +
                "numero='" + numero + '\'' +
                ", tipoTelefono='" + tipoTelefono + '\'' +
                '}';
    }
}
