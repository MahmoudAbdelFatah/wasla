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

import android.webkit.ValueCallback;
import android.widget.EditText;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wasla.R;
import com.example.wasla.adapter.InstructorAdapter;
import com.example.wasla.model.Instructor;
import com.example.wasla.model.OnlineDataBase;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InstructorAdapter instructorAdapter;
    private OnlineDataBase onlineDataBase;
    private Toolbar toolbar;
    private ArrayList<Instructor> instructorsList;
    private final int AddFeedbackDialogRequestCoder=1;
    private ProgressBar progressBar;
    private android.support.design.widget.FloatingActionButton floatingActionButton;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

      if (requestCode==AddFeedbackDialogRequestCoder)
        {
            if (resultCode == RESULT_OK) {
                String feedback = data.getStringExtra(getString(R.string.feedback));
                onlineDataBase.sendFeedback(feedback, new ValueCallback<Boolean>() {
                    @Override
                    public void onReceiveValue(Boolean aBoolean) {
                        if(aBoolean)
                            Toasty.success(getBaseContext(), "Successfully send the feedback!"
                                    , Toast.LENGTH_SHORT, true).show();
                        else
                            Toasty.error(getBaseContext(), "There is an error,try again later!", Toast.LENGTH_SHORT, true).show();

                    }
                });

            }



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacts);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar=findViewById(R.id.progressBar);
        floatingActionButton=findViewById(R.id.bt_fab);

        setSupportActionBar(toolbar);
        if(!isNetworkConnected()) {
            Toasty.warning(this, "check the internet connection!", Toast.LENGTH_LONG, true).show();
        }
        onlineDataBase = new OnlineDataBase(this);
        instructorsList=new ArrayList<>();
        recyclerView = findViewById(R.id.rv_instructor);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    //    registerForContextMenu(recyclerView);
        progressBar.setVisibility(View.VISIBLE);

        onlineDataBase.updateAvailableContacts(new ValueCallback<List<Instructor>>() {
            @Override
            public void onReceiveValue(List<Instructor> instructors) {
                instructorsList= (ArrayList<Instructor>) instructors;
                Instructor instructor;
                for(int i=0;i<100;i++)
                {
                    instructor=new Instructor();
                    instructor.setName("Mahmoud Gamal");
                    instructor.setEmail("mahmoud.gamal2791996@yahoo.com");
                    instructor.setGender(getApplicationContext().getString(R.string.male_instructor));
                    instructorsList.add(instructor);
                }

                instructorAdapter = new InstructorAdapter(getApplicationContext(),instructorsList);
                recyclerView.setAdapter(instructorAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), AddFeedbackDialog.class),AddFeedbackDialogRequestCoder);
            }
        });

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



}
