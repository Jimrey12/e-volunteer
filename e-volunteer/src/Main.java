import connectionSQL.DBConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {
    private static JFrame currentFrame;

    public static void main(String[] args) {
        try {
            DBConnection.connect();
            SwingUtilities.invokeLater(Main::showLoginFrame);
        } catch (Exception e) {
            System.out.println("Application failed to start: " + e.getMessage());
        }
    }

    private static void disposeCurrentFrame() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }

    static void showLoginFrame() {
        disposeCurrentFrame();
        JFrame frame = new JFrame("E-Volunteer Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 280);
        frame.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome to E-Volunteer Para sa Bayan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton adminButton = new JButton("Admin Login");
        JButton volunteerButton = new JButton("Volunteer Login");
        JButton signupButton = new JButton("Volunteer Sign Up");

        adminButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        volunteerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        Color buttonColor = new Color(211, 211, 211);
        Color textColor = Color.BLACK;

        adminButton.setBackground(buttonColor);
        volunteerButton.setBackground(buttonColor);
        signupButton.setBackground(buttonColor);

        adminButton.setForeground(textColor);
        volunteerButton.setForeground(textColor);
        signupButton.setForeground(textColor);

        adminButton.setPreferredSize(new Dimension(220, 60));
        volunteerButton.setPreferredSize(new Dimension(220, 60));
        signupButton.setPreferredSize(new Dimension(220, 60));

        adminButton.setMargin(new Insets(0, 20, 0, 20));
        volunteerButton.setMargin(new Insets(0, 20, 0, 20));
        signupButton.setMargin(new Insets(0, 20, 0, 20));

        adminButton.setBorder(BorderFactory.createEmptyBorder());
        volunteerButton.setBorder(BorderFactory.createEmptyBorder());
        signupButton.setBorder(BorderFactory.createEmptyBorder());

        adminButton.addActionListener(e -> AdminInterface.showAdminLoginFrame());
        volunteerButton.addActionListener(e -> VolunteerInterface.showVolunteerLoginFrame());
        signupButton.addActionListener(e -> VolunteerInterface.showVolunteerSignupFrame());

        buttonPanel.add(adminButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(volunteerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(signupButton);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(buttonPanel);

        frame.add(contentPanel);
        frame.setVisible(true);
        currentFrame = frame;
    }
}