package onepproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard {
    public static void showAdminDashboard(JFrame parentFrame) {
        JFrame adminDashboardFrame = new JFrame("Admin Dashboard");
        adminDashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminDashboardFrame.setSize(800, 400); // Ajuster la taille pour accueillir moins de colonnes
        adminDashboardFrame.setLayout(new BorderLayout());

        // Ajouter le label du titre
        JLabel titleLabel = new JLabel("Demande de récupération", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        adminDashboardFrame.add(titleLabel, BorderLayout.NORTH);

        // Mettre à jour le modèle de tableau avec de nouveaux noms de colonnes et données
        String[] columnNames = {"Login", "Gmail", "Number"}; // Supprimer la colonne "Role"
        Object[][] data = {
            {"Alice", "alice@example.com", "123-456-7890"},
            {"Bob", "bob@example.com", "234-567-8901"},
            {"Charlie", "charlie@example.com", "345-678-9012"}
        };
        JTable table = new JTable(data, columnNames);

        // Personnaliser l'apparence du tableau
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);

        // Ajouter le tableau à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        adminDashboardFrame.add(scrollPane, BorderLayout.CENTER);

        // Créer un panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton logoutButton = new JButton("Logout");
        JButton loginButton = new JButton("Login");
        JButton doneButton = new JButton("Done");

        // Ajouter les écouteurs d'action pour les boutons
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminDashboardFrame.dispose();
                parentFrame.setVisible(true);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminDashboardFrame.dispose();
                AdminLoginForm.showAdminLoginForm(parentFrame);
            }
        });

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implémenter l'action désirée pour le bouton "Done" ici
                JOptionPane.showMessageDialog(adminDashboardFrame, "Done button clicked!");
            }
        });

        // Ajouter les boutons au panel
        buttonPanel.add(logoutButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(doneButton);

        // Ajouter le panel de boutons en bas de la fenêtre
        adminDashboardFrame.add(buttonPanel, BorderLayout.SOUTH);

        adminDashboardFrame.setVisible(true);

        adminDashboardFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }
}
