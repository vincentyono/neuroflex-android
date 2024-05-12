package com.example.neuroflex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PersonalStats extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BarGraphAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_stats);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data for bar graphs
        List<BarGraphItem> data = new ArrayList<>();
        data.add(new BarGraphItem("Speed", 75));
        data.add(new BarGraphItem("Accuracy", 90));
        data.add(new BarGraphItem("Time", 80));

        adapter = new BarGraphAdapter(data);
        recyclerView.setAdapter(adapter);
    }
}
