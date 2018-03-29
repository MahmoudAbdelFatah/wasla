package com.example.wasla.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.example.wasla.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import es.dmoral.toasty.Toasty;

/**
 * Created by amr on 27/10/17.
 */

public class OnlineDataBase {
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static DatabaseReference databaseReference = firebaseDatabase.getReference();
    public List<Instructor> availableContacts = new ArrayList<>();
    private Context context;

    public OnlineDataBase(Context context) {
        this.context = context;
    }

    public void updateAvailableContacts(final ValueCallback<List<Instructor>> valueCallback) {
        databaseReference.child(context.getString(R.string.contacts_node)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinkedHashSet<Instructor> instructors = new LinkedHashSet<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    instructors.add(child.getValue(Instructor.class));
                }
                if (instructors != null) {
                    availableContacts.clear();
                    availableContacts.addAll(instructors);
                    valueCallback.onReceiveValue(availableContacts);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void sendFeedback(String feedback, final ValueCallback<Boolean> b) {
        if (!isNetworkConnected())
            Toasty.warning(context, "Not Added, Check the internet connection!", Toast.LENGTH_LONG, true).show();
        else {
            databaseReference.child(context.getString(R.string.feedback_node)).push().setValue(feedback).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    b.onReceiveValue(false);
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    b.onReceiveValue(true);
                }

            });
        }
    }

}
