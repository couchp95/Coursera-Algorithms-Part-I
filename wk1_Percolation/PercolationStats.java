/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */


import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int t;
    private final double m;
    private final double d;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n ≤ 0 or trials ≤ 0");
        double[] sum = new double[trials];
        t = trials;
        for (int i = 0; i < trials; i++) {
            Percolation a = new Percolation(n);
            while (!a.percolates()) {
                int x = StdRandom.uniform(1, n + 1);
                int y = StdRandom.uniform(1, n + 1);
                //  System.out.println(x + "\t" + y);
                if (!a.isOpen(x, y))
                    a.open(x, y);
            }
            sum[i] = 1.0 * a.numberOfOpenSites() / (n * n);
        }
        if (t == 1) d = Double.NaN;
        else d = StdStats.stddev(sum);
        m = StdStats.mean(sum);

    }

    // sample mean of percolation threshold
    public double mean() {
        return m;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return d;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (m - 1.96 * d / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (m + 1.96 * d / Math.sqrt(t));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats test = new PercolationStats(n, t);
        System.out.println("mean                    = " + test.mean());
        System.out.println("stddev                  = " + test.stddev());
        System.out.println(
                "95% confidence interval = [" + test.confidenceLo() + ", " + test.confidenceHi()
                        + "]");

    }
}
