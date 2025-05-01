package com.example.mealmate.adapters;

import com.example.mealmate.model.MealHistory;
import com.example.mealmate.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    private List<MealHistory> historyList;

    public MealAdapter(List<MealHistory> historyList) {
        this.historyList = historyList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTime, mealType, status;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.dateTime);
            mealType = itemView.findViewById(R.id.mealType);
            status = itemView.findViewById(R.id.status);
        }
    }

    @Override
    public MealAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MealHistory item = historyList.get(position);
        holder.dateTime.setText(item.getDateTime());
        holder.mealType.setText("Meal Type: " + item.getMealType());
        holder.status.setText("Status: " + item.getStatus());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
