namicArray array;
    private SinglyLinkedList list;
    private HashTable table;

    // The queue for the Queue tab.
    private PatientQueue queue;

    // The records loaded from the CSV file.
    private PatientRecord[] loadedRecords;

    // Records tab widgets.
    private JComboBox structurePicker;
    private JTextField idField;
    private JTextField ageField;
    private JTextField conditionField;
    private JTextField hospitalField;
    private JTextArea recordsOutput;

    // Queue tab widgets.
    private JTextField qIdField;
    private JTextField qAgeField;
    private JTextField qConditionField;
    private JTextField qHospitalField;
    private JComboBox qAdmissionPicker;   // Emergency / Urgent / Elective
    private JTextArea queueDisplay;

    // Benchmarks tab widgets.
    private DefaultTableModel benchModel;

    // Constructor: sets up the whole window.
    public MainWindow() {
        // Set the window title.
        super("MEDDATA");
        // Quit the program when the window is closed.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Set the size.
        setSize(900, 600);
        // Center on screen.
        setLocationRelativeTo(null);

        // Create empty data structures.
        array = new DynamicArray();
        list = new SinglyLinkedList();
        table = new HashTable();
        queue = new PatientQueue();
        loadedRecords = null;

        // Create the tabbed pane with three tabs.
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Records", buildRecordsTab());
        tabs.addTab("Queue", buildQueueTab());
        tabs.addTab("Benchmarks", buildBenchmarksTab());
        setContentPane(tabs);
    }

    // Records tab

    private JPanel buildRecordsTab() {
        // Main panel with left controls and right output.
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Left side: controls ----
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        // Data structure dropdown.
        left.add(new JLabel("Data Structure:"));
        structurePicker = new JComboBox(new String[]{
                "Dynamic Array", "Singly Linked List", "Hash Table"});
        structurePicker.setMaximumSize(new Dimension(220, 28));
        left.add(structurePicker);
        left.add(Box.createVerticalStrut(12));

        // Operation buttons in a 2x2 grid.
        left.add(new JLabel("Operation:"));
        JPanel opGrid = new JPanel(new GridLayout(2, 2, 6, 6));

        JButton insertBtn = new JButton("Insert");
        insertBtn.setActionCommand("INSERT");
        insertBtn.addActionListener(this);

        JButton searchBtn = new JButton("Search");
        searchBtn.setActionCommand("SEARCH");
        searchBtn.addActionListener(this);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setActionCommand("DELETE");
        deleteBtn.addActionListener(this);

        JButton traverseBtn = new JButton("Traverse");
        traverseBtn.setActionCommand("TRAVERSE");
        traverseBtn.addActionListener(this);

        opGrid.add(insertBtn);
        opGrid.add(searchBtn);
        opGrid.add(deleteBtn);
        opGrid.add(traverseBtn);
        opGrid.setMaximumSize(new Dimension(220, 80));
        left.add(opGrid);
        left.add(Box.createVerticalStrut(12));

        // Input fields.
        left.add(new JLabel("Inputs:"));
        idField = addLabeledField(left, "ID:");
        ageField = addLabeledField(left, "Age:");
        conditionField = addLabeledField(left, "Condition:");
        hospitalField = addLabeledField(left, "Hospital:");

        left.add(Box.createVerticalStrut(8));

        JButton loadBtn = new JButton("Load Dataset...");
        loadBtn.setActionCommand("LOAD");
        loadBtn.addActionListener(this);
        left.add(loadBtn);

        // Right side: output area
        recordsOutput = new JTextArea();
        recordsOutput.setEditable(false);
        recordsOutput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(recordsOutput);

        panel.add(left, BorderLayout.WEST);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // Queue tab

    private JPanel buildQueueTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side: input fields and buttons.
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(new JLabel("Add Patient to Queue"));
        left.add(Box.createVerticalStrut(8));
        qIdField = addLabeledField(left, "ID:");
        qAgeField = addLabeledField(left, "Age:");
        qConditionField = addLabeledField(left, "Condition:");
        qHospitalField = addLabeledField(left, "Hospital:");

        // Admission type dropdown — this is what drives priority ordering.
        left.add(new JLabel("Admission Type:"));
        qAdmissionPicker = new JComboBox(new String[]{
                "Emergency", "Urgent", "Elective"});
        qAdmissionPicker.setMaximumSize(new Dimension(220, 28));
        left.add(qAdmissionPicker);
        left.add(Box.createVerticalStrut(8));

        JButton admitBtn = new JButton("Admit");
        admitBtn.setActionCommand("ADMIT");
        admitBtn.addActionListener(this);
        left.add(admitBtn);

        left.add(Box.createVerticalStrut(20));
        left.add(new JSeparator());
        left.add(Box.createVerticalStrut(10));
        left.add(new JLabel("Process the patient at the front:"));

        JButton processBtn = new JButton("Process Next");
        processBtn.setActionCommand("PROCESS");
        processBtn.addActionListener(this);
        left.add(processBtn);

        // Right side: list of patients in queue.
        queueDisplay = new JTextArea();
        queueDisplay.setEditable(false);
        queueDisplay.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        queueDisplay.setBorder(BorderFactory.createTitledBorder(
                "Patients in Queue (Front to Back)"));
        JScrollPane scroll = new JScrollPane(queueDisplay);

        panel.add(left, BorderLayout.WEST);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // Benchmarks tab

    private JPanel buildBenchmarksTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: description and run button.
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel info = new JLabel("<html>Run timing tests on all four data structures "
                + "across three input sizes (1,000, 5,000, and 10,000 records).</html>");
        top.add(info);

        JButton runBtn = new JButton("Run Benchmarks");
        runBtn.setActionCommand("BENCHMARK");
        runBtn.addActionListener(this);
        top.add(runBtn);

        // Table to show results.
        benchModel = new DefaultTableModel(new Object[]{
                "Structure", "N", "Insert (ms)", "Search avg (us)",
                "Delete avg (us)", "Traverse (ms)"}, 0);
        JTable benchTable = new JTable(benchModel);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(benchTable), BorderLayout.CENTER);

        return panel;
    }

    // Helper methods

    // Adds a labeled text field to a panel and returns the field.
    private JTextField addLabeledField(JPanel parent, String label) {
        JPanel row = new JPanel(new BorderLayout(4, 0));
        row.add(new JLabel(label), BorderLayout.WEST);
        JTextField field = new JTextField();
        row.add(field, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(220, 26));
        parent.add(row);
        parent.add(Box.createVerticalStrut(4));
        return field;
    }

    // Shows a message in the records output area.
    private void showOutput(String s) {
        recordsOutput.setText(s);
        recordsOutput.setCaretPosition(0);
    }

    // Returns the name of the currently selected structure.
    private String getSelectedStructure() {
        return (String) structurePicker.getSelectedItem();
    }

    // Tries to turn a string into an int. Returns 0 on failure.
    private int parseIntOrZero(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Updates the queue display to show all current patients.
    private void refreshQueueDisplay() {
        PatientRecord[] all = queue.traverseRecords();
        String text = "Size: " + all.length + "\n\n";
        for (int i = 0; i < all.length; i++) {
            text = text + all[i].toString() + "\n";
        }
        queueDisplay.setText(text);
        queueDisplay.setCaretPosition(0);
    }

    // All button clicks come here

    public void actionPerformed(ActionEvent e) {
        // Get the command string we set on each button.
        String command = e.getActionCommand();

        if (command.equals("INSERT")) {
            doInsert();
        } else if (command.equals("SEARCH")) {
            doSearch();
        } else if (command.equals("DELETE")) {
            doDelete();
        } else if (command.equals("TRAVERSE")) {
            doTraverse();
        } else if (command.equals("LOAD")) {
            doLoadDataset();
        } else if (command.equals("ADMIT")) {
            doAdmit();
        } else if (command.equals("PROCESS")) {
            doProcess();
        } else if (command.equals("BENCHMARK")) {
            doBenchmark();
        }
    }

    // Records tab operations

    private void doInsert() {
        // Get the ID the user typed.
        String id = idField.getText().trim();
        if (id.length() == 0) {
            showOutput("ID is required for insert.");
            return;
        }
        // Parse age, default to 0 if blank.
        int age = parseIntOrZero(ageField.getText());
        // Build a record from the input fields.
        PatientRecord r = new PatientRecord(
                id, "Manual", age, "Unknown",
                conditionField.getText().trim(),
                hospitalField.getText().trim(),
                "Elective", 0.0);

        // Insert into whichever structure is selected.
        String picked = getSelectedStructure();
        boolean ok = false;
        if (picked.equals("Dynamic Array")) {
            ok = array.insertRecord(r);
        } else if (picked.equals("Singly Linked List")) {
            ok = list.insertRecord(r);
        } else if (picked.equals("Hash Table")) {
            ok = table.insertRecord(r);
        }

        // Show result.
        if (ok) {
            showOutput("Inserted " + id);
        } else {
            showOutput("Insert failed: duplicate ID " + id);
        }
    }

    private void doSearch() {
        String id = idField.getText().trim();
        if (id.length() == 0) {
            showOutput("Enter an ID to search.");
            return;
        }
        // Search in the selected structure.
        String picked = getSelectedStructure();
        PatientRecord found = null;
        if (picked.equals("Dynamic Array")) {
            found = array.searchRecord(id);
        } else if (picked.equals("Singly Linked List")) {
            found = list.searchRecord(id);
        } else if (picked.equals("Hash Table")) {
            found = table.searchRecord(id);
        }

        // Show result in a pop-up and in the output area.
        if (found == null) {
            JOptionPane.showMessageDialog(this, "No record found for " + id,
                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
            showOutput("No record found for " + id);
        } else {
            String msg = "Record found:\n"
                    + "ID: " + found.getId() + "\n"
                    + "Age: " + found.getAge() + "\n"
                    + "Condition: " + found.getMedicalCondition() + "\n"
                    + "Hospital: " + found.getHospital();
            JOptionPane.showMessageDialog(this, msg, "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
            showOutput(msg);
        }
    }

    private void doDelete() {
        String id = idField.getText().trim();
        if (id.length() == 0) {
            showOutput("Enter an ID to delete.");
            return;
        }
        // Ask the user to confirm.
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete record " + id + "?\nThis cannot be undone.",
                "Confirm Delete", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }
        // Delete from the selected structure.
        String picked = getSelectedStructure();
        boolean ok = false;
        if (picked.equals("Dynamic Array")) {
            ok = array.deleteRecord(id);
        } else if (picked.equals("Singly Linked List")) {
            ok = list.deleteRecord(id);
        } else if (picked.equals("Hash Table")) {
            ok = table.deleteRecord(id);
        }

        if (ok) {
            showOutput("Deleted " + id);
        } else {
            showOutput("No record found for " + id);
        }
    }

    private void doTraverse() {
        // Get all records from the selected structure.
        String picked = getSelectedStructure();
        PatientRecord[] all = null;
        if (picked.equals("Dynamic Array")) {
            all = array.traverseRecords();
        } else if (picked.equals("Singly Linked List")) {
            all = list.traverseRecords();
        } else if (picked.equals("Hash Table")) {
            all = table.traverseRecords();
        }

        // Build a string showing all records (up to 500).
        String text = "Total: " + all.length + " records\n\n";
        int shown = 0;
        for (int i = 0; i < all.length; i++) {
            text = text + all[i].toString() + "\n";
            shown = shown + 1;
            if (shown >= 500) {
                text = text + "... (" + (all.length - shown) + " more)\n";
                break;
            }
        }
        showOutput(text);
    }

    private void doLoadDataset() {
        // Show a file chooser.
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        chooser.setDialogTitle("Select dataset file to load");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = chooser.getSelectedFile();
        // Load the CSV file.
        loadedRecords = CsvLoader.load(f.getAbsolutePath());
        // Reset all structures to empty.
        array = new DynamicArray();
        list = new SinglyLinkedList();
        table = new HashTable();
        // Insert every record into all three structures.
        for (int i = 0; i < loadedRecords.length; i++) {
            array.insertRecord(loadedRecords[i]);
            list.insertRecord(loadedRecords[i]);
            table.insertRecord(loadedRecords[i]);
        }
        showOutput("Loaded " + loadedRecords.length
                + " records from " + f.getName()
                + " into all three structures.");
    }

    // Queue tab operations

    private void doAdmit() {
        String id = qIdField.getText().trim();
        if (id.length() == 0) {
            return;
        }
        // Read the admission type the user selected — this is what
        // determines where in the priority queue this patient is placed.
        String admissionType = (String) qAdmissionPicker.getSelectedItem();
        PatientRecord r = new PatientRecord(
                id, "Manual",
                parseIntOrZero(qAgeField.getText()),
                "Unknown",
                qConditionField.getText().trim(),
                qHospitalField.getText().trim(),
                admissionType, 0.0);
        queue.admit(r);
        refreshQueueDisplay();
    }

    private void doProcess() {
        PatientRecord r = queue.processNext();
        if (r == null) {
            JOptionPane.showMessageDialog(this, "Queue is empty.",
                    "Queue", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Now serving: " + r.toString(),
                    "Queue", JOptionPane.INFORMATION_MESSAGE);
        }
        refreshQueueDisplay();
    }

    // Benchmark operation

    private void doBenchmark() {
        // Make sure records are loaded.
        if (loadedRecords == null || loadedRecords.length < 10000) {
            JOptionPane.showMessageDialog(this,
                    "Load a dataset with at least 10,000 records first "
                    + "(use Records tab > Load Dataset).",
                    "Benchmarks", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Clear old results.
        benchModel.setRowCount(0);
        // Run the benchmarks.
        BenchmarkRow[] rows = Benchmark.runAll(loadedRecords);
        // Add each result to the table.
        for (int i = 0; i < rows.length; i++) {
            BenchmarkRow r = rows[i];
            // Convert nanoseconds to milliseconds and microseconds.
            String insertMs = String.format("%.3f", r.insertNs / 1000000.0);
            String searchUs = String.format("%.3f", r.searchNs / 1000.0);
            String deleteUs = String.format("%.3f", r.deleteNs / 1000.0);
            String traverseMs = String.format("%.3f", r.traverseNs / 1000000.0);

            benchModel.addRow(new Object[]{
                    r.structure, r.n, insertMs, searchUs, deleteUs, traverseMs
            });
        }
    }

    // Program entry point

    public static void main(String[] args) {
        // Create and show the window.
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}
