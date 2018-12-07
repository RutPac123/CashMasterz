package com.example.sai.cashmaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static maes.tech.intentanim.CustomIntent.customType;

public class howto extends AppCompatActivity {

    private String LANG_CURR = "en";
    private Button changit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto);

        changit =findViewById(R.id.change);


        changit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(howto.this,lang.class);
              startActivity(intent);
              customType(howto.this,"fadein-to-fadeout");
              finish();

            }
        });


    }

}
