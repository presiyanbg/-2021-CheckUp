import javax.swing.*;
import java.awt.*;

public class GraphicsDefaultPanels {
    GraphicsDefaultValues defaultValues = new GraphicsDefaultValues();

    /*
     **********************************************************
          Panel Values
     **********************************************************
  */
    private final int verticalPanelHeight = defaultValues.getFrameHeight();
    private final int verticalPanelWidth = 50;
    private final int horizontalPanelHeight = 50;
    private final int horizontalPanelWidth = defaultValues.getFrameWidth();

    /*
     **********************************************************
          Get Panel
     **********************************************************
    */
    public JPanel getVerticalPanel() {
        JPanel verticalPanel = new JPanel();
        verticalPanel.setPreferredSize(new Dimension(this.verticalPanelWidth,this.verticalPanelHeight));
        verticalPanel.setOpaque(false);
        verticalPanel.setVisible(true);
        return verticalPanel;
    }

    public JPanel getHorizontalPanel() {
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setPreferredSize(new Dimension(this.horizontalPanelWidth,this.horizontalPanelHeight));
        horizontalPanel.setOpaque(false);
        horizontalPanel.setVisible(true);
        return horizontalPanel;
    }
}
