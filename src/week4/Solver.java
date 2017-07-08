//package week4;

import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    private final MinPQ<Map.Entry<Board, Board>> pq;
    private final ArrayList<Map.Entry<Board, Board>> results;
    private final ArrayList<Board> solution;
    private final Board initial;
    private boolean solvable;

    public Solver(Board initial) {
        solution = new ArrayList<>();
        pq = new MinPQ<>(new BoardComp());
        results = new ArrayList<>();
        this.initial = initial;
        this.solvable = false;
    }           // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        if (results.isEmpty()) {
            Board board = initial;
            ArrayList<Map.Entry<Board, Board>> used = new ArrayList<>();
            ArrayList<Map.Entry<Board, Board>> usedTwin = new ArrayList<>();

            results.add(new AbstractMap.SimpleEntry<>(null, board));
            Map.Entry<Board, Board> entryBoard = null;

            MinPQ<Map.Entry<Board, Board>> pqTwin = new MinPQ<>(new BoardComp());
            Board twin = board.twin();
            Map.Entry<Board, Board> twinEntryBoard = null;

            while (!board.isGoal() && !twin.isGoal()) {
                Iterable<Board> neighbors = board.neighbors();
                Iterable<Board> twinNeighbors = twin.neighbors();

                addNeighbors(used, pq, board, entryBoard, neighbors);
                addNeighbors(usedTwin, pqTwin, twin, twinEntryBoard, twinNeighbors);

                entryBoard = pq.delMin();
                board = entryBoard.getValue();

                twinEntryBoard = pqTwin.delMin();
                twin = twinEntryBoard.getValue();

                results.add(entryBoard);

            }
            if (board.isGoal())
                solvable = true;
        }
        return solvable;
    }        // is the initial board solvable?

    private void addNeighbors(ArrayList<Map.Entry<Board, Board>> used, MinPQ<Map.Entry<Board, Board>> pq, Board board, Map.Entry<Board, Board> entryBoard, Iterable<Board> neighbors) {
        for (Board n : neighbors) {
            Map.Entry<Board, Board> e = new AbstractMap.SimpleEntry<>(board, n);
            if (entryBoard == null || (!n.equals(entryBoard.getKey()) && !used.contains(e))) {
                pq.insert(e);
                used.add(e);
            }
        }
    }

    public int moves() {
        if (solution.isEmpty())
            solution();
        if (isSolvable())
            return solution.size() - 1;
        else
            return -1;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        if (results.isEmpty())
            isSolvable();
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

    private void solve(Board board) throws InterruptedException {
        boolean solvable = isSolvable();
        if (solvable) {
            solution();
            System.out.println("Minimum number of moves = " + moves());
            for (Board b : solution)
                System.out.println(b);
        } else
            System.out.println("No solution possible");
//        System.out.println("NUMBER " + (solution.size() -1));
    }

    private class BoardComp implements Comparator<Map.Entry<Board, Board>> {


        @Override
        public int compare(Map.Entry<Board, Board> m1, Map.Entry<Board, Board> m2) {
            Board o1 = m1.getValue();
            Board o2 = m2.getValue();

            if (o1.equals(o2))
                return 0;
            if ((o1.manhattan() + o1.hamming()) > (o2.manhattan() + o2.hamming()))
                return 1;
            else
                return -1;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        int n = 3;
        int rand;
        int[][] blocks =
                {{1, 2, 3, 4, 5, 6, 7, 8, 9},
                        {10, 11, 12, 13, 14, 15, 16, 17, 18},
                        {19, 20, 21, 22, 23, 24, 25, 26, 27},
                        {28, 29, 30, 31, 32, 33, 34, 35, 36},
                        {37, 38, 39, 40, 41, 42, 43, 44, 45},
                        {46, 47, 48, 49, 50, 51, 52, 53, 54},
                        {55, 56, 57, 58, 59, 60, 61, 62, 63},
                        {64, 0, 65, 67, 68, 78, 69, 70, 72},
                        {73, 74, 66, 75, 76, 77, 79, 71, 80}};
//        int[][] blocks =
//                {{2, 3, 4, 8},
//                        {1, 6, 0, 12},
//                        {5, 10, 7, 11},
//                        {9, 13, 14, 15}
//                };
//        int[][] blocks =
//                {{1, 0},
//                        {3, 2}};
        ArrayList<Integer> duplicates = new ArrayList<>();
//        for (int i = 0; i < n; i++)
//            for (int j = 0; j < n; j++) {
////                if (j != n - 1 || i != n - 2) {
//                rand = StdRandom.uniform(1, 9);
//                while (duplicates.contains(rand))
//                    rand = StdRandom.uniform(0, 9);
//                blocks[i][j] = rand;
//                duplicates.add(rand);
////                } else
////                    blocks[i][j] = 0;
//            }


        Board initial = new Board(blocks);
        System.out.println(initial);
        Solver solver = new Solver(initial);
        solver.solve(initial);
//        System.out.println(Counter.getInstance().getC());
    } // solve a slider puzzle (given below)
}