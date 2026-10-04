import Structures.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    @Test
    void matchesArrayListOnRandomOperations() {
        Random rnd = new Random(7);
        for (int trial = 0; trial < 100; trial++) {
            int n = 1 + rnd.nextInt(300);
            DynamicArray a = new DynamicArray();
            List<Integer> ref = new ArrayList<>();

            for (int op = 0; op < n; op++) {
                int kind = rnd.nextInt(4);
                if (kind == 0 || ref.isEmpty()) {
                    int x = rnd.nextInt(1000);
                    a.add(x);
                    ref.add(x);
                } else if (kind == 1) {
                    int idx = rnd.nextInt(ref.size() + 1);
                    int x = rnd.nextInt(1000);
                    a.add(idx, x);
                    ref.add(idx, x);
                } else if (kind == 2) {
                    int idx = rnd.nextInt(ref.size());
                    assertEquals(ref.remove(idx).intValue(), a.remove(idx), "trial=" + trial);
                } else {
                    int idx = rnd.nextInt(ref.size());
                    assertEquals(ref.get(idx).intValue(), a.get(idx), "trial=" + trial);
                }
            }

            assertEquals(ref.size(), a.size(), "trial=" + trial);
            for (int i = 0; i < ref.size(); i++) {
                assertEquals(ref.get(i).intValue(), a.get(i), "trial=" + trial + " i=" + i);
            }
        }
    }

    @Test
    void containsFindsPresentAndMissingValues() {
        DynamicArray a = new DynamicArray();
        int[] values = {5, 3, 9, 1, 7};
        for (int v : values) a.add(v);

        for (int v : values) assertTrue(a.contains(v));
        assertFalse(a.contains(100));
        assertFalse(a.contains(-1));
    }

    @Test
    void emptyArray() {
        DynamicArray a = new DynamicArray();
        assertEquals(0, a.size());
        assertTrue(a.isEmpty());
        assertFalse(a.contains(0));
    }

    @Test
    void singleElement() {
        DynamicArray a = new DynamicArray();
        a.add(42);
        assertEquals(1, a.size());
        assertEquals(42, a.get(0));
        assertTrue(a.contains(42));
    }

    @Test
    void duplicateValues() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 10; i++) a.add(7);
        assertEquals(10, a.size());
        assertTrue(a.contains(7));
        a.remove(0);
        assertEquals(9, a.size());
        assertTrue(a.contains(7));
    }

    @Test
    void firstAndLastIndex() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 20; i++) a.add(i);
        assertEquals(0, a.get(0));
        assertEquals(19, a.get(19));
        a.add(0, -1);
        assertEquals(-1, a.get(0));
        assertEquals(19, a.get(20));
    }

    @Test
    void growsPastInitialCapacity() {
        DynamicArray a = new DynamicArray(2);
        for (int i = 0; i < 1000; i++) a.add(i);
        assertEquals(1000, a.size());
        for (int i = 0; i < 1000; i++) assertEquals(i, a.get(i));
    }

    @Test
    void invalidGetThrows() {
        DynamicArray a = new DynamicArray();
        a.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> new DynamicArray().get(0));
    }

    @Test
    void invalidAddThrows() {
        DynamicArray a = new DynamicArray();
        a.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> a.add(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> a.add(2, 0));
    }

    @Test
    void invalidRemoveThrows() {
        DynamicArray a = new DynamicArray();
        assertThrows(IndexOutOfBoundsException.class, () -> a.remove(0));
        a.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> a.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> a.remove(1));
    }
}
