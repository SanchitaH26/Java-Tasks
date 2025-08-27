package com.example.bank;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
public class Main {
	
	private static final BigDecimal MIN_REQUIRED_BALANCE = new BigDecimal("500.00");
    private static Scanner sc = new Scanner(System.in);
    private static List<BankAccount> accounts = new ArrayList<>();
    private static int nextId = 1; 
    public static void main(String[] args) {
        

        System.out.println("Bank Account Management System");
        boolean quit = false;
        while (!quit) {
            printMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
            case "1": createAccount(); break;
            case "2": showBalances(); break;
            case "3": depositMoney(); break;
            case "4": withdrawMoney(); break;
            case "5": transferMoney(); break;
            case "6": applyInterest(); break;
            case "7":
                BankAccount acct = chooseAccount("show transactions");
                if (acct != null) showTransactions(acct);
                break;
            case "0": quit = true; System.out.println("Exiting system."); break;
            default: System.out.println("Invalid choice.");
        }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("Choose option:");
        System.out.println("1 - Create New Account");
        System.out.println("2 - Show Balances");
        System.out.println("3 - Deposit Money");
        System.out.println("4 - Withdraw Money");
        System.out.println("5 - Transfer Money");
        System.out.println("6 - Apply Interest");
        System.out.println("7 - Show Transactions");
        System.out.println("0 - Exit");
        System.out.print("-> ");
    }
    private static void createAccount() {
        System.out.print("Enter owner name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter initial balance: ");
        BigDecimal balance = new BigDecimal(sc.nextLine());

        System.out.print("Enter overdraft limit: ");
        BigDecimal overdraft = new BigDecimal(sc.nextLine());

        System.out.print("Enter annual interest rate (e.g., 6 for 6%): ");
        BigDecimal interest = new BigDecimal(sc.nextLine());

        String accountId = "ACC-" + (nextId++);
        BankAccount newAcc = new BankAccount(accountId, name, balance, overdraft, interest);
        accounts.add(newAcc);

        System.out.println("Account created successfully: " + newAcc);
    }
    private static BankAccount chooseAccount(String action) {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available. Please create one first.");
            return null;
        }
        System.out.println("Available accounts:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + " - " + accounts.get(i));
        }
        System.out.print("Choose account number for " + action + ": ");
        try {
            int choice = Integer.parseInt(sc.nextLine());
            if (choice < 1 || choice > accounts.size()) {
                System.out.println("Invalid choice.");
                return null;
            }
            return accounts.get(choice - 1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }
    }

    private static void showBalances() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts have been created yet.");
            return;
        }
        for (BankAccount acc : accounts) {
            System.out.println(acc);
        }
    }

    private static void depositMoney() {
        BankAccount acct = chooseAccount("deposit");
        if (acct == null) return;

        System.out.print("Enter deposit amount: ");
        BigDecimal amount = new BigDecimal(sc.nextLine());
        acct.deposit(amount);
        System.out.println("New balance for " + acct.getOwnerName() + ": " + acct.getBalance());
    }

    private static void withdrawMoney() {
        BankAccount acct = chooseAccount("withdraw");
        if (acct == null) return;

        System.out.print("Enter withdraw amount: ");
        BigDecimal amount = new BigDecimal(sc.nextLine());
        BigDecimal remaining = acct.getBalance().subtract(amount);
        if (remaining.compareTo(MIN_REQUIRED_BALANCE) < 0) {
            System.out.println("Withdrawal denied: balance would fall below " + MIN_REQUIRED_BALANCE + ".");
            return;
        }
        try {
            acct.withdraw(amount);
            System.out.println("New balance for " + acct.getOwnerName() + ": " + acct.getBalance());
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void transferMoney() {
        System.out.println("Choose source account:");
        BankAccount from = chooseAccount("transfer FROM");
        if (from == null) return;

        System.out.println("Choose destination account:");
        BankAccount to = chooseAccount("transfer TO");
        if (to == null || to == from) {
            System.out.println("Invalid destination account.");
            return;
        }

        System.out.print("Enter transfer amount: ");
        BigDecimal amount = new BigDecimal(sc.nextLine());
        BigDecimal remaining = from.getBalance().subtract(amount);
        if (remaining.compareTo(MIN_REQUIRED_BALANCE) < 0) {
            System.out.println("Transfer denied: balance would fall below " + MIN_REQUIRED_BALANCE + ".");
            return;
        }
        try {
            from.transferTo(to, amount);
            System.out.println("Transfer successful!");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void applyInterest() {
        BankAccount acct = chooseAccount("apply interest");
        if (acct == null) return;

        System.out.print("Enter annual interest rate (%): ");
        BigDecimal rate = new BigDecimal(sc.nextLine()); 

        System.out.print("Enter number of months: ");
        int months = Integer.parseInt(sc.nextLine());

        try {
            acct.applyMonthlyInterest(rate, months);  
            System.out.println("Interest applied. New balance: " + acct.getBalance());
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }




    private static void showTransactions(BankAccount acct) {
        System.out.println("Transactions for " + acct.getAccountId() + " (" + acct.getOwnerName() + "):");
        List<Transaction> txs = acct.getTransactions();
        if (txs.isEmpty()) {
            System.out.println("  (no transactions)");
            return;
        }
        for (Transaction t : txs) {
            System.out.println("  " + t);
        }
    }
}
