package com.example.mealmate.model;

public class MealHistory {
    private String dateTime;
    private String mealType;
    private String status;

    public MealHistory() {
        // Default constructor required for Firebase
    }

    public MealHistory(String dateTime, String mealType, String status) {
        this.dateTime = dateTime;
        this.mealType = mealType;
        this.status = status;
    }

    // Add getters and setters
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}