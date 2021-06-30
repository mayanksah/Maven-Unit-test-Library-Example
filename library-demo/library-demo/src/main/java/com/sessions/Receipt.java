package com.sessions;

import java.time.LocalDate;

public class Receipt {
    LocalDate receiptDate;
    String bookName;
    Double amountGiven;
    Double actualAmount;
    Double balanceToBeReturn;

    public Receipt(String bookName, Double amountGiven, Double actualAmount) {
        this.receiptDate = LocalDate.now();
        this.bookName = bookName;
        this.amountGiven = amountGiven;
        this.actualAmount = actualAmount;
        this.balanceToBeReturn = actualAmount-amountGiven;
    }
}
