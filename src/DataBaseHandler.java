import java.sql.*;
import java.util.*;

public class DataBaseHandler {
    //Database connection
    private Connection dbConnection;

    //Indicator of connection ot Database
    private boolean dbConnected = false;

    //Database login info
    private final String dbDriver = "oracle.jdbc.driver.OracleDriver";
    private final String dbURL = "jdbc:oracle:thin:@localhost:1521:xe";
    private final String dbUser = "presiyan";
    private final String dbPassword = "1";

    //All table names and indicator for existence
    private final LinkedHashMap<String, Boolean> tables = new LinkedHashMap<String, Boolean>()
    {{
        put ("admins", false);
        put("bad_habits", false);
        put("diseases", false);
        put("diseases_to_bad_habits", false);
        put("doctors", false);
        put("patients", false);
        put("patients_appointments", false);
        put("patients_condition", false);
        put("patients_eating_habits" ,false);
        put("patients_to_bad_habits" ,false);
        put("patients_to_diseases",false);
        put("patients_to_doctors",false);
        put("patients_weekly_reports",false);
        put( "program_users",false);

        //Table for testing program functions
        put( "test",false);
    }};

    //All tables and create methods
    private final LinkedHashMap<String, String> tablesCreator = new LinkedHashMap<String, String>()
    {{
        put ("admins", " create table admins ( admin_id number(7) not null primary key, user_id number(7) NOT NULL, first_name varchar2(25), last_name varchar2(25), CONSTRAINT user_admins_id FOREIGN KEY (user_id) REFERENCES program_users(user_id)) ");

        put("bad_habits", "create table bad_habits  ( habit_id number(7) not null primary key, habit_name varchar2(40))");

        put("diseases", "create table diseases  ( disease_id number(7) not null primary key, disease_name varchar2(40)) ");

        put("diseases_to_bad_habits", "create table diseases_to_bad_habits ( disease_id number(7) not null, habit_id number(7) not null, CONSTRAINT dtbh_hab_id FOREIGN KEY (habit_id) REFERENCES bad_habits(habit_id), CONSTRAINT dtbh_dis_id FOREIGN KEY (disease_id) REFERENCES diseases(disease_id))");

        put("patients_weekly_reports", "create table patients_weekly_reports ( patient_id number(7) not null, date_of_report date, report_text varchar2(300), CONSTRAINT report_pat_id FOREIGN KEY (patient_id) REFERENCES patients(patient_id))");

        put("patients", "create table patients ( patient_id number(7) not null primary key, user_id number(7) NOT NULL, first_name varchar2(25),  last_name varchar2(25), egn number(8), gender char(1), weight number(3), height number(3), CONSTRAINT user_patient_id FOREIGN KEY (user_id) REFERENCES program_users(user_id))");

        put("patients_appointments", "create table patients_appointments ( doctor_id number(7) not null, patient_id number(7) not null, date_of_appointments date, CONSTRAINT pa_pat_id FOREIGN KEY (patient_id) REFERENCES patients(patient_id), CONSTRAINT pa_doc_id FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)) ");

        put("patients_condition", "create table patients_condition ( patient_id NUMBER(7) NOT NULL, sick char(1), high_blood_pressure char(1), description VARCHAR2(255), CONSTRAINT pat_id FOREIGN KEY (patient_id) REFERENCES patients(patient_id))");

        put("patients_eating_habits" , "create table patients_eating_habits ( patient_id NUMBER(7) NOT NULL, avg_calories number, avg_carbs number, avg_fats number, avg_proteins number, CONSTRAINT pat_id_eating FOREIGN KEY (patient_id) REFERENCES patients(patient_id))");

        put("patients_to_bad_habits" , "create table patients_to_bad_habits ( patient_id number(7) not null, habit_id number(7) not null, CONSTRAINT ptbh_pat_id FOREIGN KEY (patient_id) REFERENCES patients(patient_id), CONSTRAINT ptbh_hab_id FOREIGN KEY (habit_id) REFERENCES bad_habits(habit_id)) ");

        put("patients_to_doctors", " create table patients_to_doctors ( doctor_id number(7) not null, patient_id number(7) not null, date_first_appointment date, CONSTRAINT ptd_pat_id FOREIGN KEY (patient_id) REFERENCES patients(patient_id), CONSTRAINT ptd_doc_id FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id))");

        put("patients_to_diseases", "create table patients_to_diseases ( patient_id number(7) not null, disease_id number(7) not null, date_of_discovery date, CONSTRAINT ptdis_pat_id FOREIGN KEY (patient_id) REFERENCES patients(patient_id), CONSTRAINT ptdis_hab_id FOREIGN KEY (disease_id) REFERENCES diseases(disease_id))");

        put("doctors", "create table doctors ( doctor_id number(7) not null primary key, user_id number(7) NOT NULL, first_name varchar2(25), last_name varchar2(25), specialty varchar2(30), egn number(6), gender char(1), CONSTRAINT user_doctors_id FOREIGN KEY (user_id) REFERENCES program_users(user_id))");

        put( "program_users", "create table program_users ( user_id number(7) not null primary key, user_name varchar2(30) NOT NULL, user_privilege char(1) NOT NULL, user_password VARCHAR2(40)  NOT NULL, email varchar2(254) NOT NULL) ");


        //Table for testing program functions
        put( "test", "create table test  ( test_id number(7) not null primary key, test_name varchar2(40))");
    }};

    //Loading the database
    public void loadDataBase() {
        try {
            //load the driver class
            Class.forName(this.dbDriver);

            //set the connection object
           this.dbConnection = DriverManager.getConnection(this.dbURL, this.dbUser,this.dbPassword);

           //Set auto commit as false.
            dbConnection.setAutoCommit(false);

           //Set boolean -> database is loaded
            this.dbConnected = true;

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    //Closing the database
    public void closeDatabase() {
        if (this.dbConnected) { // Check if database is loaded
            try {
                this.dbConnection.close();

                System.out.println("Closing Database...");
            } catch(Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Database is not loaded");
        }
    }

    //Loop trough table rows nad columns
    public void loopTroughTable(String tableName) {
        try {
            if (!this.dbConnected) { // Check if database is NOT loaded
                this.loadDataBase();
            }

            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery("select * from " + tableName);

            ResultSetMetaData rsmd = dbQuery.getMetaData();

            int columnsNumber = rsmd.getColumnCount();

            //Loop trough all columns
            while (dbQuery.next()) {
                //Print one row
                for(int i = 1 ; i <= columnsNumber; i++){
                    System.out.print(dbQuery.getString(i) + " "); //Print one element of a row
                }

                System.out.println();//Move to the next line to print the next row.
            }

            this.closeDatabase();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    //Execute custom query
    public void executeQuery(String statementIn) {
        try {
            if (!this.dbConnected) { // Check if database is NOT loaded
                this.loadDataBase();
            }

            Statement statement = dbConnection.createStatement();

            statement.executeUpdate(statementIn);

            dbConnection.commit();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public boolean checkUsernameAndPassword (String statementIn) {
        try {
            if (!this.dbConnected) { // Check if database is NOT loaded
                this.loadDataBase();
            }

            String username = null;

            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery(statementIn);

            while (dbQuery.next()) {
                username = dbQuery.getString("user_name");
            }

            if(username != null) {
                return true;
            }

        } catch(Exception e) {
            System.out.println(e);
            return false;
        }

        return false;
    }

    public String getUserPrivilege (String statementIn) {
        try {
            if (!this.dbConnected) { // Check if database is NOT loaded
                this.loadDataBase();
            }

            String userPrivilege = null;

            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery(statementIn);

            while (dbQuery.next()) {
                userPrivilege = dbQuery.getString("user_privilege");
            }

            return userPrivilege;

        } catch(Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public int getUserId (String statementIn) {
        try {
            if (!this.dbConnected) { // Check if database is NOT loaded
                this.loadDataBase();
            }

            int userPrivilege = 0;

            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery(statementIn);

            while (dbQuery.next()) {
                userPrivilege = dbQuery.getInt("user_id");
            }

            return userPrivilege;

        } catch(Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    public int getDoctorId (String statementIn) {
        try {
            if (!this.dbConnected) { // Check if database is NOT loaded
                this.loadDataBase();
            }

            int userPrivilege = 0;

            Statement dbStatement = dbConnection.createStatement();

            ResultSet dbQuery = dbStatement.executeQuery(statementIn);

            while (dbQuery.next()) {
                userPrivilege = dbQuery.getInt("doctor_id");
            }

            return userPrivilege;

        } catch(Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    //Check if a table exist
    public boolean checkIfTableExist(String tableNameIn) {
        if (!dbConnected) {
            this.loadDataBase();
        }

        try {
            DatabaseMetaData dbm = dbConnection.getMetaData();

            ResultSet tables = dbm.getTables(null, null, tableNameIn.toUpperCase(Locale.ROOT), null);

            if (tables.next()) {
                //System.out.println("Table " + tableNameIn.toUpperCase(Locale.ROOT) + " exists");
                return true;
            }
            else {
                //System.out.println("Table " + tableNameIn.toUpperCase(Locale.ROOT) + " doesn't exists");
                return false;
            }

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    //Check if ALL tables exist
    public void checkIfAllTablesExists() {
        if (!dbConnected) {
            this.loadDataBase();
        }

        for (Map.Entry table : tables.entrySet()) {
            boolean tableExist = this.checkIfTableExist((String) table.getKey());

            if (!tableExist) {
                this.createTable((String) table.getKey());

                tableExist = this.checkIfTableExist((String) table.getKey());

                if (tableExist) {
                   //System.out.println("Table " + table.getKey() + " was created");
                }
            }

            this.tables.replace((String) table.getKey(),  tableExist);

            //System.out.println("Table " +  table.getKey() + ": exist ? : " +   table.getValue());
        }
    }

    //Create table
    public void createTable(String tableName) {
        if (!checkIfTableExist(tableName)) {
            String tableCreateScript = tablesCreator.get(tableName);

            this.executeQuery(tableCreateScript);
        } else {
            System.out.println("Table already exists");
        }
    }

    /*
        Send Database connections to other classes
     */

    public Connection getDatabase() {
        if (!this.dbConnected) {
            this.loadDataBase();
        }

        this.checkIfAllTablesExists();

        return this.dbConnection;
    }
}
