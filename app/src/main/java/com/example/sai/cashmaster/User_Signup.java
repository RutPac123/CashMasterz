package com.example.sai.cashmaster;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import steelkiwi.com.library.DotsLoaderView;

public class User_Signup extends AppCompatActivity {

    DotsLoaderView dotsLoaderView1;
    Animation anmn1;
    Animation anmn2;
    TextView tvanime1;
    EditText mailup;
    EditText passup;
    public boolean earnedCurrency = false;
    Button createacc;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__signup);
        mailup = findViewById(R.id.umail);
        passup = findViewById(R.id.upass);
        createacc = findViewById(R.id.custlogbtn);
        tvanime1 = findViewById(R.id.textupp);
        dotsLoaderView1 = findViewById(R.id.loadkiwis);

        anmn1 = AnimationUtils.loadAnimation(User_Signup.this, R.anim.alpha_one);
        tvanime1.startAnimation(anmn1);

        anmn2 = AnimationUtils.loadAnimation(User_Signup.this, R.anim.fadein);
        createacc.startAnimation(anmn2);

        mAuth = FirebaseAuth.getInstance();
        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister(view);
            }
        });

    }

    public void onRegister(View view) {
        final String myMail = mailup.getText().toString();
        final String myPass = passup.getText().toString();

        if (myMail.isEmpty() && myPass.isEmpty()) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
        } else {
            dotsLoaderView1.show();
            mAuth.createUserWithEmailAndPassword(myMail, myPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dotsLoaderView1.hide();
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(User_Signup.this, "User signed up successfully!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(User_Signup.this, MainActivity.class);
                                startActivity(intent);
                                // FirebaseUser user = mAuth.getCurrentUser();
                                // updateUI(user);
                            } else {
                                dotsLoaderView1.hide();
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(User_Signup.this, "SignUp failed!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }

                    });
        }
    }
}
