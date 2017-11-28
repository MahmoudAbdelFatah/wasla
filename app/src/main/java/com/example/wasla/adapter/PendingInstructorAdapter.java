package com.example.wasla.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wasla.R;
import com.example.wasla.model.Instructor;

import java.util.List;

/**
 * Created by amr on 28/11/17.
 */

public class PendingInstructorAdapter extends RecyclerView.Adapter<PendingInstructorAdapter.ViewHolder>{
    private List<Instructor> pendingInstructors;
    private Context context;

    public int getLastLongPress() {
        return lastLongPress;
    }

    private int lastLongPress;
    public PendingInstructorAdapter(Context context, List<Instructor> instructors) {
        this.context = context;
        this.pendingInstructors = instructors;
    }

    @Override
    public PendingInstructorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.cv_pending_instructor_item, parent, false);

        // Return a new holder instance
        PendingInstructorAdapter.ViewHolder viewHolder = new PendingInstructorAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PendingInstructorAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final Instructor instructor = pendingInstructors.get(position);

        viewHolder.instructorName.setText(instructor.getName());
        viewHolder.instructorEmail.setText(instructor.getEmail());
        if(instructor.getGender().equals("male"))
            viewHolder.instructorPhoto.setImageResource(R.drawable.male);
        else if(instructor.getGender().equals("female"))
            viewHolder.instructorPhoto.setImageResource(R.drawable.female);
        Log.v("adapter", instructor.getEmail());


    }

    @Override
    public int getItemCount() {
        return pendingInstructors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView instructorPhoto;
        public TextView instructorName;
        public TextView instructorEmail;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_instructor_content);
            instructorPhoto = itemView.findViewById(R.id.iv_instructor_photo);
            instructorName = itemView.findViewById(R.id.tv_instructor_name);
            instructorEmail = itemView.findViewById(R.id.tv_instructor_email);
        }
    }
}
