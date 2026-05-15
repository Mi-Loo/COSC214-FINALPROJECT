// A singly linked list is a chain of Node objects.
// Each node holds one record and points to the next node.
// We keep track of the first node (head).
// New records go at the end of the chain.
public class SinglyLinkedList {

    // The first node in the chain. Null if the list is empty.
    private Node head;
    // How many nodes are in the chain.
    private int size;

    // Constructor: start with an empty list.
    public SinglyLinkedList() {
        head = null;
        size = 0;
    }

    // Adds a record to the end of the chain.
    // Returns false if the ID already exists.
    public boolean insertRecord(PatientRecord r) {
        // Check for duplicate ID first.
        PatientRecord existing = searchRecord(r.getId());
        if (existing != null) {
            return false;
        }
        // Make a new node to hold this record.
        Node newNode = new Node(r);
        // If the list is empty, the new node becomes the head.
        if (head == null) {
            head = newNode;
        } else {
            // Walk to the last node in the chain.
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            // Attach the new node after the last one.
            current.next = newNode;
        }
        // One more node in the list.
        size = size + 1;
        return true;
    }

    // Searches for a record by walking the chain and checking each ID.
    // Returns the record if found, null if not.
    public PatientRecord searchRecord(String id) {
        // Start at the head.
        Node current = head;
        // Walk until we reach the end.
        while (current != null) {
            // Check if this node has the ID we want.
            if (current.value.getId().equals(id)) {
                return current.value;
            }
            // Move to the next node.
            current = current.next;
        }
        // Reached the end, didn't find it.
        return null;
    }

    // Removes a record by ID.
    // Returns true if removed, false if not found.
    public boolean deleteRecord(String id) {
        // Can't delete from an empty list.
        if (head == null) {
            return false;
        }
        // Special case: the head is the one we want to remove.
        if (head.value.getId().equals(id)) {
            // Skip the head. The second node becomes the new head.
            head = head.next;
            size = size - 1;
            return true;
        }
        // Otherwise, walk with a "previous" pointer so we can unlink.
        Node previous = head;
        while (previous.next != null) {
            // Check if the NEXT node is the one to delete.
            if (previous.next.value.getId().equals(id)) {
                // Skip over the target node.
                previous.next = previous.next.next;
                size = size - 1;
                return true;
            }
            // Move forward.
            previous = previous.next;
        }
        // Didn't find it.
        return false;
    }

    // Returns every record in an array, from head to tail.
    public PatientRecord[] traverseRecords() {
        // Make an array with exactly enough room.
        PatientRecord[] result = new PatientRecord[size];
        // Walk the chain and fill in the array.
        int i = 0;
        Node current = head;
        while (current != null) {
            result[i] = current.value;
            i = i + 1;
            current = current.next;
        }
        return result;
    }

    // Returns how many records are in the list.
    public int size() {
        return size;
    }
}
