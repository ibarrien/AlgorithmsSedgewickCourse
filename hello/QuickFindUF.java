import java.util.Arrays;

/*
Quick find implementation
 */
public class QuickFindUF {
    private int[] id;  //private field

    public QuickFindUF(int N) {
        id = new int[N];
        // default instantiate
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    public boolean connected(int p, int q) {
        return id[p] == id[q];
    }

    public void union(int p, int q) {
        int src = id[p];
        int target = id[q];
        for (int i = 0; i < id.length; i++) {
            // at most 2N + 2 id accesses
            //N union on N objects -> quadratic time
            // bc double component size each time (at worst)
            if (connected(i, src)) {
                id[i] = target;
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        QuickFindUF myUF = new QuickFindUF(10); // no named params in Java
        System.out.println("Original id:");
        System.out.println(Arrays.toString(myUF.id)); // Call the public method on the object
        System.out.println("id after union 0 and 9:");
        myUF.union(0, 9);
        System.out.println(Arrays.toString(myUF.id));
    }

}



