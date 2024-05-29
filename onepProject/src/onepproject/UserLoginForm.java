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
        userFrame.setSize(1200, 800);
        userFrame.setLocationRelativeTo(null);
        userFrame.setResizable(false); // Disable resizing
        userFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        styleButton(userLoginButton);
        userFrame.add(userLoginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton forgotPasswordButton = new JButton("Forgot Password");
        styleButton(forgotPasswordButton);
        userFrame.add(forgotPasswordButton, gbc);

        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
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
                //System.out.println(rs.next());
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
            AgentDashboard.showAgentDashboard(username);
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

    private static void showForgotPasswordForm(JFrame parentFrame) {
        JFrame forgotPasswordFrame = new JFrame("Forgot Password");
        forgotPasswordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        forgotPasswordFrame.setSize(400, 400);
        forgotPasswordFrame.setLocationRelativeTo(null);
        forgotPasswordFrame.setResizable(false); // Disable resizing
        forgotPasswordFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        styleButton(submitButton);
        forgotPasswordFrame.add(submitButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        JButton proceedButton = new JButton("Proceed");
        styleButton(proceedButton);
        forgotPasswordFrame.add(proceedButton, gbc);

        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
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
        recoveryCodeFrame.setLocationRelativeTo(null);
        recoveryCodeFrame.setResizable(false); // Disable resizing
        recoveryCodeFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        styleButton(verifyButton);
        recoveryCodeFrame.add(verifyButton, gbc);

        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = 3;
        recoveryCodeFrame.add(messageLabel, gbc);

        recoveryCodeFrame.setVisible(true);

        verifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String code = codeField.getText();
                if (verifyRecoveryCode(username, code)) {
                    messageLabel.setText("Code verified. You may now reset your password.");
                    showResetPasswordForm(parentFrame, username);
                    recoveryCodeFrame.dispose();
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
        resetPasswordFrame.setLocationRelativeTo(null);
        resetPasswordFrame.setResizable(false); // Disable resizing
        resetPasswordFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        JButton resetPasswordButton = new JButton("Reset Password");
        styleButton(resetPasswordButton);
        resetPasswordFrame.add(resetPasswordButton, gbc);

        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = 3;
        resetPasswordFrame.add(messageLabel, gbc);

        resetPasswordFrame.setVisible(true);

        resetPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                if (newPassword.equals(confirmPassword)) {
                    if (resetUserPassword(username, newPassword)) {
                        messageLabel.setForeground(Color.GREEN);
                        messageLabel.setText("Password reset successfully.");
                        resetPasswordFrame.dispose();
                        parentFrame.setVisible(true);
                    } else {
                        messageLabel.setText("Error resetting password. Please try again.");
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

    private static boolean resetUserPassword(String username, String newPassword) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "UPDATE Agent SET pass = ? WHERE login = ? UNION UPDATE superieur SET pass = ? WHERE login = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, newPassword);
                stmt.setString(2, username);
                stmt.setString(3, newPassword);
                stmt.setString(4, username);
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
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