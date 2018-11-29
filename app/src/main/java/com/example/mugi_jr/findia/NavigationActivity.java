package com.example.mugi_jr.findia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView nbloglist;
    private DatabaseReference mdatabase;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthlistner;
    private DatabaseReference mdatabaseUsers;

    private FirebaseUser mcurrentuser;

    private String mydistrict,dplink,dpname;
    private String no="9789146655";


    LinearLayoutManager layoutManager;
    private int i=0;
    private GoogleApiClient client;
    //ImageView dp;
    //TextView uname,uemail;
    private Context ctx=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //dp=(ImageView)findViewById(R.id.UserDP);
        //uname=(TextView)findViewById(R.id.UserName);
       // uemail=(TextView)findViewById(R.id.UserEmail);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mauth = FirebaseAuth.getInstance();
        mcurrentuser = mauth.getCurrentUser();
      //  mydistrict=getIntent().getExtras().getString("mydistrict");
      //  dplink=getIntent().getStringExtra("dplink");
     //  dpname =getIntent().getExtras().getString("dpname");
       // System.out.println(dpname);
      // uname.setText("Mugesh");



        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        nbloglist = (RecyclerView) findViewById(R.id.blog_list);


        nbloglist.setHasFixedSize(true);
        nbloglist.setLayoutManager(new LinearLayoutManager(this));
        //  LinearLayoutManager linear = new LinearLayoutManager(this);
        // linear.setReverseLayout(true);
        // linear.setStackFromEnd(true);
        //  nbloglist.setLayoutManager(linear);



       // System.out.println("District:" +mydistrict);
     //   mdatabase = FirebaseDatabase.getInstance().getReference().child("Blog1").child(mydistrict);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Blog");


        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mdatabaseUsers.keepSynced(true);
       // System.out.println("DpImageLInk:  " +dplink);

       // Picasso.with(ctx).load("https://firebasestorage.googleapis.com/v0/b/farmindia-1522f.appspot.com/o/Profile_Pics%2Fcropped-663599955.jpg?alt=media&token=afdb8a01-c062-4dc4-bbf1-d8ae97f6f693").into(dp);


        mauthlistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(NavigationActivity.this, LoginActivity.class);
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
        FirebaseRecyclerAdapter<Blog, NavigationActivity.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, NavigationActivity.BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                NavigationActivity.BlogViewHolder.class,
                mdatabase


        )


        {
@Override
            protected void populateViewHolder(NavigationActivity.BlogViewHolder viewHolder, Blog model, int position) {
                viewHolder.setTitle(model.getTitle());

                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setDpimage(getApplicationContext(), model.getDpimage());


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
                .setName("Home Page") // TODO: Define a title for the content shown.
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/
        if (item.getItemId() == R.id.action_jpost) {
            startActivity(new Intent(NavigationActivity.this, PostActivity.class));
        }

        if (item.getItemId() == R.id.action_logout) {
            mauth.signOut();
        }

        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(NavigationActivity.this, SetupActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camer) {

            Intent calling=new Intent(Intent.ACTION_DIAL);
            calling.setData(Uri.parse("tel"+no));

            startActivity(calling);


            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent mypro=new Intent(getApplicationContext(),MyProfileActivity.class);
            startActivity(mypro);

        } else if (id == R.id.nav_slideshow) {
            Intent mypro1=new Intent(getApplicationContext(),PriceActivity.class);
            startActivity(mypro1);

        } else if (id == R.id.nav_manage) {

        }else if(id==R.id.nav_logout)
        {
            Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_LONG).show();
            mauth.signOut();
        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
