// A dynamic array stores records in a plain Java array.
// When the array gets full, we make a bigger one and copy everything over.
// Insert is fast, but search and delete are slow because we
// have to check every single spot.
public class DynamicArray {

    // The actual array holding our records.
    private PatientRecord[] data;
    // How many records we currently have (not the array length).
    private int size;

    // Constructor: start with room for 16 records.
    public DynamicArray() {
        // Create the starting array.
        data = new PatientRecord[16];
        // No records stored yet.
        size = 0;
    }

    // Adds one record to the end of the array.
    // Returns false if a record with the same ID already exists.
    public boolean insertRecord(PatientRecord r) {
        // Check if this ID is already in the array.
        PatientRecord existing = searchRecord(r.getId());
        // If we found a match, don't add the duplicate.
        if (existing != null) {
            return false;
        }
        // Check if the array is full.
        if (size == data.length) {
            // Make a new array that is twice as big.
            PatientRecord[] bigger = new PatientRecord[data.length * 2];
            // Copy every record from the old array to the new one.
            for (int i = 0; i < size; i++) {
                bigger[i] = data[i];
            }
            // Replace the old array with the bigger one.
            data = bigger;
        }
        // Put the record in the first empty spot.
        data[size] = r;
        // We now have one more record.
        size = size + 1;
        return true;
    }

    // Looks for a record by its ID.
    // Returns the record if found, or null if not found.
    public PatientRecord searchRecord(String id) {
        // Check every spot from 0 to size-1.
        for (int i = 0; i < size; i++) {
            // Compare this record's ID to the one we want.
            if (data[i].getId().equals(id)) {
                // Found it.
                return data[i];
            }
        }
        // Checked everything, didn't find it.
        return null;
    }

    // Removes a record by its ID.
    // Returns true if we found and removed it, false if not found.
    public boolean deleteRecord(String id) {
        // Look for the record's position.
        for (int i = 0; i < size; i++) {
            // Check if this is the one to remove.
            if (data[i].getId().equals(id)) {
                // Shift every record after it back by one spot.
                for (int j = i; j < size - 1; j++) {
                    data[j] = data[j + 1];
                }
                // Clear the last spot.
                data[size - 1] = null;
                // One fewer record now.
                size = size - 1;
                return true;
            }
        }
        // ID not found.
        return false;
    }

    // Returns a new array containing every record, in order.
    public PatientRecord[] traverseRecords() {
        // Make a result array that is exactly the right size.
        PatientRecord[] result = new PatientRecord[size];
        // Copy each record into it.
        for (int i = 0; i < size; i++) {
            result[i] = data[i];
        }
        return result;
    }

    // Returns how many records are stored.
    public int size() {
        return size;
    }
}
