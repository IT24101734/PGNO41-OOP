package com.movierental.model.rental;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private String userId;
    private String movieId;
    private Date rentalDate;
    private Date dueDate;
    private Date returnDate;
    private double rentalFee;
    private double lateFee;
    private boolean returned;
    private boolean canceled;
    private String cancellationReason;
    private Date cancellationDate;


}
