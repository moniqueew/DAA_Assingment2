## Project layout


* src/Structures/             DynamicArray, inkedList, MinHeap
* src/Metrics/Metrics/        Metrics (steps, moves, comparisons, time)
* src/Benchmark/              Benchmark (runs W1-W4, writes results/results.csv)
* src/Structures/             JUnit 5 tests
* results/results.csv         Benchmark output
* DAA2_graphics               Excel charts
* REPORT.md                   Complexity table, loop invariant proofs, discussion


Runs all JUnit 5 tests:
- `DynamicArrayTest` / `LinkedListTest` — correctness against `java.util.ArrayList`
  on 100 random operation sequences each, plus edge cases (empty, single element,
  duplicates, first/last index, invalid index).
- `MinHeapTest` — sorted-output correctness against `java.util.PriorityQueue`,
  the min-heap property (`a[parent] <= a[child]`) checked after *every single*
  `insert`/`extractMin` call via `MinHeap.isValidHeap()`, plus edge cases.

`java.util` collections are used only inside the tests, as the "known-correct"
reference to compare against — never inside the structures themselves.

## Run the benchmark

Runs workloads W1 (Random Access), W2 (Search), W3 (Insert & Remove at
head/middle) and W4 (Priority Processing) for
n ∈ {100, 1 000, 10 000, 100 000}, all data generated from `new Random(42)`,
median of 5 repeats per case, and writes `results/results.csv` with columns:


## Charts

Charts were built directly in Excel from `results/results.csv` —
see `DAA2_graphics.xlsx`: raw data on one sheet, reshaped tables
driven by `SUMIFS` formulas on another, native Excel scatter charts (log
x-axis) on a third. 
To refresh them with your own benchmark run, open the workbook, select all
the old rows on the `RawData` sheet and paste your new `results.csv` in —
every table and chart recalculates automatically.

## Design highlights

- **DynamicArray** stores plain `int[]`; `add(x)` doubles capacity when full
  (amortized Θ(1)); `add(index,x)`/`remove(index)` shift the shorter possible
  side; `get(index)` is a direct, Θ(1) array read.
- **LinkedList** is a singly linked list of `Node{int value; Node next;}`
  with a tail pointer so `add(x)` (append) stays Θ(1); every positional
  operation (`add(index,x)`, `remove(index)`, `get(index)`) must walk from
  `head`, which is the whole point of the array-vs-list comparison.
- **MinHeap** is an array-based binary min-heap; `insert` bubbles the new
  element up, `extractMin` moves the last element to the root and bubbles it
  down; `peekMin` is Θ(1). `isValidHeap()` is a small extra public method
  (not part of the required API) used by the tests to directly confirm the
  heap property after every mutation.
- **Metrics** is owned by each structure instance (not static/global) and
  counts steps/moves/comparisons exactly where the work happens inside each
  method, per the assignment's counting convention (documented in
  `metrics/Metrics.java` and in each structure's Javadoc).

See `DAA2_report.docx` for the full complexity table, two loop-invariant proofs
(`DynamicArray.contains` and `MinHeap.bubbleDown`), and the discussion of
cache locality / pointer chasing behind the measured numbers.
