import java.util.ArrayList;

import bTree.BTree;

public class Main {
    public static void main(String[] args) {
        
        BTree<Integer> tree = new BTree<>(3);
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        tree.insert(4);
        tree.insert(5);


        System.out.println(tree);

        tree.remove(1);

        System.out.println(tree);
        
    }
}
