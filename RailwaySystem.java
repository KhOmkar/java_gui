import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
            if (!user.isEmpty()) {
                system.frame.dispose();
                new DashboardFrame(user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(system.frame, "Please enter username.");
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
            JButton bookTicketButton = new JButton("Book Ticket");
            JButton cancelTicketButton = new JButton("Cancel Ticket");
            JButton searchTrainButton = new JButton("Search Train");
            JButton logoutButton = new JButton("Logout");

            buttonPanel.add(viewTrainsButton);
            buttonPanel.add(bookTicketButton);
            buttonPanel.add(cancelTicketButton);
            buttonPanel.add(searchTrainButton);
            buttonPanel.add(logoutButton);

            add(buttonPanel, BorderLayout.CENTER);

            viewTrainsButton.addActionListener(e -> new TrainScheduleFrame().setVisible(true));
            bookTicketButton.addActionListener(e -> new BookTicketFrame().setVisible(true));
            cancelTicketButton.addActionListener(e -> new CancelTicketFrame().setVisible(true));
            searchTrainButton.addActionListener(e -> new SearchTrainFrame().setVisible(true));
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
            String[][] data = {
                {"Rajdhani Express", "12951", "Mumbai", "Delhi", "16:30", "08:35"},
                {"Shatabdi Express", "12001", "New Delhi", "Bhopal", "06:00", "14:00"},
                {"Duronto Express", "12262", "Howrah", "Mumbai", "20:10", "11:15"},
                {"Garib Rath", "12909", "Bandra", "Hazrat Nizamuddin", "16:35", "09:45"},
                {"Chennai Express", "12621", "Mumbai", "Chennai", "20:30", "19:45"}
            };

            JTable table = new JTable(new DefaultTableModel(data, columns));
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    static class BookTicketFrame extends JFrame {
        public BookTicketFrame() {
            setTitle("Book Ticket");
            setSize(400, 350);
            setLocationRelativeTo(null);
            setLayout(null);

            JLabel label = new JLabel("Book Your Train Ticket");
            label.setBounds(100, 10, 200, 25);
            add(label);

            JLabel sourceLabel = new JLabel("Start From:");
            sourceLabel.setBounds(20, 50, 100, 25);
            JTextField sourceField = new JTextField();
            sourceField.setBounds(140, 50, 200, 25);

            JLabel destLabel = new JLabel("Destination:");
            destLabel.setBounds(20, 90, 100, 25);
            JTextField destField = new JTextField();
            destField.setBounds(140, 90, 200, 25);

            JLabel dateLabel = new JLabel("Date:");
            dateLabel.setBounds(20, 130, 100, 25);
            JTextField dateField = new JTextField();
            dateField.setBounds(140, 130, 200, 25);

            JLabel classLabel = new JLabel("Class:");
            classLabel.setBounds(20, 170, 100, 25);
            JComboBox<String> classComboBox = new JComboBox<>(new String[]{"First", "Second", "Sleeper"});
            classComboBox.setBounds(140, 170, 200, 25);

            JButton nextButton = new JButton("Add Passenger");
            nextButton.setBounds(130, 220, 150, 25);
            nextButton.addActionListener(e -> {
                dispose();
                new AddPassengerFrame().setVisible(true);
            });

            add(sourceLabel);
            add(sourceField);
            add(destLabel);
            add(destField);
            add(dateLabel);
            add(dateField);
            add(classLabel);
            add(classComboBox);
            add(nextButton);
        }
    }

    static class AddPassengerFrame extends JFrame {
        public AddPassengerFrame() {
            setTitle("Add Passenger");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setLayout(null);

            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setBounds(20, 50, 100, 25);
            JTextField nameField = new JTextField();
            nameField.setBounds(140, 50, 200, 25);

            JLabel ageLabel = new JLabel("Age:");
            ageLabel.setBounds(20, 90, 100, 25);
            JTextField ageField = new JTextField();
            ageField.setBounds(140, 90, 50, 25);

            JLabel phoneLabel = new JLabel("Phone No:");
            phoneLabel.setBounds(20, 130, 100, 25);
            JTextField phoneField = new JTextField();
            phoneField.setBounds(140, 130, 150, 25);

            JButton backButton = new JButton("Back");
            backButton.setBounds(80, 190, 80, 25);
            backButton.addActionListener(e -> {
                dispose();
                new BookTicketFrame().setVisible(true);
            });

            JButton submitButton = new JButton("Submit");
            submitButton.setBounds(180, 190, 100, 25);
            submitButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Passenger Added Successfully!");
                dispose();
            });

            add(nameLabel);
            add(nameField);
            add(ageLabel);
            add(ageField);
            add(phoneLabel);
            add(phoneField);
            add(backButton);
            add(submitButton);
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

            tfPassengerId.setText("P123");
            tfScheduleId.setText("S456");
            tfStatus.setText("Booked");
            btnCancel.setEnabled(true);
        }

        private void cancelTicket() {
            tfStatus.setText("Cancelled");
            btnCancel.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Ticket ID " + tfTicketId.getText() + " has been cancelled.");
        }
    }

    static class SearchTrainFrame extends JFrame {
        public SearchTrainFrame() {
            setTitle("Search Train");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

            JLabel fromLabel = new JLabel("From:");
            JTextField fromField = new JTextField();
            JLabel toLabel = new JLabel("To:");
            JTextField toField = new JTextField();

            inputPanel.add(fromLabel);
            inputPanel.add(fromField);
            inputPanel.add(toLabel);
            inputPanel.add(toField);

            JButton searchButton = new JButton("Search");
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(inputPanel, BorderLayout.CENTER);
            topPanel.add(searchButton, BorderLayout.SOUTH);

            add(topPanel, BorderLayout.NORTH);

            String[] columns = {"Train Name", "Train No.", "From", "To", "Departure", "Arrival"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            add(scrollPane, BorderLayout.CENTER);

            String[][] data = {
                {"Rajdhani Express", "12951", "Mumbai", "Delhi", "16:30", "08:35"},
                {"Shatabdi Express", "12001", "New Delhi", "Bhopal", "06:00", "14:00"},
                {"Duronto Express", "12262", "Howrah", "Mumbai", "20:10", "11:15"},
                {"Garib Rath", "12909", "Bandra", "Hazrat Nizamuddin", "16:35", "09:45"},
                {"Chennai Express", "12621", "Mumbai", "Chennai", "20:30", "19:45"}
            };

            searchButton.addActionListener(e -> {
                tableModel.setRowCount(0); // Clear table

                String from = fromField.getText().trim().toLowerCase();
                String to = toField.getText().trim().toLowerCase();

                if (from.isEmpty() || to.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter both source and destination.");
                    return;
                }

                boolean found = false;
                for (String[] train : data) {
                    if (train[2].toLowerCase().equals(from) && train[3].toLowerCase().equals(to)) {
                        tableModel.addRow(train);
                        found = true;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(this, "No trains found for the selected route.");
                }
            });
        }
    }
}
