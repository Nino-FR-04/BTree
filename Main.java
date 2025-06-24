import bTree.BTree;

public class Main {
    public static void main(String[] args) {
        // Crear un árbol B de orden 4 (máx 3 claves por nodo)
        BTree<Integer> btree = new BTree<>(4);

        // Insertar claves
        int[] keys = {10, 20, 5, 6, 12, 30, 7, 17};
        for (int key : keys) {
            System.out.println("Insertando: " + key);
            btree.insert(key);
        }

        System.out.println(btree.search(10));
        btree.remove(10);
        System.out.println(btree.search(10));
        System.out.println(btree.getRoot());
        
    }
}
