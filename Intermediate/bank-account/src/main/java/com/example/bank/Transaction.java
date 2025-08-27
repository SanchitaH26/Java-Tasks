package com.example.bank;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Transaction {
    public enum Type { DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST }

    private final Type type;
    private final BigDecimal amount;
    private final Instant timestamp;
    private final String description;

    public Transaction(Type type, BigDecimal amount, String description) {
        this.type = Objects.requireNonNull(type);
        this.amount = amount;
        this.timestamp = Instant.now();
        this.description = description == null ? "" : description;
    }

    public Type getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public Instant getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return timestamp + " " + type + " " + amount + " " + description;
    }
}
