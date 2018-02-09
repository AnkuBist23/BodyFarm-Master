package aman.backender.com.bodyfarm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import aman.backender.com.bodyfarm.Authentication.Login_signup;
import aman.backender.com.bodyfarm.R;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    public static SharedPreferences mPrefs;
    public static int sp_mode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isOnline()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPrefs = getSharedPreferences("IS_login", MODE_PRIVATE);
                    String login_id = mPrefs.getString("IS_login", "");
                    String userInfo = mPrefs.getString("userInfo", "");
                    if (login_id.equals("yes")){
                        int recId = (mPrefs.getInt("recId", 0));
                        sp_mode = 5 ;
                        Intent i = new Intent(Splash.this, Home.class);
                        i.putExtra("userInfo",recId);
                        startActivity(i);
                    }else{
                        startActivity(new Intent(Splash.this,Login_signup.class));
                    }
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else
        {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alertDialog.show();
            }
            catch(Exception e)
            {

                Log.d("Splash", "Show Dialog: "+e.getMessage());
            }
        }
    }
    
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(Splash.this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
