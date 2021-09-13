import java.util.Arrays;

public class ExampleRuns {

    // Main method
    public static void main(String[] args) {
        int id_len = 10;
        QuickUnion uf = new QuickUnion(id_len);
        WeightedQuickUnion wtd_uf = new WeightedQuickUnion(id_len);
        System.out.println("Original id:");
        System.out.println(Arrays.toString(uf.getId())); // Call the public method on the object
        for (int k = 0; k < id_len - 1; k++) {
            uf.union(k, k + 1);
            wtd_uf.union(k, k + 1);
        }
        System.out.println("New union find id:");
        System.out.println(Arrays.toString(uf.getId()));
        System.out.println("New weighted union find id:");
        System.out.println(Arrays.toString(wtd_uf.getId()));
    }

}
