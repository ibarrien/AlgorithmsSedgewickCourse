/* *****************************************************************************
 *  Name:              Ivan
 *  Coursera User ID:  *
 *  Last modified:     Sept 13, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private int num_open_sites; // number of non-virtual open sites
    private int[][] grid;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf_helper;
    private int top_site_val;  // virtual site at top of grid
    private int bottom_site_val;  // virtual site at bottom of grid


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        N = n;
        num_open_sites = 0;
        top_site_val = 0; // val of top size, grid starts at row=1
        bottom_site_val = (int) (Math.pow(n, 2) + 1);  // val of bottom site
        int this_loc; // current 1d site location in loop
        int num_sites = (int) Math.pow(n, 2) + 2;  // 0 to N^2 + 1 indexing; first, last=virtual
        int num_real_sites = (int) Math.pow(n, 2);
        // CREATE THE GRID (does not include virtual sites)
        // use extra row and col for 1-based indexing when calling grid sites
        // e.g. for opening and calls to union find
        grid = new int[n + 1][n + 1];

        //Initializes union-find data structures
        uf = new WeightedQuickUnionUF(num_sites); //id[i] = i
        uf_helper = new WeightedQuickUnionUF(num_real_sites);
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

    // returns the number of open sites
    public int numberOfOpenSites() {
        //If you have a for loop inside of one of your Percolation methods,
        // you're probably doing it wrong
        // TODO: alternative of counting num distinct roots in UnionFind struct?
        return num_open_sites;
    }

    public void check_valid_row_col(int row, int col) {
        // Your code should not attempt to catch any exceptions
        // this will interfere with our grading scripts.
        if (row <= 0 || row > N)
            throw new IndexOutOfBoundsException("row index " + row + " out of bounds");
        if (col <= 0 || col > N)
            throw new IndexOutOfBoundsException("col index " + col + " out of bounds");

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        check_valid_row_col(row, col);
        return grid[row][col] == 1;
    }

    //one-to-one monotone mapping from 2d to 1d
    private int xyto1D(int row, int col) {
        check_valid_row_col(row, col);
        return (N * (row - 1)) + col;

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // recall n^2-many "true" sites live on [1...N] x [1...N]
        // at most 4 calls to union
        // open the site
        check_valid_row_col(row, col);
        if (grid[row][col] == 0) {
            grid[row][col] = 1;
            num_open_sites++;
        }
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
         */
        boolean bottom_left_check = true;
        boolean bottom_right_check = true;
        check_valid_row_col(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        int this_loc = xyto1D(row, col);
        // TODO: add two conditions to prevent backwash
        // if (row, col) is bottom site -> is it connected to a full bottom site?
        if (row == N) {
            if (isFull(N - 1, col)) {
                return true;
            }
            //look left of this bottom site
            if (col > 1) {
                if (col - 1 > 1) {
                    bottom_left_check = isFull(row, col - 1);
                }
                else {
                    int left_loc = xyto1D(row, col - 1);
                    bottom_left_check = uf_helper.find(left_loc) == uf_helper.find(this_loc);
                }
                // look right of this bottom site
                if (col < N) {
                    if (col + 1 < N) {
                        bottom_right_check = isFull(row, col + 1);
                    }
                    else {
                        int right_loc = xyto1D(row, col + 1);
                        bottom_right_check = uf_helper.find(right_loc) == uf_helper.find(this_loc);
                    }
                }

            }

        }
        boolean bottom_check = bottom_left_check && bottom_right_check;
        return uf.find(this_loc) == uf.find(top_site_val) && bottom_check;

    }


    // does the system percolate?
    public boolean percolates() {
        // We say the system percolates if there is a full site in the bottom row
        return uf.find(top_site_val) == uf.find(bottom_site_val);
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

    // test client (optional)
    public static void main(String[] args) {
        //TODO: start with n = 2; should produce grid of dim 3 x 3
        Percolation myPerc = new Percolation(3);
        // myPerc.print_curr_roots();
        System.out.format("Num open sites %d\n", myPerc.numberOfOpenSites());
        System.out.format("Check site (3,3) is full: %s\n", myPerc.isFull(3, 3));
        myPerc.open(3, 3);
        myPerc.open(1, 1);
        myPerc.open(2, 1);
        myPerc.open(3, 1);  // should be false...consider two union find structs?
        // if a bottom site is full, create its own union find struct?
        // WANT: check if any bottom *direct* neighbors are properly full
        // or at least open
        // IDEA: maintain one union_find bottom and one union_find top
        System.out.format("Check site (3,3) is full: %s\n", myPerc.isFull(3, 3));
        System.out.format("Check site (3,1) is full: %s\n", myPerc.isFull(3, 1));
        System.out.format("Num open sites %d\n", myPerc.numberOfOpenSites());




        /*
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
        System.out.format("Num open sites %d\n", myPerc.numberOfOpenSites());
        System.out.println("Opening site (2,1)");
        myPerc.open(2, 1);
        System.out.format("Num open sites %d\n", myPerc.numberOfOpenSites());
        System.out.println("Check site (1,1) and (2,1) connected: ");

        int root_a = myPerc.uf.find(a_loc);
        int root_b = myPerc.uf.find(b_loc);
        System.out.format("Root of (1,1) is %d\n", root_a);
        System.out.format("Root of (2,1) is %d\n", root_b);
        System.out.format("Does it percolate? : %s\n", myPerc.percolates());
        System.out.println("Opening site (3,2)");
        myPerc.open(3, 2);
        System.out.format("Does it percolate? : %s\n", myPerc.percolates());
        //myPerc.print_curr_roots();
        System.out.println("Opening site (2,2)");
        myPerc.open(2, 2);
        System.out.format("Does it percolate? : %s\n", myPerc.percolates());
        //myPerc.print_curr_roots();
        System.out.format("Num open sites %d\n", myPerc.numberOfOpenSites());

        System.out.println("Try Opening illegal site (1,4)");
        myPerc.open(1, 4);

         */
    }
}
