package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLoginForm {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/onep_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = ""; // Change to your actual DB password

    public static void showAdminLoginForm(JFrame parentFrame) {
        JFrame adminFrame = new JFrame("Admin Login");
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setSize(1000, 600);
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setResizable(false); // Disable resizing
        adminFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        adminFrame.add(new JLabel("Admin Username:"), gbc);

        gbc.gridx = 1;
        JTextField adminUsername = new JTextField(15);
        adminFrame.add(adminUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        adminFrame.add(new JLabel("Admin Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField adminPassword = new JPasswordField(15);
        adminFrame.add(adminPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton adminLoginButton = new JButton("Login");
        styleButton(adminLoginButton);
        adminFrame.add(adminLoginButton, gbc);

        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = 3;
        adminFrame.add(messageLabel, gbc);

        adminFrame.setVisible(true);

        adminFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });

        // Add action listener to login button
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = adminUsername.getText();
                String password = new String(adminPassword.getPassword());

                if (authenticateAdmin(username, password)) {
                    adminFrame.dispose();
                    AdminDashboard.showAdminDashboard(parentFrame);
                } else {
                    messageLabel.setText("Invalid username or password.");
                }
            }
        });
    }

    private static boolean authenticateAdmin(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM admin WHERE login = ? AND pass = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}