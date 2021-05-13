import java.util.*;

public class Patient {

    /*
        **********************************************************
            Patient Data
        **********************************************************
     */
    private boolean isPatientInit = false;

    //Basic Data
    private LinkedHashMap <String, String> basicPatientInfo = new LinkedHashMap<String, String>()
    {{
        put("First Name", "");
        put("Last Name", "");
        put("Gender", "");
        put("EGN", "");
    }};

    //Patient Condition
    private LinkedHashMap <String, Integer> conditionInfo = new LinkedHashMap<String, Integer>()
    {{
        put("Sick", 0);
        put("High blood pressure", 0);
    }};

    private String conditionDescription;

    // Diseases
    private LinkedHashMap<Integer, Disease> patientDiseasesInfo = new LinkedHashMap<Integer, Disease>();

    //Weight and Height
    private LinkedHashMap <String, Integer> bodyInfo = new LinkedHashMap<String, Integer>() {{
        put("Weight", 0);
        put("Height", 0);
    }};

    //Food intake
    private LinkedHashMap <String, Integer> foodInfo = new LinkedHashMap<String, Integer>() {{
        put("Average Calories", 0);
        put("Average Proteins", 0);
        put("Average Carbs", 0);
        put("Average Fats", 0);
    }};

    //Patient Bad Habits
    private LinkedHashMap<Integer, String> badHabitsInfo = new LinkedHashMap<Integer, String>();

    /*
       **********************************************************
            Scanner
       **********************************************************
    */
    Scanner in = new Scanner(System.in);
    ScannerChacker scanCheck = new ScannerChacker();

     /*
        **********************************************************
            Database connection
        **********************************************************
     */
    private DataBaseLoader dbLoader;

    private DataBaseInserter dbInsert;

    /*
       **********************************************************
            Patient ID - WHEN ADDED TO DATABASE
       **********************************************************
    */
    private int patientId;

    /*
        **********************************************************
            List of all Diseases
        **********************************************************
     */
    private LinkedHashMap<Integer, Disease> allDiseases = new LinkedHashMap<Integer, Disease>();

    /*
        **********************************************************
            List of all badHabits
        **********************************************************
     */
    private LinkedHashMap<Integer, String> allBadHabits = new LinkedHashMap<Integer, String>();

    /*
        **********************************************************
            Diseases Connected To Bad Habits
        **********************************************************
     */
        private LinkedHashMap<Disease, String> diseasesToBadHabits = new LinkedHashMap<Disease, String>();

    /*
       **********************************************************
            Constructors
       **********************************************************
    */
    public Patient () {
        this.loadAllFromDatabase();
    }

    /*
       **********************************************************
            Insert Patient To DATABASE
       **********************************************************
    */
    public void insertPatientToDB(int doctorId) {
        if (!this.isPatientInit) {
            this.initNewPatient();
        }

        //dbInsert.insertPatient(basicPatientInfo.get("First Name"), basicPatientInfo.get("Last Name"), basicPatientInfo.get("Gender"), Integer.parseInt(basicPatientInfo.get("EGN")), bodyInfo.get("Weight"), bodyInfo.get("Height"));

        //this.patientId = dbInsert.getLastPatientID();

        //dbInsert.insertPatientToDoctor(patientId, doctorId);

        //dbInsert.insertPatientAppointment(patientId, doctorId);

        //dbInsert.insertPatientCondition(patientId, conditionInfo.get("Sick"), conditionInfo.get("High blood pressure"), conditionDescription );

        if (patientDiseasesInfo != null) {
            dbInsert.insertPatientToDisease(patientId, patientDiseasesInfo);

            dbInsert.insertDiseasesToBadHabits(patientDiseasesInfo, badHabitsInfo);
        }

        dbInsert.insertPatientEatingHabits(patientId, foodInfo.get("Average Calories"), foodInfo.get("Average Proteins"), foodInfo.get("Average Carbs"), foodInfo.get("Average Fats"));

        dbInsert.insertPatientToBadHabits(patientId, badHabitsInfo);
    }

    /*
       **********************************************************
            Loading From Database
       **********************************************************
    */
    public void loadDatabase() {
        this.dbInsert = new DataBaseInserter();

        this.dbLoader = new DataBaseLoader();
    }

    public void loadAllDiseasesFromDB() {
        this.allDiseases = dbLoader.getAllDiseases();
    }

    public void loadAllBadHabitsFromDB() {
        this.allBadHabits = dbLoader.getAllBadHabits();
    }

    public void loadDiseasesToBadHabits() {
        this.diseasesToBadHabits = dbLoader.getDiseasesToBadHabits();
    }

    public void loadAllFromDatabase() {
        this.loadDatabase();

        this.loadAllBadHabitsFromDB();

        this.loadAllDiseasesFromDB();

        this.loadDiseasesToBadHabits();
    }
    /*
         **********************************************************
            Creates new Patients
         **********************************************************
    */
    public void initNewPatient() {
        this.loadAllFromDatabase();

        System.out.println("Adding New Patient!");

        //Insert Patient basic data:
        this.insertPatientBasicData();
//        this.getPatientBasicData();

//        //Insert Patient condition
        this.insertPatientCondition();
//        this.getPatientCondition();

//        //Insert Patient weight and height
        this.insertPatientWeightAndHeight();
//        this.getPatientWeightAndHeight();
//
//        //Insert Patient food intake
        this.insertPatientFoodIntake();
//           this.getPatientFoodIntake();
//
//        //Insert Patient Bad Habits
        this.insertPatientBadHabits();

        //Check if Patient is in danger of getting a diseases
        this.alertPatientForDisease();

        this.isPatientInit = true;

//        //Write all Patient Data
//        this.getPatientAllData();
    }

     /*
          **********************************************************
            Insert Patient Data
          **********************************************************
    */
    //Insert Patient basic data:
    public void insertPatientBasicData() {
        for (Map.Entry info : basicPatientInfo.entrySet()) {
            System.out.println("Please enter patient's " + (String) info.getKey() + ": ");
            String dataIn = in.nextLine();

            this.basicPatientInfo.replace((String) info.getKey(),  dataIn);
        }
    }

    //Insert Patient condition
    public void insertPatientCondition() {
        for (Map.Entry info : conditionInfo.entrySet()) {
            System.out.println("Please enter patient's " + (String) info.getKey() + ": ");

            int dataIn = scanCheck.checkIfIntBetween(0,1);

            this.conditionInfo.replace((String) info.getKey(),  dataIn);
        }

        if (conditionInfo.get("Sick") == 1 && !allDiseases.isEmpty()) {
            System.out.println("Insert Patient Disease/s: ");

            this.insertPatientDiseases();
        }

        System.out.println("Write Patients condition description");

        conditionDescription = in.nextLine();
    }

    //Insert Patients Diseases
    public void insertPatientDiseases() {
        boolean insertDiseases = true;

        while (insertDiseases) {
            this.loadAllDiseasesFromDB();

            int choosenDisease = 0;

            System.out.println("№  |  Disease Name");

            for (Map.Entry info : allDiseases.entrySet()) {
                System.out.println((Integer) info.getKey() + "  |  " + (String) info.getValue().toString());
            }

            System.out.println("Choose a disease / Write №:");

            choosenDisease = scanCheck.checkIfInt();

            System.out.println("You have chosen: " + choosenDisease);

            if (patientDiseasesInfo.get(choosenDisease) == null) {
                this.patientDiseasesInfo.put(choosenDisease, allDiseases.get(choosenDisease));
            }

            System.out.println("Do you want to insert another disease? ");

            insertDiseases = scanCheck.converIntToBoolean();
        }

    }

    //Insert Patient weight and height
    public void insertPatientWeightAndHeight() {
        int min = 0;
        int max = 400;

        for (Map.Entry info : bodyInfo.entrySet()) {
            System.out.println("Please enter patient's " + info.getKey() + ": ");

            int dataIn = scanCheck.checkIfIntBetween(min, max);

            this.bodyInfo.replace((String) info.getKey(), dataIn);
        }
    }

    //Insert Patient food intake
    public void insertPatientFoodIntake() {
        int min = 0;
        int max = 100000;

        for (Map.Entry info : foodInfo.entrySet()) {
            System.out.println("Please enter patient's " + info.getKey() + ": ");

            int dataIn = scanCheck.checkIfIntBetween(min, max);

            this.foodInfo.replace((String) info.getKey(), dataIn);
        }
    }

    //Insert Patient lifestyle
    public void insertPatientBadHabits() {
        this.loadAllFromDatabase();

        System.out.println("Insert Patients Bad Habits");

        System.out.println("Please input 1 for True or 0 for False");

        for (int key : this.allBadHabits.keySet()) {
            System.out.println(allDiseases.get(key));

            if (scanCheck.converIntToBoolean()) {
                this.badHabitsInfo.put((Integer) key , (String) this.allBadHabits.get(key));
            }
        }
    }

    //Alert If Patient is in risk of getting a Disease
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // NOT TESTED FULLY _ FUCK  FUCK FUCK FUCK FUCK FUCK FUCK FUCK
    public void alertPatientForDisease() {
        this.loadAllFromDatabase();

        List<String> alertDiseases = new ArrayList<>();

        for (String badHabit : badHabitsInfo.values()) {
            for (Disease connectedDiseases : this.diseasesToBadHabits.keySet()) {
                if (patientDiseasesInfo.containsValue(connectedDiseases) && alertDiseases.contains(connectedDiseases.toString())) {
                    System.out.println("Suck Anus");
                } else {
                    alertDiseases.add(connectedDiseases.toString());
                }
            }
        }

        if (!alertDiseases.isEmpty()) {
            System.out.println("Patient is in danger of: ");

            for (String alertDis : alertDiseases) {
                System.out.println(alertDis);
            }
        }
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    /*
         **********************************************************
            Get Patient Data
         **********************************************************
    */
    //Writes all patient's data
    public void getPatientAllData() {
        this.getPatientBasicData();
        this.getPatientCondition();
        this.getPatientWeightAndHeight();
        this.getPatientFoodIntake();
        this.getPatientBadHabits();
    }

    //Get Patient basic data:
    public void getPatientBasicData() {
        for (Map.Entry info : basicPatientInfo.entrySet()) {
            System.out.println("Patient's " + (String) info.getKey() + ": " + (String)  info.getValue());
        }
    }

    //Get Patient lifestyle
    public void getPatientBadHabits() {
        for (int info : this.badHabitsInfo.keySet()) {
            System.out.println(badHabitsInfo.get(info));
        }
    }

    //Get Patient food intake
    public void getPatientFoodIntake() {
        for (Map.Entry info : foodInfo.entrySet()) {
            System.out.println("Patient " + info.getKey() + ": " +  info.getValue());
        }
    }

    //GetPatient weight and height
    public void getPatientWeightAndHeight() {
        for (Map.Entry info : bodyInfo.entrySet()) {
            System.out.println("Patient " + info.getKey() + ": " +  info.getValue());
        }
    }

    //Get Patient condition
    public void getPatientCondition() {
        for (Map.Entry info : conditionInfo.entrySet()) {
            System.out.println("Patient " + (String) info.getKey() + ": " + (Integer)  info.getValue());
        }

        for (Map.Entry info : patientDiseasesInfo.entrySet()) {
            System.out.println(("Patient is suffering from : " + (String) info.getValue().toString()));
        }

        System.out.println(this.conditionDescription);
    }
}
