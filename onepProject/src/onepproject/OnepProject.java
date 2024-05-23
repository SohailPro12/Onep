/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
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
        mainFrame.setSize(300, 150);
        mainFrame.setLayout(new GridLayout(2, 1));
        
        // Create the buttons
        JButton adminButton = new JButton("Admin Login");
        JButton userButton = new JButton("User Login");
        
        // Add action listeners to the buttons
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminLoginForm.showAdminLoginForm();
            }
        });
        
        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserLoginForm.showUserLoginForm();
            }
        });
        
        // Add buttons to the frame
        mainFrame.add(adminButton);
        mainFrame.add(userButton);
        
        // Set frame to be visible
        mainFrame.setVisible(true);
    }
}
