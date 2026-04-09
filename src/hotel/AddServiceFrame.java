package hotel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddServiceFrame extends JFrame implements ActionListener {

    JTextField tserviceName, tprice;
    JButton bsave, bclear;

    AddServiceFrame(String title) {
        super(title);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(245, 245, 245));
        outerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setPreferredSize(new Dimension(650, 400));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("ADD SERVICE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lserviceName = new JLabel("Service Name:");
        JLabel lprice = new JLabel("Price:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        lserviceName.setFont(labelFont);
        lprice.setFont(labelFont);

        tserviceName = new JTextField(18);
        tprice = new JTextField(18);

        tserviceName.setFont(fieldFont);
        tprice.setFont(fieldFont);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lserviceName, gbc);
        gbc.gridx = 1; formPanel.add(tserviceName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lprice, gbc);
        gbc.gridx = 1; formPanel.add(tprice, gbc);

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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bsave) {
            try {
                String serviceName = tserviceName.getText();
                double price = Double.parseDouble(tprice.getText());

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO service (service_name, price) VALUES (?, ?)";
                PreparedStatement pst = con.prepareStatement(query);

                pst.setString(1, serviceName);
                pst.setDouble(2, price);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Service added successfully!");

                tserviceName.setText("");
                tprice.setText("");

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        if (e.getSource() == bclear) {
            tserviceName.setText("");
            tprice.setText("");
        }
    }
}