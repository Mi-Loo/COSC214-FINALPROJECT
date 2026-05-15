import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests for the SinglyLinkedList class.
public class SinglyLinkedListTest {

    private PatientRecord makeRecord(String id) {
        return new PatientRecord(id, "Test", 30, "F",
                "Asthma", "St. Mary", "Urgent", 500.0);
    }

    @Test
    void insertTwoSizeIsTwo() {
        SinglyLinkedList l = new SinglyLinkedList();
        l.insertRecord(makeRecord("P00001"));
        l.insertRecord(makeRecord("P00002"));
        assertEquals(2, l.size());
    }

    @Test
    void duplicateRejected() {
        SinglyLinkedList l = new SinglyLinkedList();
        l.insertRecord(makeRecord("P00001"));
        assertFalse(l.insertRecord(makeRecord("P00001")));
    }

    @Test
    void deleteHead() {
        SinglyLinkedList l = new SinglyLinkedList();
        l.insertRecord(makeRecord("P00001"));
        l.insertRecord(makeRecord("P00002"));
        assertTrue(l.deleteRecord("P00001"));
        assertNull(l.searchRecord("P00001"));
        assertEquals(1, l.size());
    }

    @Test
    void deleteMiddle() {
        SinglyLinkedList l = new SinglyLinkedList();
        l.insertRecord(makeRecord("P00001"));
        l.insertRecord(makeRecord("P00002"));
        l.insertRecord(makeRecord("P00003"));
        assertTrue(l.deleteRecord("P00002"));
        assertNotNull(l.searchRecord("P00001"));
        assertNotNull(l.searchRecord("P00003"));
    }

    @Test
    void deleteEmptyReturnsFalse() {
        SinglyLinkedList l = new SinglyLinkedList();
        assertFalse(l.deleteRecord("P00001"));
    }

    @Test
    void searchEmptyReturnsNull() {
        SinglyLinkedList l = new SinglyLinkedList();
        assertNull(l.searchRecord("P00001"));
    }

    @Test
    void traverseInOrder() {
        SinglyLinkedList l = new SinglyLinkedList();
        l.insertRecord(makeRecord("P00001"));
        l.insertRecord(makeRecord("P00002"));
        l.insertRecord(makeRecord("P00003"));
        PatientRecord[] all = l.traverseRecords();
        assertEquals(3, all.length);
        assertEquals("P00001", all[0].getId());
        assertEquals("P00003", all[2].getId());
    }
}
