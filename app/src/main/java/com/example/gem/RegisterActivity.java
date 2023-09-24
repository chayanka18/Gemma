package com.example.gem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gem.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity
{
    TextView alreadyHaveAccount;
   public EditText inputEmail,inputPassword,inputCConformPassword;
    public EditText inputUser;
    Button btnRegister;

    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{6}$";



    ProgressDialog progressDialog;

    CheckBox showpassword;
FirebaseDatabase firebaseDatabase;

DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        inputUser = findViewById(R.id.inputUser);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputCConformPassword = findViewById(R.id.inputCConformPassword);
        btnRegister = findViewById(R.id.btnRegister);


        showpassword = findViewById(R.id.showpassword);


        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    inputCConformPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    inputCConformPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();

                }


        });


    }

    private void PerformAuth(){
        String user = inputUser.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String conformPassword = inputCConformPassword.getText().toString();
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{6,}$";

        if (user.length() == 0 || email.length() == 0 || password.length() == 0 || conformPassword.length() == 0) /*conformPassword.length()==0*/ {
            Toast.makeText(getApplicationContext(), "Please Enter Data in All Fields", Toast.LENGTH_LONG).show();
        } else if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter Correct Email");
        }
       else if (!password.equals(conformPassword)) {
            inputCConformPassword.setError("Password entered does not matched for both field");
        } else if (password.isEmpty()) {
            inputPassword.setError("Password cannot be empty");
        } else if (password.length() < 6) {
            inputPassword.setError("Password must have atleast 6 character");
        } else if (!password.matches(passwordPattern)) {
            inputPassword.setError("Password must contain at least one lowercase letter, one uppercase letter, one special character (@$!%*?&), and be at least 6 characters long");
        } else {

            progressDialog.setMessage("Registration......");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        storeUserInfoInFirestore(user, email, password,conformPassword);
                        sendUsertoNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registration Successfull", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(RegisterActivity.this, "Account Already Exists", Toast.LENGTH_LONG).show();
                    }
                }
            });/*

            String getName = inputUser.getText().toString();
            String getEmail = inputEmail.getText().toString();
            String getPassword = inputPassword.getText().toString();
            String getConfirmPassword = inputCConformPassword.getText().toString();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("inputUser", getName);
            hashMap.put("inputEmail", getEmail);
            hashMap.put("inputPassword", getPassword);
            hashMap.put("inputCConformPassword", getConfirmPassword);
            databaseReference.child("Users")
                    .child(getName)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Toast.makeText(RegisterActivity.this, "Data Added Succesffully", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }*/
        }
    }

    private void storeUserInfoInFirestore(String user, String email, String password,String cpassword) {
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DocumentReference userDocument = firestoreDB.collection("Users").document(userId);

            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("username", user);
            userMap.put("email", email);
            userMap.put("password", password);
            userMap.put("cpassword", cpassword);

            userDocument.set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Successfully stored user information in Firestore
                            //Toast.makeText(RegisterActivity.this, "Data Added Succesffully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to store user information in Firestore
                            // Handle the failure appropriately
                            Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void sendUsertoNextActivity() {
        Intent intent=new Intent(RegisterActivity.this,onboarding.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
