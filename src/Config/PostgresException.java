package Config;
public class PostgresException extends Exception {
    private final String codigoError;
    private final String detalleTecnico;

    public PostgresException(String codigoError, String detalleTecnico) {
        super(formatMessage(codigoError, detalleTecnico));
        this.codigoError = codigoError;
        this.detalleTecnico = detalleTecnico;
    }

    private static String formatMessage(String codigoError, String detalleTecnico) {
        switch (codigoError) {
            case "23505":
                return "Ha insertado un valor repetido en un campo que no lo permite.";
            case "23503":
                return "Problema de referencia con la entidad relacionada.";
            case "23502":
                return "Uno de los campos obligatorios está vacío.";
            case "23514":
                return "Uno de los valores no cumple las reglas definidas.";
            case "22001":
                return "El valor ingresado es demasiado largo para el campo.";
            case "22P02":
                return "El valor ingresado no cumple con el formato requerido.";
            case "22003":
                return "El número ingresado es demasiado grande o pequeño para el campo.";
            case "08003":
                return "Conexión no disponible: Verifique el estado de la conexión a la base de datos.";
            default:
                return "Error desconocido con código " + codigoError + ": " + detalleTecnico;
        }
    }

    public String getCodigoError() {
        return codigoError;
    }

    public String getDetalleTecnico() {
        return detalleTecnico;
    }
}
