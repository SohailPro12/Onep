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
        setLayout(new BorderLayout(10, 10)); // Utilisation d'un layout border pour une meilleure organisation

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5)); // Panel pour les champs de formulaire
        add(formPanel, BorderLayout.CENTER);

        // Labels et champs de formulaire
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

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(buttonPanel, BorderLayout.SOUTH);

        JButton createButton = new JButton("Créer");
        JButton cancelButton = new JButton("Annuler");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implémenter la logique pour créer un compte
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText();
                String position = positionField.getText();
                String department = departmentField.getText();

                // Afficher les données saisies dans un dialogue
                JOptionPane.showMessageDialog(CreateAccountForm.this,
                        "Nom d'utilisateur: " + login + "\nMot de passe: " + password + "\nNom complet: " + fullName +
                                "\nPoste: " + position + "\nDépartement: " + department,
                        "Compte créé", JOptionPane.INFORMATION_MESSAGE);

                dispose(); // Fermer le formulaire
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fermer le formulaire sans créer de compte
            }
        });

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
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
