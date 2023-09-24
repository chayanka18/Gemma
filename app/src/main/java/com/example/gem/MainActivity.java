package com.example.gem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextView createNewAccount;
    EditText inputEmail, inputPassword;
    Button btnLogin;
    TextView forgotPass;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    CheckBox showpassword;

FirebaseDatabase database;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private boolean isLoginInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        createNewAccount = findViewById(R.id.createNewAccount);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        forgotPass = findViewById(R.id.forgotPassword);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        //showpassword
        showpassword = findViewById(R.id.showpassword1);


        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformLogin();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });
    }

    ProgressDialog loadingBar;

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);

        // write the email using which you registered
        emailet.setHint("Email");
        emailet.setMinEms(17);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailet.getText().toString().trim();
                if(email.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Field cannot be empty,Provide Email", Toast.LENGTH_SHORT).show();
                }
                else {
                    beginRecovery(email);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {
        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if (task.isSuccessful()) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(MainActivity.this, "Message has been sent ,Please check you mails", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error,Verification mail cannot be sent", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                //Toast.makeText(MainActivity.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
private  int loginAttempts = 0;
    private void PerformLogin() {

       
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{6,}$";
        if(email.length()==0||password.length()==0)
        {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (!email.matches(emailPattern))
        {
            inputEmail.setError("Enter Correct Email");

        }
        else if(email.matches("chayanka@gmail.com")&&password.matches("123456"))
        {
           startActivity(new Intent(MainActivity.this,AdminActivityCategory.class));

        }else if (password.length() < 6) {
            inputPassword.setError("Enter Proper Password.Maximum of 6 characters are must");
        } else if (!password.matches(passwordPattern)) {
            inputPassword.setError("Password must contain at least one lowercase letter, one uppercase letter, one special character (@$!%*?&), and be at least 6 characters long");
        }
            else {
            progressDialog.setMessage("Please wait till it loads");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUsertoNextActivity();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    } else {
                        /*progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Account with this email not found ", Toast.LENGTH_LONG).show();*/
                        loginAttempts++;
                        int remainingAttempts = 3 - loginAttempts;
                        if (remainingAttempts > 0) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Invalid credentials. " + remainingAttempts + " attempts remaining.", Toast.LENGTH_LONG).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Invalid credentials. Maximum login attempts reached.", Toast.LENGTH_LONG).show();
                            disableLoginFields();
                            androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("INVALID CREDENTIALS");
                            alert.setCancelable(false);
                            alert.setIcon(R.drawable.baseline_payment_24);
                            alert.setMessage("Change password and Try Again?")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                           showRecoverPasswordDialog();
                                        }
                                    });

                            androidx.appcompat.app.AlertDialog alert1 = alert.create();
                            alert1.show();

                        }
                        inputEmail.setEnabled(true);
                        inputPassword.setEnabled(true);
                        btnLogin.setEnabled(true);
                    }
                }
            });
        }
    }

    private void disableLoginFields()
    {
        inputEmail.setEnabled(false);
        inputPassword.setEnabled(false);
        btnLogin.setEnabled(false);
        Toast.makeText(MainActivity.this, "Please close the app and try again later.", Toast.LENGTH_LONG).show();

    }

    private void sendUsertoNextActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


@Override
protected void onStart() {
        super.onStart();
        if (mAuth.getUid() != null) {
            Toast.makeText(MainActivity.this, "Welcome Back", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
            //FirebaseAuth.getInstance().signOut();

        } else {
            Toast.makeText(MainActivity.this, "You can log In Now", Toast.LENGTH_SHORT).show();
        }

    }


}

