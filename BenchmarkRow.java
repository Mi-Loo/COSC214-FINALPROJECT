// Holds the timing results for one data structure at one input size.
// Used by the Benchmark class to collect results, and by MainWindow
// to display them in the table.
public class BenchmarkRow {

    // Name of the structure, like "DynamicArray".
    public String structure;
    // Number of records used for this test.
    public int n;
    // Total nanoseconds it took to insert all records.
    public long insertNs;
    // Average nanoseconds per search operation.
    public long searchNs;
    // Average nanoseconds per delete operation.
    public long deleteNs;
    // Total nanoseconds to traverse all records once.
    public long traverseNs;

    // Constructor: fill in all the timing numbers.
    public BenchmarkRow(String structure, int n, long insertNs, long searchNs,
                        long deleteNs, long traverseNs) {
        this.structure = structure;
        this.n = n;
        this.insertNs = insertNs;
        this.searchNs = searchNs;
        this.deleteNs = deleteNs;
        this.traverseNs = traverseNs;
    }
}
