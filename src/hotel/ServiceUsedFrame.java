package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ServiceUsedFrame extends JFrame implements ActionListener {

    JComboBox<String> cbBookingId, cbServiceId;
    JTextField tusageDate, tquantity;
    JButton bsave, bclear;

    ServiceUsedFrame(String title) {
        super(title);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(245, 245, 245));
        outerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(650, 450));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("USE SERVICE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbookingId = new JLabel("Booking:");
        JLabel lserviceId = new JLabel("Service:");
        JLabel lusageDate = new JLabel("Usage Date (DD-MM-YYYY):");
        JLabel lquantity = new JLabel("Quantity:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        lbookingId.setFont(labelFont);
        lserviceId.setFont(labelFont);
        lusageDate.setFont(labelFont);
        lquantity.setFont(labelFont);

        cbBookingId = new JComboBox<>();
        cbServiceId = new JComboBox<>();
        tusageDate = new JTextField(18);
        tquantity = new JTextField(18);

        cbBookingId.setFont(fieldFont);
        cbServiceId.setFont(fieldFont);
        tusageDate.setFont(fieldFont);
        tquantity.setFont(fieldFont);

        loadBookings();
        loadServices();

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lbookingId, gbc);
        gbc.gridx = 1;
        formPanel.add(cbBookingId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lserviceId, gbc);
        gbc.gridx = 1;
        formPanel.add(cbServiceId, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lusageDate, gbc);
        gbc.gridx = 1;
        formPanel.add(tusageDate, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lquantity, gbc);
        gbc.gridx = 1;
        formPanel.add(tquantity, gbc);

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

    private void loadBookings() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT booking_id FROM booking ORDER BY booking_id";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            cbBookingId.removeAllItems();
            while (rs.next()) {
                cbBookingId.addItem(rs.getString("booking_id"));
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + ex.getMessage());
        }
    }

    private void loadServices() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT service_id, service_name FROM service ORDER BY service_id";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            cbServiceId.removeAllItems();
            while (rs.next()) {
                cbServiceId.addItem(rs.getInt("service_id") + " - " + rs.getString("service_name"));
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading services: " + ex.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bsave) {
            try {
                if (cbBookingId.getSelectedItem() == null ||
                    cbServiceId.getSelectedItem() == null ||
                    tusageDate.getText().trim().isEmpty() ||
                    tquantity.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    return;
                }

                int bookingId = Integer.parseInt((String) cbBookingId.getSelectedItem());
                int serviceId = Integer.parseInt(((String) cbServiceId.getSelectedItem()).split(" - ")[0]);
                String usageDate = tusageDate.getText().trim();
                int quantity = Integer.parseInt(tquantity.getText().trim());

                if (!Validator.isValidDate(usageDate)) {
                    JOptionPane.showMessageDialog(this, "Usage Date must be in DD-MM-YYYY format.");
                    return;
                }

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO service_usage (booking_id, service_id, date, quantity) VALUES (?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);

                pst.setInt(1, bookingId);
                pst.setInt(2, serviceId);
                pst.setDate(3, Validator.parseSqlDate(usageDate));
                pst.setInt(4, quantity);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Service usage added successfully!");

                tusageDate.setText("");
                tquantity.setText("");
                if (cbBookingId.getItemCount() > 0) cbBookingId.setSelectedIndex(0);
                if (cbServiceId.getItemCount() > 0) cbServiceId.setSelectedIndex(0);

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        if (e.getSource() == bclear) {
            tusageDate.setText("");
            tquantity.setText("");
            if (cbBookingId.getItemCount() > 0) cbBookingId.setSelectedIndex(0);
            if (cbServiceId.getItemCount() > 0) cbServiceId.setSelectedIndex(0);
        }
    }
}
