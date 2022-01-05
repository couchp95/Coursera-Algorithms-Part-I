import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int ln;

    private LineNode first;
    private LineNode last;

    private class LineNode {
        private LineSegment item;
        private LineNode next;

    }

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("points is null");
        int pn = points.length;
        Point[] copy = new Point[pn];
        for (int i = 0; i < pn; i++)
            if (points[i] == null) throw new IllegalArgumentException("some points are null");
            else copy[i] = points[i];
        Arrays.sort(copy, 0, pn);
        for (int i = 1; i < pn; i++) {
            if ((copy[i].compareTo(copy[i - 1]) == 0))
                throw new IllegalArgumentException("some points are null or repeated");
        }
        for (int i = 0; i < pn; i++)
            for (int j = i + 1; j < pn; j++)
                for (int k = j + 1; k < pn; k++)
                    for (int m = k + 1; m < pn; m++) {
                        double slp1 = copy[i].slopeTo(copy[j]);
                        double slp2 = copy[i].slopeTo(copy[k]);
                        double slp3 = copy[i].slopeTo(copy[m]);
                        if (slp1 == slp2 && slp1 == slp3)
                            lineadd(copy[i], copy[m]);
                    }

    }

    public int numberOfSegments() {
        return ln;
    }

    private void lineadd(Point p1, Point p2) {
        ln++;
        if (first == null) {
            first = new LineNode();
            first.item = new LineSegment(p1, p2);
            last = first;
        }
        else {
            LineNode oldlast = last;
            last = new LineNode();
            last.item = new LineSegment(p1, p2);
            oldlast.next = last;
        }
    }

    public LineSegment[] segments() {
        LineSegment[] ls = new LineSegment[ln];
        LineNode current = first;
        for (int i = 0; i < ln; i++) {
            ls[i] = current.item;
            current = current.next;
        }
        return ls;

    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
