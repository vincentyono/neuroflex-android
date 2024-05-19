package com.example.neuroflex;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Performance extends AppCompatActivity {

    private Button btnFinish;
    private LineChart lineChart;
    private TextView scoreText, objectiveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        // Initialize buttons and views
        btnFinish = findViewById(R.id.btnNext);
        scoreText = findViewById(R.id.scoreView);
        lineChart = findViewById(R.id.lineChart);
        objectiveText = findViewById(R.id.objectiveTextView);

        Intent intent = getIntent();

        // Get list of scores for each question
        ArrayList<Integer> scoresList = intent.getIntegerArrayListExtra("scoresList");

        // Get score from the game
        int score = intent.getIntExtra("score", 0);

        // Set the score text to the score
        scoreText.setText(String.valueOf(score));

        // Setting objective text based on rank
        DbQuery.getUserTotalScore(new TotalScoreListener() {
            @Override
            public void onSuccess(int totalScore) {
                int pointsToNextTier = RankUtils.pointsToNextTier(totalScore);

                objectiveText.setText(String.valueOf(pointsToNextTier));
            }

            @Override
            public void onFailure() {
                Log.e("RankingSystem", "Failed to retrieve total score");
            }
        });

        // Call the setupLineChart method
        // Shows the score evolution (how the points progress over the questions in the last game)
        setupLineChart(scoresList);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(Performance.this, GameMenu.class);
                mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mainMenuIntent);
            }
        });
    }

    // Function to set up the line chart
    private void setupLineChart(ArrayList<Integer> scoresList) {
        List<Entry> entries = new ArrayList<>();

        // Check if score list is null
        if (scoresList != null) {
            for (int i = 0; i < scoresList.size(); i++) {
                entries.add(new Entry(i, scoresList.get(i)));
            }
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
}
