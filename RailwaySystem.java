import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RailwaySystem {

    JFrame frame = new JFrame();
    JPanel loginPanel = new JPanel();
    
    // Database connection information
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mini_dbms";
    private static final String DB_USER = "root"; // Change as per your MySQL setup
    private static final String DB_PASSWORD = "Omkar@23-24"; // Change as per your MySQL setup

    // Get database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public RailwaySystem() {
        frame.setTitle("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.LIGHT_GRAY);

        frame.add(loginPanel);
    }

    void show() {
        frame.setVisible(true);
        loginPanel.setVisible(true);
    }

    public static void main(String[] args) {
        // Load the JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found. Add it to your project.");
            e.printStackTrace();
            return;
        }
        
        RailwaySystem system = new RailwaySystem();

        JLabel emailLabel = new JLabel("User Email:");
        emailLabel.setBounds(20, 70, 120, 25);
        emailLabel.setFont(new Font("Serif", Font.ITALIC, 16));

        JTextField emailField = new JTextField();
        emailField.setBounds(140, 70, 200, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 110, 100, 25);
        passLabel.setFont(new Font("Serif", Font.ITALIC, 16));

        JPasswordField passField = new JPasswordField(); // Changed to password field for security
        passField.setBounds(140, 110, 200, 25);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 200, 80, 25);

        JButton signUpButton = new JButton("SignUp");
        signUpButton.setBounds(250, 200, 80, 25);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(system.frame, "Please enter email and password.");
                return;
            }
            
            // Authenticate user from database
            try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM User WHERE email = ? AND password_hash = ?")) {
                
                stmt.setString(1, email);
                stmt.setString(2, password); // In real app, you should hash the password
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Update last login time
                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE User SET last_login = CURRENT_TIMESTAMP WHERE email = ?")) {
                        updateStmt.setString(1, email);
                        updateStmt.executeUpdate();
                    }
                    
                    int userId = rs.getInt("user_id");
                    String role = rs.getString("user_role");
                    system.frame.dispose();
                    new DashboardFrame(email, userId, role).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(system.frame, "Invalid email or password.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(system.frame, "Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        signUpButton.addActionListener(e -> {
            new SignUpFrame().setVisible(true);
        });

        system.loginPanel.add(emailLabel);
        system.loginPanel.add(emailField);
        system.loginPanel.add(passLabel);
        system.loginPanel.add(passField);
        system.loginPanel.add(loginButton);
        system.loginPanel.add(signUpButton);

        system.frame.add(system.loginPanel);
        system.show();
    }
    
    static class SignUpFrame extends JFrame {
        private JTextField emailField, nameField, phoneField;
        private JPasswordField passwordField, confirmPasswordField;
        private JComboBox<String> genderCombo;
        private JTextField dobField;
        
        public SignUpFrame() {
            setTitle("Create Account");
            setSize(400, 400);
            setLocationRelativeTo(null);
            setLayout(null);
            
            JLabel nameLabel = new JLabel("Full Name:");
            nameLabel.setBounds(20, 30, 100, 25);
            nameField = new JTextField();
            nameField.setBounds(140, 30, 200, 25);
            
            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setBounds(20, 70, 100, 25);
            emailField = new JTextField();
            emailField.setBounds(140, 70, 200, 25);
            
            JLabel phoneLabel = new JLabel("Phone:");
            phoneLabel.setBounds(20, 110, 100, 25);
            phoneField = new JTextField();
            phoneField.setBounds(140, 110, 200, 25);
            
            JLabel dobLabel = new JLabel("Date of Birth:");
            dobLabel.setBounds(20, 150, 100, 25);
            dobField = new JTextField("YYYY-MM-DD");
            dobField.setBounds(140, 150, 200, 25);
            
            JLabel genderLabel = new JLabel("Gender:");
            genderLabel.setBounds(20, 190, 100, 25);
            genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
            genderCombo.setBounds(140, 190, 200, 25);
            
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setBounds(20, 230, 100, 25);
            passwordField = new JPasswordField();
            passwordField.setBounds(140, 230, 200, 25);
            
            JLabel confirmLabel = new JLabel("Confirm:");
            confirmLabel.setBounds(20, 270, 100, 25);
            confirmPasswordField = new JPasswordField();
            confirmPasswordField.setBounds(140, 270, 200, 25);
            
            JButton registerButton = new JButton("Register");
            registerButton.setBounds(150, 320, 100, 25);
            registerButton.addActionListener(e -> registerUser());
            
            add(nameLabel);
            add(nameField);
            add(emailLabel);
            add(emailField);
            add(phoneLabel);
            add(phoneField);
            add(dobLabel);
            add(dobField);
            add(genderLabel);
            add(genderCombo);
            add(passwordLabel);
            add(passwordField);
            add(confirmLabel);
            add(confirmPasswordField);
            add(registerButton);
        }
        
        private void registerUser() {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String dob = dobField.getText();
            String gender = (String) genderCombo.getSelectedItem();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }
            
            try (Connection conn = getConnection()) {
                // First create passenger record
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Passenger (passenger_id, name, email, phone, date_of_birth, gender) VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    
                    // Generate a passenger ID
                    int passengerId = (int)(Math.random() * 10000) + 1;
                    
                    pstmt.setInt(1, passengerId);
                    pstmt.setString(2, name);
                    pstmt.setString(3, email);
                    pstmt.setString(4, phone);
                    pstmt.setString(5, dob);
                    pstmt.setString(6, gender);
                    
                    int affectedRows = pstmt.executeUpdate();
                    
                    if (affectedRows > 0) {
                        // Now create user record
                        try (PreparedStatement ustmt = conn.prepareStatement(
                                "INSERT INTO User (user_id, email, password_hash, user_role) VALUES (?, ?, ?, ?)")) {
                            
                            // In a real app, you would hash the password
                            ustmt.setInt(1, passengerId); // Use passenger_id as user_id
                            ustmt.setString(2, email);
                            ustmt.setString(3, password); // Should be hashed in production
                            ustmt.setString(4, "passenger");
                            
                            ustmt.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.");
                            dispose();
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error registering user: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    static class DashboardFrame extends JFrame {
        private int userId;
        private String userRole;
        
        public DashboardFrame(String username, int userId, String userRole) {
            this.userId = userId;
            this.userRole = userRole;
            
            setTitle("Dashboard - Railway Management System");
            setSize(500, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
            welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            add(welcomeLabel, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

            JButton viewTrainsButton = new JButton("View Train Schedule");
            JButton bookTicketButton = new JButton("Book Ticket");
            JButton cancelTicketButton = new JButton("Cancel Ticket");
            JButton searchTrainButton = new JButton("Search Train");
            JButton myTicketsButton = new JButton("My Tickets");
            JButton logoutButton = new JButton("Logout");

            buttonPanel.add(viewTrainsButton);
            buttonPanel.add(bookTicketButton);
            buttonPanel.add(cancelTicketButton);
            buttonPanel.add(searchTrainButton);
            buttonPanel.add(myTicketsButton);
            buttonPanel.add(logoutButton);

            add(buttonPanel, BorderLayout.CENTER);

            viewTrainsButton.addActionListener(e -> new TrainScheduleFrame().setVisible(true));
            bookTicketButton.addActionListener(e -> new BookTicketFrame(userId).setVisible(true));
            cancelTicketButton.addActionListener(e -> new CancelTicketFrame(userId).setVisible(true));
            searchTrainButton.addActionListener(e -> new SearchTrainFrame().setVisible(true));
            myTicketsButton.addActionListener(e -> new MyTicketsFrame(userId).setVisible(true));
            logoutButton.addActionListener(e -> {
                dispose();
                new RailwaySystem().show();
            });
        }
    }
    
    static class MyTicketsFrame extends JFrame {
        private int userId;
        
        public MyTicketsFrame(int userId) {
            this.userId = userId;
            setTitle("My Tickets");
            setSize(800, 400);
            setLocationRelativeTo(null);
            
            String[] columns = {"Ticket ID", "Schedule ID", "Train", "From", "To", "Departure", "Arrival", "Seat", "Coach", "Status"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
            
            loadTickets(model);
        }
        
        private void loadTickets(DefaultTableModel model) {
            try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT t.ticket_id, t.schedule_id, tr.train_name, " +
                    "s1.station_name as origin, s2.station_name as destination, " +
                    "sc.departure_time, sc.arrival_time, t.seat_number, t.coach_number, t.ticket_status " +
                    "FROM Ticket t " +
                    "JOIN Schedule sc ON t.schedule_id = sc.schedule_id " +
                    "JOIN Train tr ON sc.train_id = tr.train_id " +
                    "JOIN Station s1 ON sc.origin_station_id = s1.station_id " +
                    "JOIN Station s2 ON sc.dest_station_id = s2.station_id " +
                    "WHERE t.passenger_id = ?")) {
                
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("ticket_id"),
                        rs.getInt("schedule_id"),
                        rs.getString("train_name"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getTimestamp("departure_time"),
                        rs.getTimestamp("arrival_time"),
                        rs.getString("seat_number"),
                        rs.getString("coach_number"),
                        rs.getString("ticket_status")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading tickets: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    static class TrainScheduleFrame extends JFrame {
        public TrainScheduleFrame() {
            setTitle("Train Schedule");
            setSize(800, 400);
            setLocationRelativeTo(null);

            String[] columns = {"Schedule ID", "Train Name", "Train ID", "From", "To", "Departure", "Arrival", "Platform"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
            
            loadSchedules(model);
        }
        
        private void loadSchedules(DefaultTableModel model) {
            try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT s.schedule_id, t.train_name, t.train_id, " +
                    "st1.station_name as origin, st2.station_name as destination, " +
                    "s.departure_time, s.arrival_time, s.platform_number " +
                    "FROM Schedule s " +
                    "JOIN Train t ON s.train_id = t.train_id " +
                    "JOIN Station st1 ON s.origin_station_id = st1.station_id " +
                    "JOIN Station st2 ON s.dest_station_id = st2.station_id")) {
                
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("schedule_id"),
                        rs.getString("train_name"),
                        rs.getInt("train_id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getTimestamp("departure_time"),
                        rs.getTimestamp("arrival_time"),
                        rs.getInt("platform_number")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading schedules: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    static class BookTicketFrame extends JFrame {
        private int passengerId;
        private JComboBox<String> sourceCombo, destCombo, trainCombo;
        private JTextField dateField;
        private JComboBox<String> classComboBox;
        private int selectedScheduleId = -1;
        private JTable availableTrainsTable;
        private DefaultTableModel tableModel;
        
        public BookTicketFrame(int passengerId) {
            this.passengerId = passengerId;
            setTitle("Book Ticket");
            setSize(700, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JPanel topPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            sourceCombo = new JComboBox<>();
            destCombo = new JComboBox<>();
            loadStations(sourceCombo, destCombo);
            
            dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            classComboBox = new JComboBox<>(new String[]{"First", "Second", "Sleeper"});
            
            topPanel.add(new JLabel("Start From:"));
            topPanel.add(sourceCombo);
            topPanel.add(new JLabel("Destination:"));  
            topPanel.add(destCombo);
            topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
            topPanel.add(dateField);
            topPanel.add(new JLabel("Class:"));
            topPanel.add(classComboBox);
            
            JButton searchButton = new JButton("Search Trains");
            searchButton.addActionListener(e -> searchTrains());
            
            JPanel searchPanel = new JPanel();
            searchPanel.add(searchButton);
            
            JPanel northPanel = new JPanel(new BorderLayout());
            northPanel.add(topPanel, BorderLayout.CENTER);
            northPanel.add(searchPanel, BorderLayout.SOUTH);
            add(northPanel, BorderLayout.NORTH);
            
            // Table for available trains
            String[] columns = {"Schedule ID", "Train Name", "From", "To", "Departure", "Arrival", "Platform"};
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            availableTrainsTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(availableTrainsTable);
            add(scrollPane, BorderLayout.CENTER);
            
            // Selection listener for table
            availableTrainsTable.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && availableTrainsTable.getSelectedRow() != -1) {
                    selectedScheduleId = (int) availableTrainsTable.getValueAt(availableTrainsTable.getSelectedRow(), 0);
                }
            });
            
            JButton bookButton = new JButton("Book Selected Train");
            bookButton.addActionListener(e -> {
                if (selectedScheduleId != -1) {
                    showSeatSelection(selectedScheduleId);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a train first.");
                }
            });
            
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(bookButton);
            add(bottomPanel, BorderLayout.SOUTH);
        }
        
        private void loadStations(JComboBox<String> sourceCombo, JComboBox<String> destCombo) {
            try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT station_id, station_name FROM Station")) {
                
                ArrayList<StationItem> stations = new ArrayList<>();
                while (rs.next()) {
                    stations.add(new StationItem(rs.getInt("station_id"), rs.getString("station_name")));
                }
                
                for (StationItem station : stations) {
                    sourceCombo.addItem(station.toString());
                    destCombo.addItem(station.toString());
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading stations: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        
        private void searchTrains() {
            if (sourceCombo.getSelectedIndex() == -1 || destCombo.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Please select source and destination stations.");
                return;
            }
            
            // Extract station IDs from combo box items
            StationItem sourceStation = StationItem.fromString((String)sourceCombo.getSelectedItem());
            StationItem destStation = StationItem.fromString((String)destCombo.getSelectedItem());
            
            if (sourceStation.getId() == destStation.getId()) {
                JOptionPane.showMessageDialog(this, "Source and destination cannot be the same.");
                return;
            }
            
            tableModel.setRowCount(0);
            
            try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT s.schedule_id, t.train_name, " +
                    "st1.station_name as origin, st2.station_name as destination, " +
                    "s.departure_time, s.arrival_time, s.platform_number " +
                    "FROM Schedule s " +
                    "JOIN Train t ON s.train_id = t.train_id " +
                    "JOIN Station st1 ON s.origin_station_id = st1.station_id " +
                    "JOIN Station st2 ON s.dest_station_id = st2.station_id " +
                    "WHERE s.origin_station_id = ? AND s.dest_station_id = ? " +
                    "AND DATE(s.departure_time) = ?")) {
                
                stmt.setInt(1, sourceStation.getId());
                stmt.setInt(2, destStation.getId());
                stmt.setString(3, dateField.getText());
                
                ResultSet rs = stmt.executeQuery();
                
                boolean found = false;
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("schedule_id"),
                        rs.getString("train_name"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getTimestamp("departure_time"),
                        rs.getTimestamp("arrival_time"),
                        rs.getInt("platform_number")
                    });
                    found = true;
                }
                
                if (!found) {
                    JOptionPane.showMessageDialog(this, "No trains found for the selected route and date.");
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error searching trains: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        
        private void showSeatSelection(int scheduleId) {
            JDialog dialog = new JDialog(this, "Select Seat", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            
            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JComboBox<String> coachCombo = new JComboBox<>();
            JComboBox<String> seatCombo = new JComboBox<>();
            
            // Load available coaches
            try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT DISTINCT coach_number FROM SeatAvailability " +
                    "WHERE schedule_id = ? AND is_available = true")) {
                
                stmt.setInt(1, scheduleId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    coachCombo.addItem(rs.getString("coach_number"));
                }
                
                if (coachCombo.getItemCount() == 0) {
                    // If no seats found in database, add some defaults
                    coachCombo.addItem("A1");
                    coachCombo.addItem("A2");
                    coachCombo.addItem("B1");
                    coachCombo.addItem("B2");
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading coaches: " + ex.getMessage());
                ex.printStackTrace();
            }
            
            // Coach selection listener
            coachCombo.addActionListener(e -> {
                seatCombo.removeAllItems();
                String selectedCoach = (String) coachCombo.getSelectedItem();
                
                try (Connection conn = getConnection();
                    PreparedStatement stmt = conn.prepareStatement(
                        "SELECT seat_number FROM SeatAvailability " +
                        "WHERE schedule_id = ? AND coach_number = ? AND is_available = true")) {
                    
                    stmt.setInt(1, scheduleId);
                    stmt.setString(2, selectedCoach);
                    ResultSet rs = stmt.executeQuery();
                    
                    boolean seatsFound = false;
                    while (rs.next()) {
                        seatCombo.addItem(rs.getString("seat_number"));
                        seatsFound = true;
                    }
                    
                    if (!seatsFound) {
                        // If no seats found in database, add some defaults
                        for (int i = 1; i <= 20; i++) {
                            seatCombo.addItem(String.valueOf(i));
                        }
                    }
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error loading seats: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            
            // Trigger initial seat load
            if (coachCombo.getItemCount() > 0) {
                coachCombo.setSelectedIndex(0);
            }
            
            panel.add(new JLabel("Coach:"));
            panel.add(coachCombo);
            panel.add(new JLabel("Seat:"));
            panel.add(seatCombo);
            
            JTextField fareField = new JTextField("500.00"); // Example fare
            JComboBox<String> paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "UPI", "NetBanking"});
            
            panel.add(new JLabel("Fare:"));
            panel.add(fareField);
            panel.add(new JLabel("Payment Method:"));
            panel.add(paymentMethodCombo);
            
            JButton bookButton = new JButton("Confirm Booking");
            bookButton.addActionListener(e -> {
                try {
                    bookTicket(
                        scheduleId,
                        (String) coachCombo.getSelectedItem(),
                        (String) seatCombo.getSelectedItem(),
                        Double.parseDouble(fareField.getText()),
                        (String) paymentMethodCombo.getSelectedItem()
                    );
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
                }
            });
            
            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(bookButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
        }
        
        private void bookTicket(int scheduleId, String coach, String seat, double fare, String paymentMethod) {
            Connection conn = null;
            try {
                conn = getConnection();
                conn.setAutoCommit(false);
                
                // Insert ticket record
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO Ticket (ticket_id, passenger_id, schedule_id, booking_date, " +
                        "seat_number, coach_number, fare_amount, payment_methods, ticket_status) " +
                        "VALUES (?, ?, ?, CURDATE(), ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    
                    int ticketId = (int)(Math.random() * 10000) + 1;
                    
                    stmt.setInt(1, ticketId);
                    stmt.setInt(2, passengerId);
                    stmt.setInt(3, scheduleId);
                    stmt.setString(4, seat);
                    stmt.setString(5, coach);
                    stmt.setDouble(6, fare);
                    stmt.setString(7, paymentMethod);
                    stmt.setString(8, "Confirmed");
                    
                    stmt.executeUpdate();
                    
                    // Insert payment record
                    try (PreparedStatement pStmt = conn.prepareStatement(
                            "INSERT INTO Payment (ticket_id, amount, payment_date, payment_method, transaction_id, status) " +
                            "VALUES (?, ?, NOW(), ?, ?, ?)")) {
                        
                        String transactionId = "TXN" + System.currentTimeMillis();
                        
                        pStmt.setInt(1, ticketId);
                        pStmt.setDouble(2, fare);
                        pStmt.setString(3, paymentMethod);
                        pStmt.setString(4, transactionId);
                        pStmt.setString(5, "Completed");
                        
                        pStmt.executeUpdate();
                    }
                    
                    // Update seat availability
                    try (PreparedStatement sStmt = conn.prepareStatement(
                            "INSERT INTO SeatAvailability (schedule_id, coach_number, seat_number, is_available) " +
                            "VALUES (?, ?, ?, false) " +
                            "ON DUPLICATE KEY UPDATE is_available = false")) {
                        
                        sStmt.setInt(1, scheduleId);
                        sStmt.setString(2, coach);
                        sStmt.setString(3, seat);
                        
                        sStmt.executeUpdate();
                    }
                    
                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Ticket booked successfully! Ticket ID: " + ticketId);
                    dispose();
                }
            } catch (SQLException ex) {
                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "Error booking ticket: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
    
    static class StationItem {
        private int id;
        private String name;
        
        public StationItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
        
        @Override
        public String toString() {
            return id + " - " + name;
        }
        
        public static StationItem fromString(String str) {
            String[] parts = str.split(" - ", 2);
            return new StationItem(Integer.parseInt(parts[0]), parts[1]);
        }
    }

    static class CancelTicketFrame extends JFrame {
        private int userId;
        private JTextField tfTicketId;
        private JTextField tfPassengerId, tfScheduleId, tfStatus;
        private JButton btnSearch, btnCancel;

        public CancelTicketFrame(int userId) {
            this.userId = userId;
            setTitle("Cancel Ticket");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            tfTicketId = new JTextField();
            tfPassengerId = new JTextField();
            tfScheduleId = new JTextField();
            tfStatus = new JTextField();

            tfPassengerId.setEditable(false);
            tfScheduleId.setEditable(false);
            tfStatus.setEditable(false);

            btnSearch = new JButton("Search Ticket");
            btnCancel = new JButton("Cancel Ticket");
            btnCancel.setEnabled(false);

            panel.add(new JLabel("Ticket ID:"));
            panel.add(tfTicketId);
            panel.add(new JLabel("Passenger ID:"));
            panel.add(tfPassengerId);
            panel.add(new JLabel("Schedule ID:"));
            panel.add(tfScheduleId);
            panel.add(new JLabel("Ticket Status:"));
            panel.add(tfStatus);
            panel.add(btnSearch);
            panel.add(btnCancel);

            add(panel, BorderLayout.CENTER);

            btnSearch.addActionListener(e -> searchTicket());
            btnCancel.addActionListener(e -> cancelTicket());
        }

        private void searchTicket() {
            String ticketId = tfTicketId.getText();
            if (ticketId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Ticket ID.");
                return;
            }

            try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT passenger_id, schedule_id, ticket_status FROM Ticket WHERE ticket_id = ? AND passenger_id = ?")) {
                
                stmt.setInt(1, Integer.parseInt(ticketId));
                stmt.setInt(2, userId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    tfPassengerId.setText(String.valueOf(rs.getInt("passenger_id")));
                    tfScheduleId.setText(String.valueOf(rs.getInt("schedule_id")));
                    tfStatus.setText(rs.getString("ticket_status"));
                    
                    if ("Cancelled".equals(rs.getString("ticket_status"))) {
                        btnCancel.setEnabled(false);
                    } else {
                        btnCancel.setEnabled(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Ticket not found or doesn't belong to you.");
                    tfPassengerId.setText("");
                    tfScheduleId.setText("");
                    tfStatus.setText("");
                    btnCancel.setEnabled(false);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error searching ticket: " + ex.getMessage());
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Ticket ID. Please enter a number.");
            }
        }

        private void cancelTicket() {
            String ticketId = tfTicketId.getText();
            
            Connection conn = null;
            try {
                conn = getConnection();
                conn.setAutoCommit(false);
                
                // Update ticket status
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE Ticket SET ticket_status = 'Cancelled' WHERE ticket_id = ? AND passenger_id = ?")) {
                    
                    stmt.setInt(1, Integer.parseInt(ticketId));
                    stmt.setInt(2, userId);
                    
                    int updated = stmt.executeUpdate();
                    if (updated > 0) {
                        // Free up the seat
                        try (PreparedStatement seatStmt = conn.prepareStatement(
                                "UPDATE SeatAvailability SET is_available = true " +
                                "WHERE schedule_id = ? AND coach_number = ? AND seat_number = ?")) {
                            
                            // First get the seat and coach information
                            try (PreparedStatement getStmt = conn.prepareStatement(
                                    "SELECT schedule_id, coach_number, seat_number FROM Ticket WHERE ticket_id = ?")) {
                                
                                getStmt.setInt(1, Integer.parseInt(ticketId));
                                ResultSet rs = getStmt.executeQuery();
                                
                                if (rs.next()) {
                                    seatStmt.setInt(1, rs.getInt("schedule_id"));
                                    seatStmt.setString(2, rs.getString("coach_number"));
                                    seatStmt.setString(3, rs.getString("seat_number"));
                                    seatStmt.executeUpdate();
                                }
                            }
                        }
                        
                        conn.commit();
                        tfStatus.setText("Cancelled");
                        btnCancel.setEnabled(false);
                        JOptionPane.showMessageDialog(this, "Ticket ID " + ticketId + " has been cancelled.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel ticket.");
                    }
                }
            } catch (SQLException ex) {
                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "Error cancelling ticket: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    static class SearchTrainFrame extends JFrame {
        private JComboBox<String> sourceCombo, destCombo;
        
        public SearchTrainFrame() {
            setTitle("Search Train");
            setSize(800, 400);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

            sourceCombo = new JComboBox<>();
            destCombo = new JComboBox<>();
            loadStations();

            inputPanel.add(new JLabel("From:"));
            inputPanel.add(sourceCombo);
            inputPanel.add(new JLabel("To:"));
            inputPanel.add(destCombo);

            JButton searchButton = new JButton("Search");
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(inputPanel, BorderLayout.CENTER);
            topPanel.add(searchButton, BorderLayout.SOUTH);

            add(topPanel, BorderLayout.NORTH);

            String[] columns = {"Train Name", "Train No.", "From", "To", "Departure", "Arrival", "Platform"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            add(scrollPane, BorderLayout.CENTER);

            searchButton.addActionListener(e -> {
                searchTrains(tableModel);
            });
        }
        
        private void loadStations() {
            try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT station_id, station_name FROM Station")) {
                
                ArrayList<StationItem> stations = new ArrayList<>();
                while (rs.next()) {
                    stations.add(new StationItem(rs.getInt("station_id"), rs.getString("station_name")));
                }
                
                for (StationItem station : stations) {
                    sourceCombo.addItem(station.toString());
                    destCombo.addItem(station.toString());
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading stations: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        private void searchTrains(DefaultTableModel tableModel) {
            tableModel.setRowCount(0); // Clear table

            if (sourceCombo.getSelectedIndex() == -1 || destCombo.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Please select source and destination stations.");
                return;
            }
            
            // Extract station IDs from combo box items
            StationItem sourceStation = StationItem.fromString((String)sourceCombo.getSelectedItem());
            StationItem destStation = StationItem.fromString((String)destCombo.getSelectedItem());
            
            if (sourceStation.getId() == destStation.getId()) {
                JOptionPane.showMessageDialog(this, "Source and destination cannot be the same.");
                return;
            }

            try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT t.train_name, t.train_id, " +
                    "st1.station_name as origin, st2.station_name as destination, " +
                    "s.departure_time, s.arrival_time, s.platform_number " +
                    "FROM Schedule s " +
                    "JOIN Train t ON s.train_id = t.train_id " +
                    "JOIN Station st1 ON s.origin_station_id = st1.station_id " +
                    "JOIN Station st2 ON s.dest_station_id = st2.station_id " +
                    "WHERE s.origin_station_id = ? AND s.dest_station_id = ?")) {
                
                stmt.setInt(1, sourceStation.getId());
                stmt.setInt(2, destStation.getId());
                
                ResultSet rs = stmt.executeQuery();
                
                boolean found = false;
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("train_name"),
                        rs.getString("train_id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getTimestamp("departure_time"),
                        rs.getTimestamp("arrival_time"),
                        rs.getInt("platform_number")
                    });
                    found = true;
                }
                
                if (!found) {
                    JOptionPane.showMessageDialog(this, "No trains found for the selected route.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error searching trains: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
