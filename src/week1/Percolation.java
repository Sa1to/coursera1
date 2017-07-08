package week1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF unionUF;

    private int[] grid;

    private int openSites;

    private int dimension;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n > 0) {
            createUnionObjectWithVirtualTopAndBottom(n);
            grid = new int[n * n];
            openSites = 0;
            dimension = (int) StrictMath.sqrt(grid.length);
        } else
            throw new IllegalArgumentException("Grid size must be a positive number");
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {

        validateRowAndCol(row, col);
        if (!isOpen(row, col)) {
            openSites++;
            int site = getSitePositionFromCoordinates(row, col);

            grid[site] = 1;

            connectNeighbourIfOpen(row - 1, col, site);
            connectNeighbourIfOpen(row + 1, col, site);
            connectNeighbourIfOpen(row, col - 1, site);
            connectNeighbourIfOpen(row, col + 1, site);

            if (isFull(row, col))
                grid[site] = 2;

            // connect site from last row to virtual bottom only if not percolates and is connected to virtual top
            if (!percolates() && unionUF.connected(site + 1, 0) && isSiteConnectedToLastRow(site))
                unionUF.union(site + 1, grid.length + 1);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateRowAndCol(row, col);

        int site = grid[getSitePositionFromCoordinates(row, col)];

        return (site == 1) || (site == 2);
    }


    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validateRowAndCol(row, col);
        int site = getSitePositionFromCoordinates(row, col);

        return unionUF.connected(0, site + 1) && isOpen(row, col);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionUF.connected(0, grid.length + 1);
    }

    private boolean isSiteConnectedToLastRow(int site) {
        boolean isConnected = false;
        for (int i = grid.length + 1 - dimension; i <= grid.length; i++) {
            int lastRowCol = i - (dimension * (dimension - 1));
            isConnected = isOpen(dimension, lastRowCol) && unionUF.connected(site + 1, i);
            if (isConnected)
                break;
        }
        return isConnected;
    }

    private void connectNeighbourIfOpen(int row, int col, int site) {
        if ((row >= 1) && (row <= dimension) && (col >= 1) && (col <= dimension) && (isOpen(row, col))) {
            int neighbourSite = getSitePositionFromCoordinates(row, col);
            unionUF.union(site + 1, neighbourSite + 1);
            if (isFull(row, col))
                grid[neighbourSite] = 2;
            else
                grid[neighbourSite] = 1;
        }
    }

    private int getSitePositionFromCoordinates(int row, int col) {
        return (row - 1) * dimension + (col - 1);
    }

    private void createUnionObjectWithVirtualTopAndBottom(int n) {
        unionUF = new WeightedQuickUnionUF(n * n + 2); // creates extra entries for virtual top and bottom
        for (int i = 0; i <= n; i++) // connect all first row sites to virtual top
            unionUF.union(0, i);

    }

    private void validateRowAndCol(int row, int col) {
        if (row > (dimension) || col > (dimension) || row <= 0 || col <= 0)
            throw new IndexOutOfBoundsException("index out of bound");
    }
}
