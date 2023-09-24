package com.example.gem.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;


    import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gem.Model.Review;
import com.example.gem.R;

import java.util.List;

    public class ReviewAdapter extends ArrayAdapter<Review> {

        public ReviewAdapter(Context context, List<Review> reviewList) {
            super(context, 0, reviewList);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
            }

            TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
            TextView reviewTextView = convertView.findViewById(R.id.reviewTextView);
            RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);

            Review review = getItem(position);
            if (review != null) {
                usernameTextView.setText(review.getUsername());
                reviewTextView.setText(review.getReview());
                ratingBar.setRating(review.getRating());
            }

            return convertView;
        }
    }

