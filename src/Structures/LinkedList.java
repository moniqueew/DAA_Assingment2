package Structures;

import Metrics.Metrics;

public class LinkedList {

    private static final class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final Metrics metrics = new Metrics();

    public Metrics getMetrics() {
        return metrics;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int x) {
        Node node = new Node(x);
        if (head == null) {
            head = tail = node;
            metrics.incMoves();
        } else {
            tail.next = node;
            tail = node;
            metrics.incMoves(2);
        }
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node node = new Node(x);
        if (index == 0) {
            node.next = head;
            head = node;
            metrics.incMoves();
        } else {
            Node before = nodeAt(index - 1);
            node.next = before.next;
            before.next = node;
            metrics.incMoves(2);
        }
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        int removed;
        if (index == 0) {
            removed = head.value;
            head = head.next;
            if (head == null) tail = null;
            metrics.incMoves();
        } else {
            Node before = nodeAt(index - 1);
            Node target = before.next;
            removed = target.value;
            before.next = target.next;
            if (target == tail) tail = before;
            metrics.incMoves();
        }
        size--;
        return removed;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        return nodeAt(index).value;
    }

    public boolean contains(int x) {
        Node cur = head;
        while (cur != null) {
            metrics.incSteps();
            metrics.incComparisons();
            if (cur.value == x) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }


    private Node nodeAt(int index) {
        Node cur = head;
        int i = 0;
        while (i < index) {
            metrics.incSteps();
            cur = cur.next;
            i++;
        }
        metrics.incSteps();
        return cur;
    }
}
