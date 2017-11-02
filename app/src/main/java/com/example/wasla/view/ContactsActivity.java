package com.example.wasla.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Toast;

import com.example.wasla.R;
import com.example.wasla.adapter.InstructorAdapter;
import com.example.wasla.model.Instructor;
import com.example.wasla.model.OnlineDataBase;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class ContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InstructorAdapter instructorAdapter;
    // private List<Instructor> instructors;
    private final int AddContactDialogRequestCoder = 1;
    private OnlineDataBase onlineDataBase;

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

                onlineDataBase.addAvailableContact(onlineDataBase.availableContacts.size(), instructor);
                onlineDataBase.availableContacts.add(instructor);
                instructorAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Successfully add the contact", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
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

        onlineDataBase = new OnlineDataBase();

        //instructors = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_instructor);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        instructorAdapter = new InstructorAdapter(this, onlineDataBase.availableContacts);
        recyclerView.setAdapter(instructorAdapter);
        registerForContextMenu(recyclerView);

        onlineDataBase.updateAvailableContacts(instructorAdapter);

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
}
