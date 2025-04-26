import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

class Transaction {
    private double amount;
    private String category;
    private String description;
    private LocalDate date;
    private boolean isExpense;

    public Transaction(double amount, String category, String description, LocalDate date, boolean isExpense) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.isExpense = isExpense;
    }

    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public boolean isExpense() { return isExpense; }

    public Object[] toTableRow() {
        return new Object[]{
            date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
            isExpense ? -amount : amount,
            category,
            description,
            isExpense ? "Wydatek" : "Wpływ"
        };
    }
}

class TransactionManager {
    private List<Transaction> transactions;
    private Set<String> expenseCategories;
    private Set<String> incomeCategories;

    public TransactionManager() {
        transactions = new ArrayList<>();
        expenseCategories = new HashSet<>(Arrays.asList(
            "Jedzenie", "Transport", "Rozrywka", "Rachunki", "Inne"
        ));
        incomeCategories = new HashSet<>(Arrays.asList(
            "Wypłata", "Premia", "Prezent", "Inne"
        ));
    }

    public void addTransaction(double amount, String category, String description, boolean isExpense) {
        if (isExpense) {
            if (!expenseCategories.contains(category)) {
                expenseCategories.add(category);
            }
        } else {
            if (!incomeCategories.contains(category)) {
                incomeCategories.add(category);
            }
        }
        transactions.add(new Transaction(amount, category, description, LocalDate.now(), isExpense));
    }

    public void removeTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
        }
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public Set<String> getExpenseCategories() {
        return new HashSet<>(expenseCategories);
    }

    public Set<String> getIncomeCategories() {
        return new HashSet<>(incomeCategories);
    }

    public double getBalance() {
        return transactions.stream()
            .mapToDouble(t -> t.isExpense() ? -t.getAmount() : t.getAmount())
            .sum();
    }
}

class TransactionManagerGUI extends JFrame {
    private TransactionManager manager;
    private DefaultTableModel tableModel;
    private JTable transactionTable;
    private JComboBox<String> categoryCombo;
    private JTextField amountField;
    private JTextField descriptionField;
    private JLabel balanceLabel;
    private JRadioButton expenseRadio;
    private JRadioButton incomeRadio;

    public TransactionManagerGUI() {
        manager = new TransactionManager();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Personalny Menadżer Finansów");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel górny z balansem
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        balanceLabel = new JLabel("Saldo: 0.00 zł");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(balanceLabel);

        // Panel formularza
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Radio buttons dla typu transakcji
        expenseRadio = new JRadioButton("Wydatek", true);
        incomeRadio = new JRadioButton("Wpływ", false);
        ButtonGroup group = new ButtonGroup();
        group.add(expenseRadio);
        group.add(incomeRadio);

        // Komponenty formularza
        amountField = new JTextField(10);
        amountField.setToolTipText("Wpisz kwotę (np. 99.99)");
        
        categoryCombo = new JComboBox<>(manager.getExpenseCategories().toArray(new String[0]));
        categoryCombo.setEditable(true);
        categoryCombo.setToolTipText("Wybierz istniejącą lub wpisz nową kategorię");
        
        descriptionField = new JTextField(20);
        descriptionField.setToolTipText("Dodaj opis transakcji (opcjonalnie)");
        
        JButton addButton = new JButton("Dodaj");
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.WHITE);
        
        JButton deleteButton = new JButton("Usuń zaznaczone");
        deleteButton.setBackground(new Color(178, 34, 34));
        deleteButton.setForeground(Color.WHITE);

        // Dodawanie komponentów do formularza
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(expenseRadio, gbc);
        gbc.gridx = 1;
        inputPanel.add(incomeRadio, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Kwota:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(amountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Kategoria:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(categoryCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Opis:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(descriptionField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        inputPanel.add(addButton, gbc);

        // Tabela transakcji
        String[] columnNames = {"Data", "Kwota", "Kategoria", "Opis", "Typ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(tableModel);
        
        // Wyrównanie kwoty do prawej
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        transactionTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);

        // Panel przycisków
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(deleteButton);

        // Dodawanie paneli do okna
        add(topPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Obsługa zdarzeń
        addButton.addActionListener(e -> addTransaction());
        deleteButton.addActionListener(e -> deleteSelectedTransactions());
        expenseRadio.addActionListener(e -> updateCategoryCombo());
        incomeRadio.addActionListener(e -> updateCategoryCombo());

        // Ustawienia okna
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void updateCategoryCombo() {
        categoryCombo.removeAllItems();
        Set<String> categories = expenseRadio.isSelected() ? 
            manager.getExpenseCategories() : 
            manager.getIncomeCategories();
        for (String category : categories) {
            categoryCombo.addItem(category);
        }
    }

    private void addTransaction() {
        try {
            double amount = Double.parseDouble(amountField.getText().replace(",", "."));
            String category = (String) categoryCombo.getSelectedItem();
            String description = descriptionField.getText();
            boolean isExpense = expenseRadio.isSelected();

            if (category == null || category.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Proszę wybrać lub wpisać kategorię!");
                return;
            }

            manager.addTransaction(amount, category, description, isExpense);
            updateTable();
            updateBalance();
            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Proszę wprowadzić poprawną kwotę (użyj kropki lub przecinka dla groszy)!");
        }
    }

    private void deleteSelectedTransactions() {
        int[] selectedRows = transactionTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Proszę wybrać transakcje do usunięcia!");
            return;
        }

        for (int i = selectedRows.length - 1; i >= 0; i--) {
            manager.removeTransaction(selectedRows[i]);
        }
        
        updateTable();
        updateBalance();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Transaction transaction : manager.getTransactions()) {
            tableModel.addRow(transaction.toTableRow());
        }
    }

    private void updateBalance() {
        double balance = manager.getBalance();
        balanceLabel.setText(String.format("Saldo: %.2f zł", balance));
        
        if (balance < 0) {
            balanceLabel.setForeground(Color.RED);
        } else if (balance > 0) {
            balanceLabel.setForeground(new Color(0, 100, 0));
        } else {
            balanceLabel.setForeground(Color.GRAY);
        }
    }

    private void clearInputFields() {
        amountField.setText("");
        descriptionField.setText("");
        categoryCombo.setSelectedIndex(0);
    }
}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransactionManagerGUI gui = new TransactionManagerGUI();
            gui.setVisible(true);
        });
    }
}
