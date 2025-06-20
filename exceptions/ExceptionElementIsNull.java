/**
 * Excepcion que se usa en caso de que se quiera ingresar algun dato nulo
 * en alguna estructura de datos.
 */
public class ExceptionElementIsNull extends RuntimeException {
    
    /**
     * @param mnsj Mensaje que acompaña la excepcion
     */
    public ExceptionElementIsNull(String mnsj) {
        super(mnsj);
    }

    // Excepcion sin mensaje
    public ExceptionElementIsNull(){}
}