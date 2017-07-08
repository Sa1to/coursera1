//package week4;

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] board;
    private final int[][] goal;
    private final int zeroX;
    private final int zeroY;

    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {
//        sdadas();
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
    }


    // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension n
    {
//        sdadas();
        return board.length;
    }

//    private void sdadas() {
//        long c = Counter.getInstance().getC();
//        Counter.getInstance().setC(c + 1);
//    }

    public int hamming()                   // number of blocks out of place
    {
//        sdadas();
        int h = 0;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                if (board[i][j] != goal[i][j] && board[i][j] != 0)
                    h++;
        return h;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
//        sdadas();
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
//        sdadas();
        return Arrays.deepEquals(board, goal);
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
//        sdadas();
        int d = dimension();
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

        if (!(y instanceof Board))
            return false;
        return Arrays.deepEquals(((Board) y).board, this.board);
//        return ((Board) y).hamming() == this.hamming() && ((Board) y).manhattan() == this.manhattan();
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
//        sdadas();
        ArrayList<Board> nb = new ArrayList<>();
        int right = zeroX + 1;
        int left = zeroX - 1;
        int up = zeroY - 1;
        int down = zeroY + 1;
        if (right < dimension()) {
            int[][] newBoard = getBoardCopy();
            swapInTheBoard(newBoard, zeroX, zeroY, right, zeroY);
            nb.add(new Board(newBoard));
        }
        if (left >= 0) {
            int[][] newBoard = getBoardCopy();
            swapInTheBoard(newBoard, zeroX, zeroY, left, zeroY);
            nb.add(new Board(newBoard));
        }
        if (up >= 0) {
            int[][] newBoard = getBoardCopy();
            swapInTheBoard(newBoard, zeroX, zeroY, zeroX, up);
            nb.add(new Board(newBoard));
        }
        if (down < dimension()) {
            int[][] newBoard = getBoardCopy();
            swapInTheBoard(newBoard, zeroX, zeroY, zeroX, down);
            nb.add(new Board(newBoard));
        }
        return nb;
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

//    class BoardComp implements Comparator<Board> {
//
//        @Override
//        public int compare(Board o1, Board o2) {
//            if (o1.equals(o2))
//                return 0;
//            if ((o1.manhattan() + o1.hamming()) > (o2.manhattan() + o2.hamming()))
//                return 1;
//            else
//                return -1;
//        }
//    }

    private void swapInTheBoard(int[][] board, int X1, int Y1, int X2, int Y2) {
        int temp = board[Y1][X1];
        board[Y1][X1] = board[Y2][X2];
        board[Y2][X2] = temp;
    }


    public static void main(String[] args) // unit tests (not graded)
    {
        int n = 3;
        int rand;
        int[][] blocks = new int[n][n];
        ArrayList<Integer> duplicates = new ArrayList<>();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
//                if (j != n - 1 || i != n - 2) {
                rand = StdRandom.uniform(1, 9);
                while (duplicates.contains(rand))
                    rand = StdRandom.uniform(0, 9);
                blocks[i][j] = rand;
                duplicates.add(rand);
//                } else
//                    blocks[i][j] = 0;
            }
        Board initial = new Board(blocks);

//        System.out.println(Arrays.deepToString(initial.getGoal()));
//        System.out.println(Arrays.deepToString(initial.getBoard()));
//
//        System.out.println(initial.toString());
//
//        System.out.println("hamming " + initial.hamming());
//
//        System.out.println("manhattan " + initial.manhattan());
//        Iterable<Board> test = initial.neighbors();
    }
}
