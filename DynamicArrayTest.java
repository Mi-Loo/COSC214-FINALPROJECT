import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests for the DynamicArray class.
public class DynamicArrayTest {

    // Helper: makes a test record with the given ID.
    private PatientRecord makeRecord(String id) {
        return new PatientRecord(id, "Test", 30, "M",
                "Hypertension", "Mercy", "Elective", 1000.0);
    }

    @Test
    void insertTwoRecordsSizeIsTwo() {
        DynamicArray a = new DynamicArray();
        a.insertRecord(makeRecord("P00001"));
        a.insertRecord(makeRecord("P00002"));
        assertEquals(2, a.size());
    }

    @Test
    void duplicateIdGetsRejected() {
        DynamicArray a = new DynamicArray();
        a.insertRecord(makeRecord("P00001"));
        boolean result = a.insertRecord(makeRecord("P00001"));
        assertFalse(result);
        assertEquals(1, a.size());
    }

    @Test
    void searchFindsRecord() {
        DynamicArray a = new DynamicArray();
        a.insertRecord(makeRecord("P00001"));
        assertNotNull(a.searchRecord("P00001"));
    }

    @Test
    void searchMissReturnsNull() {
        DynamicArray a = new DynamicArray();
        a.insertRecord(makeRecord("P00001"));
        assertNull(a.searchRecord("P99999"));
    }

    @Test
    void searchEmptyReturnsNull() {
        DynamicArray a = new DynamicArray();
        assertNull(a.searchRecord("P00001"));
    }

    @Test
    void deleteRemovesRecord() {
        DynamicArray a = new DynamicArray();
        a.insertRecord(makeRecord("P00001"));
        a.insertRecord(makeRecord("P00002"));
        assertTrue(a.deleteRecord("P00001"));
        assertEquals(1, a.size());
    }

    @Test
    void deleteMissingReturnsFalse() {
        DynamicArray a = new DynamicArray();
        assertFalse(a.deleteRecord("P00001"));
    }

    @Test
    void traverseGivesAllRecords() {
        DynamicArray a = new DynamicArray();
        for (int i = 1; i <= 5; i++) {
            a.insertRecord(makeRecord("P0000" + i));
        }
        PatientRecord[] all = a.traverseRecords();
        assertEquals(5, all.length);
    }

    @Test
    void arrayGrowsPast16() {
        DynamicArray a = new DynamicArray();
        for (int i = 1; i <= 100; i++) {
            String id = "P";
            // Pad with zeros manually.
            String num = "" + i;
            while (num.length() < 5) {
                num = "0" + num;
            }
            a.insertRecord(makeRecord("P" + num));
        }
        assertEquals(100, a.size());
    }
}
