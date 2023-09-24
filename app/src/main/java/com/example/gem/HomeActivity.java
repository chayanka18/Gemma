package com.example.gem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
//import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    ActionBarDrawerToggle drawerToggle;
    FirebaseUser mAuth;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


//to hide the actionBar
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.show();*/
//to change the actionBar Color
        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff00FFED));
        TextView search = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, SearchProduct.class));
              //  Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView ring = findViewById(R.id.ring);

        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Rings.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView bangle = findViewById(R.id.bangle);
        // img.setClickable(true);
        // img.bringToFront();
        bangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Bangles.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView earring = findViewById(R.id.earring);
        // img.setClickable(true);
        // img.bringToFront();
        earring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Earrings.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView chain = findViewById(R.id.chain);
        // img.setClickable(true);
        // img.bringToFront();
        chain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Chains.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView set = findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Set.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView bracelet = findViewById(R.id.bracelet);
        bracelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Bracelet.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView handmade = findViewById(R.id.handmade);
        handmade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Handmade.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView imgban2 = findViewById(R.id.imageban1);
        imgban2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Trending.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView nose = findViewById(R.id.nosepin);
        nose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, NosePin.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        ImageView hand = findViewById(R.id.imageView1);
        hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Handmade.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        ImageView wedding = findViewById(R.id.wedding);
        wedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Bridal.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        ImageView ozy = findViewById(R.id.ozy);
        ozy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Oxidized.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView kids = findViewById(R.id.kids);
        kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Kids.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        ImageView boho = findViewById(R.id.boho);
        boho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Boho.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });


        ImageView imageView = findViewById(R.id.imgviewban);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Chains.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        ImageView image = findViewById(R.id.imageban2);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Chains.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        ImageView image1 = findViewById(R.id.imageView);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                startActivity(new Intent(HomeActivity.this, Bridal.class));
                Toast.makeText(HomeActivity.this, "Happy Purchase", Toast.LENGTH_SHORT).show();
                // drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
            }
        });


        // View logout = findViewById(R.id.logout);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setClickable(true);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.nav_bottom);
        //to close and open menu
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();
        drawerLayout.bringToFront();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.person: {
                        Intent i = new Intent(HomeActivity.this,UserAccount.class);
                        startActivity(i);
                        break;
                    }
                    case R.id.cart: {
                        Intent i = new Intent(HomeActivity.this, CartActivity.class);
                        startActivity(i);
                        finish();
                        onBackPressed();
                        break;
                    }
                    case R.id.wishlist: {
                        Intent i = new Intent(HomeActivity.this, WishList.class);
                        startActivity(i);
                        break;
                    }



                }
                return false;
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        onBackPressed();
                        break;
                    }
                    case R.id.about: {
                        Intent i = new Intent(HomeActivity.this, AboutUs.class);
                        startActivity(i);
                        onBackPressed();

                        //Toast.makeText(HomeActivity.this, "About Us Selected", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case R.id.rateus: {
                        Intent i = new Intent(HomeActivity.this, RateUs.class);
                        startActivity(i);
                        onBackPressed();
                        break;
                    }


                    case R.id.logout: {
                        //noinspection SwitchStatementWithoutDefaultBranch
                        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                        alert.setTitle("Logout");
                        alert.setIcon(R.drawable.baseline_warning_24);
                        alert.setMessage("Are you sure?")
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        final FirebaseAuth mAuth;
                                        mAuth = FirebaseAuth.getInstance();
                                        mAuth.signOut();
                                        Intent i = new Intent(HomeActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                        Toast.makeText(HomeActivity.this, "You have Been Logged Out", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("Cancel", null);

                        AlertDialog alert1 = alert.create();
                        alert1.show();

                        break;

                    }
                    case R.id.help: {
                        Intent i = new Intent(HomeActivity.this, faqActivity.class);
                        startActivity(i);
                        onBackPressed();
                        break;
                    }
                    case R.id.search: {
                        Intent i = new Intent(HomeActivity.this, SearchProduct.class);
                        startActivity(i);
                        onBackPressed();
                        break;
                    }
                    case R.id.shopping: {
                        Intent i = new Intent(HomeActivity.this, UserRecentSum.class);
                        startActivity(i);
                        onBackPressed();
                        break;
                    }

                }
                return false;
            }
        });


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}



