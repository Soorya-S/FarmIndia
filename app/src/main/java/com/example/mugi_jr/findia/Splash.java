package com.example.mugi_jr.findia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Locale;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1200;
    private ImageView imageView;
    public String LANG;
    public void onCreate(Bundle s)
    {
        super.onCreate(s);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView)findViewById(R.id.imageView2);
        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(isFirst(Splash.this))
                {
                    Intent i = new Intent(Splash.this, LanguageSplash.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(Splash.this, LoginActivity.class);
                    startActivity(i);
                    LANG=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("LANGUAGE", "en");
                    finish();
                    Locale l=new Locale(LANG);
                    Locale.setDefault(l);
                    Configuration config=new Configuration();
                    config.locale=l;
                    getResources().updateConfiguration(config,getResources().getDisplayMetrics());

                }
            }
        }, SPLASH_TIME_OUT);
    }

    private static final String MY_PREFERENCES = "my_preferences";

    public static boolean isFirst(Context context)
    {
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        if(first){
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        return first;
    }

}


