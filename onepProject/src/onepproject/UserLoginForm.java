package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserLoginForm {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/onep_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = ""; // Change to your actual DB password

    public static void showUserLoginForm(JFrame parentFrame) {
        JFrame userFrame = new JFrame("User Login");
        userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userFrame.setSize(400, 300);
        userFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        userFrame.add(new JLabel("User Username:"), gbc);

        gbc.gridx = 1;
        JTextField userUsername = new JTextField(15);
        userFrame.add(userUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        userFrame.add(new JLabel("User Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField userPassword = new JPasswordField(15);
        userFrame.add(userPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton userLoginButton = new JButton("Login");
        userFrame.add(userLoginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton forgotPasswordButton = new JButton("Forgot Password");
        userFrame.add(forgotPasswordButton, gbc);

        JLabel messageLabel = new JLabel("");
        gbc.gridx = 1;
        gbc.gridy = 4;
        userFrame.add(messageLabel, gbc);

        userFrame.setVisible(true);

        userLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userUsername.getText();
                String password = new String(userPassword.getPassword());
                if (authenticateUser(username, password)) {
                    messageLabel.setText("Login successful!");
                    userFrame.dispose();
                    showUserDashboard(username);
                } else {
                    messageLabel.setText("Invalid username or password.");
                }
            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userFrame.dispose();
                showForgotPasswordForm(parentFrame);
            }
        });

        userFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }

    private static boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM Agent WHERE login = ? AND pass = ? UNION SELECT * FROM superieur WHERE login = ? AND pass = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, username);
                stmt.setString(4, password);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void showUserDashboard(String username) {
        String role = getUserRole(username);
        if (role.equals("superieur")) {
            SuperieurDashboard.showSuperieurDashboard(new JFrame(), username);
        } else if (role.equals("agent")) {
            showAgentDashboard();
        } else {
            JOptionPane.showMessageDialog(null, "Unknown user role. Please contact the administrator.");
        }
    }

    private static String getUserRole(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT 'superieur' AS role FROM superieur WHERE login = ? UNION SELECT 'agent' AS role FROM Agent WHERE login = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void showAgentDashboard() {
        JFrame dashboardFrame = new JFrame("Agent Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setSize(400, 300);
        dashboardFrame.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Agent Dashboard", SwingConstants.CENTER);
        dashboardFrame.add(welcomeLabel, BorderLayout.CENTER);

        dashboardFrame.setVisible(true);
    }

    private static void showForgotPasswordForm(JFrame parentFrame) {
        JFrame forgotPasswordFrame = new JFrame("Forgot Password");
        forgotPasswordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        forgotPasswordFrame.setSize(400, 400);
        forgotPasswordFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        forgotPasswordFrame.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        forgotPasswordFrame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        forgotPasswordFrame.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(15);
        forgotPasswordFrame.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        forgotPasswordFrame.add(new JLabel("Telephone Number:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(15);
        forgotPasswordFrame.add(phoneField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton submitButton = new JButton("Submit");
        forgotPasswordFrame.add(submitButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        JButton proceedButton = new JButton("Proceed");
        forgotPasswordFrame.add(proceedButton, gbc);

        JLabel messageLabel = new JLabel("");
        gbc.gridx = 1;
        gbc.gridy = 5;
        forgotPasswordFrame.add(messageLabel, gbc);

        forgotPasswordFrame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                if (submitPasswordRecoveryRequest(username, email, phone)) {
                    messageLabel.setText("Recovery request submitted.");
                } else {
                    messageLabel.setText("Invalid username, email, or phone number.");
                }
            }
        });

        proceedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forgotPasswordFrame.dispose();
                showRecoveryCodeForm(parentFrame);
            }
        });

        forgotPasswordFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }

    private static boolean submitPasswordRecoveryRequest(String username, String email, String phone) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO recuperation_mp (login, email, numero_tel, code) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, phone);
                stmt.setString(4, generateRecoveryCode());
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String generateRecoveryCode() {
        // Generate a random recovery code
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

    private static void showRecoveryCodeForm(JFrame parentFrame) {
        JFrame recoveryCodeFrame = new JFrame("Enter Recovery Code");
        recoveryCodeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        recoveryCodeFrame.setSize(400, 300);
        recoveryCodeFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        recoveryCodeFrame.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        recoveryCodeFrame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        recoveryCodeFrame.add(new JLabel("Recovery Code:"), gbc);

        gbc.gridx = 1;
        JTextField codeField = new JTextField(15);
        recoveryCodeFrame.add(codeField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton verifyButton = new JButton("Verify");
        recoveryCodeFrame.add(verifyButton, gbc);

        JLabel messageLabel = new JLabel("");
        gbc.gridx = 1;
        gbc.gridy = 3;
        recoveryCodeFrame.add(messageLabel, gbc);

        recoveryCodeFrame.setVisible(true);

        verifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String code = codeField.getText();
                if (verifyRecoveryCode(username, code)) {
                    messageLabel.setText("Code verified!");
                    recoveryCodeFrame.dispose();
                    showResetPasswordForm(parentFrame, username);
                } else {
                    messageLabel.setText("Invalid username or recovery code.");
                }
            }
        });

        recoveryCodeFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }

    private static boolean verifyRecoveryCode(String username, String code) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM recuperation_mp WHERE login = ? AND code = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, code);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void showResetPasswordForm(JFrame parentFrame, String username) {
        JFrame resetPasswordFrame = new JFrame("Reset Password");
        resetPasswordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resetPasswordFrame.setSize(400, 300);
        resetPasswordFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        resetPasswordFrame.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField newPasswordField = new JPasswordField(15);
        resetPasswordFrame.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        resetPasswordFrame.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField confirmPasswordField = new JPasswordField(15);
        resetPasswordFrame.add(confirmPasswordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton resetButton = new JButton("Reset");
        resetPasswordFrame.add(resetButton, gbc);

        JLabel messageLabel = new JLabel("");
        gbc.gridx = 1;
        gbc.gridy = 3;
        resetPasswordFrame.add(messageLabel, gbc);

        resetPasswordFrame.setVisible(true);

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                if (newPassword.equals(confirmPassword)) {
                    if (resetPassword(username, newPassword)) {
                        messageLabel.setText("Password reset successfully!");
                        resetPasswordFrame.dispose();
                        showUserLoginForm(parentFrame);
                    } else {
                        messageLabel.setText("Password reset failed.");
                    }
                } else {
                    messageLabel.setText("Passwords do not match.");
                }
            }
        });

        resetPasswordFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }

    private static boolean resetPassword(String username, String newPassword) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // First, check if the user is an agent or a superior
            String userTypeQuery = "SELECT 'Agent' AS userType FROM Agent WHERE login = ? UNION SELECT 'Superieur' AS userType FROM superieur WHERE login = ?";
            try (PreparedStatement userTypeStmt = conn.prepareStatement(userTypeQuery)) {
                userTypeStmt.setString(1, username);
                userTypeStmt.setString(2, username);
                ResultSet rs = userTypeStmt.executeQuery();
                if (rs.next()) {
                    String userType = rs.getString("userType");
                    String updateQuery;
                    if ("Agent".equalsIgnoreCase(userType)) {
                        updateQuery = "UPDATE Agent SET pass = ? WHERE login = ?";
                    } else {
                        updateQuery = "UPDATE superieur SET pass = ? WHERE login = ?";
                    }
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, newPassword);
                        updateStmt.setString(2, username);
                        int affectedRows = updateStmt.executeUpdate();
                        return affectedRows > 0;
                    }
                } else {
                    return false; // User not found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}