package bTree;
import exceptions.ExceptionItemDuplicated;

public class BTree<E extends Comparable<E>> {

    // Atributos
    private BNode<E> root;
    private int treeOrder;
    private boolean upNode;
    private BNode<E> rightNode; //->Division del nodo

    // Constructor
    public BTree(int treeOrder) {
        this.treeOrder = treeOrder;
        this.root = null;
    }

    public BNode<E> getRoot() {return this.root;}

    // Métodos básicos
    public boolean isEmpty() {
        return this.root == null;
    }

    public void insert(E key) {
        this.upNode = false;
        E medianKey;
        BNode<E> newRoot; //En caso de que la raiz sea la que se divida

        medianKey = insertRecursive(this.root, key);

        // Si hubo desbordamiento en la raíz, crear una nueva raíz
        if (this.upNode) {
            newRoot = new BNode<E>(this.treeOrder);
            newRoot.keysCount = 1;
            newRoot.keys.set(0, medianKey);
            newRoot.childs.set(0, this.root);
            newRoot.childs.set(1, this.rightNode);
            this.root = newRoot;
        }
    }

    private E insertRecursive(BNode<E> currentNode, E key) {
        int position[] = new int[1];
        E medianKey;

        if (currentNode == null) {
            // Caso base se encontro un hijo nulo
            this.upNode = true;
            this.rightNode = null;
            return key;
        } else {
            boolean keyExists = currentNode.searchKey(key, position);

            if (keyExists) {
                this.upNode = false;
                throw new ExceptionItemDuplicated("Llave duplicada: " + key);
            }

            // Llamada recursiva
            medianKey = insertRecursive(currentNode.childs.get(position[0]), key);

            if (this.upNode) {
                // si el nodo esta lleno se realiza una division
                if (currentNode.isFull(this.treeOrder - 1)) {
                    medianKey = splitNode(currentNode, medianKey, position[0]);
                } else {
                    // Insertar sin dividir el nodo
                    this.insertKey(currentNode, medianKey, this.rightNode, position[0]);
                    this.upNode = false;
                }
            }
            return medianKey;
        }
    }

    /**
     * Metodo que inserta una llave si el nodo tiene espacio
     */
    private void insertKey(BNode<E> node, E key, BNode<E> rightChild, int position) {
        int i;
        for (i = node.keysCount - 1; i >= position; i--) {
            node.keys.set(i + 1, node.keys.get(i));
            node.childs.set(i + 2, node.childs.get(i + 1));
        }
        node.keys.set(position, key);
        node.childs.set(position + 1, rightChild);
        node.keysCount++;
    }

    /**
     * Divide el Nodo
     */
    private E splitNode(BNode<E> currentNode, E key, int insertPosition) {
        BNode<E> rightChild = this.rightNode;
        int i, medianPos;

        // Determina la posicion de la mediana
        medianPos = (insertPosition <= this.treeOrder / 2) ? this.treeOrder / 2 : this.treeOrder / 2 + 1;

        this.rightNode = new BNode<E>(this.treeOrder);

        // Copia las llaves y los hijos en el nodo derecho
        for (i = medianPos; i < this.treeOrder - 1; i++) {
            this.rightNode.keys.set(i - medianPos, currentNode.keys.get(i));
            this.rightNode.childs.set(i - medianPos + 1, currentNode.childs.get(i + 1));
        }

        this.rightNode.keysCount = (this.treeOrder - 1) - medianPos;
        currentNode.keysCount = medianPos;

        // Inserta la llave dependiendo de la posicion de la mediana
        if (insertPosition <= this.treeOrder / 2) {
            this.insertKey(currentNode, key, rightChild, insertPosition);
        } else {
            this.insertKey(this.rightNode, key, rightChild, insertPosition - medianPos);
        }

        E medianKey = currentNode.keys.get(currentNode.keysCount - 1);

        this.rightNode.childs.set(0, currentNode.childs.get(currentNode.keysCount));
        currentNode.keysCount--;

        return medianKey;
    }

    public boolean search(E key) {
        return this.searchRecursive(this.root, key);
    }

    private boolean searchRecursive(BNode<E> node, E key) {
        int[] pos = new int[1];
        //Caso base: no se encontro la llave en el arbol
        if (node.searchKey(key, pos)) {
            System.out.println("Clave " + key + " encontrada en:");
            System.out.println("Nodo ID: " + node.idNode + " -> " + node.keys.subList(0, node.keysCount));
            System.out.println("Posición: " + pos[0]);
            return true;
        }

        if(node.searchKey(key, pos)) return true;
        return this.searchRecursive(node.childs.get(pos[0]), key);
    }

}
