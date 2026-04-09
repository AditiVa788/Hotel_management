package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddRoomFrame extends JFrame implements ActionListener {

    JTextField troomNo, tfloorNo, tstatus;
    JComboBox<String> cbHotelId, cbTypeId;
    JButton bsave, bclear;

    AddRoomFrame(String title) {
        super(title);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(245, 245, 245));
        outerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(650, 550));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("ADD ROOM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lroomNo = new JLabel("Room No:");
        JLabel lfloorNo = new JLabel("Floor No:");
        JLabel lstatus = new JLabel("Status:");
        JLabel lhotelId = new JLabel("Hotel:");
        JLabel ltypeId = new JLabel("Room Type:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        lroomNo.setFont(labelFont);
        lfloorNo.setFont(labelFont);
        lstatus.setFont(labelFont);
        lhotelId.setFont(labelFont);
        ltypeId.setFont(labelFont);

        troomNo = new JTextField(18);
        tfloorNo = new JTextField(18);
        tstatus = new JTextField(18);

        cbHotelId = new JComboBox<>();
        cbTypeId = new JComboBox<>();

        troomNo.setFont(fieldFont);
        tfloorNo.setFont(fieldFont);
        tstatus.setFont(fieldFont);
        cbHotelId.setFont(fieldFont);
        cbTypeId.setFont(fieldFont);

        loadHotels();
        loadRoomTypes();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(lroomNo, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(troomNo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lfloorNo, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tfloorNo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(lstatus, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tstatus, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(lhotelId, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(cbHotelId, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(ltypeId, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(cbTypeId, gbc);

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

    private void loadHotels() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT hotel_id, name FROM hotel";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                cbHotelId.addItem(rs.getInt("hotel_id") + " - " + rs.getString("name"));
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading hotels: " + ex.getMessage());
        }
    }

    private void loadRoomTypes() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT type_id, type FROM room_type";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                cbTypeId.addItem(rs.getInt("type_id") + " - " + rs.getString("type"));
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading room types: " + ex.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bsave) {
            try {
                String roomNo = troomNo.getText();
                int floorNo = Integer.parseInt(tfloorNo.getText());
                String status = tstatus.getText();

                String hotelItem = (String) cbHotelId.getSelectedItem();
                int hotelId = Integer.parseInt(hotelItem.split(" - ")[0]);

                String typeItem = (String) cbTypeId.getSelectedItem();
                int typeId = Integer.parseInt(typeItem.split(" - ")[0]);

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO room (room_no, floor_no, status, hotel_id, type_id) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);

                pst.setString(1, roomNo);
                pst.setInt(2, floorNo);
                pst.setString(3, status);
                pst.setInt(4, hotelId);
                pst.setInt(5, typeId);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Room added successfully!");

                troomNo.setText("");
                tfloorNo.setText("");
                tstatus.setText("");
                cbHotelId.setSelectedIndex(0);
                cbTypeId.setSelectedIndex(0);

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
            troomNo.setText("");
            tfloorNo.setText("");
            tstatus.setText("");
            if (cbHotelId.getItemCount() > 0) cbHotelId.setSelectedIndex(0);
            if (cbTypeId.getItemCount() > 0) cbTypeId.setSelectedIndex(0);
        }
    }
}