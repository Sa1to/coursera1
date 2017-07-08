package week2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Item[] arr;
    private int n;
    private int head;
    private int tail;

    public Deque()                           // construct an empty deque
    {
        arr = (Item[]) new Object[0];
        n = 0;
    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return n == 0;
    }

    public int size()                        // return the number of items on the deque
    {
        return n;
    }

    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null)
            throw new NullPointerException("You can not add null item");
        if (tail == 0)
            resizeBackwards(2 * arr.length);
        else
            tail--;
        arr[tail] = item;
        n++;
    }


    public void addLast(Item item)           // add the item to the end
    {
        if (item == null)
            throw new NullPointerException("You can not add null item");
        if (head + 1 == arr.length || arr.length == 0)
            resizeForward(2 * arr.length);
        else
            head++;
        arr[head] = item;
        n++;
    }

    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty())
            throw new NoSuchElementException("week2.Deque is empty");

        Item item = arr[tail];
        arr[tail] = null;
        tail++;
        n--;
        if (n > 0 && n == arr.length / 4)
            shrink();
        return item;
    }

    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty())
            throw new NoSuchElementException("week2.Deque is empty");

        Item item = arr[head];
        arr[head] = null;
        head--;
        n--;
        if (n > 0 && n == arr.length / 4)
            shrink();
        return item;
    }

    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }

    private void shrink() {
        Item[] copy = (Item[]) new Object[n];
        for (int i = 0; i < n; i++) {
            copy[i] = arr[tail + i];
        }
        arr = copy;
        tail = 0;
        head = arr.length - 1;
    }

    private void resizeForward(int capacity) {
        if (arr.length == 0) {
            handleFirstResizing();
            return;
        }
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = tail; i <= head; i++)
            copy[i] = arr[i];
        arr = copy;
        head++;
    }

    private void resizeBackwards(int capacity) {
        if (arr.length == 0) {
            handleFirstResizing();
            return;
        }
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = (arr.length + head); i >= arr.length; i--)
            copy[i] = arr[i - arr.length];
        tail = arr.length - 1;
        arr = copy;
        head = tail + n;
    }

    private void handleFirstResizing() {
        arr = (Item[]) new Object[1];
        tail = 0;
        head = 0;
    }

    private class DequeIterator implements Iterator<Item> {

        private int i = tail;

        public boolean hasNext() {
            if (n == 0)
                return false;
            return i <= head;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("No more elements to iterate over");
            int j = i;
            i++;
            return arr[j];
        }
    }

    public static void main(String[] args)   // unit testing (optional)
    {

        Deque<Integer> deque = new Deque<>();
        int c = 0;
        while (c == 0) {

            String operation = StdIn.readLine();
            if (operation.equals("end")) {
                Iterator i = deque.iterator();
                while (i.hasNext())
                    StdOut.println(i.next());
                break;
            }
            switch (operation) {
                case "push":
                    int a = Integer.valueOf(StdIn.readLine());
                    deque.addLast(a);
                    StdOut.println("Whole deque");
                    Iterator iterator = deque.iterator();
                    while (iterator.hasNext())
                        StdOut.println(iterator.next());
                    break;
                case "enque":
                    int q = Integer.valueOf(StdIn.readLine());
                    deque.addFirst(q);
                    StdOut.println("Whole deque");
                    Iterator iterator2 = deque.iterator();
                    while (iterator2.hasNext())
                        StdOut.println(iterator2.next());
                    break;
                case "deque":
                    Integer deq = deque.removeLast();
                    StdOut.println(deq);
                    break;
                case "pop":
                    Integer pop = deque.removeFirst();
                    StdOut.println(pop);
                    break;
                case "all":
                    Iterator iterator3 = deque.iterator();
                    while (iterator3.hasNext())
                        StdOut.println(iterator3.next());
                    break;
                case "size":
                    StdOut.println(deque.size());
                    break;

            }

        }
    }
}
