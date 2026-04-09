package hotel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public abstract class BaseFrame extends JFrame implements ActionListener {
    public BaseFrame(String title) {
        super(title);
        setLayout(new BorderLayout());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    protected void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    public abstract void actionPerformed(ActionEvent e);
}