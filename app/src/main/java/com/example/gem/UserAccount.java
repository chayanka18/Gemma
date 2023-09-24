package com.example.gem;

import static com.example.gem.AdminAddNewProduct.GalleryPick;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAccount extends AppCompatActivity {
TextView username,email,logout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        username = findViewById(R.id.username);
        email = findViewById(R.id.Email);

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(UserAccount.this);
                alert.setTitle("Logout");
                alert.setIcon(R.drawable.baseline_warning_24);
                alert.setMessage("Are you sure?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                final FirebaseAuth mAuth;
                                mAuth = FirebaseAuth.getInstance();
                                mAuth.signOut();
                                Intent i = new Intent(UserAccount.this, MainActivity.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(UserAccount.this, "You have Been Logged Out", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", null);

                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });

        String userId = FirebaseAuth.getInstance().getUid(); // Replace with the actual user ID
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Document exists, retrieve the data
                            String emaily = documentSnapshot.getString("email");
                            String usernamey = documentSnapshot.getString("username");

                            // Do something with the retrieved email and username
                            // For example, set them in TextViews
                            email.setText(emaily);
                            username.setText(usernamey);
                        } else {
                            // Document does not exist
                            // Handle the case appropriately
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while retrieving the document
                        // Handle the failure appropriately
                    }
                });
    }

}