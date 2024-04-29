package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.controller.MainController;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowRegister extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<Authority> selectionComboBox;
    private MainController mc;

    public WindowRegister(MainController mc) {
        initUI(mc);
        this.mc = mc;
    }

    public static void main(String[] args) {
        MainController mc = new MainController();
        WindowRegister registerWindow = new WindowRegister(mc);
        registerWindow.setVisible(true);
    }

    private void initUI(MainController mc) {
        setTitle("Crea una cuenta");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 1));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createUsernameLabelAndField());
        mainPanel.add(createPasswordLabelAndField());
        mainPanel.add(createNewsletterCheckbox());
        mainPanel.add(createRegisterButton());
        mainPanel.add(createGoBackButton());

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
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

    private JPanel createNewsletterCheckbox() {
        JPanel checkBoxPanel = new JPanel();
        JLabel checkBoxlabel = new JLabel("Tipo de usuario:");
        Authority[] options = {Authority.Artist, Authority.Client};
        selectionComboBox = new JComboBox(options);
        selectionComboBox.setPreferredSize(new Dimension(120, 20));
        checkBoxPanel.add(checkBoxlabel);
        checkBoxPanel.add(selectionComboBox);

        return checkBoxPanel;
    }

    private JButton createRegisterButton() {
        JButton registerButton = new JButton("Regístrate");
        registerButton.setPreferredSize(new Dimension(200, 30));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                Authority selected = (Authority) selectionComboBox.getSelectedItem();
                UserId userId = UserId.from(username);
                Credentials cre = new Credentials(userId.get(), selected, password);
                User usuario = new User(cre);
//                mc.PutUser(usuario);
                JOptionPane.showMessageDialog(WindowRegister.this, "¡Has sido registrado exitosamente! Revisa tu " +
                        "bandeja de entrada para encontrar el descuento en tu primer pedido.");
                dispose();
                new WindowHome(usuario, mc);
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
                WindowHome mainFrame = new WindowHome(null, mc);
                mainFrame.setVisible(true);
            }
        });
        JPanel jp = new JPanel();
        jp.add(back);
        return jp;
    }
}