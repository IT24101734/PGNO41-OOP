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

    // Constructor with all fields
    public Transaction(String transactionId, String userId, String movieId, Date rentalDate,
                       Date dueDate, Date returnDate, double rentalFee, double lateFee,
                       boolean returned, boolean canceled, String cancellationReason, Date cancellationDate) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.movieId = movieId;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.rentalFee = rentalFee;
        this.lateFee = lateFee;
        this.returned = returned;
        this.canceled = canceled;
        this.cancellationReason = cancellationReason;
        this.cancellationDate = cancellationDate;
    }

    // Constructor without return date and late fee (for new rentals)
    public Transaction(String transactionId, String userId, String movieId, Date rentalDate,
                       Date dueDate, double rentalFee) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.movieId = movieId;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.rentalFee = rentalFee;
        this.returnDate = null;
        this.lateFee = 0.0;
        this.returned = false;
        this.canceled = false;
        this.cancellationReason = null;
        this.cancellationDate = null;
    }

    // Default constructor
    public Transaction() {
        this.transactionId = "";
        this.userId = "";
        this.movieId = "";
        this.rentalDate = new Date();
        this.dueDate = new Date();
        this.returnDate = null;
        this.rentalFee = 0.0;
        this.lateFee = 0.0;
        this.returned = false;
        this.canceled = false;
        this.cancellationReason = null;
        this.cancellationDate = null;
    }




}
