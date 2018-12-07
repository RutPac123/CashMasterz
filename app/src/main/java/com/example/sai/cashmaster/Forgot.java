package com.example.sai.cashmaster;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import steelkiwi.com.library.DotsLoaderView;

public class Forgot extends AppCompatActivity {
    TextView view;
    Animation animation;
    DotsLoaderView loader;
    FirebaseAuth mAuth;
    EditText email;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        view=findViewById(R.id.instr);
        animation= AnimationUtils.loadAnimation(Forgot.this,R.anim.fadein);
        loader=findViewById(R.id.loadkiwisnd);
        email=findViewById(R.id.mailfrgt);
        btnSend=findViewById(R.id.lnksnd);
        mAuth=FirebaseAuth.getInstance();

        view.startAnimation(animation);
        btnSend.startAnimation(animation);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputMail = email.getText().toString();
                if (inputMail.isEmpty()) {
                    Toast.makeText(Forgot.this, "Entered your registered email id", Toast.LENGTH_SHORT).show();
                } else{
                    loader.show();
                    mAuth.sendPasswordResetEmail(inputMail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        loader.hide();
                                        Toast.makeText(Forgot.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loader.hide();
                                        Toast.makeText(Forgot.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
