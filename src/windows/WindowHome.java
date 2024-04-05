package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class WindowHome extends JFrame {
    public WindowHome() {
        initUI();
    }

    private void initUI() {
        setTitle("RedBubble");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu ropa = new JMenu("Ropa");
        menuBar.add(ropa);
        JMenu hogar = new JMenu("Hogar");
        menuBar.add(hogar);
        JMenu oficina = new JMenu("Oficina");
        menuBar.add(oficina);
        JMenu decoracion = new JMenu("Decoración");
        menuBar.add(decoracion);
        JMenu accesorios = new JMenu("Accesorios");
        menuBar.add(accesorios);
        JMenu cuenta = new JMenu("Cuenta");
        menuBar.add(cuenta);

        // Set the mnemonic for the "Sign In" option to Alt + S
        JMenuItem menuItem = new JMenuItem("Sign In", KeyEvent.VK_S);
        cuenta.add(menuItem);
        cuenta.setMnemonic(KeyEvent.VK_M);

        // Create and add a banner
        JLabel banner = new JLabel(new ImageIcon("imagenes/logo.jpg"));
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
        footerPanel.add(new JButton("Join now"), BorderLayout.EAST);

        setVisible(true);
    }

    public static void main(String[] args) {
        WindowHome w = new WindowHome();
    }
}