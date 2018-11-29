package com.example.mugi_jr.findia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LanguageSplash extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnClickListener {

    Button go;
    Spinner sp;
    public String language = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        sp = (Spinner) findViewById(R.id.spinner5);


        // sp=(Spinner)findViewById(R.id.spinner);
        go = (Button) findViewById(R.id.button2);
        List<String> categories = new ArrayList<String>();
        categories.add("English");
        categories.add("தமிழ்");
        categories.add("हिंदी");//hindi
        categories.add("ਪੰਜਾਬੀ");//punjabi
        categories.add("मराठी");//marathi
        categories.add("ગુજરાતી");//Gujarati
        categories.add("বাংলা");//bangla
        categories.add("മലയാളം");//malayalam
        categories.add("తెలుగు");//telugu
        categories.add("ಕನ್ನಡ");//kannada

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(dataAdapter);
        sp.setOnItemSelectedListener(this);
        go.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

    }


    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */


    @Override
    public void onClick(View v) {
        Locale l = new Locale(language);
        Locale.setDefault(l);
        Configuration config = new Configuration();
        config.locale = l;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("LANGUAGE", language).commit();
        Intent i = new Intent(LanguageSplash.this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getSelectedItemPosition() == 0) {
            language = "en";
        } else if (parent.getSelectedItemPosition() == 1) {
            language = "ta";
        } else if (parent.getSelectedItemPosition() == 2) {
            language = "hi";
        } else if (parent.getSelectedItemPosition() == 3) {
            language = "pa";
        } else if (parent.getSelectedItemPosition() == 4) {
            language = "mr";
        } else if (parent.getSelectedItemPosition() == 5) {
            language = "gu";
        } else if (parent.getSelectedItemPosition() == 6) {
            language = "bn";
        } else if (parent.getSelectedItemPosition() == 7) {
            language = "ml";
        } else if (parent.getSelectedItemPosition() == 8) {
            language = "te";
        } else if (parent.getSelectedItemPosition() == 9) {
            language = "kn";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
