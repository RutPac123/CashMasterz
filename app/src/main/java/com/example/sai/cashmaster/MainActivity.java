package com.example.sai.cashmaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Intent intentshare;
    String body = "Please share our app ";  // app link will be here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragmnet()).commit();
        BottomNavigationView bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_home:
                           selectedFragment = new HomeFragmnet();
                           break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragmnet();
                            break;
                        case R.id.nav_exit:
                            selectedFragment = new ExitFragmnet();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        if(id == R.id.how){
            Toast.makeText(this, "how", Toast.LENGTH_SHORT).show();
        }

        else if(id == R.id.fback){    Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:"));
            String[] str={"rutvikpachkawade98@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL,str);
            intent.putExtra(Intent.EXTRA_SUBJECT,"Hey,I want to send a feedback :)");
            intent.setType("message/rfc822");
            startActivity(intent);
        }

        else if(id == R.id.toc){
            Toast.makeText(this, "toc", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.logout){
            mAuth = FirebaseAuth.getInstance();

            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Do want to Exit or Logout?");
            builder.setCancelable(true);
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences preferences=getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
                    preferences.edit().remove("isfirstrun").apply();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    updateUI();

                }
            });
            AlertDialog alertDialog= builder.create();
            alertDialog.show();
        }
        else if(id == R.id.rate){
            try{

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"))); // app link will be here

            }catch(Exception e){

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")));

            }
        }else if(id == R.id.share){

            intentshare = new Intent(Intent.ACTION_SEND);
            intentshare.putExtra(Intent.EXTRA_SUBJECT, "My App");
            intentshare.setType("text/plain");
            intentshare.putExtra(Intent.EXTRA_TEXT, body+Uri.parse("https://youtube.com"));
            startActivity(intentshare.createChooser(intentshare, "Share it via"));

        }
        return super.onOptionsItemSelected(item);
    }
    private void updateUI() {
        Intent intnt=new Intent(this,login.class);
        startActivity(intnt);
        this.finish();
        Toast.makeText(this, "You are logged out!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do want to exit?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SharedPreferences preferences=getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
                preferences.edit().remove("isfirstrun").apply();
                Intent intent =new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }
}
