/* Think of 1-dim array as forest of tress.

 */

import java.util.Arrays;

public class QuickUnion {

    private int[] id;  //private field

    public QuickUnion(int N) {
        //initialize
        id = new int[N];
        // default instantiate
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    private int root(int p) {
        if (id[p] == p) {
            return p;
        } else {
            return root(id[p]);
        }
        // alternative: while(id[p] != p) {p = id[p]} return p;

    }

    public void union(int p, int q) {
        //set root of p to be root of q in id array
        int root_p = root(p);
        int root_q = root(q);
        id[root_p] = id[root_q];
    }

    public boolean connected(int p, int q) {
        // aka "find operation"
        // will be slow if tall tree, e.g. a linked list ~ O(N), linear in N array accesses
        return root(p) == root(q);
        // return id[p] == id[q];
    }

    // Main method
    public static void main(String[] args) {
        QuickUnion myUF = new QuickUnion(10); // no named params in Java
        System.out.println("Original id:");
        System.out.println(Arrays.toString(myUF.id)); // Call the public method on the object
        myUF.union(4, 3);
        System.out.println("id after union 4 and 3:");
        System.out.println(Arrays.toString(myUF.id)); //[0, 1, 2, 3, 3, 5, 6, 7, 8, 9]
        myUF.union(3, 8);
        System.out.println("id after union 3 and 8:");
        System.out.println(Arrays.toString(myUF.id)); //[0, 1, 2, 8, 3, 5, 6, 7, 8, 9]
        myUF.union(6, 5);
        System.out.println("id after union 6 and 5:");
        System.out.println(Arrays.toString(myUF.id)); //[0, 1, 2, 8, 3, 5, 5, 7, 8, 9]
        myUF.union(9, 4);  // now 9 is a child of 8
        boolean is_9_8_connected = myUF.connected(9, 8);
        System.out.println("Are 9 and 8 connected?");
        System.out.println(is_9_8_connected); // true
        myUF.union(2, 1);
        myUF.union(9, 4);
        System.out.println("Are 5 and 4 connected?");
        boolean is_5_4_connected = myUF.connected(5, 4);
        System.out.println(is_5_4_connected);  // false
        myUF.union(5, 0);
        myUF.union(7, 2);
        myUF.union(6, 1);
        myUF.union(7, 3);
        System.out.println("final id state");
        System.out.println(Arrays.toString(myUF.id)); //[1, 8, 1, 8, 3, 0, 5, 1, 8, 8]
    }
}
