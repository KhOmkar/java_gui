import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RailwaySystem {

    JFrame frame = new JFrame();
    JPanel loginPanel = new JPanel();
    
    // Custom colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    
    // Database connection information
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mini_dbms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Omkar@23-24";

    // Get database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public RailwaySystem() {
        // Set modern look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
            // Customize Nimbus colors
            UIManager.put("nimbusBase", PRIMARY_COLOR);
            UIManager.put("nimbusBlueGrey", SECONDARY_COLOR);
            UIManager.put("control", BACKGROUND_COLOR);
            
            // Customize component colors
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.foreground", TEXT_COLOR);
            UIManager.put("Button.background", PRIMARY_COLOR);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.foreground", TEXT_COLOR);
            UIManager.put("Label.foreground", TEXT_COLOR);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.setTitle("Railway Management System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);

        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(BACKGROUND_COLOR);
        loginPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        frame.add(loginPanel);
        setupLoginPanel();
    }

    private void setupLoginPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Railway Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 30, 10);
        loginPanel.add(titleLabel, gbc);

        // Reset insets for other components
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;

        // Email field
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        styleTextField(emailField);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(emailField, gbc);

        // Password field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPanel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(20);
        styleTextField(passField);
        gbc.gridx = 0;
        gbc.gridy = 4;
        loginPanel.add(passField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        
        styleButton(loginButton);
        styleButton(signUpButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(30, 10, 10, 10);
        loginPanel.add(buttonPanel, gbc);

        // Add action listeners
        loginButton.addActionListener(e -> handleLogin(emailField.getText(), new String(passField.getPassword())));
        signUpButton.addActionListener(e -> new SignUpFrame().setVisible(true));
    }

    private void handleLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter email and password.");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM User WHERE email = ? AND password_hash = ?")) {

            stmt.setString(1, email);
            stmt.setString(2, password);

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
                frame.dispose();
                new DashboardFrame(email, userId, role).setVisible(true);
            } else {
                showError("Invalid email or password.");
            }
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Utility methods for styling components
    private static void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
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
        system.show();
    }
    
    static class SignUpFrame extends JFrame {
        private JTextField emailField, nameField, phoneField;
        private JPasswordField passwordField, confirmPasswordField;
        private JComboBox<String> genderCombo;
        private JTextField dobField;
        
        public SignUpFrame() {
            setTitle("Create New Account");
            setSize(500, 700);
            setLocationRelativeTo(null);
            
            // Set background color
            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(BACKGROUND_COLOR);
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);
            
            // Title
            JLabel titleLabel = new JLabel("Create New Account", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(PRIMARY_COLOR);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 30, 10);
            mainPanel.add(titleLabel, gbc);
            
            // Reset insets and gridwidth
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridwidth = 1;
            
            // Name field
            addFormField(mainPanel, "Full Name:", nameField = new JTextField(20), gbc, 1);
            
            // Email field
            addFormField(mainPanel, "Email Address:", emailField = new JTextField(20), gbc, 3);
            
            // Phone field
            addFormField(mainPanel, "Phone Number:", phoneField = new JTextField(20), gbc, 5);
            
            // DOB field
            dobField = new JTextField(20);
            dobField.setText("YYYY-MM-DD");
            addFormField(mainPanel, "Date of Birth:", dobField, gbc, 7);
            
            // Gender combo
            genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
            styleComboBox(genderCombo);
            addFormField(mainPanel, "Gender:", genderCombo, gbc, 9);
            
            // Password fields
            addFormField(mainPanel, "Password:", passwordField = new JPasswordField(20), gbc, 11);
            addFormField(mainPanel, "Confirm Password:", confirmPasswordField = new JPasswordField(20), gbc, 13);
            
            // Register button
            JButton registerButton = new JButton("Create Account");
            styleButton(registerButton);
            gbc.gridx = 0;
            gbc.gridy = 15;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(30, 10, 10, 10);
            mainPanel.add(registerButton, gbc);
            
            // Style text fields
            styleTextField(nameField);
            styleTextField(emailField);
            styleTextField(phoneField);
            styleTextField(dobField);
            styleTextField(passwordField);
            styleTextField(confirmPasswordField);
            
            registerButton.addActionListener(e -> registerUser());
            
            // Add main panel to frame
            add(mainPanel);
        }
        
        private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int gridy) {
            JLabel label = new JLabel(labelText);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setForeground(TEXT_COLOR);
            
            gbc.gridx = 0;
            gbc.gridy = gridy;
            panel.add(label, gbc);
            
            gbc.gridy = gridy + 1;
            panel.add(field, gbc);
        }
        
        private static void styleComboBox(JComboBox<?> comboBox) {
            comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            comboBox.setBackground(Color.WHITE);
            comboBox.setForeground(TEXT_COLOR);
            comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
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
                showError("Please fill all required fields.");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match.");
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
                            
                            ustmt.setInt(1, passengerId);
                            ustmt.setString(2, email);
                            ustmt.setString(3, password);
                            ustmt.setString(4, "passenger");
                            
                            ustmt.executeUpdate();
                            JOptionPane.showMessageDialog(this, 
                                "Registration successful! You can now login.", 
                                "Success", 
                                JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                    }
                }
            } catch (SQLException ex) {
                showError("Error registering user: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        
        private void showError(String message) {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class DashboardFrame extends JFrame {
        private int userId;
        private String userRole;
        
        public DashboardFrame(String username, int userId, String userRole) {
            this.userId = userId;
            this.userRole = userRole;
            
            setTitle("Dashboard - Railway Management System");
            setSize(900, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Create main panel with background color
            JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
            mainPanel.setBackground(BACKGROUND_COLOR);
            mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
            
            // Welcome panel
            JPanel welcomePanel = new JPanel(new BorderLayout());
            welcomePanel.setBackground(BACKGROUND_COLOR);
            
            JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            welcomeLabel.setForeground(PRIMARY_COLOR);
            welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            
            JLabel roleLabel = new JLabel("Role: " + userRole, SwingConstants.CENTER);
            roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            roleLabel.setForeground(SECONDARY_COLOR);
            
            welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
            welcomePanel.add(roleLabel, BorderLayout.SOUTH);
            
            // Button panel with grid layout
            JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
            buttonPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            
            // Create and style buttons
            JButton viewTrainsButton = createDashboardButton("View Train Schedule", "📅");
            JButton bookTicketButton = createDashboardButton("Book Ticket", "🎫");
            JButton cancelTicketButton = createDashboardButton("Cancel Ticket", "❌");
            JButton searchTrainButton = createDashboardButton("Search Train", "🔍");
            JButton myTicketsButton = createDashboardButton("My Tickets", "📋");
            JButton logoutButton = createDashboardButton("Logout", "🚪");
            
            // Add buttons to panel
            buttonPanel.add(viewTrainsButton);
            buttonPanel.add(bookTicketButton);
            buttonPanel.add(cancelTicketButton);
            buttonPanel.add(searchTrainButton);
            buttonPanel.add(myTicketsButton);
            buttonPanel.add(logoutButton);
            
            // Add components to main panel
            mainPanel.add(welcomePanel, BorderLayout.NORTH);
            mainPanel.add(buttonPanel, BorderLayout.CENTER);
            
            // Add action listeners
            viewTrainsButton.addActionListener(e -> new TrainScheduleFrame().setVisible(true));
            bookTicketButton.addActionListener(e -> new BookTicketFrame(userId).setVisible(true));
            cancelTicketButton.addActionListener(e -> new CancelTicketFrame(userId).setVisible(true));
            searchTrainButton.addActionListener(e -> new SearchTrainFrame().setVisible(true));
            myTicketsButton.addActionListener(e -> new MyTicketsFrame(userId).setVisible(true));
            logoutButton.addActionListener(e -> {
                dispose();
                new RailwaySystem().show();
            });
            
            // Add main panel to frame
            add(mainPanel);
        }
        
        private JButton createDashboardButton(String text, String emoji) {
            JButton button = new JButton("<html><center>" + emoji + "<br>" + text + "</center></html>");
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setForeground(Color.WHITE);
            button.setBackground(PRIMARY_COLOR);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
            ));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Add hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(SECONDARY_COLOR);
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(PRIMARY_COLOR);
                }
            });
            
            return button;
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
            setSize(1000, 600);
            setLocationRelativeTo(null);
            
            // Create main panel
            JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
            mainPanel.setBackground(BACKGROUND_COLOR);
            mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
            
            // Title panel
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(BACKGROUND_COLOR);
            
            JLabel titleLabel = new JLabel("Train Schedule", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(PRIMARY_COLOR);
            titlePanel.add(titleLabel, BorderLayout.CENTER);
            
            // Table panel
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setBackground(BACKGROUND_COLOR);
            
            String[] columns = {"Schedule ID", "Train Name", "Train ID", "From", "To", "Departure", "Arrival", "Platform"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            JTable table = new JTable(model);
            styleTable(table);
            
            // Scroll pane for table
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
            scrollPane.getViewport().setBackground(Color.WHITE);
            
            tablePanel.add(scrollPane, BorderLayout.CENTER);
            
            // Add panels to main panel
            mainPanel.add(titlePanel, BorderLayout.NORTH);
            mainPanel.add(tablePanel, BorderLayout.CENTER);
            
            // Add main panel to frame
            add(mainPanel);
            
            // Load data
            loadSchedules(model);
        }
        
        private void styleTable(JTable table) {
            // Header styling
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setBackground(PRIMARY_COLOR);
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setPreferredSize(new Dimension(0, 40));
            
            // Row styling
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setRowHeight(35);
            table.setGridColor(new Color(230, 230, 230));
            table.setShowVerticalLines(true);
            table.setShowHorizontalLines(true);
            
            // Selection styling
            table.setSelectionBackground(new Color(232, 241, 249));
            table.setSelectionForeground(TEXT_COLOR);
            
            // Alternating row colors
            table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value,
                            isSelected, hasFocus, row, column);
                    
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    }
                    
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    return c;
                }
            });
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
                    "JOIN Station st2 ON s.dest_station_id = st2.station_id " +
                    "ORDER BY s.departure_time")) {
                
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
                showError("Error loading schedules: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        
        private void showError(String message) {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class BookTicketFrame extends JFrame {
        private int passengerId;
        private JComboBox<String> sourceCombo, destCombo;
        private JTextField dateField;
        private JComboBox<String> classComboBox;
        private int selectedScheduleId = -1;
        private JTable availableTrainsTable;
        private DefaultTableModel tableModel;
        
        public BookTicketFrame(int passengerId) {
            this.passengerId = passengerId;
            setTitle("Book Ticket");
            setSize(900, 500);
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
            
            // Table for available trains with updated columns
            String[] columns = {"Schedule ID", "Train Name", "From", "To", "Departure", "Arrival", "Platform", "Train Type", "Available Seats"};
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
                    "SELECT DISTINCT s.schedule_id, t.train_name, " +
                    "st1.station_name as origin, st2.station_name as destination, " +
                    "s.departure_time, s.arrival_time, s.platform_number, " +
                    "t.train_type, " +
                    "(SELECT COUNT(*) FROM SeatAvailability sa " +
                    "WHERE sa.schedule_id = s.schedule_id AND sa.is_available = true) as available_seats " +
                    "FROM Schedule s " +
                    "JOIN Train t ON s.train_id = t.train_id " +
                    "JOIN Station st1 ON s.origin_station_id = st1.station_id " +
                    "JOIN Station st2 ON s.dest_station_id = st2.station_id " +
                    "LEFT JOIN SeatAvailability sa ON s.schedule_id = sa.schedule_id " +
                    "WHERE s.origin_station_id = ? AND s.dest_station_id = ? " +
                    "AND DATE(s.departure_time) = ? " +
                    "GROUP BY s.schedule_id " +
                    "HAVING available_seats > 0")) {
                
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
                        rs.getInt("platform_number"),
                        rs.getString("train_type"),
                        rs.getInt("available_seats")
                    });
                    found = true;
                }
                
                if (!found) {
                    JOptionPane.showMessageDialog(this, "No trains found with available seats for the selected route and date.");
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