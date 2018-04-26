package android.wasla.support.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        databaseReference.child(context.getString(android.wasla.support.R.string.contacts_node)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Instructor> instructors = new HashMap<String,Instructor>();
                Instructor instructor;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    instructor = child.getValue(Instructor.class);
                    instructors.put(instructor.getEmail().toLowerCase(), instructor);
                }
                if (instructors != null) {
                    availableContacts.clear();
                    availableContacts.addAll(instructors.values());
                    Log.i("size", instructors.values().size()+"");
                    //sort contacts
                    Collections.sort(availableContacts, new Comparator<Instructor>() {
                        @Override
                        public int compare(Instructor o1, Instructor o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
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
            databaseReference.child(context.getString(android.wasla.support.R.string.feedback_node)).push().setValue(feedback).addOnFailureListener(new OnFailureListener() {
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
