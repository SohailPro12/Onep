package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountForm extends JDialog {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField positionField;
    private JTextField departmentField;

    public CreateAccountForm(JFrame parentFrame) {
        super(parentFrame, "Create Account", true);
        setSize(400, 300);
        setLocationRelativeTo(parentFrame);
        setLayout(new GridLayout(6, 2, 10, 10));

        // Create and add form fields
        add(new JLabel("Login:"));
        loginField = new JTextField();
        add(loginField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        add(fullNameField);

        add(new JLabel("Position:"));
        positionField = new JTextField();
        add(positionField);

        add(new JLabel("Department:"));
        departmentField = new JTextField();
        add(departmentField);

        // Add buttons
        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic to create an account
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText();
                String position = positionField.getText();
                String department = departmentField.getText();

                // For now, we'll just show the entered data in a dialog
                JOptionPane.showMessageDialog(CreateAccountForm.this,
                        "Login: " + login + "\nPassword: " + password + "\nFull Name: " + fullName +
                                "\nPosition: " + position + "\nDepartment: " + department,
                        "Account Created", JOptionPane.INFORMATION_MESSAGE);
                
                dispose(); // Close the form
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form without creating an account
            }
        });

        add(createButton);
        add(cancelButton);
    }
}
