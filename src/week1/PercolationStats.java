package week1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] openSites;
    private int trials;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Grid dimensions and trials must be greater then 0!");
        openSites = new double[trials];
        this.trials = trials;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int randomRow = StdRandom.uniform(1, n + 1);
                int randomCol = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(randomRow, randomCol))
                    percolation.open(randomRow, randomCol);
            }
            openSites[i] = ((double) percolation.numberOfOpenSites()) / ((double) (n * n));
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSites);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSites);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / (StrictMath.sqrt(trials)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / (StrictMath.sqrt(trials)));
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        int trials = StdIn.readInt();
        PercolationStats perc = new PercolationStats(n, trials);
        StdIn.readLine();
        StdOut.println("mean = " + perc.mean());
        StdOut.println("stddev = " + perc.stddev());
        StdOut.println("95% confidence interval = " + perc.confidenceLo() + ", " + perc.confidenceHi());
    }
}