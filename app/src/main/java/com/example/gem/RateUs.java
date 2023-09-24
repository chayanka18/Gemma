package com.example.gem;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RateUs extends AppCompatActivity
{
    TextView rateCount,showRating;
    EditText review;
    Button submit;
    RatingBar ratingBar;
    float rateValue;
    String temp,show;

FirebaseFirestore db;
    FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        showRating=findViewById(R.id.showRating);
        rateCount=findViewById(R.id.ratingCount);
        ratingBar=findViewById(R.id.ratingBar);
        review=findViewById(R.id.review);
        submit=findViewById(R.id.submit);


        //Initialize firebase auth and firestore
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        final ScrollView scrollView = findViewById(R.id.scrollView);
        final View activityRootView = findViewById(R.id.activity_root_view);

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = activityRootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is visible
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.scrollTo(0, scrollView.getBottom());
                        }
                    });
                } else {
                    // Keyboard is hidden
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.scrollTo(0, 0);
                        }
                    });
                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                rateValue=ratingBar.getRating();
                if(rateValue<=1 && rateValue>0)
                {
                    rateCount.setText("Bad  "+rateValue+"/5");
                }
                else if(rateValue<=2 && rateValue>1)
                {
                    rateCount.setText("OK  "+rateValue+"/5");
                }
                else if(rateValue<=3 && rateValue>2)
                {
                    rateCount.setText("Good  "+rateValue+"/5");
                }
                else if(rateValue<=4 && rateValue>3)
                {
                    rateCount.setText("Very Good  "+rateValue+"/5");
                }
                else if(rateValue<=5 && rateValue>4)
                {
                    rateCount.setText("Best  "+rateValue+"/5");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //temp = rateCount.getText().toString();
                    // showRating.setText("Your Rating:\n"+temp+"\n"+review.getText());
                temp = rateCount.getText().toString();
                String userReview = review.getText().toString();

                // Check if rating bar is not clicked or no review is given
                if (temp.isEmpty() || userReview.isEmpty()) {
                    Toast.makeText(RateUs.this, "Please provide a rating and review before submitting.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method without updating the review
                }
                    review.setText("");
                    ratingBar.setRating(0);
                    rateCount.setText("");
                    show = showRating.getText().toString();
                    Map<String, Object> user = new HashMap<>();
                    user.put("showRating", show);
                user.put("userReview", userReview);
                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(RateUs.this, "Thank you for giving your Feedback", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RateUs.this, HomeActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RateUs.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            });
            }
        });
    }
}
