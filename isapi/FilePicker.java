/* import java.awt.EventQueue;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.SpringUtilities.makeGrid;
import javax.SpringUtilities;
*/
import javax.swing.*;
import javax.swing.SpringLayout;
import java.awt.*;

public class FilePicker extends JFrame
{
  JFileChooser fc = new JFileChooser();

  public FilePicker(String title)
  {
    super(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //sets choose to allow directories only to be chosen

    /*    fc.setFileFilter(new FileFilter()
    {
    @Override
    public boolean accept(File file)
    {
    return file.getName().toUpperCase().equals(".CSV")||
    file.getName().toUpperCase().equals(".TXT");
    }

    @Override
    public String getDescription()
    {
    return ".txt files or .csv files";
    }
    });
    */
    JPanel pnl = new JPanel();
    pnl.setLayout(new GridLayout(3, 1));

    JPanel panel = new JPanel(new SpringLayout());
    for (int i = 0; i < 9; i++) {
      JTextField textField = new JTextField(Integer.toString(i));
      panel.add(textField);
    }

    SpringUtilities.makeGrid(panel,
    3, 3, //rows, cols
    5, 5, //initialX, initialY
    5, 5);//xPad, yPad

    JButton btn = new JButton("Open Directory to Import");
    ActionListener al;
    al = new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent ae)
      {
        fc.setApproveButtonText("Lets do this!");
        switch (fc.showOpenDialog(FilePicker.this))
        {
          case JFileChooser.APPROVE_OPTION:
          JOptionPane.showMessageDialog(FilePicker.this, "Selected: "+
          fc.getSelectedFile(),
          "FilePicker",
          JOptionPane.OK_OPTION);
          break;

          case JFileChooser.CANCEL_OPTION:
          JOptionPane.showMessageDialog(FilePicker.this, "Cancelled",
          "FilePicker",
          JOptionPane.OK_OPTION);
          break;

          case JFileChooser.ERROR_OPTION:
          JOptionPane.showMessageDialog(FilePicker.this, "Error",
          "FilePicker",
          JOptionPane.OK_OPTION);
        }
      }
    };
    btn.addActionListener(al);
    pnl.add(btn);

    btn = new JButton("Save Exported File");
    al = new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent ae)
      {
        switch (fc.showSaveDialog(FilePicker.this))
        {
          case JFileChooser.APPROVE_OPTION:
          JOptionPane.showMessageDialog(FilePicker.this, "Selected: "+
          fc.getSelectedFile(),
          "FilePicker",
          JOptionPane.OK_OPTION);
          break;

          case JFileChooser.CANCEL_OPTION:
          JOptionPane.showMessageDialog(FilePicker.this, "Cancelled",
          "FilePicker",
          JOptionPane.OK_OPTION);
          break;

          case JFileChooser.ERROR_OPTION:
          JOptionPane.showMessageDialog(FilePicker.this, "Error",
          "FilePicker",
          JOptionPane.OK_OPTION);
        }
      }
    };
    btn.addActionListener(al);
    pnl.add(btn);

    //    contactbtn = new JCheckBoxMenuItem("Contact import");

    setContentPane(pnl);

    pack();
    setVisible(true);
  }

  public static void main(String[] args)
  {
    Runnable r = new Runnable()
    {
      @Override
      public void run()
      {
        new FilePicker("Csv Import Assistant");
      }
    };
    EventQueue.invokeLater(r);
  }
}
