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
    private final int AddContactDialogRequestCoder = 1;
    private final int AddFeedbackDialogRequestCoder = 2;
    private OnlineDataBase onlineDataBase;
    private Toolbar toolbar;
    private boolean isSearchOpened = false;
    private EditText etSearch;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AddContactDialogRequestCoder) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String email = data.getStringExtra("email");
                String gender= data.getStringExtra("gender");
                Instructor instructor = new Instructor();
                instructor.setName(name);
                instructor.setEmail(email);
                instructor.setGender(gender);
                //admin privileges
                /*   if(onlineDataBase.addAvailableContact(OnlineDataBase.availableContacts.size(), instructor)) {
                     //   onlineDataBase.availableContacts.add(instructor);
                     instructorAdapter.notifyDataSetChanged();
                     Toasty.success(this, "Successfully add the contact!", Toast.LENGTH_SHORT, true).show();
                 }
                 else
                 {
                     Toasty.error(this, "This email is already in the contacts list!", Toast.LENGTH_SHORT, true).show();

                 }
                */
                // normal user privileges
             if(  onlineDataBase.addPendingContact(OnlineDataBase.pendingContacts.size(), instructor)) {
                 instructorAdapter.notifyDataSetChanged();
                 Toasty.success(this, "Your request added to the pending list until an admin confirm it,Thanks!", Toast.LENGTH_SHORT, true).show();
             }
             else
             {
                 Toasty.error(this, "This email is already in the pending list!", Toast.LENGTH_SHORT, true).show();

             }
            }
        }
        else if (requestCode==AddFeedbackDialogRequestCoder)
        {
            if (resultCode == RESULT_OK) {
                String feedback = data.getStringExtra("feedback");
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
        final FloatingActionMenu floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab);
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.addContact_item);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Intent intent = new Intent(getApplicationContext(), AddContactDialog.class);
                startActivityForResult(intent, AddContactDialogRequestCoder);
            }
        });
        if(!isNetworkConnected()) {
            Toasty.warning(this, "check the internet connection!", Toast.LENGTH_LONG, true).show();
        }
        onlineDataBase = new OnlineDataBase();
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.rv_instructor) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_list, menu);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.edit:
                // edit stuff here
                //TODO:: why this var >>x ?
                int x=instructorAdapter.getLastLongPress();
                onlineDataBase.deleteAvailableContact(instructorAdapter.getLastLongPress());
                instructorAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
        if(id == R.id.pending_list) {
            startActivity(new Intent(this, PendingContactsActivity.class));
            return true;
        }
        else if(id == R.id.send_feedback) {
            startActivityForResult(new Intent(this, AddFeedbackDialog.class),AddFeedbackDialogRequestCoder);
            return true;
        }
        else {
            //nothing
        }

        return super.onOptionsItemSelected(item);
    }



}
