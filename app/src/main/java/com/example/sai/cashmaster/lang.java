package com.example.sai.cashmaster;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import static maes.tech.intentanim.CustomIntent.customType;

public class lang extends AppCompatActivity {

    private Button changeng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang);

        changeng = findViewById(R.id.btneng);
        changeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mintent = new Intent(lang.this,howto.class);
                startActivity(mintent);
                customType(lang.this,"fadein-to-fadeout");
                finish();
            }
        });

    }
}
