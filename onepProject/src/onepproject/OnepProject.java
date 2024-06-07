package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class OnepProject {
    public static void main(String[] args) {
        // Create the main frame
        JFrame mainFrame = new JFrame("Onep");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 600); // Set the fixed size
        mainFrame.setLocationRelativeTo(null); // Center the window
        mainFrame.setResizable(false); // Disable resizing
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.white);

        // Create a panel with CardLayout
        JPanel cardPanel = new JPanel(new CardLayout());

        // Create the main panel with buttons
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Styling for the buttons
        JButton adminButton = new JButton("Admin Login");
        JButton userButton = new JButton("User Login");

        adminButton.setFont(new Font("Arial", Font.BOLD, 16));
        adminButton.setBackground(new Color(70, 130, 180));
        adminButton.setForeground(Color.WHITE);
        adminButton.setFocusPainted(false);

        userButton.setFont(new Font("Arial", Font.BOLD, 16));
        userButton.setBackground(new Color(70, 130, 180));
        userButton.setForeground(Color.WHITE);
        userButton.setFocusPainted(false);

        // Add buttons to the main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(adminButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(userButton, gbc);

        // Header with Logo
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(Color.white);

        // Load and scale main logo from the internet
        BufferedImage mainLogo = null;
        try {
            URL mainLogoUrl = new URL("http://www.onep.ma/news/2017/semaine-eau_25-09-2017/Logo-ONEE.jpg");
            mainLogo = ImageIO.read(mainLogoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image scaledMainLogo = mainLogo.getScaledInstance(500, 100, Image.SCALE_SMOOTH); // Scale the main logo
        ImageIcon mainLogoIcon = new ImageIcon(scaledMainLogo);
        JLabel mainLogoLabel = new JLabel(mainLogoIcon);

        // Load and scale app logo from the internet
        BufferedImage appLogo = null;
        try {
            URL appLogoUrl = new URL("https://i.ibb.co/PZjxwDy/large-removebg-preview.png");
            appLogo = ImageIO.read(appLogoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image scaledAppLogo = appLogo.getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Scale the app logo
        ImageIcon appLogoIcon = new ImageIcon(scaledAppLogo);
        JLabel appLogoLabel = new JLabel(appLogoIcon);

        // Create a panel to hold both logos side by side
        JPanel logosPanel = new JPanel();
        logosPanel.setBackground(Color.white);
        logosPanel.add(mainLogoLabel);
        logosPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Space between logos
        logosPanel.add(appLogoLabel);

        // Add logos panel to header panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        headerPanel.add(logosPanel, gbc);

        // Header label
        JLabel headerLabel = new JLabel("<html>Bienvenue chez <span style='color:#4682B4;'>CoordiTeam</span></html>", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 25));

        // Add header label to header panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        headerPanel.add(headerLabel, gbc);

        // Footer
        JLabel footerLabel = new JLabel("© 2024 Onep Project Inc.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add components to card panel
        cardPanel.add(mainPanel, "MainPanel");

        // Add components to main frame
        mainFrame.add(headerPanel, BorderLayout.NORTH);
        mainFrame.add(cardPanel, BorderLayout.CENTER);
        mainFrame.add(footerLabel, BorderLayout.SOUTH);

        // Set frame to be visible
        mainFrame.setVisible(true);

        // Add action listeners to the buttons
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                AdminLoginForm.showAdminLoginForm(mainFrame);
            }
        });

        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                UserLoginForm.showUserLoginForm(mainFrame);
            }
        });
    }
}
