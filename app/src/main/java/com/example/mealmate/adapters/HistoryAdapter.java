package com.example.mealmate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealmate.R;
import com.example.mealmate.model.MealHistory;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<MealHistory> historyList;

    public HistoryAdapter(List<MealHistory> historyList) {
        this.historyList = historyList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealType, dateTime, status;

        public ViewHolder(View view) {
            super(view);
            mealType = view.findViewById(R.id.tvMealType);
            dateTime = view.findViewById(R.id.tvDateTime);
            status = view.findViewById(R.id.tvStatus);
        }
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealHistory item = historyList.get(position);
        holder.mealType.setText("Meal: " + item.getMealType());
        holder.dateTime.setText("Date: " + item.getDateTime());
        holder.status.setText("Status: " + item.getStatus());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
