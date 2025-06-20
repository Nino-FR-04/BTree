package bTree;
import java.util.ArrayList;

public class BNode<E> {
    
    //Atributos
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int keysCount;
    protected int idNode;
    public static int idUnique = 0;

    //Constructor
    /**
     * Inicializa el nodo con un orden dado.
     * @param orderNode orden del nodo e indicador de inicializacion.
     */
    public BNode(int orderNode) {
        this.keys = new ArrayList<>(orderNode);
        this.childs = new ArrayList<>(orderNode + 1);
        this.keysCount = 0;
        
        for(int i=0;i < orderNode;i++) {this.keys.add(null);}
        for(int i=0;i <= orderNode;i++) {this.childs.add(null);}
        
        //Cada que se crea un nodo se le asigna un id unico
        this.idNode = BNode.idUnique++;
    }

    public ArrayList<E> getKeys() {return this.keys;}
    public ArrayList<BNode<E>> getChilds() {return this.childs;}

    //Metodos
    public boolean isFull(int maxKeys) {return this.keysCount >= maxKeys;}
    public boolean isEmptyNode() {return this.keysCount == 0;}
    
    @SuppressWarnings("unchecked")
    public boolean searchNode(E key, int[] position) {
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
        sb.append(this.idNode + "       ");

        if(this.isEmptyNode()) return sb.toString();

        sb.append("(");

        for(int i=0; i < this.keysCount ;i++) {
            sb.append(this.keys.get(i));
            if(i < this.keysCount - 1) sb.append(", ");
        }
        sb.append(")");

        return sb.toString();
    }
}