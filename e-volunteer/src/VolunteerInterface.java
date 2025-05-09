import com.toedter.calendar.JDateChooser;
import connectionSQL.DBConnection;
import dataAccess.DBAccess;
import dataObject.service;
import dataObject.serviceSchedule;
import dataObject.volunteer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class VolunteerInterface {
    private static JFrame currentFrame;
    private static int loggedInVolunteerId;

    private static void disposeCurrentFrame() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }

    public static void showVolunteerLoginFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Volunteer Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Volunteer Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setOpaque(false);

        JTextField volunteerIdField = new JTextField();
        JTextField contactField = new JTextField();

        volunteerIdField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        contactField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel volunteerIdLabel = new JLabel("Volunteer ID:");
        JLabel contactLabel = new JLabel("Contact Number:");

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
                int volunteerId = Integer.parseInt(volunteerIdField.getText().trim());
                String contact = contactField.getText().replaceAll("[^0-9]", "");
                if (contact.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid contact number");
                } else {
                    if (DBAccess.loginVolunteer(volunteerId, contact)) {
                        loggedInVolunteerId = volunteerId;
                        showVolunteerHomeFrame();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid Volunteer ID or Contact");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Volunteer ID must be a number");
            }
        });

        backButton.addActionListener(e -> {
            disposeCurrentFrame();
            Main.showLoginFrame();
        });

        inputPanel.add(volunteerIdLabel);
        inputPanel.add(volunteerIdField);
        inputPanel.add(contactLabel);
        inputPanel.add(contactField);

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

    public static void showVolunteerSignupFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Volunteer Sign Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Volunteer Sign Up", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setOpaque(false);

        JTextField fnameField = new JTextField();
        JTextField lnameField = new JTextField();
        JDateChooser birthDateChooser = new JDateChooser();
        birthDateChooser.setMaxSelectableDate(new Date());
        JTextField contactField = new JTextField();

        fnameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        lnameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        contactField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel fnameLabel = new JLabel("First Name:");
        JLabel lnameLabel = new JLabel("Last Name:");
        JLabel birthDateLabel = new JLabel("Birth Date:");
        JLabel contactLabel = new JLabel("Contact Number:");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        saveButton.setPreferredSize(new Dimension(150, 40));
        backButton.setPreferredSize(new Dimension(150, 40));

        saveButton.setBackground(new Color(211, 211, 211));
        backButton.setBackground(new Color(211, 211, 211));
        saveButton.setForeground(Color.BLACK);
        backButton.setForeground(Color.BLACK);

        saveButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setBorder(BorderFactory.createEmptyBorder());

        saveButton.addActionListener(e -> {
            String contact = contactField.getText().replaceAll("[^0-9]", "");
            Date birthDate = birthDateChooser.getDate();

            if (birthDate == null) {
                JOptionPane.showMessageDialog(frame, "Please select a valid birth date.");
                return;
            }

            String formattedBirthDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(birthDate);

            volunteer newVolunteer = DBAccess.signupVolunteer(
                    fnameField.getText(),
                    lnameField.getText(),
                    formattedBirthDate,
                    contact
            );
            if (newVolunteer != null) {
                loggedInVolunteerId = newVolunteer.getVolId();
                JOptionPane.showMessageDialog(frame, "Sign up successful! Your Volunteer ID is: " + newVolunteer.getVolId());
                showVolunteerHomeFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Sign up failed. Please check your details.");
            }
        });

        backButton.addActionListener(e -> {
            disposeCurrentFrame();
            Main.showLoginFrame();
        });

        inputPanel.add(fnameLabel);
        inputPanel.add(fnameField);
        inputPanel.add(lnameLabel);
        inputPanel.add(lnameField);
        inputPanel.add(birthDateLabel);
        inputPanel.add(birthDateChooser);
        inputPanel.add(contactLabel);
        inputPanel.add(contactField);

        buttonPanel.add(saveButton);
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

    private static void showVolunteerHomeFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Volunteer Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("WELCOME TO E-VOLUNTEER PARA SA BAYAN", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton servicesButton = new JButton("Services");
        JButton participationButton = new JButton("Participation");
        JButton profileButton = new JButton("Profile");
        JButton logoutButton = new JButton("Log Out");

        servicesButton.addActionListener(e -> showServicesFrame());
        participationButton.addActionListener(e -> showParticipationFrame());
        profileButton.addActionListener(e -> showProfileFrame());
        logoutButton.addActionListener(e -> {
            disposeCurrentFrame();
            Main.showLoginFrame();
        });

        JButton[] buttons = {servicesButton, participationButton, profileButton, logoutButton};
        for (JButton button : buttons) {
            button.setBackground(new Color(230, 230, 230));
            button.setForeground(Color.BLACK);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setPreferredSize(new Dimension(120, 40));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(210, 210, 210));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(230, 230, 230));
                }
            });
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }

        frame.add(welcomeLabel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        currentFrame = frame;
    }

    private static void showServicesFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Services");
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

        JPanel servicesPanel = new JPanel();
        servicesPanel.setBackground(Color.WHITE);
        servicesPanel.setBorder(new LineBorder(Color.BLACK, 2));
        servicesPanel.setLayout(new GridBagLayout());
        GridBagConstraints servicesGbc = new GridBagConstraints();
        servicesGbc.insets = new Insets(15, 15, 15, 15);
        servicesGbc.fill = GridBagConstraints.HORIZONTAL;

        JButton backButton = new JButton("<");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        servicesGbc.gridx = 0;
        servicesGbc.gridy = 0;
        servicesGbc.anchor = GridBagConstraints.WEST;
        servicesPanel.add(backButton, servicesGbc);

        JLabel servicesLabel = new JLabel("SERVICES", SwingConstants.CENTER);
        servicesLabel.setFont(new Font("Arial", Font.BOLD, 28));
        servicesLabel.setForeground(Color.BLACK);
        servicesGbc.gridx = 0;
        servicesGbc.gridy = 1;
        servicesGbc.gridwidth = 3;
        servicesGbc.anchor = GridBagConstraints.CENTER;
        servicesPanel.add(servicesLabel, servicesGbc);

        List<service> servicesList = DBAccess.getServices();
        String[] serviceNames = servicesList.stream()
                .map(svc -> svc.getServiceID() + " - " + svc.getServiceDetails() + " - " + svc.getMaxNumVol())
                .toArray(String[]::new);
        JComboBox<String> serviceCombo = new JComboBox<>(serviceNames);
        serviceCombo.setFont(new Font("Arial", Font.PLAIN, 18));
        serviceCombo.setForeground(Color.BLACK);
        servicesGbc.gridy = 2;
        servicesGbc.gridwidth = 3;
        servicesPanel.add(serviceCombo, servicesGbc);

        JLabel scheduleLabel = new JLabel("Choose Schedule", SwingConstants.LEFT);
        scheduleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scheduleLabel.setForeground(Color.BLACK);
        servicesGbc.gridy = 3;
        servicesPanel.add(scheduleLabel, servicesGbc);

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 18));
        searchField.setForeground(Color.BLACK);
        servicesGbc.gridy = 4;
        servicesGbc.gridwidth = 2;
        servicesPanel.add(searchField, servicesGbc);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 18));
        searchButton.setBackground(Color.WHITE);
        searchButton.setForeground(Color.BLACK);
        searchButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        servicesGbc.gridx = 2;
        servicesGbc.gridwidth = 1;
        servicesPanel.add(searchButton, servicesGbc);

        String[] columns = {"SCHEDULE ID", "DATE", "START", "END", "VENUE", "SLOTS"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable scheduleTable = new JTable(tableModel);
        configureTable(scheduleTable);
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        servicesGbc.gridx = 0;
        servicesGbc.gridy = 5;
        servicesGbc.gridwidth = 3;
        servicesGbc.fill = GridBagConstraints.BOTH;
        servicesGbc.weightx = 1.0;
        servicesGbc.weighty = 1.0;
        servicesPanel.add(scrollPane, servicesGbc);

        JButton bookButton = new JButton("Book");
        bookButton.setFont(new Font("Arial", Font.BOLD, 18));
        bookButton.setBackground(new Color(50, 50, 50));
        bookButton.setForeground(Color.WHITE);
        bookButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        servicesGbc.gridy = 6;
        servicesGbc.fill = GridBagConstraints.HORIZONTAL;
        servicesGbc.weightx = 0;
        servicesGbc.weighty = 0;
        servicesPanel.add(bookButton, servicesGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(servicesPanel, gbc);

        backButton.addActionListener(e -> showVolunteerHomeFrame());

        ActionListener loadSchedules = e -> {
            tableModel.setRowCount(0);
            String selectedService = (String) serviceCombo.getSelectedItem();
            if (selectedService != null) {
                int serviceId = Integer.parseInt(selectedService.split(" - ")[0]);
                List<serviceSchedule> schedules = DBAccess.searchSchedules(serviceId, searchField.getText(), loggedInVolunteerId);
                for (serviceSchedule schedule : schedules) {
                    String slots = schedule.getSlots() + "/" + schedule.getMaxSlots();
                    tableModel.addRow(new Object[]{
                            schedule.getServSchedId(),
                            schedule.getDate(),
                            schedule.getTimeStart(),
                            schedule.getTimeEnd(),
                            schedule.getVenue(),
                            slots
                    });
                }
                if (schedules.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No schedules found for the selected service.");
                }
            }
        };

        serviceCombo.addActionListener(e -> {
            searchField.setText("");
            loadSchedules.actionPerformed(e);
        });

        searchButton.addActionListener(loadSchedules);

        bookButton.addActionListener(e -> {
            int selectedRow = scheduleTable.getSelectedRow();
            if (selectedRow >= 0) {
                int scheduleId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                if (DBAccess.bookService(loggedInVolunteerId, scheduleId)) {
                    JOptionPane.showMessageDialog(frame, "Service booked successfully!");
                    loadSchedules.actionPerformed(e);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to book service. You may have already booked this schedule or no slots are available.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a schedule.");
            }
        });

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        currentFrame = frame;

        if (serviceCombo.getItemCount() > 0) {
            serviceCombo.setSelectedIndex(0);
            loadSchedules.actionPerformed(null);
        }
    }

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

    private static void showParticipationFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Participation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        String[] columns = {"SCHEDULE ID", "SERVICE", "DATE", "TIME START", "TIME END", "VENUE", "STATUS"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable participationTable = new JTable(tableModel);
        configureTable(participationTable);
        JScrollPane scrollPane = new JScrollPane(participationTable);
        JButton cancelButton = new JButton("Cancel");
        JButton backButton = new JButton("Back");

        searchButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            List<Object[]> participations = DBAccess.getParticipations(loggedInVolunteerId, searchField.getText());
            for (Object[] participation : participations) {
                tableModel.addRow(new Object[]{
                        participation[0],
                        participation[1],
                        participation[2],
                        participation[3],
                        participation[4],
                        participation[5],
                        participation[6]
                });
            }
            if (participations.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No participations found.");
            }
        });

        cancelButton.addActionListener(e -> {
            int selectedRow = participationTable.getSelectedRow();
            if (selectedRow >= 0) {
                int scheduleId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                String status = tableModel.getValueAt(selectedRow, 6).toString();
                if (status.equals("Pending")) {
                    if (DBAccess.cancelParticipation(loggedInVolunteerId, scheduleId)) {
                        JOptionPane.showMessageDialog(frame, "Participation cancelled successfully!");
                        searchButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to cancel participation.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Can only cancel pending participations.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a participation.");
            }
        });

        backButton.addActionListener(e -> showVolunteerHomeFrame());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Volunteer ID: " + loggedInVolunteerId));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(cancelButton, BorderLayout.SOUTH);
        frame.add(backButton, BorderLayout.EAST);
        frame.setVisible(true);
        currentFrame = frame;

        searchButton.doClick();
    }

    private static void showProfileFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("Profile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(7, 2, 10, 10));
        frame.setLocationRelativeTo(null);

        volunteer profile = DBAccess.getProfile(loggedInVolunteerId);
        if (profile == null) {
            JOptionPane.showMessageDialog(frame, "Failed to load profile.");
            showVolunteerHomeFrame();
            return;
        }

        JTextField idField = new JTextField(String.valueOf(profile.getVolId()));
        idField.setEditable(false);
        JTextField fnameField = new JTextField(profile.getVolFname());
        JTextField lnameField = new JTextField(profile.getVolLname());
        JTextField birthDateField = new JTextField("YYYY-MM-DD");
        JTextField contactField = new JTextField(profile.getVolContact());
        JButton updateButton = new JButton("Update");
        JButton backButton = new JButton("Back");

        updateButton.addActionListener(e -> {
            volunteer updatedProfile = new volunteer();
            updatedProfile.setVolId(profile.getVolId());
            updatedProfile.setVolFname(fnameField.getText());
            updatedProfile.setVolLname(lnameField.getText());
            updatedProfile.setVolContact(contactField.getText().replaceAll("[^0-9]", ""));
            if (DBAccess.updateProfile(updatedProfile)) {
                JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update profile.");
            }
        });

        backButton.addActionListener(e -> showVolunteerHomeFrame());

        frame.add(new JLabel("Volunteer ID:"));
        frame.add(idField);
        frame.add(new JLabel("First Name:"));
        frame.add(fnameField);
        frame.add(new JLabel("Last Name:"));
        frame.add(lnameField);
        frame.add(new JLabel("Birth Date (YYYY-MM-DD):"));
        frame.add(birthDateField);
        frame.add(new JLabel("Contact Number:"));
        frame.add(contactField);
        frame.add(updateButton);
        frame.add(backButton);
        frame.setVisible(true);
        currentFrame = frame;
    }
}