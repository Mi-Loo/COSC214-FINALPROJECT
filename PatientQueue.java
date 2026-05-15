// A priority queue for patient admissions.
// Patients are ordered by admission type so that the most urgent
// cases are always processed first, regardless of arrival order.
//
// Priority rule:
//   Emergency = 1  (highest priority, processed first)
//   Urgent    = 2
//   Elective  = 3  (lowest priority, processed last)
//
// When a new patient is admitted, we walk the queue and insert them
// immediately before the first patient who has a lower priority score.
// This keeps the chain sorted at all times, so processNext() can
// still just pop from the front in O(1).
//
// Insert is O(n) in the worst case because we may have to walk the
// whole chain to find the right spot. All other operations are the
// same as a regular queue.
public class PatientQueue {

    // The front of the line (highest priority patient, served next).
    private Node front;
    // The back of the line (lowest priority patient).
    private Node back;
    // How many patients are currently waiting.
    private int size;

    // Constructor: start with an empty queue.
    public PatientQueue() {
        front = null;
        back = null;
        size = 0;
    }

    // Converts an admission type string into a numeric priority score.
    // Lower number = higher priority = closer to the front of the queue.
    private int priorityScore(PatientRecord r) {
        String type = r.getAdmissionType();
        // Emergency patients go first.
        if (type.equalsIgnoreCase("Emergency")) {
            return 1;
        }
        // Urgent patients go second.
        if (type.equalsIgnoreCase("Urgent")) {
            return 2;
        }
        // Elective (and anything else) goes last.
        return 3;
    }

    // Admits a patient and inserts them in the correct priority position.
    // Emergency beats Urgent beats Elective.
    // Among patients with the same priority, arrival order is preserved
    // (we insert AFTER existing patients of equal priority).
    public void admit(PatientRecord r) {
        // Wrap the record in a node.
        Node newNode = new Node(r);
        int newScore = priorityScore(r);

        // Case 1: queue is empty — new node is both front and back.
        if (front == null) {
            front = newNode;
            back = newNode;
            size = size + 1;
            return;
        }

        // Case 2: new patient has higher priority than the current front.
        // Insert before the front so they go first.
        if (newScore < priorityScore(front.value)) {
            newNode.next = front;
            front = newNode;
            size = size + 1;
            return;
        }

        // Case 3: walk the chain until we find a node whose NEXT patient
        // has strictly lower priority than the new patient.
        // We stop there and insert between current and current.next.
        Node current = front;
        while (current.next != null) {
            if (newScore < priorityScore(current.next.value)) {
                // Insert between current and current.next.
                newNode.next = current.next;
                current.next = newNode;
                size = size + 1;
                return;
            }
            current = current.next;
        }

        // Case 4: new patient has the lowest priority — append to back.
        back.next = newNode;
        back = newNode;
        size = size + 1;
    }

    // Removes and returns the patient at the front of the line.
    // Because the queue is kept sorted, this is always the highest
    // priority patient. Returns null if the queue is empty.
    public PatientRecord processNext() {
        // Nothing to process.
        if (front == null) {
            return null;
        }
        // Grab the front record to return.
        PatientRecord result = front.value;
        // Move the front pointer forward one spot.
        front = front.next;
        // If the queue is now empty, back must also be null.
        if (front == null) {
            back = null;
        }
        size = size - 1;
        return result;
    }

    // Returns the highest priority patient without removing them.
    // Returns null if the queue is empty.
    public PatientRecord peek() {
        if (front == null) {
            return null;
        }
        return front.value;
    }

    // Searches for a patient by ID. Walks the whole chain.
    // Returns the record if found, null if not.
    public PatientRecord searchRecord(String id) {
        Node current = front;
        while (current != null) {
            if (current.value.getId().equals(id)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    // Removes a patient by ID regardless of their position.
    // Returns true if the patient was found and removed.
    public boolean deleteRecord(String id) {
        // Nothing to delete from an empty queue.
        if (front == null) {
            return false;
        }
        // Special case: the front patient is the one to remove.
        if (front.value.getId().equals(id)) {
            front = front.next;
            if (front == null) {
                back = null;
            }
            size = size - 1;
            return true;
        }
        // Walk with a previous pointer so we can unlink the target node.
        Node previous = front;
        while (previous.next != null) {
            if (previous.next.value.getId().equals(id)) {
                // If we're removing the back node, update the back pointer.
                if (previous.next == back) {
                    back = previous;
                }
                // Skip over the target.
                previous.next = previous.next.next;
                size = size - 1;
                return true;
            }
            previous = previous.next;
        }
        // ID not found in the queue.
        return false;
    }

    // Wraps admit() so PatientQueue shares the same interface as the
    // other three data structures (insertRecord / searchRecord / etc.).
    public boolean insertRecord(PatientRecord r) {
        admit(r);
        return true;
    }

    // Returns every patient in queue order (highest to lowest priority)
    // as an array, without removing anyone.
    public PatientRecord[] traverseRecords() {
        PatientRecord[] result = new PatientRecord[size];
        int i = 0;
        Node current = front;
        while (current != null) {
            result[i] = current.value;
            i = i + 1;
            current = current.next;
        }
        return result;
    }

    // Returns how many patients are currently in the queue.
    public int size() {
        return size;
    }
}
