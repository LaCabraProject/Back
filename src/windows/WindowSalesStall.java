package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class WindowSalesStall extends JFrame {
    public WindowSalesStall() {
        initUI();
    }

    private void initUI() {
        setTitle("Vender Artículos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Lista de Artículos para Vender:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(4, 1));
        panel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField addItemField = new JTextField(20);
        JButton btnAddItem = new JButton("Agregar Artículo");
        addPanel.add(new JLabel("Artículo:"));
        addPanel.add(addItemField);
        addPanel.add(btnAddItem);
        bottomPanel.add(addPanel);

        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField descriptionField = new JTextField(20);
        JButton btnAddDescription = new JButton("Agregar Descripción");
        descriptionPanel.add(new JLabel("Descripción:"));
        descriptionPanel.add(descriptionField);
        descriptionPanel.add(btnAddDescription);
        bottomPanel.add(descriptionPanel);

        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField photoField = new JTextField(20);
        JButton btnAddPhoto = new JButton("Agregar Foto");
        photoPanel.add(new JLabel("Ruta de la Foto:"));
        photoPanel.add(photoField);
        photoPanel.add(btnAddPhoto);
        bottomPanel.add(photoPanel);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRemoveItem = new JButton("Eliminar Artículo Seleccionado");
        JButton btnClearList = new JButton("Limpiar Lista");
        controlPanel.add(btnRemoveItem);
        controlPanel.add(btnClearList);
        bottomPanel.add(controlPanel);

        btnAddItem.addActionListener(e -> {
            String newItem = addItemField.getText();
            if (!newItem.isEmpty()) {
                textArea.append(newItem);
                String description = descriptionField.getText();
                if (!description.isEmpty()) {
                    textArea.append(" - Descripción: " + description);
                }
                textArea.append("\n");
                addItemField.setText("");
                descriptionField.setText("");
            }
        });

        btnAddDescription.addActionListener(e -> {
            String description = descriptionField.getText();
            if (!description.isEmpty()) {
                textArea.append("Descripción: " + description + "\n");
                descriptionField.setText("");
            }
        });

        btnAddPhoto.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                photoField.setText(filePath);
            }
        });

        btnRemoveItem.addActionListener(e -> {
            int selectedIndex = textArea.getSelectionStart();
            if (selectedIndex != -1) {
                int endIndex = textArea.getSelectionEnd();
                textArea.replaceRange("", selectedIndex, endIndex);
            }
        });

        btnClearList.addActionListener(e -> textArea.setText(""));
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WindowSalesStall());
    }
}
