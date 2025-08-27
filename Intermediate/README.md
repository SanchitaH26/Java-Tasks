
---

### 4. **Intermediate/README.md**
```markdown
# Intermediate Tasks 


## Tasks
- **Task1** → Bank Account Management System with Unit Testing
- **Goal** → Implement a full banking logic app with JUnit test cases.
##  Run Instructions
```bash
javac Calculator.java
java Calculator
```
### **Features**

Account Creation

Create a new bank account with Owner Name, Initial Balance, Overdraft Limit, and Annual Interest Rate.

Auto-generates unique account IDs (e.g., ACC-1).

Validates numeric input for balances, overdrafts, and interest rates.

View Balances

Display all accounts with Account ID, Owner Name, Balance, Overdraft Limit, and Interest Rate.

Deposit Money

Deposit an amount to a selected account.

Updates balance and records transaction history.

Withdraw Money

Withdraw money from a selected account.

Enforces a minimum required balance (e.g., ₹500).

Prevents overdrawing beyond allowed limits.

Records transaction history.

Transfer Money

Transfer funds between two accounts.

Checks minimum balance constraints on source account.

Records transaction in both accounts’ histories.

Apply Interest

Apply monthly interest to an account based on an annual interest rate.

Supports specifying the number of months.

Updates account balance accordingly.

Transaction History

Maintain a detailed history of deposits, withdrawals, and transfers.

View transactions for any account.

User-Friendly Menu

Interactive console menu with clear options.

Handles invalid input and provides appropriate error messages.

Data Safety

Validates inputs to prevent incorrect data entry.

Prevents withdrawals or transfers that violate minimum balance requirements.

Unit Testing Ready

Backend logic designed to allow unit testing of deposit, withdraw, transfer, and interest calculation functions.

### **Expected Output**

1. Account Creation

Enter owner name: John Doe
Enter initial balance: 10000
Enter overdraft limit: 2000
Enter annual interest rate (e.g., 6 for 6%): 5
 Account created successfully: ACC-1 | Owner: John Doe | Balance: 10000 | Overdraft: 2000 | Interest: 5%

2. Viewing Balances

ACC-1 | Owner: John Doe | Balance: 10000 | Overdraft: 2000 | Interest: 5%
ACC-2 | Owner: Alice Smith | Balance: 5000 | Overdraft: 1000 | Interest: 6%

3. Deposit Money

Choose account number for deposit: 1
Enter deposit amount: 2000
New balance for John Doe: 12000

4. Withdraw Money

Choose account number for withdraw: 1
Enter withdraw amount: 11500
Withdrawal denied: balance would fall below 500.00.

5. Transfer Money

Choose source account: 1
Choose destination account: 2
Enter transfer amount: 3000
Transfer successful!

6. Apply Interest

Choose account number to apply interest: 2
Enter annual interest rate (%): 6
Enter number of months: 3
Interest applied. New balance: 5065.00

7. Show Transactions

Transactions for ACC-1 (John Doe):
  Deposit: +2000.00 | Balance: 12000.00
  Withdrawal: -3000.00 | Balance: 9000.00
  Transfer to ACC-2: -3000.00 | Balance: 6000.00

8. Invalid Input Handling

Choose account number for deposit: 5
Invalid choice.


- **Task2/** → Inventory Management System with Basic GUI 

**Goal**: Build a GUI-based app to manage product stock, price, and quantity.
##  Run Instructions
```bash
javac App.java
java App
```
### **Features**

1. Add Products

Enter Name, Price, Quantity, and Barcode.

Validates numeric fields (Price as double, Quantity as integer).

Prevents negative values.

2. Edit & Save Products

Select a product from the table and load its details into the form.

Update details and save changes back to the table.

Prevents editing conflicts (disable Add/Delete while editing).

3. Delete Products

Delete the currently selected product from the table.

Prevents accidental deletion while editing.

4. Live Search by Name

Instant filtering as you type in the Search name field.

Case-insensitive matching.

5. Filter by Quantity & Price

Show products with Quantity ≥ X.

Show products with Price ≤ Y.

Supports multiple filters combined (name + quantity + price).

6. Clear Filters

Reset all filters and show the full inventory again.

7. Transaction Total (Visible Products)

Displays the total value of all currently visible products in the bottom bar.

Updates dynamically when filtering, adding, editing, or deleting products.

8. User-Friendly Interface

Organized layout with form panel, filter panel, and table view.

Buttons for quick actions (Add, Edit, Save, Delete, Apply/Clear Filters).

Informative error dialogs for invalid inputs.

### **Expected Output**

1. Adding a Product

Name: Apple
Price: 1.5
Quantity: 10
Barcode: A12345
Product added successfully

Table updates:

Name      | Price | Quantity | Barcode
--------------------------------------
Apple     | 1.5   | 10       | A12345


Bottom label updates:

Total (visible): 15.00

2. Editing a Product

Select "Apple" row → fields auto-fill.

Change Price: 2.0, Quantity: 8 → click Save.

Product updated successfully


Table updates:

Name      | Price | Quantity | Barcode
--------------------------------------
Apple     | 2.0   | 8        | A12345


Bottom label updates:

Total (visible): 16.00

3. Deleting a Product

Select "Apple" row → click Delete.

Product deleted


Table:

(empty)


Bottom label:

Total (visible): 0.00

4. Live Search

Type ap in Search name → instantly filters to products containing "ap".

5. Applying Filters

Example: Min quantity 5, Max price 100.

Table only shows products with Quantity ≥ 5 AND Price ≤ 100.

Bottom label recalculates only for visible rows.

6. Clearing Filters

Click Clear Filters → resets table to show all products.

7. Error Handling

Enter negative price:

Price and Quantity cannot be negative.


Enter non-numeric values in Price/Quantity:

Price must be a number and Quantity must be an integer.


Click Edit/Delete with no selection:

Select a product to edit/delete.