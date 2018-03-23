package com.example.wasla.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wasla.R;

import es.dmoral.toasty.Toasty;

public class AddFeedbackDialog extends Activity {
    private EditText feedbackET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback_dialog);
        feedbackET = findViewById(R.id.et_feedback);
        final Button addButton = findViewById(R.id.add_feedback_button);
        final Button cancelButton = findViewById(R.id.cancel_feedback_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                if (feedbackET.getText().toString().isEmpty() || feedbackET.getText().toString().trim().length() == 0)
                    Toasty.warning(getApplicationContext(), "Empty Text!", Toast.LENGTH_SHORT, true).show();
                else {
                    returnIntent.putExtra(getString(R.string.feedback), feedbackET.getText().toString().trim());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
    }
}
