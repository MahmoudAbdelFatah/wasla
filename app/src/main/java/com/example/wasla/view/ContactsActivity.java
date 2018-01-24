package com.example.wasla.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.wasla.R;
import com.example.wasla.adapter.InstructorAdapter;
import com.example.wasla.model.Instructor;
import com.example.wasla.model.OnlineDataBase;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import es.dmoral.toasty.Toasty;

public class ContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InstructorAdapter instructorAdapter;
    private OnlineDataBase onlineDataBase;
    private Toolbar toolbar;
    private final int AddFeedbackDialogRequestCoder=1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

      if (requestCode==AddFeedbackDialogRequestCoder)
        {
            if (resultCode == RESULT_OK) {
                String feedback = data.getStringExtra(getString(R.string.feedback));
                onlineDataBase.sendFeedback(feedback);
                Toasty.success(this, "Successfully send the feedback!", Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(!isNetworkConnected()) {
            Toasty.warning(this, "check the internet connection!", Toast.LENGTH_LONG, true).show();
        }
        onlineDataBase = new OnlineDataBase(this);
        recyclerView = findViewById(R.id.rv_instructor);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        instructorAdapter = new InstructorAdapter(this, onlineDataBase.availableContacts);
        recyclerView.setAdapter(instructorAdapter);
        registerForContextMenu(recyclerView);

        onlineDataBase.updateAvailableContacts(instructorAdapter);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                instructorAdapter.getFilter().filter(s);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       if(id == R.id.send_feedback) {

            startActivityForResult(new Intent(this, AddFeedbackDialog.class),AddFeedbackDialogRequestCoder);
            return true;
        }
        else {
            //nothing
        }

        return super.onOptionsItemSelected(item);
    }



}
