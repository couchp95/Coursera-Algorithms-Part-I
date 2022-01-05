/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // private MinPQ<SB> minPQ;
    // private MinPQ<SB> twinPQ;
    private Queue<Board> q;
    private SB d;
    // private int moves;
    // private int pri;
    // private Board previous;
    private boolean solvable;
    private int minMoves;

    private class SB implements Comparable<SB> {
        public Board bd;
        public SB previous;
        private int pri;
        public int moves;


        private SB(Board inputBD) {
            bd = inputBD;
            previous = null;
            pri = 0;
            // pri = bd.manhattan() + moves;
        }

        public int compareTo(SB that) {
            int a = Integer.compare(this.pri, that.pri);
            if (a != 0) return a;
            if (this.bd.manhattan() < that.bd.manhattan()) return -1;
            else return 1;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        SB init = new SB(initial);
        SB twin = new SB(initial.twin());
        init.moves = 0;
        twin.moves = 0;
        init.previous = null;
        twin.previous = null;
        init.pri = init.bd.manhattan() + init.moves;
        twin.pri = twin.bd.manhattan() + twin.moves;
        // minPQ = new MinPQ<SB>();
        // minPQ.insert(init);
        // twinPQ = new MinPQ<SB>();
        // twinPQ.insert(twin);
        // SB d = minPQ.delMin();
        d = init;
        SB t = twin;
        // SB t = twinPQ.delMin();
        q = new Queue<Board>();
        q.enqueue(d.bd);
        MinPQ<SB> minPQ = new MinPQ<SB>();
        MinPQ<SB> twinPQ = new MinPQ<SB>();
        while (!d.bd.isGoal() && !t.bd.isGoal()) {
            for (Board b : d.bd.neighbors()) {
                if (d.previous != null && b.equals(d.previous.bd)) continue;
                SB nbr = new SB(b);
                nbr.moves = d.moves + 1;
                nbr.pri = nbr.bd.manhattan() + nbr.moves;
                nbr.previous = d;
                minPQ.insert(nbr);
            }
            //   if (d.bd.isGoal() || t.bd.isGoal())
            //       System.out.println(d.bd.hamming() + "\t" + t.bd.hamming());
            d = minPQ.delMin();
            q.enqueue(d.bd);
            // System.out.println(d.bd);
            for (Board p : t.bd.neighbors()) {
                if (t.previous != null && p.equals(t.previous.bd)) continue;
                SB nbr = new SB(p);
                nbr.moves = t.moves + 1;
                nbr.pri = nbr.bd.manhattan() + nbr.moves;
                nbr.previous = t;
                twinPQ.insert(nbr);
            }
            t = twinPQ.delMin();
            // System.out.println(t.bd);
        }
        // System.out.println(d.bd + "\n" + t.bd);
        if (d.bd.isGoal()) {
            solvable = true;
            minMoves = d.moves;
        }
        else {
            solvable = false;
            minMoves = -1;
        }


    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return minMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solvable != true) return null;
        SB temp = d;
        Stack<Board> stack = new Stack<Board>();
        while (temp.previous != null) {
            stack.push(temp.bd);
            temp = temp.previous;
        }
        stack.push(temp.bd);
        return stack;
    }

    // test client (see below)
    public static void main(String[] args) {
        /*
        int[][] test = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] test1 = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        int[][] test2 = { { 1, 2, 3 }, { 4, 5, 6 }, { 8, 7, 0 } };
        Board b = new Board(test);
        Board a = new Board(test2);
        System.out.println(a);
        Solver solver = new Solver(a);
                 */
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
