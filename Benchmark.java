// Times the four data structures at three input sizes (1000, 5000, 10000)
// using System.nanoTime() for precise measurement.
public class Benchmark {

    // Runs all benchmarks. Takes the full loaded records array.
    // Returns an array of BenchmarkRow objects with the timing results.
    // There are at most 12 rows (4 structures x 3 sizes).
    public static BenchmarkRow[] runAll(PatientRecord[] source) {
        // The three sizes we test at.
        int[] sizes = {1000, 5000, 10000};

        // We'll collect results here. Max 12 rows.
        BenchmarkRow[] results = new BenchmarkRow[12];
        // Tracks how many rows we've actually filled.
        int rowCount = 0;

        // Test each size.
        for (int s = 0; s < sizes.length; s++) {
            int n = sizes[s];
            // Skip if we don't have enough records.
            if (source.length < n) {
                continue;
            }

            // Make a subset array with just the first n records.
            PatientRecord[] subset = new PatientRecord[n];
            for (int i = 0; i < n; i++) {
                subset[i] = source[i];
            }

            // Time each structure on this subset.
            results[rowCount] = timeDynamicArray(subset);
            rowCount = rowCount + 1;

            results[rowCount] = timeLinkedList(subset);
            rowCount = rowCount + 1;

            results[rowCount] = timeHashTable(subset);
            rowCount = rowCount + 1;

            results[rowCount] = timeQueue(subset);
            rowCount = rowCount + 1;
        }

        // Make a right-sized array to return.
        BenchmarkRow[] output = new BenchmarkRow[rowCount];
        for (int i = 0; i < rowCount; i++) {
            output[i] = results[i];
        }
        return output;
    }

    // Times insert/search/delete/traverse on a DynamicArray.
    private static BenchmarkRow timeDynamicArray(PatientRecord[] subset) {
        DynamicArray arr = new DynamicArray();

        // --- Insert all records ---
        long start = System.nanoTime();
        for (int i = 0; i < subset.length; i++) {
            arr.insertRecord(subset[i]);
        }
        long insertTime = System.nanoTime() - start;

        // --- Search 100 records (or fewer if subset is small) ---
        int sampleCount = 100;
        if (subset.length < sampleCount) {
            sampleCount = subset.length;
        }
        start = System.nanoTime();
        for (int i = 0; i < sampleCount; i++) {
            arr.searchRecord(subset[i].getId());
        }
        // Divide to get average time per search.
        long searchTime = (System.nanoTime() - start) / sampleCount;

        // --- Traverse all records once ---
        start = System.nanoTime();
        arr.traverseRecords();
        long traverseTime = System.nanoTime() - start;

        // --- Delete 100 records ---
        start = System.nanoTime();
        for (int i = 0; i < sampleCount; i++) {
            arr.deleteRecord(subset[i].getId());
        }
        long deleteTime = (System.nanoTime() - start) / sampleCount;

        return new BenchmarkRow("DynamicArray", subset.length,
                insertTime, searchTime, deleteTime, traverseTime);
    }

    // Times insert/search/delete/traverse on a SinglyLinkedList.
    private static BenchmarkRow timeLinkedList(PatientRecord[] subset) {
        SinglyLinkedList list = new SinglyLinkedList();

        // --- Insert ---
        long start = System.nanoTime();
        for (int i = 0; i < subset.length; i++) {
            list.insertRecord(subset[i]);
        }
        long insertTime = System.nanoTime() - start;

        // --- Search ---
        int sampleCount = 100;
        if (subset.length < sampleCount) {
            sampleCount = subset.length;
        }
        start = System.nanoTime();
        for (int i = 0; i < sampleCount; i++) {
            list.searchRecord(subset[i].getId());
        }
        long searchTime = (System.nanoTime() - start) / sampleCount;

        // --- Traverse ---
        start = System.nanoTime();
        list.traverseRecords();
        long traverseTime = System.nanoTime() - start;

        // --- Delete ---
        start = System.nanoTime();
        for (int i = 0; i < sampleCount; i++) {
            list.deleteRecord(subset[i].getId());
        }
        long deleteTime = (System.nanoTime() - start) / sampleCount;

        return new BenchmarkRow("SinglyLinkedList", subset.length,
                insertTime, searchTime, deleteTime, traverseTime);
    }

    // Times insert/search/delete/traverse on a HashTable.
    private static BenchmarkRow timeHashTable(PatientRecord[] subset) {
        HashTable table = new HashTable();

        // --- Insert ---
        long start = System.nanoTime();
        for (int i = 0; i < subset.length; i++) {
            table.insertRecord(subset[i]);
        }
        long insertTime = System.nanoTime() - start;

        // --- Search ---
        int sampleCount = 100;
        if (subset.length < sampleCount) {
            sampleCount = subset.length;
        }
        start = System.nanoTime();
        for (int i = 0; i < sampleCount; i++) {
            table.searchRecord(subset[i].getId());
        }
        long searchTime = (System.nanoTime() - start) / sampleCount;

        // --- Traverse ---
        start = System.nanoTime();
        table.traverseRecords();
        long traverseTime = System.nanoTime() - start;

        // --- Delete ---
        start = System.nanoTime();
        for (int i = 0; i < sampleCount; i++) {
            table.deleteRecord(subset[i].getId());
        }
        long deleteTime = (System.nanoTime() - start) / sampleCount;

        return new BenchmarkRow("HashTable", subset.length,
                insertTime, searchTime, deleteTime, traverseTime);
    }

    // Times the queue.
    private static BenchmarkRow timeQueue(PatientRecord[] subset) {
        PatientQueue queue = new PatientQueue();

        // --- Insert (admit) ---
        long start = System.nanoTime();
        for (int i = 0; i < subset.length; i++) {
            queue.admit(subset[i]);
        }
        long insertTime = System.nanoTime() - start;

        // --- Search ---
        int sampleCount = 100;
        if (subset.length < sampleCount) {
            sampleCount = subset.length;
        }
        start = System.nanoTime();
        for (int i = 0; i < sampleCount; i++) {
            queue.searchRecord(subset[i].getId());
        }
        long searchTime = (System.nanoTime() - start) / sampleCount;

        // --- Traverse ---
        start = System.nanoTime();
        queue.traverseRecords();
        long traverseTime = System.nanoTime() - start;

        // --- Delete ---
        start = System.nanoTime();
        for (int i = 0; i < sampleCount; i++) {
            queue.deleteRecord(subset[i].getId());
        }
        long deleteTime = (System.nanoTime() - start) / sampleCount;

        return new BenchmarkRow("PatientQueue", subset.length,
                insertTime, searchTime, deleteTime, traverseTime);
    }
}
