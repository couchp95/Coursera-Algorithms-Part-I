/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!pointSet.contains(p)) pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointSet)
            draw(p);
    }

    private void draw(Point2D p) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(p.x(), p.y());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D s : pointSet) {
            if (rect.contains(s)) {
                q.enqueue(s);
            }
        }
        return q;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double minDist = Double.MAX_VALUE;
        Point2D minP = null;
        for (Point2D s : pointSet) {
            if (p.distanceSquaredTo(s) < minDist) {
                minDist = p.distanceSquaredTo(s);
                minP = s;
            }
        }
        return minP;

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        In file = new In(args[0]);
        PointSET s = new PointSET();
        while (!file.isEmpty()) {
            double x = file.readDouble();
            double y = file.readDouble();
            // System.out.println(x + "," + y);
            s.insert(new Point2D(x, y));
        }
        s.draw();
        RectHV rect = new RectHV(0.25, 0.375, 0.75, 0.75);
        StdDraw.rectangle(0.25, 0.375, 0.25, 0.375 / 2);
        System.out.println(s.range(rect));

    }
}
