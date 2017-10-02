 package week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class Solver {
    private final ArrayList<Map.Entry<Board, Board>> results;
    private final ArrayList<Board> solution;
    private Board initial;
    private boolean solvable;

    public Solver(Board initial) {
        solution = new ArrayList<>();
        results = new ArrayList<>();
        this.initial = initial;
        this.solvable = false;
    }           // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        MinPQ<Map.Entry<Map.Entry<Board, Integer>, Board>> pq = new MinPQ<>(new BoardComp());

        if (results.isEmpty()) {
            Board board = initial;
            List<Board> used = new ArrayList<>();
            List<Board> usedTwin = new ArrayList<>();

            Map.Entry<Map.Entry<Board, Integer>, Board> entryBoard =
                    new AbstractMap.SimpleEntry<>(new AbstractMap.SimpleEntry<Board, Integer>(null, 0), board);

            MinPQ<Map.Entry<Map.Entry<Board, Integer>, Board>> pqTwin = new MinPQ<>(new BoardComp());
            Board twin = board.twin();
            Map.Entry<Map.Entry<Board, Integer>, Board> twinEntryBoard =
                    new AbstractMap.SimpleEntry<>(new AbstractMap.SimpleEntry<Board, Integer>(null, 0), twin);

            AbstractMap.SimpleEntry<Board, Board> initialBoardEntrySimplified = new AbstractMap.SimpleEntry<>(null, board);
            results.add(initialBoardEntrySimplified);

            while (!board.isGoal() && !twin.isGoal()) {
                Iterable<Board> neighbors = board.neighbors();
                Iterable<Board> twinNeighbors = twin.neighbors();

                addNeighbors(used, pq, board, entryBoard, neighbors);
                addNeighbors(usedTwin, pqTwin, twin, twinEntryBoard, twinNeighbors);

                entryBoard = pq.delMin();
                board = entryBoard.getValue();

                twinEntryBoard = pqTwin.delMin();
                twin = twinEntryBoard.getValue();

                Map.Entry<Board, Board> entryToResults =
                        new AbstractMap.SimpleEntry<>(entryBoard.getKey().getKey(), entryBoard.getValue());
                results.add(entryToResults);

            }
            if (board.isGoal())
                solvable = true;
        }
        return solvable;
    }        // is the initial board solvable?

    private void addNeighbors(Collection<Board> used,
                              MinPQ<Map.Entry<Map.Entry<Board, Integer>, Board>> queue,
                              Board board,
                              Map.Entry<Map.Entry<Board, Integer>, Board> entryBoard,
                              Iterable<Board> neighbors) {
        for (Board n : neighbors) {
            Map.Entry<Board, Integer> key = new AbstractMap.SimpleEntry<>(board, entryBoard.getKey().getValue() + 1);
            Map.Entry<Map.Entry<Board, Integer>, Board> e = new AbstractMap.SimpleEntry<>(key, n);
            if (entryBoard.getKey().getKey() == null || !n.equals(entryBoard.getKey().getKey()) && !used.contains(e.getValue())) {
                queue.insert(e);
                used.add(e.getValue());
            }
        }
    }

    public int moves() {
        if (solution.isEmpty()) {
            solution();
        }
        if (isSolvable()) {
            return solution.size() - 1;
        } else
            return -1;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {

        if (results.isEmpty() && !isSolvable()) {
            return null;
        }
        int lastInChain = results.size() - 1;
        for (int i = results.size() - 1; i >= 0; i--) {
            if (i == 0)
                solution.add(results.get(i).getValue());
            else {
                Map.Entry<Board, Board> curr = results.get(i);
                if (i == results.size() - 1 || curr.getValue().equals(results.get(lastInChain).getKey())) {
                    solution.add(curr.getValue());
                    lastInChain = i;
                }
            }
        }

        Collections.reverse(solution);
        return solution;
    }      // sequence of boards in a shortest solution; null if unsolvable

    private void solve(Board board) {
        initial = board;
        if (isSolvable()) {
            if (solution.isEmpty()) {
                solution();
            }
            System.out.println("Minimum number of moves = " + moves());
            for (Board b : solution) {
                System.out.println(b);
            }
        } else
            System.out.println("No solution possible");
    }

    private static class BoardComp implements Comparator<Map.Entry<Map.Entry<Board, Integer>, Board>> {


        @Override
        public int compare(Map.Entry<Map.Entry<Board, Integer>, Board> m1, Map.Entry<Map.Entry<Board, Integer>, Board> m2) {
            if (m1.equals(m2))
                return 0;

            Board o1 = m1.getValue();
            Board o2 = m2.getValue();

            if (o1.equals(o2))
                return 0;
            if ((o1.manhattan() + m1.getKey().getValue() + o1.hamming() + m1.getKey().getValue())
                    >
                    (o2.manhattan() + m2.getKey().getValue() + o2.hamming() + m2.getKey().getValue()))
                return 1;
            else
                return -1;
        }
    }

    public static void main(String[] args) {

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            Solver solver = new Solver(initial);
            solver.solve(initial);
            StdOut.println(filename + ": " + solver.moves());
        }
    }
//    public static void main(int[][] args) {
////        int n = 3;
////        int rand;
////        int[][] blocks =
////                {{1, 2, 3, 4, 5, 6, 7, 8, 9},
////                        {10, 11, 12, 13, 14, 15, 16, 17, 18},
////                        {19, 20, 21, 22, 23, 24, 25, 26, 27},
////                        {28, 29, 30, 31, 32, 33, 34, 35, 36},
////                        {37, 38, 39, 40, 41, 42, 43, 44, 45},
////                        {46, 47, 48, 49, 50, 51, 52, 53, 54},
////                        {55, 56, 57, 58, 59, 60, 61, 62, 63},
////                        {64, 0, 65, 67, 68, 78, 69, 70, 72},
////                        {73, 74, 66, 75, 76, 77, 79, 71, 80}};
////        int[][] blocks =
////                {{2, 3, 4, 8},
////                        {1, 6, 0, 12},
////                        {5, 10, 7, 11},
////                        {9, 13, 14, 15}
////                };
//        int[][] blocks =
//                {{5, 2, 3},
//                        {4, 7, 0},
//                        {8, 6, 1}
//                };
////        int[][] blocks =
////                {{1, 0},
////                        {3, 2}};
////        for (int i = 0; i < n; i++)
////            for (int j = 0; j < n; j++) {
//////                if (j != n - 1 || i != n - 2) {
////                rand = StdRandom.uniform(1, 9);
////                while (duplicates.contains(rand))
////                    rand = StdRandom.uniform(0, 9);
////                blocks[i][j] = rand;
////                duplicates.add(rand);
//////                } else
//////                    blocks[i][j] = 0;
////            }
//        Board initial = new Board(blocks);
//        System.out.println(initial);
//        Solver solver = new Solver(initial);
//        solver.solve(initial);
//        System.out.println(solver.moves());
//    } // solve a slider puzzle (given below)
}