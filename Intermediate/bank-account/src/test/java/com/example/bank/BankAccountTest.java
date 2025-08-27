package com.example.bank;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankAccountTest {

    private BankAccount a;
    private BankAccount b;

    @BeforeEach
    public void setup() {
        a = new BankAccount("A-001", "Alice", BigDecimal.valueOf(1000.00), BigDecimal.valueOf(200.00), BigDecimal.valueOf(12.0));
        b = new BankAccount("B-001", "Bob", BigDecimal.valueOf(500.00), BigDecimal.ZERO, BigDecimal.valueOf(6.0));
    }

    
    @Test public void deposit_increasesBalance() {
        a.deposit(BigDecimal.valueOf(250.00));
        assertEquals(1250.00, a.getBalance().doubleValue(), 0.001);
    }

    @Test public void deposit_recordsTransaction() {
        int before = a.getTransactions().size();
        a.deposit(BigDecimal.valueOf(100.00));
        assertEquals(before + 1, a.getTransactions().size());
        assertEquals(Transaction.Type.DEPOSIT, a.getTransactions().get(a.getTransactions().size() - 1).getType());
    }

    @Test public void deposit_rejectsZero() {
        assertThrows(InvalidAmountException.class, () -> a.deposit(BigDecimal.ZERO));
    }

    @Test public void deposit_rejectsNegative() {
        assertThrows(InvalidAmountException.class, () -> a.deposit(BigDecimal.valueOf(-10)));
    }

    @Test public void deposit_largeAmountWorks() {
        a.deposit(BigDecimal.valueOf(1_000_000.55));
        assertTrue(a.getBalance().doubleValue() > 1_000_000);
    }

    
    @Test public void withdraw_decreasesBalance() {
        a.withdraw(BigDecimal.valueOf(200.00));
        assertEquals(800.00, a.getBalance().doubleValue(), 0.001);
    }

    @Test public void withdraw_exactBalanceAllowed() {
        BankAccount c = new BankAccount("C", "Carl", BigDecimal.valueOf(100.00), BigDecimal.ZERO, BigDecimal.valueOf(5.0));
        c.withdraw(BigDecimal.valueOf(100.00));
        assertEquals(0.00, c.getBalance().doubleValue(), 0.001);
    }

    @Test public void withdraw_usingOverdraftAllowedWithinLimit() {
        a.withdraw(BigDecimal.valueOf(1100.00)); 
        assertEquals(-100.00, a.getBalance().doubleValue(), 0.001);
    }

    @Test public void withdraw_exceedsOverdraftThrows() {
        assertThrows(InsufficientFundsException.class, () -> a.withdraw(BigDecimal.valueOf(1301.00)));
    }

    @Test public void withdraw_invalidAmountThrows() {
        assertThrows(InvalidAmountException.class, () -> a.withdraw(BigDecimal.ZERO));
        assertThrows(InvalidAmountException.class, () -> a.withdraw(BigDecimal.valueOf(-5)));
    }

    
    @Test public void transfer_successful_movesFunds() {
        a.transferTo(b, BigDecimal.valueOf(200.00));
        assertEquals(800.00, a.getBalance().doubleValue(), 0.001);
        assertEquals(700.00, b.getBalance().doubleValue(), 0.001);
    }

    @Test public void transfer_recordsTransactionsOnBoth() {
        int aBefore = a.getTransactions().size();
        int bBefore = b.getTransactions().size();
        a.transferTo(b, BigDecimal.valueOf(50.00));
        assertEquals(aBefore + 1, a.getTransactions().size());
        assertEquals(bBefore + 1, b.getTransactions().size());
    }

    @Test public void transfer_toSelf_throws() {
        assertThrows(IllegalArgumentException.class, () -> a.transferTo(a, BigDecimal.valueOf(10)));
    }

    @Test public void transfer_insufficientFundsThrows() {
        
        assertThrows(InsufficientFundsException.class, () -> a.transferTo(b, BigDecimal.valueOf(1300)));
    }

    @Test public void transfer_invalidAmountThrows() {
        assertThrows(InvalidAmountException.class, () -> a.transferTo(b, BigDecimal.ZERO));
    }

    
    @Test
    public void applyMonthlyInterest_positiveRate_increasesBalance() {
        a.applyMonthlyInterest(BigDecimal.valueOf(12.0), 1); 
        assertEquals(1010.00, a.getBalance().doubleValue(), 0.001);
    }
    @Test
    public void applyMonthlyInterest_multipleMonths_appliesEachMonth() {
        a.applyMonthlyInterest(BigDecimal.valueOf(12.0), 2); 
        assertEquals(1020.10, a.getBalance().doubleValue(), 0.01);
    }

    @Test 
    public void applyMonthlyInterest_recordsTransaction() {
        int before = a.getTransactions().size();
        a.applyMonthlyInterest(BigDecimal.valueOf(12.0), 1);
        assertEquals(before + 1, a.getTransactions().size());
        assertEquals(Transaction.Type.INTEREST, a.getTransactions().get(a.getTransactions().size() - 1).getType());
    }

    @Test 
    public void applyMonthlyInterest_zeroRate_throws() {
        assertThrows(IllegalArgumentException.class, () -> a.applyMonthlyInterest(BigDecimal.ZERO, 1));
    }

    @Test
    public void applyMonthlyInterest_negativeMonths_throws() {
        assertThrows(IllegalArgumentException.class, () -> a.applyMonthlyInterest(BigDecimal.valueOf(5.0), 0));
        assertThrows(IllegalArgumentException.class, () -> a.applyMonthlyInterest(BigDecimal.valueOf(5.0), -2));
    }

}
