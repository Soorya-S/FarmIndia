package com.example.mugi_jr.findia;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PriceActivity extends AppCompatActivity {

    private ListView nlist;
    DatabaseReference ndatabase;
    private TextView dis;
    private Spinner spinner;
    private String districname;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        nlist = (ListView) findViewById(R.id.nlistView);
        dis = (TextView) findViewById(R.id.districttext);


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
                    check_price(districname);
                }

            }
            ;

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void check_price(String dis2) {


        ndatabase = FirebaseDatabase.getInstance().getReference("Price").child(dis2);
        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                this,
                String.class,
                R.layout.pricedisp,
                ndatabase


        ) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(R.id.cropname);
                textView.setText(model);
            }
        };
        nlist.setAdapter(firebaseListAdapter);

    }
}

