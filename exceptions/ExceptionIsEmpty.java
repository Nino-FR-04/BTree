package exceptions;

/**
 * Excepcion que se usa en caso de que una estructura de datos
 * este vacia y se quiera hacer una operacionde eliminacion u otra.
 */
public class ExceptionIsEmpty extends RuntimeException {
    
    /**
     * @param mnsj Mensaje que acompaña la excepcion
     */
    public ExceptionIsEmpty(String mnsj) {
        super(mnsj);
    }

    // Excepcion sin mensaje
    public ExceptionIsEmpty(){}
}