package org.lacabra.store.client.graphical.dispatcher;

import org.lacabra.store.client.controller.MainController;

import javax.swing.*;
import java.util.Map;

public interface IWindowDispatcher {
    MainController controller();

    Map<Long, DispatchedWindow> windows();

    Long getId(DispatchedWindow w);

    DispatchedWindow getWindow(final Long id);

    Long dispatch(final Class<? extends DispatchedWindow> cls);
    Long dispatch(final DispatchedWindow w);

    boolean close(final DispatchedWindow w);
    boolean close(final Long id);

    JPanel banner(final JFrame frame);
    JPanel footer();
    JPanel footer(final boolean full);

    void message(final Long id, final String message);
    void message(final DispatchedWindow w, final String message);

    String input(final Long id, final String message);
    String input(final DispatchedWindow w, final String message);
}
