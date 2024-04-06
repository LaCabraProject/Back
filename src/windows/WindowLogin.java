package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowLogin extends JFrame {
	
    private static final String EXIT_ON_CLOSE = "EXIT_ON_CLOSE";

    private JTextField usernameField;
    private JPasswordField passwordField;

    public WindowLogin() {
        initUI();
    }

    private void initUI() {
        setTitle("Iniciar sesión");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel loginPanel = createLoginPanel();
        add(loginPanel, BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);

        setIconImage(new ImageIcon("icon.png").getImage());
        setVisible(true); 
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(7, 1));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        loginPanel.add(createUsernameLabelAndField());
        loginPanel.add(createPasswordLabelAndField());
        loginPanel.add(createForgotPasswordLabel());
        loginPanel.add(createRememberCheckbox());
        loginPanel.add(createLoginButton());
        loginPanel.add(createSignUpLabel());
        loginPanel.add(createGoBackButton());

        return loginPanel;
    }

    private JPanel createUsernameLabelAndField() {
        JLabel usernameLabel = new JLabel("Nombre de usuario");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));

        JPanel panel = new JPanel();
        panel.add(usernameLabel);
        panel.add(usernameField);

        return panel;
    }

    private JPanel createPasswordLabelAndField() {
        JLabel passwordLabel = new JLabel("Contraseña");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));

        JPanel panel = new JPanel();
        panel.add(passwordLabel);
        panel.add(passwordField);

        return panel;
    }

    private JLabel createForgotPasswordLabel() {
        JLabel forgotPasswordLabel = new JLabel("¿Has olvidado tu contraseña?");
        forgotPasswordLabel.setForeground(Color.BLUE);
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(WindowLogin.this, "Contacta con nosotros para recuperar tu contraseña.");
            }
        });

        return forgotPasswordLabel;
    }

    private JCheckBox createRememberCheckbox() {
        JCheckBox rememberCheckbox = new JCheckBox("Recordar");
        rememberCheckbox.setPreferredSize(new Dimension(200, 30));

        return rememberCheckbox;
    }

    private JButton createLoginButton() {
        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.setPreferredSize(new Dimension(200, 30));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("admin") && password.equals("admin")) {
                    dispose();
                    WindowHome mainFrame = new WindowHome(true);
                    mainFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(WindowLogin.this, "Nombre de usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return loginButton;
    }

    private JLabel createSignUpLabel() {
        JLabel signUpLabel = new JLabel("¿Aún no tienes una cuenta? Regístrate.");
        signUpLabel.setForeground(Color.BLUE);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(WindowLogin.this, "Contacta con nosotros para crear una cuenta.");
            }
        });

        return signUpLabel;
    }
    
    private JPanel createGoBackButton() {
        JButton back = new JButton("Volver al inicio");
        back.setPreferredSize(new Dimension(120, 20));
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                WindowHome mainFrame = new WindowHome(false);
                mainFrame.setVisible(true);                
            }
        });
        JPanel jp= new JPanel();
        jp.add(back);
        return jp;
    }

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