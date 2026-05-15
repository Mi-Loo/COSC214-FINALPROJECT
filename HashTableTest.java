import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests for the HashTable class.
public class HashTableTest {

    private PatientRecord makeRecord(String id) {
        return new PatientRecord(id, "Test", 25, "M",
                "Diabetes", "Bowie Med", "Emergency", 750.0);
    }

    @Test
    void insertThenSearch() {
        HashTable h = new HashTable();
        h.insertRecord(makeRecord("P00001"));
        PatientRecord found = h.searchRecord("P00001");
        assertNotNull(found);
        assertEquals("P00001", found.getId());
    }

    @Test
    void duplicateRejected() {
        HashTable h = new HashTable();
        h.insertRecord(makeRecord("P00001"));
        assertFalse(h.insertRecord(makeRecord("P00001")));
        assertEquals(1, h.size());
    }

    @Test
    void searchMissReturnsNull() {
        HashTable h = new HashTable();
        h.insertRecord(makeRecord("P00001"));
        assertNull(h.searchRecord("P99999"));
    }

    @Test
    void searchEmptyReturnsNull() {
        HashTable h = new HashTable();
        assertNull(h.searchRecord("P00001"));
    }

    @Test
    void deleteRemovesIt() {
        HashTable h = new HashTable();
        h.insertRecord(makeRecord("P00001"));
        h.insertRecord(makeRecord("P00002"));
        assertTrue(h.deleteRecord("P00001"));
        assertNull(h.searchRecord("P00001"));
        assertNotNull(h.searchRecord("P00002"));
    }

    @Test
    void deleteEmptyReturnsFalse() {
        HashTable h = new HashTable();
        assertFalse(h.deleteRecord("P00001"));
    }

    @Test
    void traverseGivesAll() {
        HashTable h = new HashTable();
        for (int i = 1; i <= 50; i++) {
            String num = "" + i;
            while (num.length() < 5) {
                num = "0" + num;
            }
            h.insertRecord(makeRecord("P" + num));
        }
        PatientRecord[] all = h.traverseRecords();
        assertEquals(50, all.length);
    }
}
