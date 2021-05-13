import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class DataBaseLoader {
    private Connection dbConnection;
    private DataBaseHandler loadDB;

    private LinkedHashMap<Integer, Disease> allDiseases = new LinkedHashMap<Integer, Disease>();
    private LinkedHashMap<Integer, String> allBadHabits = new LinkedHashMap<Integer, String>();


    public DataBaseLoader() {
        loadDB = new DataBaseHandler();
        this.dbConnection = loadDB.getDatabase();
    }

    public boolean checkUsernameAndPassword(String username, String password) {
        return loadDB.checkUsernameAndPassword("select * from program_users where (user_name = '" + username + "' and user_password = '" + password + "') or (user_password = '" + password + "' and  email = '" + username + "')");
    }

    public String getUserPrivilege(String username) {
        return loadDB.getUserPrivilege("select user_privilege from program_users where user_name = '" + username + "'  or  email = '" + username + "'");
    }

    public int getUserId(String username) {
        return loadDB.getUserId("select user_id from program_users where user_name = '" + username + "'  or  email = '" + username + "'");
    }

    public int getDoctorId(String username) {
        int userId = this.getUserId(username);

        int doctorId = loadDB.getDoctorId("select doctor_id from doctors where user_id = '" + userId+ "'");

        if (doctorId == 0) {
            return userId;
        } else {
            return doctorId;
        }
    }

    public LinkedHashMap<String, Integer> getDiseaseWithBadHabit(int disease_id) {
        LinkedHashMap<String, Integer> badHabitsCount = new LinkedHashMap<String, Integer>();

        try {
            String statementIn = "select bad_habits.habit_name, count(diseases_to_bad_habits.habit_id)"
                    + " from diseases_to_bad_habits"
                    + " join diseases  on diseases.disease_id = diseases_to_bad_habits.disease_id "
                    + " join bad_habits on bad_habits.habit_id = diseases_to_bad_habits.habit_id "
                    + " where diseases_to_bad_habits.disease_id = " + disease_id
                    + " group by disease_name, habit_name";

            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery(statementIn);

            while (dbQuery.next()) {
                String habitName = dbQuery.getString("HABIT_NAME");
                int habitCount = dbQuery.getInt("count(diseases_to_bad_habits.habit_id)");

                badHabitsCount.put(habitName, habitCount);
            }
        } catch(Exception e) {
            System.out.println(e);
            return badHabitsCount;
        }

        return badHabitsCount;
    }

    public LinkedHashMap<String, String> getPatientAndDoctorEmail(int pat_user_id) {
        LinkedHashMap<String, String> patEmaildocEmail = new LinkedHashMap<String, String>();

        try {
            String statementIn = "select distinct UD.email as doctor_email,  UP.email as patient_email from PATIENTS_TO_DOCTORS "
                    + " join doctors d on d.doctor_id = PATIENTS_TO_DOCTORS.doctor_id "
                    + " join program_users UD on UD.USER_ID = d.user_id "
                    + " join patients P on P.PATIENT_ID = PATIENTS_TO_DOCTORS.PATIENT_ID "
                    + " join program_users UP on UP.user_id = P.user_id "
                    + " where PATIENTS_TO_DOCTORS.patient_id = (select patient_id from patients where user_id = " + pat_user_id + ")";

            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery(statementIn);

            while (dbQuery.next()) {
                String doctorEmail = dbQuery.getString("doctor_email");
                String patientEmail = dbQuery.getString("patient_email");

                patEmaildocEmail.put(doctorEmail, patientEmail);
            }
        } catch(Exception e) {
            System.out.println(e);
            return patEmaildocEmail;
        }

        return patEmaildocEmail;
    }

    public List<String> getPatientsForDoctor(int doctor_id) {
        List<String> patients = new ArrayList<String>();

        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet tablePatients = dbStatement.executeQuery("select patients.first_name, patients.last_name"
                    + " from patients_to_doctors"
                    + " join PATIENTS on patients_to_doctors.PATIENT_ID = PATIENTS.PATIENT_ID"
                    + " where patients_to_doctors.doctor_id =" + doctor_id);

            while (tablePatients.next()) {
                String firstName = tablePatients.getString("first_name");
                String lastName = tablePatients.getString("last_name");

                String fullName = firstName + "  " + lastName ;

                patients.add(fullName);
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return patients;
    }

    public void loadDiseases() {
        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery("select * from diseases");

            ResultSetMetaData rsmd = dbQuery.getMetaData();

            int columnsNumber = rsmd.getColumnCount();

            while (dbQuery.next()) {
                int diseaseId = dbQuery.getInt("disease_id");

                String diseaseName = dbQuery.getString("disease_name");

                Disease diseaseObj = new Disease(diseaseName);

                allDiseases.put(diseaseId, diseaseObj);
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void loadBadHabits() {
        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery("select * from bad_habits");

            ResultSetMetaData rsmd = dbQuery.getMetaData();

            while (dbQuery.next()) {
                int habitId = dbQuery.getInt("habit_id");

                String habitName = dbQuery.getString("habit_name");

                allBadHabits.put(habitId, habitName);
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public LinkedHashMap getDiseasesToBadHabits() {
        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery("select disease_name , habit_name from diseases_to_bad_habits join DISEASES on diseases.disease_id = diseases_to_bad_habits.disease_id join BAD_HABITS on BAD_HABITS.HABIT_ID = diseases_to_bad_habits.HABIT_ID");

            ResultSetMetaData rsmd = dbQuery.getMetaData();

            LinkedHashMap<Disease, String> diseasesToBadHabits = new LinkedHashMap<Disease, String>();

            while (dbQuery.next()) {
                String diseaseName= dbQuery.getString("disease_name");

                Disease diseaseObj = new Disease(diseaseName);

                String habitName = dbQuery.getString("habit_name");

                //System.out.println(diseaseObj.getName() + " / " + habitName);

                diseasesToBadHabits.put(diseaseObj, habitName);
            }

            return diseasesToBadHabits;
        } catch(Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public LinkedHashMap getAllDiseases() {
        this.loadDiseases();

        return this.allDiseases;
    }

    public LinkedHashMap getAllBadHabits() {
        this.loadBadHabits();

        return this.allBadHabits;
    }

    /*
        Functions For Testing
     */

    public void printUsers() {
        try {
            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery("select * from program_users");

            ResultSetMetaData rsmd = dbQuery.getMetaData();

            int columnsNumber = rsmd.getColumnCount();

            while (dbQuery.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");

                    String columnValue = dbQuery.getString(i);

                    System.out.print(columnValue);
                }

                System.out.println("");
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void printDiseases() {
        this.loadDiseases();

        for (int key : allDiseases.keySet()) {
            System.out.println(allDiseases.get(key).getName());
        }
    }

    public void printBadHabits() {
        this.loadBadHabits();

        for (int key : allBadHabits.keySet()) {
            System.out.println(allBadHabits.get(key));
        }
    }
}
