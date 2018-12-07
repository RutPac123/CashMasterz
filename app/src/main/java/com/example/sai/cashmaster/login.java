package com.example.sai.cashmaster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import steelkiwi.com.library.DotsLoaderView;

public class login extends AppCompatActivity {
    private Button fb_loging;
    private CallbackManager mCallbackManager;
    Button msignin;
    TextView signup;
    Animation animation;
    Animation animate;
    TextView welcome;
    private static final String TAG = "tag";
    DotsLoaderView dotsLoaderView;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fb_loging = findViewById(R.id.fb_login);
        mAuth = FirebaseAuth.getInstance();
        msignin = findViewById(R.id.btncustom);
        welcome = findViewById(R.id.wlcm);
        signup = findViewById(R.id.textsignup);
        dotsLoaderView = findViewById(R.id.loadkiwi);
        animation = AnimationUtils.loadAnimation(login.this, R.anim.anime);
        animate = AnimationUtils.loadAnimation(login.this, R.anim.scale);

            //Paper.book().write("language","hi");

       // updateView((String)Paper.book().read("language"));

        welcome.startAnimation(animate);
        msignin.startAnimation(animation);
        fb_loging.startAnimation(animation);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent upintent = new Intent(login.this, User_Signup.class);
                startActivity(upintent);
            }
        });

        msignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intsign = new Intent(login.this, Custom_login.class);
                startActivity(intsign);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(login.this, MainActivity.class));
                } else {
                    if (BuildConfig.DEBUG) {
                        //mAuth.signOut();
                    }
                }

            }
        };
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        //onclick fb login method
        fb_loging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dotsLoaderView.show();
                fb_loging.setEnabled(false);
                LoginManager.getInstance().logInWithReadPermissions(login.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        dotsLoaderView.hide();
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        dotsLoaderView.hide();
                        Intent intent = new Intent(login.this, login.class);
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        dotsLoaderView.hide();
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                });
            }
        });

    }

//    private void updateView(String language) {
//        Context context = LocaleHelper.setLocale(this,language);
//        Resources resources= context.getResources();
//        welcome.setText(resources.getString(R.string.Welcome));
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }

    }

    private void updateUI() {
        Intent nextintent = new Intent(login.this, MainActivity.class);
        startActivity(nextintent);
        finish();
        Toast.makeText(this, "Congo! You are now logged in!!", Toast.LENGTH_SHORT).show();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            fb_loging.setEnabled(true);
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            fb_loging.setEnabled(true);
                            // If sign in fails, display a message to the user.
                            Log.i("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
