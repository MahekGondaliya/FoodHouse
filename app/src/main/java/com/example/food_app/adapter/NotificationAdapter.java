package com.example.food_app.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.R;
import com.example.food_app.model.NotificationModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<NotificationModel> notificationList;

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel model = notificationList.get(position);

        holder.title.setText(model.getTitle());
        holder.subtitle.setText(model.getSubtitle());
        holder.description.setText(model.getDescription());
        holder.date.setText(model.getReadableDate());
        SharedPreferences sp =
                holder.itemView.getContext()
                        .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        String userId = sp.getString("USER_ROLE", null);

         if (userId.equals("admin")){
             holder.deleteIcon.setOnClickListener(v -> {
                 int adapterPosition = holder.getAdapterPosition();
                 if (adapterPosition == RecyclerView.NO_POSITION) return;

                 // Extra safety
                 if (adapterPosition < 0 || adapterPosition >= notificationList.size()) return;

                 NotificationModel notification = notificationList.get(adapterPosition);
                 String notificationId = notification.getNotificationId();

                 if (notificationId == null || notificationId.isEmpty()) {
                     Toast.makeText(holder.itemView.getContext(),
                             "Error: Notification ID missing",
                             Toast.LENGTH_SHORT).show();
                     return;
                 }

                 // Change "notifications" only if your node name is different
                 DatabaseReference ref = FirebaseDatabase.getInstance()
                         .getReference("notifications")
                         .child(notificationId);

                 ref.removeValue()
                         .addOnSuccessListener(unused -> {
                             // Remove from local list and notify adapter
                             notificationList.remove(adapterPosition);
                             notifyItemRemoved(adapterPosition);

                             Toast.makeText(holder.itemView.getContext(),
                                     "Notification deleted",
                                     Toast.LENGTH_SHORT).show();
                         })
                         .addOnFailureListener(e ->
                                 Toast.makeText(holder.itemView.getContext(),
                                         "Failed to delete: " + e.getMessage(),
                                         Toast.LENGTH_SHORT).show());
             });
         }else
             holder.deleteIcon.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, subtitle, description, date;
        ImageView deleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            subtitle = itemView.findViewById(R.id.textSubtitle);
            description = itemView.findViewById(R.id.textDescription);
            date = itemView.findViewById(R.id.textDate);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
