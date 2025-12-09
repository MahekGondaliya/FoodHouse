package com.example.food_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.R;
import com.example.food_app.model.Option;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {


    private final List<Option> items;

    public OptionsAdapter(List<Option> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsAdapter.ViewHolder holder, int position) {
        Option opt = items.get(position);
        holder.option_icon.setImageResource(opt.getIconRes());
        holder.option_text.setText(opt.getTitle());
        holder.itemView.setOnClickListener(v -> opt.getAction().run());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView option_icon;
        TextView option_text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            option_icon = itemView.findViewById(R.id.option_icon);
            option_text = itemView.findViewById(R.id.option_text);
        }
    }
}
