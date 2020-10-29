package common.reachit.examtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    EditText username, userphone, usermail;
    Button regbtn;
    CircleImageView profileImage;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    String name, phone, email;
    UserProfile userProfile;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    ImageView ivArrow;
    Uri saveUri;
    private final int PICK_IMAGE_REQUEST= 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ivArrow = (ImageView) findViewById(R.id.ivicon);

        profileImage = (CircleImageView) findViewById(R.id.ivImage);

        ivArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        username = (EditText) findViewById(R.id.etUserName);
        userphone = (EditText) findViewById(R.id.etPhone);
        usermail = (EditText) findViewById(R.id.etEmail);
        regbtn = (Button) findViewById(R.id.btnRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("users");


        progressDialog = new ProgressDialog(this);

        //Event for button
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
//                if (validate()) {
//                    //it register to database
//                    progressDialog.setMessage("Please Wait...");
//                    progressDialog.show();
//                    myRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                           /* if (dataSnapshot.child(id).exists()) {
//                                progressDialog.dismiss();
//                                //Toast.makeText(SignUp.this, "Phone number already Registered you may signin !!", Toast.LENGTH_LONG).show();
//                                //Intent intent = new Intent(SignUp.this, Register.class);
//                                //startActivity(intent);
//                                //finish();
//                            }*/
//                            //else {
//                            progressDialog.dismiss();
//                            UserProfile newUser = new UserProfile(username.getText().toString(), userphone.getText().toString(), usermail.getText().toString());
//                            myRef.child(id).setValue(newUser);
//                            Toast.makeText(SignUp.this, "Signup successfully !!", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(SignUp.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                            // }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
            }
        });
    }

    private void uploadImage() {
        if(saveUri != null && !username.getText().toString().equals("") && !usermail.getText().toString().equals("")&& !userphone.getText().toString().equals("")){
            final ProgressDialog progressDialog =new ProgressDialog(this);
            progressDialog.setMessage("Uploading");
            progressDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("category/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this,"Successfully data is Uploaded !!!",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    userProfile = new UserProfile(username.getText().toString(),userphone.getText().toString(),usermail.getText().toString(),uri.toString());
                                    myRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(userProfile);

                                    Intent intent = new Intent(SignUp.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+progress+"%");

                        }
                    });
        }
        else{
            Toast.makeText(SignUp.this,"Fields are empty !!",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null &&data.getData() !=null){
            saveUri = data.getData();

            Picasso.get().load(saveUri).noPlaceholder().centerCrop().fit()
                    .into(profileImage);
        }
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    private boolean validate() {

        Boolean result = false;

        name = username.getText().toString();
        phone = userphone.getText().toString();
        email = usermail.getText().toString();
        if (name.equals("") || phone.equals("") || email.equals("")) {
            //Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
            builder.setTitle("Oops!")
                    .setMessage("Fields are empty")
                    .setPositiveButton("ok", null).setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            result = true;
        }
        return result;

    }
}
