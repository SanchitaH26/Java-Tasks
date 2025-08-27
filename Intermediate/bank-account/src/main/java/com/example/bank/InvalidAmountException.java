package com.example.bank;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) { super(message); }
}
