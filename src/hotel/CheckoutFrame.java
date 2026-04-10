package hotel;


import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CheckoutFrame extends JFrame implements ActionListener {

    JTextField tbookingId;
    JButton bcheckout, bclear;

    CheckoutFrame(String title) {
        super(title);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(245, 245, 245));
        outerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(650, 300));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("CHECKOUT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbookingId = new JLabel("Booking ID:");
        lbookingId.setFont(new Font("Arial", Font.PLAIN, 16));

        tbookingId = new JTextField(18);
        tbookingId.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lbookingId, gbc);

        gbc.gridx = 1;
        formPanel.add(tbookingId, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        bcheckout = new JButton("Checkout");
        bclear = new JButton("Clear");

        buttonPanel.add(bcheckout);
        buttonPanel.add(bclear);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        outerPanel.add(mainPanel);

        JScrollPane scrollPane = new JScrollPane(outerPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        bcheckout.addActionListener(this);
        bclear.addActionListener(this);

        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bcheckout) {
            try {
                int bookingId = Integer.parseInt(tbookingId.getText());

                Connection con = DBConnection.getConnection();

                String findRoomQuery = "SELECT room_id FROM booking WHERE booking_id = ?";
                PreparedStatement pst1 = con.prepareStatement(findRoomQuery);
                pst1.setInt(1, bookingId);
                ResultSet rs = pst1.executeQuery();

                int roomId = -1;
                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                } else {
                    JOptionPane.showMessageDialog(this, "Booking ID not found!");
                    con.close();
                    return;
                }

                String updateBooking = "UPDATE booking SET booking_status = 'Checked Out' WHERE booking_id = ?";
                PreparedStatement pst2 = con.prepareStatement(updateBooking);
                pst2.setInt(1, bookingId);
                pst2.executeUpdate();

                String updateRoom = "UPDATE room SET status = 'Available' WHERE room_id = ?";
                PreparedStatement pst3 = con.prepareStatement(updateRoom);
                pst3.setInt(1, roomId);
                pst3.executeUpdate();

                JOptionPane.showMessageDialog(this, "Checkout completed successfully!");

                tbookingId.setText("");
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        if (e.getSource() == bclear) {
            tbookingId.setText("");
        }
    }
}
