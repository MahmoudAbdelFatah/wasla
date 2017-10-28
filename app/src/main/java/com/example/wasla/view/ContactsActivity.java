package com.example.wasla.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.wasla.R;
import com.example.wasla.adapter.InstructorAdapter;
import com.example.wasla.model.Instructor;
import com.example.wasla.model.OnlineDataBase;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;
public class ContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InstructorAdapter instructorAdapter;
    private List<Instructor> instructors;
    private final int AddContactDialogRequestCoder=1;
    private OnlineDataBase onlineDataBase;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AddContactDialogRequestCoder) {
            if(resultCode == RESULT_OK){
                String name=  data.getStringExtra("name");
                String email=  data.getStringExtra("email");
              //  Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
             //   Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
                Instructor instructor=new Instructor();
                instructor.setName(name);
                instructor.setEmail(email);
                instructors.add(instructor);
                onlineDataBase.setAvailableContacts(instructors);
                //TODO: Amr add/remove only a specific record to the database
                instructorAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Successfully add the contact", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        final FloatingActionMenu floatingActionMenu =(FloatingActionMenu)findViewById(R.id.fab);
        final FloatingActionButton floatingActionButton= (FloatingActionButton)findViewById(R.id.addContact_item);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Intent intent=new Intent(getApplicationContext(),AddContactDialog.class);
                startActivityForResult(intent,AddContactDialogRequestCoder);
            }
        });

        onlineDataBase=new OnlineDataBase();

        instructors = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_instructor);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        instructorAdapter = new InstructorAdapter(this , instructors);
        recyclerView.setAdapter(instructorAdapter);

        //test set available contacts

        Instructor i1=new Instructor();
        i1.setEmail("amr@yahoo");
        i1.setName("amr");

        Instructor i2=new Instructor();
        i2.setEmail("mohamed@yahoo");
        i2.setName("mohamed");

        instructors.add(i1);
        instructors.add(i2);
        instructorAdapter.notifyDataSetChanged();
        //onlineDataBase.setAvailableContacts(l);

        /*
        //test get available contacts async
        OnlineDataBase onlineDataBase=new OnlineDataBase();
        onlineDataBase.getAvailableContacts();
        */

    }


}
