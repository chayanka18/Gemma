package com.example.gem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gem.Model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity
{
private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
private Button confirmOrderBtn;
private String totalAmount="";
//private String pprice="";
    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // pprice = getIntent().getStringExtra("pprice");

        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this,"Total Price :" + totalAmount,Toast.LENGTH_SHORT).show();
        confirmOrderBtn=findViewById(R.id.confirm_final_order_btn);
        nameEditText=findViewById(R.id.shippment_name);
        phoneEditText=findViewById(R.id.shippment_phone_number);
        addressEditText=findViewById(R.id.shippment_address);
        cityEditText=findViewById(R.id.shippment_city);


        confirmOrderBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Check();
            }
        });
    }


    private void Check()
    {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this,"Please Provide your Full Name",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this,"Please Provide your Phone number",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this,"Please Provide your Address",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this,"Please Provide your City name",Toast.LENGTH_LONG).show();
        }
        else if(phoneEditText.getText().toString().length()<10)
        {
            Toast.makeText(this,"Phone number must contain 10 digits",Toast.LENGTH_LONG).show();
        }
        else
        {
            ConfirmOrder(currentUserId);
        }
    }

    private void ConfirmOrder(String currentUserId)
    {
        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child("User View")
                .child("Users")
                .child(currentUserId);

        final HashMap<String, Object> ordersMap = new HashMap<>();
       // int p = Integer.parseInt();
       // int p = pprice != null ? Integer.parseInt(pprice.toString()) : 0;
        //int p = Integer.parseInt(numericPrice);
       // ordersMap.put("pprice",pprice);
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("pname", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        //ordersMap.put("state","Not Shipped");
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
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                   String currentUserId="";
                                    currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
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
                                    Toast.makeText(ConfirmFinalOrderActivity.this, "Thank You for purchasing,Details collected ", Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                    getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    onBackPressed();
                                }


                            }
                        });
            }

        });


    }
}