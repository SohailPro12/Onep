package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateAccountForm extends JDialog {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField positionField;
    private JTextField departmentField;
    private JRadioButton superieurRadioButton;
    private JRadioButton agentRadioButton;

    public CreateAccountForm(JFrame parentFrame) {
        super(parentFrame, "Create Account", true);
        setSize(400, 350);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5)); // Adjusting grid layout for extra row
        add(formPanel, BorderLayout.CENTER);

        formPanel.add(new JLabel("Nom d'utilisateur:"));
        loginField = new JTextField();
        formPanel.add(loginField);

        formPanel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Nom complet:"));
        fullNameField = new JTextField();
        formPanel.add(fullNameField);

        formPanel.add(new JLabel("Poste:"));
        positionField = new JTextField();
        formPanel.add(positionField);

        formPanel.add(new JLabel("Département:"));
        departmentField = new JTextField();
        formPanel.add(departmentField);

        // Adding radio buttons for Supérieur or Agent
        formPanel.add(new JLabel("Compte que vous créez:"));
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        superieurRadioButton = new JRadioButton("Supérieur");
        agentRadioButton = new JRadioButton("Agent");

        // Group the radio buttons to ensure only one can be selected
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(superieurRadioButton);
        roleGroup.add(agentRadioButton);

        rolePanel.add(superieurRadioButton);
        rolePanel.add(agentRadioButton);
        formPanel.add(rolePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(buttonPanel, BorderLayout.SOUTH);

        JButton createButton = new JButton("Créer");
        JButton cancelButton = new JButton("Annuler");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText();
                String position = positionField.getText();
                String department = departmentField.getText();
                String role = superieurRadioButton.isSelected() ? "Supérieur" : "Agent";

                // Display entered data in a dialog
                JOptionPane.showMessageDialog(CreateAccountForm.this,
                        "Nom d'utilisateur: " + login + "\nMot de passe: " + password + "\nNom complet: " + fullName +
                                "\nPoste: " + position + "\nDépartement: " + department + "\nCompte: " + role,
                        "Compte créé", JOptionPane.INFORMATION_MESSAGE);

                // Insert data into the database
                insertDataIntoDatabase(login, password, fullName, position, department, role);

                dispose(); // Close the form
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form without creating an account
            }
        });

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
    }

    private void insertDataIntoDatabase(String login, String password, String fullName, String position, String department, String role) {
        String url = "jdbc:mysql://localhost:3306/onep_db";
        String username = "root"; // Change to your database username
        String dbPassword = ""; // Change to your database password

        String insertSQL;
        if ("Supérieur".equals(role)) {
            insertSQL = "INSERT INTO superieur (login, pass, NomComplete, Post, Departement) VALUES (?, ?, ?, ?, ?)";
        } else {
            insertSQL = "INSERT INTO agent (login, pass, NomComplete, Post, Departement) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection conn = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, position);
            pstmt.setString(5, department);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            JButton openButton = new JButton("Ouvrir le formulaire");
            openButton.addActionListener(e -> {
                CreateAccountForm form = new CreateAccountForm(frame);
                form.setVisible(true);
            });

            frame.add(openButton);
            frame.setVisible(true);
        });
    }
}
