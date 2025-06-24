package exceptions;
/**
 * Excepcion que se usa en caso de que se quiera ingresar algun dato nulo
 * en alguna estructura de datos.
 */
public class ExceptionElementNotFound extends RuntimeException {
    
    /**
     * @param mnsj Mensaje que acompaña la excepcion
     */
    public ExceptionElementNotFound(String mnsj) {
        super(mnsj);
    }

    // Excepcion sin mensaje
    public ExceptionElementNotFound(){}
}