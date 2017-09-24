 package week4;

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] board;
    private final int[][] goal;
    private final int zeroX;
    private final int zeroY;
    private final int manhattan;
    private final int hamming;

    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {
        int dim = blocks.length;
        int[] oneDimBoard = new int[dim * dim];
        int zeroInOneDim = 0;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++) {
                oneDimBoard[i * dim + j] = blocks[i][j];
                if (blocks[i][j] == 0)
                    zeroInOneDim = i * dim + j;
            }

        int[] g = oneDimBoard.clone();
        zeroY = zeroInOneDim / dim;
        zeroX = zeroInOneDim - dim * zeroY;
        Arrays.sort(g);
        int[] oneDimGoal = new int[oneDimBoard.length];
        for (int i = 0; i < g.length - 1; i++)
            oneDimGoal[i] = g[i + 1];
        oneDimGoal[oneDimGoal.length - 1] = 0;

        board = blocks.clone();
        goal = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                goal[i][j] = oneDimGoal[j + i * dim];
            }

        }

        hamming = countHamming();
        manhattan = countManhattan();
    }


    public int dimension()                 // board dimension n
    {
        return board.length;
    }

    public int hamming()                   // number of blocks out of place
    {
        return hamming;
    }

    private int countHamming() {
        int h = 0;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                if (board[i][j] != goal[i][j] && board[i][j] != 0)
                    h++;
        return h;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        return manhattan;
    }

    private int countManhattan() {
        int position;
        int m = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++)
                for (int k = 0; k < dimension(); k++) {
                    position = Arrays.binarySearch(goal[k], board[i][j]);
                    if (position >= 0) {
                        m = m + (Math.abs(i - k) + Math.abs(j - position));
                        break;
                    }
                }

        }
        return m;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        return Arrays.deepEquals(board, goal);
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        int x1 = StdRandom.uniform(0, dimension());
        int y1 = StdRandom.uniform(0, dimension());
        int x2 = StdRandom.uniform(0, dimension());
        int y2 = StdRandom.uniform(0, dimension());
        while ((x1 == zeroX && y1 == zeroY) || (x2 == zeroX && y2 == zeroY) || (x1 == x2 && y1 == y2)) {
            x1 = StdRandom.uniform(0, dimension());
            y1 = StdRandom.uniform(0, dimension());
            x2 = StdRandom.uniform(0, dimension());
            y2 = StdRandom.uniform(0, dimension());
        }
        int[][] newBoard = getBoardCopy();
        swapInTheBoard(newBoard, x1, y1, x2, y2);
        return new Board(newBoard);
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        return (y != null) && (y.getClass() == this.getClass()) && (Arrays.deepEquals(((Board) y).board, this.board));
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        ArrayList<Board> nb = new ArrayList<>();
        int right = zeroX + 1;
        int left = zeroX - 1;
        int up = zeroY - 1;
        int down = zeroY + 1;
        if (right < dimension()) {
            int[][] newBoard = getBoardCopy();
            swapInTheBoard(newBoard, zeroX, zeroY, right, zeroY);
            addNeighbour(nb, newBoard);
        }
        if (left >= 0) {
            int[][] newBoard = getBoardCopy();
            swapInTheBoard(newBoard, zeroX, zeroY, left, zeroY);
            addNeighbour(nb, newBoard);
        }
        if (up >= 0) {
            int[][] newBoard = getBoardCopy();
            swapInTheBoard(newBoard, zeroX, zeroY, zeroX, up);
            addNeighbour(nb, newBoard);
        }
        if (down < dimension()) {
            int[][] newBoard = getBoardCopy();
            swapInTheBoard(newBoard, zeroX, zeroY, zeroX, down);
            addNeighbour(nb, newBoard);
        }
        return nb;
    }

    private void addNeighbour(ArrayList<Board> nb, int[][] newBoard) {
        Board newNeighbour = new Board(newBoard);
//        newNeighbour.movesDoneSoFar = this.movesDoneSoFar + 1;
        nb.add(newNeighbour);
    }

    private int[][] getBoardCopy() {
        int[][] newBoard;
        newBoard = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++)
            System.arraycopy(board[i], 0, newBoard[i], 0, board.length);
        return newBoard;
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private void swapInTheBoard(int[][] boardToSwapIn, int x1, int y1, int x2, int y2) {
        int temp = boardToSwapIn[y1][x1];
        boardToSwapIn[y1][x1] = boardToSwapIn[y2][x2];
        boardToSwapIn[y2][x2] = temp;
    }


    public static void main(String[] args) // unit tests (not graded)
    {
//        int n = 3;
//        int rand;
//        int[][] blocks = new int[n][n];
//        ArrayList<Integer> duplicates = new ArrayList<>();
//        for (int i = 0; i < n; i++)
//            for (int j = 0; j < n; j++) {
//                rand = StdRandom.uniform(1, 9);
//                while (duplicates.contains(rand))
//                    rand = StdRandom.uniform(0, 9);
//                blocks[i][j] = rand;
//                duplicates.add(rand);
//            }

    }
}
