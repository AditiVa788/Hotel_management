package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewBookingsFrame extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton bUpdate, bDelete;

    ViewBookingsFrame(String title) {
        super(title);
        setLayout(new BorderLayout());

        String[] columns = {"Booking ID", "Customer ID", "Room ID", "Guests", "Check In", "Check Out", "Booking Date", "Status"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        loadData();

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        bUpdate = new JButton("Update Status");
        bDelete = new JButton("Delete Booking");

        bUpdate.addActionListener(this);
        bDelete.addActionListener(this);

        buttonPanel.add(bUpdate);
        buttonPanel.add(bDelete);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(1000, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0); // clear existing data
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT booking_id, customer_id, room_id, no_of_guests, check_in, check_out, booking_date, booking_status FROM booking";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String[] row = {
                    rs.getString("booking_id"),
                    rs.getString("customer_id"),
                    rs.getString("room_id"),
                    rs.getString("no_of_guests"),
                    rs.getString("check_in"),
                    rs.getString("check_out"),
                    rs.getString("booking_date"),
                    rs.getString("booking_status")
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
            JOptionPane.showMessageDialog(this, "Please select a booking from the table first.");
            return;
        }

        String bookingId = (String) model.getValueAt(row, 0);

        if (e.getSource() == bUpdate) {
            String[] options = {"Confirmed", "Cancelled", "Completed"};
            String currentStatus = (String) model.getValueAt(row, 7);
            String newStatus = (String) JOptionPane.showInputDialog(this, 
                "Select new status for Booking #" + bookingId + ":", 
                "Update Status", JOptionPane.QUESTION_MESSAGE, null, options, currentStatus);

            if (newStatus != null && !newStatus.isEmpty()) {
                try {
                    Connection con = DBConnection.getConnection();
                    String q = "UPDATE booking SET booking_status = ? WHERE booking_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setString(1, newStatus);
                    pst.setString(2, bookingId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Booking status updated successfully.");
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        } 
        else if (e.getSource() == bDelete) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this booking?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DBConnection.getConnection();
                    String q = "DELETE FROM booking WHERE booking_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setString(1, bookingId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Booking deleted successfully.");
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting booking: " + ex.getMessage());
                }
            }
        }
    }
}
