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

public class Custom_login extends AppCompatActivity {
    DotsLoaderView dotsLoaderView2;
    TextView forgot;
    TextView sintxt;
    Animation anmn1;
    Animation anmn2;
    EditText usrmail;
    EditText usrpass;
    Button btnLog;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);


        forgot=findViewById(R.id.frgt);
        sintxt=findViewById(R.id.textinn);
        usrmail=findViewById(R.id.smail);
        usrpass=findViewById(R.id.spass);
        btnLog=findViewById(R.id.sinbtn);
        dotsLoaderView2=findViewById(R.id.loadkiwil);

        anmn1= AnimationUtils.loadAnimation(Custom_login.this,R.anim.alpha_one);
        sintxt.startAnimation(anmn1);

        anmn2=AnimationUtils.loadAnimation(Custom_login.this,R.anim.fadein);
        btnLog.startAnimation(anmn2);

        mAuth=FirebaseAuth.getInstance();

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn(view);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Custom_login.this,Forgot.class);
                startActivity(intent);
            }
        });
    }
    public void SignIn(View view){
        final String sinmail=usrmail.getText().toString();
        final String sinpass=usrpass.getText().toString();
        if(sinmail.isEmpty() && sinpass.isEmpty()){
            Toast.makeText(this, "Empty credentials!!", Toast.LENGTH_SHORT).show();
        }else {
            dotsLoaderView2.show();
            mAuth.signInWithEmailAndPassword(sinmail, sinpass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dotsLoaderView2.hide();
//                                Intent nintent=new Intent(Custom_Login.this,MainActivity.class);
//                                nintent.putExtra("username",usname);
//                                startActivity(nintent);

                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                Intent intent = new Intent(Custom_login.this, MainActivity.class);
                                startActivity(intent);
                                //FirebaseUser user = mAuth.getCurrentUser();
                                // updateUI(user);
                            } else {
                                // loginanime2();
                                dotsLoaderView2.hide();
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(Custom_login.this, "Authentication failed,Check your credentials!",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }

    }
}
