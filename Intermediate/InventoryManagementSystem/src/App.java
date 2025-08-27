import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {


    private JTextField nameField, priceField, quantityField, barcodeField;

    
    private JTextField searchNameField;   
    private JTextField minQtyField;       
    private JTextField maxPriceField;     

    
    private JButton addButton, editButton, saveButton, deleteButton, applyFiltersButton, clearFiltersButton;

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    private JLabel totalValueLabel;

    private int editingRowModelIndex = -1; 

    public App() {
        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        model = new DefaultTableModel(new String[]{"Name", "Price", "Quantity", "Barcode"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 1 -> Double.class; 
                    case 2 -> Integer.class;
                    default -> String.class;
                };
            }
        };
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        add(new JScrollPane(table), BorderLayout.CENTER);

       
        sorter.addRowSorterListener(new RowSorterListener() {
            @Override public void sorterChanged(RowSorterEvent e) { updateVisibleTotal(); }
        });

        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        barcodeField = new JTextField();

        addLabeled(formPanel, gc, 0, "Name:", nameField);
        addLabeled(formPanel, gc, 1, "Price:", priceField);
        addLabeled(formPanel, gc, 2, "Quantity:", quantityField);
        addLabeled(formPanel, gc, 3, "Barcode:", barcodeField);

        JPanel formButtons = new JPanel(new GridLayout(1, 4, 8, 8));
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        saveButton = new JButton("Save");
        deleteButton = new JButton("Delete");
        formButtons.add(addButton);
        formButtons.add(editButton);
        formButtons.add(saveButton);
        formButtons.add(deleteButton);

        gc.gridy = 4; gc.gridx = 0; gc.gridwidth = 2;
        formPanel.add(formButtons, gc);

        add(formPanel, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search & Filters"));
        GridBagConstraints fg = new GridBagConstraints();
        fg.insets = new Insets(6,6,6,6);
        fg.anchor = GridBagConstraints.WEST;
        fg.fill = GridBagConstraints.HORIZONTAL;
        fg.weightx = 1;

        searchNameField = new JTextField();
        minQtyField = new JTextField();
        maxPriceField = new JTextField();

        addLabeled(filterPanel, fg, 0, "Search name:", searchNameField);
        addLabeled(filterPanel, fg, 1, "Min quantity (≥):", minQtyField);
        addLabeled(filterPanel, fg, 2, "Max price (≤):", maxPriceField);

        JPanel filterButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        applyFiltersButton = new JButton("Apply Filters");
        clearFiltersButton = new JButton("Clear Filters");
        filterButtons.add(applyFiltersButton);
        filterButtons.add(clearFiltersButton);

        fg.gridy = 3; fg.gridx = 0; fg.gridwidth = 2;
        filterPanel.add(filterButtons, fg);

        add(filterPanel, BorderLayout.NORTH);

        JPanel bottom = new JPanel(new BorderLayout());
        totalValueLabel = new JLabel("Total (visible): 0.00");
        totalValueLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        bottom.add(totalValueLabel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> beginEdit());
        saveButton.addActionListener(e -> saveEdit());
        deleteButton.addActionListener(e -> deleteSelected());

        searchNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });

        applyFiltersButton.addActionListener(e -> applyFilters());
        clearFiltersButton.addActionListener(e -> clearFilters());

        setEditMode(false);

        setVisible(true);
    }

    private void addLabeled(JPanel panel, GridBagConstraints gc, int row, String label, JComponent comp) {
        gc.gridy = row; gc.gridx = 0; gc.gridwidth = 1; gc.weightx = 0;
        panel.add(new JLabel(label), gc);
        gc.gridx = 1; gc.gridwidth = 1; gc.weightx = 1;
        panel.add(comp, gc);
    }

    private void setEditMode(boolean editing) {
        
        saveButton.setEnabled(editing);
        editButton.setEnabled(!editing);
        addButton.setEnabled(!editing);
        deleteButton.setEnabled(!editing);
    }


    private void addProduct() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String qtyText = quantityField.getText().trim();
        String barcode = barcodeField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || qtyText.isEmpty() || barcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        int qty;
        try {
            price = Double.parseDouble(priceText);
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a number and Quantity must be an integer.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (price < 0 || qty < 0) {
            JOptionPane.showMessageDialog(this, "Price and Quantity cannot be negative.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.addRow(new Object[]{name, price, qty, barcode});
        clearForm();
        applyFilters();       
        updateVisibleTotal();
    }

    private void beginEdit() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        editingRowModelIndex = table.convertRowIndexToModel(viewRow);

        nameField.setText(String.valueOf(model.getValueAt(editingRowModelIndex, 0)));
        priceField.setText(String.valueOf(model.getValueAt(editingRowModelIndex, 1)));
        quantityField.setText(String.valueOf(model.getValueAt(editingRowModelIndex, 2)));
        barcodeField.setText(String.valueOf(model.getValueAt(editingRowModelIndex, 3)));

        setEditMode(true);
    }

    private void saveEdit() {
        if (editingRowModelIndex < 0) {
            JOptionPane.showMessageDialog(this, "No product is being edited.", "Save", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String qtyText = quantityField.getText().trim();
        String barcode = barcodeField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || qtyText.isEmpty() || barcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        int qty;
        try {
            price = Double.parseDouble(priceText);
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a number and Quantity must be an integer.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (price < 0 || qty < 0) {
            JOptionPane.showMessageDialog(this, "Price and Quantity cannot be negative.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.setValueAt(name, editingRowModelIndex, 0);
        model.setValueAt(price, editingRowModelIndex, 1);
        model.setValueAt(qty, editingRowModelIndex, 2);
        model.setValueAt(barcode, editingRowModelIndex, 3);

       
        editingRowModelIndex = -1;
        setEditMode(false);
        clearForm();
        applyFilters();      
        updateVisibleTotal();
        JOptionPane.showMessageDialog(this, "Product updated successfully.");
    }

    private void deleteSelected() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to delete.", "Delete", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (editingRowModelIndex >= 0) {
            JOptionPane.showMessageDialog(this, "Finish or cancel the current edit before deleting.", "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        model.removeRow(modelRow);
        applyFilters();
        updateVisibleTotal();
    }

    private void applyFilters() {
        String nameQuery = searchNameField.getText().trim().toLowerCase();
        String minQtyText = minQtyField.getText().trim();
        String maxPriceText = maxPriceField.getText().trim();

        List<RowFilter<Object,Object>> filters = new ArrayList<>();

        
        if (!nameQuery.isEmpty()) {
            filters.add(new RowFilter<>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    String name = String.valueOf(entry.getValue(0)).toLowerCase();
                    return name.contains(nameQuery);
                }
            });
        }

        if (!minQtyText.isEmpty()) {
            try {
                int minQ = Integer.parseInt(minQtyText);
                filters.add(new RowFilter<>() {
                    @Override
                    public boolean include(Entry<?, ?> entry) {
                        int qty = ((Number) entry.getValue(2)).intValue();
                        return qty >= minQ;
                    }
                });
            } catch (NumberFormatException ignored) {
                JOptionPane.showMessageDialog(this, "Min quantity must be an integer.", "Filter", JOptionPane.WARNING_MESSAGE);
            }
        }

        if (!maxPriceText.isEmpty()) {
            try {
                double maxP = Double.parseDouble(maxPriceText);
                filters.add(new RowFilter<>() {
                    @Override
                    public boolean include(Entry<?, ?> entry) {
                        double price = ((Number) entry.getValue(1)).doubleValue();
                        return price <= maxP;
                    }
                });
            } catch (NumberFormatException ignored) {
                JOptionPane.showMessageDialog(this, "Max price must be a number.", "Filter", JOptionPane.WARNING_MESSAGE);
            }
        }

        if (filters.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
        updateVisibleTotal();
    }

    private void clearFilters() {
        searchNameField.setText("");
        minQtyField.setText("");
        maxPriceField.setText("");
        sorter.setRowFilter(null);
        updateVisibleTotal();
    }

   
    private void updateVisibleTotal() {
        double total = 0.0;
        for (int viewRow = 0; viewRow < table.getRowCount(); viewRow++) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            double price = ((Number) model.getValueAt(modelRow, 1)).doubleValue();
            int qty = ((Number) model.getValueAt(modelRow, 2)).intValue();
            total += price * qty;
        }
        totalValueLabel.setText(String.format("Total (visible): %.2f", total));
    }

    private void clearForm() {
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        barcodeField.setText("");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}
