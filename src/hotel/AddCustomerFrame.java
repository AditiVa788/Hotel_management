package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddCustomerFrame extends JFrame implements ActionListener {

    JTextField tname, tdob, tidProof, tphone, temail, taddress, tcity, tlandmark;
    JComboBox<String> cbGender;
    JButton bsave, bclear;

    AddCustomerFrame(String title) {
        super(title);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(245, 245, 245));
        outerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(650, 750));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("ADD CUSTOMER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lname = new JLabel("Name:");
        JLabel lgender = new JLabel("Gender:");
        JLabel ldob = new JLabel("DOB (DD-MM-YYYY):");
        JLabel lidProof = new JLabel("ID Proof:");
        JLabel lphone = new JLabel("Phone:");
        JLabel lemail = new JLabel("Email:");
        JLabel laddress = new JLabel("Address:");
        JLabel lcity = new JLabel("City:");
        JLabel llandmark = new JLabel("Landmark:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        lname.setFont(labelFont);
        lgender.setFont(labelFont);
        ldob.setFont(labelFont);
        lidProof.setFont(labelFont);
        lphone.setFont(labelFont);
        lemail.setFont(labelFont);
        laddress.setFont(labelFont);
        lcity.setFont(labelFont);
        llandmark.setFont(labelFont);

        tname = new JTextField(18);
        cbGender = new JComboBox<>(new String[] {"Male", "Female", "Transgender", "Other"});
        cbGender.setBackground(Color.WHITE);
        tdob = new JTextField(18);
        tidProof = new JTextField(18);
        tphone = new JTextField(18);
        temail = new JTextField(18);
        taddress = new JTextField(18);
        tcity = new JTextField(18);
        tlandmark = new JTextField(18);

        tname.setFont(fieldFont);
        cbGender.setFont(fieldFont);
        tdob.setFont(fieldFont);
        tidProof.setFont(fieldFont);
        tphone.setFont(fieldFont);
        temail.setFont(fieldFont);
        taddress.setFont(fieldFont);
        tcity.setFont(fieldFont);
        tlandmark.setFont(fieldFont);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(lname, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tname, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lgender, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(cbGender, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(ldob, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tdob, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(lidProof, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tidProof, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(lphone, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tphone, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(lemail, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(temail, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        formPanel.add(laddress, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(taddress, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
        formPanel.add(lcity, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tcity, gbc);

        gbc.gridx = 0; gbc.gridy = 8; gbc.weightx = 0;
        formPanel.add(llandmark, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tlandmark, gbc);

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
                String gender = (String) cbGender.getSelectedItem();
                String dob = tdob.getText();
                String idProof = tidProof.getText();
                String phone = tphone.getText();
                String email = temail.getText();
                String address = taddress.getText();
                String city = tcity.getText();
                String landmark = tlandmark.getText();
                
                if (name.trim().isEmpty() || gender.trim().isEmpty() || address.trim().isEmpty() || city.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                    return;
                }

                if (!Validator.isValidDate(dob)) {
                    JOptionPane.showMessageDialog(this, "Invalid DOB format. Please use DD-MM-YYYY.");
                    return;
                }
                if (!Validator.isValidIdProof(idProof)) {
                    JOptionPane.showMessageDialog(this, "Invalid ID Proof. Must be 12-digit Aadhar or PAN format.");
                    return;
                }
                if (!Validator.isValidPhone(phone)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone. Must be 10 digits starting with 7, 8, or 9.");
                    return;
                }
                if (!Validator.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(this, "Invalid Email format.");
                    return;
                }

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO customer (name, gender, dob, id_proof, phone, email, street_no, city, landmark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);


                pst.setString(1, name);
                pst.setString(2, gender);
                pst.setDate(3, Validator.parseSqlDate(dob));
                pst.setString(4, idProof);
                pst.setString(5, phone);
                pst.setString(6, email);
                pst.setString(7, address);
                pst.setString(8, city);
                pst.setString(9, landmark);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Customer added successfully!");

                tname.setText("");
                cbGender.setSelectedIndex(0);
                tdob.setText("");
                tidProof.setText("");
                tphone.setText("");
                temail.setText("");
                taddress.setText("");
                tcity.setText("");
                tlandmark.setText("");

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
            cbGender.setSelectedIndex(0);
            tdob.setText("");
            tidProof.setText("");
            tphone.setText("");
            temail.setText("");
            taddress.setText("");
            tcity.setText("");
            tlandmark.setText("");
        }
    }
}
