package com.example.food_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.R;
import com.example.food_app.model.RecycleViewRecommendModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewRecommendAdapter extends RecyclerView.Adapter<RecycleViewRecommendAdapter.ViewHolder> {

    private final List<RecycleViewRecommendModel> itemModelList;

    private Context context;

    public RecycleViewRecommendAdapter(List<RecycleViewRecommendModel> itemModelList) {
        this.itemModelList = itemModelList;
    }

    @NonNull
    @Override
    public RecycleViewRecommendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_recommend, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewRecommendAdapter.ViewHolder holder, int position) {

        RecycleViewRecommendModel recommendModel = itemModelList.get(position);
        Picasso.get().load(recommendModel.getImageResId()).into(holder.item_image);
        holder.txt_Tital.setText(recommendModel.gettitle());
        holder.txt_Rating.setText(recommendModel.getRating());
        holder.txt_Price.setText(recommendModel.getPrice());
        holder.txt_Description.setText(recommendModel.getDescription());

        holder.btn_floating.setOnClickListener(v -> {
            Toast.makeText(context, recommendModel.gettitle(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        FloatingActionButton btn_floating;
        TextView txt_Tital, txt_Rating, txt_Description, txt_Price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            btn_floating = itemView.findViewById(R.id.btn_floating);
            txt_Tital = itemView.findViewById(R.id.txt_Tital);
            txt_Rating = itemView.findViewById(R.id.txt_Rating);
            txt_Description = itemView.findViewById(R.id.txt_Description);
            txt_Price = itemView.findViewById(R.id.txt_Price);
        }
    }
}
