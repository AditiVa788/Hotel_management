package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageCustomersFrame extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton bUpdate, bDelete;

    ManageCustomersFrame(String title) {
        super(title);
        setLayout(new BorderLayout());

        String[] columns = {"Customer ID", "Name", "Gender", "Phone", "Email", "City"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        loadData();

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        bUpdate = new JButton("Update Contact Info");
        bDelete = new JButton("Delete Customer");

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
            String query = "SELECT customer_id, name, gender, phone, email, city FROM customer";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String[] row = {
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("gender"),
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
            JOptionPane.showMessageDialog(this, "Please select a customer from the table first.");
            return;
        }

        String customerId = (String) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);

        if (e.getSource() == bUpdate) {
            String currentPhone = (String) model.getValueAt(row, 3);
            String currentEmail = (String) model.getValueAt(row, 4);

            JTextField tPhone = new JTextField(currentPhone);
            JTextField tEmail = new JTextField(currentEmail);
            
            Object[] message = {
                "Phone:", tPhone,
                "Email:", tEmail
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Update Info for " + name, JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
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

                try {
                    Connection con = DBConnection.getConnection();
                    String q = "UPDATE customer SET phone = ?, email = ? WHERE customer_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setString(1, phone);
                    pst.setString(2, email);
                    pst.setString(3, customerId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Customer updated successfully.");
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        } 
        else if (e.getSource() == bDelete) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete customer '" + name + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DBConnection.getConnection();
                    String q = "DELETE FROM customer WHERE customer_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setString(1, customerId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting customer (They may have active bookings): " + ex.getMessage());
                }
            }
        }
    }
}
