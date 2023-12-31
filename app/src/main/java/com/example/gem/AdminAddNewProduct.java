package com.example.gem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProduct extends AppCompatActivity {
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName,InputProductDescription,InputProductPrice;
    static final int GalleryPick=1;
    private Uri ImageUri;

private ProgressDialog loadingBar;
    private DatabaseReference ProductRef;
    private StorageReference ProductImageRef;
private String CategoryName,Description,Price,Pname,saveCurrentDate,saveCurrentTime,ProductRandomKey,downloadImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName=getIntent().getExtras().get("Category").toString();


        ProductImageRef= FirebaseStorage.getInstance().getReference().child("Product Images");

        ProductRef=FirebaseDatabase.getInstance().getReference().child("Products");
AddNewProductButton=(Button)findViewById(R.id.add_new_product);
        InputProductImage=(ImageView) findViewById(R.id.select_product_image);
        InputProductName=(EditText) findViewById(R.id.product_name);
        InputProductDescription=(EditText) findViewById(R.id.product_description);
        InputProductPrice=(EditText) findViewById(R.id.product_price);
        loadingBar=new ProgressDialog(this);



        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }


        });
        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });


        }

    private void OpenGallery() 
    {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }
    
    private void ValidateProductData()
    {
        Description=InputProductDescription.getText().toString();
        Price=InputProductPrice.getText().toString();
        Pname=InputProductName.getText().toString();
        
        if(ImageUri==null)
        {
            Toast.makeText(this,"Product Image is Mandatory.....",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Please write Product Description",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this,"Please write Product Description",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this,"Please write Product Description",Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();
        }
        
    }

    private void StoreProductInformation() 
    {

        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please wait, while we are adding the new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        ProductRandomKey=saveCurrentDate+saveCurrentTime;

        StorageReference filePath=ProductImageRef.child(ImageUri.getLastPathSegment() + ProductRandomKey +".jpg");

        final UploadTask uploadTask=filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message=e.toString();
                Toast.makeText(AdminAddNewProduct.this, "Error"+message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddNewProduct.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {

                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(AdminAddNewProduct.this, "Getting Product ImageUrl Successfully", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();

                        }
                    }
                });
            }
        });


    }

    private void saveProductInfoToDatabase()
    {

        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",ProductRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",Price);
        productMap.put("pname",Pname);

        ProductRef.child(ProductRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent i=new Intent(AdminAddNewProduct.this,AdminActivityCategory.class);
                            startActivity(i);
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProduct.this, " Product Added Successfully", Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message=task.getException().toString();
                            Toast.makeText(AdminAddNewProduct.this, "Error "+message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}