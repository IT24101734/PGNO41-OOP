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

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public double getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Date getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(Date cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    // Calculate days overdue if the movie is late
    public int calculateDaysOverdue() {
        if (returned && returnDate != null && returnDate.after(dueDate)) {
            long diffInMillies = returnDate.getTime() - dueDate.getTime();
            return (int) (diffInMillies / (1000 * 60 * 60 * 24)) + 1; // +1 to include the due date
        }
        return 0;
    }

    // Calculate days remaining in the rental period
    public int calculateDaysRemaining() {
        if (returned || canceled) {
            return 0;
        }

        Date currentDate = new Date();
        if (currentDate.after(dueDate)) {
            return 0; // No days remaining, already overdue
        }

        long diffInMillies = dueDate.getTime() - currentDate.getTime();
        return (int) (diffInMillies / (1000 * 60 * 60 * 24)) + 1; // +1 to include today
    }

    // Get rental duration in days
    public int getRentalDuration() {
        long diffInMillies = dueDate.getTime() - rentalDate.getTime();
        return (int) (diffInMillies / (1000 * 60 * 60 * 24)) + 1; // +1 to include the start date
    }

    // Check if the rental is currently overdue
    public boolean isOverdue() {
        if (returned || canceled) {
            return false;
        }
        return new Date().after(dueDate);
    }

    // Check if the rental is active (not returned and not canceled)
    public boolean isActive() {
        return !returned && !canceled;
    }


}
