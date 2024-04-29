package org.lacabra.store.client.windows;

import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.item.ItemType;
import org.lacabra.store.server.api.type.user.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WindowItemDetail extends JFrame {
    private JPanel panelPrincipal = new JPanel();
    private JPanel panelProducto = new JPanel();
    private JPanel panelInfo = new JPanel();
    private JLabel labelId= new JLabel("Id: ");
    private JLabel labelNombre= new JLabel("Nombre: ");
    private JLabel labelTipo= new JLabel("Tipo: ");
    private JLabel labelDescripcion= new JLabel("Descripción: ");
    private JLabel labelPrecio= new JLabel("Precio: ");
    private JLabel labelStock= new JLabel("Stock: ");
    private JLabel labelVendedor= new JLabel("Vendedor: ");
    private JTextField textId;
    private JTextField textNombre;
    private JTextField textTipo;
    private JTextField textDescripcion;
    private JTextField textPrecio;
    private JTextField textStock;
    private JTextField textVendedor;
    private List<String> reseñas;

    public WindowItemDetail(ItemDTO itemDTO) {
        super("Detalle del Artículo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelProducto.setLayout(new BorderLayout());
        panelInfo.setLayout(new GridLayout(0, 2));
        ImageIcon iconoImagen=null;
        if(itemDTO.type().equals(ItemType.Clothing)) {
            iconoImagen = new ImageIcon("src/main/java/org/lacabra/store/client/img/ropa.jpg");
        }else if(itemDTO.type().equals(ItemType.Decoration)) {
            iconoImagen = new ImageIcon("src/main/java/org/lacabra/store/client/img/decoracion.jpg");
        }else if(itemDTO.type().equals(ItemType.Accessories)) {
            iconoImagen = new ImageIcon("src/main/java/org/lacabra/store/client/img/accesorio.jpg");
        }else if(itemDTO.type().equals(ItemType.Utilities)) {
            iconoImagen = new ImageIcon("src/main/java/org/lacabra/store/client/img/utilidad.jpg");
        }
        JLabel etiquetaImagen = new JLabel(iconoImagen);
        etiquetaImagen.setPreferredSize(new Dimension(300, 300));
        panelProducto.add(etiquetaImagen, BorderLayout.NORTH);
        panelProducto.add(panelInfo, BorderLayout.CENTER);
        panelPrincipal.add(panelProducto, BorderLayout.CENTER);

        {
            textId = new JTextField(""+itemDTO.id().toString());
            textId.setEditable(false);
            textTipo = new JTextField(itemDTO.type().toString());
            textTipo.setEditable(false);
            textNombre = new JTextField(itemDTO.name());
            textNombre.setEditable(false);
            textDescripcion = new JTextField(itemDTO.description());
            textDescripcion.setEditable(false);
            textPrecio = new JTextField(itemDTO.price().toString());
            textPrecio.setEditable(false);
            textStock = new JTextField(itemDTO.stock().toString());
            textStock.setEditable(false);
            textVendedor = new JTextField("mikel.mason");
            textVendedor.setEditable(false);
        }

        {
            panelInfo.add(labelId);
            panelInfo.add(textId);
            panelInfo.add(labelNombre);
            panelInfo.add(textNombre);
            panelInfo.add(labelTipo);
            panelInfo.add(textTipo);
            panelInfo.add(labelDescripcion);
            panelInfo.add(textDescripcion);
            panelInfo.add(labelPrecio);
            panelInfo.add(textPrecio);
            panelInfo.add(labelStock);
            panelInfo.add(textStock);
            panelInfo.add(labelVendedor);
            panelInfo.add(textVendedor);
        }

        JTabbedPane panelPestanas = crearPanelReseñas();
        panelPrincipal.add(panelPestanas, BorderLayout.EAST);

        JPanel piePagina = crearPiePagina();
        panelPrincipal.add(piePagina, BorderLayout.SOUTH);

        add(panelPrincipal);

        setVisible(true);
    }

    private JTabbedPane crearPanelReseñas() {
        JTabbedPane panelPestanas = new JTabbedPane();

        JPanel panelResenas = new JPanel();
        panelResenas.setLayout(new BorderLayout());
        panelResenas.setBorder(new CompoundBorder(new TitledBorder("Reseñas del Producto"), new EmptyBorder(10, 7, 10, 10)));

        reseñas = new ArrayList<>();
        reseñas.add("¡Excelente producto!");
        reseñas.add("Me encantó, muy recomendado.");
        reseñas.add("Buena calidad y buen precio.");

        JTextArea areaResenas = new JTextArea();
        areaResenas.setEditable(false);
        actualizarResenas(areaResenas);
        JScrollPane scrollPane = new JScrollPane(areaResenas);
        panelResenas.add(scrollPane, BorderLayout.CENTER);

        JButton botonAgregarResena = new JButton("Agregar Reseña");
        botonAgregarResena.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevaResena = JOptionPane.showInputDialog("Escribe tu reseña:");
                if (nuevaResena != null && !nuevaResena.isEmpty()) {
                    reseñas.add(nuevaResena);
                    actualizarResenas(areaResenas);
                }
            }
        });
        panelResenas.add(botonAgregarResena, BorderLayout.SOUTH);

        panelPestanas.addTab("Reseñas", panelResenas);

        return panelPestanas;
    }

    private void actualizarResenas(JTextArea areaResenas) {
        areaResenas.setText("");
        for (String resena : reseñas) {
            areaResenas.append("- " + resena + "\n");
        }
    }

    private JPanel crearPiePagina() {
        JPanel piePagina = new JPanel();
        piePagina.setLayout(new GridLayout(1, 5));
        piePagina.setBackground(Color.LIGHT_GRAY);
        piePagina.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton botonVolver = new JButton();
        botonVolver.setPreferredSize(new Dimension(32, 32));
        BufferedImage image0 = null;
        try {
            image0 = ImageIO.read(new File("src/main/java/org/lacabra/store/client/img/back.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Image SI = image0.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        botonVolver.setIcon(new ImageIcon(SI));
        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        piePagina.add(botonVolver);

        JButton botonVendedor = new JButton("Otros productos del vendedor");
        botonVendedor.setPreferredSize(new Dimension(32, 32));
        botonVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        piePagina.add(botonVendedor);

        JButton botonValorar = new JButton("Pulsa para valorar el producto");
        botonValorar.setPreferredSize(new Dimension(32, 32));
        botonValorar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Valora el producto");
                dialog.setModal(true);
                dialog.setLayout(new GridBagLayout());

                JLabel instructionsLabel = new JLabel("Por favor, valora el producto del 1 al 5:");
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.insets = new Insets(5, 10, 5, 10);
                constraints.anchor = GridBagConstraints.WEST;
                dialog.add(instructionsLabel, constraints);

                JPanel ratingButtonsPanel = new JPanel();
                ratingButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                for (int i = 1; i <= 5; i++) {
                    JButton ratingButton = new JButton("" + i);
                    ratingButton.setPreferredSize(new Dimension(40, 32));
                    ratingButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int rating = Integer.parseInt(ratingButton.getText());
                            JOptionPane.showMessageDialog(dialog, "Gracias por tu calificación de " + rating + " estrellas!", "Calificación recibida", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                        }
                    });
                    ratingButtonsPanel.add(ratingButton);
                }
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.gridwidth = 2;
                constraints.fill = GridBagConstraints.HORIZONTAL;
                dialog.add(ratingButtonsPanel, constraints);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });
        piePagina.add(botonValorar);
        return piePagina;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String[] words = {"cabra", "goat", "beast"};
                Collection<String> keywords = new ArrayList<>(Arrays.asList(words));
                new WindowItemDetail(new ItemDTO(ObjectId.from(220), ItemType.Decoration, "chair", "a goated chair", keywords
                        , 20, 0, new BigInteger("2"), new User("mikel").id()));
            }
        });
    }
}

