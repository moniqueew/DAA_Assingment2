package Benchmark;

import Structures.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;



public class Benchmark {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int REPEATS = 5;
    private static final long SEED = 42L;
    private static final int FILL_RANGE = 1_000_000;

    private record Result(double timeMs, long steps, long moves, long comparisons) {
    }

    public static void main(String[] args) throws IOException {
        String outputPath = args.length > 0 ? args[0] : "results/results.csv";
        new java.io.File(outputPath).getParentFile().mkdirs();

        try (PrintWriter out = new PrintWriter(new FileWriter(outputPath))) {
            out.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

            for (int n : SIZES) {
                runW1(out, n);
                runW2(out, n);
                runW3(out, n, "head");
                runW3(out, n, "middle");
                runW4(out, n);
                System.out.println("done n=" + n);
            }
        }
        System.out.println("Benchmark finished -> " + outputPath);
    }

    private static void runW1(PrintWriter out, int n) {
        writeRow(out, "W1", "-", "DynamicArray", n, repeatMedian(() -> {
            DynamicArray a = new DynamicArray();
            fill(a, n);
            a.getMetrics().reset();
            Random q = new Random(SEED + 1);
            a.getMetrics().startTimer();
            for (int i = 0; i < 10_000; i++) {
                a.get(q.nextInt(n));
            }
            a.getMetrics().stopTimer();
            return new Result(a.getMetrics().getElapsedMillis(), a.getMetrics().getSteps(),
                    a.getMetrics().getMoves(), a.getMetrics().getComparisons());
        }));

        writeRow(out, "W1", "-", "LinkedList", n, repeatMedian(() -> {
            LinkedList l = new LinkedList();
            fill(l, n);
            l.getMetrics().reset();
            Random q = new Random(SEED + 1);
            l.getMetrics().startTimer();
            for (int i = 0; i < 10_000; i++) {
                l.get(q.nextInt(n));
            }
            l.getMetrics().stopTimer();
            return new Result(l.getMetrics().getElapsedMillis(), l.getMetrics().getSteps(),
                    l.getMetrics().getMoves(), l.getMetrics().getComparisons());
        }));
    }

    private static void runW2(PrintWriter out, int n) {
        writeRow(out, "W2", "-", "DynamicArray", n, repeatMedian(() -> {
            DynamicArray a = new DynamicArray();
            int[] filled = fill(a, n);
            a.getMetrics().reset();
            Random q = new Random(SEED + 2);
            a.getMetrics().startTimer();
            for (int i = 0; i < 1_000; i++) {
                int x = (i % 2 == 0) ? filled[q.nextInt(n)] : -(q.nextInt(FILL_RANGE) + 1);
                a.contains(x);
            }
            a.getMetrics().stopTimer();
            return new Result(a.getMetrics().getElapsedMillis(), a.getMetrics().getSteps(),
                    a.getMetrics().getMoves(), a.getMetrics().getComparisons());
        }));

        writeRow(out, "W2", "-", "LinkedList", n, repeatMedian(() -> {
            LinkedList l = new LinkedList();
            int[] filled = fill(l, n);
            l.getMetrics().reset();
            Random q = new Random(SEED + 2);
            l.getMetrics().startTimer();
            for (int i = 0; i < 1_000; i++) {
                int x = (i % 2 == 0) ? filled[q.nextInt(n)] : -(q.nextInt(FILL_RANGE) + 1);
                l.contains(x);
            }
            l.getMetrics().stopTimer();
            return new Result(l.getMetrics().getElapsedMillis(), l.getMetrics().getSteps(),
                    l.getMetrics().getMoves(), l.getMetrics().getComparisons());
        }));
    }

    private static void runW3(PrintWriter out, int n, String variant) {
        writeRow(out, "W3", variant, "DynamicArray", n, repeatMedian(() -> {
            DynamicArray a = new DynamicArray();
            fill(a, n);
            int idx = variant.equals("head") ? 0 : n / 2;
            a.getMetrics().reset();
            a.getMetrics().startTimer();
            for (int i = 0; i < 1_000; i++) {
                a.add(idx, i);
            }
            for (int i = 0; i < 1_000; i++) {
                a.remove(idx);
            }
            a.getMetrics().stopTimer();
            return new Result(a.getMetrics().getElapsedMillis(), a.getMetrics().getSteps(),
                    a.getMetrics().getMoves(), a.getMetrics().getComparisons());
        }));

        writeRow(out, "W3", variant, "LinkedList", n, repeatMedian(() -> {
            LinkedList l = new LinkedList();
            fill(l, n);
            int idx = variant.equals("head") ? 0 : n / 2;
            l.getMetrics().reset();
            l.getMetrics().startTimer();
            for (int i = 0; i < 1_000; i++) {
                l.add(idx, i);
            }
            for (int i = 0; i < 1_000; i++) {
                l.remove(idx);
            }
            l.getMetrics().stopTimer();
            return new Result(l.getMetrics().getElapsedMillis(), l.getMetrics().getSteps(),
                    l.getMetrics().getMoves(), l.getMetrics().getComparisons());
        }));
    }

    private static void runW4(PrintWriter out, int n) {
        writeRow(out, "W4", "-", "MinHeap", n, repeatMedian(() -> {
            MinHeap h = new MinHeap();
            Random rnd = new Random(SEED);
            int[] values = new int[n];
            for (int i = 0; i < n; i++) values[i] = rnd.nextInt(FILL_RANGE);

            h.getMetrics().reset();
            h.getMetrics().startTimer();
            for (int v : values) {
                h.insert(v);
            }
            int prev = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int min = h.extractMin();
                if (min < prev) throw new IllegalStateException("Heap order violated");
                prev = min;
            }
            h.getMetrics().stopTimer();
            return new Result(h.getMetrics().getElapsedMillis(), h.getMetrics().getSteps(),
                    h.getMetrics().getMoves(), h.getMetrics().getComparisons());
        }));
    }


    private interface Case {
        Result run();
    }

    private static Result repeatMedian(Case c) {
        Result[] results = new Result[REPEATS];
        for (int r = 0; r < REPEATS; r++) {
            results[r] = c.run();
        }
        Result[] sorted = results.clone();
        Arrays.sort(sorted, (x, y) -> Double.compare(x.timeMs(), y.timeMs()));
        return sorted[sorted.length / 2];
    }

    private static int[] fill(DynamicArray a, int n) {
        Random rnd = new Random(SEED);
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = rnd.nextInt(FILL_RANGE);
            a.add(values[i]);
        }
        return values;
    }

    private static int[] fill(LinkedList l, int n) {
        Random rnd = new Random(SEED);
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = rnd.nextInt(FILL_RANGE);
            l.add(values[i]);
        }
        return values;
    }

    private static void writeRow(PrintWriter out, String workload, String variant,
                                 String structure, int n, Result r) {
        out.printf("%s,%s,%s,%d,%.4f,%d,%d,%d%n",
                workload, variant, structure, n, r.timeMs(), r.steps(), r.moves(), r.comparisons());
    }
}
