package bTree;
import exceptions.ExceptionElementNotFound;
import exceptions.ExceptionIsEmpty;
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

    //Test
    public BNode<E> getRoot() {return this.root;}

    // Métodos básicos
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * Inserta una nueva clave en el árbol B. 
     * Si la inserción provoca un desbordamiento en la raíz, se crea una nueva raíz.
     * 
     * @param key La clave que se desea insertar en el árbol.
     * @throws ExceptionItemDuplicated Si la clave que se intenta insertar ya existe en el árbol.
     */
    public void insert(E key) throws ExceptionItemDuplicated {
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

    /**
     * Método recursivo que permite insertar una clave en el árbol B.
     * Este método desciende por los nodos hasta encontrar la posición adecuada
     * para insertar la nueva clave. Puede provocar divisiones y propagaciones hacia arriba.
     * 
     * @param currentNode El nodo actual donde se busca la posición para insertar.
     * @param key La clave que se desea insertar.
     * @return La clave que debe ser promovida al nivel superior si ocurre una división.
     * @throws ExceptionItemDuplicated Si la clave que se intenta insertar ya existe en el árbol.
     */
    private E insertRecursive(BNode<E> currentNode, E key) {
        int position[] = new int[1]; // Posición donde debería insertarse la clave
        E medianKey;

        if (currentNode == null) {
            // Caso base se encontro un hijo nulo
            this.upNode = true; //implica que la insercion se realiza un nodo arriba
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
                    // El nodo tiene espacio por lo que no es necesario dividirlo
                    this.insertKey(currentNode, medianKey, this.rightNode, position[0]);
                    this.upNode = false;
                }
            }
            return medianKey;
        }
    }

    /**
     * Inserta una clave y su hijo derecho en un nodo que tiene espacio disponible.
     * Este método realiza los desplazamientos necesarios para mantener las claves ordenadas.
     *
     * @param node El nodo donde se insertará la nueva clave.
     * @param key La clave que se desea insertar.
     * @param rightChild El hijo derecho asociado a la clave insertada.
     * @param position La posición exacta donde se debe insertar la nueva clave dentro del nodo.
     */
    private void insertKey(BNode<E> node, E key, BNode<E> rightChild, int position) {
        int i;

        // Desplazar las claves y los hijos hacia la derecha para hacer espacio
        for (i = node.keysCount - 1; i >= position; i--) {
            node.keys.set(i + 1, node.keys.get(i));
            node.childs.set(i + 2, node.childs.get(i + 1));
        }

        node.keys.set(position, key);
        node.childs.set(position + 1, rightChild);
        node.keysCount++;
    }

    /**
     * Divide un nodo que está lleno al insertar una nueva clave.
     * Se crea un nuevo nodo derecho, se redistribuyen las claves y se retorna la clave mediana que debe subir al padre.
     *
     * @param currentNode El nodo actual que está lleno y necesita dividirse.
     * @param key La nueva clave que se desea insertar.
     * @param insertPosition La posición donde debe insertarse la nueva clave en el nodo actual.
     * @return La clave mediana que debe promocionarse al nodo padre.
     */
    private E splitNode(BNode<E> currentNode, E key, int insertPosition) {
        BNode<E> rightChild = this.rightNode;
        int i, medianPos;

        // Determina la posicion de la mediana
        medianPos = (insertPosition <= this.treeOrder / 2) ? this.treeOrder / 2 : this.treeOrder / 2 + 1;

        this.rightNode = new BNode<E>(this.treeOrder);

        /* 
        Copia las llaves y los hijos en el nodo derecho basandose en la posicion
        de la mediana
         */
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

    /**
     * Busca una clave en el árbol B.
     *
     * @param key La clave a buscar.
     * @return true si la clave se encuentra en el árbol, false en caso contrario.
     */
    public boolean search(E key) {
        return this.searchRecursive(this.root, key);
    }

    /**
     * Método recursivo que busca una clave dentro del árbol B.
     *
     * @param node Nodo actual donde se busca la clave.
     * @param key Clave que se desea encontrar.
     * @return true si la clave fue encontrada, false si no está en este subárbol.
     */
    private boolean searchRecursive(BNode<E> node, E key) {
        int[] pos = new int[1];

        //Caso base: no se encontro la llave en el arbol
        if(node == null) return false;

        if (node.searchKey(key, pos)) {
            System.out.println("Clave " + key + " encontrada en:");
            System.out.println("Nodo: " + node);
            System.out.println("Posición: " + pos[0]);
            return true;
        }

        return this.searchRecursive(node.childs.get(pos[0]), key);
    }

    public void remove(E key) throws ExceptionIsEmpty,ExceptionElementNotFound {
        
        if(this.isEmpty()) {
            throw new ExceptionIsEmpty("El arbol esta vacio");
        }

        removeRecursive(this.root, key);

        // Si después de eliminar, la raíz queda sin claves, ajustamos la raíz
        if (this.root != null && this.root.keysCount == 0) {
            this.root = this.root.childs.get(0); // La raíz se reduce
        }
    }

    private void removeRecursive(BNode<E> node, E key) throws ExceptionElementNotFound {
        int[] pos = new int[1];

        if (node == null) { // Llegamos a un hijo nulo sin encontrar la clave
            throw new ExceptionElementNotFound("La clave " + key + " no existe en el árbol.");
        }

        boolean found = node.searchKey(key, pos);

        if (found) { // Clave encontrada
            if (node.childs.get(0) == null) { // Es una hoja
                removeKeyFromNode(node, pos[0]);
            } else { // Nodo interno
                replaceWithPredecessor(node, pos[0]);
            }
        } else { // La clave no está en este nodo
            if (node.childs.get(pos[0]) == null) { // Llegaste a un hijo nulo
                throw new ExceptionElementNotFound("La clave " + key + " no existe en el árbol.");
            }

            removeRecursive(node.childs.get(pos[0]), key);

            // Verificar subdesbordamiento
            if (node.childs.get(pos[0]).keysCount < (this.treeOrder - 1) / 2) {
                fixUnderflow(node, pos[0]);
            }
        }
    }

    private void removeKeyFromNode(BNode<E> node, int pos) {
        for (int i = pos; i < node.keysCount - 1; i++) {
            node.keys.set(i, node.keys.get(i + 1));
            node.childs.set(i + 1, node.childs.get(i + 2));
        }
        node.keys.set(node.keysCount - 1, null);
        node.childs.set(node.keysCount, null);
        node.keysCount--;
    }

    private void replaceWithPredecessor(BNode<E> node, int pos) {
        BNode<E> predNode = node.childs.get(pos);
        while (predNode.childs.get(predNode.keysCount) != null) {
            predNode = predNode.childs.get(predNode.keysCount);
        }

        E predKey = predNode.keys.get(predNode.keysCount - 1);
        node.keys.set(pos, predKey);
        removeRecursive(node.childs.get(pos), predKey);
    }

    private void fixUnderflow(BNode<E> parent, int posChild) {
        // Intentar redistribuir con hermano izquierdo
        if (posChild > 0 && parent.childs.get(posChild - 1).keysCount > (this.treeOrder - 1) / 2) {
            redistributeLeft(parent, posChild);
        }
        // Intentar redistribuir con hermano derecho
        else if (posChild < parent.keysCount && parent.childs.get(posChild + 1).keysCount > (this.treeOrder - 1) / 2) {
            redistributeRight(parent, posChild);
        }
        // Si no es posible redistribuir, fusionar
        else if (posChild > 0) { // Fusionar con el hermano izquierdo
            mergeNodes(parent, posChild - 1);
        } else { // Fusionar con el hermano derecho
            mergeNodes(parent, posChild);
        }
    }

    private void redistributeLeft(BNode<E> parent, int posChild) {
        BNode<E> leftSibling = parent.childs.get(posChild - 1);
        BNode<E> child = parent.childs.get(posChild);

        // Mover clave del padre al hijo
        for (int i = child.keysCount - 1; i >= 0; i--) {
            child.keys.set(i + 1, child.keys.get(i));
        }
        for (int i = child.keysCount; i >= 0; i--) {
            child.childs.set(i + 1, child.childs.get(i));
        }

        child.keys.set(0, parent.keys.get(posChild - 1));
        child.childs.set(0, leftSibling.childs.get(leftSibling.keysCount));
        child.keysCount++;

        // Mover clave del hermano al padre
        parent.keys.set(posChild - 1, leftSibling.keys.get(leftSibling.keysCount - 1));
        leftSibling.keys.set(leftSibling.keysCount - 1, null);
        leftSibling.childs.set(leftSibling.keysCount, null);
        leftSibling.keysCount--;
    }

    private void redistributeRight(BNode<E> parent, int posChild) {
        BNode<E> rightSibling = parent.childs.get(posChild + 1);
        BNode<E> child = parent.childs.get(posChild);

        child.keys.set(child.keysCount, parent.keys.get(posChild));
        child.childs.set(child.keysCount + 1, rightSibling.childs.get(0));
        child.keysCount++;

        parent.keys.set(posChild, rightSibling.keys.get(0));

        for (int i = 0; i < rightSibling.keysCount - 1; i++) {
            rightSibling.keys.set(i, rightSibling.keys.get(i + 1));
            rightSibling.childs.set(i, rightSibling.childs.get(i + 1));
        }

        rightSibling.childs.set(rightSibling.keysCount - 1, rightSibling.childs.get(rightSibling.keysCount));
        rightSibling.keys.set(rightSibling.keysCount - 1, null);
        rightSibling.childs.set(rightSibling.keysCount, null);
        rightSibling.keysCount--;
    }

    private void mergeNodes(BNode<E> parent, int posChild) {
        BNode<E> leftChild = parent.childs.get(posChild);
        BNode<E> rightChild = parent.childs.get(posChild + 1);

        leftChild.keys.set(leftChild.keysCount, parent.keys.get(posChild));
        leftChild.childs.set(leftChild.keysCount + 1, rightChild.childs.get(0));
        leftChild.keysCount++;

        for (int i = 0; i < rightChild.keysCount; i++) {
            leftChild.keys.set(leftChild.keysCount, rightChild.keys.get(i));
            leftChild.childs.set(leftChild.keysCount + 1, rightChild.childs.get(i + 1));
            leftChild.keysCount++;
        }

        // Eliminar clave del padre
        for (int i = posChild; i < parent.keysCount - 1; i++) {
            parent.keys.set(i, parent.keys.get(i + 1));
            parent.childs.set(i + 1, parent.childs.get(i + 2));
        }

        parent.keys.set(parent.keysCount - 1, null);
        parent.childs.set(parent.keysCount, null);
        parent.keysCount--;
    }
}
