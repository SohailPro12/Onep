package onepproject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreateAccountForm extends JDialog {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField positionField;
    private JComboBox<String> departmentComboBox;
    private JTextField emailField;
    private JTextField phoneField;
    private JRadioButton superieurRadioButton;
    private JRadioButton agentRadioButton;

    public CreateAccountForm(JFrame parentFrame) {
        super(parentFrame, "Create Account", true);
        setSize(450, 550);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // Set colors
        Color backgroundColor = new Color(245, 245, 245);
        Color buttonColor = new Color(0, 123, 255);
        Color textColor = new Color(33, 37, 41);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(backgroundColor);
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and add components
        addLabel(formPanel, gbc, "Num user:", 0, 0, textColor);
        loginField = createTextField(formPanel, gbc, 1, 0, "Enter your username");

        addLabel(formPanel, gbc, "Mot de passe:", 0, 1, textColor);
        passwordField = createPasswordField(formPanel, gbc, 1, 1, "Enter your password");

        addLabel(formPanel, gbc, "Nom complet:", 0, 2, textColor);
        fullNameField = createTextField(formPanel, gbc, 1, 2, "Enter your full name");

        addLabel(formPanel, gbc, "Poste:", 0, 3, textColor);
        positionField = createTextField(formPanel, gbc, 1, 3, "Enter your position");

        addLabel(formPanel, gbc, "Département:", 0, 4, textColor);
        departmentComboBox = new JComboBox<>();
        populateDepartmentComboBox();
        departmentComboBox.setToolTipText("Select your department");
        departmentComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        departmentComboBox.setBorder(new LineBorder(Color.GRAY));
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(departmentComboBox, gbc);

        addLabel(formPanel, gbc, "Email:", 0, 5, textColor);
        emailField = createTextField(formPanel, gbc, 1, 5, "Enter your email");

        addLabel(formPanel, gbc, "Numéro de téléphone:", 0, 6, textColor);
        phoneField = createTextField(formPanel, gbc, 1, 6, "Enter your phone number");

        addLabel(formPanel, gbc, "Compte que vous créez:", 0, 7, textColor);
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.setBackground(backgroundColor);
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
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(backgroundColor);
        add(buttonPanel, BorderLayout.SOUTH);

        JButton createButton = new JButton("Créer");
        styleButton(createButton, buttonColor);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInputs()) {
                    String login = loginField.getText();
                    String password = new String(passwordField.getPassword());
                    String fullName = fullNameField.getText();
                    String position = positionField.getText();
                    String department = (String) departmentComboBox.getSelectedItem();
                    String email = emailField.getText();
                    String phone = phoneField.getText();
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
        styleButton(cancelButton, new Color(220, 53, 69));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form without creating an account
            }
        });

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
    }

    private void addLabel(JPanel panel, GridBagConstraints gbc, String text, int x, int y, Color textColor) {
        JLabel label = new JLabel(text);
        label.setForeground(textColor);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(label, gbc);
    }

    private JTextField createTextField(JPanel panel, GridBagConstraints gbc, int x, int y, String placeholder) {
        JTextField textField = new JTextField();
        textField.setToolTipText(placeholder);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(new LineBorder(Color.GRAY));
        addPlaceholderStyle(textField, placeholder);
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(textField, gbc);
        return textField;
    }

    private JPasswordField createPasswordField(JPanel panel, GridBagConstraints gbc, int x, int y, String placeholder) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setToolTipText(placeholder);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(new LineBorder(Color.GRAY));
        addPlaceholderStyle(passwordField, placeholder);
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(passwordField, gbc);
        return passwordField;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
    }

private void addPlaceholderStyle(JTextField textField, String placeholder) {
    textField.setText(placeholder);
    textField.setForeground(Color.GRAY);
    textField.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (textField.getText().equals(placeholder)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (textField.getText().isEmpty()) {
                textField.setForeground(Color.GRAY);
                textField.setText(placeholder);
            }
        }
    });
}


    private boolean validateInputs() {
        if (loginField.getText().isEmpty() ||
                passwordField.getPassword().length == 0 ||
                fullNameField.getText().isEmpty() ||
                positionField.getText().isEmpty() ||
                departmentComboBox.getSelectedItem() == null ||
                emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() ||
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
        int departmentId = getDepartmentId(department);
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
            pstmt.setInt(5, departmentId);
            pstmt.setString(6, email);
            pstmt.setString(7, phone);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static int getDepartmentId(String departmentName) {
        int departmentId = -1;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/onep_db", "root", "")) {
            String query = "SELECT id FROM department WHERE libelle = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, departmentName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    departmentId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departmentId;
    }

    private void populateDepartmentComboBox() {
        String url = "jdbc:mysql://localhost:3306/onep_db";
        String username = "root"; // Change to your database username
        String dbPassword = ""; // Change to your database password

        String query = "SELECT libelle FROM department";

        try (Connection conn = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                departmentComboBox.addItem(rs.getString("libelle"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}