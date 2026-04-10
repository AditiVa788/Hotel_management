package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewRoomsFrame extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton bUpdate, bDelete;

    ViewRoomsFrame(String title) {
        super(title);
        setLayout(new BorderLayout());

        String[] columns = {"Room ID", "Room No", "Floor No", "Status", "Hotel ID", "Type ID"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        loadData();

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        bUpdate = new JButton("Update Status");
        bDelete = new JButton("Delete Room");

        bUpdate.addActionListener(this);
        bDelete.addActionListener(this);

        buttonPanel.add(bUpdate);
        buttonPanel.add(bDelete);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0); // clear existing data
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT room_id, room_no, floor_no, status, hotel_id, type_id FROM room";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String[] row = {
                    rs.getString("room_id"),
                    rs.getString("room_no"),
                    rs.getString("floor_no"),
                    rs.getString("status"),
                    rs.getString("hotel_id"),
                    rs.getString("type_id")
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
            JOptionPane.showMessageDialog(this, "Please select a room from the table first.");
            return;
        }

        String roomId = (String) model.getValueAt(row, 0);

        if (e.getSource() == bUpdate) {
            String[] options = {"Available", "Occupied", "Maintenance"};
            String currentStatus = (String) model.getValueAt(row, 3);
            String newStatus = (String) JOptionPane.showInputDialog(this, 
                "Select new status for Room " + model.getValueAt(row, 1) + ":", 
                "Update Status", JOptionPane.QUESTION_MESSAGE, null, options, currentStatus);

            if (newStatus != null && !newStatus.isEmpty()) {
                try {
                    Connection con = DBConnection.getConnection();
                    String q = "UPDATE room SET status = ? WHERE room_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setString(1, newStatus);
                    pst.setString(2, roomId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Status updated successfully.");
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        } 
        else if (e.getSource() == bDelete) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this room?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DBConnection.getConnection();
                    String q = "DELETE FROM room WHERE room_id = ?";
                    PreparedStatement pst = con.prepareStatement(q);
                    pst.setString(1, roomId);
                    pst.executeUpdate();
                    con.close();

                    JOptionPane.showMessageDialog(this, "Room deleted successfully.");
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting room (It may have associated bookings): " + ex.getMessage());
                }
            }
        }
    }
}
