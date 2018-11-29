package com.example.mugi_jr.findia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class SetupActivity extends AppCompatActivity {

    private ImageButton imgbtn;
    private EditText name;
    private Button btn;
    private static final int GALLERY_REQUEST= 1;
    private Uri imguri=null;
    private DatabaseReference mdatabaseusers;
    private FirebaseAuth mAuth;
    private StorageReference mstorage;
    private ProgressDialog mprogress;
    public String Name2="",District="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        mprogress = new ProgressDialog(this);
        mstorage = FirebaseStorage.getInstance().getReference().child("Profile_Pics");
        mAuth = FirebaseAuth.getInstance();
        mdatabaseusers= FirebaseDatabase.getInstance().getReference().child("users");
        imgbtn = (ImageButton)findViewById(R.id.profileimage);
        name = (EditText) findViewById(R.id.dpname);
        btn = (Button) findViewById(R.id.setupfinish);
        name.setEnabled(false);
        SharedPreferences sp1=getSharedPreferences("myprefs1",MODE_PRIVATE);
       Name2 = sp1.getString("Namekey",null);
        District=sp1.getString("distkey",null);
        name.setText(Name2);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupActivity();

            }
        });

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_Int = new Intent();
                gallery_Int.setAction(Intent.ACTION_GET_CONTENT);
                gallery_Int.setType("image/*");
                startActivityForResult(gallery_Int,GALLERY_REQUEST);
            }
        });
    }

    private void startSetupActivity() {



       // final String name1 = name.getText().toString().trim();
        final String user_id = mAuth.getCurrentUser().getUid();

        if(!TextUtils.isEmpty(Name2) && imguri != null)
        {
            mprogress.setMessage("Finishing Setup..");
            mprogress.show();

            StorageReference filepath = mstorage.child(imguri.getLastPathSegment());
            filepath.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                  //  mdatabaseusers.child(user_id).child("name").setValue(name1);
                   // mdatabaseusers.child(user_id).child("Postimage").setValue(downloadUri);
                    mdatabaseusers.child(District).child("Farmers").child(Name2).child("Postimage").setValue(downloadUri);

                    mprogress.dismiss();
                    Intent mainIntent = new Intent(SetupActivity.this,NavigationActivity.class);
                    mainIntent.putExtra("mydistrict",District);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }
            });
            /*

       */
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imguri = result.getUri();
                imgbtn.setImageURI(imguri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

