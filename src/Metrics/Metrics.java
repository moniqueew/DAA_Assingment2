package Metrics;

public class Metrics {

    private long steps = 0;
    private long moves = 0;
    private long comparisons = 0;
    private long startNanos = 0;
    private long elapsedNanos = 0;

    public void incSteps() { steps++; }
    public void incSteps(long n) { steps += n; }
    public long getSteps() { return steps; }

    public void incMoves() { moves++; }
    public void incMoves(long n) { moves += n; }
    public long getMoves() { return moves; }

    public void incComparisons() { comparisons++; }
    public void incComparisons(long n) { comparisons += n; }
    public long getComparisons() { return comparisons; }

    public void startTimer() { startNanos = System.nanoTime(); }
    public void stopTimer() { elapsedNanos += System.nanoTime() - startNanos; }
    public long getElapsedNanos() { return elapsedNanos; }
    public double getElapsedMillis() { return elapsedNanos / 1_000_000.0; }

    public void reset() {
        steps = 0;
        moves = 0;
        comparisons = 0;
        startNanos = 0;
        elapsedNanos = 0;
    }

    @Override
    public String toString() {
        return "Metrics{steps=" + steps + ", moves=" + moves +
                ", comparisons=" + comparisons + ", timeMs=" + getElapsedMillis() + "}";
    }
}
