// JDBC-connected Railway System Java GUI (Database Only)


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RailwaySystem {

    JFrame frame = new JFrame();
    JPanel loginPanel = new JPanel();

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
        RailwaySystem system = new RailwaySystem();

        JLabel emailLabel = new JLabel("User Email:");
        emailLabel.setBounds(20, 70, 120, 25);
        emailLabel.setFont(new Font("Serif", Font.ITALIC, 16));

        JTextField emailField = new JTextField();
        emailField.setBounds(140, 70, 200, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 110, 100, 25);
        passLabel.setFont(new Font("Serif", Font.ITALIC, 16));

        JTextField passField = new JTextField();
        passField.setBounds(140, 110, 200, 25);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 200, 80, 25);

        JButton signUpButton = new JButton("SignUp");
        signUpButton.setBounds(250, 200, 80, 25);

        loginButton.addActionListener(e -> {
            String user = emailField.getText();
            String pass = passField.getText();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(system.frame, "Please fill all fields.");
                return;
            }
            try (Connection con = DBConnection.getConnection()) {
                String query = "SELECT * FROM User WHERE email = ? AND password_hash = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, user);
                stmt.setString(2, pass);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    system.frame.dispose();
                    new DashboardFrame(user).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(system.frame, "Invalid credentials!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(system.frame, "Database error: " + ex.getMessage());
            }
        });

        signUpButton.addActionListener(e -> {
            String user = emailField.getText();
            String pass = passField.getText();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(system.frame, "Please enter email and password to sign up.");
                return;
            }
            try (Connection con = DBConnection.getConnection()) {
                String query = "INSERT INTO User (email, password_hash, user_role) VALUES (?, ?, 'passenger')";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, user);
                stmt.setString(2, pass);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(system.frame, "User registered successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(system.frame, "Error: " + ex.getMessage());
            }
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

    static class DBConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/mini_dbms";
        private static final String USER = "root";
        private static final String PASSWORD = "Omkar@23-24";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    static class DashboardFrame extends JFrame {
        public DashboardFrame(String username) {
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
            JButton cancelTicketButton = new JButton("Cancel Ticket");
            JButton logoutButton = new JButton("Logout");

            buttonPanel.add(viewTrainsButton);
            buttonPanel.add(cancelTicketButton);
            buttonPanel.add(logoutButton);

            add(buttonPanel, BorderLayout.CENTER);

            viewTrainsButton.addActionListener(e -> new TrainScheduleFrame().setVisible(true));
            cancelTicketButton.addActionListener(e -> new CancelTicketFrame().setVisible(true));
            logoutButton.addActionListener(e -> {
                dispose();
                new RailwaySystem().show();
            });
        }
    }

    static class TrainScheduleFrame extends JFrame {
        public TrainScheduleFrame() {
            setTitle("Train Schedule");
            setSize(600, 300);
            setLocationRelativeTo(null);

            String[] columns = {"Train Name", "Train No.", "From", "To", "Departure", "Arrival"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable table = new JTable(model);

            try (Connection con = DBConnection.getConnection()) {
                String query = "SELECT t.train_name, t.train_id, s1.station_name AS origin, " +
                               "s2.station_name AS destination, sc.departure_time, sc.arrival_time " +
                               "FROM Schedule sc JOIN Train t ON sc.train_id = t.train_id " +
                               "JOIN Station s1 ON sc.origin_station_id = s1.station_id " +
                               "JOIN Station s2 ON sc.dest_station_id = s2.station_id";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("train_name"),
                        rs.getInt("train_id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getTimestamp("departure_time"),
                        rs.getTimestamp("arrival_time")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading schedule.");
            }

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    static class CancelTicketFrame extends JFrame {

        private JTextField tfTicketId, tfPassengerId, tfScheduleId, tfStatus;
        private JButton btnSearch, btnCancel;

        public CancelTicketFrame() {
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
            try (Connection con = DBConnection.getConnection()) {
                String query = "SELECT passenger_id, schedule_id, ticket_status FROM Ticket WHERE ticket_id = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(ticketId));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    tfPassengerId.setText(rs.getString("passenger_id"));
                    tfScheduleId.setText(rs.getString("schedule_id"));
                    tfStatus.setText(rs.getString("ticket_status"));
                    btnCancel.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Ticket not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error.");
            }
        }

        private void cancelTicket() {
            try (Connection con = DBConnection.getConnection()) {
                String update = "UPDATE Ticket SET ticket_status = 'Cancelled' WHERE ticket_id = ?";
                PreparedStatement stmt = con.prepareStatement(update);
                stmt.setInt(1, Integer.parseInt(tfTicketId.getText()));
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    tfStatus.setText("Cancelled");
                    btnCancel.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "Ticket cancelled.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Cancellation failed.");
            }
        }
    }
}
