package com.example.wasla.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wasla.R;

public class AddContactDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_dialog);
        final EditText nameEditText=(EditText)findViewById(R.id.name);
        final EditText emailEditText=(EditText)findViewById(R.id.email);
        final Button addButton=(Button)findViewById(R.id.addContactButton);
        final Button cancelButton=(Button)findViewById(R.id.cancelContactButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent=new Intent();
                returnIntent.putExtra("name",nameEditText.getText().toString());
                returnIntent.putExtra("email",emailEditText.getText().toString());

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
}
