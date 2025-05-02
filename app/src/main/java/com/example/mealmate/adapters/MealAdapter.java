package com.example.mealmate.adapters;
import com.example.mealmate.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.model.MealHistory;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<MealHistory> mealList;

    public MealAdapter(List<MealHistory> mealList) {
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_history, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealHistory meal = mealList.get(position);
        holder.tvMealType.setText("Meal: " + meal.getMealType());
        holder.tvDateTime.setText("Time: " + meal.getDateTime());
        holder.tvStatus.setText("Status: " + meal.getStatus());

        // Color code the status
        switch (meal.getStatus()) {
            case "Pending":
                holder.tvStatus.setTextColor(Color.YELLOW);
                break;
            case "Delivered":
                holder.tvStatus.setTextColor(Color.GREEN);
                break;
            case "Collected":
                holder.tvStatus.setTextColor(Color.BLUE);
                break;
            default:
                holder.tvStatus.setTextColor(Color.BLACK);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView tvMealType, tvDateTime, tvStatus;

        MealViewHolder(View itemView) {
            super(itemView);
            tvMealType = itemView.findViewById(R.id.tv_meal_type);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }

    public void updateData(List<MealHistory> newList) {
        mealList.clear();
        mealList.addAll(newList);
        notifyDataSetChanged();
    }

}
