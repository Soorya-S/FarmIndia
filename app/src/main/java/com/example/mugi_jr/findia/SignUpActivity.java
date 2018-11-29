package com.example.mugi_jr.findia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    public boolean network_enabled = false;
    public EditText name_m;
    private EditText email_m;
    private EditText phone_m;
    private EditText pass;
    private EditText Distric_m;
    private EditText category;
    EditText cateEdittext;
    EditText disEdittext;
    Button register;
    private int email_flag=0;
    private int name_flag=0;
    private int blood_flag=0;
    private int phone_flag=0;
    private int pass_flag=0;
    private int distric_flag=0;
    ProgressBar loader;
    ProgressDialog prodialog;
    public String name1="hello";
    private String email1;
    private String password;
    private String mob;
    private String Distric;
    private String category1;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private int flag=1;
    AlertDialog  levelDialog;
   private SharedPreferences sp;
    public static final String prefs="myprefs1";
    public static final String diskey="distkey";
    public static final String namekey="Namekey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users");
        mAuth=FirebaseAuth.getInstance();
        prodialog=new ProgressDialog(SignUpActivity.this);
        loader=(ProgressBar) findViewById(R.id.loading);
        sp=getSharedPreferences(prefs,Context.MODE_PRIVATE);
        if (loader != null) {
            loader.setVisibility(View.GONE);
        }

        register=(Button)findViewById(R.id.register_cloud);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setEnabled(false);
                SignUpActivity.this.prodialog.setMessage("Please Wait");
                SignUpActivity.this.prodialog.setCancelable(false);
                SignUpActivity.this.prodialog.show();
                attempt_register();
                confirm_register();
            }
        });
        disEdittext=(EditText)findViewById(R.id.distric);
        disEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_distric();
            }
        });
        cateEdittext=(EditText)findViewById(R.id.category);
        cateEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_category();
            }
        });

    }
    public void attempt_register() {
        //--------------------------------------------
        name_m = (EditText) findViewById(R.id.name_m);
        email_m = (EditText) findViewById(R.id.email_m);

        pass = (EditText) findViewById(R.id.pass);

        phone_m = (EditText) findViewById(R.id.phone_m);
        Distric_m=(EditText)findViewById(R.id.distric);

        category=(EditText)findViewById(R.id.category);
        //---------------------------------------------

        name_m.setError(null);
        email_m.setError(null);

        pass.setError(null);

        phone_m.setError(null);
        Distric_m.setError(null);
        category.setError(null);


        email1 = email_m.getText().toString();
        name1 = name_m.getText().toString();
        name1=name1.replaceAll("\\s+","");
        mob = phone_m.getText().toString();
        password=pass.getText().toString();
        Distric=Distric_m.getText().toString();
        category1 =category.getText().toString();


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email1)) {
            email_m.setError("Email is Required");
            focusView = email_m;
            cancel = true;


            email_flag=1;
        }
        else
        {
            email_flag=2;
            if(email1.contains("@")&&((email1.contains(".com"))||(email1.contains(".in"))))
            {}
            else
            {
                email_m.setError("Invalid");
                focusView = email_m;
                cancel = true;
                email_flag=1;
            }
        }
        if (TextUtils.isEmpty(name1)) {
            name_m.setError("Name is Required");
            focusView = name_m;
            cancel = true;
            name_flag=1;
        }
        else
        {
            name_flag=2;
            if(name1.length()<4)
            {
                name_flag=1;
                name_m.setError("Name should contain at least 4 characters");
            }

        }
        if(TextUtils.isEmpty(password))
        {
            pass.setError("Password is Required");
            focusView=pass;
            cancel=true;
            pass_flag=1;

        }
        else
        {
            pass_flag=2;

        }


        if (TextUtils.isEmpty(mob)) {
            phone_m.setError("Phone No.is Required");
            focusView = phone_m;
            cancel = true;
            phone_flag=1;
        }
        else
        {
            phone_flag=2;
            if(mob.length()==10)
            {}
            else
            {
                phone_m.setError("Check your Number");
                focusView = phone_m;
                cancel = true;
                phone_flag=1;
            }

        }
        if(TextUtils.isEmpty(Distric))
        {
            Distric_m.setError("Password is Required");
            focusView=Distric_m;
            cancel=true;
            distric_flag=1;

        }
        else
        {
            distric_flag=2;

        }
        if (TextUtils.isEmpty(category1)) {
            category.setError("Choose your Category");
            focusView = category;
            cancel = true;
            blood_flag=1;
        }
        else
        {
            blood_flag=2;
        }



    }

    public void confirm_register() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        final Boolean isconnect = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!isconnect) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Internet Connection Not Available");
            dialog.setPositiveButton(("Turn On"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    startActivity(intent);

                }
            });
            dialog.setNegativeButton(("Cancel"), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();


        }


         else if (email_flag == 2 && name_flag == 2 && phone_flag == 2 && blood_flag == 2&&distric_flag ==2 && pass_flag == 2) {
               mAuth.createUserWithEmailAndPassword(email1,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful())
                     {

                         String User_id=mAuth.getCurrentUser().getUid();
                         DatabaseReference currentuser=mDatabase.child(Distric).child("Farmers").child(name1);
                        // currentuser.child("Name").setValue(name1);
                         currentuser.child("Email").setValue(email1);
                         currentuser.child("Phone").setValue(mob);
                         currentuser.child("UID").setValue(User_id);
                         currentuser.child("Category").setValue(category1);
                         SignUpActivity.this.prodialog.dismiss();
                         loader.setVisibility(View.GONE);
                         SharedPreferences.Editor editor=sp.edit();
                         editor.putString(namekey,name1);
                         editor.putString(diskey,Distric);
                         editor.commit();
                         DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
                         ref=ref.child("signin_credentials").child(User_id);
                         ref.child("district").setValue(Distric);
                         ref.child("type").setValue("Farmers");
                         ref.child("name").setValue(name1);
                         ref.child("mobile").setValue(mob);
                         Intent intent1 = new Intent(SignUpActivity.this,SetupActivity.class);


                         startActivity(intent1);
                         finish();

                     }
                   }
               });


            }
            else{
                SignUpActivity.this.prodialog.dismiss();
                register.setEnabled(true);
            }

    }


    public void show_category(){
        if (flag == 1) {
            flag = 0;

            final CharSequence[] items = {"Apple","Red Gram","Bajra","Barley","Castor Seed","Bengal gram","Cotton","Cumin","Ground Nut","Jowar","Guar Seed","Mahua Flower","Maize","Lentil","Green Gram",
                    "Mustard Seed","Onion","Paddy","Potato","Red Chilli","Shelling Peas","Soyabean","Sunflower Seed","Tamarind","Tomato","Turmeric","Black Gram",
            "Wheat","Rajma","Raghi","Lobia","Basmati Rice","Kusum Seed","Sesame Seed","Pear","Mandarin","Sapota","Musk Melon","Table Grapes","Litchi",
            "Pomegranate","Banana","Plum","Peach","Mango","Orange","Custard Apple","Watermelon","Lemon","Bottle Gourd","Bitter gourd","Cucumber",
            "Brinjal","Cauliflower","Green Chillies","Carrot","Sweet Potato","Spinach","Mustard Leaf","Coriander Leaf","Garlic","Lady Finger",
            "Ginger","Beet Root","Ribbed Celery","Ajowain","Gud"};
            // Creating and Building the Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setTitle("Select Your Category");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    cateEdittext.setText(items[item]);
                    flag = 1;
                    levelDialog.dismiss();


                }
            });

            levelDialog = builder.create();
            levelDialog.show();
        }

    }
    public void show_distric(){
        if (flag == 1) {
            flag = 0;

            final CharSequence[] items = {"Amritsar","Barnaalaa","Bathinda","Faridkot","Fatehgarh Sahib","Firozpur","Fazilka",
            "Gurdaspur","Hoshiarpur","Jalandhar","Kapurthala","Ludhiana","Mansa","Moga","Sri Muktsar Sahib","Pathankot","Patiala",
            "Rupnagar","Sahizada Ajit Singh nagar","Sangrur","Shahid Bhagat Singh Nagar","Tarn Taran"};
            // Creating and Building the Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setTitle("Select Your Distric");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    disEdittext.setText(items[item]);
                    flag = 1;
                    levelDialog.dismiss();


                }
            });

            levelDialog = builder.create();
            levelDialog.show();
        }

    }


}
