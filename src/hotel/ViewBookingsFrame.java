package hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class ViewBookingsFrame extends JFrame {

    JTable table;

    ViewBookingsFrame(String title) {
        super(title);

        String[] columns = {"Booking ID", "Customer ID", "Room ID", "Guests", "Check In", "Check Out", "Booking Date", "Status"};
        String[][] data = new String[100][8];

        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT booking_id, customer_id, room_id, no_of_guests, check_in, check_out, booking_date, booking_status FROM booking";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getString("booking_id");
                data[i][1] = rs.getString("customer_id");
                data[i][2] = rs.getString("room_id");
                data[i][3] = rs.getString("no_of_guests");
                data[i][4] = rs.getString("check_in");
                data[i][5] = rs.getString("check_out");
                data[i][6] = rs.getString("booking_date");
                data[i][7] = rs.getString("booking_status");
                i++;
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        table = new JTable(data, columns);
        JScrollPane sp = new JScrollPane(table);

        add(sp);

        setSize(1000, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}