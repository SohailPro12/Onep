package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgentDashboard extends JFrame {

    public AgentDashboard(String username) {
        setTitle("Agent Dashboard - Welcome " + username);
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Agent Dashboard", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

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
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    public static void showAgentDashboard(String username) {
        SwingUtilities.invokeLater(() -> new AgentDashboard(username));
    }
}
