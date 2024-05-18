package com.example.neuroflex.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.neuroflex.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    private List<DocumentSnapshot> featuredList;
    private Context context;

    public FeaturedAdapter(List<DocumentSnapshot> featuredList, Context context) {
        this.featuredList = featuredList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_featured, parent, false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        DocumentSnapshot featuredItem = featuredList.get(position);

        String title = featuredItem.getString("title");
        String description = featuredItem.getString("description");
        String imageUrl = featuredItem.getString("imageUrl");
        String articleUrl = featuredItem.getString("articleUrl");


        holder.title.setText(title);
        holder.description.setText(description);
        Glide.with(context).load(imageUrl).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return featuredList.size();
    }

    static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView description;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            description = itemView.findViewById(R.id.featured_description);
        }
    }
}
