/**
 * Excepcion que se usa en caso de que se quiera ingresar algun dato duplicado
 * en alguna estructura de datos.
 */
public class ExceptionItemDuplicated extends RuntimeException {
    
    /**
     * @param mnsj Mensaje que acompaña la excepcion
     */
    public ExceptionItemDuplicated(String mnsj) {
        super(mnsj);
    }

    // Excepcion sin mensaje
    public ExceptionItemDuplicated(){}
}