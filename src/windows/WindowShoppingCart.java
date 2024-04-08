package windows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class WindowShoppingCart extends JFrame {
    private JComboBox<String> shippingMethodComboBox;
    private JList<String> productList;
    private DefaultListModel<String> productListModel;
    private JTextField totalCostField;
    private JButton addProductButton;
    private JButton removeProductButton;
    private JButton applyCouponButton;
    private JButton checkoutButton;
    private ShoppingCart cart;

    public WindowShoppingCart() {
        
        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        add(topPanel, BorderLayout.NORTH);

        
        JLabel shippingMethodLabel = new JLabel("Shipping Method:");
        shippingMethodComboBox = new JComboBox<>(new String[]{"Standard", "Express", "Next Day"});
        topPanel.add(shippingMethodLabel);
        topPanel.add(shippingMethodComboBox);

        JPanel rightTopPanel = new JPanel(new GridLayout(1, 2));
        JLabel totalCostLabel = new JLabel("Total Cost:");
        totalCostField = new JTextField();
        totalCostField.setEditable(false);
        totalCostField.setColumns(10);
        rightTopPanel.add(totalCostLabel);
        rightTopPanel.add(totalCostField);
        topPanel.add(rightTopPanel);

        add(topPanel, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftCenterPanel = new JPanel(new BorderLayout());
        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productScrollPane = new JScrollPane(productList);
        leftCenterPanel.add(productScrollPane, BorderLayout.CENTER);

        JPanel rightCenterPanel = new JPanel(new GridLayout(2, 1));
        JPanel centerBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addProductButton = new JButton("Add Product");
        removeProductButton = new JButton("Remove Product");
        centerBottomPanel.add(addProductButton);
        centerBottomPanel.add(removeProductButton);
        rightCenterPanel.add(centerBottomPanel);

        JPanel centerRightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        applyCouponButton = new JButton("Apply Coupon");
        checkoutButton = new JButton("Checkout");
        centerRightPanel.add(applyCouponButton);
        centerRightPanel.add(checkoutButton);
        rightCenterPanel.add(centerRightPanel);

        leftCenterPanel.add(rightCenterPanel, BorderLayout.SOUTH);
        centerPanel.add(leftCenterPanel);
        centerPanel.add(rightCenterPanel);

        add(centerPanel, BorderLayout.CENTER);

        
        List<String> products = new ArrayList<>();
        products.add("Product 1");
        products.add("Product 2");
        products.add("Product 3");
        for (String product : products) {
            productListModel.addElement(product);
        }

        
        cart = new ShoppingCart();

        
        totalCostField.setText("$" + calculateTotalCost());

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
                
                String selectedProduct = productList.getSelectedValue();
                if (selectedProduct != null) {
                    cart.addProduct(selectedProduct);
                    totalCostField.setText("$" + calculateTotalCost());
                }
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
                
                CheckoutDialog dialog = new CheckoutDialog(WindowShoppingCart.this, cart);
                dialog.setVisible(true);
            }
        });
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

class ShoppingCart {
    private List<String> products;

    public ShoppingCart() {
        products = new ArrayList<>();
    }

    public void addProduct(String product) {
        products.add(product);
    }

    public void removeProduct(String product) {
        products.remove(product);
    }

    public int getProductCount() {
        return products.size();
    }
    
    public List<String> getProducts() {
        return products;
    }

    public double getTotalCost() {
        double totalCost = 0.0;
    
        return totalCost;
    }
}

class CheckoutDialog extends JDialog {
    private ShoppingCart cart;

    public CheckoutDialog(Frame owner, ShoppingCart cart) {
        super(owner);
        this.cart = cart;

        
        setTitle("Checkout");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

       
        JPanel leftPanel = new JPanel(new BorderLayout());
        JLabel shippingLabel = new JLabel("Shipping Address:");
        JTextField shippingField = new JTextField();
        leftPanel.add(shippingLabel, BorderLayout.NORTH);
        leftPanel.add(shippingField, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

       
        JPanel rightPanel = new JPanel(new BorderLayout());
        JLabel summaryLabel = new JLabel("Order Summary:");
        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightPanel.add(summaryLabel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);

        
        updateSummaryArea(summaryArea);
    }

    private void updateSummaryArea(JTextArea area) {
        StringBuilder sb = new StringBuilder();
        sb.append("Products (").append(cart.getProductCount()).append("):\n");
        for (String product : cart.getProducts()) {
            sb.append(product).append("\n");
        }
        sb.append("\nTotal Cost: $").append(cart.getTotalCost());
        area.setText(sb.toString());
    }

}