package week2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] arr;
    private int n;
    private int frontPosition;

    public RandomizedQueue()// construct an empty randomized queue
    {
        arr = (Item[]) new Object[0];
        n = 0;
    }

    public boolean isEmpty()                 // is the queue empty?
    {
        return n == 0;
    }

    public int size()                        // return the number of items on the queue
    {
        return n;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null)
            throw new NullPointerException("You can not add null item");

        if (n == 0) {
            arr = (Item[]) new Object[1];
            frontPosition = 0;
            arr[0] = item;
        } else {
            if ((arr.length - 1) == frontPosition)
                resize(arr.length * 2);
            frontPosition++;
            arr[frontPosition] = item;
        }
        n++;
    }


    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty())
            throw new NoSuchElementException("week2.RandomizedQueue is empty");

        int elementToDequeue;

        if (n > 0 && n == arr.length / 4)
            resize(arr.length / 2);

        do {
            elementToDequeue = StdRandom.uniform(frontPosition + 1);
        }
        while (arr[elementToDequeue] == null);


        Item dequeuedItem = arr[elementToDequeue];
        arr[elementToDequeue] = null;
        n--;
        return dequeuedItem;
    }

    public Item sample()                     // return (but do not remove) a random item
    {
        if (isEmpty())
            throw new NoSuchElementException("week2.RandomizedQueue is empty");

        int elementToDequeue;
        do {
            elementToDequeue = StdRandom.uniform(frontPosition + 1);
        }
        while (arr[elementToDequeue] == null);

        return arr[elementToDequeue];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                copy[j] = arr[i];
                j++;
            }
        }
        frontPosition = j - 1;
        arr = copy;
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;
        private int counter;
        private Item[] arrCopy;

        public RandomizedQueueIterator() {
            if (n != 0) {
                do {
                    i = StdRandom.uniform(frontPosition + 1);
                    counter = n;
                    arrCopy = (Item[]) new Object[arr.length];
                    for (int j = 0; j < arr.length; j++)
                        arrCopy[j] = arr[j];
                }
                while (arr[i] == null);
            }
        }

        public boolean hasNext() {
            return counter != 0;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("No more elements to iterate over");
            do {
                i = StdRandom.uniform(frontPosition + 1);
            }
            while (arrCopy[i] == null);
            arrCopy[i] = null;
            counter--;
            return arr[i];
        }

    }

    public static void main(String[] args)   // unit testing (optional)
    {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        int c = 0;
        while (c == 0) {

            String operation = StdIn.readLine();
            if (operation.equals("end")) {
                Iterator i = queue.iterator();
                while (i.hasNext())
                    StdOut.println(i.next());
                break;
            }
            switch (operation) {
                case "in":
                    int a = Integer.valueOf(StdIn.readLine());
                    queue.enqueue(a);
                    StdOut.println("Whole queue");
                    Iterator iterator = queue.iterator();
                    while (iterator.hasNext())
                        StdOut.println(iterator.next());
                    break;
                case "out":
                    Integer deq = queue.dequeue();
                    StdOut.println(deq);
                    StdOut.println("Whole queue");
                    Iterator iterator2 = queue.iterator();
                    while (iterator2.hasNext())
                        StdOut.println(iterator2.next());
                    break;
                case "all":
                    Iterator iterator3 = queue.iterator();
                    while (iterator3.hasNext())
                        StdOut.println(iterator3.next());
                    break;
                case "size":
                    StdOut.println(queue.size());
                    break;

            }

        }
    }
}