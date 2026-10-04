package Structures;

import Metrics.Metrics;


public class DynamicArray {

    private static final int DEFAULT_CAPACITY = 4;

    private int[] data;
    private int size;
    private final Metrics metrics = new Metrics();

    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 1) initialCapacity = 1;
        this.data = new int[initialCapacity];
        this.size = 0;
    }

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
        ensureCapacity(size + 1);
        data[size] = x;
        metrics.incMoves();
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            metrics.incMoves();
        }
        data[index] = x;
        metrics.incMoves();
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        int removed = data[index];
        metrics.incSteps();
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            metrics.incMoves();
        }
        size--;
        return removed;
    }


    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        metrics.incSteps();
        return data[index];
    }


    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            metrics.incSteps();
            metrics.incComparisons();
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= data.length) return;
        int newCapacity = data.length * 2;
        while (newCapacity < minCapacity) newCapacity *= 2;
        int[] newData = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            metrics.incMoves();
        }
        data = newData;
    }
}
