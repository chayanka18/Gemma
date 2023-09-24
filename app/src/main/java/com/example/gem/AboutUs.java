package com.example.gem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }
    public void Email(View view)
    {
        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:gemmajewelleries2@gmail.com")));
    }
    public void Call(View view)
    {
        startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:0820 2521807")));
    }
}