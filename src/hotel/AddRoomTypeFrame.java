package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddRoomTypeFrame extends JFrame implements ActionListener {

    JTextField ttype, tbasePrice, tmaxOccupancy, tdescription;
    JButton bsave, bclear;

    AddRoomTypeFrame(String title) {
        super(title);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(245, 245, 245));
        outerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(650, 500));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("ADD ROOM TYPE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel ltype = new JLabel("Room Type:");
        JLabel lbasePrice = new JLabel("Base Price:");
        JLabel lmaxOccupancy = new JLabel("Max Occupancy:");
        JLabel ldescription = new JLabel("Description:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        ltype.setFont(labelFont);
        lbasePrice.setFont(labelFont);
        lmaxOccupancy.setFont(labelFont);
        ldescription.setFont(labelFont);

        ttype = new JTextField(18);
        tbasePrice = new JTextField(18);
        tmaxOccupancy = new JTextField(18);
        tdescription = new JTextField(18);

        ttype.setFont(fieldFont);
        tbasePrice.setFont(fieldFont);
        tmaxOccupancy.setFont(fieldFont);
        tdescription.setFont(fieldFont);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(ltype, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(ttype, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lbasePrice, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tbasePrice, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(lmaxOccupancy, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tmaxOccupancy, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(ldescription, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tdescription, gbc);

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
                String type = ttype.getText();
                double basePrice = Double.parseDouble(tbasePrice.getText());
                int maxOccupancy = Integer.parseInt(tmaxOccupancy.getText());
                String description = tdescription.getText();

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO room_type (type, base_price, max_occupancy, description) VALUES (?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);

                pst.setString(1, type);
                pst.setDouble(2, basePrice);
                pst.setInt(3, maxOccupancy);
                pst.setString(4, description);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Room type added successfully!");

                ttype.setText("");
                tbasePrice.setText("");
                tmaxOccupancy.setText("");
                tdescription.setText("");

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
            ttype.setText("");
            tbasePrice.setText("");
            tmaxOccupancy.setText("");
            tdescription.setText("");
        }
    }
}