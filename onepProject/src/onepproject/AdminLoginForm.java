package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
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
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add image from URL
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
            adminFrame.add(imageLabel, gbc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gbc.gridheight = 1;

        // Admin Username Label and Field
        JLabel usernameLabel = new JLabel("Admin Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 0;
        adminFrame.add(usernameLabel, gbc);

        JTextField adminUsername = new JTextField(15);
        adminUsername.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 2;
        adminFrame.add(adminUsername, gbc);

        // Admin Password Label and Field
        JLabel passwordLabel = new JLabel("Admin Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 1;
        adminFrame.add(passwordLabel, gbc);

        JPasswordField adminPassword = new JPasswordField(15);
        adminPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 2;
        adminFrame.add(adminPassword, gbc);

        // Login Button
        JButton adminLoginButton = new JButton("Login");
        styleButton(adminLoginButton);
        gbc.gridx = 2;
        gbc.gridy = 2;
        adminFrame.add(adminLoginButton, gbc);

        // Message Label
        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        gbc.gridx = 2;
        gbc.gridy = 3;
        adminFrame.add(messageLabel, gbc);

        // Go Back Button
        JButton goBackButton = new JButton("Go Back");
        styleButton(goBackButton);
        gbc.gridy = 4;
        adminFrame.add(goBackButton, gbc);
        
        JButton goUserButton = new JButton("Je suis un Utilisateur");
        styleButton(goUserButton);
        gbc.gridy = 5;
        adminFrame.add(goUserButton, gbc);

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

        // Add action listener to go back button
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();
                parentFrame.setVisible(true);
            }
        });
        
        goUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adminFrame.setVisible(false);
                UserLoginForm.showUserLoginForm(adminFrame);
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
        button.setBackground(new Color(10, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}