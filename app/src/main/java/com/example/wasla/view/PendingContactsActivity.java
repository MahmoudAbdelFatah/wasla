package com.example.wasla.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.wasla.R;
import com.example.wasla.adapter.PendingInstructorAdapter;
import com.example.wasla.model.OnlineDataBase;

import es.dmoral.toasty.Toasty;

public class PendingContactsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private PendingInstructorAdapter pendingInstructorAdapter;
    private OnlineDataBase onlineDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_contacts);
        setTitle("Pending Contacts");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_instructor);

        if(!isNetworkConnected()) {
            Toasty.warning(this, "check the internet connection!", Toast.LENGTH_LONG, true).show();
        }

        onlineDataBase=new OnlineDataBase();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        pendingInstructorAdapter = new PendingInstructorAdapter(this, OnlineDataBase.pendingContacts);
        recyclerView.setAdapter(pendingInstructorAdapter);
        onlineDataBase.updatePendingContacts(pendingInstructorAdapter);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
