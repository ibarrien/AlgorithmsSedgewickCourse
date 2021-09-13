/* Neat improvements to QuickUnion based maintaining smaller depths.

 */

import java.util.Arrays;

public class WeightedQuickUnion {

    private int[] id;  // forest of trees representation
    private int[] sz;  // root i has sz[i] many children nodes (>= 1)

    public WeightedQuickUnion(int N) {

        //initialize
        id = new int[N];
        sz = new int[N]; // number of child nodes connected to root
        // default instantiate
        for (int i = 0; i < N; i++) {
            id[i] = i;
            sz[i] = 1; // each root has only itself as (child) node
        }
    }

    public int[] getId() {
        // int[] this_id = id;
        return id.clone();
    }

    private int root(int p) {
        while (p != id[p]) {
            id[p] = id[id[p]]; // set parent to grandparent
            p = id[p];
        }
        return p;

    }

    public void union(int p, int q) {
        // weighted union: to keep depth on avg small,
        // keep larger sizes up top (as root) and smaller sizes bottom (children)
        int root_p = root(p);
        int root_q = root(q);
        if (sz[root_p] > sz[root_q]) {
            //p-component is larger, so add q-component beneath p-component
            id[root_q] = root_p;
            // update sizes
            sz[root_p] += sz[root_q]; // only update size of p-component; q-size unchanged
        } else {
            id[root_p] = root_q;
            sz[root_q] += sz[root_p];
        }
    }

    public boolean connected(int p, int q) {
        // aka "find operation"
        // will be slow if tall tree, e.g. a linked list ~ O(N), linear in N array accesses
        return root(p) == root(q);
        // consider path compression alternative, keep nodes traversed
    }


    // Main method
    public static void main(String[] args) {
        WeightedQuickUnion myUF = new WeightedQuickUnion(10); // no named params in Java
        System.out.println("Original id:");
        System.out.println(Arrays.toString(myUF.id)); // Call the public method on the object
        myUF.union(6, 7);
        myUF.union(7, 8);
        // myUF.union(8, 9);

        System.out.println("id curr state");
        System.out.println(Arrays.toString(myUF.id)); //[0, 1, 2, 3, 3, 5, 6, 7, 8, 9]

    }
}
