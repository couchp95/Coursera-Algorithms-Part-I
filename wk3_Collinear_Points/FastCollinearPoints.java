/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> segments = new ArrayList<LineSegment>();


    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("points is null");
        int pn = points.length;
        for (int i = 0; i < pn; i++)
            if (points[i] == null) throw new IllegalArgumentException("some points are null");

        Point[] copy = Arrays.copyOf(points, pn);
        Arrays.sort(copy, 0, pn);
        for (int i = 1; i < pn; i++) {
            if ((copy[i].compareTo(copy[i - 1]) == 0))
                throw new IllegalArgumentException("some points are repeated");
        }
        // System.out.println("--------------------");

        for (int i = 0; i < pn; i++) {
            Point[] others = Arrays.copyOf(copy, pn);
            Arrays.sort(others, copy[i].slopeOrder());
            int sum = 1;
            double slp = copy[i].slopeTo(others[0]);
            for (int k = 1; k < pn; k++) {
                if (copy[i].slopeTo(others[k]) == slp) {
                    sum++;
                }
                else {
                    // System.out.println("sum=" + sum + "\tmin=" + minPoint + "\tmax=" + maxPoint);
                    // System.out.println("#copy[i]=" + copy[i] + "\tothers[k]=" + others[k]);
                    if (sum >= 3) {
                        Arrays.sort(others, k - sum, k);
                        if (copy[i].compareTo(others[k - sum]) < 0)
                            segments.add(new LineSegment(copy[i], others[k - 1]));
                    }
                    slp = copy[i].slopeTo(others[k]);
                    sum = 1;


                }
            }
            if (sum >= 3) {
                Arrays.sort(others, pn - sum, pn);
                if (copy[i].compareTo(others[pn - sum]) < 0)
                    segments.add(new LineSegment(copy[i], others[pn - 1]));
            }
        }

    }


    public int numberOfSegments() {
        return segments.size();
    }


    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
