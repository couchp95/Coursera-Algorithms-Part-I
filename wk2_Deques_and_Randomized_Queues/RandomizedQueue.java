import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[1];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void resize(int capacity) {
        assert capacity >= n;
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = a[i];
        }
        a = copy;
        //     for (int j = 0; j < n; j++) System.out.print(a[j] + " ");
        //     System.out.println("n=" + n);
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int s = StdRandom.uniform(n);
        Item tmp = a[n - 1];
        a[n - 1] = a[s];
        a[s] = tmp;
        Item item = a[n - 1];
        a[n - 1] = null;
        n--;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int s = StdRandom.uniform(n);
        return a[s];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        // StdRandom.shuffle(a, 0, n);
        return new ReverseArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ReverseArrayIterator implements Iterator<Item> {
        private int i;
        private int[] permutation;

        public ReverseArrayIterator() {
            i = n - 1;
            permutation = StdRandom.permutation(n);
        }


        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[permutation[i--]];
        }

    }


    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

}
