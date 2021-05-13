import javax.swing.*;
import java.awt.*;

public class GraphicsMainFrame extends JFrame {
    private final GraphicsDefaultValues defaultValues = new GraphicsDefaultValues();
    private final GraphicsDefaultPanels defaultPanels = new GraphicsDefaultPanels();

    private JPanel currentPanel = new JPanel();
    private final JPanel verticalPanelWEST = defaultPanels.getVerticalPanel();
    private final JPanel verticalPanelEAST = defaultPanels.getVerticalPanel();
    private final JPanel horizontalPanelNORTH = defaultPanels.getHorizontalPanel();
    private final JPanel horizontalPanelSOUTH = defaultPanels.getHorizontalPanel();

    GraphicsMainFrame() {
        //Frame Default Parameters
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(defaultValues.getFrameWidth(), defaultValues.getFrameHeight());
        this.getContentPane().setBackground(defaultValues.getFrameBackgroundColor());
        this.setTitle(defaultValues.getProgramTitle());

        //Frame Layout Set up
        this.setLayout(new BorderLayout());
        this.add(horizontalPanelNORTH, BorderLayout.NORTH);
        this.add(horizontalPanelSOUTH, BorderLayout.SOUTH);
        this.add(verticalPanelWEST, BorderLayout.WEST);
        this.add(verticalPanelEAST, BorderLayout.EAST);
        this.add(currentPanel, BorderLayout.CENTER);

        //Frame Default Operations
        this.setResizable(false);
        this.setVisible(true);
    }

    public void setCurrentPanel(JPanel panelIn) {
        this.remove(currentPanel);
        this.currentPanel = panelIn;
        this.add(currentPanel, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this);
    }

}
