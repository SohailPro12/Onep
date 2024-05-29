package onepproject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private JTextField emailField;
    private JTextField phoneField; // New field for phone number
    private JRadioButton superieurRadioButton;
    private JRadioButton agentRadioButton;

    public CreateAccountForm(JFrame parentFrame) {
        super(parentFrame, "Create Account", true);
        setSize(450, 500);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginLabel = new JLabel("Num user:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(loginLabel, gbc);

        loginField = new JTextField();
        loginField.setToolTipText("Enter your username");
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(loginField, gbc);

        JLabel passwordLabel = new JLabel("Mot de passe:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setToolTipText("Enter your password");
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        JLabel fullNameLabel = new JLabel("Nom complet:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(fullNameLabel, gbc);

        fullNameField = new JTextField();
        fullNameField.setToolTipText("Enter your full name");
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(fullNameField, gbc);

        JLabel positionLabel = new JLabel("Poste:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(positionLabel, gbc);

        positionField = new JTextField();
        positionField.setToolTipText("Enter your position");
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(positionField, gbc);

        JLabel departmentLabel = new JLabel("Département:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(departmentLabel, gbc);

        departmentField = new JTextField();
        departmentField.setToolTipText("Enter your department");
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(departmentField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setToolTipText("Enter your email");
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(emailField, gbc);

        JLabel phoneLabel = new JLabel("Numéro de téléphone:"); // New label for phone number
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(phoneLabel, gbc);

        phoneField = new JTextField(); // New text field for phone number
        phoneField.setToolTipText("Enter your phone number");
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(phoneField, gbc);

        JLabel roleLabel = new JLabel("Compte que vous créez:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(roleLabel, gbc);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        superieurRadioButton = new JRadioButton("Supérieur");
        agentRadioButton = new JRadioButton("Agent");

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(superieurRadioButton);
        roleGroup.add(agentRadioButton);

        rolePanel.add(superieurRadioButton);
        rolePanel.add(agentRadioButton);

        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(rolePanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(buttonPanel, BorderLayout.SOUTH);

        JButton createButton = new JButton("Créer");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInputs()) {
                    String login = loginField.getText();
                    String password = new String(passwordField.getPassword());
                    String fullName = fullNameField.getText();
                    String position = positionField.getText();
                    String department = departmentField.getText();
                    String email = emailField.getText();
                    String phone = phoneField.getText(); // Get phone number
                    String role = superieurRadioButton.isSelected() ? "Supérieur" : "Agent";

                    // Display entered data in a dialog
                    JOptionPane.showMessageDialog(CreateAccountForm.this,
                            "Num user: " + login + "\nMot de passe: " + password + "\nNom complet: " + fullName +
                                    "\nPoste: " + position + "\nDépartement: " + department + "\nEmail: " + email + "\nNuméro de téléphone: " + phone + "\nCompte: " + role,
                            "Compte créé", JOptionPane.INFORMATION_MESSAGE);

                    // Insert data into the database
                    insertDataIntoDatabase(login, password, fullName, position, department, email, phone, role);

                    dispose(); // Close the form
                }
            }
        });

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form without creating an account
            }
        });

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
    }

    private boolean validateInputs() {
        if (loginField.getText().isEmpty() ||
                passwordField.getPassword().length == 0 ||
                fullNameField.getText().isEmpty() ||
                positionField.getText().isEmpty() ||
                departmentField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || // Validate phone number
                (!superieurRadioButton.isSelected() && !agentRadioButton.isSelected())) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs et sélectionner un rôle.", "Champs manquants", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void insertDataIntoDatabase(String login, String password, String fullName, String position, String department, String email, String phone, String role) {
        String url = "jdbc:mysql://localhost:3306/onep_db";
        String username = "root"; // Change to your database username
        String dbPassword = ""; // Change to your database password

        String insertSQL;
        if ("Supérieur".equals(role)) {
            insertSQL = "INSERT INTO superieur (login, pass, NomComplete, Post, Departement, email, numero_tel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            insertSQL = "INSERT INTO agent (login, pass, NomComplete, Post, Departement, email, numero_tel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        try (Connection conn = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, position);
            pstmt.setString(5, department);
            pstmt.setString(6, email);
            pstmt.setString(7, phone); // Set phone number

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
