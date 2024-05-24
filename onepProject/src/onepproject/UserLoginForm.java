package onepproject;

import javax.swing.*;
import java.awt.*;

public class UserLoginForm {
    public static void showUserLoginForm(JFrame parentFrame) {
        JFrame userFrame = new JFrame("User Login");
        userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userFrame.setSize(400, 200);
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

        userFrame.setVisible(true);

        userFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }
}
