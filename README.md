# MEDDATA

Final project for COSC 214 (Data Structures and Algorithms) at Bowie State.

This is a Java app that loads a CSV of patient records and lets you store
them in four different data structures, run benchmarks comparing the
structures, and walk through a hospital admissions queue. It has a Swing
GUI with three tabs.

Built by team MEDDATA:
- Somtochukwu Igboeli
- Zeyana Paulemon
- Mahki Knight

## What it actually does

When you launch it, you get a window with three tabs:

- **Records tab.** Pick one of four data structures from a dropdown
  (Dynamic Array, Singly Linked List, Hash Table, or Patient Queue) and
  do insert / search / delete on patient records.
- **Queue tab.** A FIFO admissions queue. You can enqueue a patient,
  dequeue the next one to be seen, or peek at who's first in line.
- **Benchmarks tab.** Click "Run benchmarks" and it times insert, search,
  and delete on all four data structures at three input sizes (1000,
  5000, 10000) using `System.nanoTime()`. The results show up in a
  table.

The CSV we used is the Kaggle "Healthcare Dataset." The file doesn't
have a patient ID column so `CsvLoader` generates IDs as `P00001`,
`P00002`, and so on.

## Files

| File | What it is |
|------|------------|
| `PatientRecord.java` | The model object - one CSV row turned into a Java object |
| `Node.java` | One link in a chain (used by linked list and queue) |
| `Entry.java` | One slot in a hash table bucket |
| `DynamicArray.java` | Array-backed structure, grows when full |
| `SinglyLinkedList.java` | Chain of Node objects |
| `HashTable.java` | Buckets + separate chaining for collisions |
| `PatientQueue.java` | FIFO queue using Node objects |
| `CsvLoader.java` | Reads the Kaggle CSV into a PatientRecord array |
| `BenchmarkRow.java` | Holds timing results for one structure at one size |
| `Benchmark.java` | Runs the timing tests across all four structures |
| `MainWindow.java` | The Swing GUI, has main() |
| `DynamicArrayTest.java` | JUnit tests |
| `SinglyLinkedListTest.java` | JUnit tests |
| `HashTableTest.java` | JUnit tests |
| `PatientQueueTest.java` | JUnit tests |

## How to build and run

We didn't use Maven or Gradle, just plain `javac`. Everything is in the
default package which keeps the build commands simple.

```bash
# compile every .java file in the folder
javac *.java

# run it
java MainWindow
```

When the window opens, click the "Load CSV" button at the top and pick
the Kaggle healthcare CSV file. After it loads you'll see the row count
in the bottom status bar and you can start using the tabs.

### Running the JUnit tests

The tests use JUnit 5. You need `junit-platform-console-standalone.jar`
on the classpath. If you have it in the same folder:

```bash
javac -cp junit-platform-console-standalone-1.10.0.jar *.java
java -jar junit-platform-console-standalone-1.10.0.jar \
     --class-path . --scan-class-path
```

If you're using an IDE like IntelliJ or Eclipse just right-click the
test files and run them, that's what we did most of the time.

## Notes on the four data structures

We picked these on purpose so the benchmark would show real differences.

- **Dynamic Array.** Insert is O(1) amortized (we grow the array by
  doubling when it's full, same idea as Java's ArrayList). Search and
  delete are O(n) because you have to walk through every slot.
- **Singly Linked List.** Insert at head is O(1). Search and delete are
  also O(n) but for a different reason - you have to traverse pointers
  instead of array indexes. In practice it ended up slower than the
  array because of cache misses, which was interesting.
- **Hash Table.** Uses the patient ID's `hashCode()` mod bucket count to
  pick a bucket, then a chain of `Entry` objects inside the bucket to
  handle collisions. Lookup is roughly O(1) when collisions are rare.
  This is where the speed-up shows up in the benchmark.
- **Patient Queue.** FIFO using two Node pointers (head and tail).
  Enqueue and dequeue are both O(1). We use this for the Queue tab to
  simulate hospital admissions in arrival order.

## Git history / the order things got added

We pushed this in stages instead of one big dump, so the commit log
actually shows how the project came together. The order roughly was:

1. **Repo setup.** README skeleton and `.gitignore` for `*.class` and
   IDE files.
2. **`PatientRecord.java`.** The model. Everything else depends on this
   one class.
3. **`Node.java` and `Entry.java`.** Two tiny helper classes (just hold
   a record and a pointer). Both used by other structures.
4. **`DynamicArray.java`.** First data structure. Doesn't depend on
   Node/Entry, just uses a plain Java array, so we did it first to set
   the template for the others (the same insert/search/delete API).
5. **`SinglyLinkedList.java`.** Second structure. Uses Node from step 3.
6. **`HashTable.java`.** Third structure. Uses Entry from step 3.
7. **`PatientQueue.java`.** Fourth structure. Also uses Node. After
   this commit we had all four structures done.
8. **`CsvLoader.java`.** Reads the Kaggle CSV file and produces
   PatientRecord objects. Doesn't touch the data structures directly
   but feeds them.
9. **`BenchmarkRow.java` and `Benchmark.java`.** The timing engine.
   `BenchmarkRow` is a small holder class, `Benchmark` exercises all
   four structures at three input sizes.
10. **`MainWindow.java`.** The Swing GUI. This commit ties everything
    together - it loads the CSV via CsvLoader, runs the benchmark, and
    displays results in a JTable. It also has `main()` so this is what
    actually makes the project runnable.
11. **JUnit test classes.** All four `*Test.java` files. We added them
    at the end because they test classes that already exist.

We did it this way for two reasons. First, every commit in the log
actually compiles. If our professor checks out any older commit and
runs `javac *.java`, it works, because we only depend on stuff that's
already been added. Second, the log reads in the same order we'd
explain it: model -> primitives -> structures -> loading -> benchmarks
-> GUI -> tests. That made the demo easier because we could literally
walk through the commits.

We didn't get this right on the first try - we originally committed
MainWindow before HashTable was done and the build broke. So we reset
and redid it. That's the version that's in the log now.

## Things we'd change if we had more time

- The CSV loader does two passes over the file (one to count lines,
  one to actually read them) which is dumb. We did it that way so we
  could pre-allocate a fixed-size array, but using an ArrayList would
  have been cleaner. We didn't want to use Collections because the
  course is about implementing the structures ourselves.
- The benchmark only times sequential inserts. Random-order inserts
  would probably make the hash table look even better.
- The Swing UI is pretty plain. We focused on getting the structures
  and timing right instead of making it look nice.

## What's in the Kaggle dataset

The columns we use are:

- Name
- Age
- Gender
- Medical Condition
- Hospital
- Admission Type
- Billing Amount

The original Kaggle file has more columns (date of admission, doctor,
insurance provider, etc.) but we only kept the ones the assignment
asked for. `CsvLoader` looks up the columns by header name so it
shouldn't break if the order is different.

## Course context

This is the implementation phase (Phase 2) of the MEDDATA project for
COSC 214. Phase 1 was a design report turned in earlier in the semester
that described the architecture, the data structure choices, and the
benchmark plan. The code in this repo is the Phase 1 design actually
built out.
