import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests for the PatientQueue class (priority queue version).
// Emergency > Urgent > Elective regardless of arrival order.
public class PatientQueueTest {

    // Helper: builds a record with a specific ID and admission type.
    private PatientRecord makeRecord(String id, String admissionType) {
        return new PatientRecord(id, "Test Patient", 40, "M",
                "Hypertension", "General Hospital", admissionType, 3000.0);
    }

    // Emergency patient admitted last should come out first.
    @Test
    void emergencyBeatsElective() {
        PatientQueue q = new PatientQueue();
        q.admit(makeRecord("P00001", "Elective"));
        q.admit(makeRecord("P00002", "Emergency"));
        // P00002 was admitted second but is Emergency — should be processed first.
        assertEquals("P00002", q.processNext().getId());
        assertEquals("P00001", q.processNext().getId());
    }

    // Full priority order: Emergency -> Urgent -> Elective.
    @Test
    void fullPriorityOrder() {
        PatientQueue q = new PatientQueue();
        // Admitted in worst-case order (lowest priority first).
        q.admit(makeRecord("P00001", "Elective"));
        q.admit(makeRecord("P00002", "Urgent"));
        q.admit(makeRecord("P00003", "Emergency"));
        // Should come out Emergency -> Urgent -> Elective.
        assertEquals("P00003", q.processNext().getId());
        assertEquals("P00002", q.processNext().getId());
        assertEquals("P00001", q.processNext().getId());
    }

    // Among patients with the same priority, arrival order is preserved.
    @Test
    void samepriorityPreservesArrivalOrder() {
        PatientQueue q = new PatientQueue();
        q.admit(makeRecord("P00001", "Emergency"));
        q.admit(makeRecord("P00002", "Emergency"));
        q.admit(makeRecord("P00003", "Emergency"));
        // All Emergency — should come out in the order they arrived.
        assertEquals("P00001", q.processNext().getId());
        assertEquals("P00002", q.processNext().getId());
        assertEquals("P00003", q.processNext().getId());
    }

    // processNext() on an empty queue returns null, no crash.
    @Test
    void processEmptyReturnsNull() {
        PatientQueue q = new PatientQueue();
        assertNull(q.processNext());
    }

    // peek() shows the front patient without removing them.
    @Test
    void peekDoesNotRemove() {
        PatientQueue q = new PatientQueue();
        q.admit(makeRecord("P00001", "Emergency"));
        assertEquals("P00001", q.peek().getId());
        assertEquals("P00001", q.peek().getId());
        assertEquals(1, q.size());
    }

    // searchRecord() finds a patient by ID anywhere in the queue.
    @Test
    void searchFindsPatient() {
        PatientQueue q = new PatientQueue();
        q.admit(makeRecord("P00001", "Elective"));
        q.admit(makeRecord("P00002", "Emergency"));
        assertNotNull(q.searchRecord("P00001"));
        assertNotNull(q.searchRecord("P00002"));
    }

    // deleteRecord() removes a patient by ID without disturbing the others.
    @Test
    void deleteRemovesCorrectPatient() {
        PatientQueue q = new PatientQueue();
        q.admit(makeRecord("P00001", "Elective"));
        q.admit(makeRecord("P00002", "Urgent"));
        q.admit(makeRecord("P00003", "Emergency"));
        assertTrue(q.deleteRecord("P00002"));
        assertEquals(2, q.size());
        assertNull(q.searchRecord("P00002"));
        assertNotNull(q.searchRecord("P00001"));
        assertNotNull(q.searchRecord("P00003"));
    }

    // traverseRecords() returns all patients in priority order.
    @Test
    void traverseReturnsPriorityOrder() {
        PatientQueue q = new PatientQueue();
        q.admit(makeRecord("P00001", "Elective"));
        q.admit(makeRecord("P00002", "Emergency"));
        PatientRecord[] all = q.traverseRecords();
        assertEquals(2, all.length);
        // Emergency should be first in the traversal.
        assertEquals("P00002", all[0].getId());
        assertEquals("P00001", all[1].getId());
    }

    // Size starts at zero and tracks correctly after admits and removes.
    @Test
    void sizeTracksCorrectly() {
        PatientQueue q = new PatientQueue();
        assertEquals(0, q.size());
        q.admit(makeRecord("P00001", "Urgent"));
        q.admit(makeRecord("P00002", "Elective"));
        assertEquals(2, q.size());
        q.processNext();
        assertEquals(1, q.size());
    }
}
