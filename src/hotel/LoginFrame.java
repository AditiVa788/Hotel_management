package hotel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame implements ActionListener {

    JLabel titleLabel, userLabel, passLabel;
    JTextField userField;
    JPasswordField passField;
    JButton loginButton, clearButton;

    LoginFrame(String title) {
        super(title);

        setLayout(new GridBagLayout());   
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(420, 230));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("HOTEL MANAGEMENT SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        userField = new JTextField(15);
        userField.setFont(new Font("Arial", Font.PLAIN, 16));

        passField = new JPasswordField(15);
        passField.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        formPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        formPanel.add(passField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");

        loginButton.setPreferredSize(new Dimension(100, 35));
        clearButton.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loginButton.addActionListener(this);
        clearButton.addActionListener(this);

        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.equals("admin") && password.equals("1234")) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new Dashboard("Hotel Dashboard");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        }

        if (e.getSource() == clearButton) {
            userField.setText("");
            passField.setText("");
        }
    }
}