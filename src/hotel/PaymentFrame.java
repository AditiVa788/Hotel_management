package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PaymentFrame extends JFrame implements ActionListener, ItemListener {

    JComboBox<String> cbBookingId, cbPaymentMode, cbPaymentStatus;
    JTextField tpaymentDate, tamount;
    JLabel ltotalBillValue;
    JButton bsave, bclear;

    PaymentFrame(String title) {
        super(title);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(245, 245, 245));
        outerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(650, 500));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("PAYMENT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbookingId = new JLabel("Booking ID:");
        JLabel ltotalBillTitle = new JLabel("Calculated Total Bill:");
        JLabel lpaymentDate = new JLabel("Payment Date (DD-MM-YYYY):");
        JLabel lpaymentMode = new JLabel("Payment Mode:");
        JLabel lpaymentStatus = new JLabel("Payment Status:");
        JLabel lamount = new JLabel("Total Amount Paid:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);
        Font boldFont = new Font("Arial", Font.BOLD, 16);

        lbookingId.setFont(labelFont);
        ltotalBillTitle.setFont(labelFont);
        lpaymentDate.setFont(labelFont);
        lpaymentMode.setFont(labelFont);
        lpaymentStatus.setFont(labelFont);
        lamount.setFont(boldFont);

        ltotalBillValue = new JLabel("Calculated Automatically");
        ltotalBillValue.setFont(boldFont);
        ltotalBillValue.setForeground(new Color(0, 102, 204));

        cbBookingId = new JComboBox<>();
        cbPaymentMode = new JComboBox<>(new String[] {"Cash", "Card", "UPI", "Net Banking"});
        cbPaymentStatus = new JComboBox<>(new String[] {"Paid", "Pending", "Refunded"});

        tpaymentDate = new JTextField(18);
        tamount = new JTextField(18);

        cbBookingId.setFont(fieldFont);
        cbPaymentMode.setFont(fieldFont);
        cbPaymentStatus.setFont(fieldFont);
        tpaymentDate.setFont(fieldFont);
        tamount.setFont(fieldFont);

        tamount.setEditable(true); // WE UNLOCKED THE AMOUNT!

        loadBookingIds();

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lbookingId, gbc);
        gbc.gridx = 1;
        formPanel.add(cbBookingId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(ltotalBillTitle, gbc);
        gbc.gridx = 1;
        formPanel.add(ltotalBillValue, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lpaymentDate, gbc);
        gbc.gridx = 1;
        formPanel.add(tpaymentDate, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lpaymentMode, gbc);
        gbc.gridx = 1;
        formPanel.add(cbPaymentMode, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lamount, gbc);
        gbc.gridx = 1;
        formPanel.add(tamount, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(lpaymentStatus, gbc);
        gbc.gridx = 1;
        formPanel.add(cbPaymentStatus, gbc);

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
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        bsave.addActionListener(this);
        bclear.addActionListener(this);
        cbBookingId.addItemListener(this);

        if (cbBookingId.getItemCount() > 0) {
            cbBookingId.setSelectedIndex(0);
            loadSelectedBookingDetails();
        }

        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadBookingIds() {
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
            JOptionPane.showMessageDialog(this, "Error loading booking IDs.");
        }
    }

    private void loadSelectedBookingDetails() {
        try {
            if (cbBookingId.getSelectedItem() == null) {
                tpaymentDate.setText("");
                cbPaymentMode.setSelectedIndex(0);
                cbPaymentStatus.setSelectedIndex(0);
                tamount.setText("");
                ltotalBillValue.setText("0.0");
                return;
            }

            int bookingId = Integer.parseInt((String) cbBookingId.getSelectedItem());
            Connection con = DBConnection.getConnection();

            // 1. Calculate Room Cost
            String amountQuery =
                "SELECT GREATEST(DATEDIFF(b.check_out, b.check_in), 1) as stay_days, rt.base_price " +
                "FROM booking b " +
                "JOIN room r ON b.room_id = r.room_id " +
                "JOIN room_type rt ON r.type_id = rt.type_id " +
                "WHERE b.booking_id = ?";

            PreparedStatement pstAmount = con.prepareStatement(amountQuery);
            pstAmount.setInt(1, bookingId);
            ResultSet rsAmount = pstAmount.executeQuery();

            double roomCost = 0.0;
            if (rsAmount.next()) {
                int days = rsAmount.getInt("stay_days");
                double basePrice = rsAmount.getDouble("base_price");
                roomCost = days * basePrice;
            }
            
            // 2. Calculate Services Cost
            String serviceQuery = 
                "SELECT COALESCE(SUM(su.quantity * s.price), 0) AS service_total " +
                "FROM service_usage su " +
                "JOIN service s ON su.service_id = s.service_id " +
                "WHERE su.booking_id = ?";
            PreparedStatement pstService = con.prepareStatement(serviceQuery);
            pstService.setInt(1, bookingId);
            ResultSet rsService = pstService.executeQuery();
            
            double serviceCost = 0.0;
            if (rsService.next()) {
                serviceCost = rsService.getDouble("service_total");
            }
            
            double totalBill = roomCost + serviceCost;
            ltotalBillValue.setText(String.format("%.2f", totalBill));

            // Load Existing Payment Details
            String paymentQuery =
                "SELECT payment_date, payment_mode, payment_status, amount " +
                "FROM payment WHERE booking_id = ?";

            PreparedStatement pstPayment = con.prepareStatement(paymentQuery);
            pstPayment.setInt(1, bookingId);
            ResultSet rsPayment = pstPayment.executeQuery();

            if (rsPayment.next()) {
                Date paymentDate = rsPayment.getDate("payment_date");
                if (paymentDate != null) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
                    tpaymentDate.setText(sdf.format(paymentDate));
                } else {
                    tpaymentDate.setText("");
                }

                String paymentMode = rsPayment.getString("payment_mode");
                String paymentStatus = rsPayment.getString("payment_status");
                String savedAmount = rsPayment.getString("amount");

                if (paymentMode != null) cbPaymentMode.setSelectedItem(paymentMode);
                else cbPaymentMode.setSelectedIndex(0);

                if (paymentStatus != null) cbPaymentStatus.setSelectedItem(paymentStatus);
                else cbPaymentStatus.setSelectedIndex(0);

                if (savedAmount != null) tamount.setText(savedAmount);
            } else {
                tpaymentDate.setText("");
                cbPaymentMode.setSelectedIndex(0);
                cbPaymentStatus.setSelectedIndex(0);
                tamount.setText("0.0"); // Start paid amount at 0
            }

            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading booking details: " + ex.getMessage());
        }
    }

    private boolean paymentExists(Connection con, int bookingId) throws SQLException {
        String query = "SELECT payment_id FROM payment WHERE booking_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, bookingId);
        ResultSet rs = pst.executeQuery();
        return rs.next();
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbBookingId && e.getStateChange() == ItemEvent.SELECTED) {
            loadSelectedBookingDetails();
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == bsave) {
            try {
                if (cbBookingId.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Please select a booking ID.");
                    return;
                }

                if (tamount.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Amount not available.");
                    return;
                }

                int bookingId = Integer.parseInt((String) cbBookingId.getSelectedItem());
                String paymentDateText = tpaymentDate.getText().trim();
                String paymentMode = (String) cbPaymentMode.getSelectedItem();
                String paymentStatus = (String) cbPaymentStatus.getSelectedItem();
                double amount = Double.parseDouble(tamount.getText().trim());

                if (!paymentDateText.isEmpty() && !Validator.isValidDate(paymentDateText)) {
                    JOptionPane.showMessageDialog(this, "Payment Date must be in DD-MM-YYYY format.");
                    return;
                }

                Connection con = DBConnection.getConnection();

                if (paymentExists(con, bookingId)) {
                    String updateQuery =
                        "UPDATE payment " +
                        "SET payment_date = ?, payment_mode = ?, payment_status = ?, amount = ? " +
                        "WHERE booking_id = ?";

                    PreparedStatement pst = con.prepareStatement(updateQuery);

                    if (paymentDateText.isEmpty()) pst.setNull(1, Types.DATE);
                    else pst.setDate(1, Validator.parseSqlDate(paymentDateText));

                    pst.setString(2, paymentMode);
                    pst.setString(3, paymentStatus);
                    pst.setDouble(4, amount);
                    pst.setInt(5, bookingId);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Payment updated successfully!");
                } else {
                    String insertQuery =
                        "INSERT INTO payment (booking_id, payment_date, payment_mode, payment_status, amount) " +
                        "VALUES (?, ?, ?, ?, ?)";

                    PreparedStatement pst = con.prepareStatement(insertQuery);
                    pst.setInt(1, bookingId);

                    if (paymentDateText.isEmpty()) pst.setNull(2, Types.DATE);
                    else pst.setDate(2, Validator.parseSqlDate(paymentDateText));

                    pst.setString(3, paymentMode);
                    pst.setString(4, paymentStatus);
                    pst.setDouble(5, amount);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Payment recorded successfully!");
                }

                con.close();
                loadSelectedBookingDetails();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date. Use format DD-MM-YYYY.");
            } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Booking ID!");
            } catch (java.sql.SQLSyntaxErrorException ex) {
                JOptionPane.showMessageDialog(this, "Database column mismatch.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.");
            }
        }

        if (e.getSource() == bclear) {
            tpaymentDate.setText("");
            cbPaymentMode.setSelectedIndex(0);
            cbPaymentStatus.setSelectedIndex(0);
            if (cbBookingId.getItemCount() > 0) cbBookingId.setSelectedIndex(0);
        }
    }
}
