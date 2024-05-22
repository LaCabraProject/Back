/**
 * @file HomeWindow.java
 * @brief Define la ventana principal de la aplicación.
 */

package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.graphical.component.ImageDisplayer;
import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.Signal;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.server.api.type.item.ItemFilters;
import org.lacabra.store.server.api.type.user.Authority;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serial;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @class HomeWindow
 * @brief Implementa la interfaz gráfica para la ventana principal de la aplicación.
 */

/**
 * @class HomeWindow
 * @brief Implementa la interfaz gráfica para la ventana principal de la aplicación.
 */
public final class HomeWindow extends DispatchedWindow {
    /**
     * @brief Título de la ventana principal.
     */
    public static final String TITLE = "GOAT";

    /**
     * @brief Tamaño de la ventana principal.
     */
    public static final Dimension SIZE = new Dimension(800, 600);

    /**
     * @brief Intervalo del carrusel de banners en milisegundos.
     */
    public static final int CAROUSEL_INTERVAL = 3000;

    /**
     * @brief Imágenes usadas en el carrusel.
     */
    public static final String[] CAROUSEL_IMGS = {"brushes.png", "sales.png", "office.png"};

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @brief Constructor de la ventana principal.
     * @param wd Dispatcher de ventanas.
     */
    /**
     * @param wd Dispatcher de ventanas.
     * @brief Constructor de la ventana principal.
     */
    public HomeWindow(final WindowDispatcher wd) {
        super(wd);
    }

    /**
     * @brief Inicializa la ventana.
     * @param wd Dispatcher de ventanas.
     */
    /**
     * @param wd Dispatcher de ventanas.
     * @brief Configura el dispatcher de la ventana.
     */

    @Override
    public void setDispatcher(final WindowDispatcher wd) {
        super.setDispatcher(wd);

        final var controller = this.controller();
        if (controller == null) return;

        this.auth(() -> {
            final var user = controller.getUser();
            assert (user != null);
            final var uid = user.id();
            assert (uid != null);

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setLocationRelativeTo(null);

            final var wsize = new Signal<>(SIZE);

            {
                final var bar = new JMenuBar();
                this.setJMenuBar(bar);

                {
                    final var search = new JMenu("Buscar");

                    {
                        final var item = new JMenuItem("Todo");
                        item.addActionListener(e -> this.replace(ShoppingWindow.class));
                        item.addActionListener(e -> this.load(controller.GET.Item.id(ObjectId.from((int) (Math.random() * 100) % 21)), (Consumer<ItemDTO>) ((dto) -> {
                                    if (dto == null) {
                                        this.message("No se pudo obtener un ítem :(");
                                    } else {
                                        this.message("Un ítem random de la API: " + dto);
                                    }
                                }), "Obteniendo un ítem...", 5000, null)

                        );

                        search.add(item);
                    }

                    {
                        final var clothing = new JMenuItem("Ropa");

                        search.add(clothing);
                    }

                    {
                        final var home = new JMenuItem("Home");

                        search.add(home);
                    }

                    {
                        final var office = new JMenuItem("Oficina");

                        search.add(office);
                    }

                    {
                        final var decoration = new JMenuItem("Decoración");

                        search.add(decoration);
                    }

                    bar.add(search);
                }

                if (user.authorities().contains(Authority.Artist)) {
                    bar.add(Box.createHorizontalGlue());

                    final var puesto = new JMenuItem("Mis productos");
                    puesto.addActionListener(e -> this.replace(WindowSalesStall.class));
                    puesto.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                    bar.add(puesto);
                }

                {
                    bar.add(Box.createHorizontalGlue());

                    final var cuenta = new JMenu("Hola, " + uid.get());
                    cuenta.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                    {
                        final var perfil = new JMenuItem("Perfil");
                        perfil.addActionListener(e -> this.dispatch(ProfileWindow.class));

                        cuenta.add(perfil);
                    }

                    {
                        final var logout = new JMenuItem("Cerrar sesión");
                        logout.addActionListener(e -> this.replace(LoginWindow.class));

                        cuenta.add(logout);
                    }

                    bar.add(cuenta);
                }

                if (user.authorities().contains(Authority.Client)) {
                    final var carrito = new JMenuItem("Mi carrito");
                    carrito.addActionListener(e -> this.replace(ShoppingCartWindow.class));
                    carrito.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                    bar.add(carrito);
                }
            }

            {
                final var banner = this.banner();

                if (banner != null) this.add(banner, BorderLayout.NORTH);
            }

            {
                final Integer[] length = {null};

                final var p = new JPanel(null);

                final var current = new Signal<>((Integer) null);

                final Consumer<Boolean> change = (forward) -> {
                    var l = length[0];
                    if (l == null || l == 0) return;

                    var idx = current.get();
                    if (idx == null) idx = 0;

                    if (forward) idx++;
                    else if (idx == 0) idx = l - 1;
                    else idx--;

                    current.set(idx % l);
                };

                {
                    final var carousel = new JPanel();
                    carousel.setLayout(new BorderLayout());

                    {
                        final BiConsumer<MouseEvent, ItemFilters> dispatch = (event, filters) -> {
                            final var signal = new Signal<>(filters);

                            if (event.getButton() == MouseEvent.BUTTON1) this.replace(ShoppingWindow.class, signal);
                            else this.dispatch(ShoppingWindow.class, signal);
                        };

                        final Consumer[] run = {
                                (Consumer<MouseEvent>) e -> dispatch.accept(e, null),
                                (Consumer<MouseEvent>) e -> dispatch.accept(e, null),
                                (Consumer<MouseEvent>) e -> dispatch.accept(e, null)
                        };

                        final var size = wsize.peek();

                        final var slides = IntStream.range(0, CAROUSEL_IMGS.length).mapToObj(i -> {
                            final var img = CAROUSEL_IMGS[i];
                            final var f = run[i];

                            try {
                                final var res = this.getClass().getClassLoader().getResource("img/slides/" + img);
                                if (res == null) return null;

                                final var c = new ImageDisplayer(ImageIO.read(res.toURI().toURL()), size.width,
                                        size.height, Image.SCALE_SMOOTH);

                                c.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        final var b = e.getButton();

                                        if (b == MouseEvent.BUTTON1 || b == MouseEvent.BUTTON3) f.accept(e);
                                    }

                                    @Override
                                    public void mouseEntered(MouseEvent e) {
                                        c.setCursor(new Cursor(Cursor.HAND_CURSOR));
                                    }

                                    @Override
                                    public void mouseExited(MouseEvent e) {
                                        c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                    }
                                });

                                return c;
                            } catch (URISyntaxException | NullPointerException | IOException e) {
                                Logger.getLogger().warning(e);
                                return null;
                            }
                        }).filter(Objects::nonNull).toArray(ImageDisplayer[]::new);

                        length[0] = slides.length;

                        final Consumer<Dimension> setSize = sz -> {
                            final var ww = sz.width;
                            final var wh = sz.height;

                            final ImageDisplayer c;
                            try {
                                c = (ImageDisplayer) carousel.getComponent(0);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                return;
                            }

                            c.setSize(sz);
                            carousel.setBounds(0, 0, ww, wh);
                            carousel.repaint();
                        };
                        setSize(size);

                        wsize.effect(setSize);

                        current.effect(idx -> {
                            if (idx == null) return;
                            if (idx < 0 || idx >= length[0]) return;

                            carousel.removeAll();
                            carousel.add(slides[idx]);
                            carousel.repaint();
                        });

                        final var timer = new Timer(CAROUSEL_INTERVAL, e -> change.accept(true));
                        timer.start();

                        carousel.addMouseWheelListener(new MouseAdapter() {
                            @Override
                            public void mouseWheelMoved(MouseWheelEvent e) {
                                if (MouseWheelEvent.WHEEL_BLOCK_SCROLL == e.getScrollAmount()) return;

                                timer.stop();

                                var forward = true;

                                var scroll = e.getWheelRotation();
                                if (scroll < 0) {
                                    scroll *= -1;
                                    forward = false;
                                }

                                for (int i = 0; i < scroll; i++)
                                    change.accept(forward);

                                timer.start();
                            }
                        });

                        p.add(carousel);
                    }

                    {
                        final var dotp = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        dotp.setBackground(new Color(0, 0, 0, 0));

                        {
                            final var dots = IntStream.range(0, length[0]).mapToObj(i -> {
                                final var c = new JLabel("⬤");
                                c.putClientProperty("JLabel.arc", 999);
                                c.setOpaque(true);
                                c.setForeground(Color.WHITE);

                                dotp.add(c);

                                if (i < length[0] - 1) dotp.add(Box.createHorizontalStrut(20));

                                return c;
                            }).toArray(JLabel[]::new);

                            current.effect(idx -> {
                                if (idx == null) return;
                                if (idx < 0 || idx >= length[0]) return;

                                for (int i = 0; i < dots.length; i++) {
                                    dots[i].setForeground(i == idx ? Color.BLACK : Color.WHITE);
                                }
                            });
                        }

                        p.add(dotp);
                    }

                    p.add(carousel);
                }

                this.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        wsize.set(getContentPane().getSize());
                    }
                });

                this.addWindowStateListener(e -> {
                    wsize.set(this.getContentPane().getSize());
                });

                this.connect(wsize);

                this.connect(current);
                current.set(0);

                this.add(p, BorderLayout.CENTER);
            }

            {
                final var footer = this.footer();
                if (footer != null) this.add(footer, BorderLayout.SOUTH);
            }

            this.setVisible(true);
        }, () -> this.replace(AuthWindow.class));
    }
}