package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgentDashboard extends JFrame {
    private JTextField taskField;
    private JTextArea taskDescriptionArea;

    public AgentDashboard(String username) {
        setTitle("Agent Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        add(formPanel, BorderLayout.CENTER);

        formPanel.add(new JLabel("Task:"));
        taskField = new JTextField();
        formPanel.add(taskField);

        formPanel.add(new JLabel("Task Description:"));
        taskDescriptionArea = new JTextArea(5, 20);
        formPanel.add(new JScrollPane(taskDescriptionArea));

        JButton submitButton = new JButton("Submit Task");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String task = taskField.getText();
                String taskDescription = taskDescriptionArea.getText();

                if (task.isEmpty() || taskDescription.isEmpty()) {
                    JOptionPane.showMessageDialog(AgentDashboard.this,
                            "Please fill in all fields.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Insert the task into the database
                    insertTaskIntoDatabase(username, task, taskDescription);
                }
            }
        });

        add(submitButton, BorderLayout.SOUTH);
    }

    private void insertTaskIntoDatabase(String username, String task, String taskDescription) {
        String url = "jdbc:mysql://localhost:3306/onep_db";
        String dbUsername = "root"; // Change to your database username
        String dbPassword = ""; // Change to your database password

        String insertSQL = "INSERT INTO tasks (username, task, taskDescription) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, task);
            pstmt.setString(3, taskDescription);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Task submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void showAgentDashboard(String username) {
        SwingUtilities.invokeLater(() -> {
            AgentDashboard agentDashboard = new AgentDashboard(username);
            agentDashboard.setVisible(true);
        });
    }
}
