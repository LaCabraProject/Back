package org.lacabra.store.client.windows;

import org.lacabra.store.client.Controller.MainController;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WindowHome extends JFrame {
    private JPanel carouselPanel;
    private JScrollPane scrollPane;
    private Timer timer;

    public WindowHome(User usuario, MainController mc) {
        initUI(usuario, mc);
    }

    public static void main(String[] args) {
        MainController mc = new MainController();
        new WindowHome(null, mc);
    }
    private void initUI(User usuario, MainController mc) {
        setTitle("GOAT");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu searchIcon = new JMenu();
        JMenuItem searchItem = new JMenuItem("Buscar");
        searchItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (usuario != null) {
                    new WindowShopping(usuario, mc);
                } else {
                    new WindowShopping(null, mc);
                }

            }
        });
        searchIcon.add(searchItem);
        BufferedImage image0 = null;
        try {
            image0 = ImageIO.read(new File("src/main/java/org/lacabra/store/client/img/lupa.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Image SI = image0.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        searchIcon.setIcon(new ImageIcon(SI));
        menuBar.add(searchIcon);

        //cuenta
        if (usuario == null) {
            JMenu iniciar = new JMenu("Iniciar sesión");
            menuBar.add(iniciar);
            JMenuItem signIn = new JMenuItem("Iniciar sesión");
            iniciar.add(signIn);
            JMenuItem register = new JMenuItem("Crear cuenta");
            iniciar.add(register);
            signIn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new WindowLogin(mc);
                }
            });
            register.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new WindowRegister(mc);
                }
            });
        } else {
            JMenu cuenta = new JMenu("Cuenta");
            menuBar.add(cuenta);
            JMenuItem cuentaInfo = new JMenuItem("Ver perfil");
            cuenta.add(cuentaInfo);
            JMenuItem cuentaCompras = new JMenuItem("Mis compras");
            cuenta.add(cuentaCompras);
            JMenuItem cuentaVentas = new JMenuItem("Mis ventas");
            if (usuario.authorities().contains(Authority.Artist) || usuario.authorities().contains(Authority.Admin) || usuario.authorities().contains(Authority.Artist)) {
                cuenta.add(cuentaVentas);
                cuentaVentas.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new WindowSalesStall(usuario, mc);
                    }
                });
            }
            JMenuItem signOut = new JMenuItem("Cerrar sesión", KeyEvent.VK_S);
            cuenta.add(signOut);
            cuenta.setMnemonic(KeyEvent.VK_M);
            signOut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new WindowHome(null, mc);
                }
            });
        }

        // Create and add a banner
        //rgb(253,1,10) original
        JLabel banner = new JLabel(new ImageIcon("src/main/java/org/lacabra/store/client/img/logo.png"));
        JPanel bannerPanel = new JPanel();
        bannerPanel.setLayout(new BorderLayout());
        bannerPanel.add(banner, BorderLayout.CENTER);
        bannerPanel.setPreferredSize(new Dimension(getWidth(), banner.getPreferredSize().height));
        bannerPanel.setBackground(new Color(253, 5, 20, 215));
        add(bannerPanel, BorderLayout.NORTH);

        // Create and add a panel for the main content
        carouselPanel = new JPanel();
        carouselPanel.setLayout(new GridLayout(1, 1));
        carouselPanel.setPreferredSize(new Dimension(200, 200));
        scrollPane = new JScrollPane(carouselPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Agregar los elementos del carrusel
        for (int i = 1; i <= 5; i++) {
            JLabel label = new JLabel("Item " + i);
            label.setHorizontalAlignment(JLabel.CENTER); // Centra el texto en el JLabel
            carouselPanel.add(label);
        }

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        add(footerPanel, BorderLayout.SOUTH);

        // Add some example components to the footer panel
        footerPanel.add(new JLabel("Siguenos en Facebook"), BorderLayout.WEST);
        JButton socialMedia = new JButton();
        socialMedia.setPreferredSize(new Dimension(90, 30));
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/main/java/org/lacabra/store/client/img/facebook.png"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Image scaledImage = image.getScaledInstance(90, 30, Image.SCALE_SMOOTH);
        socialMedia.setIcon(new ImageIcon(scaledImage));
        socialMedia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "https://www.facebook.com/Redbubble";
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
                } catch (java.io.IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        footerPanel.add(socialMedia, BorderLayout.EAST);
        setVisible(true);
    }
}