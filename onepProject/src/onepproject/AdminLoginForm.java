/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onepproject;

import javax.swing.*;
import java.awt.*;

public class AdminLoginForm {
    public static void showAdminLoginForm() {
        JFrame adminFrame = new JFrame("Admin Login");
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setSize(300, 200);
        adminFrame.setLayout(new GridLayout(3, 2));
        
        // Add components for admin login
        adminFrame.add(new JLabel("Admin Username:"));
        JTextField adminUsername = new JTextField();
        adminFrame.add(adminUsername);
        
        adminFrame.add(new JLabel("Admin Password:"));
        JPasswordField adminPassword = new JPasswordField();
        adminFrame.add(adminPassword);
        
        JButton adminLoginButton = new JButton("Login");
        adminFrame.add(adminLoginButton);
        
        adminFrame.setVisible(true);
    }
}
