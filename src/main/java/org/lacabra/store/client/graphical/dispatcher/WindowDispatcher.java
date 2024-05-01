package org.lacabra.store.client.graphical.dispatcher;

import org.lacabra.store.client.controller.MainController;
import org.lacabra.store.internals.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

public class WindowDispatcher implements IWindowDispatcher, Serializable {
    public static final Color BANNER_COLOR = new Color(254, 5, 20, 215);
    public static final Dimension MEDIA_BUTTON_SIZE = new Dimension(32, 32);
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Color FOOTER_BACKGROUND = Color.LIGHT_GRAY;
    private static final int FOOTER_BORDER = 5;
    private static final Color LINK_FOREGROUND = Color.BLUE;
    private static final URI PRIVACY_POLICY_URI = URI.create("https://www.lipsum.com/");
    private static final URI TERMS_OF_USE_URI = URI.create("https://www.lipsum.com/");
    private static final String FACEBOOK_IMG = "facebook.png";
    private static final String INSTAGRAM_IMG = "instagram.png";
    private static final String TWITTER_IMG = "twitter.png";
    private static final URI FACEBOOK_URI = URI.create("https://www.facebook.com/Redbubble");
    private static final URI INSTAGRAM_URI = URI.create("https://www.instagram.com/redbubble/");
    private static final URI TWITTER_URI = URI.create("https://twitter.com/redbubble");

    static {
        UIManager.put("ComboBox.selectionBackground", Color.LIGHT_GRAY);
        UIManager.put("ComboBox.selectionForeground", Color.BLACK);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
    }

    public final HashMap<Long, WindowState> state = new HashMap<>();
    private final HashMap<Long, DispatchedWindow> windows = new HashMap<>();
    private final MainController controller;
    private final JPanel[] footer = new JPanel[2];

    public WindowDispatcher() {
        this(null);
    }

    public WindowDispatcher(final MainController controller) {
        super();

        this.controller = new MainController(controller) {
            @Override
            public void setHostname(String hostname) throws MalformedURLException {
                if (this.getHostname() != null) super.setHostname(hostname);
            }

            @Override
            public void setPort(String port) {
                if (this.getPort() != null) super.setPort(port);
            }

            @Override
            public void setPort(Integer port) {
                if (this.getPort() != null) super.setPort(port);
            }

            @Override
            public void setEndpoint(String endpoint) {
                if (this.getEndpoint() != null) super.setEndpoint(endpoint);
            }
        };
    }

    public static WindowDispatcher fromArgs(final String[] args) throws MalformedURLException {
        return new WindowDispatcher(MainController.fromArgs(args));
    }

    public Map<Long, WindowState> state() {
        return new HashMap<>(this.state);
    }

    public WindowState stateOf(final DispatchedWindow w) {
        return this.stateOf(this.getId(w));
    }

    public WindowState stateOf(final Long id) {
        if (id == null) return null;

        return this.state.get(id);
    }

    public Long connect(final DispatchedWindow w, final Signal<?> signal) {
        return this.connect(this.stateOf(w), signal);
    }

    public Long connect(final Long w, final Signal<?> signal) {
        if (w == null) return null;

        final var state = this.state.get(w);

        if (state == null) return null;

        return state.connect(signal);
    }

    public Long connect(final WindowState state, final Signal<?> signal) {
        if (state == null) return null;

        return this.connect(this.state.entrySet().stream().filter(x -> x.getValue().equals(state)).map(Map.Entry::getKey).findFirst().orElse(null), signal);
    }

    public Long[] connect(final DispatchedWindow w, final Signal<?>... signals) {
        if (signals == null) return new Long[0];

        return Stream.of(signals).map(x -> this.connect(w, x)).toArray(Long[]::new);
    }

    public Long[] connect(final Long w, final Signal<?>... signals) {
        if (signals == null) return new Long[0];

        return Stream.of(signals).map(x -> this.connect(w, x)).toArray(Long[]::new);
    }

    public Long[] connect(final WindowState state, final Signal<?>... signals) {
        if (signals == null) return new Long[0];

        return Stream.of(signals).map(x -> this.connect(state, x)).toArray(Long[]::new);
    }

    public Signal<?> disconnect(final Long w, final Signal<?> signal) {
        if (w == null) return null;

        final var state = this.state.get(w);

        if (state == null) return null;

        return state.disconnect(signal);
    }

    public Signal<?> disconnect(final Long w, final Long signal) {
        if (w == null) return null;

        final var state = this.state.get(w);

        if (state == null) return null;

        return state.disconnect(signal);
    }

    public Signal<?> disconnect(final DispatchedWindow w, final Long signal) {
        return this.disconnect(this.stateOf(w), signal);
    }

    public Signal<?> disconnect(final DispatchedWindow w, final Signal<?> signal) {
        return this.disconnect(this.stateOf(w), signal);
    }

    public Signal<?> disconnect(final WindowState state, final Signal<?> signal) {
        if (state == null) return null;

        return this.disconnect(this.state.entrySet().stream().filter(x -> x.getValue().equals(state)).map(Map.Entry::getKey).findFirst().orElse(null), signal);
    }

    public Signal<?> disconnect(final WindowState state, final Long signal) {
        if (state == null) return null;

        return this.disconnect(this.state.entrySet().stream().filter(x -> x.getValue().equals(state)).map(Map.Entry::getKey).findFirst().orElse(null), signal);
    }

    public Map<Long, DispatchedWindow> windows() {
        return new HashMap<>(this.windows);
    }

    public Long getId(final DispatchedWindow w) {
        if (w == null) return null;

        for (Map.Entry<Long, DispatchedWindow> entry : this.windows.entrySet()) {
            if (entry.getValue().equals(w)) return entry.getKey();
        }

        return null;
    }

    public DispatchedWindow getWindow(final Long id) {
        if (id == null) return null;

        return this.windows.get(id);
    }

    public Long dispatch(final Class<? extends DispatchedWindow> cls) {
        if (cls == null) return null;

        try {
            return this.dispatch(cls.getDeclaredConstructor(WindowDispatcher.class).newInstance(this));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Long dispatch(final DispatchedWindow w) {
        if (w == null) return null;

        final Set<Long> keys = Set.of(this.windows.keySet().toArray(new Long[0]));
        final Random r = new Random();

        for (int i = 0; i < 1_000_000; i++) {
            final Long l = r.nextLong();
            if (keys.contains(l)) continue;

            this.windows.put(l, w);
            w.setDispatcher(this);

            return l;
        }

        return null;
    }

    public boolean close(final DispatchedWindow w) {
        return this.close(this.getId(w));
    }

    public boolean close(final Long id) {
        if (id == null) return false;

        final DispatchedWindow w = this.windows.remove(id);
        if (w == null) return true;

        final var state = this.stateOf(w);
        if (state != null) state.disconnectAll();

        w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));

        return true;
    }

    public MainController controller() {
        return this.controller;
    }

    private Component yieldComponent(Component c) {
        if (c == null) return null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(c);

            return (Component) new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger().warning(e);
            return null;
        }
    }

    public JPanel banner(JFrame frame) {
        try {
            final var p = new JPanel();
            p.setLayout(new BorderLayout());
            p.setBackground(BANNER_COLOR);

            final var res = this.getClass().getClassLoader().getResource("logo.png");

            if (res != null) {
                final var banner =
                        new JLabel(new ImageIcon(ImageIO.read(new File(res.getFile())).getScaledInstance(10, 10,
                                Image.SCALE_SMOOTH)));

                if (frame != null)
                    p.setPreferredSize(new Dimension(frame.getWidth(), banner.getPreferredSize().height));

                p.add(banner, BorderLayout.CENTER);
            }

            return p;
        } catch (IOException e) {
            Logger.getLogger().warning(e);

            return null;
        }
    }

    public JPanel footer() {
        return this.footer(false);
    }

    public JPanel footer(final boolean full) {
        final var idx = full ? 1 : 0;

        if (this.footer[idx] != null) return (JPanel) this.yieldComponent(this.footer[idx]);

        final var footer = new JPanel();

        if (full) {
            footer.setLayout(new GridLayout(1, 5));
            footer.setBackground(FOOTER_BACKGROUND);
            footer.setBorder(new EmptyBorder(FOOTER_BORDER, FOOTER_BORDER, FOOTER_BORDER, FOOTER_BORDER));

            for (Map.Entry<String, URI> details : Map.of("Política de privacidad", PRIVACY_POLICY_URI,
                    "Términos de " + "uso", TERMS_OF_USE_URI).entrySet()) {
                final var title = details.getKey();
                final var uri = details.getValue();

                final var l = new JLabel(title);
                l.setForeground(LINK_FOREGROUND);

                final var f = l.getFont();

                l.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            Desktop.getDesktop().browse(uri);
                        } catch (IOException ex) {
                            Logger.getLogger().warning(ex);
                        }
                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public void mouseEntered(MouseEvent e) {
                        final var attr = (Map<TextAttribute, Object>) f.getAttributes();

                        attr.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                        l.setFont(f.deriveFont(attr));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        l.setFont(f);
                    }
                });

                footer.add(l);
            }

            for (Map.Entry<String, URI> media : Map.of(FACEBOOK_IMG, FACEBOOK_URI, TWITTER_IMG, TWITTER_URI,
                    INSTAGRAM_IMG, INSTAGRAM_URI).entrySet()) {
                final var file = media.getKey();
                final var uri = media.getValue();

                final var b = new JButton(new ImageIcon(file));
                b.setPreferredSize(MEDIA_BUTTON_SIZE);
                b.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(uri);
                    } catch (IOException ex) {
                        Logger.getLogger().warning(ex);
                    }
                });

                footer.add(b);
            }
        } else {
            footer.setLayout(new BorderLayout());

            {
                final var label = new JLabel("Síguenos en Facebook");

                footer.add(label, BorderLayout.WEST);
            }

            try {
                final var media = new JButton();
                media.setPreferredSize(new Dimension(90, 30));
                media.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(FACEBOOK_URI);
                    } catch (IOException ex) {
                        Logger.getLogger().warning(ex);
                    }
                });

                final var res = this.getClass().getClassLoader().getResource("facebook.png");

                if (res != null) {
                    media.setIcon(new ImageIcon(ImageIO.read(new File(res.getFile())).getScaledInstance(10, 10,
                            Image.SCALE_SMOOTH)));

                    footer.add(media, BorderLayout.EAST);

                    this.footer[idx] = footer;
                }
            } catch (IOException e) {
                Logger.getLogger().warning(e);
            }
        }

        if (this.footer[idx] == null) return null;

        return this.footer(full);
    }

    public void message(final Long id, final String message) {
        final var w = this.getWindow(id);
        if (w == null) return;

        JOptionPane.showMessageDialog(w, message);
    }

    public void message(final DispatchedWindow w, final String message) {
        this.message(this.getId(w), message);
    }

    @Override
    public String input(final Long id, final String message) {
        final var w = this.getWindow(id);
        if (w == null) return null;

        return JOptionPane.showInputDialog(message);
    }

    @Override
    public String input(final DispatchedWindow w, final String message) {
        return this.input(this.getId(w), message);
    }
}
