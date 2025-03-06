package com.example.food_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.model.ItemModel;
import com.example.food_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewBigAdapter extends RecyclerView.Adapter<RecycleViewBigAdapter.ViewHolder>{

    private final List<ItemModel> itemModelList;
  private Context context  ;

    public RecycleViewBigAdapter( List<ItemModel> itemModelList) {

        this.itemModelList = itemModelList;
    }

    @NonNull
    @Override
    public RecycleViewBigAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_big, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewBigAdapter.ViewHolder holder, int position) {
        ItemModel itemModel = itemModelList.get(position);
        Picasso.get().load(itemModel.getImageResId()).into(holder.imageView);
//        holder.textView.setText(itemModel.getName());

        holder.button.setOnClickListener(v ->
                Toast.makeText(context, "Clicked: " + itemModel.getName(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            button = itemView.findViewById(R.id.item_button);
        }
    }
}

