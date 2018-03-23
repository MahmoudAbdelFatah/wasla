package com.example.wasla.model;

import android.content.Context;
import android.util.Log;

import com.example.wasla.R;
import com.example.wasla.adapter.InstructorAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amr on 27/10/17.
 */

public class OnlineDataBase {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    public final static List<Instructor> availableContacts = new ArrayList<>();
    private Context context;

    public OnlineDataBase(Context context) {
        this.context = context;
        Log.d("test", "entered onlineDataBase constructor");
    }

    public void updateAvailableContacts(final InstructorAdapter instructorAdapter) {
        databaseReference.child(context.getString(R.string.contacts_node)).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Instructor> instructors = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    instructors.add(child.getValue(Instructor.class));
                }
                if (instructors != null) {
                    availableContacts.clear();
                    availableContacts.addAll(instructors);
                    instructorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendFeedback(String feedback) {
        databaseReference.child(context.getString(R.string.feedback_node)).push().setValue(feedback);
    }
}
