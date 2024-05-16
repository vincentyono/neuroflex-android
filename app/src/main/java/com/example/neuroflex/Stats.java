package com.example.neuroflex;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.view.Gravity;

public class Stats extends AppCompatActivity {

    private HorizontalBarChart barChart;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Initialize views
        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);

        // Fetch and display daily scores
        fetchAndDisplayDailyScores();
    }

    private void setupLineChart(List<Map<String, Object>> dailyScores) {
        List<Entry> entries = new ArrayList<>();

        // Loop through the daily scores and add them to the chart
        for (Map<String, Object> scoreData : dailyScores) {
            Timestamp scoreDate = (Timestamp) scoreData.get("date");
            int score = (int) (long) scoreData.get("score"); // Cast to int

            // Convert timestamp to Unix timestamp in milliseconds for chart
            long timestamp = scoreDate.toDate().getTime();
            entries.add(new Entry(timestamp, score));
        }

        // Create a dataset with the entries
        LineDataSet dataSet = new LineDataSet(entries, "Score");

        // Customize the dataset
        dataSet.setColor(Color.parseColor("#359C5B")); // Set color to #359C5B
        dataSet.setCircleColor(Color.parseColor("#359C5B")); // Set circle color to #359C5B
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.parseColor("#359C5B")); // Set value text color to #359C5B

        // Create a LineData object with the dataset
        LineData lineData = new LineData(dataSet);

        // Set data to the chart
        lineChart.setData(lineData);

        // Customize chart appearance
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false); // Disable legend
        lineChart.getAxisLeft().setEnabled(false); // Disable left axis
        lineChart.getAxisRight().setEnabled(false); // Disable right axis
        lineChart.getXAxis().setEnabled(false); // Disable X-axis

        // Refresh the chart
        lineChart.invalidate();
    }

    private void fetchAndDisplayDailyScores() {
        DbQuery.getDailyScores(new DbQuery.DailyScoresListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> dailyScores) {
                // Once daily scores are fetched successfully, set up the bar chart
                setupLineChart(dailyScores);
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure to fetch daily scores
                // You can display an error message or take appropriate action here
            }
        });
    }
}
