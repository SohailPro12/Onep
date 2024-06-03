package onepproject;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.PatternSyntaxException;

public class AgentDashboard extends JFrame {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/onep_db";
    private static final String DATABASE_USER = "root";  // MySQL username
    private static final String DATABASE_PASSWORD = "";  // MySQL password

    private JLabel taskIdValueLabel;
    private JTextField commentField;
    private JComboBox<String> progressionComboBox ;

    private JTable taskTable;
    private JTextField searchField; // Added search field


    private Color backgroundColor = new Color(250, 250, 250); // Very light gray
    private Color panelColor = new Color(245, 245, 245); // Slightly darker gray
    private Color buttonColor = new Color(70, 130, 180); // Steel blue
    private Color buttonTextColor = Color.WHITE;
    private Color columnColor1 = new Color(255, 255, 255); // White
    private Color columnColor2 = new Color(230, 230, 250); // Lavender
    private Color selectedRowColor = new Color(173, 216, 230); // Light blue

    private Font labelFont = new Font("Arial", Font.BOLD, 14);
    private Font buttonFont = new Font("Arial", Font.BOLD, 12);
    

    

    private Object[][] getTaskDataFromDatabase(String username) {
        String AgentFullName = getAgentFullName(username);
        Object[][] data = null;
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            String query = "SELECT tache.id, Titre, Description, superieur, commentaires.comment, progression,reponse FROM tache LEFT JOIN commentaires ON tache.id = commentaires.Id_Tache WHERE tache.Agent = '" + AgentFullName + "'";
            ResultSet rs = stmt.executeQuery(query);

            rs.last();  // Move to the last row
            int rowCount = rs.getRow();  // Get the row count
            rs.beforeFirst();  // Move back to the first row

            data = new Object[rowCount][7];
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getInt("id");
                data[i][1] = rs.getString("Titre");
                data[i][2] = rs.getString("Description");
                data[i][3] = rs.getString("Superieur");
                data[i][4] = rs.getString("Comment");
                data[i][5] = rs.getString("Progression");
                data[i][6] = rs.getString("reponse");
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des données de la base de données: " + e.getMessage());
        }
        return data;
    }

    private String getAgentFullName(String username) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String query = "SELECT NomComplete FROM agent WHERE login = ?";
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

    private void applyCustomCellRenderer() {
        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    cellComponent.setBackground(selectedRowColor);
                } else {
                    if (row % 2 == 0) {
                        cellComponent.setBackground(columnColor1);
                    } else {
                        cellComponent.setBackground(columnColor2);
                    }
                }
                return cellComponent;
            }
        };

        for (int i = 0; i < taskTable.getColumnCount(); i++) {
            taskTable.getColumnModel().getColumn(i).setCellRenderer(tableCellRenderer);
        }
    }

    public static void showAgentDashboard(String username) {
        SwingUtilities.invokeLater(() -> new AgentDashboard(username));
    }

    public AgentDashboard(String username) {
        setTitle("Tableau de bord des agents ");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);

        JLabel welcomeLabel = new JLabel("Bienvenue a Votre Gestion des Tâches ,"+username, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID de tâche", "Title", "Description", "Superieur", "Commentaires", "Progression", "Response"};
        Object[][] data = getTaskDataFromDatabase(username);

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable = new JTable(tableModel);

        taskTable.setFillsViewportHeight(true);
        taskTable.setPreferredScrollableViewportSize(new Dimension(800, 300));
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        applyCustomCellRenderer();  // Apply custom cell renderer initially

        taskTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    int taskId = Integer.parseInt(taskTable.getValueAt(selectedRow, 0).toString());
                    taskIdValueLabel.setText(String.valueOf(taskId));
                    fetchCommentAndProgression(taskId);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(panelColor);

        JLabel searchLabel = new JLabel("Recherche:");
        searchLabel.setFont(labelFont);
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 25));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchTasks();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchTasks();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchTasks();
            }
        });
        searchPanel.add(searchField);

        mainPanel.add(searchPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new BorderLayout(10, 10));
        buttonPanel.setBackground(panelColor);

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        fieldsPanel.setBackground(panelColor);

        JLabel taskIdLabel = new JLabel("ID de tâche:");
        taskIdLabel.setFont(labelFont);
        taskIdValueLabel = new JLabel("");
        taskIdValueLabel.setFont(labelFont);
        fieldsPanel.add(taskIdLabel);
        fieldsPanel.add(taskIdValueLabel);

        JLabel commentLabel = new JLabel("Commentaire:");
        commentLabel.setFont(labelFont);
        commentField = new JTextField();
        commentField.setPreferredSize(new Dimension(100, 25));
        fieldsPanel.add(commentLabel);
        fieldsPanel.add(commentField);

        JLabel progressionLabel = new JLabel("Progression:");
        progressionLabel.setFont(labelFont);
        String[] progressionOptions = {"Pas commencé", "En cours", "Complété"};
        progressionComboBox = new JComboBox<>(progressionOptions);
        fieldsPanel.add(progressionLabel);
        fieldsPanel.add(progressionComboBox);

        buttonPanel.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonsSubPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsSubPanel.setBackground(panelColor);

        JButton sendCommentButton = new JButton("Envoyer");
        sendCommentButton.setBackground(buttonColor);
        sendCommentButton.setForeground(buttonTextColor);
        sendCommentButton.setFont(buttonFont);
        sendCommentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    int taskId = Integer.parseInt(taskTable.getValueAt(selectedRow, 0).toString());
                    String comment = commentField.getText();
                    String progression = progressionComboBox.getSelectedItem().toString();
                    updateCommentInDatabase(taskId, comment, progression, username);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une tâche sur laquelle commenter.");
                }
            }
        });
        buttonsSubPanel.add(sendCommentButton);

        JButton expandButton = new JButton("Expand");
        expandButton.setBackground(buttonColor);
        expandButton.setForeground(buttonTextColor);
        expandButton.setFont(buttonFont);
        expandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    int taskId = Integer.parseInt(taskTable.getValueAt(selectedRow, 0).toString());
                    showTaskDetails(taskId);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une tâche svp.");
                }
            }
        });
        buttonsSubPanel.add(expandButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(buttonColor);
        logoutButton.setForeground(buttonTextColor);
        logoutButton.setFont(buttonFont);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserLoginForm.showUserLoginForm(new JFrame());
            }
        });
        buttonsSubPanel.add(logoutButton);

        buttonPanel.add(buttonsSubPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void fetchCommentAndProgression(int taskId) {
        String query = "SELECT comment, progression, reponse FROM commentaires WHERE Id_Tache = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, taskId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String comment = resultSet.getString("comment");
                    int progression = resultSet.getInt("progression");

                    commentField.setText(comment);
                    switch (progression) {
                        case 0:
                            progressionComboBox.setSelectedItem("");
                            break;
                        case 1:
                            progressionComboBox.setSelectedItem("En cours");
                            break;
                        case 2:
                            progressionComboBox.setSelectedItem("Complété");
                            break;
                        default:
                            progressionComboBox.setSelectedItem("Pas commencé");
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching comment and progression: " + e.getMessage());
        }
    }

    private void updateCommentInDatabase(int taskId, String comment, String progression, String username) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            // Update comment in commentaires table
            String updateQuery = "UPDATE commentaires SET Comment = ?, Progression = ?, Agent = ? WHERE Id_Tache = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, comment);
                // Calculate progression value
                int calculatedProgression = calculateProgression(progression);
                preparedStatement.setInt(2, calculatedProgression);
                preparedStatement.setString(3, username);
                preparedStatement.setInt(4, taskId);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Commentaire mis à jour avec succès.");

                // Update the specific cells in the table instead of refreshing the entire table
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Update comment and progression columns
                    taskTable.setValueAt(comment, selectedRow, 4);
                    taskTable.setValueAt(progression, selectedRow, 5);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour du commentaire : " + e.getMessage());
        }
    }

    private int calculateProgression(String progressionLabel) {
        switch (progressionLabel) {
            case "Pas commencé":
                return 0;
            case "En cours":
                return 50;
            case "Complété":
                return 100;
            default:
                return 0;
        }
    }

    private void searchTasks() {
        String searchText = searchField.getText().trim();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(taskTable.getModel());
        taskTable.setRowSorter(sorter);
        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
            } catch (PatternSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

 private void showTaskDetails(int taskId) {
    JDialog detailDialog = new JDialog(this, "Détails de la tâche", true);
    detailDialog.setSize(500, 400);
    detailDialog.setLocationRelativeTo(this);

    JPanel detailPanel = new JPanel(new GridBagLayout());
    detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    detailPanel.setBackground(backgroundColor);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;

    // Fetch task details from the database
    String query = "SELECT * FROM tache WHERE id = ?";
    try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, taskId);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                String title = resultSet.getString("Titre");
                String description = resultSet.getString("Description");
                String superieur = resultSet.getString("Superieur");
                String agent = resultSet.getString("Agent");

                addDetail(detailPanel, gbc, "ID de tâche: ", String.valueOf(taskId), 0);
                addDetail(detailPanel, gbc, "Titre: ", title, 1);
                addDetail(detailPanel, gbc, "Description: ", description, 2);
                addDetail(detailPanel, gbc, "Superieur: ", superieur, 3);
                addDetail(detailPanel, gbc, "Agent: ", agent, 4);

                // Fetch comments, progression, and response
                String commentQuery = "SELECT comment, progression, reponse FROM commentaires WHERE Id_Tache = ?";
                try (PreparedStatement commentStmt = connection.prepareStatement(commentQuery)) {
                    commentStmt.setInt(1, taskId);
                    try (ResultSet commentRs = commentStmt.executeQuery()) {
                        if (commentRs.next()) {
                            String comment = commentRs.getString("comment");
                            String progression = commentRs.getString("progression");
                            String reponse = commentRs.getString("reponse");

                            addDetail(detailPanel, gbc, "Commentaire: ", comment, 5);
                            addDetail(detailPanel, gbc, "Progression: ", progression, 6);
                            addDetail(detailPanel, gbc, "Reponse: ", reponse, 7);
                        }
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des détails de la tâche: " + e.getMessage());
    }

    detailDialog.add(detailPanel);
    detailDialog.setVisible(true);
}

private void addDetail(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
    gbc.gridx = 0;
    gbc.gridy = row;
    JLabel labelComponent = new JLabel(label);
    labelComponent.setFont(labelFont);
    panel.add(labelComponent, gbc);

    gbc.gridx = 1;
    JLabel valueComponent = new JLabel(value);
    valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
    valueComponent.setForeground(Color.DARK_GRAY);
    panel.add(valueComponent, gbc);
}
}