package com.example.wasla.model;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.wasla.adapter.InstructorAdapter;
import com.example.wasla.view.ContactsActivity;
import com.google.firebase.database.ChildEventListener;
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
        public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        public static DatabaseReference databaseReference = firebaseDatabase.getReference();
        public final static List<Instructor> availableContacts=new ArrayList<>() ;
        public final static List<Instructor> pendingContacts=new ArrayList<>() ;

        public OnlineDataBase()
        {
            //   availableContacts=new ArrayList<>() ;
            //   pendingContacts=new ArrayList<>() ;
            Log.d("test","entered constractor");
        }
        public void setAvailableContacts(List<Instructor> l)
        {
            //availableContacts=l;
            availableContacts.clear();
            availableContacts.addAll(l);
            databaseReference.child("availableContacts").setValue(availableContacts);
        }
        public void updateAvailableContacts(  final InstructorAdapter instructorAdapter)
        {
            databaseReference.child("availableContacts").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<Instructor>> type=new GenericTypeIndicator<List<Instructor>>(){};
                //    availableContacts= dataSnapshot.getValue(type);

                    availableContacts.clear();
                    availableContacts.addAll(dataSnapshot.getValue(type));
                  //  instructors.clear();
                   // instructors.addAll(dataSnapshot.getValue(type));

                    instructorAdapter.notifyDataSetChanged();
                    // Log.d("test",availableContacts.get(1).getName()); //for testing
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        public void setPendingContacts(List<Instructor> l)
        {
          //  pendingContacts=l;
            pendingContacts.clear();
            pendingContacts.addAll(l);
            databaseReference.child("pendingContacts").setValue(pendingContacts);
        }


    public void updatePendingContacts( final InstructorAdapter instructorAdapter)
    {
        databaseReference.child("pendingContacts").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Instructor>> type=new GenericTypeIndicator<List<Instructor>>(){};
            //    pendingContacts=dataSnapshot.getValue(type);

                pendingContacts.clear();
                pendingContacts.addAll(dataSnapshot.getValue(type));
                //  instructors.clear();
                // instructors.addAll(dataSnapshot.getValue(type));

                instructorAdapter.notifyDataSetChanged();
                // Log.d("test",availableContacts.get(1).getName()); //for testing
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

        private ChildEventListener childEventListenerAvailableContacts = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        public void addAvailableContactsListener() {
            databaseReference.child("availableContacts").addChildEventListener(childEventListenerAvailableContacts);
        }
        public void detachAvailableContactsListener()
        {
            databaseReference.child("availableContacts").removeEventListener(childEventListenerAvailableContacts);
        }



        private ChildEventListener childEventListenerPendingContacts = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        public void addPendingContactsListener() {
            databaseReference.child("pendingContacts").addChildEventListener(childEventListenerPendingContacts);
        }
        public void detachPendingContactsListener()
        {
            databaseReference.child("pendingContacts").removeEventListener(childEventListenerPendingContacts);
        }
        /*
        // will break the consistency of the database ... update the hole list instead
            public void deleteAvailableContact(int index)
            {
                databaseReference.child("availableContacts").child(String.valueOf(index)).setValue(null);
            }*/
        public void addAvailableContact(int index,Object value)
        {
            databaseReference.child("availableContacts").child(String.valueOf(index)).setValue(value);
        }
        /*
    // will break the consistency of the database ... update the hole list instead
        public void deletePendingContact(int index)
        {
            databaseReference.child("pendingContacts").child(String.valueOf(index)).setValue(null);
        }*/
        public void addPendingContact(int index,Object value)
        {
            databaseReference.child("pendingContacts").child(String.valueOf(index)).setValue(value);
        }
    }
