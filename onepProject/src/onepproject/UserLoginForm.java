package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.net.URL;

public class UserLoginForm {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/onep_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = ""; // Change to your actual DB password

    public static void showUserLoginForm(JFrame parentFrame) {
        JFrame userFrame = new JFrame("User Login");
        userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userFrame.setSize(1000, 600);
        userFrame.setLocationRelativeTo(null);
        userFrame.setResizable(false); // Disable resizing
        userFrame.setLayout(new GridBagLayout());
        userFrame.getContentPane().setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add image
        try {
            URL imageUrl = new URL("https://i.ibb.co/YXyW9Zt/Onep-svg.png");
            ImageIcon originalIcon = new ImageIcon(imageUrl);
    
            // Get the width and height of the original image
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
    
            // Calculate the width and height to fit the left part of the frame
            int targetWidth = 300; // Adjust this as needed
            int targetHeight = (int) Math.round((double) targetWidth / originalWidth * originalHeight);
    
            // Resize the image to fit the target dimensions
            ImageIcon resizedIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT));

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = GridBagConstraints.REMAINDER; // Span the entire height
            gbc.gridwidth = 1; // Only occupy one column
            JLabel imageLabel = new JLabel(resizedIcon);
            userFrame.add(imageLabel, gbc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        userFrame.add(new JLabel("User Username:"), gbc);

        gbc.gridx = 2;
        JTextField userUsername = new JTextField(20);
        userUsername.setFont(new Font("Arial", Font.PLAIN, 18));
        userFrame.add(userUsername, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        userFrame.add(new JLabel("User Password:"), gbc);

        gbc.gridx = 2;
        JPasswordField userPassword = new JPasswordField(20);
        userPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        userFrame.add(userPassword, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        JButton userLoginButton = new JButton("Login");
        styleButton(userLoginButton);
        userFrame.add(userLoginButton, gbc);

        gbc.gridy = 3;
        JButton forgotPasswordButton = new JButton("Mot de passe oublié");
        styleButton(forgotPasswordButton);
        userFrame.add(forgotPasswordButton, gbc);

        gbc.gridy = 4;
        JButton goBackButton = new JButton("Go Back");
        styleButton(goBackButton);
        userFrame.add(goBackButton, gbc);
        
        gbc.gridy = 5;
        JButton goAdminButton = new JButton("Je suis un administrateur");
        styleButton(goAdminButton);
        userFrame.add(goAdminButton, gbc);

        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridy = 5;
        userFrame.add(messageLabel, gbc);

        userFrame.setVisible(true);

        userLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userUsername.getText();
                String password = new String(userPassword.getPassword());
                int authResult = authenticateUser(username, password);
                if (authResult == 1) {
                    JOptionPane.showMessageDialog(userFrame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    userFrame.dispose();
                    showUserDashboard(username);
                } else if (authResult == 0) {
                    JOptionPane.showMessageDialog(userFrame, "Invalid password.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(userFrame, "Invalid username.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userFrame.dispose();
                showForgotPasswordForm(parentFrame);
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userFrame.dispose();
                parentFrame.setVisible(true);
            }
        });
        goAdminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userFrame.setVisible(false);
                AdminLoginForm.showAdminLoginForm(userFrame);
            }
        });

        userFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }

    private static int authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM Agent WHERE BINARY login = ? UNION SELECT * FROM superieur WHERE BINARY login = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String storedPassword = rs.getString("pass");
                    if (storedPassword.equals(password)) {
                        return 1; // Authentication successful
                    } else {
                        return 0; // Invalid password
                    }
                } else {
                    return -1; // Invalid username
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
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
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        forgotPasswordFrame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        forgotPasswordFrame.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 18));
        forgotPasswordFrame.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        forgotPasswordFrame.add(new JLabel("Telephone Number:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 18));
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
                boolean userExists = checkUserDetails(username, email, phone);
                if (userExists) {
                    JOptionPane.showMessageDialog(forgotPasswordFrame, "User details validated. Proceed to reset password.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    messageLabel.setText("Proceed to reset password.");
                    // Logic to allow password reset can be added here
                } else {
                    JOptionPane.showMessageDialog(forgotPasswordFrame, "Invalid details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    messageLabel.setText("Invalid details. Please try again.");
                }
            }
        });

        proceedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to proceed with password reset
                JOptionPane.showMessageDialog(forgotPasswordFrame, "Password reset functionality is not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        forgotPasswordFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }

    private static boolean checkUserDetails(String username, String email, String phone) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM Agent WHERE login = ? AND email = ? AND telephone = ? " +
                           "UNION SELECT * FROM superieur WHERE login = ? AND email = ? AND telephone = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, phone);
                stmt.setString(4, username);
                stmt.setString(5, email);
                stmt.setString(6, phone);
                ResultSet rs = stmt.executeQuery();
                return rs.next(); // True if user exists, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

     private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }
}
