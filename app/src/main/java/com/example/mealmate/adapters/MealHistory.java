package com.example.mealmate.adapters;

public class MealHistory {
    private String dateTime;
    private String mealType;
    private String status;

    public MealHistory() {}

    public MealHistory(String dateTime, String mealType, String status) {
        this.dateTime = dateTime;
        this.mealType = mealType;
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMealType() {
        return mealType;
    }

    public String getStatus() {
        return status;
    }
}
