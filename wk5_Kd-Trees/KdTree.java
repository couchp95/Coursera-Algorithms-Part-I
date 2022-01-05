/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final boolean XC = true;
    private Node root;
    private double minDistance;
    private Point2D closestPoint;
    private int size;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean lv;

        public Node(Point2D p, RectHV r, boolean lv) {
            this.p = p;
            this.rect = r;
            this.lv = lv;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        minDistance = Double.MAX_VALUE;
        size = 0;
        closestPoint = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (root == null);
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private int compare(Point2D p, Node x) {
        if (x.lv == XC) return Point2D.X_ORDER.compare(p, x.p);
        else return Point2D.Y_ORDER.compare(p, x.p);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, true, 0, 0, 1, 1);
    }

    private Node insert(Node x, Point2D p, boolean lv, double xmin, double ymin, double xmax,
                        double ymax) {
        if (x == null) {
            size++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax), lv);
        }
        int cmp;
        if (lv == XC) cmp = Point2D.X_ORDER.compare(p, x.p);
        else cmp = Point2D.Y_ORDER.compare(p, x.p);

        if (cmp < 0) {
            if (lv == XC) {
                x.lb = insert(x.lb, p, !lv, xmin, ymin, x.p.x(), ymax);
            }
            else {
                x.lb = insert(x.lb, p, !lv, xmin, ymin, xmax, x.p.y());
            }
        }
        else if (cmp > 0) {
            if (lv == XC) {
                x.rt = insert(x.rt, p, !lv, x.p.x(), ymin, xmax, ymax);
            }
            else {
                x.rt = insert(x.rt, p, !lv, xmin, x.p.y(), xmax, ymax);
            }
        }
        else {
            if (lv == XC) {
                if (Double.compare(x.p.y(), p.y()) == 0) {
                    x.p = p;
                }
                else {
                    x.rt = insert(x.rt, p, !lv, x.p.x(), ymin, xmax, ymax);
                }
            }
            else {
                if (Double.compare(x.p.x(), p.x()) == 0) {
                    x.p = p;
                }
                else {
                    x.rt = insert(x.rt, p, !lv, xmin, x.p.y(), xmax, ymax);
                }
            }
        }
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) return false;
        int cmp = compare(p, x);
        if (cmp < 0) return contains(x.lb, p);
        else if (cmp > 0) return contains(x.rt, p);
        else if (p.compareTo(x.p) != 0) return contains(x.rt, p);
        else return true;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;

        // System.out.println(x.p);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(x.p.x(), x.p.y());
        StdDraw.setPenRadius(0.002);
        if (x.lv == XC) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        draw(x.lb);
        draw(x.rt);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<Point2D>();
        if (rect == null) throw new IllegalArgumentException();
        range(root, rect, q);
        return q;
    }

    private void range(Node x, RectHV r, Queue<Point2D> q) {
        if (x != null) {
            if (r.contains(x.p)) q.enqueue(x.p);
            if (x.lb != null && x.lb.rect.intersects(r)) range(x.lb, r, q);
            if (x.rt != null && x.rt.rect.intersects(r)) range(x.rt, r, q);
        }
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        closestPoint = null;
        minDistance = Double.MAX_VALUE;
        nearest(root, p);
        return closestPoint;
    }

    private void nearest(Node x, Point2D p) {
        if (x == null || x.rect.distanceSquaredTo(p) > minDistance) return;

        if (x.p.distanceSquaredTo(p) < minDistance) {
            minDistance = x.p.distanceSquaredTo(p);
            closestPoint = x.p;
        }
        if (x.lb != null && x.lb.rect.contains(p)) {
            nearest(x.lb, p);
            nearest(x.rt, p);
        }
        else if (x.rt != null && x.rt.rect.contains(p)) {
            nearest(x.rt, p);
            nearest(x.lb, p);
        }
        else if (x.lb != null && x.rt != null) {
            if (x.lb.rect.distanceSquaredTo(p) < x.rt.rect
                    .distanceSquaredTo(p)) {
                nearest(x.lb, p);
                nearest(x.rt, p);
            }
            else {
                nearest(x.rt, p);
                nearest(x.lb, p);
            }
        }
        else if (x.lb != null) {
            nearest(x.lb, p);
            nearest(x.rt, p);
        }
        else {
            nearest(x.rt, p);
            nearest(x.lb, p);
        }
    }

    public static void main(String[] args) {
        In file = new In(args[0]);
        KdTree s = new KdTree();
        while (!file.isEmpty()) {
            double x = file.readDouble();
            double y = file.readDouble();
            // System.out.println(x + "," + y);
            s.insert(new Point2D(x, y));

        }
        System.out.println(s.size());
        s.draw();
        Point2D p = new Point2D(0.169, 0.807);
        p.draw();
        System.out.println(s.nearest(p));
    }
}
