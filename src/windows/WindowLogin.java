package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowLogin extends JFrame {

    // Constants
    private static final String EXIT_ON_CLOSE = "EXIT_ON_CLOSE";

    // Components
    private JTextField usernameField;
    private JPasswordField passwordField;

    public WindowLogin() {
        initUI();
    }

    // Initialize the UI components
    private void initUI() {
        setTitle("Iniciar sesión");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a panel for the login form
        JPanel loginPanel = createLoginPanel();
        add(loginPanel, BorderLayout.CENTER);

        // Set the background color
        getContentPane().setBackground(Color.WHITE);

        // Set the icon image
        setIconImage(new ImageIcon("icon.png").getImage());
    }

    // Create and configure the login panel
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(6, 1));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        loginPanel.add(createUsernameLabelAndField());
        loginPanel.add(createPasswordLabelAndField());
        loginPanel.add(createForgotPasswordLabel());
        loginPanel.add(createRememberCheckbox());
        loginPanel.add(createLoginButton());
        loginPanel.add(createSignUpLabel());

        return loginPanel;
    }

    // Create and configure the username label and field
    private JPanel createUsernameLabelAndField() {
        JLabel usernameLabel = new JLabel("Nombre de usuario");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));

        JPanel panel = new JPanel();
        panel.add(usernameLabel);
        panel.add(usernameField);

        return panel;
    }

    // Create and configure the password label and field
    private JPanel createPasswordLabelAndField() {
        JLabel passwordLabel = new JLabel("Contraseña");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));

        JPanel panel = new JPanel();
        panel.add(passwordLabel);
        panel.add(passwordField);

        return panel;
    }

    // Create and configure the "¿Has olvidado tu contraseña?" label
    private JLabel createForgotPasswordLabel() {
        JLabel forgotPasswordLabel = new JLabel("¿Has olvidado tu contraseña?");
        forgotPasswordLabel.setForeground(Color.BLUE);
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add mouse listener for forgotPasswordLabel
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(WindowLogin.this, "Contacta con nosotros para recuperar tu contraseña.");
            }
        });

        return forgotPasswordLabel;
    }

    // Create and configure the "Recordar" checkbox
    private JCheckBox createRememberCheckbox() {
        JCheckBox rememberCheckbox = new JCheckBox("Recordar");
        rememberCheckbox.setPreferredSize(new Dimension(200, 30));

        return rememberCheckbox;
    }

    // Create and configure the login button
    private JButton createLoginButton() {
        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.setPreferredSize(new Dimension(200, 30));

        // Add action listener for loginButton
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check if the username and password are correct
                if (username.equals("admin") && password.equals("password")) {
                    // If the login is successful, close the login window
                    dispose();

                    // Open the main window
                    WindowHome mainFrame = new WindowHome(true);
                    mainFrame.setVisible(true);
                } else {
                    // If the login is not successful, show an error message
                    JOptionPane.showMessageDialog(WindowLogin.this, "Nombre de usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return loginButton;
    }

    // Create and configure the "¿Aún no tienes una cuenta?" label
    private JLabel createSignUpLabel() {
        JLabel signUpLabel = new JLabel("¿Aún no tienes una cuenta? Regístrate.");
        signUpLabel.setForeground(Color.BLUE);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add mouse listener for signUpLabel
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(WindowLogin.this, "Contacta connosotros para crear una cuenta.");
            }
        });

        return signUpLabel;
    }

    // Set the default close operation
    private void setDefaultCloseOperation(String exitOnClose) {
        if (exitOnClose.equals(EXIT_ON_CLOSE)) {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public static void main(String[] args) {
        WindowLogin loginFrame = new WindowLogin();
        loginFrame.setVisible(true);
    }
}