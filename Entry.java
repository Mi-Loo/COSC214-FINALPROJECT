// An Entry is one slot in a hash table bucket.
// It holds a key (the patient ID), the record, and a pointer
// to the next entry in the same bucket (for when two IDs
// end up in the same bucket).
// Used by HashTable.
public class Entry {

    // The patient ID that acts as the key for this entry.
    public String key;
    // The actual patient record.
    public PatientRecord value;
    // Points to the next entry in this bucket, or null if none.
    public Entry next;

    // Constructor: makes a new entry with a key and value.
    public Entry(String key, PatientRecord value) {
        // Save the key and value.
        this.key = key;
        this.value = value;
        // No next entry yet.
        this.next = null;
    }
}
