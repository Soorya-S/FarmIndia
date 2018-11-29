package com.example.mugi_jr.findia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RegisterButtons extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_buttons);
    }
    public void register_page(View view)

    {

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


            } else {
                //findViewById(R.id.loading).setVisibility(View.VISIBLE);
                Intent intent = new Intent(this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }

    }
    public void register_page1(View view)

    {
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


        } else {
            //findViewById(R.id.loading).setVisibility(View.VISIBLE);
            Intent intent = new Intent(this,SignUpActivity1.class);
            startActivity(intent);
            finish();
        }

    }

}
