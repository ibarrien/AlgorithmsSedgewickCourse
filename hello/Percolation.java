/* *****************************************************************************
 *  Name:              Ivan
 *  Coursera User ID:  *
 *  Last modified:     Sept 13, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private int[][] grid;
    private WeightedQuickUnionUF uf;
    private int top_site_val;  // virtual site at top of grid
    private int bottom_site_val;  // virtual site at bottom of grid


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        N = n;
        top_site_val = 0; // val of top size, grid starts at row=1
        bottom_site_val = (int) (Math.pow(n, 2) + 1);  // val of bottom site
        int this_loc; // current 1d site location in loop
        int num_sites = (int) Math.pow(n, 2) + 2;  // 0 to N^2 + 1 indexing; first, last=virtual

        // CREATE THE GRID
        // use extra row and col for 1-based indexing when calling grid sites
        // e.g. for opening and calls to union find
        // do not write int grid [][] = new int
        // ... since already instance var!
        grid = new int[n + 1][n + 1];

        //Initializes an empty union-find data structure with n elements 0 through n-1.
        uf = new WeightedQuickUnionUF(num_sites); //id[i] = i
        // initialize virtual site
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                grid[i][j] = 0; // initialize all sites on grid as closed
                if (i == 1) {
                    //connect top row to virtual
                    this_loc = xyto1D(i, j);
                    uf.union(top_site_val, this_loc);
                }
                else if (i == n) {
                    //connect top row to virtual
                    this_loc = xyto1D(i, j);
                    uf.union(bottom_site_val, this_loc);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row][col] == 1;
    }

    //one-to-one monotone mapping from 2d to 1d
    private int xyto1D(int r, int c) {
        return (N * (r - 1)) + c;

    }

    public void print_curr_roots() {
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                int this_loc = xyto1D(i, j);
                boolean this_loc_open = isOpen(i, j);
                System.out.format("Loc (%d, %d) has 1d = %d, is_open=%s, "
                                          + "UFRoot=%d\n",
                                  i, j, this_loc, this_loc_open, uf.find(this_loc));
            }
        }
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // TODO: implement check for valid row, cal pair on "true grid"
        // recall n^2-many "true" sites live on [1...N] x [1...N]
        // at most 4 calls to union
        // open the site
        grid[row][col] = 1;
        int this_loc = xyto1D(row, col);
        //check up neighbor
        if (row > 1 && isOpen(row - 1, col)) {
            int up_neighbor = xyto1D(row - 1, col);
            uf.union(this_loc, up_neighbor);
        }
        //check down neighbor
        if (row < N && isOpen(row + 1, col)) {
            int down_neighbor = xyto1D(row + 1, col);
            uf.union(this_loc, down_neighbor);
        }
        //check right neighbor
        if (col < N && isOpen(row, col + 1)) {
            int right_neighbor = xyto1D(row, col + 1);
            uf.union(this_loc, right_neighbor);
        }
        //check left neighbor
        if (col > 1 && isOpen(row, col - 1)) {
            int left_neighbor = xyto1D(row, col - 1);
            uf.union(this_loc, left_neighbor);
        }

    }


    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        /*  A full site is an open site that can be connected to an open site
        in the top row via a chain of neighboring (left, right, up, down) open
        sites.
        We say the system percolates if there is a full site in the bottom row
         */

        int this_loc = xyto1D(row, col);
        return uf.find(this_loc) == uf.find(top_site_val);

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        //TODO
        return 0;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(top_site_val) == uf.find(bottom_site_val);
    }

    // test client (optional)
    public static void main(String[] args) {
        //TODO: start with n = 2; should produce grid of dim 3 x 3
        Percolation myPerc = new Percolation(3);
        myPerc.print_curr_roots();
        int a_loc = myPerc.xyto1D(1, 1);
        int b_loc = myPerc.xyto1D(2, 1);
        System.out.println("Ininitializing Percolation...");
        System.out.println("Num real sites:");
        System.out.println(myPerc.N);
        System.out.format("Does it percolate? : %s\n", myPerc.percolates());
        System.out.format("Root of (1,1) is %d\n", myPerc.uf.find(a_loc));
        System.out.format("Root of (2,1) is %d\n", myPerc.uf.find(b_loc));
        System.out.println("Check site (1,1) is closed: ");
        System.out.println(!myPerc.isOpen(1, 1));
        System.out.println("Opening site (1,1)");
        myPerc.open(1, 1);
        System.out.println("Check site (1,1) is open: ");
        System.out.println(myPerc.isOpen(1, 1));
        System.out.println("Opening site (2,1)");
        myPerc.open(2, 1);
        System.out.println("Check site (1,1) and (2,1) connected: ");

        int root_a = myPerc.uf.find(a_loc);
        int root_b = myPerc.uf.find(b_loc);
        System.out.format("Root of (1,1) is %d\n", root_a);
        System.out.format("Root of (2,1) is %d\n", root_b);
        System.out.format("Does it percolate? : %s\n", myPerc.percolates());
        System.out.println("Opening site (3,3)");
        myPerc.open(3, 3);
        System.out.format("Does it percolate? : %s\n", myPerc.percolates());
        myPerc.print_curr_roots();

    }
}
