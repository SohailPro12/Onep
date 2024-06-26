package onepproject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class AdminDashboard {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/onep_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = ""; // Change to your actual DB password

    public static void showAdminDashboard(JFrame parentFrame) {
        JFrame adminDashboardFrame = new JFrame("Admin Dashboard");
        adminDashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminDashboardFrame.setSize(1000, 600);
        adminDashboardFrame.setLocationRelativeTo(null);
        adminDashboardFrame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Demande de récupération", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        adminDashboardFrame.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Login", "Gmail", "Numero","Code de recuperation", "Selectionner"};
        Object[][] data = fetchDataFromDatabase();

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            Class[] types = new Class[]{String.class, String.class, String.class,Integer.class ,Boolean.class};
            boolean[] canEdit = new boolean[]{false, false, false,false, true};

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180)); // Steel blue
        table.getTableHeader().setForeground(Color.WHITE);

        // Custom cell renderer for alternating row colors and selected row color
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(Color.ORANGE); // Orange for selected row
                    c.setForeground(Color.BLACK);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(255, 255, 204)); // Light yellow
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };

        table.setDefaultRenderer(Object.class, cellRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        adminDashboardFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton logoutButton = new JButton("Logout");
        JButton createAccountButton = new JButton("Create Account");
        JButton doneButton = new JButton("Fait");

        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        createAccountButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        doneButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        logoutButton.setBackground(new Color(70, 130, 180));
        createAccountButton.setBackground(new Color(70, 130, 180));
        doneButton.setBackground(new Color(70, 130, 180));

        logoutButton.setForeground(Color.WHITE);
        createAccountButton.setForeground(Color.WHITE);
        doneButton.setForeground(Color.WHITE);

        logoutButton.setFocusPainted(false);
        createAccountButton.setFocusPainted(false);
        doneButton.setFocusPainted(false);
JButton deleteUserButton = new JButton("Supprimer Compte Utilisateur");
deleteUserButton.setFont(new Font("SansSerif", Font.BOLD, 14));
deleteUserButton.setBackground(new Color(70, 130, 180));
deleteUserButton.setForeground(Color.WHITE);
deleteUserButton.setFocusPainted(false);

deleteUserButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String login = JOptionPane.showInputDialog(adminDashboardFrame, "Entrez le login de l'utilisateur à supprimer:", "Supprimer Compte Utilisateur", JOptionPane.PLAIN_MESSAGE);
        if (login != null && !login.trim().isEmpty()) {
            int confirmation = JOptionPane.showConfirmDialog(adminDashboardFrame, "Êtes-vous sûr de vouloir supprimer l'utilisateur avec le login: " + login + " ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                if (deleteUserAccountFromDatabase(login)) {
                    JOptionPane.showMessageDialog(adminDashboardFrame, "Compte utilisateur supprimé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(adminDashboardFrame, "Impossible de supprimer le compte utilisateur de la base de données.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(adminDashboardFrame, "Le login ne peut pas être vide.");
        }
    }
});
buttonPanel.add(deleteUserButton);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminDashboardFrame.dispose();
                parentFrame.setVisible(true);
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateAccountForm(adminDashboardFrame).setVisible(true);
            }
        });

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String login = (String) table.getValueAt(selectedRow, 0);
                    String email = (String) table.getValueAt(selectedRow, 1);
                    String number = (String) table.getValueAt(selectedRow, 2);

                    int confirmation = JOptionPane.showConfirmDialog(adminDashboardFrame,
                            "Êtes-vous sûr de vouloir supprimer cette ligne?\nLogin: " + login + "\nEmail: " + email + "\nNumber: " + number,
                            "Delete Confirmation", JOptionPane.YES_NO_OPTION);

                    if (confirmation == JOptionPane.YES_OPTION) {
                        if (deleteRowFromDatabase(login)) {
                            ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                            JOptionPane.showMessageDialog(adminDashboardFrame, "Ligne supprimée avec succès.");
                        } else {
                            JOptionPane.showMessageDialog(adminDashboardFrame, "Impossible de supprimer la ligne de la base de données.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(adminDashboardFrame, "Veuillez sélectionner une ligne.");
                }
            }
        });

        buttonPanel.add(logoutButton);
        buttonPanel.add(createAccountButton);
        buttonPanel.add(doneButton);

        adminDashboardFrame.add(buttonPanel, BorderLayout.SOUTH);

        adminDashboardFrame.setVisible(true);

        adminDashboardFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });
    }

    private static Object[][] fetchDataFromDatabase() {
        Vector<Vector<Object>> dataVector = new Vector<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT login, email, numero_tel,code FROM recuperation_mp";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getString("login"));
                    row.add(rs.getString("email"));
                    row.add(rs.getString("numero_tel"));
                    row.add(rs.getInt("code"));
                    row.add(false);
                    dataVector.add(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        Object[][] data = new Object[dataVector.size()][4];
        for (int i = 0; i < dataVector.size(); i++) {
            data[i] = dataVector.get(i).toArray();
        }
        return data;
    }

    private static boolean deleteRowFromDatabase(String login) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "DELETE FROM recuperation_mp WHERE login = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, login);
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting row from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean deleteUserAccountFromDatabase(String login) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
        String role = getUserRole(login);
        if (role.equals("superieur")){
        String query = "DELETE FROM superieur WHERE login = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }}else if (role.equals("agent")){
        String query = "DELETE FROM Agent WHERE login = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }}
        return true;
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du compte utilisateur: " + e.getMessage(), "Erreur de base de données", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    }
}
    
     private static String getUserRole(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT 'superieur' AS role FROM superieur WHERE login = ? UNION SELECT 'agent' AS role FROM Agent WHERE login = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

}