import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int size;
    private WeightedQuickUnionUF a;
    private WeightedQuickUnionUF b;
    private boolean[][] map; //  true = open
    private int openSites;
    private boolean percol;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n <=0");
        size = n;
        a = new WeightedQuickUnionUF(size * size + 2);
        b = new WeightedQuickUnionUF(size * size + 1);
        map = new boolean[size + 1][size + 1];
        openSites = 0;
    }

    private int xyTo1D(int x, int y) {
        if (validatingIndex(x, y)) return ((x - 1) * size + y - 1);
        return -1;
    }

    private boolean validatingIndex(int x, int y) {
        if (x < 1 || x > size || y < 1 || y > size)
            throw new IllegalArgumentException("x,y outside prescribed range");
        return true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (validatingIndex(row, col) && !isOpen(row, col)) {
            openSites++;
            map[row][col] = true;
            if (row > 1 && isOpen(row - 1, col)) {
                a.union(xyTo1D(row, col), xyTo1D(row - 1, col));
                b.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            }
            if (row < size && isOpen(row + 1, col)) {
                a.union(xyTo1D(row, col), xyTo1D(row + 1, col));
                b.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            }
            if (col > 1 && isOpen(row, col - 1)) {
                a.union(xyTo1D(row, col), xyTo1D(row, col - 1));
                b.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            }
            if (col < size && isOpen(row, col + 1)) {
                a.union(xyTo1D(row, col), xyTo1D(row, col + 1));
                b.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            }
            if (row == 1) {
                a.union(xyTo1D(row, col), size * size);
                b.union(xyTo1D(row, col), size * size);
            }
            if (row == size) a.union(xyTo1D(row, col), size * size + 1);
            percol = isFull(row, col);

        }
    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (validatingIndex(row, col))
            return (map[row][col]);
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (validatingIndex(row, col))
            return (b.find(xyTo1D(row, col)) == b.find(size * size));
        //  }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // return percol;
        return (a.find(size * size) == a.find(size * size + 1));
    }

    // test client (optional)
    public static void main(String[] args) {
        // empty
    }


}
