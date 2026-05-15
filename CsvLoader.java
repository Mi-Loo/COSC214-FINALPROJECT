import java.io.File;
import java.util.Scanner;

// Loads the Kaggle healthcare CSV file and turns each row
// into a PatientRecord. Uses Scanner to read line by line.
// The Kaggle file doesn't have IDs, so we generate them:
// P00001, P00002, P00003, ...
public class CsvLoader {

    // Reads the CSV file and returns an array of PatientRecords.
    public static PatientRecord[] load(String path) {
        // First pass: count how many data lines the file has.
        int lineCount = 0;
        try {
            // Open the file with Scanner.
            Scanner counter = new Scanner(new File(path));
            // Skip the header line.
            if (counter.hasNextLine()) {
                counter.nextLine();
            }
            // Count every remaining non-blank line.
            while (counter.hasNextLine()) {
                String line = counter.nextLine();
                if (line.trim().length() > 0) {
                    lineCount = lineCount + 1;
                }
            }
            counter.close();
        } catch (Exception e) {
            System.out.println("Error counting lines: " + e.getMessage());
            return new PatientRecord[0];
        }

        // Now we know how big to make the array.
        PatientRecord[] records = new PatientRecord[lineCount];

        // Second pass: actually read and parse each line.
        try {
            Scanner reader = new Scanner(new File(path));
            // Read the header line to find which column is which.
            String headerLine = reader.nextLine();
            String[] headers = splitCsvLine(headerLine);

            // Figure out which column number holds each field.
            // Start with -1 meaning "not found yet".
            int nameCol = -1;
            int ageCol = -1;
            int genderCol = -1;
            int conditionCol = -1;
            int hospitalCol = -1;
            int admissionCol = -1;
            int billingCol = -1;

            // Look at each header name.
            for (int i = 0; i < headers.length; i++) {
                // Clean up and lowercase the header.
                String h = headers[i].trim().toLowerCase();
                // Match it to our fields.
                if (h.equals("name")) {
                    nameCol = i;
                } else if (h.equals("age")) {
                    ageCol = i;
                } else if (h.equals("gender")) {
                    genderCol = i;
                } else if (h.equals("medical condition")) {
                    conditionCol = i;
                } else if (h.equals("hospital")) {
                    hospitalCol = i;
                } else if (h.equals("admission type")) {
                    admissionCol = i;
                } else if (h.equals("billing amount")) {
                    billingCol = i;
                }
            }

            // Counter for generating IDs.
            int idCounter = 1;
            // Index into our records array.
            int recordIndex = 0;

            // Read each data line.
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                // Skip blank lines.
                if (line.trim().length() == 0) {
                    continue;
                }
                // Split the line into columns.
                String[] parts = splitCsvLine(line);

                // Build the ID string like P00001.
                String id = padId(idCounter);
                idCounter = idCounter + 1;

                // Pull each field from the right column.
                String name = getColumn(parts, nameCol, "Unknown");
                int age = parseIntSafe(getColumn(parts, ageCol, "0"));
                String gender = getColumn(parts, genderCol, "Unknown");
                String condition = getColumn(parts, conditionCol, "Unknown");
                String hospital = getColumn(parts, hospitalCol, "Unknown");
                String admission = getColumn(parts, admissionCol, "Elective");
                double billing = parseDoubleSafe(getColumn(parts, billingCol, "0"));

                // Build the record and put it in the array.
                PatientRecord r = new PatientRecord(id, name, age, gender,
                        condition, hospital, admission, billing);
                records[recordIndex] = r;
                recordIndex = recordIndex + 1;
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return records;
    }

    // Makes an ID string like P00001 from a number.
    private static String padId(int number) {
        // Turn the number into a string.
        String numStr = "" + number;
        // Pad with zeros until it's 5 digits.
        while (numStr.length() < 5) {
            numStr = "0" + numStr;
        }
        // Put "P" in front.
        return "P" + numStr;
    }

    // Safely gets one column from a row.
    // Returns the fallback if the column doesn't exist or is empty.
    private static String getColumn(String[] parts, int index, String fallback) {
        // Make sure the index is valid.
        if (index < 0 || index >= parts.length) {
            return fallback;
        }
        // Get and clean up the value.
        String v = parts[index].trim();
        // If empty, use the fallback.
        if (v.length() == 0) {
            return fallback;
        }
        return v;
    }

    // Tries to turn a string into an int. Returns 0 if it fails.
    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Tries to turn a string into a double. Returns 0.0 if it fails.
    private static double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Splits a CSV line into its columns.
    // Handles commas inside double-quoted values.
    private static String[] splitCsvLine(String line) {
        // First, count how many columns there are.
        int columnCount = 1; // At least one column.
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                columnCount = columnCount + 1;
            }
        }

        // Now split into an array of that size.
        String[] result = new String[columnCount];
        int col = 0;
        String current = "";
        inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            // If we see a quote, flip in/out of quoted mode.
            if (c == '"') {
                inQuotes = !inQuotes;
            }
            // If we see a comma outside of quotes, finish this column.
            else if (c == ',' && !inQuotes) {
                result[col] = current;
                col = col + 1;
                current = "";
            }
            // Otherwise, add the character to the current column.
            else {
                current = current + c;
            }
        }
        // Save the last column.
        result[col] = current;

        return result;
    }
}
