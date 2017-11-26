package com.example.wasla.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.wasla.R;

public class AddFeedbackDialog extends Activity {
    private EditText feedbackET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback_dialog);
        feedbackET = (EditText) findViewById(R.id.et_feedback);
        final Button addButton = (Button) findViewById(R.id.add_feedback_button);
        final Button cancelButton = (Button) findViewById(R.id.cancel_feedback_button);
        //TODO>> amr: handle the pending list here!
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
