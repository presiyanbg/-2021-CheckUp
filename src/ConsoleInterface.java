import java.util.*;
import java.lang.*;

public class ConsoleInterface {
    /*
       **********************************************************
            User info
       **********************************************************
    */
    private String username, password, userPrivilege;

    /*
      **********************************************************
           Loads program if user info is correct
      **********************************************************
    */
    private boolean loadProgram = false;

    /*
      **********************************************************
           Program options
      **********************************************************
    */
    //  ADMIN
    private final LinkedHashMap<Integer, String> adminOptions = new LinkedHashMap<Integer, String>() {{
        put(1, "Add new Admin");
        put(2, "Add new Doctor");
        put(3, "Add new Patient");
        put(4, "See all Diseases");
        put(5, "Write weakly report");
        put(7, "Make appointment ");
        put(0, "Exit");
    }};
    // DOCTOR
    private final LinkedHashMap<Integer, String> doctorOptions = new LinkedHashMap<Integer, String>() {{
        put(1, "Add new Patient");
        put(2, "See all Patients");
        put(3, "See all Diseases");
        put(0, "Exit");
    }};
    // PATIENT
    private final LinkedHashMap<Integer, String> patientOptions = new LinkedHashMap<Integer, String>() {{
        put(1, "See all Diseases");
        put(2, "Write weakly report");
        put(3, "Make appointment ");
        put(0, "Exit");
    }};

    /*
      **********************************************************
           Database connection
      **********************************************************
    */
    private DataBaseLoader dbLoader;

    /*
      **********************************************************
           All Diseases
      **********************************************************
    */
    private LinkedHashMap<Integer, Disease> allDiseases;

    /*
      **********************************************************
           Diseases Connected To Bad Habits
      **********************************************************
    */
    private LinkedHashMap<Disease, String> diseasesToBadHabits;

    /*
      **********************************************************
           List of all patients
      **********************************************************
    */
    private List <Patient> allPatients;

    /*
      **********************************************************
           Scanner
      **********************************************************
    */
    Scanner in = new Scanner(System.in);
    ScannerChacker scanCheck = new ScannerChacker();

    /*
      **********************************************************
           Load files
      **********************************************************
    */
    FileHandler loadFiles = new FileHandler();

    /*
        **********************************************************
            Basic Constructor
        **********************************************************
     */
    public ConsoleInterface() {
        System.out.println("Program loaded");

        dbLoader = new DataBaseLoader();
    }

    /*
        **********************************************************
            Sign in
        **********************************************************
     */
    public void signIn() {
        System.out.println("Welcome");
        System.out.println("Login");

        //Gets username and password from users - max 3 mistakes
        for (int i = 0; i <= 2; i++) {
            //Get username/email
            System.out.print("Username Or Email: ");

            this.username = in.nextLine();

            //Get password
            System.out.print("Password: ");

            this.password = in.nextLine();

            //Checks username and password inside a file
            if (dbLoader.checkUsernameAndPassword(username, password)) {
                this.loadProgram = true;

                this.userPrivilege = dbLoader.getUserPrivilege(username);

                this.loadInterface();

                break;
            } else {
                System.err.println("Incorrect username of password. Tries left: " + (2 - i));
            }
        }
    }

    /*
        **********************************************************
            Loads interface - choose program option
        **********************************************************
     */
    public  void loadInterface() {
        if (loadProgram) {
            int userOption = 0;

            boolean validOption = false;

            List <Integer> maxProgramOptions = new ArrayList<>();

            LinkedHashMap<Integer, String> programOptions = this.patientOptions;

            System.out.println("Chose an option (0,1..)");

            //Writes all options
            switch (userPrivilege) {
                case "a" :
                    programOptions = this.adminOptions;

                    break;
                case "d":
                    programOptions = this.doctorOptions;

                    break;
                case "p":
                    programOptions = this.patientOptions;

                    break;
                default:
                    System.out.println("User has no options");

                    break;
            }

            if (programOptions != null) {
                System.out.println("");
                System.out.println(" ****************************************** ");

                for (Map.Entry info : programOptions.entrySet()) {
                    System.out.println( info.getKey() + "  |  " +  info.getValue().toString());

                    maxProgramOptions.add((Integer) info.getKey());
                }

                System.out.println(" ****************************************** ");
                System.out.println("");
            }

            while (!validOption) {
                userOption = scanCheck.checkIfInt();

                if (maxProgramOptions.contains(userOption)) {
                    validOption = true;
                } else {
                    System.err.println("Not a valid option: " + userOption);
                }
            }

            //System.out.println("You have choosen: " + userOption + " | " + programOptions.get(userOption));

            openProgramOption((String) programOptions.get(userOption));
        }
    }

    /*
        **********************************************************
            Opens chosen option from loadInterface()
        **********************************************************
     */
    public  void openProgramOption (String option) {
        boolean stopProgram = false;

        switch (option) {
            case "Add new Admin" :
                this.insertNewAdmin();

                break;
            case "Add new Doctor" :
                this.insertNewDoctor();

                break;
            case "Add new Patient" :
                this.insertNewPatient();

                break;
            case "See all Diseases" :
                this.seeDiseasesToBadHabits();

                break;
            case "Write weakly report" :
                System.out.println("Write weakly report");

                break;
            case "Make appointment " :
                System.out.println("Make appointment ");

                break;
            case "See all Patients " :
                System.out.println("See all Patients ");

                break;
            case "Exit" :
                stopProgram = true;

                break;
            default:
                break;
        }

        if (stopProgram) {
            this.exitProgram();
        } else {
            this.loadInterface();
        }
    }

    /*
        **********************************************************
            Program Options
        **********************************************************
     */
    //Insert New Admin
    public void insertNewAdmin() {
        Admin adminInsert = new Admin();

        adminInsert.insertAdmin();
    }

    //Insert New Doctor
    public void insertNewDoctor() {
        Doctor doctorInsert = new Doctor();

        doctorInsert.insertDoctor();
    }

    //Insert New Patient
    public void insertNewPatient() {
        int doctorId = dbLoader.getDoctorId(this.username);

        Patient patient = new Patient();

        patient.insertPatientToDB(doctorId);
    }

    //Seee diseases connection to bad habits
    public void seeDiseasesToBadHabits() {
        this.getAllDiseases();

        this.getDiseasesToBadHabits();

        //Go trough diseases
        for (Map.Entry<Integer, Disease> DiseaseEntry : allDiseases.entrySet()) {
            System.out.println(DiseaseEntry.getKey() + " / " + DiseaseEntry.getValue());
        }

        System.out.println("Choose disease:");

        //Max And Min Values for Disease Key
        int firstDisease = 1;

        int lastDisease = getLastDisease(allDiseases);

        //Get Value in Min/Max
        int diseaseNumber = scanCheck.checkIfIntBetween(firstDisease, lastDisease - 1);

        String chosenDisease = allDiseases.get(diseaseNumber).toString();

        System.out.println("Choosen Disease: " + chosenDisease);

        System.out.println("Number of connections / Habit Name ");

        int badHabitCounter = 0;

        //Display Bad Habits that are connected to the chosen Disease
        for (Map.Entry<Disease, String> DiseaseEntry : diseasesToBadHabits.entrySet()) {
            if (chosenDisease.equals(DiseaseEntry.getKey().toString())) {
                String badHabit = DiseaseEntry.getValue();

                String diseaseKey = DiseaseEntry.getKey().toString();

                //Count how many times a Bad Habit is connected to a Disease
                for (Map.Entry<Disease, String> secondDiseaseEntry : diseasesToBadHabits.entrySet()) {
                    if (badHabit.equals(secondDiseaseEntry.getValue()) && diseaseKey.equals(secondDiseaseEntry.getKey().toString())) {
                        badHabitCounter++;
                    }
                }

                System.out.println(badHabitCounter + " / " + badHabit);

                badHabitCounter = 0;
            }
        }
    }

    //Writes all diseases to the console
    public void printAllDiseases() {
//        this.loadAllDiseasesFromDB();
//
//        for (int key : allDiseases.keySet()) {
//            System.out.println(allDiseases.get(key).getName());
//        }
    }

    //Loads all patients from a file
    public void  loadAllPatients() {
//        this.loadAllDiseases();
//        this.allPatients =  loadFiles.loadPatients("patients.txt", this.allDiseases);
    }

    //Writes all patients to the console
    public  void seeAllPatients() {
        this.loadAllPatients();

        for (Patient patient:this.allPatients) {
            System.out.println(patient.toString());
        }
    }

    //Exits program
    public int exitProgram() {
        System.out.println("Bye");

        return 1;
    }

    /*
      **********************************************************
                            Needed Functions
      **********************************************************
    */

    /*
      **********************************************************
           Get Last Disease (Count Diseases)
      **********************************************************
    */
    public static int getLastDisease(LinkedHashMap<Integer, Disease> lhm) {
        int count = 1;

        for (Map.Entry<Integer, Disease> it : lhm.entrySet()) {
            count++;
        }

        return count;
    }

    /*
      **********************************************************
           Get All Diseases
      **********************************************************
    */
    public void getAllDiseases() {
        this.allDiseases = dbLoader.getAllDiseases();
    }

    /*
      **********************************************************
           Get Disease Connected To Bad Habits
      **********************************************************
    */
    public void getDiseasesToBadHabits() {
        this.diseasesToBadHabits = dbLoader.getDiseasesToBadHabits();
    }

}

