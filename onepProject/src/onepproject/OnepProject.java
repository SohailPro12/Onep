package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnepProject {
    public static void main(String[] args) {
        // Create the main frame
        JFrame mainFrame = new JFrame("Main Form");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 200);
        mainFrame.setLayout(new BorderLayout());

        // Create a panel with CardLayout
        JPanel cardPanel = new JPanel(new CardLayout());

        // Create the main panel with buttons
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton adminButton = new JButton("Admin Login");
        JButton userButton = new JButton("User Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(adminButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(userButton, gbc);

        // Add the main panel to the card panel
        cardPanel.add(mainPanel, "MainPanel");

        // Add the card panel to the main frame
        mainFrame.add(cardPanel, BorderLayout.CENTER);

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
