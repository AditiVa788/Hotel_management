package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageHotelsFrame extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton bUpdate, bDelete;

    ManageHotelsFrame(String title) {
        super(title);
        setLayout(new BorderLayout());

        String[] columns = {"Hotel ID", "Name", "Rating", "Phone", "Email", "City"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        loadData();

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        bUpdate = new JButton("Update Info");
        bDelete = new JButton("Delete Hotel");

        bUpdate.addActionListener(this);
        bDelete.addActionListener(this);

        buttonPanel.add(bUpdate);
        buttonPanel.add(bDelete);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT hotel_id, name, rating, phone, email, city FROM hotel";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String[] row = {
                    rs.getString("hotel_id"),
                    rs.getString("name"),
                    rs.getString("rating"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city")
                };
                model.addRow(row);
            }
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a hotel from the table first.");
            return;
        }

        String hotelId = (String) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);

        if (e.getSource() == bUpdate) {
            String currentRating = (String) model.getValueAt(row, 2);
            String currentPhone = (String) model.getValueAt(row, 3);
            String currentEmail = (String) model.getValueAt(row, 4);

            JTextField tRating = new JTextField(currentRating);
            JTextField tPhone = new JTextField(currentPhone);
            JTextField tEmail = new JTextField(currentEmail);
            
            Object[] message = {
                "Rating:", tRating,
                "Phone:", tPhone,
                "Email:", tEmail
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Update Info for " + name, JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String ratingStr = tRating.getText().trim();
                String phone = tPhone.getText().trim();
                String email = tEmail.getText().trim();

                if (!Validator.isValidPhone(phone)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone. Must be 10 digits starting with 7, 8, or 9.");
                    return;
                }
                if (!Validator.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(this, "Invalid Email format.");
                    return;
                }

                Double rating;
                try {
                	rating = Double.parseDouble(ratingStr);
                } catch (NumberFormatException nfe) {
                	JOptionPane.showMessageDialog(this, "Invalid Rating value.");
                    return;
                }

                if (!Validator.isValidRating(rating)) {
                    JOptionPane.showMessageDialog(this, "Invalid Rating. Must be between 0.0 and 5.0.");
                    return;
                }

                try {
                    Connection con = DBConnection.getConnection();
                    String q = "UPDATE hotel SET rating = ?, phone = ?, email = ? WHERE hotel_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setDouble(1, rating);
                    pst.setString(2, phone);
                    pst.setString(3, email);
                    pst.setString(4, hotelId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Hotel updated successfully.");
                    loadData();
                } catch (NumberFormatException nfe) {
                	JOptionPane.showMessageDialog(this, "Invalid Rating value.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        } 
        else if (e.getSource() == bDelete) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete hotel '" + name + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DBConnection.getConnection();
                    String q = "DELETE FROM hotel WHERE hotel_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setString(1, hotelId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Hotel deleted successfully.");
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting hotel (It may have associated rooms/employees): " + ex.getMessage());
                }
            }
        }
    }
}
