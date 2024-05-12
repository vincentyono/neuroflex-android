package com.example.neuroflex;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BarGraphAdapter extends RecyclerView.Adapter<BarGraphAdapter.ViewHolder> {

    private List<BarGraphItem> data;

    public BarGraphAdapter(List<BarGraphItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bargraph_stats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BarGraphItem item = data.get(position);
        holder.textViewLabel.setText(item.getLabel());
        holder.textViewValue.setText(String.valueOf(item.getValue()));
        // Set bar graph icon here if needed
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewLabel, textViewValue;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLabel = itemView.findViewById(R.id.textViewLabel);
            textViewValue = itemView.findViewById(R.id.textViewValue);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
