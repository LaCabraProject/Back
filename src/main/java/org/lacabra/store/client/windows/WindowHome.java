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
    private int currentIndex = 0;
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
        JMenu ropa = new JMenu("Ropa");
        //ropa.setPreferredSize(new Dimension(menuBar.getHeight(),(int) Math.floor(menuBar.getWidth()/6)));
        //menuBar.add(ropa);
        JMenu hogar = new JMenu("Hogar");
        //hogar.setPreferredSize(new Dimension(menuBar.getHeight(),(int)menuBar.getWidth()/6));
        //menuBar.add(hogar);
        JMenu oficina = new JMenu("Oficina");
        //oficina.setPreferredSize(new Dimension(menuBar.getHeight(),(int)menuBar.getWidth()/6));
        //menuBar.add(oficina);
        JMenu decoracion = new JMenu("Decoración");
        //decoracion.setPreferredSize(new Dimension(menuBar.getHeight(),(int)menuBar.getWidth()/6));
        //menuBar.add(decoracion);
        JMenu accesorios = new JMenu("Accesorios");
        //accesorios.setPreferredSize(new Dimension(menuBar.getHeight(),(int)menuBar.getWidth()/6));
        //menuBar.add(accesorios);

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

        //ropa
        JMenuItem ropa0 = new JMenuItem("Todos");
        ropa.add(ropa0);
        JMenuItem ropa1 = new JMenuItem("Camisetas");
        ropa.add(ropa1);
        JMenuItem ropa2 = new JMenuItem("Sudaderas");
        ropa.add(ropa2);
        JMenuItem ropa3 = new JMenuItem("Pantalones");
        ropa.add(ropa3);
        JMenuItem ropa4 = new JMenuItem("Gorras");
        ropa.add(ropa4);
        JMenuItem ropa5 = new JMenuItem("Faldas");
        ropa.add(ropa5);
        JMenuItem ropa6 = new JMenuItem("Legging");
        ropa.add(ropa6);
        JMenuItem ropa7 = new JMenuItem("Calcetines");
        ropa.add(ropa7);

        //hogar
        JMenuItem hogar0 = new JMenuItem("Todos");
        hogar.add(hogar0);
        JMenuItem hogar1 = new JMenuItem("Cojines");
        hogar.add(hogar1);
        JMenuItem hogar2 = new JMenuItem("Relojes");
        hogar.add(hogar2);
        JMenuItem hogar3 = new JMenuItem("Tazas");
        hogar.add(hogar3);
        JMenuItem hogar4 = new JMenuItem("Mantas");
        hogar.add(hogar4);
        JMenuItem hogar5 = new JMenuItem("Delantales");
        hogar.add(hogar5);
        JMenuItem hogar6 = new JMenuItem("Posavasos");
        hogar.add(hogar6);
        JMenuItem hogar7 = new JMenuItem("Imanes");
        hogar.add(hogar7);

        //oficina
        JMenuItem oficina0 = new JMenuItem("Todos");
        oficina.add(oficina0);
        JMenuItem oficina1 = new JMenuItem("Cuadernos");
        oficina.add(oficina1);
        JMenuItem oficina2 = new JMenuItem("Calendarios");
        oficina.add(oficina2);
        JMenuItem oficina3 = new JMenuItem("Pegatinas");
        oficina.add(oficina3);
        JMenuItem oficina4 = new JMenuItem("Tarjetas de visita");
        oficina.add(oficina4);
        JMenuItem oficina5 = new JMenuItem("Libretas");
        oficina.add(oficina5);
        JMenuItem oficina6 = new JMenuItem("Bolígrafos");
        oficina.add(oficina6);
        JMenuItem oficina7 = new JMenuItem("Mousepads");
        oficina.add(oficina7);

        //decoraciones
        JMenuItem decoracion0 = new JMenuItem("Todos");
        decoracion.add(decoracion0);
        JMenuItem decoracion1 = new JMenuItem("Cuadros");
        decoracion.add(decoracion1);
        JMenuItem decoracion2 = new JMenuItem("Lienzos");
        decoracion.add(decoracion2);
        JMenuItem decoracion3 = new JMenuItem("Espejos");
        decoracion.add(decoracion3);
        JMenuItem decoracion4 = new JMenuItem("Vinilos");
        decoracion.add(decoracion4);
        JMenuItem decoracion5 = new JMenuItem("Pósters");
        decoracion.add(decoracion5);
        JMenuItem decoracion6 = new JMenuItem("Lonas");
        decoracion.add(decoracion6);
        JMenuItem decoracion7 = new JMenuItem("Relojes de pared");
        decoracion.add(decoracion7);

        //accesorios
        JMenuItem accesorios0 = new JMenuItem("Todos");
        accesorios.add(accesorios0);
        JMenuItem accesorios1 = new JMenuItem("Mochilas");
        accesorios.add(accesorios1);
        JMenuItem accesorios2 = new JMenuItem("Bolsos de mano");
        accesorios.add(accesorios2);
        JMenuItem accesorios3 = new JMenuItem("Colgantes");
        accesorios.add(accesorios3);
        JMenuItem accesorios4 = new JMenuItem("Bolsos de viaje");
        accesorios.add(accesorios4);
        JMenuItem accesorios5 = new JMenuItem("Estuches");
        accesorios.add(accesorios5);
        JMenuItem accesorios6 = new JMenuItem("Pulsera");
        accesorios.add(accesorios6);
        JMenuItem accesorios7 = new JMenuItem("Carteras");
        accesorios.add(accesorios7);


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

        // Inicializar el temporizador para el desplazamiento automático
        timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextCard();
            }
        });
        timer.start();

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

    private void showNextCard() {
        int numCards = carouselPanel.getComponentCount();
        currentIndex = (currentIndex + 1) % numCards;
        scrollPane.getHorizontalScrollBar().setValue(currentIndex * scrollPane.getViewport().getViewSize().width / numCards);
    }
}