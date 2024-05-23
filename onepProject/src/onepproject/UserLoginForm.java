/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onepproject;

import javax.swing.*;
import java.awt.*;

public class UserLoginForm {
    public static void showUserLoginForm() {
        JFrame userFrame = new JFrame("User Login");
        userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userFrame.setSize(300, 200);
        userFrame.setLayout(new GridLayout(3, 2));
        
        // Add components for user login
        userFrame.add(new JLabel("User Username:"));
        JTextField userUsername = new JTextField();
        userFrame.add(userUsername);
        
        userFrame.add(new JLabel("User Password:"));
        JPasswordField userPassword = new JPasswordField();
        userFrame.add(userPassword);
        
        JButton userLoginButton = new JButton("Login");
        userFrame.add(userLoginButton);
        
        userFrame.setVisible(true);
    }
}
