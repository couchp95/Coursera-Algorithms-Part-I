/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int n;
    private char[][] board;
    private int ham, man;
    private byte blankx, blanky;
    private byte rndx1, rndy1, rndx2, rndy2;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = new char[n][n];
        ham = 0;
        man = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                board[i][j] = (char) tiles[i][j];

                if (board[i][j] == 0) {
                    blankx = (byte) i;
                    blanky = (byte) j;
                    continue;
                }
                if (board[i][j] != 0 && board[i][j] != (i * n + j + 1)) ham++;
                int x1;
                if (board[i][j] % n == 0) x1 = board[i][j] / n - 1;
                else x1 = board[i][j] / n;
                byte y1 = (byte) Math.floorMod(board[i][j] - x1 * n - 1, n);
                man = man + Math.abs(x1 - i) + Math.abs(y1 - j);


            }
        do {
            rndx1 = (byte) StdRandom.uniform(n);
            rndy1 = (byte) StdRandom.uniform(n);
            rndx2 = (byte) StdRandom.uniform(n);
            rndy2 = (byte) StdRandom.uniform(n);
        } while ((rndx1 == rndx2 && rndy1 == rndy2) || board[rndx1][rndy1] == 0
                || board[rndx2][rndy2] == 0);
        // System.out.println("random:" + rndx + "," + rndy);

    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", (int) board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return ham;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return man;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (ham == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.board.length != n) return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (that.board[i][j] != this.board[i][j]) return false;
        return true;
    }

    private Board next(int x, int y) {
        int[][] nbr = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                nbr[i][j] = board[i][j];
        // System.out.println("blank:" + blankx + "," + blanky + "\tx,y=" + x + "," + y);
        nbr[blankx][blanky] = nbr[x][y];
        nbr[x][y] = 0;
        Board nbrBoard = new Board(nbr);
        return nbrBoard;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> nbrq = new Queue<Board>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == j) continue;
                if (i != 0 && j != 0) continue;
                // System.out.println("i,j=" + i + "," + j);
                if (blankx + i >= 0 && blankx + i < n && blanky + j >= 0 && blanky + j < n) {
                    nbrq.enqueue(next(blankx + i, blanky + j));
                    // System.out.print((blankx + i) + "\t" + (blanky + j));
                }
                //  System.out.println();
            }

        }
        return nbrq;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twin = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                twin[i][j] = board[i][j];
        int tmp = twin[rndx1][rndy1];
        twin[rndx1][rndy1] = twin[rndx2][rndy2];
        twin[rndx2][rndy2] = tmp;
        return new Board(twin);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] test = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] test1 = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        int[][] test2 = { { 1, 2, 3 }, { 5, 7, 6 }, { 0, 4, 8 } };
        Board b = new Board(test);
        Board a = new Board(test2);
        System.out.println(b);
        System.out.println(b.twin());
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.equals(a));
        Queue<Board> q = (Queue<Board>) a.neighbors();
        for (Board qq : q)
            System.out.println(qq);
/*
        int[][] tens = new int[10][10];
        for (int i = 1; i <= 10; i++)
            for (int j = 1; j <= 10; j++)
                tens[i - 1][j - 1] = (i - 1) * 10 + j;
        tens[9][9] = 0;
        System.out.println(new Board(tens).manhattan());
  */
    }
}
