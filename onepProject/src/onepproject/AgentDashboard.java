package onepproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgentDashboard extends JFrame {
    private JTextField taskNumberField;
    private JTextField commentField;
    private JComboBox<String> statusComboBox;
    private JTable taskTable;
    private DefaultTableModel model;

    public AgentDashboard(String username) {
        setTitle("Agent Dashboard - Welcome " + username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel for task modification
        JPanel modificationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel taskNumberLabel = new JLabel("Task Number:");
        modificationPanel.add(taskNumberLabel, gbc);

        gbc.gridx = 1;
        taskNumberField = new JTextField(20);
        modificationPanel.add(taskNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel commentLabel = new JLabel("Comment:");
        modificationPanel.add(commentLabel, gbc);

        gbc.gridx = 1;
        commentField = new JTextField(20);
        modificationPanel.add(commentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel statusLabel = new JLabel("Status:");
        modificationPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        String[] statusOptions = {"Pending", "In Progress", "Completed"};
        statusComboBox = new JComboBox<>(statusOptions);
        modificationPanel.add(statusComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton modifyButton = new JButton("Modify");
        modificationPanel.add(modifyButton, gbc);

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement modify task logic here
                String taskNumber = taskNumberField.getText();
                String comment = commentField.getText();
                String status = (String) statusComboBox.getSelectedItem();

                // Update the task in the table based on taskNumber
                boolean taskFound = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(taskNumber)) {
                        model.setValueAt(comment, i, 1); // Assuming comment goes to description column
                        model.setValueAt(status, i, 2);  // Assuming status goes to assigned from superior column
                        taskFound = true;
                        break;
                    }
                }

                if (!taskFound) {
                    JOptionPane.showMessageDialog(AgentDashboard.this, 
                        "Task Number not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Panel for task display
        JPanel taskPanel = new JPanel(new BorderLayout());
        
        // Title for the data grid view
        JLabel taskTableTitle = new JLabel("Task List", JLabel.CENTER);
        taskTableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        taskPanel.add(taskTableTitle, BorderLayout.NORTH);
        
        String[] columnNames = {"Task Number", "Task Description", "Status"};
        Object[][] data = {
                {"1", "Task 1 Description", "Pending"},
                {"2", "Task 2 Description", "In Progress"}
                // Add more rows as needed
        };
        model = new DefaultTableModel(data, columnNames);
        taskTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        taskPanel.add(scrollPane, BorderLayout.CENTER);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Implement logout logic here
                // For example, showing the login form
                // or closing the application
            }
        });

        // Adding titles and making the interface more professional
        JLabel headerLabel = new JLabel("Agent Task Management Dashboard", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        mainPanel.add(modificationPanel, BorderLayout.WEST);
        mainPanel.add(taskPanel, BorderLayout.CENTER);
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    public static void showAgentDashboard(String username) {
        SwingUtilities.invokeLater(() -> new AgentDashboard(username));
    }
}
