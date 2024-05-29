package onepproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class SuperieurDashboard {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/onep_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = ""; // Change to your actual DB password
    private static String superieurFullName;

    public static void showSuperieurDashboard(JFrame parentFrame, String username) {
        JFrame dashboardFrame = new JFrame("Superieur Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setSize(1000, 800);

        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setResizable(false); // Disable resizing
        dashboardFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Task Form Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel taskFormLabel = new JLabel("Task Form");
        taskFormLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardFrame.add(taskFormLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        dashboardFrame.add(new JLabel("Task ID:"), gbc);

        gbc.gridx = 1;
        JTextField taskIdField = new JTextField(20);
        taskIdField.setEditable(false);
        dashboardFrame.add(taskIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dashboardFrame.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        JTextField titleField = new JTextField(20);
        dashboardFrame.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dashboardFrame.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        JTextField descriptionField = new JTextField(20);
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
        JTextField budgetField = new JTextField(20);
        dashboardFrame.add(budgetField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        dashboardFrame.add(new JLabel("Superieur:"), gbc);

        gbc.gridx = 1;
        JTextField supField = new JTextField(20);
        dashboardFrame.add(supField, gbc);

        // Task buttons in one line
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton addButton = new JButton("Ajouter");
        JButton deleteButton = new JButton("Supprimer");
        JButton modifyButton = new JButton("Modifier");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(clearButton);
        dashboardFrame.add(buttonPanel, gbc);

        // DataGrid view
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 10;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        String[] columnNames = {"Task ID", "Title", "Description", "Agent", "Budget", "Commentaires", "Progression"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        dashboardFrame.add(scrollPane, gbc);

        // Expand button next to DataGrid view
        gbc.gridx = 5;
        gbc.gridy = 10;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        JButton expandButton = new JButton("Expand");
        dashboardFrame.add(expandButton, gbc);

        // Search by Task ID
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        JLabel searchLabel = new JLabel("Search by Task ID:");
        dashboardFrame.add(searchLabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 11;
        JTextField searchBar = new JTextField(15);
        dashboardFrame.add(searchBar, gbc);

        // Search by Agent Name
        gbc.gridx = 2;
        gbc.gridy = 12;
        JLabel searchAgentLabel = new JLabel("Search by Agent Name:");
        dashboardFrame.add(searchAgentLabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 12;
        JTextField searchAgentBar = new JTextField(15);
        dashboardFrame.add(searchAgentBar, gbc);

        // Statistiques and View Credentials buttons in one line
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        JPanel statsCredButtonPanel = new JPanel();
        statsCredButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton statsButton = new JButton("Statistiques");
        JButton viewCredentialsButton = new JButton("View Credentials");
        statsCredButtonPanel.add(statsButton);
        statsCredButtonPanel.add(viewCredentialsButton);
        dashboardFrame.add(statsCredButtonPanel, gbc);

        // Logout button at the bottom
        gbc.gridx = 5;
        gbc.gridy = 13;
        gbc.gridwidth = 1;
        JButton logoutButton = new JButton("Logout");
        dashboardFrame.add(logoutButton, gbc);

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

        searchBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTasksById(searchBar, tableModel);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose();
                UserLoginForm.showUserLoginForm(parentFrame);
            }
        });

        statsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showStatistics();
            }
        });
        viewCredentialsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCredentialsView();
            }
        });

        searchAgentBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTasksByAgentName(searchAgentBar, tableModel);
            }
        });

        expandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame expandFrame = new JFrame("Expanded DataGrid View");
                expandFrame.setSize(1000, 600);
                JTable expandedTable = new JTable(tableModel);
                expandFrame.add(new JScrollPane(expandedTable));
                expandFrame.setVisible(true);
            }
        });

        taskTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    taskIdField.setText(taskTable.getValueAt(selectedRow, 0).toString());
                    titleField.setText(taskTable.getValueAt(selectedRow, 1).toString());
                    descriptionField.setText(taskTable.getValueAt(selectedRow, 2).toString());
                    agentComboBox.setSelectedItem(taskTable.getValueAt(selectedRow, 3).toString());
                    budgetField.setText(taskTable.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetFields(taskIdField, titleField, descriptionField, departmentComboBox, agentComboBox, budgetField, supField);
            }
        });
    }

    private static void loadDepartments(JComboBox<String> departmentComboBox) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT libelle FROM department";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    departmentComboBox.addItem(rs.getString("libelle"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT MAX(id) From Tache";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return String.valueOf(rs.getInt(1) + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "1";
    }

    private static String getSuperieurFullName(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT NomComplete FROM superieur WHERE login = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("NomComplete");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

  private static void loadTasks(DefaultTableModel tableModel) {
    tableModel.setRowCount(0); // Clear the table
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
        String query = "SELECT t.id, Titre, t.Description, t.Agent, budget, c.comment as commentaires, c.progression FROM tache t, commentaires c where t.id = c.Id_Tache";
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
            String query = "INSERT INTO tache (id ,Titre, Description, Agent, budget, superieur) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, id);
                stmt.setString(2, title);
                stmt.setString(3, description);
                stmt.setString(4, agentName);
                stmt.setString(5, budget);
                stmt.setString(6, sup);
                stmt.executeUpdate();
            }
            String query2 = "INSERT INTO commentaires (comment, Agent, Id_Tache,progression) VALUES (?, ?, ?,?)";
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
                String query2 = "DELETE FROM commentaires WHERE id_tache = ?";
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

    private static void searchTasksById(JTextField searchBar, DefaultTableModel tableModel) {
       String searchQuery = searchBar.getText();
        if (!searchQuery.isEmpty()) {
            tableModel.setRowCount(0); // Clear the table
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String query = "SELECT t.id, Titre, t.Description, t.Agent, budget, c.comment as commentaires, c.progression FROM tache t, commentaires c where t.id = c.Id_Tache AND t.id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, searchQuery);
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0);
                    while (rs.next()) {
                        Object[] row = {
                                rs.getInt("id"),
                                rs.getString("Titre"),
                                rs.getString("description"),
                                rs.getString("agent"),
                                rs.getDouble("budget"),
                                rs.getString("commentaires"),
                                rs.getString("progression")
                        };
                        tableModel.addRow(row);
                    }
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            loadTasks(tableModel); // Reload all tasks if search bar is empty
        }
    }

    private static void searchTasksByAgentName(JTextField searchAgentBar, DefaultTableModel tableModel) {
        String searchText = searchAgentBar.getText();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT t.id, Titre, t.Description, t.Agent, budget, c.comment as commentaires, c.progression FROM tache t, commentaires c where t.id = c.Id_Tache AND t.Agent LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, "%" + searchText + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0);
                    while (rs.next()) {
                        Object[] row = {
                                rs.getInt("id"),
                                rs.getString("Titre"),
                                rs.getString("description"),
                                rs.getString("agent"),
                                rs.getDouble("budget"),
                                rs.getString("commentaires"),
                                rs.getString("progression")
                        };
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static void resetFields(JTextField taskIdField, JTextField titleField, JTextField descriptionField,
                                    JComboBox<String> departmentComboBox, JComboBox<String> agentComboBox, JTextField budgetField, JTextField supField) {
        taskIdField.setText(getNextTaskId());
        titleField.setText("");
        descriptionField.setText("");
        departmentComboBox.setSelectedIndex(-1);
        agentComboBox.removeAllItems();
        budgetField.setText("");
        supField.setText(superieurFullName);
    }
    
    
 
     
     
     
   private static void showStatistics() {
        JFrame statsFrame = new JFrame("Task Progression Statistics");
        statsFrame.setSize(800, 600);
        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Retrieve data from the database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT Titre, progression FROM tache t JOIN commentaires c ON t.id = c.Id_Tache";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String title = rs.getString("Titre");
                    int progression = rs.getInt("progression");
                    dataset.addValue(progression, "Progression", title);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create the chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Task Progression",
                "Task",
                "Progression",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        statsFrame.setContentPane(chartPanel);

        statsFrame.setVisible(true);
    }
 
 private static void showCredentialsView() {
    JFrame credentialsFrame = new JFrame("Credentials View");
    credentialsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    credentialsFrame.setSize(600, 400);

    // Create a table to display credentials
    String[] columnNames = {"Name", "Username", "Email", "Phone"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    JTable credentialsTable = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(credentialsTable);
    credentialsFrame.add(scrollPane);

    // Load credentials from the database
    loadCredentials(tableModel);

    credentialsFrame.setVisible(true);
}

private static void loadCredentials(DefaultTableModel tableModel) {
    // Fetch credentials from the database and populate the table
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
        String query = "SELECT NomComplete, login, email, numero_tel FROM agent UNION SELECT NomComplete, login, email,numero_tel FROM superieur";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                        rs.getString("NomComplete"),
                        rs.getString("login"),
                        rs.getString("email"),
                        rs.getString("numero_tel")
                };
                tableModel.addRow(row);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}