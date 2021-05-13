import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class DataBaseInserter {
    private Connection dbConnection;
    private DataBaseHandler loadDB;

    private final ScannerChacker checkInput = new ScannerChacker();
    Scanner in = new Scanner(System.in);

    //Date And Time
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    LocalDateTime timeNow;

    public DataBaseInserter() {
        loadDB = new DataBaseHandler();

        this.dbConnection = loadDB.getDatabase();
    }

    public int getPatientId(int userIn) {
        int patientId = 0;

        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet tablePatients = dbStatement.executeQuery("  select patient_id from patients " +
                    "where user_id = '" + userIn + "'");

            while (tablePatients.next()) {
                patientId = tablePatients.getInt("patient_id");
            }

        } catch(Exception e) {
            System.out.println(e);
        }

        return patientId;
    }

    public int getLastUserID() {
        //select count(program_users.user_id) from program_users
        int lastUserId = 0;

        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet tablePatients = dbStatement.executeQuery("select max(user_id) from program_users");

            while (tablePatients.next()) {
                lastUserId = tablePatients.getInt("max(user_id)");
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return lastUserId;
    }

    public int getLastAdminID() {
        //select count(program_users.user_id) from program_users
        int adminUserId = 0;

        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet tablePatients = dbStatement.executeQuery("select max(admin_id) from admins");

            while (tablePatients.next()) {
                adminUserId = tablePatients.getInt("max(admin_id)");
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return adminUserId;
    }

    public int getLastDoctorID() {
        //select count(program_users.user_id) from program_users
        int doctorId = 0;

        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet tablePatients = dbStatement.executeQuery("select max(doctor_id) from doctors");

            while (tablePatients.next()) {
                doctorId = tablePatients.getInt("max(doctor_id)");
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return doctorId;
    }

    public int getLastPatientID() {
        //select count(program_users.user_id) from program_users
        int patientId = 0;

        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet tablePatients = dbStatement.executeQuery("  select max(patient_id) from patients");

            while (tablePatients.next()) {
                patientId = tablePatients.getInt("max(patient_id)");
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return patientId;
    }

    public void insertUser(String userName, String userPassword, String userEmail, String userPrivilege) {
        try {

            String sqlQuery = "INSERT INTO program_users (user_name, user_privilege, user_password, email) VALUES ('" + userName + "' , '" +  userPrivilege  + "' , '" + userPassword  + "', '" + userEmail + "' )";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertAdmin(String userName, String userPassword, String userEmail, String firstName, String lastName) {
        try {
            this.insertUser(userName, userPassword, userEmail, "a");

            int userId = this.getLastUserID();

            String sqlQuery = "INSERT INTO admins ( user_id, first_name, last_name) VALUES (" + userId + ", '" + firstName + "' , '" + lastName +"')";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertDoctor(String userName, String userPassword, String userEmail, String firstName, String lastName, String specialty, int egn, String gender) {
        try {
            this.insertUser(userName, userPassword, userEmail, "d");

            int userId = this.getLastUserID();

            String sqlQuery = "INSERT INTO doctors (user_id, first_name,last_name,specialty, egn, gender) VALUES (" + userId  +  ", '" + firstName  +  "', '" + lastName  +  "', '" + specialty  +  "', " + egn  +  ", '" + gender  +  "' )";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertPatient(String userName, String userPassword, String userEmail, String firstName, String lastName, String gender, int egn, int weight, int height  ) {
        try {
            this.insertUser(userName, userPassword, userEmail, "p");

            int userId = this.getLastUserID();

            String sqlQuery = "INSERT INTO PATIENTS (user_id, first_name, last_name, egn, gender, weight, height) VALUES (" + userId  +  ", '" + firstName  +  "', '" + lastName +  "', " + egn  +  ", '" + gender  +  "', " + weight + "," + height + " )";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertPatientAppointment(int patientId, int doctorIn) {
        this.getDateAndTime();

        try {
            String sqlQuery = "INSERT INTO PATIENTS_APPOINTMENTS (patient_id, doctor_id, DATE_OF_APPOINTMENTS) VALUES (" + patientId  +  ", " + doctorIn  + " , (TO_DATE('" +  dtf.format(this.timeNow)  +  "', 'yyyy/mm/dd')) )";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertPatientToDoctor(int patientId, int doctorIn) {
        this.getDateAndTime();

        try {
            String sqlQuery = "INSERT INTO PATIENTS_TO_DOCTORS (patient_id, doctor_id, date_first_appointment) VALUES (" + patientId  +  ", " + doctorIn  + " , (TO_DATE('" +  dtf.format(this.timeNow)  +  "', 'yyyy/mm/dd')) )";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertPatientCondition(int patientId, int sick, int highBloodPressure, String description) {
        try {
            String sqlQuery = "INSERT INTO PATIENTS_CONDITION (patient_id, sick, high_blood_pressure, description) VALUES (" + patientId  +  ", " + sick  + " , " + highBloodPressure + ",' " + description + "' )";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertPatientToDisease(int patientId,  LinkedHashMap<Integer, Disease> diseases) {
        this.getDateAndTime();

        for (Map.Entry<Integer, Disease> disease : diseases.entrySet()) {
            try {
                String sqlQuery = "INSERT INTO PATIENTS_TO_DISEASES (patient_id, disease_id, date_of_discovery) VALUES (" + patientId  +  ", " + (Integer) disease.getKey()  + " , (TO_DATE('" +  dtf.format(this.timeNow)  +  "', 'yyyy/mm/dd')) )";

                loadDB.executeQuery(sqlQuery);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    public void insertPatientEatingHabits(int patientId, int AverageCalories, int AverageProteins, int AverageCarbs, int AverageFats) {
        try {
            String sqlQuery = "INSERT INTO PATIENTS_EATING_HABITS (patient_id, avg_calories, avg_carbs, avg_fats, avg_proteins) VALUES (" + patientId  +  ", " + AverageCalories  + " , " + AverageProteins + ", " + AverageCarbs + ", " + AverageFats + " )";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertPatientToBadHabits(int patientId, LinkedHashMap<Integer, String> badHabits) {
        for (Map.Entry badHabit : badHabits.entrySet()) {
            try {
                String sqlQuery = "INSERT INTO PATIENTS_TO_BAD_HABITS (patient_id, habit_id) VALUES (" + patientId  + ", " + badHabit.getKey() + " )";

                loadDB.executeQuery(sqlQuery);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    public void insertDiseasesToBadHabits(LinkedHashMap<Integer, Disease> diseases, LinkedHashMap<Integer, String> badHabits) {
        for (Map.Entry<Integer, Disease> disease : diseases.entrySet()) {
            for (Map.Entry badHabit : badHabits.entrySet()) {
                try {
                    String sqlQuery = "INSERT INTO DISEASES_TO_BAD_HABITS ( disease_id, habit_id) VALUES (" + (Integer) disease.getKey() + ", " + (Integer) badHabit.getKey() + ")";

                    loadDB.executeQuery(sqlQuery);
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void insertPatientWeaklyReport(int userId, String reportText) {
        this.getDateAndTime();

        int patientId = this.getPatientId(userId);

        try {
            String sqlQuery = "INSERT INTO PATIENTS_WEEKLY_REPORTS (patient_id, DATE_OF_REPORT, REPORT_TEXT) VALUES (" + patientId  + " , (TO_DATE('" +  dtf.format(this.timeNow)  +  "', 'yyyy/mm/dd')) , '" + reportText + "' )";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertDisese(String diseasesNameIn) {
        try {
            String sqlQuery = "INSERT INTO diseases (disease_name) VALUES (' " + diseasesNameIn + " ')";

            Statement statement = dbConnection.createStatement();

            statement.executeUpdate(sqlQuery);

            dbConnection.commit();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void insertBadHabit(String badHabitIn) {
        try {
            String sqlQuery = "INSERT INTO bad_habits (habit_name) VALUES (' " + badHabitIn + " ')";

            loadDB.executeQuery(sqlQuery);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void getDateAndTime() {
            this.timeNow = LocalDateTime.now();
    }
}
