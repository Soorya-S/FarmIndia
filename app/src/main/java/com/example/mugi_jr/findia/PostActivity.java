package com.example.mugi_jr.findia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageButton nselectImage;
    private EditText npost;
    private Button nsubmit;
    private Uri imageUri=null;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference nstorage;
    private DatabaseReference ndatabase,ndatabase1;
    private ProgressDialog nprogress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentuser;
    private DatabaseReference mdatabaseusers;
    private String name,type,district,id,mobile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mAuth = FirebaseAuth.getInstance();
        mCurrentuser = mAuth.getCurrentUser();
        mdatabaseusers = FirebaseDatabase.getInstance().getReference().child("users");
        nselectImage = (ImageButton) findViewById(R.id.imageButton);
        nprogress = new ProgressDialog(this);

        npost = (EditText)findViewById(R.id.title);

        nsubmit = (Button) findViewById(R.id.post);
        nstorage = FirebaseStorage.getInstance().getReference();
       ndatabase1 = FirebaseDatabase.getInstance().getReference().child("Blog");
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("signin_credentials").child(mCurrentuser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                district=String.valueOf(dataSnapshot.child("district").getValue());
                type=String.valueOf(dataSnapshot.child("type").getValue());
                name=String.valueOf(dataSnapshot.child("name").getValue());
                mobile=String.valueOf(dataSnapshot.child("mobile").getValue());
                Log.e("district",district);
                Log.e("Type",type);
                Log.e("name",name);
                ndatabase = FirebaseDatabase.getInstance().getReference().child("Blog1").child(district);
                mdatabaseusers.child(district).child(type).child(name).child("Postimage").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        id=String.valueOf(dataSnapshot.getValue());
                        Log.e("id",id);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        nselectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        nsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startposting();
            }
        });
    }

    private void startposting() {
        nprogress.setMessage("posting..");

        final String title_val = npost.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val)  && imageUri != null )
        {
            nprogress.show();
            StorageReference filepath = nstorage.child("CropImages").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downUrloadurl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newpost = ndatabase.push();
                    final DatabaseReference newpost1 = ndatabase1.push();


                    //   newpost.child("Username").setValue(dataSnapshot.child("name").getValue());



                    mdatabaseusers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newpost.child("title").setValue(title_val);

                            newpost.child("image").setValue(downUrloadurl.toString());
                             newpost.child("Uid").setValue(mCurrentuser.getUid());
                            newpost.child("Dpimage").setValue(id);
                            newpost.child("mobile").setValue(mobile);
                            newpost1.child("title").setValue(title_val);

                            newpost1.child("image").setValue(downUrloadurl.toString());
                            newpost1.child("Uid").setValue(mCurrentuser.getUid());
                            newpost1.child("Dpimage").setValue(id);
                            newpost1.child("username").setValue(name);
                            newpost1.child("mobile").setValue(mobile);

                            newpost.child("username").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        startActivity(new Intent(PostActivity.this, NavigationActivity.class));

                                    }
                                }
                            });




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    nprogress.dismiss();


                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            imageUri = data.getData();
            nselectImage.setImageURI(imageUri);
        }
    }
}

