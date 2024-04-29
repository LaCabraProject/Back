package org.lacabra.store.client.graphical;

import org.lacabra.store.client.controller.MainController;
import org.lacabra.store.internals.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class WindowDispatcher {
    private final Map<Long, DispatchedWindow> windows = new HashMap<>();
    private final MainController controller;

    public final Color BANNER_COLOR = new Color(254, 5, 20, 215);

    public final URI FACEBOOK_URI = URI.create("https://www.facebook.com/Redbubble");
    private JPanel footer;

    public WindowDispatcher(MainController controller) {
        super();

        this.controller = new MainController(controller) {
            @Override
            public void setHostname(String hostname) throws MalformedURLException {
                if (this.getHostname() != null)
                    super.setHostname(hostname);
            }

            @Override
            public void setPort(String port) {
                if (this.getPort() != null)
                    super.setPort(port);
            }

            @Override
            public void setPort(Integer port) {
                if (this.getPort() != null)
                    super.setPort(port);
            }

            @Override
            public void setEndpoint(String endpoint) {
                if (this.getEndpoint() != null)
                    super.setEndpoint(endpoint);
            }
        };
    }

    public Map<Long, DispatchedWindow> windows() {
        return new HashMap<>(this.windows);
    }

    public Long getId(final DispatchedWindow w) {
        if (w == null)
            return null;

        for (Map.Entry<Long, DispatchedWindow> entry : this.windows.entrySet()) {
            if (entry.getValue().equals(w))
                return entry.getKey();
        }

        return null;
    }

    public DispatchedWindow getWindow (final Long id) {
        if (id == null)
            return null;

        return this.windows.get(id);
    }

    public Long dispatch(Class<? extends DispatchedWindow> w) {
        try {
            return this.dispatch(w.getDeclaredConstructor(WindowDispatcher.class).newInstance(this));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Long dispatch(DispatchedWindow w) {
        if (w == null) return null;

        final Set<Long> keys = Set.of(this.windows.keySet().toArray(new Long[0]));
        final Random r = new Random();

        for (int i = 0; i < 1_000_000; i++) {
            final Long l = r.nextLong();
            if (keys.contains(l)) continue;

            this.windows.put(l, w);

            return l;
        }

        return null;
    }

    public boolean close(DispatchedWindow w) {
        return this.close(this.getId(w));
    }

    public boolean close(Long id) {
        if (id == null) return false;

        final DispatchedWindow w = this.windows.remove(id);
        if (w == null) return false;

        w.dispose();
        return true;
    }

    public MainController controller() {
        return this.controller;
    }

    private Component yieldComponent(Component c) {
        if (c == null)
            return null;

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
                        new JLabel(new ImageIcon(ImageIO.read(new File(res.getFile())).getScaledInstance(10,
                                10, Image.SCALE_SMOOTH)));

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
        if (this.footer != null)
            return (JPanel) this.yieldComponent(this.footer);

        final var footer = new JPanel();
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
                media.setIcon(new ImageIcon(ImageIO.read(new File(res.getFile())).getScaledInstance(10,
                        10, Image.SCALE_SMOOTH)));

                footer.add(media, BorderLayout.EAST);

                this.footer = footer;
            }
        } catch (IOException e) {
            Logger.getLogger().warning(e);
        }

        if (this.footer == null)
            return null;

        return this.footer();
    }
}
