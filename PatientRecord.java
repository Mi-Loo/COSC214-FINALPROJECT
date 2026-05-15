// This class represents one patient record from the dataset.
// Each row of the CSV file gets turned into one of these objects.
public class PatientRecord {

    // These are the fields we store for each patient.
    private String id;               // Unique ID like P00001
    private String name;             // Patient's name
    private int age;                 // Patient's age in years
    private String gender;           // Male or Female
    private String medicalCondition; // What the patient has, like Diabetes
    private String hospital;         // Which hospital they went to
    private String admissionType;    // Emergency, Urgent, or Elective
    private double billingAmount;    // How much they were billed in dollars

    // Constructor: builds a new PatientRecord with all the fields filled in.
    public PatientRecord(String id, String name, int age, String gender,
                         String medicalCondition, String hospital,
                         String admissionType, double billingAmount) {
        // Save each value into this object's fields.
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.hospital = hospital;
        this.admissionType = admissionType;
        this.billingAmount = billingAmount;
    }

    // Getters: each one returns one field.
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getMedicalCondition() { return medicalCondition; }
    public String getHospital() { return hospital; }
    public String getAdmissionType() { return admissionType; }
    public double getBillingAmount() { return billingAmount; }

    // Returns a readable string showing all the fields on one line.
    public String toString() {
        // Stick all the fields together with separators.
        String result = id + " | " + name + " | age=" + age + " | " + gender
             + " | " + medicalCondition + " | " + admissionType
             + " | $" + billingAmount;
        return result;
    }
}
