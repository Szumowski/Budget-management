# Personal Budget Manager

A Java Swing-based desktop application for managing personal finances. This application helps users track their income and expenses with a user-friendly graphical interface.

## Features

- **Transaction Management**
  - Add income and expense transaction
  - Categorize transactions with predefined or custom categories
  - Add descriptions to transactions
  - Delete selected transactions
  - Automatic date tracking for all transactions

- **Financial Overview**
  - Real-time balance calculation
  - Color-coded balance display (red for negative, green for positive)
  - Comprehensive transaction history table
  - Transaction filtering by type (Income/Expense)

- **Category Management**
  - Default expense categories: Food, Transport, Entertainment, Bills, Other
  - Default income categories: Salary, Bonus, Gift, Other
  - Support for custom categories
  - Dynamic category selection based on transaction type

## Technical Details

- Built with Java Swing for the GUI
- Uses MVC pattern:
  - `Transaction` class as the data model
  - `TransactionManager` as the controller
  - `TransactionManagerGUI` as the view
- Implements proper data encapsulation and separation of concerns
- Uses Java 8+ features including streams and lambda expressions

## User Interface

The application window is divided into several sections:
- Top panel displaying current balance
- Right panel with input form for new transactions
- Center panel with scrollable transaction history table
- Bottom panel with transaction management buttons

## How to Use

1. **Adding a Transaction:**
   - Select transaction type (Income/Expense)
   - Enter the amount (use . or , for decimals)
   - Select or enter a category
   - Add a description (optional)
   - Click "Add" button

2. **Deleting Transactions:**
   - Select one or more transactions in the table
   - Click "Delete selected" button

3. **Viewing Balance:**
   - Current balance is always displayed at the top
   - Balance updates automatically with each transaction

## Requirements

- Java Runtime Environment (JRE) 8 or higher
- Minimum screen resolution: 800x600

## Installation

1. Clone this repository
2. Compile the Java files
3. Run the `Main` class

```bash
javac Main.java
java Main
```

## Technical Implementation

The application consists of three main classes:

- `Transaction`: Represents individual financial transactions
- `TransactionManager`: Handles business logic and data management
- `TransactionManagerGUI`: Manages the user interface and event handling

## Language Support

The application interface is currently in Polish, with the following translations:
- "Wydatek" - Expense
- "Wpływ" - Income
- "Saldo" - Balance
- "Dodaj" - Add
- "Usuń zaznaczone" - Delete selected

## Contributing

Feel free to fork this repository and submit pull requests. You can also open issues for bugs or feature requests.

## License

This project is open-source and available under the MIT License.
