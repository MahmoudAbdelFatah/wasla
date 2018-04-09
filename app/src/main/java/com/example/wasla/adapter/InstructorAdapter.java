package com.example.wasla.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wasla.R;
import com.example.wasla.model.Instructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MahmoudAbdelFatah on 23-Oct-17.
 */

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> implements Filterable {
    private List<Instructor> instructors;
    private List<Instructor> instructorsFilteredList;
    private Context context;


    public InstructorAdapter(Context context, List<Instructor> instructors) {
        this.context = context;
        this.instructors = instructors;
        this.instructorsFilteredList = instructors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.cv_instructor_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final Instructor instructor = instructorsFilteredList.get(position);

        viewHolder.instructorName.setText(instructor.getName());
        viewHolder.instructorEmail.setText(instructor.getEmail());
        viewHolder.instructorName.setSelected(true);
        viewHolder.instructorEmail.setSelected(true);
        if (instructor.getGender().equals(context.getString(R.string.male_instructor)))
            viewHolder.instructorPhoto.setImageResource(R.drawable.male);
        else if (instructor.getGender().equals(context.getString(R.string.female_instructor)))
            viewHolder.instructorPhoto.setImageResource(R.drawable.female);
        Log.v("adapter", instructor.getEmail());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, instructor.getEmail());
                if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share with.."));
                }
            }
        });

        viewHolder.imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{instructor.getEmail()});
                if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(emailIntent, "Send mail.."));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return instructorsFilteredList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView instructorPhoto, imageSend;
        public TextView instructorName;
        public TextView instructorEmail;
        public CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            instructorPhoto = itemView.findViewById(R.id.iv_instructor_photo);
            //imageShare = itemView.findViewById(R.id.iv_share);
            imageSend = itemView.findViewById(R.id.iv_send_email);

            instructorName = itemView.findViewById(R.id.tv_instructor_name);
            instructorEmail = itemView.findViewById(R.id.tv_instructor_email);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    instructorsFilteredList = instructors;
                } else {
                    List<Instructor> filteredList = new ArrayList<>();
                    for (Instructor instructor : instructors) {
                        if (instructor.getName().toLowerCase().contains(charString) || instructor.getEmail().toLowerCase().contains(charString)) {
                            filteredList.add(instructor);
                        }
                    }
                    instructorsFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = instructorsFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                instructorsFilteredList = (List<Instructor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
