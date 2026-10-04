package Structures;

import Metrics.Metrics;

public class MinHeap {

    private static final int DEFAULT_CAPACITY = 4;

    private int[] data;
    private int size;
    private final Metrics metrics = new Metrics();

    public MinHeap() {
        this(DEFAULT_CAPACITY);
    }

    public MinHeap(int initialCapacity) {
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

    public void insert(int x) {
        ensureCapacity(size + 1);
        data[size] = x;
        metrics.incMoves();
        size++;
        bubbleUp(size - 1);
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.incSteps();
        return data[0];
    }


    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.incSteps();
        int min = data[0];
        size--;
        if (size > 0) {
            data[0] = data[size];
            metrics.incMoves();
            bubbleDown(0);
        }
        return min;
    }

    private void bubbleUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            metrics.incSteps(2);
            metrics.incComparisons();
            if (data[parent] <= data[i]) {
                break;
            }
            swap(i, parent);
            i = parent;
        }
    }


    private void bubbleDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < size) {
                metrics.incSteps(2);
                metrics.incComparisons();
                if (data[left] < data[smallest]) {
                    smallest = left;
                }
            }
            if (right < size) {
                metrics.incSteps(2);
                metrics.incComparisons();
                if (data[right] < data[smallest]) {
                    smallest = right;
                }
            }
            if (smallest == i) {
                break;
            }
            swap(i, smallest);
            i = smallest;
        }
    }


    public boolean isValidHeap() {
        for (int i = 1; i < size; i++) {
            int parent = (i - 1) / 2;
            if (data[parent] > data[i]) {
                return false;
            }
        }
        return true;
    }

    private void swap(int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
        metrics.incMoves(2);
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
