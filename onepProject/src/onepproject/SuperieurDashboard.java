package onepproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SuperieurDashboard {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/onep_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = ""; // Change to your actual DB password
    private static String superieurFullName;

    public static void showSuperieurDashboard(JFrame parentFrame, String username) {
        JFrame dashboardFrame = new JFrame("Superieur Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setSize(1000, 600);
        dashboardFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Task Form
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        dashboardFrame.add(new JLabel("Task Form"), gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        dashboardFrame.add(new JLabel("Task ID:"), gbc);

        gbc.gridx = 1;
        JTextField taskIdField = new JTextField(15);
        taskIdField.setEditable(false);
        dashboardFrame.add(taskIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dashboardFrame.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        JTextField titleField = new JTextField(15);
        dashboardFrame.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dashboardFrame.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        JTextField descriptionField = new JTextField(15);
        dashboardFrame.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        dashboardFrame.add(new JLabel("Department:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> departmentComboBox = new JComboBox<>();
        loadDepartments(departmentComboBox);
        dashboardFrame.add(departmentComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        dashboardFrame.add(new JLabel("Agent:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> agentComboBox = new JComboBox<>();
        dashboardFrame.add(agentComboBox, gbc);

        departmentComboBox.addActionListener(e -> loadAgents(departmentComboBox, agentComboBox));

        gbc.gridx = 0;
        gbc.gridy = 6;
        dashboardFrame.add(new JLabel("Budget:"), gbc);

        gbc.gridx = 1;
        JTextField budgetField = new JTextField(15);
        dashboardFrame.add(budgetField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        dashboardFrame.add(new JLabel("Superieur:"), gbc);
        gbc.gridx = 1;
        JTextField supField = new JTextField(15);
        dashboardFrame.add(supField, gbc);

        // Task buttons
        gbc.gridx = 0;
        gbc.gridy = 8;
        JButton addButton = new JButton("Ajouter");
        dashboardFrame.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        JButton deleteButton = new JButton("Supprimer");
        dashboardFrame.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        JButton modifyButton = new JButton("Modifier");
        dashboardFrame.add(modifyButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        JButton searchButton = new JButton("Chercher");
        dashboardFrame.add(searchButton, gbc);

        gbc.gridx = 2;
gbc.gridy = 0;
gbc.gridheight = 10;
gbc.gridwidth = 3;
String[] columnNames = {"Task ID", "Title", "Description", "Agent", "Budget", "Commentaires", "Progression"}; // Add Comment and Progression columns
DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
JTable taskTable = new JTable(tableModel);
JScrollPane scrollPane = new JScrollPane(taskTable);
dashboardFrame.add(scrollPane, gbc);

        // Other buttons
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JButton logoutButton = new JButton("Logout");
        dashboardFrame.add(logoutButton, gbc);

        gbc.gridx = 5;
        gbc.gridy = 1;
        JButton statsButton = new JButton("Statistiques");
        dashboardFrame.add(statsButton, gbc);

        gbc.gridx = 5;
        gbc.gridy = 2;
        JLabel searchLabel = new JLabel("Search by Task ID or Agent Name:");
        dashboardFrame.add(searchLabel, gbc);

        gbc.gridx = 5;
        gbc.gridy = 3;
        JTextField searchBar = new JTextField(15);
        dashboardFrame.add(searchBar, gbc);

        dashboardFrame.setVisible(true);

        // Load initial data
        loadTasks(tableModel);
        taskIdField.setText(getNextTaskId());

        // Set superieur's full name
        superieurFullName = getSuperieurFullName(username);
        supField.setText(superieurFullName);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask(titleField, descriptionField, agentComboBox, budgetField, supField, tableModel, taskIdField);
           resetFields(taskIdField, titleField, descriptionField, departmentComboBox, agentComboBox, budgetField, supField);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTask(taskTable, tableModel);
            resetFields(taskIdField, titleField, descriptionField, departmentComboBox, agentComboBox, budgetField, supField);
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyTask(taskTable, titleField, descriptionField, agentComboBox, budgetField, tableModel);
           resetFields(taskIdField, titleField, descriptionField, departmentComboBox, agentComboBox, budgetField, supField);
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTasks(searchBar, tableModel);
            
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose();
                parentFrame.setVisible(true); // Show login form
            }
        });

        statsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showStatistics();
            }
        });

        taskTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && taskTable.getSelectedRow() != -1) {
                int selectedRow = taskTable.getSelectedRow();
                taskIdField.setText(taskTable.getValueAt(selectedRow, 0).toString());
                titleField.setText(taskTable.getValueAt(selectedRow, 1).toString());
                descriptionField.setText(taskTable.getValueAt(selectedRow, 2).toString());
                agentComboBox.setSelectedItem(taskTable.getValueAt(selectedRow, 3).toString());
                budgetField.setText(taskTable.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    private static void loadDepartments(JComboBox<String> departmentComboBox) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT libelle FROM department";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    departmentComboBox.addItem(rs.getString("libelle"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void resetFields(JTextField taskIdField, JTextField titleField, JTextField descriptionField,
                                JComboBox<String> departmentComboBox, JComboBox<String> agentComboBox, JTextField budgetField, JTextField supField) {
    taskIdField.setText("");
    titleField.setText("");
    descriptionField.setText("");
    departmentComboBox.setSelectedIndex(-1);  // Reset the department combo box
    agentComboBox.removeAllItems();  // Clear agent combo box
    budgetField.setText("");

     taskIdField.setText(getNextTaskId());
}


    private static void loadAgents(JComboBox<String> departmentComboBox, JComboBox<String> agentComboBox) {
    agentComboBox.removeAllItems();
    String selectedDepartmentName = (String) departmentComboBox.getSelectedItem();
    int departmentId = getDepartmentId(selectedDepartmentName);

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
        String query = "SELECT NomComplete FROM agent WHERE Departement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                agentComboBox.addItem(rs.getString("NomComplete"));
            }
        }

        query = "SELECT NomComplete FROM superieur WHERE Departement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                agentComboBox.addItem(rs.getString("NomComplete"));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private static int getDepartmentId(String departmentName) {
    int departmentId = -1;
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
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


    private static String getNextTaskId() {
        String nextId = "";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT MAX(id) FROM tache";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int maxId = rs.getInt(1);
                    nextId = String.valueOf(maxId + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

   private static void loadTasks(DefaultTableModel tableModel) {
    tableModel.setRowCount(0); // Clear the table
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
        String query = "SELECT t.id, Titre, t.Description, t.Agent, budget, c.Description as commentaires, c.progression FROM tache t, commentaires c where t.id = c.Id_Tache";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("Titre"),
                        rs.getString("Description"),
                        rs.getString("Agent"),
                        rs.getInt("budget"),
                        rs.getString("commentaires"), // Include Comment column
                        rs.getString("Progression") // Include Progression column
                };
                tableModel.addRow(row);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    private static void addTask(JTextField titleField, JTextField descriptionField, JComboBox<String> agentComboBox, JTextField budgetField, JTextField supField, DefaultTableModel tableModel, JTextField taskIdField) {
        String id = taskIdField.getText();
        String title = titleField.getText();
        String description = descriptionField.getText();
        String agentName = (String) agentComboBox.getSelectedItem();
        String budget = budgetField.getText();
        String sup = supField.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO tache (id ,Titre, Description, Agent, budget, sup) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, id);
                stmt.setString(2, title);
                stmt.setString(3, description);
                stmt.setString(4, agentName);
                stmt.setString(5, budget);
                stmt.setString(6, sup);
                stmt.executeUpdate();
            }
            String query2 = "INSERT INTO commentaires (Description, Agent, Id_Tache,progression) VALUES (?, ?, ?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(query2)) {
                stmt.setString(1, "");
                stmt.setString(2, agentName);
                stmt.setString(3, id);
                stmt.setString(4, "");
                stmt.executeUpdate();
            }

            Object[] row = {Integer.parseInt(taskIdField.getText()), title, description, agentName, Integer.parseInt(budget)};
            tableModel.addRow(row);
            taskIdField.setText(getNextTaskId());
         
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteTask(JTable taskTable, DefaultTableModel tableModel) {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            int taskId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String query = "DELETE FROM tache WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, taskId);
                    stmt.executeUpdate();
                    
                }
                tableModel.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void modifyTask(JTable taskTable, JTextField titleField, JTextField descriptionField, JComboBox<String> agentComboBox, JTextField budgetField, DefaultTableModel tableModel) {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            int taskId = (int) tableModel.getValueAt(selectedRow, 0);
            String title = titleField.getText();
            String description = descriptionField.getText();
            String agentName = (String) agentComboBox.getSelectedItem();
            String budget = budgetField.getText();

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String query = "UPDATE tache SET Titre = ?, Description = ?, Agent = ?, budget = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, title);
                    stmt.setString(2, description);
                    stmt.setString(3, agentName);
                    stmt.setString(4, budget);
                    stmt.setInt(5, taskId);
                    stmt.executeUpdate();
                }
                tableModel.setValueAt(title, selectedRow, 1);
                tableModel.setValueAt(description, selectedRow, 2);
                tableModel.setValueAt(agentName, selectedRow, 3);
                tableModel.setValueAt(budget, selectedRow, 4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void searchTasks(JTextField searchBar, DefaultTableModel tableModel) {
        String searchQuery = searchBar.getText();
        if (!searchQuery.isEmpty()) {
            tableModel.setRowCount(0); // Clear the table
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String query = "SELECT id, Titre, Description, Agent, budget FROM tache WHERE id = ? OR Agent LIKE ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, searchQuery);
                    stmt.setString(2, "%" + searchQuery + "%");
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        Object[] row = {
                                rs.getInt("id"),
                                rs.getString("Titre"),
                                rs.getString("Description"),
                                rs.getString("Agent"),
                                rs.getInt("budget")
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            loadTasks(tableModel); // Reload all tasks if search bar is empty
        }
    }

    private static void showStatistics() {
        // Logic to show statistics
        JOptionPane.showMessageDialog(null, "Statistics not implemented yet!");
    }

    private static String getSuperieurFullName(String username) {
        String fullName = "";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT NomComplete FROM superieur WHERE login = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    fullName = rs.getString("NomComplete");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullName;
    }
}
