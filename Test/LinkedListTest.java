
import org.junit.jupiter.api.Test;
import Structures.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {

    @Test
    void matchesArrayListOnRandomOperations() {
        Random rnd = new Random(11);
        for (int trial = 0; trial < 100; trial++) {
            int n = 1 + rnd.nextInt(300);
            LinkedList l = new LinkedList();
            List<Integer> ref = new ArrayList<>();

            for (int op = 0; op < n; op++) {
                int kind = rnd.nextInt(4);
                if (kind == 0 || ref.isEmpty()) {
                    int x = rnd.nextInt(1000);
                    l.add(x);
                    ref.add(x);
                } else if (kind == 1) {
                    int idx = rnd.nextInt(ref.size() + 1);
                    int x = rnd.nextInt(1000);
                    l.add(idx, x);
                    ref.add(idx, x);
                } else if (kind == 2) {
                    int idx = rnd.nextInt(ref.size());
                    assertEquals(ref.remove(idx).intValue(), l.remove(idx), "trial=" + trial);
                } else {
                    int idx = rnd.nextInt(ref.size());
                    assertEquals(ref.get(idx).intValue(), l.get(idx), "trial=" + trial);
                }
            }

            assertEquals(ref.size(), l.size(), "trial=" + trial);
            for (int i = 0; i < ref.size(); i++) {
                assertEquals(ref.get(i).intValue(), l.get(i), "trial=" + trial + " i=" + i);
            }
        }
    }

    @Test
    void containsFindsPresentAndMissingValues() {
        LinkedList l = new LinkedList();
        int[] values = {5, 3, 9, 1, 7};
        for (int v : values) l.add(v);

        for (int v : values) assertTrue(l.contains(v));
        assertFalse(l.contains(100));
        assertFalse(l.contains(-1));
    }

    @Test
    void emptyList() {
        LinkedList l = new LinkedList();
        assertEquals(0, l.size());
        assertTrue(l.isEmpty());
        assertFalse(l.contains(0));
    }

    @Test
    void singleElement() {
        LinkedList l = new LinkedList();
        l.add(42);
        assertEquals(1, l.size());
        assertEquals(42, l.get(0));
        assertTrue(l.contains(42));
    }

    @Test
    void duplicateValues() {
        LinkedList l = new LinkedList();
        for (int i = 0; i < 10; i++) l.add(7);
        assertEquals(10, l.size());
        assertTrue(l.contains(7));
        l.remove(0);
        assertEquals(9, l.size());
        assertTrue(l.contains(7));
    }

    @Test
    void firstAndLastIndex() {
        LinkedList l = new LinkedList();
        for (int i = 0; i < 20; i++) l.add(i);
        assertEquals(0, l.get(0));
        assertEquals(19, l.get(19));
        l.add(0, -1);
        assertEquals(-1, l.get(0));
        assertEquals(19, l.get(20));
    }

    @Test
    void removeDownToEmptyKeepsTailConsistent() {
        LinkedList l = new LinkedList();
        for (int i = 0; i < 5; i++) l.add(i);
        for (int i = 0; i < 5; i++) l.remove(0);
        assertEquals(0, l.size());
        assertTrue(l.isEmpty());
        // tail must also be reset so a subsequent add() still works in O(1)
        l.add(99);
        assertEquals(1, l.size());
        assertEquals(99, l.get(0));
    }

    @Test
    void invalidGetThrows() {
        LinkedList l = new LinkedList();
        l.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> l.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> l.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> new LinkedList().get(0));
    }

    @Test
    void invalidAddThrows() {
        LinkedList l = new LinkedList();
        l.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> l.add(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> l.add(2, 0));
    }

    @Test
    void invalidRemoveThrows() {
        LinkedList l = new LinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> l.remove(0));
        l.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> l.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> l.remove(1));
    }
}
