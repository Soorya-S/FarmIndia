package com.example.mugi_jr.findia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

//import android.support.v7.app.AppCompatActivity;
//import com.firebase.client.core.Context;

public class RetailerActivity extends AppCompatActivity {


    private RecyclerView nbloglist;
    private DatabaseReference mdatabase,mdatabase1;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthlistner;
    private DatabaseReference mdatabaseUsers;
    private FirebaseUser mcurrentUser;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    public String districname;
    private String name,type,district,mobile,no="9789146655";
Button b;
    private String key;



    LinearLayoutManager layoutManager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer);
        mauth=FirebaseAuth.getInstance();
        mcurrentUser=mauth.getCurrentUser();
         b=(Button)findViewById(R.id.call);
      /*  b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calling=new Intent(Intent.ACTION_DIAL);
                calling.setData(Uri.parse("tel"+no));
                calling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(calling);
            }
        });*/

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("signin_credentials").child(mcurrentUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                district = String.valueOf(dataSnapshot.child("district").getValue());
                type = String.valueOf(dataSnapshot.child("type").getValue());
                name = String.valueOf(dataSnapshot.child("name").getValue());
               // mobile=String.valueOf(dataSnapshot.child("phno").getValue());

                Log.e("district", district);
                Log.e("farmer", type);
                Log.e("name", name);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.District, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getSelectedItem()+"selected",Toast.LENGTH_LONG).show();
                if(parent.getSelectedItemPosition()!=0) {
                    districname = parent.getSelectedItem().toString();
                    Log.e("DistrictName", districname);
                    Intent customactivity = new Intent(RetailerActivity.this, CustomRetailerActivity.class);
                    customactivity.putExtra("DistrictName", districname);
                    startActivity(customactivity);
                }

            }
;

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        nbloglist = (RecyclerView) findViewById(R.id.blog_list);


        nbloglist.setHasFixedSize(true);
        //nbloglist.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linear = new LinearLayoutManager(this);
        linear.setReverseLayout(true);
        linear.setStackFromEnd(true);
        nbloglist.setLayoutManager(linear);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mdatabaseUsers.keepSynced(true);


        mauthlistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(RetailerActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }

            }
        };


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        checkUser_exist();
        mauth.addAuthStateListener(mauthlistner);
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mdatabase


        )


        {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                key=getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());

                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setDpimage(getApplicationContext(), model.getDpimage());
               viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RetailerActivity.this,key,Toast.LENGTH_LONG).show();
                       /* Log.e("Key",key);
                        mdatabase1 = FirebaseDatabase.getInstance().getReference().child("Blog").child("Kgd1gTJSPG7iHGdVUdy");
                        mdatabase1.child("mobile").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                no=String.valueOf(dataSnapshot.getValue());
                                Log.e("Mobile",no);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/

                      /*  Intent calling=new Intent(Intent.ACTION_DIAL);
                        calling.setData(Uri.parse("9789146655"));
                        calling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(calling);*/
                    }
                });
               /* b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RetailerActivity.this,key,Toast.LENGTH_LONG).show();
                        mdatabase1 = FirebaseDatabase.getInstance().getReference().child("Blog").child(key);
                        mdatabase1.child("mobile").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                no=String.valueOf(dataSnapshot.getValue());
                                Log.e("Mobile",no);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Intent calling=new Intent(Intent.ACTION_DIAL);
                        calling.setData(Uri.parse(no));
                        calling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(calling);



                    }
                });*/

            }


        };
        nbloglist.setAdapter(firebaseRecyclerAdapter);
        nbloglist.setLayoutManager(layoutManager);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    private void checkUser_exist() {
        if (mauth.getCurrentUser() != null) {


            final String User_id = mauth.getCurrentUser().getUid();
            mdatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(User_id)) {
                        //Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        //setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(setupIntent);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Retailer Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public BlogViewHolder(View itemView) {

            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);

        }


        public void setUsername(String username) {
            TextView uname = (TextView) mView.findViewById(R.id.uname);
            uname.setText(username);
        }


        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }

        public void setDpimage(Context ctx, String Dpimage) {
            CircleImageView dpimage = (CircleImageView) mView.findViewById(R.id.dpimage);
            Picasso.with(ctx).load(Dpimage).into(dpimage);
        }


    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_navigation_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_jpost) {
            startActivity(new Intent(RetailerActivity.this, PostActivity.class));
        }

        if (item.getItemId() == R.id.action_logout) {
            mauth.signOut();
        }

        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(RetailerActivity.this, SetupActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }
}







