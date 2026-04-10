package hotel;


import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddEmployeeFrame extends JFrame implements ActionListener {

    JTextField tname, tdesignation, tphone, tsalary;
    JComboBox<String> cbHotelId;
    JButton bsave, bclear;

    AddEmployeeFrame(String title) {
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

        JLabel titleLabel = new JLabel("ADD EMPLOYEE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lname = new JLabel("Name:");
        JLabel ldesignation = new JLabel("Designation:");
        JLabel lphone = new JLabel("Phone:");
        JLabel lsalary = new JLabel("Salary:");
        JLabel lhotelId = new JLabel("Hotel:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        lname.setFont(labelFont);
        ldesignation.setFont(labelFont);
        lphone.setFont(labelFont);
        lsalary.setFont(labelFont);
        lhotelId.setFont(labelFont);

        tname = new JTextField(18);
        tdesignation = new JTextField(18);
        tphone = new JTextField(18);
        tsalary = new JTextField(18);
        cbHotelId = new JComboBox<>();

        tname.setFont(fieldFont);
        tdesignation.setFont(fieldFont);
        tphone.setFont(fieldFont);
        tsalary.setFont(fieldFont);
        cbHotelId.setFont(fieldFont);

        loadHotels();

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lname, gbc);
        gbc.gridx = 1;
        formPanel.add(tname, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(ldesignation, gbc);
        gbc.gridx = 1;
        formPanel.add(tdesignation, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lphone, gbc);
        gbc.gridx = 1;
        formPanel.add(tphone, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lsalary, gbc);
        gbc.gridx = 1;
        formPanel.add(tsalary, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lhotelId, gbc);
        gbc.gridx = 1;
        formPanel.add(cbHotelId, gbc);

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

    private void loadHotels() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT hotel_id, name FROM hotel ORDER BY hotel_id";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            cbHotelId.removeAllItems();
            while (rs.next()) {
                cbHotelId.addItem(rs.getInt("hotel_id") + " - " + rs.getString("name"));
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading hotels: " + ex.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bsave) {
            try {
                if (tname.getText().trim().isEmpty() ||
                    tdesignation.getText().trim().isEmpty() ||
                    tphone.getText().trim().isEmpty() ||
                    tsalary.getText().trim().isEmpty() ||
                    cbHotelId.getSelectedItem() == null) {

                    JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    return;
                }

                String name = tname.getText().trim();
                String designation = tdesignation.getText().trim();
                String phone = tphone.getText().trim();
                double salary;
                try {
                	salary = Double.parseDouble(tsalary.getText().trim());
                } catch (NumberFormatException ex) {
                	JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for Salary.");
                    return;
                }

                if (!Validator.isValidPhone(phone)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone. Must be 10 digits starting with 7, 8, or 9.");
                    return;
                }
                if (!Validator.isValidSalary(salary)) {
                    JOptionPane.showMessageDialog(this, "Invalid Salary. Must be greater than or equal to 0.");
                    return;
                }

                String hotelItem = (String) cbHotelId.getSelectedItem();
                int hotelId = Integer.parseInt(hotelItem.split(" - ")[0]);

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO employee (name, designation, phone, salary, hotel_id) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);

                pst.setString(1, name);
                pst.setString(2, designation);
                pst.setString(3, phone);
                pst.setDouble(4, salary);
                pst.setInt(5, hotelId);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Employee added successfully!");

                tname.setText("");
                tdesignation.setText("");
                tphone.setText("");
                tsalary.setText("");
                if (cbHotelId.getItemCount() > 0) cbHotelId.setSelectedIndex(0);

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        if (e.getSource() == bclear) {
            tname.setText("");
            tdesignation.setText("");
            tphone.setText("");
            tsalary.setText("");
            if (cbHotelId.getItemCount() > 0) cbHotelId.setSelectedIndex(0);
        }
    }
}
