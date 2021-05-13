import java.util.*;

public class Disease {
    private String name;

    //List of patients sick with this disease.
    private List <String> sickPatientsNames = new ArrayList<String>();;

    // Constructor.

    public Disease(String name_in) {
        this.name = name_in;
    }

    // Basic Functions.

    public String getName() { return this.name; }

    public void rename(String new_name) {
        this.name = new_name;
    }

    public String toString() {
        return this.name;
    }

    public boolean compare(Disease dis2) {
        return this.name.equals(dis2.toString());
    }

    // List all patients sick with this disease.

    public void listAllPatients (List <String> listIn) {
        this.sickPatientsNames = listIn;
    }

    // Add new patient to the sick list.

    public void addPatient (String patientNameIn) {
        this.sickPatientsNames.add(patientNameIn);
    }

    // Remove patient from the sick list.

    public void removePatient (String patientNameOut) {
        this.sickPatientsNames.remove(patientNameOut);
    }

}
