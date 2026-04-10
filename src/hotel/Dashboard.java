package hotel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Dashboard extends JFrame implements ActionListener {

    JButton bHotel, bEmployee, bCustomer, bRoomType, bRoom, bService;
    JButton bBooking, bPayment, bServiceUsed, bMaintenance;
    JButton bViewRooms, bViewBookings, bCheckout, bExit;
    JButton bManageCustomers, bManageHotels, bManageEmployees;

    Dashboard(String title) {
        super(title);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("HOTEL MANAGEMENT SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(6, 3, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        bHotel = new JButton("Add Hotel");
        bEmployee = new JButton("Add Employee");
        bCustomer = new JButton("Add Customer");

        bRoomType = new JButton("Add Room Type");
        bRoom = new JButton("Add Room");
        bService = new JButton("Add Service");

        bBooking = new JButton("Book Room");
        bPayment = new JButton("Payment");
        bServiceUsed = new JButton("Use Service");

        bMaintenance = new JButton("Maintenance");
        bViewRooms = new JButton("View Rooms");
        bViewBookings = new JButton("View Bookings");

        bManageCustomers = new JButton("Manage Customers");
        bManageHotels = new JButton("Manage Hotels");
        bManageEmployees = new JButton("Manage Employees");

        bCheckout = new JButton("Checkout");
        bExit = new JButton("Exit");

        panel.add(bHotel);
        panel.add(bEmployee);
        panel.add(bCustomer);

        panel.add(bRoomType);
        panel.add(bRoom);
        panel.add(bService);

        panel.add(bBooking);
        panel.add(bPayment);
        panel.add(bServiceUsed);

        panel.add(bMaintenance);
        panel.add(bViewRooms);
        panel.add(bViewBookings);

        panel.add(bManageHotels);
        panel.add(bManageEmployees);
        panel.add(bManageCustomers);

        panel.add(bCheckout);
        panel.add(new JLabel("")); 
        panel.add(bExit);

        add(panel, BorderLayout.CENTER);

        bHotel.addActionListener(this);
        bEmployee.addActionListener(this);
        bCustomer.addActionListener(this);
        bRoomType.addActionListener(this);
        bRoom.addActionListener(this);
        bService.addActionListener(this);
        bBooking.addActionListener(this);
        bPayment.addActionListener(this);
        bServiceUsed.addActionListener(this);
        bMaintenance.addActionListener(this);
        bViewRooms.addActionListener(this);
        bViewBookings.addActionListener(this);
        bManageHotels.addActionListener(this);
        bManageEmployees.addActionListener(this);
        bManageCustomers.addActionListener(this);
        bCheckout.addActionListener(this);
        bExit.addActionListener(this);

        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
    if (e.getSource() == bHotel) {
        new AddHotelFrame("Add Hotel");
    }
    else if (e.getSource() == bEmployee) {
        new AddEmployeeFrame("Add Employee");
    }
    else if (e.getSource() == bCustomer) {
        new AddCustomerFrame("Add Customer");
    }
    else if (e.getSource() == bRoomType) {
        new AddRoomTypeFrame("Add Room Type");
    }
    else if (e.getSource() == bRoom) {
        new AddRoomFrame("Add Room");
    }
    else if (e.getSource() == bService) {
        new AddServiceFrame("Add Service");
    }
    else if (e.getSource() == bBooking) {
        new BookRoomFrame("Book Room");
    }
    else if (e.getSource() == bPayment) {
        new PaymentFrame("Payment");
    }
    else if (e.getSource() == bServiceUsed) {
        new ServiceUsedFrame("Use Service");
    }
    else if (e.getSource() == bMaintenance) {
        new MaintenanceFrame("Maintenance");
    }
    else if (e.getSource() == bViewRooms) {
        new ViewRoomsFrame("Manage Rooms");
    }
    else if (e.getSource() == bViewBookings) {
        new ViewBookingsFrame("Manage Bookings");
    }
    else if (e.getSource() == bManageHotels) {
        new ManageHotelsFrame("Manage Hotels");
    }
    else if (e.getSource() == bManageEmployees) {
        new ManageEmployeesFrame("Manage Employees");
    }
    else if (e.getSource() == bManageCustomers) {
        new ManageCustomersFrame("Manage Customers");
    }
    else if (e.getSource() == bCheckout) {
        new CheckoutFrame("Checkout");
    }
    else if (e.getSource() == bExit) {
        System.exit(0);
    }
}
}
