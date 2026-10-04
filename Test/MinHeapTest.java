import Structures.*;
import org.junit.jupiter.api.Test;
import java.util.PriorityQueue;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

    @Test
    void sortedOutputMatchesPriorityQueue() {
        Random rnd = new Random(13);
        for (int trial = 0; trial < 50; trial++) {
            int n = 1 + rnd.nextInt(500);
            MinHeap h = new MinHeap();
            PriorityQueue<Integer> ref = new PriorityQueue<>();

            for (int i = 0; i < n; i++) {
                int x = rnd.nextInt(10000);
                h.insert(x);
                ref.add(x);
            }

            int prev = Integer.MIN_VALUE;
            while (!ref.isEmpty()) {
                int expected = ref.poll();
                int actual = h.extractMin();
                assertEquals(expected, actual, "trial=" + trial);
                assertTrue(actual >= prev, "non-decreasing violated, trial=" + trial);
                prev = actual;
            }
            assertTrue(h.isEmpty(), "trial=" + trial);
        }
    }

    @Test
    void heapPropertyHoldsAfterEveryInsert() {
        Random rnd = new Random(17);
        MinHeap h = new MinHeap();
        for (int i = 0; i < 500; i++) {
            h.insert(rnd.nextInt(10000));
            assertTrue(h.isValidHeap(), "heap property violated after insert #" + i);
        }
    }

    @Test
    void heapPropertyHoldsAfterEveryExtract() {
        Random rnd = new Random(19);
        MinHeap h = new MinHeap();
        int n = 500;
        for (int i = 0; i < n; i++) h.insert(rnd.nextInt(10000));

        for (int i = 0; i < n; i++) {
            h.extractMin();
            assertTrue(h.isValidHeap(), "heap property violated after extractMin #" + i);
        }
    }

    @Test
    void emptyHeap() {
        MinHeap h = new MinHeap();
        assertEquals(0, h.size());
        assertTrue(h.isEmpty());
    }

    @Test
    void singleElement() {
        MinHeap h = new MinHeap();
        h.insert(42);
        assertEquals(42, h.peekMin());
        assertEquals(42, h.extractMin());
        assertTrue(h.isEmpty());
    }

    @Test
    void duplicateValues() {
        MinHeap h = new MinHeap();
        for (int i = 0; i < 10; i++) h.insert(5);
        for (int i = 0; i < 10; i++) assertEquals(5, h.extractMin());
        assertTrue(h.isEmpty());
    }

    @Test
    void peekDoesNotRemove() {
        MinHeap h = new MinHeap();
        h.insert(3);
        h.insert(1);
        h.insert(2);
        assertEquals(1, h.peekMin());
        assertEquals(1, h.peekMin());
        assertEquals(3, h.size());
    }

    @Test
    void emptyPeekThrows() {
        assertThrows(IllegalStateException.class, () -> new MinHeap().peekMin());
    }

    @Test
    void emptyExtractThrows() {
        assertThrows(IllegalStateException.class, () -> new MinHeap().extractMin());
    }

}
