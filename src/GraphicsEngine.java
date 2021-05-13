import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class GraphicsEngine implements ActionListener {
    /*
     **********************************************************
          Connections
     **********************************************************
    */
    private final GraphicsDefaultValues defaultValues = new GraphicsDefaultValues();
    private final GraphicsDefaultComponents defaultComponents = new GraphicsDefaultComponents();
    public GraphicsMainFrame mainFrame;
    private DataBaseLoader dbLoader = new DataBaseLoader();
    private DataBaseInserter dbInserter = new DataBaseInserter();

    /*
     **********************************************************
          User Info
     **********************************************************
    */
    private String userName;
    private String userPrivilege;
    private int userId;

    /*
     **********************************************************
          All Diseases
     **********************************************************
    */
    private LinkedHashMap<Integer, Disease> allDiseases;

    /*
     **********************************************************
          All Bad Habits
     **********************************************************
    */
    private LinkedHashMap<Integer, String> allBadHabits;

    // User Privilege Levels
    final String adminPrivilege = "a";
    final String doctorPrivilege = "d";
    final String patientPrivilege = "p";

    /*
     **********************************************************
          Default Back Button - back to Main Menu
     **********************************************************
    */
    private  final JButton buttonBackToMainMenu = defaultComponents.getButton("to Main Menu");

    private  final int centerOfScreen = (defaultValues.getPanelWidth() / 2 ) - (defaultValues.getButtonWigth() / 2);
    private final int leftColumnOfScreen = centerOfScreen - (centerOfScreen / 2);
    private final int rightColumnOfScreen = centerOfScreen + (centerOfScreen / 2);


    /*
     **********************************************************
          Constructor
     **********************************************************
    */
    GraphicsEngine() {
        this.mainFrame = new GraphicsMainFrame();

        this.buttonBackToMainMenu.addActionListener(this);

        this.mainFrame.setCurrentPanel(this.drawLoginPanel());

        this.allDiseases = this.dbLoader.getAllDiseases();

        this.allBadHabits = this.dbLoader.getAllBadHabits();
    }

    /*
     **********************************************************
          Login Panel
     **********************************************************
    */

    public JPanel drawLoginPanel() {
        final JButton buttonLogIn = defaultComponents.getButton("Log in");
        final JTextField userNameTextField = defaultComponents.getTextfield();
        final JTextField userPasswordTextField = defaultComponents.getTextfield();
        final JLabel userNameLabel = defaultComponents.getLabel("Username");
        final JLabel passwordLabel = defaultComponents.getLabel("Password");
        final JLabel logoLabel = defaultComponents.getLabelWithIcon("welcome_bg.jpg");

        JPanel loginPanel = new JPanel();
        loginPanel.setSize(new Dimension(defaultValues.getPanelWidth(), defaultValues.getPanelHeight()));
        loginPanel.setBackground(defaultValues.getPanelBackgroundColor());
        loginPanel.setLayout(null);

        int centerOfScreen__local = defaultValues.getPanelWidth() / 2;
        int componentHorizontalPlace = centerOfScreen__local / 4;
        int componentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        loginPanel.add(userNameLabel);
        userNameLabel.setLocation(componentHorizontalPlace,componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        loginPanel.add(userNameTextField);
        userNameTextField.setLocation( componentHorizontalPlace,componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        loginPanel.add(passwordLabel);
        passwordLabel.setLocation(componentHorizontalPlace,componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        loginPanel.add(userPasswordTextField);
        userPasswordTextField.setLocation(componentHorizontalPlace, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;
        componentVerticalPlace += componentSpaceBetween;

        loginPanel.add(buttonLogIn);
        buttonLogIn.setLocation(componentHorizontalPlace, componentVerticalPlace);

        loginPanel.add(logoLabel);
        logoLabel.setSize(new Dimension(defaultValues.getPanelWidth() / 2, defaultValues.getPanelHeight()) );
        logoLabel.setLocation(centerOfScreen__local, 0);

        buttonLogIn.addActionListener(e -> {
            if (e.getSource() == buttonLogIn) {
                String username = userNameTextField.getText();
                String password = userPasswordTextField.getText();

                if (dbLoader.checkUsernameAndPassword(username, password)) {
                    this.userId = dbLoader.getUserId(username);
                    this.userPrivilege = dbLoader.getUserPrivilege(username);
                    this.userName = username;
                    this.mainFrame.setCurrentPanel(this.drawOptionsPanel());
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong credentials!");
                }
            }
        });


        loginPanel.setVisible(true);

        return loginPanel;
    }

    /*
     **********************************************************
          Options Panel
     **********************************************************
    */

    //Option Name
    String addNewAdmin = "Add new Admin";
    String addNewDoctor = "Add new Doctor";
    String addNewPatient = "Add new Patient";
    String addNewDisease = "Add new Disease";
    String addNewBadHabit = "Add new Bad Habit";
    String seeAllDiseases = "See all Diseases";
    String seeAllPatients = "See all Patients";
    String writeWeaklyReport = "Write weakly report";
    String makeAppointment = "Make appointment";
    String exitProgram = "Exit Program";

    // ADMIN Program Options
    private final LinkedHashMap<String, JButton> adminOptions = new LinkedHashMap<String, JButton>() {{
        put( addNewAdmin, defaultComponents.getButton( addNewAdmin));
        put( addNewDoctor, defaultComponents.getButton( addNewDoctor));
        put( addNewPatient, defaultComponents.getButton( addNewPatient));
        put( addNewDisease, defaultComponents.getButton( addNewDisease));
        put( addNewBadHabit, defaultComponents.getButton( addNewBadHabit));
        put( seeAllDiseases,defaultComponents.getButton( seeAllDiseases));
        put( seeAllPatients,defaultComponents.getButton( seeAllPatients));
        put( writeWeaklyReport, defaultComponents.getButton( writeWeaklyReport));
        put( makeAppointment, defaultComponents.getButton( makeAppointment));
        put(exitProgram, defaultComponents.getButton( exitProgram));
    }};
    // DOCTOR Program Options
    private final LinkedHashMap<String, JButton> doctorOptions = new LinkedHashMap<String, JButton>() {{
        put( addNewPatient, defaultComponents.getButton( addNewPatient));
        put( seeAllDiseases,defaultComponents.getButton( seeAllDiseases));
        put( seeAllPatients,defaultComponents.getButton( seeAllPatients));
        put(exitProgram, defaultComponents.getButton( exitProgram));
    }};
    // PATIENT Program Options
    private final LinkedHashMap<String, JButton> patientOptions = new LinkedHashMap<String, JButton>() {{
        put( seeAllDiseases,defaultComponents.getButton( seeAllDiseases));
        put( writeWeaklyReport, defaultComponents.getButton( writeWeaklyReport));
        put( makeAppointment, defaultComponents.getButton( makeAppointment));
        put(exitProgram, defaultComponents.getButton( exitProgram));
    }};

    // CURRENT Program Options
    private LinkedHashMap<String, JButton> currentOptions;

    public JPanel drawOptionsPanel() {
        JPanel optionsPanel = defaultComponents.getBasePanel();

        int componentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        switch (this.userPrivilege) {
            case adminPrivilege :
                this.currentOptions = this.adminOptions;

                for (String optionButton : this.adminOptions.keySet()) {
                    JButton button = adminOptions.get(optionButton);
                    optionsPanel.add(button);
                    button.setLocation(this.centerOfScreen, componentVerticalPlace);
                    button.addActionListener(this);
                    componentVerticalPlace += componentSpaceBetween;
                }

                break;
            case doctorPrivilege:
                this.currentOptions = this.doctorOptions;

                for (String optionButton : this.doctorOptions.keySet()) {
                    JButton button = doctorOptions.get(optionButton);
                    optionsPanel.add(button);
                    button.setLocation(this.centerOfScreen, componentVerticalPlace);
                    button.addActionListener(this);
                    componentVerticalPlace += componentSpaceBetween;
                }

                break;
            case patientPrivilege:
                this.currentOptions = this.patientOptions;

                for (String optionButton : this.patientOptions.keySet()) {
                    JButton button = patientOptions.get(optionButton);
                    optionsPanel.add(button);
                    button.setLocation(this.centerOfScreen, componentVerticalPlace);
                    button.addActionListener(this);
                    componentVerticalPlace += componentSpaceBetween;
                }

                break;
            default:
                JOptionPane.showMessageDialog(null, "User has no options " + this.userName);
                break;
        }

        return optionsPanel;
    }

    /*
     ********************************************************************************************************************
     *
     *       Draw Options
     *
     ********************************************************************************************************************
     */

    /*
     **********************************************************
          Add new User
     **********************************************************
    */

    private String add_userName, add_userPassword, add_userEmail, add_userPrivilege;

    public JPanel drawAddNewUser(String newUserPrivilege) {
        final JLabel userNameLebel = defaultComponents.getLabel("Username");
        final JLabel emailLabel = defaultComponents.getLabel("Email");
        final JLabel passwordLebel = defaultComponents.getLabel("Password");
        final JTextField userNameTextField = defaultComponents.getTextfield();
        final JTextField emailTextField = defaultComponents.getTextfield();
        final JTextField passwordTextField = defaultComponents.getTextfield();
        final JButton addUserButton = defaultComponents.getButton("Add user");

        JPanel addNewUser = defaultComponents.getBasePanel();

        int componentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        addNewUser.add(userNameLebel);
        userNameLebel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addNewUser.add(userNameTextField);
        userNameTextField.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addNewUser.add(emailLabel);
        emailLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addNewUser.add(emailTextField);
        emailTextField.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addNewUser.add(passwordLebel);
        passwordLebel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addNewUser.add(passwordTextField);
        passwordTextField.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;
        componentVerticalPlace += componentSpaceBetween;

        addNewUser.add(addUserButton);
        addUserButton.setLocation(centerOfScreen - (defaultValues.getButtonWigth() / 2), componentVerticalPlace);

        addNewUser.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(centerOfScreen + (defaultValues.getButtonWigth() / 2),componentVerticalPlace);

        AtomicBoolean userAdded = new AtomicBoolean(false);

        addUserButton.addActionListener(e -> {
            userAdded.set(true);

            if (userAdded.get()) {
                this.add_userPrivilege = newUserPrivilege;
                this.add_userName = userNameTextField.getText();
                this.add_userEmail = emailTextField.getText();
                this.add_userPassword = passwordTextField.getText();

                if (newUserPrivilege.equals(this.adminPrivilege)) {
                    this.mainFrame.setCurrentPanel(this.drawAddNewAdmin());
                } else if (newUserPrivilege.equals(this.doctorPrivilege)) {
                    this.mainFrame.setCurrentPanel(this.drawAddNewDoctor());
                } else if (newUserPrivilege.equals(this.patientPrivilege)) {
                    this.mainFrame.setCurrentPanel(this.drawAddNewPatient());
                } else {
                    JOptionPane.showMessageDialog(null, "User cannot be added");
                }
            }
        });

        return addNewUser;
    }

    /*
     **********************************************************
          Add new Admin
     **********************************************************
    */

    public JPanel drawAddNewAdmin() {
        JPanel addNewAdmin = defaultComponents.getBasePanel();

        JLabel adminFirstNameLabel = defaultComponents.getLabel("First Name");
        JTextField adminFirstNameTextField = defaultComponents.getTextfield();

        JLabel adminLastNameLabel = defaultComponents.getLabel("Last Name");
        JTextField adminLastNameTextField = defaultComponents.getTextfield();

        JButton addAdminButton = defaultComponents.getButton("Add Admin");

        int componentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        addNewAdmin.add(adminFirstNameLabel);
        adminFirstNameLabel.setLocation(this.centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addNewAdmin.add(adminFirstNameTextField);
        adminFirstNameTextField.setLocation(this.centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addNewAdmin.add(adminLastNameLabel);
        adminLastNameLabel.setLocation(this.centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addNewAdmin.add(adminLastNameTextField);
        adminLastNameTextField.setLocation(this.centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;
        componentVerticalPlace += componentSpaceBetween;

        addNewAdmin.add(addAdminButton);
        addAdminButton.setLocation(this.centerOfScreen - (defaultValues.getButtonWigth() / 2), componentVerticalPlace);

        addNewAdmin.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(this.centerOfScreen + (defaultValues.getButtonWigth() / 2),componentVerticalPlace);

        addAdminButton.addActionListener(e -> {
            String add_firstName = adminFirstNameTextField.getText();
            String add_lastName = adminLastNameTextField.getText();

            this.dbInserter.insertAdmin(this.add_userName, this.add_userPassword, this.add_userEmail, add_firstName, add_lastName);

            this.mainFrame.setCurrentPanel(drawOptionsPanel());
        });

        return addNewAdmin;
    }

     /*
     **********************************************************
          Add new Doctor
     **********************************************************
     */
     public JPanel drawAddNewDoctor() {
         JPanel addNewDoctor = defaultComponents.getBasePanel();

         JLabel doctorFirstNameLabel = defaultComponents.getLabel("First Name");
         JTextField doctorFirstNameTextField = defaultComponents.getTextfield();

         JLabel doctorLastNameLabel = defaultComponents.getLabel("Last Name");
         JTextField doctorLastNameTestField = defaultComponents.getTextfield();

         JLabel doctorSpecialtyLabel = defaultComponents.getLabel("Specialty");
         JTextField doctorSpecialtyTextField = defaultComponents.getTextfield();

         JLabel doctorEGNLabel = defaultComponents.getLabel("EGN");
         JTextField doctorEGNTextField = defaultComponents.getTextfield();

         JLabel doctorGenderLabel = defaultComponents.getLabel("Gender");
         JTextField doctorGenderTextField = defaultComponents.getTextfield();

         JButton addDoctorButton = defaultComponents.getButton("Add Doctor");

         int leftComponentVerticalPlace = 150;
         int rightComponentVerticalPlace = 150;
         int componentSpaceBetween = 50;

         // LEFT COLUMN

         addNewDoctor.add(doctorFirstNameLabel);
         doctorFirstNameLabel.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
         leftComponentVerticalPlace += componentSpaceBetween;

         addNewDoctor.add(doctorFirstNameTextField);
         doctorFirstNameTextField.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
         leftComponentVerticalPlace += componentSpaceBetween;

         addNewDoctor.add(doctorLastNameLabel);
         doctorLastNameLabel.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
         leftComponentVerticalPlace += componentSpaceBetween;

         addNewDoctor.add(doctorLastNameTestField);
         doctorLastNameTestField.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
         leftComponentVerticalPlace += componentSpaceBetween;

         addNewDoctor.add(doctorSpecialtyLabel);
         doctorSpecialtyLabel.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
         leftComponentVerticalPlace += componentSpaceBetween;

         addNewDoctor.add(doctorSpecialtyTextField);
         doctorSpecialtyTextField.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);

         // RIGHT COLUMN

         addNewDoctor.add(doctorEGNLabel);
         doctorEGNLabel.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
         rightComponentVerticalPlace += componentSpaceBetween;

         addNewDoctor.add(doctorEGNTextField);
         doctorEGNTextField.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
         rightComponentVerticalPlace += componentSpaceBetween;

         addNewDoctor.add(doctorGenderLabel);
         doctorGenderLabel.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
         rightComponentVerticalPlace += componentSpaceBetween;

         addNewDoctor.add(doctorGenderTextField);
         doctorGenderTextField.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
         rightComponentVerticalPlace += (componentSpaceBetween * 4);

         addNewDoctor.add(addDoctorButton);
         addDoctorButton.setLocation(this.leftColumnOfScreen, rightComponentVerticalPlace );

         addNewDoctor.add(buttonBackToMainMenu);
         buttonBackToMainMenu.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);

         addDoctorButton.addActionListener(e -> {
             String add_firstName = doctorFirstNameTextField.getText();
             String add_lastName = doctorLastNameTestField.getText();
             String add_specialty = doctorSpecialtyTextField.getText();
             int add_egn = Integer.parseInt(doctorEGNTextField.getText());
             String add_gender = doctorGenderTextField.getText();

            this.dbInserter.insertDoctor(this.add_userName, this.add_userPassword, this.add_userEmail, add_firstName, add_lastName, add_specialty, add_egn, add_gender);

            this.mainFrame.setCurrentPanel(this.drawOptionsPanel());
         });

         return addNewDoctor;
     }

    /*
    **********************************************************
          Add new Diseases
    **********************************************************
    */
     public JPanel drawAddDisease() {
         JPanel addNewDisease = defaultComponents.getBasePanel();

         JLabel diseaseNameLabel = defaultComponents.getLabel("Disease Name: ");
         JTextField diseaseNameTextField = defaultComponents.getTextfield();

         JButton addDiseaseButton = defaultComponents.getButton("Add Disease");

         int componentVerticalPlace = 150;
         int componentSpaceBetween = 50;

         addNewDisease.add(diseaseNameLabel);
         diseaseNameLabel.setLocation(this.centerOfScreen, componentVerticalPlace);
         componentVerticalPlace += componentSpaceBetween;

         addNewDisease.add(diseaseNameTextField);
         diseaseNameTextField.setLocation(this.centerOfScreen, componentVerticalPlace);
         componentVerticalPlace += componentSpaceBetween;

         addNewDisease.add(addDiseaseButton);
         addDiseaseButton.setLocation(this.centerOfScreen, componentVerticalPlace);
         componentVerticalPlace += componentSpaceBetween;

         addNewDisease.add(buttonBackToMainMenu);
         buttonBackToMainMenu.setLocation(this.centerOfScreen, componentVerticalPlace);

         addDiseaseButton.addActionListener(e -> {
             String diseaseName = diseaseNameTextField.getText();

             dbInserter.insertDisese(diseaseName);
         });

         return  addNewDisease;
     }

     /*
     **********************************************************
           Add new Bad Habit
     **********************************************************
     */
     public  JPanel drawAddNewBadHabit() {
         JPanel addNewBadHabit = defaultComponents.getBasePanel();

         JLabel badHabitNameLabel = defaultComponents.getLabel("Bad Habit Name: ");
         JTextField badHabitNameTextField = defaultComponents.getTextfield();

         JButton addBadHabitButton = defaultComponents.getButton("Add Bad Habit");

         int componentVerticalPlace = 150;
         int componentSpaceBetween = 50;

         addNewBadHabit.add(badHabitNameLabel);
         badHabitNameLabel.setLocation(this.centerOfScreen, componentVerticalPlace);
         componentVerticalPlace += componentSpaceBetween;

         addNewBadHabit.add(badHabitNameTextField);
         badHabitNameTextField.setLocation(this.centerOfScreen, componentVerticalPlace);
         componentVerticalPlace += componentSpaceBetween;

         addNewBadHabit.add(addBadHabitButton);
         addBadHabitButton.setLocation(this.centerOfScreen, componentVerticalPlace);
         componentVerticalPlace += componentSpaceBetween;

         addNewBadHabit.add(buttonBackToMainMenu);
         buttonBackToMainMenu.setLocation(this.centerOfScreen, componentVerticalPlace);

         addBadHabitButton.addActionListener(e -> {
             String badHabitName = badHabitNameTextField.getText();

             dbInserter.insertBadHabit(badHabitName);
         });

         return  addNewBadHabit;
     }

    /*
     **********************************************************
          Add new Patient
     **********************************************************
    */
    private int patient_id_add;

    public JPanel drawAddNewPatient() {
        JPanel addNewPatient = defaultComponents.getBasePanel();

        JLabel patientFirstNameLabel = defaultComponents.getLabel("First Name");
        JTextField patientFirstNameTextField = defaultComponents.getTextfield();

        JLabel patientLastNameLabel = defaultComponents.getLabel("Last Name");
        JTextField patientLastNameTestField = defaultComponents.getTextfield();

        JLabel patientEGNLabel = defaultComponents.getLabel("EGN");
        JTextField patientEGNTextField = defaultComponents.getTextfield();

        JLabel patientGenderLabel = defaultComponents.getLabel("Gender");
        JTextField patientGenderTextField = defaultComponents.getTextfield();

        JLabel patientWeightLabel = defaultComponents.getLabel("Weight");
        JTextField patientWeightTextField = defaultComponents.getTextfield();

        JLabel patientHeightLabel = defaultComponents.getLabel("Height");
        JTextField patientHeightTextField = defaultComponents.getTextfield();

        JButton addPatientButton = defaultComponents.getButton("Add Patient");

        int leftComponentVerticalPlace = 150;
        int rightComponentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        //LEFT COLUMN
        addNewPatient.add(patientFirstNameLabel);
        patientFirstNameLabel.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
        leftComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientFirstNameTextField);
        patientFirstNameTextField.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
        leftComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientLastNameLabel);
        patientLastNameLabel.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
        leftComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientLastNameTestField);
        patientLastNameTestField.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
        leftComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientWeightLabel);
        patientWeightLabel.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
        leftComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientWeightTextField);
        patientWeightTextField.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace);
        leftComponentVerticalPlace += componentSpaceBetween * 2;

        //RIGHT COLUMN
        addNewPatient.add(patientEGNLabel);
        patientEGNLabel.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
        rightComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientEGNTextField);
        patientEGNTextField.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
        rightComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientGenderLabel);
        patientGenderLabel.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
        rightComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientGenderTextField);
        patientGenderTextField.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
        rightComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientHeightLabel);
        patientHeightLabel.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
        rightComponentVerticalPlace += componentSpaceBetween;

        addNewPatient.add(patientHeightTextField);
        patientHeightTextField.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);
        rightComponentVerticalPlace += componentSpaceBetween * 2;

        addNewPatient.add(addPatientButton);
        addPatientButton.setLocation(this.leftColumnOfScreen, leftComponentVerticalPlace );

        addNewPatient.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(this.rightColumnOfScreen, rightComponentVerticalPlace);

        addPatientButton.addActionListener(e -> {
            String add_firstName = patientFirstNameTextField.getText();
            String add_lastName = patientFirstNameTextField.getText();
            String add_gender = patientGenderTextField.getText();
            int add_egn = Integer.parseInt(patientEGNTextField.getText());
            int add_weight = Integer.parseInt(patientWeightTextField.getText());
            int add_height = Integer.parseInt(patientHeightTextField.getText());

            this.dbInserter.insertPatient(this.add_userName, this.add_userPassword, this.add_userEmail,
                    add_firstName, add_lastName, add_gender, add_egn, add_weight, add_height
            );

            patient_id_add = dbInserter.getLastPatientID();
            int doctorId = dbLoader.getDoctorId(this.userName);

            this.dbInserter.insertPatientAppointment(patient_id_add, doctorId );
            this.dbInserter.insertPatientToDoctor(patient_id_add, doctorId);

            this.mainFrame.setCurrentPanel(this.drawAddPatientDiseases(patient_id_add));
        });

        return addNewPatient;
    }

    /*
     **********************************************************
          Add Patient Diseases
     **********************************************************
    */
    public JPanel drawAddPatientDiseases(int patientId) {
        this.allDiseases = dbLoader.getAllDiseases();

        LinkedHashMap<Integer, Disease> patientDiseases = new LinkedHashMap<>();
        
        JPanel addPatientDiseases = defaultComponents.getBasePanel();

        JLabel isPatientSickLabel = defaultComponents.getLabel("Add patients diseases: ");

        JButton addButton = defaultComponents.getButton(" Add ");
        JButton continueButton = defaultComponents.getButton(" Continue ");

        JComboBox listDiseases = defaultComponents.getComboBoxDiseases(allDiseases);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList addedDiseases = defaultComponents.getListWithModel(model);
        JScrollPane scrollPane = defaultComponents.getScrollPanelWithList(addedDiseases);

        int componentVerticalPlace = 50;
        int componentSpaceBetween = 50;

        addPatientDiseases.add(isPatientSickLabel);
        isPatientSickLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientDiseases.add(listDiseases);
        listDiseases.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientDiseases.add(scrollPane);
        scrollPane.setLocation(this.defaultValues.getDefaultCenterForBig(), componentVerticalPlace);
        componentVerticalPlace += this.defaultValues.getVerticalSpaceAfterBig();

        addPatientDiseases.add(addButton);
        addButton.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientDiseases.add(continueButton);
        continueButton.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientDiseases.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(centerOfScreen, componentVerticalPlace);

        addButton.addActionListener(e -> {
            if (listDiseases.getSelectedItem() != null) {
                String addDisease = String.valueOf(listDiseases.getSelectedItem());

                model.addElement(addDisease);

                listDiseases.removeItem(listDiseases.getSelectedItem());

                this.mainFrame.revalidate();
                this.mainFrame.repaint();
            }
        });

        continueButton.addActionListener(e -> {
            if (model.size() != 0) {
                for (int i = 0; i < model.getSize(); i++) {
                    int diseasesKey = Integer.parseInt(model.get(i).substring(0, 1));

                    patientDiseases.put(diseasesKey, allDiseases.get(diseasesKey));
                }

                this.dbInserter.insertPatientToDisease(patientId, patientDiseases);

                this.patientAddedDiseases = patientDiseases;

                this.mainFrame.setCurrentPanel(this.drawAddPatientBadHabits(patientId));
            } else {
                this.mainFrame.setCurrentPanel(this.drawAddPatientBadHabits(patientId));
            }
        });


        return addPatientDiseases;
    }

    /*
     **********************************************************
          Add Patient Bad Habits
     **********************************************************
    */
    public JPanel drawAddPatientBadHabits(int patientId) {
        this.allBadHabits = dbLoader.getAllBadHabits();

        LinkedHashMap<Integer, String> patientBadHibits = new LinkedHashMap<>();

        JPanel addPatientBadHabits = defaultComponents.getBasePanel();

        JLabel addBadHabitLabel = defaultComponents.getLabel("Add patients bad habits: ");

        JButton addButton = defaultComponents.getButton(" Add ");
        JButton continueButton = defaultComponents.getButton(" Continue ");

        JComboBox listBadHabits = defaultComponents.getComboBoxBadHabits(allBadHabits);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList addedHabits = defaultComponents.getListWithModel(model);
        JScrollPane scrollPane = defaultComponents.getScrollPanelWithList(addedHabits);

        int componentVerticalPlace = 50;
        int componentSpaceBetween = 50;

        addPatientBadHabits.add(addBadHabitLabel);
        addBadHabitLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientBadHabits.add(listBadHabits);
        listBadHabits.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientBadHabits.add(scrollPane);
        scrollPane.setLocation(this.defaultValues.getDefaultCenterForBig(), componentVerticalPlace);
        componentVerticalPlace += this.defaultValues.getVerticalSpaceAfterBig();

        addPatientBadHabits.add(addButton);
        addButton.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientBadHabits.add(continueButton);
        continueButton.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientBadHabits.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(centerOfScreen, componentVerticalPlace);

        addButton.addActionListener(e -> {
            if (listBadHabits.getSelectedItem() != null) {
                String addHabit = String.valueOf(listBadHabits.getSelectedItem());

                model.addElement(addHabit);

                listBadHabits.removeItem(listBadHabits.getSelectedItem());

                this.mainFrame.revalidate();
                this.mainFrame.repaint();
            }
        });

        continueButton.addActionListener(e -> {

            if (model.getSize() != 0) {
                for (int i = 0; i < model.getSize(); i++) {
                    int habitKey = Integer.parseInt(model.get(i).substring(0, 1));

                    patientBadHibits.put(habitKey, allBadHabits.get(habitKey));
                }

                dbInserter.insertPatientToBadHabits(patientId, patientBadHibits);

                this.patientAddedBadHabits = patientBadHibits;

                if (patientAddedDiseases.size() > 0) {
                    this.connectDiseasesToBadHabits();
                }

                this.mainFrame.setCurrentPanel(this.drawAddPatientEatingHabits(patientId));
            } else {
                this.mainFrame.setCurrentPanel(this.drawAddPatientEatingHabits(patientId));
            }
        });

        return addPatientBadHabits;
    }

    /*
     **********************************************************
          Connect Diseases To Bad Habits -- Hidden
     **********************************************************
    */
    private LinkedHashMap<Integer, Disease> patientAddedDiseases;
    private LinkedHashMap<Integer, String> patientAddedBadHabits;

    private void connectDiseasesToBadHabits() {
        this.dbInserter.insertDiseasesToBadHabits(patientAddedDiseases, patientAddedBadHabits);
    }

     /*
     **********************************************************
          Add Eating Habits
     **********************************************************
    */

    public JPanel drawAddPatientEatingHabits(int patientId) {
        JPanel addPatientEatingHabits = defaultComponents.getBasePanel();

        JLabel averageCaloriesLabel = defaultComponents.getLabel(" Average Calories: ");
        JTextField averageCaloriesTextField = defaultComponents.getTextfield();

        JLabel averageProteinsLabel = defaultComponents.getLabel(" Average Proteins: ");
        JTextField averageProteinsTextField = defaultComponents.getTextfield();

        JLabel averageCarbsLabel = defaultComponents.getLabel(" Average Carbs: ");
        JTextField averageCarbsTextField = defaultComponents.getTextfield();

        JLabel averageFatsLabel = defaultComponents.getLabel(" Average Fats: ");
        JTextField averageFatsTextField = defaultComponents.getTextfield();

        JButton continueButton = defaultComponents.getButton(" Continue ");

        int componentVerticalPlace = 50;
        int componentSpaceBetween = 50;

        addPatientEatingHabits.add(averageCaloriesLabel);
        averageCaloriesLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientEatingHabits.add(averageCaloriesTextField);
        averageCaloriesTextField.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientEatingHabits.add(averageProteinsLabel);
        averageProteinsLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientEatingHabits.add(averageProteinsTextField);
        averageProteinsTextField.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientEatingHabits.add(averageCarbsLabel);
        averageCarbsLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientEatingHabits.add(averageCarbsTextField);
        averageCarbsTextField.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientEatingHabits.add(averageFatsLabel);
        averageFatsLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientEatingHabits.add(averageFatsTextField);
        averageFatsTextField.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;
        componentVerticalPlace += componentSpaceBetween;

        addPatientEatingHabits.add(continueButton);
        continueButton.setLocation(centerOfScreen, componentVerticalPlace);

        continueButton.addActionListener(e -> {
            int avgCal = Integer.parseInt(averageCaloriesTextField.getText());
            int avgProt = Integer.parseInt(averageProteinsTextField.getText());
            int avgCarb = Integer.parseInt(averageCarbsTextField.getText());
            int avgFats = Integer.parseInt(averageFatsTextField.getText());

            this.dbInserter.insertPatientEatingHabits(patientId, avgCal, avgProt, avgCarb, avgFats);

            this.mainFrame.setCurrentPanel(this.drawAddPatientCondition(patientId));
        });

        return  addPatientEatingHabits;
    }

    /*
    **********************************************************
         Add Patient Condition
    **********************************************************
    */

    public JPanel drawAddPatientCondition(int patientId) {
        JPanel addPatientCondition = defaultComponents.getBasePanel();

        JLabel patientConditionLabel = defaultComponents.getLabel("Insert patient basic condition");

        JCheckBox  patientSickButton = defaultComponents.getCheckBox(" Patient is sick ");

        JCheckBox  patientHBPButton = defaultComponents.getCheckBox(" Patient has high blood pressure ");

        JLabel patientDescriptionLabel = defaultComponents.getLabel("Write patient description");

        JTextField patientDescriptionTextField = defaultComponents.getTextfield();

        JButton continueButton = defaultComponents.getButton("Continue");

        int componentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        addPatientCondition.add(patientConditionLabel);
        patientConditionLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientCondition.add(patientSickButton);
        patientSickButton.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientCondition.add(patientHBPButton);
        patientHBPButton.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientCondition.add(patientDescriptionLabel);
        patientDescriptionLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        addPatientCondition.add(patientDescriptionTextField);
        patientDescriptionTextField.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;
        componentVerticalPlace += componentSpaceBetween;

        addPatientCondition.add(continueButton);
        continueButton.setLocation(centerOfScreen, componentVerticalPlace);

        continueButton.addActionListener(e -> {
            int patSick = patientSickButton.isSelected() ? 1 : 0;
            int patHBP = patientHBPButton.isSelected() ? 1 : 0;
            String patDesc = patientDescriptionTextField.getText();

            this.dbInserter.insertPatientCondition(patientId, patSick, patHBP, patDesc);

            this.mainFrame.setCurrentPanel(this.drawOptionsPanel());
        });

        return  addPatientCondition;
    }

    /*
     **********************************************************
          See all Diseases
     **********************************************************
    */
    public JPanel drawSeeAllDiseases() {
        JPanel seeAllDiseasesPanel = defaultComponents.getBasePanel();

        this.allDiseases = dbLoader.getAllDiseases();

        DefaultListModel<String> model = new DefaultListModel<>();
        JList allDiseasesList = defaultComponents.getListWithModel(model);
        JScrollPane scrollPane = defaultComponents.getScrollPanelWithList(allDiseasesList);

        JButton seeDiseaseButton = defaultComponents.getButton("See Disease");

        int componentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        for (Map.Entry<Integer, Disease> DiseaseEntry : allDiseases.entrySet()) {
            String text = DiseaseEntry.getKey() + " \t \t " + DiseaseEntry.getValue().getName();

            model.addElement(text);
        }

        seeAllDiseasesPanel.add(scrollPane);
        scrollPane.setLocation(this.defaultValues.getDefaultCenterForBig(), componentVerticalPlace);
        componentVerticalPlace += this.defaultValues.getVerticalSpaceAfterBig();

        seeAllDiseasesPanel.add(seeDiseaseButton);
        seeDiseaseButton.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        seeAllDiseasesPanel.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(this.centerOfScreen,componentVerticalPlace);

        seeDiseaseButton.addActionListener(e -> {
            String listIndex = allDiseasesList.getSelectedValue().toString().substring(0,1);

            int disease_id = Integer.parseInt(listIndex) ;

            this.mainFrame.setCurrentPanel(this.drawDiseaseCountBadHabits(disease_id));
        });

        return seeAllDiseasesPanel;
    }

    /*
     **********************************************************
          See all Disease With Bad Habits Count
     **********************************************************
    */

    public JPanel drawDiseaseCountBadHabits(int disease_id) {
        JPanel drawCountPanel = defaultComponents.getBasePanel();

        LinkedHashMap<String, Integer> getDiseaseWithBadHabit = dbLoader.getDiseaseWithBadHabit(disease_id);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList allBadHabitsList = defaultComponents.getListWithModel(model);
        JScrollPane scrollPane = defaultComponents.getScrollPanelWithList(allBadHabitsList);

        JButton backToAllDiseases = defaultComponents.getButton("Back to all diseases");

        int componentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        for (Map.Entry<String, Integer> habitEntry : getDiseaseWithBadHabit.entrySet()) {
            String text = "Habit: " + habitEntry.getKey() + " Count: " + habitEntry.getValue();

            model.addElement(text);

            this.mainFrame.revalidate();
            this.mainFrame.repaint();
        }

        drawCountPanel.add(scrollPane);
        scrollPane.setLocation(this.defaultValues.getDefaultCenterForBig(), componentVerticalPlace);
        componentVerticalPlace += this.defaultValues.getVerticalSpaceAfterBig();

        drawCountPanel.add(backToAllDiseases);
        backToAllDiseases.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        drawCountPanel.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(this.centerOfScreen,componentVerticalPlace);

        backToAllDiseases.addActionListener(e -> {
            this.mainFrame.setCurrentPanel(this.drawSeeAllDiseases());
        });

        return  drawCountPanel;
    }

    /*
     **********************************************************
          See all Patients
     **********************************************************
    */
    public JPanel drawSeeAllPatient() {
        JPanel seeAllPatientPanel = defaultComponents.getBasePanel();

        int doctor_id = dbLoader.getDoctorId(this.userName);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList allPatientsList = defaultComponents.getListWithModel(model);
        JScrollPane scrollPane = defaultComponents.getScrollPanelWithList(allPatientsList);

        for (String pat : this.dbLoader.getPatientsForDoctor(doctor_id)) {
            model.addElement(pat);

            this.mainFrame.revalidate();
            this.mainFrame.repaint();
        }

        int componentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        seeAllPatientPanel.add(scrollPane);
        scrollPane.setLocation(this.defaultValues.getDefaultCenterForBig(), componentVerticalPlace);
        componentVerticalPlace += this.defaultValues.getVerticalSpaceAfterBig();
        componentVerticalPlace += componentSpaceBetween;

        seeAllPatientPanel.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(this.centerOfScreen,componentVerticalPlace);


        return seeAllPatientPanel;
    }

    /*
     **********************************************************
          Write weakly report
     **********************************************************
    */
    public JPanel drawWriteWeaklyReport() {
        JPanel writeWeaklyReportPanel = defaultComponents.getBasePanel();

        JLabel writeReportLabel = defaultComponents.getLabel("Tell us how you feel:");

        JTextField writeReportTextField = defaultComponents.getTextfield();

        JButton submitReportButton = defaultComponents.getButton("Submit");

        int componentVerticalPlace = 150;
        int componentSpaceBetween = 50;

        writeWeaklyReportPanel.add(writeReportLabel);
        writeReportLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        writeWeaklyReportPanel.add(writeReportTextField);
        writeReportTextField.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;
        componentVerticalPlace += componentSpaceBetween;

        writeWeaklyReportPanel.add(submitReportButton);
        submitReportButton.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        writeWeaklyReportPanel.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(centerOfScreen, componentVerticalPlace);

        submitReportButton.addActionListener(e -> {
            String reportText = writeReportTextField.getText();

            this.dbInserter.insertPatientWeaklyReport(userId, reportText);

            this.mainFrame.setCurrentPanel(this.drawOptionsPanel());
        });

        return writeWeaklyReportPanel;
    }

    /*
     **********************************************************
          Make appointment
     **********************************************************
    */
    public JPanel drawMakeAppointment() {
        LinkedHashMap<String, String> emails = this.dbLoader.getPatientAndDoctorEmail(this.userId);

        JPanel makeAppointmentPanel = defaultComponents.getBasePanel();

        JLabel titleLabel = defaultComponents.getLabel("<html> Write down how you feel and <br/> when do you want to see your doctor </html>");

        JTextField emailTextField = defaultComponents.getTextfieldBig();

        JButton sendEmailButton = defaultComponents.getButton("Send");

        int componentVerticalPlace = 50;
        int componentSpaceBetween = 50;

        makeAppointmentPanel.add(titleLabel);
        titleLabel.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        makeAppointmentPanel.add(emailTextField);
        emailTextField.setLocation(this.defaultValues.getDefaultCenterForBig(), componentVerticalPlace);
        componentVerticalPlace += this.defaultValues.getVerticalSpaceAfterBig();
        componentVerticalPlace += componentSpaceBetween;

        makeAppointmentPanel.add(sendEmailButton);
        sendEmailButton.setLocation(centerOfScreen, componentVerticalPlace);
        componentVerticalPlace += componentSpaceBetween;

        makeAppointmentPanel.add(buttonBackToMainMenu);
        buttonBackToMainMenu.setLocation(centerOfScreen,componentVerticalPlace);

        sendEmailButton.addActionListener(e -> {
            String docEmail = "";

            for (String docEmailKey : emails.keySet()) {
                docEmail = docEmailKey;
            }

            String patEmail = emails.get(docEmail);

            String emailText = emailTextField.getText();

            if (EmailHandler.sendEmail(patEmail,this.add_userPassword, docEmail, emailText)) {
                JOptionPane.showMessageDialog(null, "Email send successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Email error please call support");
            }

            this.mainFrame.setCurrentPanel(this.drawOptionsPanel());
        });

        return makeAppointmentPanel;
    }

    /*
     ********************************************************************************************************************
            *
            *       Action Listener
            *
     ********************************************************************************************************************
    */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == currentOptions.get(this.addNewAdmin)) {
            this.mainFrame.setCurrentPanel(this.drawAddNewUser(this.adminPrivilege));

        } else if (e.getSource() == currentOptions.get(this.addNewDoctor)) {
            this.mainFrame.setCurrentPanel(this.drawAddNewUser(this.doctorPrivilege));

        } else if (e.getSource() == currentOptions.get(this.addNewPatient)) {
            this.mainFrame.setCurrentPanel(this.drawAddNewUser(this.patientPrivilege));

        } else if (e.getSource() == currentOptions.get(this.addNewDisease)) {
            this.mainFrame.setCurrentPanel(this.drawAddDisease());

        } else if (e.getSource() == currentOptions.get(this.addNewBadHabit)) {
            this.mainFrame.setCurrentPanel(this.drawAddNewBadHabit());

        } else if (e.getSource() == currentOptions.get(this.seeAllDiseases)) {
            this.mainFrame.setCurrentPanel(this.drawSeeAllDiseases());

        } else if (e.getSource() == currentOptions.get(this.seeAllPatients)) {
            this.mainFrame.setCurrentPanel(this.drawSeeAllPatient());

        } else if (e.getSource() == currentOptions.get(this.makeAppointment)) {
            this.mainFrame.setCurrentPanel(this.drawMakeAppointment());

        } else if (e.getSource() == currentOptions.get(this.writeWeaklyReport)) {
            this.mainFrame.setCurrentPanel(this.drawWriteWeaklyReport());

        } else if (e.getSource() == currentOptions.get(this.exitProgram)) {
            System.exit(0);

        } else if (e.getSource() == this.buttonBackToMainMenu) {
            this.mainFrame.setCurrentPanel(this.drawOptionsPanel());

        } else {
            JOptionPane.showMessageDialog(null, "Option TO BE ADDED 404");
        }
    }

}
