// A hash table stores records in "buckets" for fast lookup.
// To pick which bucket, we use the ID's hashCode() and take the remainder.
// If two IDs end up in the same bucket, they form a chain of Entry objects.
// Search is very fast because we only look in one bucket instead of the whole thing.
public class HashTable {

    // The array of buckets. Each bucket is the start of an Entry chain (or null).
    private Entry[] buckets;
    // Total number of records stored across all buckets.
    private int size;

    // Constructor: start with 32 empty buckets.
    public HashTable() {
        buckets = new Entry[32];
        size = 0;
    }

    // Figures out which bucket index a key belongs in.
    private int bucketFor(String key) {
        // Get Java's built-in hash number for this string.
        int h = key.hashCode();
        // The hash can be negative, so make it positive.
        if (h < 0) {
            h = h * -1;
        }
        // Use remainder (%) to get an index between 0 and buckets.length-1.
        int index = h % buckets.length;
        return index;
    }

    // Adds one record. Returns false if the ID already exists.
    public boolean insertRecord(PatientRecord r) {
        // Check for duplicate first.
        PatientRecord existing = searchRecord(r.getId());
        if (existing != null) {
            return false;
        }
        // Find which bucket this ID goes in.
        int index = bucketFor(r.getId());
        // Make a new entry for this record.
        Entry newEntry = new Entry(r.getId(), r);
        // Put it at the front of that bucket's chain.
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        // One more record total.
        size = size + 1;
        return true;
    }

    // Searches for a record by ID.
    // Only looks in the one bucket where the ID should be.
    // Returns the record if found, null if not.
    public PatientRecord searchRecord(String id) {
        // Find the right bucket.
        int index = bucketFor(id);
        // Walk that bucket's chain.
        Entry current = buckets[index];
        while (current != null) {
            // Check if this entry has the key we want.
            if (current.key.equals(id)) {
                return current.value;
            }
            current = current.next;
        }
        // Not in this bucket.
        return null;
    }

    // Removes a record by ID. Returns true if removed.
    public boolean deleteRecord(String id) {
        // Find the right bucket.
        int index = bucketFor(id);
        // We need a "previous" pointer to unlink the entry.
        Entry previous = null;
        Entry current = buckets[index];
        // Walk the chain in this bucket.
        while (current != null) {
            // Is this the entry to remove?
            if (current.key.equals(id)) {
                if (previous == null) {
                    // Removing the first entry in the bucket.
                    buckets[index] = current.next;
                } else {
                    // Removing from the middle or end.
                    previous.next = current.next;
                }
                size = size - 1;
                return true;
            }
            // Move forward.
            previous = current;
            current = current.next;
        }
        // Didn't find it.
        return false;
    }

    // Returns all records in a new array.
    // The order depends on bucket positions, not insertion order.
    public PatientRecord[] traverseRecords() {
        // Make an array big enough for every record.
        PatientRecord[] result = new PatientRecord[size];
        int i = 0;
        // Go through every bucket.
        for (int b = 0; b < buckets.length; b++) {
            // Walk the chain in this bucket.
            Entry current = buckets[b];
            while (current != null) {
                result[i] = current.value;
                i = i + 1;
                current = current.next;
            }
        }
        return result;
    }

    // Returns total number of records.
    public int size() {
        return size;
    }
}
