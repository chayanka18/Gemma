package com.example.gem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivityCategory extends AppCompatActivity {
private ImageView sets,rings,chains,bangles,special,trending;
private ImageView handmade,bracelets,earrings,nosepin;
private ImageView wedding,kids,boho,oxy;
private Button LogoutBtn,CheckOrderButton,ViewAll;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ViewAll=findViewById(R.id.view_products);
        CheckOrderButton = findViewById(R.id.check_orders_btn);
        LogoutBtn = findViewById(R.id.admin_logout);
        sets = findViewById(R.id.set);
        rings = findViewById(R.id.ring);
        chains = findViewById(R.id.chain);
        bangles = findViewById(R.id.bangle);
        handmade = findViewById(R.id.handmade);
        bracelets = findViewById(R.id.bracelet);
        earrings = findViewById(R.id.earring);
        nosepin = findViewById(R.id.nosepin);
        special=findViewById(R.id.special);
        trending=findViewById(R.id.trending);
        wedding=findViewById(R.id.wedding);
        oxy=findViewById(R.id.oxy);
        boho=findViewById(R.id.boho);
        kids=findViewById(R.id.kids);


        ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminActivityCategory.this, All.class);
                startActivity(intent);
            }
        });
        sets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "sets");
                startActivity(intent);
            }
        });
        rings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "rings");
                startActivity(intent);
            }
        });
        chains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "chains");
                startActivity(intent);
            }
        });

        bangles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "bangles");
                startActivity(intent);
            }
        });
        handmade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "handmade");
                startActivity(intent);
            }
        });
        bracelets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "bracelets");
                startActivity(intent);
            }
        });
        earrings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "earrings");
                startActivity(intent);
            }
        });
        nosepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "nosepin");
                startActivity(intent);
            }
        });
        special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "special");
                startActivity(intent);
            }
        });
        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "trending");
                startActivity(intent);
            }
        });
        wedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "wedding");
                startActivity(intent);
            }
        });
        kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "kids");
                startActivity(intent);
            }
        });
        oxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "oxy");
                startActivity(intent);
            }
        });
        boho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityCategory.this, AdminAddNewProduct.class);
                intent.putExtra("Category", "boho");
                startActivity(intent);
            }
        });

        CheckOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminActivityCategory.this, AdminNewOrderActivity.class);
                intent.putExtra("Category", "kids");
                startActivity(intent);

            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AdminActivityCategory.this);
                alert.setTitle("Logout");
                alert.setIcon(R.drawable.baseline_warning_24);
                alert.setMessage("Are you sure?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                final FirebaseAuth mAuth;
                                mAuth = FirebaseAuth.getInstance();
                                mAuth.signOut();
                                sendUsertoNextActivity();
                                Intent i = new Intent(AdminActivityCategory.this, MainActivity.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(AdminActivityCategory.this, "You have been Logged Out", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", null);

                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });
    }
        private void sendUsertoNextActivity() {
            Intent intent = new Intent(AdminActivityCategory.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


}

