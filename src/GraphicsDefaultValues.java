import java.awt.*;
import java.io.File;

public class GraphicsDefaultValues {
    /*
   **********************************************************
        Program Values
   **********************************************************
    */
    private final String programTitle = "CheckUp!";

    /*
   **********************************************************
        Default Values
   **********************************************************
    */
    private final Font defaultFont = new Font("MV Bold", Font.PLAIN, 15);
    private final int defaultWidth = 300;
    private final int defaultWidthBig = 600;
    private final int defaultHeight = 50;
    private final int defaultHeightBig = 300;
    private final Dimension defaultDimentsions = new Dimension(this.defaultWidth, defaultHeight);
    private final Dimension defaultDimentsionsBig = new Dimension(this.defaultWidthBig, defaultHeightBig);

     /*
       **********************************************************
            Panel Names
       **********************************************************
    */
    private final String logInPanel =  "logInPanel";
    private final String optionsPanel =  "optionsPanel";

    /*
       **********************************************************
            Frame Values
       **********************************************************
    */
    private final int frameHeight = 800;
    private final int frameWidth = 1200;
    private final Color frameBackgroundColor = new Color(207, 207, 207);

    /*
       **********************************************************
            Panel Values
       **********************************************************
    */
    private final int panelHeight = this.frameHeight - 50;
    private final int panelWidth = this.frameWidth - 50;
    private final Color panelBackgroundColor = new Color(125, 125, 125);

    /*
       **********************************************************
            Button Values
       **********************************************************
    */
    private final int buttonHieght = 50;
    private final int buttonWigth = this.defaultWidth;
    private final Color buttonBackground = new Color(255, 59, 71);
    private final Color buttonFontColor = new Color(255, 255, 255);

    /*
       **********************************************************
            Textfield Values
       **********************************************************
    */
    private final Color textfieldBackground = new Color(255, 130, 138);
    private final Color textfieldFontColor = new Color(61, 61, 61);
    private final Dimension textFieldDimension = new Dimension(this.defaultWidth, 50);

    /*
       **********************************************************
            Label Values
       **********************************************************
    */
    private final Color labelFontColor = new Color(255, 255, 255);
    private final Font labelFont = new Font("MV Bold", Font.PLAIN, 15);
    private final Dimension labelDimension = new Dimension(this.defaultWidth, 50);

    /*
       ********************************************************************************************************************
           Functions
       ********************************************************************************************************************
    */

    /*
       **********************************************************
            Get Program Values
       **********************************************************
    */
    public String getProgramTitle() {
        return this.programTitle;
    }

    /*
   **********************************************************
       Get Default Values
   **********************************************************
    */
    public Font getDefaultFont() { return this.defaultFont; }

    public Dimension getDefaultDimentsions() { return this.defaultDimentsions; }

    public Dimension getDefaultDimentsionsBig() { return this.defaultDimentsionsBig; }

    public int getDefaultCenterForBig() {
        return (this.panelWidth - this.defaultWidthBig) / 2;
    }

    public int getVerticalSpaceAfterBig() {
        return this.defaultHeightBig;
    }

    /*
       **********************************************************
            Get Program Values
       **********************************************************
    */
    public  String getLogInPanel() { return this.logInPanel; }

    public String getOptionsPanel() { return this.optionsPanel; }

    /*
       **********************************************************
            Get Frame Values
       **********************************************************
    */
    public int getFrameHeight() {
        return this.frameHeight;
    }

    public int getFrameWidth() {
        return this.frameWidth;
    }

    Color getFrameBackgroundColor() {
        return this.frameBackgroundColor;
    }

     /*
       **********************************************************
            Get Panel Values
       **********************************************************
    */
    public int getPanelHeight() {
        return this.panelHeight;
    }

    public int getPanelWidth() {
        return this.panelWidth;
    }

    public Color getPanelBackgroundColor() {
        return panelBackgroundColor;
    }

     /*
       **********************************************************
            Get Button Values
       **********************************************************
    */
    public int getButtonHieght() {
        return this.buttonHieght;
    }

    public int getButtonWigth() {
        return this.buttonWigth;
    }

    public Color getButtonBackground() {
        return this.buttonBackground;
    }

    public Color getButtonFontColor() {
        return this.buttonFontColor;
    }

    /*
       **********************************************************
            Get Textfield Values
       **********************************************************
    */

    public Color getTextfieldBackground() {
        return this.textfieldBackground;
    }

    public Color getTextfieldFontColor() {
        return this.textfieldFontColor;
    }

    public Dimension getTextFieldDimension() {
        return this.textFieldDimension;
    }

    /*
       **********************************************************
            Get Label Values
       **********************************************************
    */
    public Color getLabelFontColor() {
        return this.labelFontColor;
    }

    public Font getLabelFont() {
        return this.labelFont;
    }

    public Dimension getLabelDimension() {
        return this.labelDimension;
    }
}

