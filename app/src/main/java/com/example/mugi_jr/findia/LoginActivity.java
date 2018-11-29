package com.example.mugi_jr.findia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText nemail,npassword;
    Button b;
    TextView t;
    private String type,district,name,id;

    private String email2;
    private String password2;
    private DatabaseReference mdatabaseusers;
    private FirebaseAuth mauth;
    ProgressBar loader;
    ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nemail=(EditText)findViewById(R.id.email);
        npassword=(EditText)findViewById(R.id.password);
        mdatabaseusers = FirebaseDatabase.getInstance().getReference().child("users");

        mprogress=new ProgressDialog(LoginActivity.this);
        loader=(ProgressBar)findViewById(R.id.loading);
        if (loader != null) {
            loader.setVisibility(View.GONE);
        }


        b=(Button)findViewById(R.id.register);
        t=(TextView)findViewById(R.id.sign);
        mauth=FirebaseAuth.getInstance();
        b.setOnClickListener(this);
        t.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v==b)
        {
            LoginActivity();
        }
        else
        {
            Intent inte=new Intent(this,RegisterButtons.class);
            startActivity(inte);
            finish();
        }

    }

    private void LoginActivity() {
        email2 = nemail.getText().toString().trim();
        password2 = npassword.getText().toString().trim();
        if(!TextUtils.isEmpty(email2)&& !TextUtils.isEmpty(password2))
        {
            mprogress.setMessage("Checking Login...");
            mprogress.show();
            mauth.signInWithEmailAndPassword(email2,password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mprogress.dismiss();
                        loader.setVisibility(View.GONE);
                        checkUser_exist();
                    }
                    else
                    {
                        mprogress.dismiss();
                        loader.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        Intent Setupintent = new Intent(LoginActivity.this,RegisterButtons
                                .class);
                        Setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(Setupintent);
                    }
                }
            });
        }
    }

    private void checkUser_exist() {
        final String User_id = mauth.getCurrentUser().getUid();
        mdatabaseusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(User_id))
                {
                    Intent mainIntent = new Intent(LoginActivity.this,RegisterButtons.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
                else
                {

                    DatabaseReference df=FirebaseDatabase.getInstance().getReference("signin_credentials").child(User_id);
                            df.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    do {
                                        district=String.valueOf(dataSnapshot.child("district").getValue());
                                        type = String.valueOf(dataSnapshot.child("type").getValue());
                                        name=String.valueOf(dataSnapshot.child("name").getValue());
                                        Log.e("district",district);
                                        Log.e("type",type);
                                        Log.e("name",name);
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

                                    }while(type==null||type=="");


                                    if(type.equals("Farmers"))
                                    {

                                        Intent Setupintent = new Intent(LoginActivity.this,NavigationActivity
                                                .class);
                                      // Setupintent.putExtra("dpname",name);
                                      //  System.out.println(name);
                                     //   Setupintent.putExtra("mydistrict",district);
                                       // Setupintent.putExtra("dplink",id);
                                       // System.out.println("nearIntednt:"+ id);

                                        Setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(Setupintent);

                                    }
                                    else
                                    {

                                        Intent Setupintent = new Intent(LoginActivity.this,RetailerActivity.class);
                                        Setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(Setupintent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
