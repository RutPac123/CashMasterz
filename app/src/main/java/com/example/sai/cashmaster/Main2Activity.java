package com.example.sai.cashmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.FacebookSdk;

public class Main2Activity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setContentView(R.layout.activity_main2);

        final ImageView imageView=(ImageView)findViewById(R.id.iconsplash);
        final AsyncTask<String,String,String> asyncTask=new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                SharedPreferences sharedPreferences=getSharedPreferences("PREFERENCE",MODE_PRIVATE);
                sharedPreferences.edit().remove("isfirstrun").commit();
                return null;
            }
        };
        asyncTask.execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //startService(new Intent(getBaseContext(), Import_fragment.OnClearFromRecentService.class));
                Intent intent=new Intent(Main2Activity.this,login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
