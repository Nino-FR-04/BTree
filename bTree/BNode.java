package bTree;
import java.util.ArrayList;

public class BNode<E> {
    
    //Atributos
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int keysCount;
    protected int idNode;
    public static int idUnique = 0;

    /**
     * Constructor que inicializa el nodo con el orden especificado.
     * @param orderNode orden del nodo.
     */
    public BNode(int orderNode) {
        this.keys = new ArrayList<>(orderNode);
        this.childs = new ArrayList<>(orderNode);
        this.keysCount = 0;

        for(int i=0;i < orderNode;i++){
            this.keys.add(null);
            this.childs.add(null);
        }

        //Cada que se crea un nodo se le asigna un id unico
        this.idNode = BNode.idUnique++;
    }

    /* -> Test
    public ArrayList<E> getKeys() {return this.keys;}
    public ArrayList<BNode<E>> getChilds() {return this.childs;}
    */

    // -> Metodos
    /**
     * Verifica si el nodo está lleno.
     * @param maxKey maximo de claves por nodo
     * @return true si el nodo está lleno, false en caso contrario.
     */
    public boolean isFull(int maxKeys) {return this.keysCount >= maxKeys;}

    /**
     * Verifica si el nodo está vacío.
     * @return true si el nodo no contiene claves, false en caso contrario.
     */
    public boolean isEmpty() {return this.keysCount == 0;}
    
    /**
     * Busca una clave dentro del nodo.
     * @param key clave a buscar.
     * @param position arreglo que contiene la posición actual de búsqueda.
     * @return true si la clave se encuentra en el nodo, false en caso contrario.
     */
    @SuppressWarnings("unchecked")
    public boolean searchKey(E key, int[] position) {
        while (position[0] < this.keysCount && 
                ((Comparable<E>) key).compareTo(this.keys.get(position[0])) > 0) {
            
            position[0]++;
        }

        if(position[0] < this.keysCount && 
                ((Comparable<E>) key).compareTo(this.keys.get(position[0])) == 0) {
            
            return true;
        }
        return false;
    }

    //toString
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        if(this.isEmpty()) return "";
        
        sb.append(this.idNode + "       ");
        sb.append("(");

        for(int i=0; i < this.keysCount ;i++) {
            sb.append(this.keys.get(i));
            if(i < this.keysCount - 1) sb.append(", ");
        }
        sb.append(")");

        return sb.toString();
    }
}