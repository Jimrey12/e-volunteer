import connectionSQL.DBConnection;
import dataAccess.DBAccess;
import dataObject.service;
import dataObject.serviceSchedule;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminInterface {
    private static JFrame currentFrame;
    private static int loggedInAdminId;

    private static void disposeCurrentFrame() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }

    public static void showAdminLoginFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Admin Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setOpaque(false);

        JTextField adminIdField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        adminIdField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel adminIdLabel = new JLabel("Admin ID:");
        JLabel passwordLabel = new JLabel("Password:");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        loginButton.setPreferredSize(new Dimension(150, 40));
        backButton.setPreferredSize(new Dimension(150, 40));

        loginButton.setBackground(new Color(211, 211, 211));
        backButton.setBackground(new Color(211, 211, 211));
        loginButton.setForeground(Color.BLACK);
        backButton.setForeground(Color.BLACK);

        loginButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setBorder(BorderFactory.createEmptyBorder());

        loginButton.addActionListener(e -> {
            try {
                int adminId = Integer.parseInt(adminIdField.getText());
                String password = new String(passwordField.getPassword());
                if (DBAccess.loginAdmin(adminId, password)) {
                    loggedInAdminId = adminId;
                    showAdminHomeFrame();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Admin ID or Password");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Admin ID must be a number");
            }
        });

        backButton.addActionListener(e -> {
            disposeCurrentFrame();
            Main.showLoginFrame();
        });

        inputPanel.add(adminIdLabel);
        inputPanel.add(adminIdField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(inputPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(buttonPanel);

        frame.add(contentPanel);
        frame.setVisible(true);
        currentFrame = frame;
    }

    private static void showAdminHomeFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Admin Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("WELCOME TO E-VOLUNTEER PARA SA BAYAN", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton manageServicesButton = new JButton("Manage Services");
        JButton beneficiariesButton = new JButton("Beneficiaries");
        JButton resourcesButton = new JButton("Resources");
        JButton manageParticipationsButton = new JButton("Manage Participations");
        JButton logoutButton = new JButton("Log Out");

        manageServicesButton.addActionListener(e -> showManageServicesFrame());
        beneficiariesButton.addActionListener(e -> showBeneficiariesFrame());
        resourcesButton.addActionListener(e -> showResourcesFrame());
        manageParticipationsButton.addActionListener(e -> showManageParticipationsFrame());
        logoutButton.addActionListener(e -> {
            disposeCurrentFrame();
            Main.showLoginFrame();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5, 10, 10));
        buttonPanel.add(manageServicesButton);
        buttonPanel.add(beneficiariesButton);
        buttonPanel.add(resourcesButton);
        buttonPanel.add(manageParticipationsButton);
        buttonPanel.add(logoutButton);

        frame.add(welcomeLabel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        currentFrame = frame;
    }

    public static void showManageParticipationsFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Manage Volunteer Participations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        String[] columns = {"SCHEDULE ID", "VOLUNTEER ID", "SERVICE", "DATE", "TIME START", "TIME END", "VENUE", "STATUS"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable participationTable = new JTable(tableModel);
        configureTable(participationTable);
        JScrollPane scrollPane = new JScrollPane(participationTable);

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        JButton doneButton = new JButton("Mark as Done");
        JButton backButton = new JButton("Back");

        searchButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            List<Object[]> participations = DBAccess.getAllParticipations(searchField.getText());
            for (Object[] participation : participations) {
                tableModel.addRow(new Object[]{
                        participation[0], // Schedule ID
                        participation[1], // Volunteer ID
                        participation[2], // Service
                        participation[3], // Date
                        participation[4], // Time Start
                        participation[5], // Time End
                        participation[6], // Venue
                        participation[7]  // Status
                });
            }
            if (participations.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No participations found.");
            }
        });

        confirmButton.addActionListener(e -> {
            int selectedRow = participationTable.getSelectedRow();
            if (selectedRow >= 0) {
                int scheduleId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                int volunteerId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                String status = tableModel.getValueAt(selectedRow, 7).toString();
                if (status.equals("Pending")) {
                    if (DBAccess.updateParticipationStatus(scheduleId, volunteerId, "Confirmed")) {
                        JOptionPane.showMessageDialog(frame, "Participation confirmed successfully!");
                        searchButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to confirm participation.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Can only confirm pending participations.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a participation.");
            }
        });

        cancelButton.addActionListener(e -> {
            int selectedRow = participationTable.getSelectedRow();
            if (selectedRow >= 0) {
                int scheduleId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                int volunteerId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                String status = tableModel.getValueAt(selectedRow, 7).toString();
                if (status.equals("Pending") || status.equals("Confirmed")) {
                    if (DBAccess.updateParticipationStatus(scheduleId, volunteerId, "Cancelled")) {
                        JOptionPane.showMessageDialog(frame, "Participation cancelled successfully!");
                        searchButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to cancel participation.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Can only cancel pending or confirmed participations.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a participation.");
            }
        });

        doneButton.addActionListener(e -> {
            int selectedRow = participationTable.getSelectedRow();
            if (selectedRow >= 0) {
                int scheduleId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                int volunteerId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                String status = tableModel.getValueAt(selectedRow, 7).toString();
                if (status.equals("Confirmed")) {
                    if (DBAccess.updateParticipationStatus(scheduleId, volunteerId, "Done")) {
                        JOptionPane.showMessageDialog(frame, "Participation marked as done successfully!");
                        searchButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to mark participation as done.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Can only mark confirmed participations as done.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a participation.");
            }
        });

        backButton.addActionListener(e -> showAdminHomeFrame());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search Participations:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(confirmButton);
        bottomPanel.add(cancelButton);
        bottomPanel.add(doneButton);
        bottomPanel.add(backButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
        currentFrame = frame;

        searchButton.doClick();
    }

    private static void showManageServicesFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Manage Services");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Services Panel
        JPanel servicesPanel = new JPanel();
        servicesPanel.setBackground(Color.WHITE);
        servicesPanel.setBorder(new LineBorder(Color.BLACK, 2));
        servicesPanel.setLayout(new GridBagLayout());
        GridBagConstraints servicesGbc = new GridBagConstraints();
        servicesGbc.insets = new Insets(15, 15, 15, 15);
        servicesGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel servicesTitle = new JLabel("Services", SwingConstants.CENTER);
        servicesTitle.setFont(new Font("Arial", Font.BOLD, 20));
        servicesGbc.gridx = 0;
        servicesGbc.gridy = 0;
        servicesGbc.gridwidth = 3;
        servicesPanel.add(servicesTitle, servicesGbc);

        JTextField serviceSearchField = new JTextField(20);
        serviceSearchField.setFont(new Font("Arial", Font.PLAIN, 16));
        servicesGbc.gridy = 1;
        servicesGbc.gridwidth = 1;
        servicesPanel.add(serviceSearchField, servicesGbc);

        JButton addServiceButton = new JButton("+");
        addServiceButton.setFont(new Font("Arial", Font.BOLD, 16));
        addServiceButton.setBackground(Color.WHITE);
        addServiceButton.setForeground(Color.BLACK);
        servicesGbc.gridx = 1;
        servicesPanel.add(addServiceButton, servicesGbc);

        JButton deleteServiceButton = new JButton("Delete");
        deleteServiceButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteServiceButton.setBackground(Color.WHITE);
        deleteServiceButton.setForeground(Color.BLACK);
        servicesGbc.gridx = 2;
        servicesPanel.add(deleteServiceButton, servicesGbc);

        String[] serviceColumns = {"Select", "SERVICE ID", "SERVICE", "MAXIMUM VOLUNTEERS"};
        DefaultTableModel serviceTableModel = new DefaultTableModel(serviceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 3; // Checkbox and MaxNumVolunteers are editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : Object.class; // Checkbox column
            }
        };
        JTable serviceTable = new JTable(serviceTableModel);
        serviceTable.setFont(new Font("Arial", Font.PLAIN, 16));
        serviceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        serviceTable.setRowHeight(30);
        serviceTable.setGridColor(Color.LIGHT_GRAY);
        serviceTable.setShowGrid(true);
        JScrollPane serviceScrollPane = new JScrollPane(serviceTable);
        servicesGbc.gridx = 0;
        servicesGbc.gridy = 2;
        servicesGbc.gridwidth = 3;
        servicesGbc.weightx = 1.0;
        servicesGbc.weighty = 1.0;
        servicesGbc.fill = GridBagConstraints.BOTH;
        servicesPanel.add(serviceScrollPane, servicesGbc);

        // Schedules Panel
        JPanel schedulesPanel = new JPanel();
        schedulesPanel.setBackground(Color.WHITE);
        schedulesPanel.setBorder(new LineBorder(Color.BLACK, 2));
        schedulesPanel.setLayout(new GridBagLayout());
        GridBagConstraints schedulesGbc = new GridBagConstraints();
        schedulesGbc.insets = new Insets(15, 15, 15, 15);
        schedulesGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel schedulesTitle = new JLabel("Schedules", SwingConstants.CENTER);
        schedulesTitle.setFont(new Font("Arial", Font.BOLD, 20));
        schedulesGbc.gridx = 0;
        schedulesGbc.gridy = 0;
        schedulesGbc.gridwidth = 2;
        schedulesPanel.add(schedulesTitle, schedulesGbc);

        JTextField scheduleSearchField = new JTextField(20);
        scheduleSearchField.setFont(new Font("Arial", Font.PLAIN, 16));
        schedulesGbc.gridy = 1;
        schedulesGbc.gridwidth = 1;
        schedulesPanel.add(scheduleSearchField, schedulesGbc);

        JButton addScheduleButton = new JButton("+");
        addScheduleButton.setFont(new Font("Arial", Font.BOLD, 16));
        addScheduleButton.setBackground(Color.WHITE);
        addScheduleButton.setForeground(Color.BLACK);
        schedulesGbc.gridx = 1;
        schedulesPanel.add(addScheduleButton, schedulesGbc);

        String[] scheduleColumns = {"SCHEDULE ID", "SERVICE ID", "DATE", "TIME START", "TIME END", "VENUE"};
        DefaultTableModel scheduleTableModel = new DefaultTableModel(scheduleColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 2; // All columns except IDs are editable
            }
        };
        JTable scheduleTable = new JTable(scheduleTableModel);
        scheduleTable.setFont(new Font("Arial", Font.PLAIN, 16));
        scheduleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        scheduleTable.setRowHeight(30);
        scheduleTable.setGridColor(Color.LIGHT_GRAY);
        scheduleTable.setShowGrid(true);
        JScrollPane scheduleScrollPane = new JScrollPane(scheduleTable);
        schedulesGbc.gridx = 0;
        schedulesGbc.gridy = 2;
        schedulesGbc.gridwidth = 2;
        schedulesGbc.weightx = 1.0;
        schedulesGbc.weighty = 1.0;
        schedulesGbc.fill = GridBagConstraints.BOTH;
        schedulesPanel.add(scheduleScrollPane, schedulesGbc);

        // Done Button
        JButton doneButton = new JButton("DONE");
        doneButton.setFont(new Font("Arial", Font.BOLD, 16));
        doneButton.setBackground(new Color(50, 50, 50));
        doneButton.setForeground(Color.WHITE);
        doneButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(doneButton);

        // Back Button
        JButton backButton = new JButton("<");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(backButton);

        // Load initial data
        loadServices(serviceTableModel);
        loadSchedules(scheduleTableModel);

        // Action Listeners
        serviceSearchField.addActionListener(e -> loadServices(serviceTableModel, serviceSearchField.getText()));
        addServiceButton.addActionListener(e -> addService(serviceTableModel));
        deleteServiceButton.addActionListener(e -> deleteServices(serviceTableModel));
        scheduleSearchField.addActionListener(e -> loadSchedules(scheduleTableModel, scheduleSearchField.getText()));
        addScheduleButton.addActionListener(e -> addSchedule(scheduleTableModel));
        doneButton.addActionListener(e -> saveChanges(serviceTableModel, scheduleTableModel));
        backButton.addActionListener(e -> showAdminHomeFrame());

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(servicesPanel, gbc);
        gbc.gridy = 1;
        mainPanel.add(schedulesPanel, gbc);
        gbc.gridy = 2;
        mainPanel.add(buttonPanel, gbc);
        gbc.gridy = 3;
        mainPanel.add(backPanel, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        currentFrame = frame;
    }

    private static void showBeneficiariesFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Beneficiaries");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Beneficiaries Panel
        JPanel beneficiariesPanel = new JPanel();
        beneficiariesPanel.setBackground(Color.WHITE);
        beneficiariesPanel.setBorder(new LineBorder(Color.BLACK, 2));
        beneficiariesPanel.setLayout(new GridBagLayout());
        GridBagConstraints beneficiariesGbc = new GridBagConstraints();
        beneficiariesGbc.insets = new Insets(15, 15, 15, 15);
        beneficiariesGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel beneficiariesTitle = new JLabel("BENEFICIARY", SwingConstants.CENTER);
        beneficiariesTitle.setFont(new Font("Arial", Font.BOLD, 20));
        beneficiariesGbc.gridx = 0;
        beneficiariesGbc.gridy = 0;
        beneficiariesGbc.gridwidth = 3;
        beneficiariesPanel.add(beneficiariesTitle, beneficiariesGbc);

        JTextField beneficiarySearchField = new JTextField(20);
        beneficiarySearchField.setFont(new Font("Arial", Font.PLAIN, 16));
        beneficiariesGbc.gridy = 1;
        beneficiariesGbc.gridwidth = 1;
        beneficiariesPanel.add(beneficiarySearchField, beneficiariesGbc);

        JButton addBeneficiaryButton = new JButton("ADD");
        addBeneficiaryButton.setFont(new Font("Arial", Font.BOLD, 16));
        addBeneficiaryButton.setBackground(Color.WHITE);
        addBeneficiaryButton.setForeground(Color.BLACK);
        beneficiariesGbc.gridx = 1;
        beneficiariesPanel.add(addBeneficiaryButton, beneficiariesGbc);

        JButton deleteBeneficiaryButton = new JButton("Delete");
        deleteBeneficiaryButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteBeneficiaryButton.setBackground(Color.WHITE);
        deleteBeneficiaryButton.setForeground(Color.BLACK);
        beneficiariesGbc.gridx = 2;
        beneficiariesPanel.add(deleteBeneficiaryButton, beneficiariesGbc);

        String[] beneficiaryColumns = {"Select", "BENEFICIARY ID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT", "TYPE", "SERVICE ID"};
        DefaultTableModel beneficiaryTableModel = new DefaultTableModel(beneficiaryColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || (column >= 2 && column <= 7); // Checkbox and editable fields
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class; // Checkbox column
            }
        };
        JTable beneficiaryTable = new JTable(beneficiaryTableModel);
        beneficiaryTable.setFont(new Font("Arial", Font.PLAIN, 16));
        beneficiaryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        beneficiaryTable.setRowHeight(30);
        beneficiaryTable.setGridColor(Color.LIGHT_GRAY);
        beneficiaryTable.setShowGrid(true);
        JScrollPane beneficiaryScrollPane = new JScrollPane(beneficiaryTable);
        beneficiariesGbc.gridx = 0;
        beneficiariesGbc.gridy = 2;
        beneficiariesGbc.gridwidth = 3;
        beneficiariesGbc.weightx = 1.0;
        beneficiariesGbc.weighty = 1.0;
        beneficiariesGbc.fill = GridBagConstraints.BOTH;
        beneficiariesPanel.add(beneficiaryScrollPane, beneficiariesGbc);

        // Done Button
        JButton doneButton = new JButton("DONE");
        doneButton.setFont(new Font("Arial", Font.BOLD, 16));
        doneButton.setBackground(new Color(50, 50, 50));
        doneButton.setForeground(Color.WHITE);
        doneButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(doneButton);

        // Back Button
        JButton backButton = new JButton("<");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(backButton);

        // Load initial data
        loadBeneficiaries(beneficiaryTableModel);

        // Action Listeners
        beneficiarySearchField.addActionListener(e -> loadBeneficiaries(beneficiaryTableModel, beneficiarySearchField.getText()));
        addBeneficiaryButton.addActionListener(e -> addBeneficiary(beneficiaryTableModel));
        deleteBeneficiaryButton.addActionListener(e -> deleteBeneficiaries(beneficiaryTableModel));
        doneButton.addActionListener(e -> saveBeneficiaryChanges(beneficiaryTableModel));
        backButton.addActionListener(e -> showAdminHomeFrame());

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(beneficiariesPanel, gbc);
        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);
        gbc.gridy = 2;
        mainPanel.add(backPanel, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        currentFrame = frame;
    }

    private static void showResourcesFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Resources");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Resources Panel
        JPanel resourcesPanel = new JPanel();
        resourcesPanel.setBackground(Color.WHITE);
        resourcesPanel.setBorder(new LineBorder(Color.BLACK, 2));
        resourcesPanel.setLayout(new GridBagLayout());
        GridBagConstraints resourcesGbc = new GridBagConstraints();
        resourcesGbc.insets = new Insets(15, 15, 15, 15);
        resourcesGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel resourcesTitle = new JLabel("RESOURCES", SwingConstants.CENTER);
        resourcesTitle.setFont(new Font("Arial", Font.BOLD, 20));
        resourcesGbc.gridx = 0;
        resourcesGbc.gridy = 0;
        resourcesGbc.gridwidth = 3;
        resourcesPanel.add(resourcesTitle, resourcesGbc);

        JTextField resourceSearchField = new JTextField(20);
        resourceSearchField.setFont(new Font("Arial", Font.PLAIN, 16));
        resourcesGbc.gridy = 1;
        resourcesGbc.gridwidth = 1;
        resourcesPanel.add(resourceSearchField, resourcesGbc);

        JButton addResourceButton = new JButton("ADD");
        addResourceButton.setFont(new Font("Arial", Font.BOLD, 16));
        addResourceButton.setBackground(Color.WHITE);
        addResourceButton.setForeground(Color.BLACK);
        resourcesGbc.gridx = 1;
        resourcesPanel.add(addResourceButton, resourcesGbc);

        JButton deleteResourceButton = new JButton("Delete");
        deleteResourceButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteResourceButton.setBackground(Color.WHITE);
        deleteResourceButton.setForeground(Color.BLACK);
        resourcesGbc.gridx = 2;
        resourcesPanel.add(deleteResourceButton, resourcesGbc);

        String[] resourceColumns = {"Select", "RESOURCE ID", "NAME", "QUANTITY", "FUNDS", "DATE ALLOCATED", "SERVICE ID"};
        DefaultTableModel resourceTableModel = new DefaultTableModel(resourceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || (column >= 2 && column <= 6); // Checkbox and editable fields
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class; // Checkbox column
            }
        };
        JTable resourceTable = new JTable(resourceTableModel);
        resourceTable.setFont(new Font("Arial", Font.PLAIN, 16));
        resourceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        resourceTable.setRowHeight(30);
        resourceTable.setGridColor(Color.LIGHT_GRAY);
        resourceTable.setShowGrid(true);
        JScrollPane resourceScrollPane = new JScrollPane(resourceTable);
        resourcesGbc.gridx = 0;
        resourcesGbc.gridy = 2;
        resourcesGbc.gridwidth = 3;
        resourcesGbc.weightx = 1.0;
        resourcesGbc.weighty = 1.0;
        resourcesGbc.fill = GridBagConstraints.BOTH;
        resourcesPanel.add(resourceScrollPane, resourcesGbc);

        // Done Button
        JButton doneButton = new JButton("DONE");
        doneButton.setFont(new Font("Arial", Font.BOLD, 16));
        doneButton.setBackground(new Color(50, 50, 50));
        doneButton.setForeground(Color.WHITE);
        doneButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(doneButton);

        // Back Button
        JButton backButton = new JButton("<");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(backButton);

        // Load initial data
        loadResources(resourceTableModel);

        // Action Listeners
        resourceSearchField.addActionListener(e -> loadResources(resourceTableModel, resourceSearchField.getText()));
        addResourceButton.addActionListener(e -> addResource(resourceTableModel));
        deleteResourceButton.addActionListener(e -> deleteResources(resourceTableModel));
        doneButton.addActionListener(e -> saveResourceChanges(resourceTableModel));
        backButton.addActionListener(e -> showAdminHomeFrame());

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(resourcesPanel, gbc);
        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);
        gbc.gridy = 2;
        mainPanel.add(backPanel, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        currentFrame = frame;
    }

    // Reuse table configuration from VolunteerInterface
    private static void configureTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setRowHeight(30);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setForeground(Color.BLACK);
        renderer.setBackground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            table.getColumnModel().getColumn(i).setPreferredWidth(150);
        }
    }

    private static void loadServices(DefaultTableModel model) {
        model.setRowCount(0);
        List<service> services = DBAccess.getServices();
        for (service s : services) {
            model.addRow(new Object[]{false, s.getServiceID(), s.getServiceDetails(), s.getMaxNumVol()});
        }
    }

    private static void loadServices(DefaultTableModel model, String searchTerm) {
        model.setRowCount(0);
        List<service> services = DBAccess.getServices(searchTerm);
        for (service s : services) {
            model.addRow(new Object[]{false, s.getServiceID(), s.getServiceDetails(), s.getMaxNumVol()});
        }
    }

    private static void loadSchedules(DefaultTableModel model) {
        model.setRowCount(0);
        List<serviceSchedule> schedules = DBAccess.getSchedules();
        for (serviceSchedule s : schedules) {
            model.addRow(new Object[]{s.getServSchedId(), s.getServiceId(), s.getDate(), s.getTimeStart(), s.getTimeEnd(), s.getVenue()});
        }
    }

    private static void loadSchedules(DefaultTableModel model, String searchTerm) {
        model.setRowCount(0);
        List<serviceSchedule> schedules = DBAccess.searchSchedules(0, searchTerm, 0);
        for (serviceSchedule s : schedules) {
            model.addRow(new Object[]{s.getServSchedId(), s.getServiceId(), s.getDate(), s.getTimeStart(), s.getTimeEnd(), s.getVenue()});
        }
    }

    private static void loadBeneficiaries(DefaultTableModel model) {
        model.setRowCount(0);
        List<Object[]> beneficiaries = DBAccess.getBeneficiaries();
        for (Object[] beneficiary : beneficiaries) {
            model.addRow(new Object[]{
                    false, // Checkbox
                    beneficiary[0], // BeneficiaryID
                    beneficiary[1], // BenFname
                    beneficiary[2], // BenLname
                    beneficiary[3], // BenAddress
                    beneficiary[4], // BenContact
                    beneficiary[5], // BenType
                    beneficiary[6]  // ServiceID
            });
        }
    }

    private static void loadBeneficiaries(DefaultTableModel model, String searchTerm) {
        model.setRowCount(0);
        List<Object[]> beneficiaries = DBAccess.getBeneficiaries(searchTerm);
        for (Object[] beneficiary : beneficiaries) {
            model.addRow(new Object[]{
                    false, // Checkbox
                    beneficiary[0], // BeneficiaryID
                    beneficiary[1], // BenFname
                    beneficiary[2], // BenLname
                    beneficiary[3], // BenAddress
                    beneficiary[4], // BenContact
                    beneficiary[5], // BenType
                    beneficiary[6]  // ServiceID
            });
        }
    }

    private static void loadResources(DefaultTableModel model) {
        model.setRowCount(0);
        List<Object[]> resources = DBAccess.getResources();
        for (Object[] resource : resources) {
            model.addRow(new Object[]{
                    false, // Checkbox
                    resource[0], // ResourceId
                    resource[1], // ResourceName
                    resource[2], // ResourceQuantity
                    resource[3], // ResourceFunds
                    resource[4], // ResourceDateAllocated
                    resource[5]  // ServiceID
            });
        }
    }

    private static void loadResources(DefaultTableModel model, String searchTerm) {
        model.setRowCount(0);
        List<Object[]> resources = DBAccess.getResources(searchTerm);
        for (Object[] resource : resources) {
            model.addRow(new Object[]{
                    false, // Checkbox
                    resource[0], // ResourceId
                    resource[1], // ResourceName
                    resource[2], // ResourceQuantity
                    resource[3], // ResourceFunds
                    resource[4], // ResourceDateAllocated
                    resource[5]  // ServiceID
            });
        }
    }

    private static void addService(DefaultTableModel model) {
        String serviceName = JOptionPane.showInputDialog("Enter Service Name:");
        if (serviceName == null || serviceName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Service name cannot be empty!");
            return;
        }
        String maxVolStr = JOptionPane.showInputDialog("Enter Maximum Volunteers:");
        if (maxVolStr == null) return;
        try {
            int maxVol = Integer.parseInt(maxVolStr);
            if (maxVol <= 0) {
                JOptionPane.showMessageDialog(null, "Maximum Volunteers must be positive!");
                return;
            }
            if (DBAccess.addService(serviceName, maxVol)) {
                loadServices(model);
                JOptionPane.showMessageDialog(null, "Service added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add service!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number for Maximum Volunteers!");
        }
    }

    private static void deleteServices(DefaultTableModel model) {
        boolean changesMade = false;
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            boolean isSelected = (boolean) model.getValueAt(i, 0); // Checkbox column
            if (isSelected) {
                int serviceId = Integer.parseInt(model.getValueAt(i, 1).toString());
                if (DBAccess.deleteService(serviceId)) {
                    model.removeRow(i);
                    changesMade = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot delete service ID " + serviceId + ": It may be referenced by schedules, beneficiaries, or resources.");
                }
            }
        }
        if (changesMade) {
            JOptionPane.showMessageDialog(null, "Selected services deleted successfully!");
        } else if (model.getRowCount() > 0) {
            JOptionPane.showMessageDialog(null, "No services were deleted. Ensure services are not referenced.");
        } else {
            JOptionPane.showMessageDialog(null, "No services were selected for deletion.");
        }
    }

    private static void addSchedule(DefaultTableModel model) {
        String serviceIdStr = JOptionPane.showInputDialog("Enter Service ID:");
        if (serviceIdStr == null) return;
        String date = JOptionPane.showInputDialog("Enter Date (YYYY-MM-DD):");
        if (date == null) return;
        String timeStart = JOptionPane.showInputDialog("Enter Time Start (HH:MM:SS):");
        if (timeStart == null) return;
        String timeEnd = JOptionPane.showInputDialog("Enter Time End (HH:MM:SS):");
        if (timeEnd == null) return;
        String venue = JOptionPane.showInputDialog("Enter Venue:");
        if (venue == null || venue.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Venue cannot be empty!");
            return;
        }
        try {
            int serviceId = Integer.parseInt(serviceIdStr);
            // Simulate adding to database (implement DBAccess.addSchedule if needed)
            model.addRow(new Object[]{model.getRowCount() + 2001, serviceId, date, timeStart, timeEnd, venue});
            JOptionPane.showMessageDialog(null, "Schedule added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Service ID!");
        }
    }

    private static void addBeneficiary(DefaultTableModel model) {
        String fname = JOptionPane.showInputDialog("Enter First Name:");
        if (fname == null || fname.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "First name cannot be empty!");
            return;
        }
        String lname = JOptionPane.showInputDialog("Enter Last Name:");
        if (lname == null || lname.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Last name cannot be empty!");
            return;
        }
        String address = JOptionPane.showInputDialog("Enter Address:");
        if (address == null || address.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Address cannot be empty!");
            return;
        }
        String contact = JOptionPane.showInputDialog("Enter Contact Number:");
        if (contact == null || contact.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Contact number cannot be empty!");
            return;
        }
        String type = JOptionPane.showInputDialog("Enter Beneficiary Type (e.g., Community, Barangay, Individual):");
        if (type == null || type.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Beneficiary type cannot be empty!");
            return;
        }
        String serviceIdStr = JOptionPane.showInputDialog("Enter Service ID:");
        if (serviceIdStr == null) return;
        try {
            int serviceId = Integer.parseInt(serviceIdStr);
            boolean added = DBAccess.addBeneficiary(fname, lname, address, contact, type, serviceId);
            if (added) {
                loadBeneficiaries(model);
                JOptionPane.showMessageDialog(null, "Beneficiary added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add beneficiary. Check Service ID or database constraints.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Service ID!");
        }
    }

    private static void deleteBeneficiaries(DefaultTableModel model) {
        boolean changesMade = false;
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            boolean isSelected = (boolean) model.getValueAt(i, 0); // Checkbox column
            if (isSelected) {
                int beneficiaryId = Integer.parseInt(model.getValueAt(i, 1).toString());
                if (DBAccess.deleteBeneficiary(beneficiaryId)) {
                    model.removeRow(i);
                    changesMade = true;
                }
            }
        }
        if (changesMade) {
            JOptionPane.showMessageDialog(null, "Selected beneficiaries deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "No beneficiaries were selected for deletion.");
        }
    }

    private static void addResource(DefaultTableModel model) {
        String name = JOptionPane.showInputDialog("Enter Resource Name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Resource name cannot be empty!");
            return;
        }
        String quantityStr = JOptionPane.showInputDialog("Enter Resource Quantity (leave blank if none):");
        Integer quantity = null;
        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    JOptionPane.showMessageDialog(null, "Quantity cannot be negative!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid number for Quantity!");
                return;
            }
        }
        String fundsStr = JOptionPane.showInputDialog("Enter Resource Funds (PHP, leave blank if none):");
        Double funds = null;
        if (fundsStr != null && !fundsStr.trim().isEmpty()) {
            try {
                funds = Double.parseDouble(fundsStr);
                if (funds < 0) {
                    JOptionPane.showMessageDialog(null, "Funds cannot be negative!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid number for Funds!");
                return;
            }
        }
        String dateAllocated = JOptionPane.showInputDialog("Enter Date Allocated (YYYY-MM-DD):");
        if (dateAllocated == null || dateAllocated.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Date allocated cannot be empty!");
            return;
        }
        String serviceIdStr = JOptionPane.showInputDialog("Enter Service ID:");
        if (serviceIdStr == null) return;
        try {
            int serviceId = Integer.parseInt(serviceIdStr);
            boolean added = DBAccess.addResource(name, quantity, funds, dateAllocated, serviceId);
            if (added) {
                loadResources(model);
                JOptionPane.showMessageDialog(null, "Resource added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add resource. Check Service ID or database constraints.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Service ID!");
        }
    }

    private static void deleteResources(DefaultTableModel model) {
        boolean changesMade = false;
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            boolean isSelected = (boolean) model.getValueAt(i, 0); // Checkbox column
            if (isSelected) {
                int resourceId = Integer.parseInt(model.getValueAt(i, 1).toString());
                if (DBAccess.deleteResource(resourceId)) {
                    model.removeRow(i);
                    changesMade = true;
                }
            }
        }
        if (changesMade) {
            JOptionPane.showMessageDialog(null, "Selected resources deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "No resources were selected for deletion.");
        }
    }

    private static void saveChanges(DefaultTableModel serviceModel, DefaultTableModel scheduleModel) {
        boolean changesMade = false;

        // Update Services
        for (int i = 0; i < serviceModel.getRowCount(); i++) {
            try {
                int serviceId = Integer.parseInt(serviceModel.getValueAt(i, 1).toString());
                String serviceDetails = (String) serviceModel.getValueAt(i, 2);
                if (serviceDetails == null || serviceDetails.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Service details cannot be empty for Service ID: " + serviceId);
                    return;
                }

                int maxVol;
                try {
                    maxVol = Integer.parseInt(serviceModel.getValueAt(i, 3).toString());
                    if (maxVol <= 0) {
                        JOptionPane.showMessageDialog(null, "Maximum Volunteers must be positive for Service ID: " + serviceId);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid number for Maximum Volunteers for Service ID: " + serviceId);
                    return;
                }

                // Update the service in the database
                if (DBAccess.updateService(serviceId, serviceDetails, maxVol)) {
                    changesMade = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update Service ID: " + serviceId);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Service ID at row " + (i + 1));
                return;
            }
        }

        // Update Schedules
        for (int i = 0; i < scheduleModel.getRowCount(); i++) {
            try {
                int schedId = Integer.parseInt(scheduleModel.getValueAt(i, 0).toString());
                int serviceId = Integer.parseInt(scheduleModel.getValueAt(i, 1).toString());
                String date = (String) scheduleModel.getValueAt(i, 2);
                String timeStart = (String) scheduleModel.getValueAt(i, 3);
                String timeEnd = (String) scheduleModel.getValueAt(i, 4);
                String venue = (String) scheduleModel.getValueAt(i, 5);

                // Validate inputs
                if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(null, "Invalid date format for Schedule ID: " + schedId + ". Use YYYY-MM-DD.");
                    return;
                }
                if (timeStart == null || !timeStart.matches("\\d{2}:\\d{2}:\\d{2}")) {
                    JOptionPane.showMessageDialog(null, "Invalid Time Start format for Schedule ID: " + schedId + ". Use HH:MM:SS.");
                    return;
                }
                if (timeEnd == null || !timeEnd.matches("\\d{2}:\\d{2}:\\d{2}")) {
                    JOptionPane.showMessageDialog(null, "Invalid Time End format for Schedule ID: " + schedId + ". Use HH:MM:SS.");
                    return;
                }
                if (venue == null || venue.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Venue cannot be empty for Schedule ID: " + schedId);
                    return;
                }

                // Update the schedule in the database
                if (DBAccess.updateSchedule(schedId, serviceId, date, timeStart, timeEnd, venue)) {
                    changesMade = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update Schedule ID: " + schedId + ". Check if Service ID exists.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Schedule ID or Service ID at row " + (i + 1));
                return;
            }
        }

        if (changesMade) {
            JOptionPane.showMessageDialog(null, "Changes saved successfully!");
            // Reload data to reflect any updates
            loadServices(serviceModel);
            loadSchedules(scheduleModel);
        } else {
            JOptionPane.showMessageDialog(null, "No changes were made.");
        }
    }

    private static void saveBeneficiaryChanges(DefaultTableModel model) {
        boolean changesMade = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                int beneficiaryId = Integer.parseInt(model.getValueAt(i, 1).toString());
                String fname = (String) model.getValueAt(i, 2);
                String lname = (String) model.getValueAt(i, 3);
                String address = (String) model.getValueAt(i, 4);
                String contact = (String) model.getValueAt(i, 5);
                String type = (String) model.getValueAt(i, 6);
                int serviceId = Integer.parseInt(model.getValueAt(i, 7).toString());

                // Validate inputs
                if (fname == null || fname.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "First name cannot be empty for Beneficiary ID: " + beneficiaryId);
                    return;
                }
                if (lname == null || lname.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Last name cannot be empty for Beneficiary ID: " + beneficiaryId);
                    return;
                }
                if (address == null || address.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Address cannot be empty for Beneficiary ID: " + beneficiaryId);
                    return;
                }
                if (contact == null || contact.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Contact cannot be empty for Beneficiary ID: " + beneficiaryId);
                    return;
                }
                if (type == null || type.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Beneficiary type cannot be empty for Beneficiary ID: " + beneficiaryId);
                    return;
                }

                if (DBAccess.updateBeneficiary(beneficiaryId, fname, lname, address, contact, type, serviceId)) {
                    changesMade = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update Beneficiary ID: " + beneficiaryId + ". Check if Service ID exists.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Beneficiary ID or Service ID at row " + (i + 1));
                return;
            }
        }

        if (changesMade) {
            JOptionPane.showMessageDialog(null, "Beneficiary changes saved successfully!");
            loadBeneficiaries(model);
        } else {
            JOptionPane.showMessageDialog(null, "No changes were made to beneficiaries.");
        }
    }

    private static void saveResourceChanges(DefaultTableModel model) {
        boolean changesMade = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                int resourceId = Integer.parseInt(model.getValueAt(i, 1).toString());
                String name = (String) model.getValueAt(i, 2);
                String quantityStr = (String) model.getValueAt(i, 3);
                String fundsStr = (String) model.getValueAt(i, 4);
                String dateAllocated = (String) model.getValueAt(i, 5);
                int serviceId = Integer.parseInt(model.getValueAt(i, 6).toString());

                // Validate inputs
                if (name == null || name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Resource name cannot be empty for Resource ID: " + resourceId);
                    return;
                }
                Integer quantity = null;
                if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity < 0) {
                        JOptionPane.showMessageDialog(null, "Quantity cannot be negative for Resource ID: " + resourceId);
                        return;
                    }
                }
                Double funds = null;
                if (fundsStr != null && !fundsStr.trim().isEmpty()) {
                    funds = Double.parseDouble(fundsStr);
                    if (funds < 0) {
                        JOptionPane.showMessageDialog(null, "Funds cannot be negative for Resource ID: " + resourceId);
                        return;
                    }
                }
                if (dateAllocated == null || !dateAllocated.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(null, "Invalid date format for Resource ID: " + resourceId + ". Use YYYY-MM-DD.");
                    return;
                }

                if (DBAccess.updateResource(resourceId, name, quantity, funds, dateAllocated, serviceId)) {
                    changesMade = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update Resource ID: " + resourceId + ". Check if Service ID exists.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Resource ID, Quantity, Funds, or Service ID at row " + (i + 1));
                return;
            }
        }

        if (changesMade) {
            JOptionPane.showMessageDialog(null, "Resource changes saved successfully!");
            loadResources(model);
        } else {
            JOptionPane.showMessageDialog(null, "No changes were made to resources.");
        }
    }
}