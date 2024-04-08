package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowShoppingCart extends JFrame {
    private JComboBox<String> shippingMethodComboBox;
    private JList<String> productList;
    private DefaultListModel<String> productListModel;
    private JTextField totalCostField;
    private JButton addProductButton;
    private JButton removeProductButton;
    private JButton applyCouponButton;
    private JButton checkoutButton;

    public WindowShoppingCart() {
        
        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        
        JPanel topPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftTopPanel = new JPanel(new GridLayout(2, 1));
        JLabel shippingMethodLabel = new JLabel("Shipping Method:");
        shippingMethodComboBox = new JComboBox<>(new String[]{"Standard", "Express", "Next Day"});
        leftTopPanel.add(shippingMethodLabel);
        leftTopPanel.add(shippingMethodComboBox);
        topPanel.add(leftTopPanel);

        JPanel rightTopPanel = new JPanel(new GridLayout(1, 2));
        JLabel totalCostLabel = new JLabel("Total Cost:");
        totalCostField = new JTextField();
        totalCostField.setEditable(false);
        totalCostField.setColumns(10);
        rightTopPanel.add(totalCostLabel);
        rightTopPanel.add(totalCostField);
        topPanel.add(rightTopPanel);

        add(topPanel, BorderLayout.NORTH);

       
        JPanel centerPanel = new JPanel(new BorderLayout());
        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productScrollPane = new JScrollPane(productList);
        centerPanel.add(productScrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        
        JPanel bottomPanel = new JPanel(new GridLayout(1, 4));
        addProductButton = new JButton("Add Product");
        removeProductButton = new JButton("Remove Product");
        applyCouponButton = new JButton("Apply Coupon");
        checkoutButton = new JButton("Checkout");

        bottomPanel.add(addProductButton);
        bottomPanel.add(removeProductButton);
        bottomPanel.add(applyCouponButton);
        bottomPanel.add(checkoutButton);

        add(bottomPanel, BorderLayout.SOUTH);

        
        shippingMethodComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
            }
        });

        productList.addListSelectionListener(e -> {
            
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        applyCouponButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
            }
        });

      
        productListModel.addElement("Product 1");
        productListModel.addElement("Product 2");
        productListModel.addElement("Product 3");
        
        totalCostField.setText("$100.00"); 
    }

    public void run() {
        setVisible(true);
    }

    private double calculateTotalCost() {
        double totalCost = 0.0;
        
        return totalCost;
    }
    
    
    public static void main(String[] args) {
        WindowShoppingCart app = new WindowShoppingCart();
        app.run();
    }
}