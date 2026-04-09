package hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class ViewRoomsFrame extends JFrame {

    JTable table;

    ViewRoomsFrame(String title) {
        super(title);

        String[] columns = {"Room ID", "Room No", "Floor No", "Status", "Hotel ID", "Type ID"};
        String[][] data = new String[100][6];

        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT room_id, room_no, floor_no, status, hotel_id, type_id FROM room";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getString("room_id");
                data[i][1] = rs.getString("room_no");
                data[i][2] = rs.getString("floor_no");
                data[i][3] = rs.getString("status");
                data[i][4] = rs.getString("hotel_id");
                data[i][5] = rs.getString("type_id");
                i++;
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        table = new JTable(data, columns);
        JScrollPane sp = new JScrollPane(table);

        add(sp);

        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}