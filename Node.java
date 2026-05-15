// A Node is one link in a chain.
// It holds one PatientRecord and a pointer to the next Node.
// Used by SinglyLinkedList and PatientQueue.
public class Node {

    // The patient record stored at this spot in the chain.
    public PatientRecord value;
    // Points to the next node in the chain, or null if this is the last one.
    public Node next;

    // Constructor: makes a new node holding the given record.
    public Node(PatientRecord v) {
        // Save the record.
        this.value = v;
        // No next node yet.
        this.next = null;
    }
}
