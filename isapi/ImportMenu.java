import javax.SpringUtilities;
import javax.swing.*;\
import java.SpringForm;

public class ImportMenu {
  private static void createAndShowGUI() {
      String[] labels = {"Appname: ", "Api Key: ", "Input file location: ", "Output file location: "};
      int numPairs = labels.length;

      //Create and populate the panel.
      JPanel p = new JPanel(new SpringLayout());
      for (int i = 0; i < numPairs; i++) {
          JLabel l = new JLabel(labels[i], JLabel.TRAILING);
          p.add(l);
          JTextField textField = new JTextField(10);
          l.setLabelFor(textField);
          p.add(textField);
      }

      //Lay out the panel.
      SpringUtilities.makeCompactGrid(p,
                                      numPairs, 2, //rows, cols
                                      6, 6,        //initX, initY
                                      6, 6);       //xPad, yPad

      //Create and set up the window.
      JFrame frame = new JFrame("SpringForm");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //Set up the content pane.
      p.setOpaque(true);  //content panes must be opaque
      frame.setContentPane(p);

      //Display the window.
      frame.pack();
      frame.setVisible(true);
  }
}
