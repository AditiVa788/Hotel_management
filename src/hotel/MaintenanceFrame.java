package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MaintenanceFrame extends JFrame implements ActionListener {

    JComboBox<String> cbRoomId, cbEmpId;
    JTextField tdate, tdescription;
    JButton bsave, bclear;

    MaintenanceFrame(String title) {
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

        JLabel titleLabel = new JLabel("MAINTENANCE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lroomId = new JLabel("Room:");
        JLabel lempId = new JLabel("Employee:");
        JLabel ldate = new JLabel("Date (YYYY-MM-DD):");
        JLabel ldescription = new JLabel("Description:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        lroomId.setFont(labelFont);
        lempId.setFont(labelFont);
        ldate.setFont(labelFont);
        ldescription.setFont(labelFont);

        cbRoomId = new JComboBox<>();
        cbEmpId = new JComboBox<>();
        tdate = new JTextField(18);
        tdescription = new JTextField(18);

        cbRoomId.setFont(fieldFont);
        cbEmpId.setFont(fieldFont);
        tdate.setFont(fieldFont);
        tdescription.setFont(fieldFont);

        loadRooms();
        loadEmployees();

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lroomId, gbc);
        gbc.gridx = 1;
        formPanel.add(cbRoomId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lempId, gbc);
        gbc.gridx = 1;
        formPanel.add(cbEmpId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(ldate, gbc);
        gbc.gridx = 1;
        formPanel.add(tdate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(ldescription, gbc);
        gbc.gridx = 1;
        formPanel.add(tdescription, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        bsave = new JButton("Save");
        bclear = new JButton("Clear");

        buttonPanel.add(bsave);
        buttonPanel.add(bclear);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        outerPanel.add(mainPanel);

        JScrollPane scrollPane = new JScrollPane(outerPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        bsave.addActionListener(this);
        bclear.addActionListener(this);

        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadRooms() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT room_id, room_no FROM room ORDER BY room_id";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            cbRoomId.removeAllItems();
            while (rs.next()) {
                cbRoomId.addItem(rs.getInt("room_id") + " - Room " + rs.getString("room_no"));
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading rooms.");
        }
    }

    private void loadEmployees() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT emp_id, name FROM employee ORDER BY emp_id";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            cbEmpId.removeAllItems();
            while (rs.next()) {
                cbEmpId.addItem(rs.getInt("emp_id") + " - " + rs.getString("name"));
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading employees.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bsave) {
            try {
                if (cbRoomId.getSelectedItem() == null ||
                    cbEmpId.getSelectedItem() == null ||
                    tdate.getText().trim().isEmpty() ||
                    tdescription.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    return;
                }

                int roomId = Integer.parseInt(((String) cbRoomId.getSelectedItem()).split(" - ")[0]);
                int empId = Integer.parseInt(((String) cbEmpId.getSelectedItem()).split(" - ")[0]);
                String dateText = tdate.getText().trim();
                String description = tdescription.getText().trim();

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO maintenance (room_id, emp_id, date, description) VALUES (?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);

                pst.setInt(1, roomId);
                pst.setInt(2, empId);
                pst.setDate(3, Date.valueOf(dateText));
                pst.setString(4, description);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Maintenance record added successfully!");

                tdate.setText("");
                tdescription.setText("");
                if (cbRoomId.getItemCount() > 0) cbRoomId.setSelectedIndex(0);
                if (cbEmpId.getItemCount() > 0) cbEmpId.setSelectedIndex(0);

                con.close();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date. Use format YYYY-MM-DD.");
            } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Room ID or Employee ID.");
            } catch (java.sql.SQLSyntaxErrorException ex) {
                JOptionPane.showMessageDialog(this, "Database column mismatch.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.");
            }
        }

        if (e.getSource() == bclear) {
            tdate.setText("");
            tdescription.setText("");
            if (cbRoomId.getItemCount() > 0) cbRoomId.setSelectedIndex(0);
            if (cbEmpId.getItemCount() > 0) cbEmpId.setSelectedIndex(0);
        }
    }
}