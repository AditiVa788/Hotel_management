package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddHotelFrame extends JFrame implements ActionListener {

    JTextField tname, trating, tstreet, tcity, tlandmark, tphone, temail;
    JButton bsave, bclear;

    AddHotelFrame(String title) {
        super(title);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(245, 245, 245));
        outerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(650, 700));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("ADD HOTEL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lname = new JLabel("Hotel Name:");
        JLabel lrating = new JLabel("Rating:");
        JLabel lstreet = new JLabel("Street No:");
        JLabel lcity = new JLabel("City:");
        JLabel llandmark = new JLabel("Landmark:");
        JLabel lphone = new JLabel("Phone:");
        JLabel lemail = new JLabel("Email:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        lname.setFont(labelFont);
        lrating.setFont(labelFont);
        lstreet.setFont(labelFont);
        lcity.setFont(labelFont);
        llandmark.setFont(labelFont);
        lphone.setFont(labelFont);
        lemail.setFont(labelFont);

        tname = new JTextField(18);
        trating = new JTextField(18);
        tstreet = new JTextField(18);
        tcity = new JTextField(18);
        tlandmark = new JTextField(18);
        tphone = new JTextField(18);
        temail = new JTextField(18);

        tname.setFont(fieldFont);
        trating.setFont(fieldFont);
        tstreet.setFont(fieldFont);
        tcity.setFont(fieldFont);
        tlandmark.setFont(fieldFont);
        tphone.setFont(fieldFont);
        temail.setFont(fieldFont);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(lname, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tname, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lrating, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(trating, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(lstreet, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tstreet, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(lcity, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tcity, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(llandmark, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tlandmark, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(lphone, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tphone, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        formPanel.add(lemail, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(temail, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        bsave = new JButton("Save");
        bclear = new JButton("Clear");

        bsave.setPreferredSize(new Dimension(100, 35));
        bclear.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(bsave);
        buttonPanel.add(bclear);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        outerPanel.add(mainPanel);

        JScrollPane scrollPane = new JScrollPane(outerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        bsave.addActionListener(this);
        bclear.addActionListener(this);

        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bsave) {
            try {
                String name = tname.getText();
                double rating = Double.parseDouble(trating.getText());
                String street = tstreet.getText();
                String city = tcity.getText();
                String landmark = tlandmark.getText();
                String phone = tphone.getText();
                String email = temail.getText();

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO hotel (name, rating, street_no, city, landmark, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);

                pst.setString(1, name);
                pst.setDouble(2, rating);
                pst.setString(3, street);
                pst.setString(4, city);
                pst.setString(5, landmark);
                pst.setString(6, phone);
                pst.setString(7, email);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Hotel added successfully!");

                tname.setText("");
                trating.setText("");
                tstreet.setText("");
                tcity.setText("");
                tlandmark.setText("");
                tphone.setText("");
                temail.setText("");

                con.close();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
            }
            catch (java.sql.SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID! Please make sure referenced data exists.");
            }
            catch (java.sql.SQLSyntaxErrorException ex) {
                JOptionPane.showMessageDialog(this, "Database column mismatch. Please contact developer.");
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.");
            }
        }

        if (e.getSource() == bclear) {
            tname.setText("");
            trating.setText("");
            tstreet.setText("");
            tcity.setText("");
            tlandmark.setText("");
            tphone.setText("");
            temail.setText("");
        }
    }
}