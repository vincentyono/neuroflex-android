package com.example.neuroflex;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
    private int gameIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_stats);

        // Initialize views
        speedBarChart = findViewById(R.id.speedChart);
        accuracyBarChart = findViewById(R.id.accuracyChart);
        timeBarChart = findViewById(R.id.timeChart);
        lineChart = findViewById(R.id.lineChart);

        Spinner gameModeSpinner = findViewById(R.id.gameSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.game_modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameModeSpinner.setAdapter(adapter);

        gameModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gameIndex = position;
                loadStatsForSelectedGameMode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "No mode selected");
            }
        });

        // Get daily scores and set up the charts
        loadStatsForSelectedGameMode();
    }

    private void loadStatsForSelectedGameMode() {
        DbQuery.getDailyScores(new DbQuery.DailyScoresListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> dailyScores) {
                // Set up the line chart
                setupLineChart(dailyScores);

                // Once daily scores are loaded, get average stats for the selected game mode
                DbQuery.getAverageStats(gameIndex, new DbQuery.OnAverageStatsLoadedListener() {
                    @Override
                    public void onAverageStatsLoaded(Map<String, Double> modeStats) {
                        Log.d(TAG, "Average Stats Loaded: " + modeStats);
                        // Clears bar charts
                        speedBarChart.clear();
                        accuracyBarChart.clear();
                        timeBarChart.clear();

                        // Check if modeStats is null or empty
                        if (modeStats.get("Speed") == 0 && modeStats.get("Accuracy") == 0 && modeStats.get("Time") == 0) {
                            // Display "Mode never played"
                            speedBarChart.setNoDataText("Mode never played");
                            accuracyBarChart.setNoDataText("Mode never played");
                            timeBarChart.setNoDataText("Mode never played");
                        } else {
                            // Sets up bar charts with new data
                            setupBarChart(speedBarChart, modeStats.get("Speed"));
                            setupBarChart(accuracyBarChart, modeStats.get("Accuracy"));
                            setupBarChart(timeBarChart, modeStats.get("Time"));
                        }
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

        Log.d(TAG, "Bar chart setup complete");
    }
}
