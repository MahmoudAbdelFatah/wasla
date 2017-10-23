package com.example.wasla.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.example.wasla.R;
import com.example.wasla.adapter.InstructorAdapter;
import com.example.wasla.model.Instructor;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InstructorAdapter instructorAdapter;
    private List<Instructor> instructors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        instructors = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_instructor);
        recyclerView.setLayoutManager(new GridLayoutManager(this,numberOfColumns()));
        instructorAdapter = new InstructorAdapter(this , instructors);
        recyclerView.setAdapter(instructorAdapter);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2  ;
        return nColumns;
    }
}
