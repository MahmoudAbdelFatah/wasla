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
 * Created by MahmoudAbdelFatah on 23-Oct-17.
 */

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> {
    private List<Instructor> instructors;
    private Context context;

    public int getLastLongPress() {
        return lastLongPress;
    }

    private int lastLongPress;
    public InstructorAdapter(Context context, List<Instructor> instructors) {
        this.context = context;
        this.instructors = instructors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.rv_instructor_items, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final Instructor instructor = instructors.get(position);

        viewHolder.instructorName.setText(instructor.getName());
        viewHolder.instructorEmail.setText(instructor.getEmail());
        if(instructor.getGender().equals("male"))
        viewHolder.instructorPhoto.setImageResource(R.drawable.male);
        else if(instructor.getGender().equals("female"))
            viewHolder.instructorPhoto.setImageResource(R.drawable.female);
        Log.v("adapter", instructor.getEmail());

        viewHolder.imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, instructor.getName() + "\n" + instructor.getEmail());
                if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(shareIntent);
                }
            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{instructor.getEmail()});
                if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(emailIntent, "Send mail!"));
                }
            }
        });

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO: amr implementing checked email here!
                lastLongPress=position;
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return instructors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView instructorPhoto, imageShare;
        public TextView instructorName;
        public TextView instructorEmail;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_instructor_content);
            instructorPhoto = itemView.findViewById(R.id.iv_instructor_photo);
            imageShare = itemView.findViewById(R.id.iv_share);
            instructorName = itemView.findViewById(R.id.tv_instructor_name);
            instructorEmail = itemView.findViewById(R.id.tv_instructor_email);
        }
    }
}
