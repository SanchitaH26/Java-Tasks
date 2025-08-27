package com.example.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BankAccount {
    private final String accountId;
    private final String ownerName;
    private BigDecimal balance;
    private BigDecimal overdraftLimit; 
    private BigDecimal interestRate; 
    private final List<Transaction> transactions = new ArrayList<>();

    public BankAccount(String accountId, String ownerName, BigDecimal initialBalance, BigDecimal overdraftLimit, BigDecimal interestRate) {
        this.accountId = Objects.requireNonNull(accountId);
        this.ownerName = Objects.requireNonNull(ownerName);
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Initial balance must be non-negative");
        }
        this.balance = initialBalance.setScale(2, RoundingMode.HALF_UP);
        this.overdraftLimit = (overdraftLimit == null) ? BigDecimal.ZERO : overdraftLimit.setScale(2, RoundingMode.HALF_UP);
        this.interestRate = (interestRate == null) ? BigDecimal.ZERO : interestRate.setScale(2, RoundingMode.HALF_UP);
    }

    public String getAccountId() { return accountId; }
    public String getOwnerName() { return ownerName; }
    public BigDecimal getBalance() { return balance; }
    public BigDecimal getInterestRate() { return interestRate; }
    public BigDecimal getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(BigDecimal newLimit) {
        if (newLimit == null || newLimit.compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException("Overdraft must be >= 0");
        this.overdraftLimit = newLimit.setScale(2, RoundingMode.HALF_UP);
    }
    public void setInterestRate(BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Interest rate must be >= 0");
        }
        this.interestRate = rate.setScale(2, RoundingMode.HALF_UP);
    }
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void deposit(BigDecimal amount) {
        validateAmountPositive(amount);
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        balance = balance.add(amount);
        transactions.add(new Transaction(Transaction.Type.DEPOSIT, amount, "Deposit"));
    }

    public void withdraw(BigDecimal amount) {
        validateAmountPositive(amount);
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal allowedNegative = balance.negate().add(overdraftLimit);
        if (balance.subtract(amount).compareTo(overdraftLimit.negate()) < 0) {
            throw new InsufficientFundsException("Insufficient funds including overdraft.");
        }
        balance = balance.subtract(amount);
        transactions.add(new Transaction(Transaction.Type.WITHDRAWAL, amount, "Withdrawal"));
    }

    public void transferTo(BankAccount other, BigDecimal amount) {
        if (other == null) throw new IllegalArgumentException("Destination account cannot be null");
        if (other == this) throw new IllegalArgumentException("Cannot transfer to same account");
        validateAmountPositive(amount);
        amount = amount.setScale(2, RoundingMode.HALF_UP);

        
        if (balance.subtract(amount).compareTo(overdraftLimit.negate()) < 0) {
            throw new InsufficientFundsException("Insufficient funds including overdraft to transfer");
        }
        balance = balance.subtract(amount);
        other.balance = other.balance.add(amount);

        transactions.add(new Transaction(Transaction.Type.TRANSFER_OUT, amount, "Transfer to " + other.accountId));
        other.transactions.add(new Transaction(Transaction.Type.TRANSFER_IN, amount, "Transfer from " + this.accountId));
    }

    
    public void applyMonthlyInterest(BigDecimal annualRate, int months) {
        if (annualRate == null || annualRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Interest rate must be positive");
        }
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Cannot apply interest on non-positive balance");
        }
        if (months <= 0) {
            throw new IllegalArgumentException("Months must be positive");
        }

        
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        for (int i = 0; i < months; i++) {
            BigDecimal interest = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            balance = balance.add(interest);
            transactions.add(new Transaction(Transaction.Type.INTEREST, interest, "Monthly interest"));
        }
    }


    private static void validateAmountPositive(BigDecimal amount) {
        if (amount == null) throw new InvalidAmountException("Amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new InvalidAmountException("Amount must be positive");
    }

    @Override
    public String toString() {
        return "Account[" + accountId + "] owner=" + ownerName + " balance=" + balance + " overdraft=" + overdraftLimit;
    }
}
