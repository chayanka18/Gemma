package com.example.gem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RazorPay extends AppCompatActivity implements PaymentResultListener {
    //Initialize variable
    Button btPay;
    TextView fprice;
    //int famount=0;
    String currentUserId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //int price = Integer.parseInt(getIntent().getStringExtra("Total Price"));
        int price = getIntent().getIntExtra("amount", 0); // 0 is the default value if the extra is not found

        //Initialize amount
        //String sAmount="100";
        int f= Integer.parseInt(String.valueOf(price));
        //Convert and round off
        int amount=Math.round(Float.parseFloat(String.valueOf(f)) * 100);
        //String totalAmount = getIntent().getStringExtra("Total Price");

                //Initialize razorpay checkout
                Checkout checkout=new Checkout();
                //Set key id

                checkout.setKeyID("rzp_test_eALrTvDcdQymoo");

                checkout.setImage(R.drawable.razor);

                JSONObject object=new JSONObject();
                try {
                    object.put("name","Gemma");
                    object.put("description","Test Payment");
                    object.put("theme.color","#0093DD");
                    object.put("currency","INR");
                    object.put("amount",amount);
                    object.put("prefill.contact","9876543210");
                    object.put("prefill.email","Gemma@rzp.com");
                    checkout.open(RazorPay.this,object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


    @Override
    public void onPaymentSuccess(String s) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Set title
        builder.setTitle("Payment ID");
        builder.setPositiveButton("Ok",(dialog,which)->{
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();

                });
        //Set message
        builder.setMessage(s);
        //show alert dialog
        builder.show();
      /*  final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child("User View")
                .child("Users")
                .child(currentUserId);
        final HashMap<String, Object> ordersMap = new HashMap<>();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String finalCurrentUserId = currentUserId;
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference().child("Cart List")
                        .child("User View")
                        .child("Users")
                        .child(finalCurrentUserId)
                        .child("Products")
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    String currentUserId = "";
                                    currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                                            .child("Orders");
                                    ordersRef.child("Admin View")
                                            .child("Users")
                                            .child(currentUserId)
                                            .updateChildren(ordersMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                    Toast.makeText(RazorPay.this, "Thank You for purchasing,Details collected ", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(RazorPay.this, HomeActivity.class);
                                    getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }

                            }
                        });
            }
        });*/
       /* currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String finalCurrentUserId = currentUserId;
        FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View")
                .child("Users")
                .child(finalCurrentUserId)
                .child("Products")
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });*/
    }

    @Override
    public void onPaymentError(int i, String s) {
        //Display toast
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}