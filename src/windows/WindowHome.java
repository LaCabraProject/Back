package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class WindowHome extends JFrame {
    public WindowHome(boolean autentification) {
        initUI(autentification);
    }

    private void initUI(boolean autentification) {
        setTitle("RedBubble");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu ropa = new JMenu("Ropa");
        //ropa.setPreferredSize(new Dimension(menuBar.getHeight(),(int) Math.floor(menuBar.getWidth()/6)));
        menuBar.add(ropa);        
        JMenu hogar = new JMenu("Hogar");
        //hogar.setPreferredSize(new Dimension(menuBar.getHeight(),(int)menuBar.getWidth()/6));
        menuBar.add(hogar);
        JMenu oficina = new JMenu("Oficina");
        //oficina.setPreferredSize(new Dimension(menuBar.getHeight(),(int)menuBar.getWidth()/6));
        menuBar.add(oficina);
        JMenu decoracion = new JMenu("Decoración");
        //decoracion.setPreferredSize(new Dimension(menuBar.getHeight(),(int)menuBar.getWidth()/6));
        menuBar.add(decoracion);
        JMenu accesorios = new JMenu("Accesorios");
        //accesorios.setPreferredSize(new Dimension(menuBar.getHeight(),(int)menuBar.getWidth()/6));
        menuBar.add(accesorios);
        JMenu cuenta = new JMenu("Cuenta");
        //cuenta.setPreferredSize(new Dimension(menuBar.getHeight(),(int)menuBar.getWidth()/6));
        menuBar.add(cuenta);

        //cuenta        
        if(autentification==false){
        	JMenuItem signIn = new JMenuItem("Iniciar sesión", KeyEvent.VK_S);
            cuenta.add(signIn);
            cuenta.setMnemonic(KeyEvent.VK_M);
            signIn.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					new WindowHome(true);					
				}
			});
        }else {
        	JMenuItem cuentaInfo = new JMenuItem("Ver perfil");
        	cuenta.add(cuentaInfo);
        	JMenuItem signOut = new JMenuItem("Cerrar sesión", KeyEvent.VK_S);
        	cuenta.add(signOut);
        	cuenta.setMnemonic(KeyEvent.VK_M);
        	signOut.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();					
					new WindowHome(false);
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
        JLabel banner = new JLabel(new ImageIcon("imagenes/logo.png"));
        add(banner, BorderLayout.NORTH);

        // Create and add a panel for the main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(4, 2));
        add(contentPanel, BorderLayout.CENTER);

        // Add some example components to the content panel
        contentPanel.add(new JLabel("Explore decoration that will blow your mind"));
        contentPanel.add(new JButton("Shop now"));
        contentPanel.add(new JLabel("Clothing"));
        contentPanel.add(new JButton("Shop now"));
        contentPanel.add(new JLabel("Stickers"));
        contentPanel.add(new JButton("Shop now"));
        contentPanel.add(new JLabel("Phone cases"));
        contentPanel.add(new JButton("Shop now"));

        // Create and add a panel for the footer
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        add(footerPanel, BorderLayout.SOUTH);

        // Add some example components to the footer panel
        footerPanel.add(new JLabel("Follow us on social media"), BorderLayout.WEST);
        JButton socialMedia= new JButton("Join now");
        socialMedia.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				String url="https://www.facebook.com/Redbubble";
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

    public static void main(String[] args) {
        WindowHome w = new WindowHome(false);
    }
}