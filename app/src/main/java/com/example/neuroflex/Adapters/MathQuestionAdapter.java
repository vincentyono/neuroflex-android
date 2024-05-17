package com.example.neuroflex.Adapters;

import android.media.MediaPlayer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.neuroflex.R;
import com.example.neuroflex.mathpuzzle.MathQuestion;

import java.util.ArrayList;

public class MathQuestionAdapter extends RecyclerView.Adapter<MathQuestionAdapter.ViewHolder> {
    private ArrayList<MathQuestion> _mathQuestions;
    private MediaPlayer _mediaPlayer;

    public MathQuestionAdapter(ArrayList<MathQuestion> mathQuestions) {
        this._mathQuestions = mathQuestions;
    }

    public void removeListItem(int index) {
        this._mathQuestions.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, this._mathQuestions.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView questionView;
       EditText answerView;
       ViewHolder(View view) {
           super(view);
           questionView = view.findViewById(R.id.question);
           answerView = view.findViewById(R.id.answer);
       }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_mathpuzzle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MathQuestion question = this._mathQuestions.get(position);
        holder.questionView.setText(question.getQuestion());

        holder.answerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                question.setC(Integer.parseInt(s.toString()));

                if(question.verifyAnswer()) {
                    removeListItem(position);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return this._mathQuestions.size();
    }

}
