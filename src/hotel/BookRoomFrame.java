package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BookRoomFrame extends JFrame implements ActionListener {

    JComboBox<String> cbCustomerId, cbRoomId, cbStatus;
    JTextField tguests, tcheckIn, tcheckOut, tbookingDate;
    JButton bsave, bclear;

    BookRoomFrame(String title) {
        super(title);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(245, 245, 245));
        outerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(650, 700));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("BOOK ROOM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lcustomerId = new JLabel("Customer:");
        JLabel lroomId = new JLabel("Room:");
        JLabel lguests = new JLabel("No. of Guests:");
        JLabel lcheckIn = new JLabel("Check In (YYYY-MM-DD):");
        JLabel lcheckOut = new JLabel("Check Out (YYYY-MM-DD):");
        JLabel lbookingDate = new JLabel("Booking Date (YYYY-MM-DD):");
        JLabel lstatus = new JLabel("Booking Status:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        lcustomerId.setFont(labelFont);
        lroomId.setFont(labelFont);
        lguests.setFont(labelFont);
        lcheckIn.setFont(labelFont);
        lcheckOut.setFont(labelFont);
        lbookingDate.setFont(labelFont);
        lstatus.setFont(labelFont);

        cbCustomerId = new JComboBox<>();
        cbRoomId = new JComboBox<>();
        cbStatus = new JComboBox<>(new String[] { "Confirmed", "Pending" });

        tguests = new JTextField(18);
        tcheckIn = new JTextField(18);
        tcheckOut = new JTextField(18);
        tbookingDate = new JTextField(18);

        cbCustomerId.setFont(fieldFont);
        cbRoomId.setFont(fieldFont);
        cbStatus.setFont(fieldFont);
        tguests.setFont(fieldFont);
        tcheckIn.setFont(fieldFont);
        tcheckOut.setFont(fieldFont);
        tbookingDate.setFont(fieldFont);

        loadCustomers();
        loadAvailableRooms();

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lcustomerId, gbc);
        gbc.gridx = 1;
        formPanel.add(cbCustomerId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lroomId, gbc);
        gbc.gridx = 1;
        formPanel.add(cbRoomId, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lguests, gbc);
        gbc.gridx = 1;
        formPanel.add(tguests, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lcheckIn, gbc);
        gbc.gridx = 1;
        formPanel.add(tcheckIn, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lcheckOut, gbc);
        gbc.gridx = 1;
        formPanel.add(tcheckOut, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(lbookingDate, gbc);
        gbc.gridx = 1;
        formPanel.add(tbookingDate, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(lstatus, gbc);
        gbc.gridx = 1;
        formPanel.add(cbStatus, gbc);

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
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        bsave.addActionListener(this);
        bclear.addActionListener(this);

        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadCustomers() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT customer_id, name FROM customer ORDER BY customer_id";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            cbCustomerId.removeAllItems();
            while (rs.next()) {
                cbCustomerId.addItem(rs.getInt("customer_id") + " - " + rs.getString("name"));
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + ex.getMessage());
        }
    }

    private void loadAvailableRooms() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT room_id, room_no FROM room WHERE status = 'Available' ORDER BY room_id";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            cbRoomId.removeAllItems();
            while (rs.next()) {
                cbRoomId.addItem(rs.getInt("room_id") + " - Room " + rs.getString("room_no"));
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + ex.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bsave) {
            try {
                if (cbCustomerId.getSelectedItem() == null ||
                    cbRoomId.getSelectedItem() == null ||
                    tguests.getText().trim().isEmpty() ||
                    tcheckIn.getText().trim().isEmpty() ||
                    tcheckOut.getText().trim().isEmpty() ||
                    tbookingDate.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    return;
                }

                int customerId = Integer.parseInt(((String) cbCustomerId.getSelectedItem()).split(" - ")[0]);
                int roomId = Integer.parseInt(((String) cbRoomId.getSelectedItem()).split(" - ")[0]);
                int guests = Integer.parseInt(tguests.getText().trim());
                String checkIn = tcheckIn.getText().trim();
                String checkOut = tcheckOut.getText().trim();
                String bookingDate = tbookingDate.getText().trim();
                String status = (String) cbStatus.getSelectedItem();

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO booking (customer_id, room_id, no_of_guests, check_in, check_out, booking_date, booking_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);

                pst.setInt(1, customerId);
                pst.setInt(2, roomId);
                pst.setInt(3, guests);
                pst.setString(4, checkIn);
                pst.setString(5, checkOut);
                pst.setString(6, bookingDate);
                pst.setString(7, status);

                pst.executeUpdate();

                String updateRoom = "UPDATE room SET status = 'Occupied' WHERE room_id = ?";
                PreparedStatement pst2 = con.prepareStatement(updateRoom);
                pst2.setInt(1, roomId);
                pst2.executeUpdate();

                JOptionPane.showMessageDialog(this, "Room booked successfully!");

                tguests.setText("");
                tcheckIn.setText("");
                tcheckOut.setText("");
                tbookingDate.setText("");
                cbStatus.setSelectedIndex(0);

                loadAvailableRooms();

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
            tguests.setText("");
            tcheckIn.setText("");
            tcheckOut.setText("");
            tbookingDate.setText("");
            if (cbCustomerId.getItemCount() > 0) cbCustomerId.setSelectedIndex(0);
            if (cbRoomId.getItemCount() > 0) cbRoomId.setSelectedIndex(0);
            cbStatus.setSelectedIndex(0);
        }
    }
}