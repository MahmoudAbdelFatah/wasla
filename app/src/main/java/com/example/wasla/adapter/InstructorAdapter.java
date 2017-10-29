package com.example.wasla.adapter;

import android.content.Context;
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

    public InstructorAdapter(Context context, List<Instructor> instructors){
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
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Instructor instructor =  instructors.get(position);

        viewHolder.instructorName.setText(instructor.getName());
        viewHolder.instructorEmail.setText(instructor.getEmail());
        Log.v("adapter", instructor.getEmail());

        viewHolder.imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: implementing the share here!
            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: implementing send email here!
            }
        });

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO: implementing checked email here!
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
            instructorPhoto=itemView.findViewById(R.id.iv_instructor_photo);
            imageShare = itemView.findViewById(R.id.iv_share);
            instructorName =  itemView.findViewById(R.id.tv_instructor_name);
            instructorEmail =  itemView.findViewById(R.id.tv_instructor_email);
        }
    }
}
