package com.example.neuroflex;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.List;
import android.view.Gravity;

public class Stats extends AppCompatActivity {

    private HorizontalBarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Initialize views
        barChart = findViewById(R.id.barChart);

        // Setup the HorizontalBarChart with dummy data
        setupBarChart();
    }

    private void setupBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 80f));
        entries.add(new BarEntry(1f, 85f));
        entries.add(new BarEntry(2f, 90f));

        BarDataSet dataSet = new BarDataSet(entries, "Score");
        dataSet.setColor(getResources().getColor(R.color.orange)); // Set color to orange

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Customize chart appearance
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);

        // Hide grid lines
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);

        // Hide labels on right side and top
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setEnabled(false);

        // Invalidate the chart to refresh
        barChart.invalidate();
    }
}