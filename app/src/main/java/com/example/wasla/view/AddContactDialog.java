package com.example.wasla.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wasla.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddContactDialog extends Activity {
    private EditText nameEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_dialog);

        nameEditText = (EditText) findViewById(R.id.name);
        emailEditText = (EditText) findViewById(R.id.email);
        final Button addButton = (Button) findViewById(R.id.addContactButton);
        final Button cancelButton = (Button) findViewById(R.id.cancelContactButton);
        final Spinner genderSpinner=(Spinner)findViewById(R.id.spinner_gender);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValid()) {
                    return;
                }
                String email = emailEditText.getText().toString();
                if(!isValidEmail(email)) {
                    emailEditText.setError("not valid email");
                    return;
                }
                int genderIndex=genderSpinner.getSelectedItemPosition();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("name", nameEditText.getText().toString());
                returnIntent.putExtra("email", emailEditText.getText().toString());
                switch (genderIndex)
                {
                    case 0 :
                        returnIntent.putExtra("gender", "male");
                        break;
                    case 1 :
                        returnIntent.putExtra("gender", "female");
                        break;
                }

                setResult(RESULT_OK, returnIntent);
                finish();
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

    public boolean isValid() {
        boolean flag = true;
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (name.toString().trim().length() == 0) {
            nameEditText.setError("ENTER THE Name");
            flag = false;
        }
        if (email.toString().trim().length() == 0) {
            emailEditText.setError("ENTER THE Email");
            flag = false;
        }
        return flag;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
