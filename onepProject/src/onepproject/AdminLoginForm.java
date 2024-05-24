package onepproject;

import javax.swing.*;
import java.awt.*;

public class AdminLoginForm {
    public static void showAdminLoginForm(JFrame parentFrame) {
        JFrame adminFrame = new JFrame("Admin Login");
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setSize(400, 200);
        adminFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
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
        adminFrame.add(adminLoginButton, gbc);

        adminFrame.setVisible(true);

        adminFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }
}
