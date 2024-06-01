package onepproject;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AgentDashboard extends JFrame {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/onep_db";
    private static final String DATABASE_USER = "root";  // MySQL username
    private static final String DATABASE_PASSWORD = "";  // MySQL password

    private JLabel taskIdValueLabel;
    private JTextField commentField;
    private JComboBox<String> progressionComboBox;
    private JTextField taskTitleSearchField;
    private JTable taskTable;

    private Color backgroundColor = new Color(245, 245, 245); // Light gray
    private Color panelColor = new Color(220, 220, 220); // Slightly darker gray
    private Color buttonColor = new Color(70, 130, 180); // Steel blue
    private Color buttonTextColor = Color.WHITE;
    private Color columnColor1 = new Color(245, 245, 245); // Light gray
    private Color columnColor2 = new Color(204, 229, 255); // Light blue
    private Color selectedRowColor = new Color(135, 206, 250); // Light sky blue

    private Font labelFont = new Font("Arial", Font.BOLD, 14);
    private Font buttonFont = new Font("Arial", Font.BOLD, 12);

    private Object[][] getTaskDataFromDatabase() {
        String query = "SELECT tache.id, Titre, Description, superieur, commentaires.comment, progression, reponse FROM tache LEFT JOIN commentaires ON tache.id = commentaires.Id_Tache";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery(query)) {

            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();

            Object[][] data = new Object[rowCount][7]; // 7 columns in the table
            int row = 0;
            while (resultSet.next()) {
                data[row][0] = resultSet.getInt("id");
                data[row][1] = resultSet.getString("Titre");
                data[row][2] = resultSet.getString("Description");
                data[row][3] = resultSet.getString("superieur");
                data[row][4] = resultSet.getString("comment");
                data[row][5] = resultSet.getInt("progression");
                data[row][6] = resultSet.getString("reponse");
                row++;
            }
            return data;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching data from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return new Object[0][0];
        }
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
                            progressionComboBox.setSelectedItem("Not Started");
                            break;
                        case 50:
                            progressionComboBox.setSelectedItem("In Progress");
                            break;
                        case 100:
                            progressionComboBox.setSelectedItem("Completed");
                            break;
                        default:
                            progressionComboBox.setSelectedItem("Not Started");
                    }
                } else {
                    commentField.setText("");
                    progressionComboBox.setSelectedItem("Not Started");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching comment and progression from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateCommentInDatabase(int taskId, String comment, String progression, String agent) {
        int numericProgression = calculateProgression(progression);

        String query = "UPDATE commentaires SET comment=?, progression=?, Agent=? WHERE Id_Tache=?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment);
            preparedStatement.setInt(2, numericProgression);
            preparedStatement.setString(3, agent);
            preparedStatement.setInt(4, taskId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Comment updated successfully!");
                refreshTaskTable();
            } else {
                JOptionPane.showMessageDialog(this, "No task found with id " + taskId, "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating comment in database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int calculateProgression(String progressionLabel) {
        switch (progressionLabel) {
            case "Not Started":
                return 0;
            case "In Progress":
                return 50;
            case "Completed":
                return 100;
            default:
                return 0;
        }
    }

    private void fetchTaskByTitle(String title) {
        String query = "SELECT tache.id, Titre, Description, superieur, commentaires.comment, progression, reponse FROM tache LEFT JOIN commentaires ON tache.id = commentaires.Id_Tache WHERE Titre LIKE ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Object[][] data = new Object[1][7];
                    data[0][0] = resultSet.getInt("id");
                    data[0][1] = resultSet.getString("Titre");
                    data[0][2] = resultSet.getString("Description");
                    data[0][3] = resultSet.getString("superieur");
                    data[0][4] = resultSet.getString("comment");
                    data[0][5] = resultSet.getInt("progression");
                    data[0][6] = resultSet.getString("reponse");
                    DefaultTableModel tableModel = new DefaultTableModel(data, new String[]{"Task ID", "Title", "Description", "Superieur", "Comment", "Progression", "Response"});
                    taskTable.setModel(tableModel);
                    applyCustomCellRenderer();  // Apply custom cell renderer
                    taskIdValueLabel.setText(String.valueOf(resultSet.getInt("id")));  // Set Task ID value
                } else {
                    Object[][] data = getTaskDataFromDatabase();
                    DefaultTableModel tableModel = new DefaultTableModel(data, new String[]{"Task ID", "Title", "Description", "Superieur", "Comment", "Progression", "Response"});
                    taskTable.setModel(tableModel);
                    applyCustomCellRenderer();  // Apply custom cell renderer
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching task from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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

    private void refreshTaskTable() {
        Object[][] data = getTaskDataFromDatabase();
        DefaultTableModel tableModel = new DefaultTableModel(data, new String[]{"Task ID", "Title", "Description", "Superieur", "Comment", "Progression", "Response"});
        taskTable.setModel(tableModel);
        applyCustomCellRenderer();
    }

    public AgentDashboard(String username) {
        setTitle("Agent Dashboard - Welcome " + username);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);

        JLabel welcomeLabel = new JLabel("Welcome to the Agent Dashboard", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        String[] columnNames = {"Task ID", "Title", "Description", "Superieur", "Comment", "Progression", "Response"};
        Object[][] data = getTaskDataFromDatabase();

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
        JLabel searchLabel = new JLabel("Search Tasks by Title:");
        searchLabel.setFont(labelFont);
        taskTitleSearchField = new JTextField(10);
        taskTitleSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchTask();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchTask();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchTask();
            }

            private void searchTask() {
                String taskTitleText = taskTitleSearchField.getText();
                if (!taskTitleText.isEmpty()) {
                    fetchTaskByTitle(taskTitleText);
                } else {
                    Object[][] data = getTaskDataFromDatabase();
                    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
                    taskTable.setModel(tableModel);
                    applyCustomCellRenderer();  // Apply custom cell renderer
                }
            }
        });
        searchPanel.add(searchLabel);
        searchPanel.add(taskTitleSearchField);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new BorderLayout(10, 10));
        buttonPanel.setBackground(panelColor);

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        fieldsPanel.setBackground(panelColor);

        JLabel taskIdLabel = new JLabel("Task ID:");
        taskIdLabel.setFont(labelFont);
        taskIdValueLabel = new JLabel("");
        taskIdValueLabel.setFont(labelFont);
        fieldsPanel.add(taskIdLabel);
        fieldsPanel.add(taskIdValueLabel);

        JLabel commentLabel = new JLabel("Comment:");
        commentLabel.setFont(labelFont);
        commentField = new JTextField();
        commentField.setPreferredSize(new Dimension(100, 25));
        fieldsPanel.add(commentLabel);
        fieldsPanel.add(commentField);

        JLabel progressionLabel = new JLabel("Progression:");
        progressionLabel.setFont(labelFont);
        String[] progressionOptions = {"Not Started", "In Progress", "Completed"};
        progressionComboBox = new JComboBox<>(progressionOptions);
        fieldsPanel.add(progressionLabel);
        fieldsPanel.add(progressionComboBox);

        buttonPanel.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonsSubPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsSubPanel.setBackground(panelColor);

        JButton sendCommentButton = new JButton("Send");
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
                    JOptionPane.showMessageDialog(null, "Please select a task to comment on.");
                }
            }
        });
        buttonsSubPanel.add(sendCommentButton);

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

    public static void showAgentDashboard(String username) {
        SwingUtilities.invokeLater(() -> new AgentDashboard(username));
        
    }

}
