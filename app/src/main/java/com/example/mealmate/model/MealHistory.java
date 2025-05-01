package com.example.mealmate.model;

public class MealHistory {
    private String mealType;
    private String dateTime;
    private String status;

    public MealHistory(){}

    public MealHistory(String mealType,String dateTime,String status){
        this.mealType = mealType;
        this.dateTime = dateTime;
        this.status = status;

    }

    public String getMealType(){return mealType;}
    public String getDateTime(){return dateTime;}
    public String getStatus(){return status;}
}
