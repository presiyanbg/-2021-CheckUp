import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class GraphicsDefaultComponents {
    private final GraphicsDefaultValues defaultValues = new GraphicsDefaultValues();

    /*
     **********************************************************
          Get Base Panel
     **********************************************************
    */
    public JPanel getBasePanel() {
        JPanel basePanel = new JPanel();
        basePanel.setSize(new Dimension(defaultValues.getPanelWidth(), defaultValues.getPanelHeight()));
        basePanel.setBackground(defaultValues.getPanelBackgroundColor());
        basePanel.setLayout(null);

        return basePanel;
    }

    /*
     **********************************************************
          Get Button
     **********************************************************
    */
    public JButton getButton(String buttonTitle) {
        JButton button = new JButton();

        button.setText(buttonTitle);
        button.setFont(defaultValues.getDefaultFont());
        button.setSize(new Dimension(defaultValues.getButtonWigth(), defaultValues.getButtonHieght()));
        button.setBackground(defaultValues.getButtonBackground());
        button.setForeground(defaultValues.getButtonFontColor());
        button.setFocusable(false);

        return button;
    }

    /*
       **********************************************************
            Get Textfield
       **********************************************************
    */
    public JTextField getTextfield() {
        JTextField textField = new JTextField();

        textField.setMargin(new Insets(10, 10, 10, 10));
        textField.setFont(defaultValues.getDefaultFont());
        textField.setBackground(defaultValues.getTextfieldBackground());
        textField.setForeground(defaultValues.getTextfieldFontColor());
        textField.setSize(defaultValues.getTextFieldDimension());

        return textField;
    }

    /*
       **********************************************************
            Get Textfield -- BIG
       **********************************************************
    */

    public JTextField getTextfieldBig() {
        JTextField textField = new JTextField();

        textField.setMargin(new Insets(10, 10, 10, 10));
        textField.setFont(defaultValues.getDefaultFont());
        textField.setBackground(defaultValues.getTextfieldBackground());
        textField.setForeground(defaultValues.getTextfieldFontColor());
        textField.setSize(defaultValues.getDefaultDimentsionsBig());

        return textField;
    }

    /*
       **********************************************************
            Get Label
       **********************************************************
    */
    public JLabel getLabel(String labelTitle) {
        JLabel label = new JLabel();

        label.setText(labelTitle);
        label.setSize(new Dimension(defaultValues.getLabelDimension()));
        label.setForeground(defaultValues.getLabelFontColor());
        label.setFont(defaultValues.getLabelFont());
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        return label;
    }

     /*
       **********************************************************
            Get Label With Icon
       **********************************************************
    */
    public JLabel getLabelWithIcon(String iconName) {
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon(iconName);
        int halfHorizontalSizeOfScreen = defaultValues.getPanelWidth() / 2;
        int verticalSizeOfScreen = defaultValues.getPanelHeight();
        int startOfVerticalScreen = 150;

        Image img = icon.getImage();
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, startOfVerticalScreen, halfHorizontalSizeOfScreen, verticalSizeOfScreen, null);
        ImageIcon newIcon = new ImageIcon(bi);

        label.setPreferredSize(defaultValues.getLabelDimension());
        label.setIcon(newIcon);

        return label;
    }

    public JScrollPane getScrollPanelWithList(JList list) {
        JScrollPane scrollPane = new JScrollPane(list);

        scrollPane.setFont(defaultValues.getDefaultFont());
        scrollPane.setSize(defaultValues.getDefaultDimentsionsBig());

        return scrollPane;
    }

    /*
       **********************************************************
            Get Combo Box -- Diseases
       **********************************************************
    */

    public JComboBox getComboBoxDiseases(LinkedHashMap <Integer, Disease> diseases) {
        int numberOfDiseases = diseases.size();
        int counter = 0;

        String[] diseasesNames = new String[numberOfDiseases];

        for (Map.Entry<Integer, Disease> DiseaseEntry : diseases.entrySet()) {
            diseasesNames[counter] = DiseaseEntry.toString();

            counter++;
        }

        JComboBox box = new JComboBox(diseasesNames);

        box.setSelectedIndex(0);
        box.setSize(defaultValues.getDefaultDimentsions());
        box.setFont(defaultValues.getDefaultFont());

        return box;
    }

    /*
       **********************************************************
            Get Combo Box -- Bad Habits
       **********************************************************
    */

    public JComboBox getComboBoxBadHabits(LinkedHashMap <Integer, String> habits) {
        int numberOfHabits = habits.size();
        int counter = 0;

        String[] badHabits = new String[numberOfHabits];

        for (Map.Entry<Integer, String> DiseaseEntry : habits.entrySet()) {
            badHabits[counter] = DiseaseEntry.toString();

            counter++;
        }

        JComboBox box = new JComboBox(badHabits);

        box.setSelectedIndex(0);
        box.setSize(defaultValues.getDefaultDimentsions());
        box.setFont(defaultValues.getDefaultFont());

        return box;
    }

    /*
       **********************************************************
            Get Empty List
       **********************************************************
    */

    public JList getEmptyList() {
        JList list = new JList();

        list.setSize(defaultValues.getDefaultDimentsions());
        list.setFont(defaultValues.getDefaultFont());

        return list;
    }

    /*
       **********************************************************
            Get List With Model
       **********************************************************
    */

    public JList getListWithModel(DefaultListModel model) {
        JList list = new JList(model);

        list.setSize(defaultValues.getDefaultDimentsions());
        list.setFont(defaultValues.getDefaultFont());

        return list;
    }

    /*
       **********************************************************
            Get Check Box
       **********************************************************
    */
    public JCheckBox getCheckBox(String text) {
        JCheckBox radioButton = new JCheckBox(text);

        radioButton.setSize(defaultValues.getDefaultDimentsions());
        radioButton.setFont(defaultValues.getDefaultFont());


        return  radioButton;
    }

}
