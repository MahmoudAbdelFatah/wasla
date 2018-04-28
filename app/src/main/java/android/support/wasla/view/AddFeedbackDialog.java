package android.support.wasla.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class AddFeedbackDialog extends Activity {
    private EditText feedbackET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.support.wasla.R.layout.activity_add_feedback_dialog);
        feedbackET = findViewById(android.support.wasla.R.id.et_feedback);
        final Button addButton = findViewById(android.support.wasla.R.id.add_feedback_button);
        final Button cancelButton = findViewById(android.support.wasla.R.id.cancel_feedback_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                if (feedbackET.getText().toString().isEmpty() || feedbackET.getText().toString().trim().length() == 0)
                    Toasty.warning(getApplicationContext(), "Empty Text!", Toast.LENGTH_SHORT, true).show();
                else {
                    returnIntent.putExtra(getString(android.support.wasla.R.string.feedback), feedbackET.getText().toString().trim());
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
