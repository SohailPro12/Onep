package onepproject;

import javax.swing.*;
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
        adminDashboardFrame.setSize(800, 400);
        adminDashboardFrame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Demande de récupération", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        adminDashboardFrame.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Login", "Gmail", "Number"};
        Object[][] data = fetchDataFromDatabase();

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        adminDashboardFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton logoutButton = new JButton("Logout");
        JButton loginButton = new JButton("Login");
        JButton doneButton = new JButton("Done");

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
                JOptionPane.showMessageDialog(adminDashboardFrame, "Done button clicked!");
            }
        });

        buttonPanel.add(logoutButton);
        buttonPanel.add(loginButton);
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
            String query = "SELECT login, email, numero_tel FROM recuperation_mp";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getString("login"));
                    row.add(rs.getString("email"));
                    row.add(rs.getString("numero_tel"));
                    dataVector.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] data = new Object[dataVector.size()][3];
        for (int i = 0; i < dataVector.size(); i++) {
            data[i] = dataVector.get(i).toArray();
        }
        return data;
    }
}
