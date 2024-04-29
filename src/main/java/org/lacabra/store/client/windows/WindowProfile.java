package org.lacabra.store.client.windows;

import javax.swing.*;
import java.awt.*;

public class WindowProfile extends JFrame {
    private JLabel etiquetaNombre, etiquetaCorreo, etiquetaContraseña, etiquetaDireccion, etiquetaTelefono, etiquetaPago,
            etiquetaNumeroTarjeta, etiquetaFechaCaducidad, etiquetaCVV, etiquetaNombreTarjeta;
    private JTextField campoNombre, campoCorreo, campoDireccion, campoTelefono, campoNumeroTarjeta,
            campoFechaCaducidad, campoCVV, campoNombreTarjeta, campoContraseña;
    private JCheckBox checkBoxBoletin, checkBoxProgramaLealtad;
    private JComboBox<String> comboBoxIdioma, comboBoxMoneda;

    public WindowProfile() {
        super("Perfil de Usuario");
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Crear paneles para cada sección
        JPanel panelInfoUsuario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelInfoUsuario.setBorder(BorderFactory.createTitledBorder("Información de Usuario"));
        panelInfoUsuario.add(new JLabel("Nombre:"));
        panelInfoUsuario.add(campoNombre = new JTextField());
        panelInfoUsuario.add(new JLabel("Correo:"));
        panelInfoUsuario.add(campoCorreo = new JTextField());
        panelInfoUsuario.add(new JLabel("Contraseña:"));
        panelInfoUsuario.add(campoContraseña = new JPasswordField());
        panelInfoUsuario.add(new JLabel("Dirección:"));
        panelInfoUsuario.add(campoDireccion = new JTextField());
        panelInfoUsuario.add(new JLabel("Teléfono:"));
        panelInfoUsuario.add(campoTelefono = new JTextField());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = new Insets(10, 10, 10, 10);
        add(panelInfoUsuario, constraints);

        JPanel panelInfoPago = new JPanel(new GridLayout(5, 2, 5, 5));
        panelInfoPago.setBorder(BorderFactory.createTitledBorder("Información de Pago"));
        panelInfoPago.add(new JLabel("Método de Pago:"));
        panelInfoPago.add(etiquetaPago = new JLabel("Tarjeta de Crédito"));
        panelInfoPago.add(new JLabel("Número de Tarjeta:"));
        panelInfoPago.add(campoNumeroTarjeta = new JTextField());
        panelInfoPago.add(new JLabel("Fecha de Caducidad:"));
        panelInfoPago.add(campoFechaCaducidad = new JTextField());
        panelInfoPago.add(new JLabel("CVV:"));
        panelInfoPago.add(campoCVV = new JTextField());
        panelInfoPago.add(new JLabel("Nombre en la Tarjeta:"));
        panelInfoPago.add(campoNombreTarjeta = new JTextField());
        constraints.gridy = 1;
        constraints.weighty = 1;
        add(panelInfoPago, constraints);

        JPanel panelPreferencias = new JPanel(new BorderLayout());
        panelPreferencias.setBorder(BorderFactory.createTitledBorder("Preferencias"));
        JPanel panelDetallesPreferencias = new JPanel(new GridLayout(4, 1, 5, 5));
        checkBoxBoletin = new JCheckBox("Suscribirse al boletín");
        checkBoxProgramaLealtad = new JCheckBox("Participar en el programa de lealtad");
        comboBoxIdioma = new JComboBox<>(new String[]{"Inglés", "Español", "Francés", "Alemán"});
        comboBoxMoneda = new JComboBox<>(new String[]{"USD", "EUR", "GBP", "JPY"});
        panelDetallesPreferencias.add(checkBoxBoletin);
        panelDetallesPreferencias.add(checkBoxProgramaLealtad);
        panelDetallesPreferencias.add(comboBoxIdioma);
        panelDetallesPreferencias.add(comboBoxMoneda);
        panelPreferencias.add(panelDetallesPreferencias, BorderLayout.CENTER);
        constraints.gridy = 2;
        constraints.weighty = 1;
        add(panelPreferencias, constraints);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WindowProfile::new);
    }
}
