package org.lacabra.store.client.windows;

import org.lacabra.store.client.Controller.MainController;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowLogin extends JFrame {
    private static final String EXIT_ON_CLOSE = "EXIT_ON_CLOSE";
    private MainController mc;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public WindowLogin(MainController mc) {
        initUI(mc);
        this.mc = mc;
    }

    public static void main(String[] args) {
        MainController mc = new MainController();
        WindowLogin loginFrame = new WindowLogin(mc);
        loginFrame.setVisible(true);
    }

    private void initUI(MainController mc) {
        setTitle("Iniciar sesión");
        setSize(400, 400);
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
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);

        loginPanel.add(createUsernameLabelAndField(), constraints);
        constraints.gridy++;
        loginPanel.add(createPasswordLabelAndField(), constraints);
        constraints.gridy++;
        loginPanel.add(createLoginButton(), constraints);
        constraints.gridy++;
        loginPanel.add(createSignUpLabel(), constraints);
        constraints.gridy++;
        loginPanel.add(createGoBackButton(), constraints);

        return loginPanel;
    }

    private JPanel createUsernameLabelAndField() {
        JLabel usernameLabel = new JLabel("Usuario");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));

        JPanel panel = new JPanel();
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.setBorder(new EmptyBorder(0, 22, 0, 0));
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
                    User usuario = null;
                    usuario = new User(new Credentials(username, Authority.Admin, password));
                    WindowHome mainFrame = new WindowHome(usuario, mc);
                    mainFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(WindowLogin.this, "Nombre de usuario o contraseña incorrectos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
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
                dispose();
                new WindowRegister(mc);
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
                WindowHome mainFrame = new WindowHome(null, mc);
                mainFrame.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.add(back);
        return panel;
    }

    private void setDefaultCloseOperation(String exitOnClose) {
        if (exitOnClose.equals(EXIT_ON_CLOSE)) {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
}