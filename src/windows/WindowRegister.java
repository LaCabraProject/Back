package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowRegister extends JFrame {

    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox newsletterCheckbox;

    public WindowRegister() {
        initUI();
    }

    private void initUI() {
        setTitle("Crea una cuenta");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7, 1));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createEmailLabelAndField());
        mainPanel.add(createUsernameLabelAndField());
        mainPanel.add(createPasswordLabelAndField());
        mainPanel.add(createNewsletterCheckbox());
        mainPanel.add(createRegisterButton());
        mainPanel.add(createGoBackButton());

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createEmailLabelAndField() {
        JLabel emailLabel = new JLabel("Correo electrónico");
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30));

        JPanel panel = new JPanel();
        panel.add(emailLabel);
        panel.add(emailField);

        return panel;
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

    private JCheckBox createNewsletterCheckbox() {
        newsletterCheckbox = new JCheckBox("Enviadme ofertas especiales y novedades");
        newsletterCheckbox.setPreferredSize(new Dimension(200, 30));

        return newsletterCheckbox;
    }

    private JButton createRegisterButton() {
        JButton registerButton = new JButton("Regístrate");
        registerButton.setPreferredSize(new Dimension(200, 30));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                boolean subscribeToNewsletter = newsletterCheckbox.isSelected();

                // TODO: Implement registration logic here

                JOptionPane.showMessageDialog(WindowRegister.this, "¡Has sido registrado exitosamente! Revisa tu bandeja de entrada para encontrar el descuento en tu primer pedido.");
            }
        });

        return registerButton;
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

    public static void main(String[] args) {
        WindowRegister registerWindow = new WindowRegister();
        registerWindow.setVisible(true);
    }
}