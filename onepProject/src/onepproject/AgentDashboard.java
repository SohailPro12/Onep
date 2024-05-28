package onepproject;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AgentDashboard extends JFrame {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/onep_db";
    private static final String DATABASE_USER = "root";  // MySQL username
    private static final String DATABASE_PASSWORD = "";  // MySQL password

    // Method to retrieve task data from the database
    private Object[][] getTaskDataFromDatabase() {
        String query = "SELECT id, Titre, Description, sup FROM tache";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery(query)) {

            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();

            Object[][] data = new Object[rowCount][4]; // 4 columns in the table
            int row = 0;
            while (resultSet.next()) {
                data[row][0] = resultSet.getInt("id");
                data[row][1] = resultSet.getString("Titre");
                data[row][2] = resultSet.getString("Description");
                data[row][3] = resultSet.getString("sup");
                row++;
            }
            return data;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching data from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    // Method to insert comment into the database
    private void insertCommentIntoDatabase(int taskId, String comment, String progression, String agent) {
        // Calculate numeric progression based on the progression label
        int numericProgression = calculateProgression(progression);

        String query = "INSERT INTO commentaires (comment, Agent, Id_Tache, progression) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment);
            preparedStatement.setString(2, agent);
            preparedStatement.setInt(3, taskId);
            preparedStatement.setInt(4, numericProgression); // Insert numeric progression
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Comment sent successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error inserting comment into database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Method to calculate numeric progression based on the progression label
    private int calculateProgression(String progressionLabel) {
        switch (progressionLabel) {
            case "Not Started":
                return 0;
            case "In Progress":
                return 50;
            case "Completed":
                return 100;
            default:
                return 0; // Default to Not Started if unknown
        }
    }

    // Constructor
    public AgentDashboard(String username) {
        setTitle("Agent Dashboard - Welcome " + username);
        setSize(1000, 600); // Increased size to accommodate the table and buttons
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Agent Dashboard", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Task ID", "Title", "Description", "Superieur"};
        Object[][] data = getTaskDataFromDatabase();

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make no columns editable
            }
        };
        JTable taskTable = new JTable(tableModel);

        // Set table properties
        taskTable.setFillsViewportHeight(true);
        taskTable.setPreferredScrollableViewportSize(new Dimension(800, 300));

        JScrollPane scrollPane = new JScrollPane(taskTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 10));

        // Sub panel for fields
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        // Comment text field
        JLabel commentLabel = new JLabel("Comment:");
        JTextField commentField = new JTextField();
        commentField.setPreferredSize(new Dimension(100, 25));
        fieldsPanel.add(commentLabel);
        fieldsPanel.add(commentField);

        // Progression combobox
        JLabel progressionLabel = new JLabel("Progression:");
        String[] progressionOptions = {"Not Started", "In Progress", "Completed"};
        JComboBox<String> progressionComboBox = new JComboBox<>(progressionOptions);
        fieldsPanel.add(progressionLabel);
        fieldsPanel.add(progressionComboBox);

        buttonPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsSubPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Modifier button
        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    String taskId = taskTable.getValueAt(selectedRow, 0).toString();
                    String title = taskTable.getValueAt(selectedRow, 1).toString();
                    String description = taskTable.getValueAt(selectedRow, 2).toString();
                    String superieur = taskTable.getValueAt(selectedRow, 3).toString();
                    String comment = commentField.getText();
                    String progression = progressionComboBox.getSelectedItem().toString();
                    JOptionPane.showMessageDialog(null, "Modify Task ID: " + taskId + "\nTitle: " + title + "\nDescription: " + description + "\nSuperieur: " + superieur + "\nComment: " + comment + "\nProgression: " + progression);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a task to modify.");
                }
            }
        });
        buttonsSubPanel.add(modifierButton);

        // Send Comment button
        JButton sendCommentButton = new JButton("Send Comment");
        sendCommentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    int taskId = Integer.parseInt(taskTable.getValueAt(selectedRow, 0).toString());
                    String comment = commentField.getText();
                    String progression = progressionComboBox.getSelectedItem().toString();
                    insertCommentIntoDatabase(taskId, comment, progression, username);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a task to comment on.");
                }
            }
        });
        buttonsSubPanel.add(sendCommentButton);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Implement logout logic here
                // For example, showing the login form
                UserLoginForm.showUserLoginForm(new JFrame());
            }
        });
        buttonsSubPanel.add(logoutButton);

        buttonPanel.add(buttonsSubPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    public static void showAgentDashboard(String username) {
        SwingUtilities.invokeLater(() -> new AgentDashboard(username));
    }

    public static void main(String[] args) {
        showAgentDashboard("sohail"); // Example username
    }
}
