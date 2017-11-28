package com.example.wasla.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wasla.R;
import com.example.wasla.adapter.ArrayAdapterSearchView;
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
    private MenuItem searchAction;
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
        searchAction = (MenuItem) findViewById(R.id.action_search);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
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
        return true;
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
        else if(id == R.id.action_search) {
            handleMenuSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar
        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

            //add the search icon in the action bar
            searchAction.setIcon(getResources().getDrawable(R.drawable.ic_open_search));
            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title
            etSearch = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor
            //this is a listener to do a search when the user clicks on search button
            etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });

            etSearch.requestFocus();
            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
            //add the close icon
            searchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_search));
            isSearchOpened = true;
        }
    }

    private void doSearch() {
        //TODO>> Teha: the search doesn't work fine
        final ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) MenuItemCompat.getActionView(searchAction);

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }
}
