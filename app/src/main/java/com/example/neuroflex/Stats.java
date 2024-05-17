package com.example.neuroflex;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Stats extends AppCompatActivity {

    private HorizontalBarChart speedBarChart;
    private HorizontalBarChart accuracyBarChart;
    private HorizontalBarChart timeBarChart;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_stats);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_game) {
                startActivity(new Intent(getApplicationContext(), GameMenu.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_stats) {
                return true;
            } else if (itemId == R.id.bottom_leaderboard) {
                startActivity(new Intent(getApplicationContext(), Leaderboard.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        // Initialize views
        speedBarChart = findViewById(R.id.speedChart);
        accuracyBarChart = findViewById(R.id.accuracyChart);
        timeBarChart = findViewById(R.id.timeChart);
        lineChart = findViewById(R.id.lineChart);

        // Get daily scores and set up the charts
        DbQuery.getDailyScores(new DbQuery.DailyScoresListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> dailyScores) {
                // Set up the line chart
                setupLineChart(dailyScores);

                // Once daily scores are loaded, get average stats for each game mode
                DbQuery.getAverageStats(2, new DbQuery.OnAverageStatsLoadedListener() {
                    @Override
                    public void onAverageStatsLoaded(Map<String, Double> modeStats) {
                        Log.d(TAG, "Average Stats Loaded: " + modeStats);
                        setupBarChart(speedBarChart,modeStats.get("Speed"));
                        setupBarChart(accuracyBarChart, modeStats.get("Accuracy"));
                        setupBarChart(timeBarChart, modeStats.get("Time"));
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "Failed to get average stats: " + e.getMessage());
                    }
                });

            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Failed to get daily scores");
            }
        });
    }

    private void setupLineChart(List<Map<String, Object>> dailyScores) {
        List<Entry> entries = new ArrayList<>();

        // Loop through daily scores and add them to chart
        for (Map<String, Object> scoreData : dailyScores) {
            Timestamp scoreDate = (Timestamp) scoreData.get("date");
            int score = (int) (long) scoreData.get("score"); // Cast to int

            // Convert timestamp to Unix timestamp in milliseconds for chart
            long timestamp = scoreDate.toDate().getTime();
            entries.add(new Entry(timestamp, score));
        }

        // Create a dataset with the entries
        LineDataSet dataSet = new LineDataSet(entries, "Score");

        dataSet.setColor(Color.parseColor("#359C5B")); // Set color to green
        dataSet.setCircleColor(Color.parseColor("#359C5B"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.parseColor("#359C5B"));

        // Create a LineData object with the dataset
        LineData lineData = new LineData(dataSet);

        // Set data to the chart
        lineChart.setData(lineData);

        // Customize chart appearance
        lineChart.getDescription().setEnabled(false); // Disable description
        lineChart.getLegend().setEnabled(false); // Disable legend
        lineChart.getAxisLeft().setEnabled(false); // Disable left axis
        lineChart.getAxisRight().setEnabled(false); // Disable right axis
        lineChart.getXAxis().setEnabled(false); // Disable X-axis

        // Refresh the chart
        lineChart.invalidate();
    }

    private void setupBarChart(HorizontalBarChart barChart, double value) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float) value));

        BarDataSet dataSet = new BarDataSet(entries, "Input Value");
        dataSet.setColor(Color.parseColor("#AAC22F"));

        BarData data = new BarData(dataSet);

        // Configure the chart
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        // Disable touch gestures for zooming
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);

        // Disable right axis
        barChart.getAxisRight().setEnabled(false);

        // Formatting X axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false); // Hide axis line
        xAxis.setDrawGridLines(false); // Hide grid lines
        xAxis.setDrawLabels(false); // Hide labels

        // Formatting Y axis
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setDrawAxisLine(false); // Hide axis line
        yAxis.setDrawGridLines(false); // Hide grid lines
        yAxis.setDrawLabels(false); // Hide labels

        // Disable highlight effect
        dataSet.setHighlightEnabled(false);

        // Refresh the chart
        barChart.invalidate();
    }
}
