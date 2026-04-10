package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageEmployeesFrame extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton bUpdate, bDelete;

    ManageEmployeesFrame(String title) {
        super(title);
        setLayout(new BorderLayout());

        String[] columns = {"Emp ID", "Name", "Designation", "Phone", "Salary", "Hotel ID"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        loadData();

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        bUpdate = new JButton("Update Info");
        bDelete = new JButton("Delete Employee");

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
            String query = "SELECT emp_id, name, designation, phone, salary, hotel_id FROM employee";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String[] row = {
                    rs.getString("emp_id"),
                    rs.getString("name"),
                    rs.getString("designation"),
                    rs.getString("phone"),
                    rs.getString("salary"),
                    rs.getString("hotel_id")
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
            JOptionPane.showMessageDialog(this, "Please select an employee from the table first.");
            return;
        }

        String employeeId = (String) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);

        if (e.getSource() == bUpdate) {
            String currentDesignation = (String) model.getValueAt(row, 2);
            String currentPhone = (String) model.getValueAt(row, 3);
            String currentSalary = (String) model.getValueAt(row, 4);

            JTextField tDesignation = new JTextField(currentDesignation);
            JTextField tPhone = new JTextField(currentPhone);
            JTextField tSalary = new JTextField(currentSalary);
            
            Object[] message = {
                "Designation:", tDesignation,
                "Phone:", tPhone,
                "Salary:", tSalary
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Update Info for " + name, JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String newDesignation = tDesignation.getText().trim();
                String phone = tPhone.getText().trim();
                String salaryStr = tSalary.getText().trim();

                if (!Validator.isValidPhone(phone)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone. Must be 10 digits starting with 7, 8, or 9.");
                    return;
                }

                Double salary;
                try {
                    salary = Double.parseDouble(salaryStr);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Invalid Salary value.");
                    return;
                }

                if (!Validator.isValidSalary(salary)) {
                    JOptionPane.showMessageDialog(this, "Invalid Salary. Must be greater than or equal to 0.");
                    return;
                }

                try {
                    Connection con = DBConnection.getConnection();
                    String q = "UPDATE employee SET designation = ?, phone = ?, salary = ? WHERE emp_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setString(1, newDesignation);
                    pst.setString(2, phone);
                    pst.setDouble(3, salary);
                    pst.setString(4, employeeId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Employee updated successfully.");
                    loadData();
                } catch (NumberFormatException nfe) {
                	JOptionPane.showMessageDialog(this, "Invalid Salary value.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        } 
        else if (e.getSource() == bDelete) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete employee '" + name + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DBConnection.getConnection();
                    String q = "DELETE FROM employee WHERE emp_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setString(1, employeeId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting employee: " + ex.getMessage());
                }
            }
        }
    }
}
