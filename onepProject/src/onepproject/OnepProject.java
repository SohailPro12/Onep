package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OnepProject {
    public static void main(String[] args) {
        // Create the main frame
        JFrame mainFrame = new JFrame("Main Form");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 500); // Set the fixed size
        mainFrame.setLocationRelativeTo(null); // Center the window
        mainFrame.setResizable(false); // Disable resizing
        mainFrame.setLayout(new BorderLayout());

        // Create a panel with CardLayout
        JPanel cardPanel = new JPanel(new CardLayout());

        // Create the main panel with buttons
        JPanel mainPanel = new JPanel(new GridBagLayout());
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
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel headerLabel = new JLabel("Welcome to One Project", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Load and scale logo
        BufferedImage originalLogo = null;
        try {
            originalLogo = ImageIO.read(new File("C:\\Users\\LENOVO\\Documents\\NetBeansProjects\\Onep\\onepProject\\src\\images\\WhatsApp Image 2024-05-28 à 18.39.20_1d42cc15.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image scaledLogo = originalLogo.getScaledInstance(350, 200, Image.SCALE_SMOOTH); // Scale the logo
        ImageIcon logoIcon = new ImageIcon(scaledLogo);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between logo and text
        headerPanel.add(headerLabel);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Footer
        JLabel footerLabel = new JLabel("© 2024 One Project Inc.", SwingConstants.CENTER);
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
