package com.example.sai.cashmaster;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

import javax.xml.datatype.Duration;

import co.enhance.Enhance;
import me.itangqi.waveloadingview.WaveLoadingView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class HomeFragmnet extends Fragment implements RewardedVideoAdListener{

    Activity context;
    SharedPreferences startpref;
    public Button invitbtn;
    Vibrator vibrator;
    public Boolean isNotificationEnabled=true;
    MediaPlayer mmedia;
    public String androidId;
    public String todaystring;
    public SharedPreferences timepref;
    public SharedPreferences mtimepref;
    public boolean earnedCurrency = false;
    public double wvPts = 0.0;
    public double a = 0.0;
    public double adpts = 0.0;
    public double dailyperc=0.0;
    public double pts = 0.0;
    public double radpts = 0.0;
    public WaveLoadingView waveLoadingView;
    public String setpts;
    public double mpts;
    public boolean currentday;
    public boolean currentinviteday;
    TextView points;
    Animation animation;
    Animation an1;
    Animation an2;
    Animation an3;
    Calendar calendar;
    public int year;
    public int month;
    public int day;
    public double daily;
    public Button mEnhacedbtn;
    public Button mRewardedbtn;
    public Button videoadbtn;
    ImageView mimage;
    private static String uniqueID = null;
    private static final int REQUEST_INVITE = 100;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private RewardedVideoAd mRewardedVideoAd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context =getActivity();
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.fragment_home,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        invitbtn = context.findViewById(R.id.invt);
        startpref = context.getSharedPreferences("BONUS", Context.MODE_PRIVATE);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        androidId = "" + Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        waveLoadingView = context.findViewById(R.id.wave);
        points = context.findViewById(R.id.tvpts);
        //specialOff=context.findViewById(R.id.btnSpec);
        mEnhacedbtn = context.findViewById(R.id.btnenhance);
        mRewardedbtn = context.findViewById(R.id.btnrw);
        videoadbtn=context.findViewById(R.id.iron);

        //mimage=context.findViewById(R.id.gift);
        animation = AnimationUtils.loadAnimation(context, R.anim.fadein);
        an1 = AnimationUtils.loadAnimation(context, R.anim.zoomin);
        an2 = AnimationUtils.loadAnimation(context, R.anim.zoomout);
        an3 = AnimationUtils.loadAnimation(context, R.anim.shake);

        points.startAnimation(an1);
        points.startAnimation(an2);
        mEnhacedbtn.startAnimation(animation);
        mRewardedbtn.startAnimation(animation);
        //btnrwrd.startAnimation(animation);
        invitbtn.startAnimation(animation);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        todaystring = year + "" + month + "" + day + "";

        timepref = context.getSharedPreferences("REWARD", 0);
        currentday = timepref.getBoolean(todaystring, false);
        mtimepref = context.getSharedPreferences("INVITE", 0);
        currentinviteday = mtimepref.getBoolean(todaystring, false);

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        videoadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRewardedVideoAd.isLoaded()){
                    mRewardedVideoAd.show();
                }else{
                    Toast.makeText(context, "Video is not loaded!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set currency callback for offerwalls
        // It is called every time the user receives a reward from offerwall
        Enhance.setReceivedCurrencyCallback(new Enhance.CurrencyCallback() {
            @Override
            public void onCurrencyGranted(int amount) {
                if (!isOnline()) {
                    nointernet();
                } else {
                    mmedia = MediaPlayer.create(context, R.raw.water);
                    mmedia.start();
                    Toast.makeText(context, "Currency granted : " + String.valueOf(amount), Toast.LENGTH_SHORT).show();

                    SharedPreferences pref = context.getSharedPreferences("SAVING", Context.MODE_PRIVATE);
                    setpts = pref.getString(encrypt("mypoints"), encrypt("0"));
                    if (Double.valueOf(points.getText().toString()) >= 4.4) {
                        Toast.makeText(context, "You have reached the goal!", Toast.LENGTH_SHORT).show();
                    } else {
                        double mset = Double.valueOf(points.getText().toString()) + amount * (0.00088);
                        points.setText(String.valueOf(mset));


                        SharedPreferences sharedPreferences = context.getSharedPreferences("SAVING", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edt = sharedPreferences.edit();
                        edt.putString(encrypt("mypoints"), encrypt(points.getText().toString()));
                        edt.apply();
                        id(context);
                        adpts = adpts + ((amount * 0.00088) / (0.044));
                        //remeber to take 2 zeroes back in this calculation!!
                        double progVal = (amount * 0.00088) / (0.044);
                        progress(progVal);

                        //saving ads points
                        SharedPreferences crssprf = context.getSharedPreferences("ADS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editads = crssprf.edit();
                        editads.putString(encrypt("adpoints"), encrypt(String.valueOf(adpts)));
                        editads.apply();

                        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
                            FirebaseDatabase.getInstance().getReference("MyUsers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                    .child(uniqueID).child(androidId).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(String.valueOf(mset));

                            FirebaseDatabase.getInstance().getReference("MyUsers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                    .child(uniqueID).child(androidId).child("Offerwall")
                                    .setValue(adpts);

                        } else {
                            FirebaseDatabase.getInstance().getReference("MyUsers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                    .child(uniqueID).child(androidId).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(String.valueOf(mset));


                            FirebaseDatabase.getInstance().getReference("MyUsers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                    .child(uniqueID).child(androidId).child("Offerwall")
                                    .setValue(adpts);
                        }
                    }
                }
            }
        });


        // Daily reward granting area
        dailyreward();
        // invites
        invitbtn.setEnabled(false);
        if (!currentinviteday) {

            invitbtn.setEnabled(true);
            invitbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // onInviteClicked();
                    SharedPreferences.Editor timedaily1 = mtimepref.edit();
                    timedaily1.putBoolean(todaystring, true);
                    timedaily1.apply();

                }
            });

        } else {
            Toast.makeText(context, "You can send invites tommorow :)", Toast.LENGTH_SHORT).show();
        }

        waveLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // main points part
                if (Double.valueOf(points.getText().toString()) >= 4.4) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
//                    } else {
//                        vibrator.vibrate(100);
//                    }
                    Snackbar.make(view, "You have reached the goal!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    mpts = Double.valueOf(points.getText().toString());

                    mpts = mpts + 0.00088;

                    points.setText(String.valueOf(mpts));
                    //writing
                    SharedPreferences sharedPreferences = context.getSharedPreferences("SAVING", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = sharedPreferences.edit();
                    edt.putString(encrypt("mypoints"), encrypt(points.getText().toString()));
                    edt.apply();
                    id(context);

                    pts = pts + 0.02;
                    SharedPreferences ptspref = context.getSharedPreferences("PTS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editpts = ptspref.edit();
                    editpts.putString(encrypt("clickpoints"), encrypt(String.valueOf(pts)));
                    editpts.apply();
                    if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
                        FirebaseDatabase.getInstance().getReference("MyUsers").
                                child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                .child(uniqueID).child(androidId).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(points.getText().toString());

                        //check fire
                        FirebaseDatabase.getInstance().getReference("MyUsers").
                                child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                .child(uniqueID).child(androidId).child("Points")
                                .setValue(pts);

                    } else {
                        FirebaseDatabase.getInstance().getReference("MyUsers")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                .child(uniqueID).child(androidId).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(String.valueOf(points.getText().toString()));

                        //check fire
                        FirebaseDatabase.getInstance().getReference("MyUsers").
                                child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                .child(uniqueID).child(androidId).child("Points")
                                .setValue(pts);
                    }

                    //remember to take 100/x value as increament value!!
                    wvPts = wvPts + 0.02;
                    // waveloading part
                    SharedPreferences preff = context.getSharedPreferences("WAVE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = preff.edit();
                    ed.putString(encrypt("wavedata"), encrypt(String.valueOf(wvPts)));
                    ed.apply();

                    waveLoadingView.setProgressValue((int) Math.abs(wvPts));
                    if (wvPts < 50) {
                        waveLoadingView.setBottomTitle(String.format("%d%%", Math.round(wvPts)));
                        waveLoadingView.setTopTitle("");
                        waveLoadingView.setCenterTitle("");

                    } else if (wvPts == 50) {

                        {
                            waveLoadingView.setBottomTitle("");
                            waveLoadingView.setTopTitle("");
                            waveLoadingView.setCenterTitle(String.format("%d%%", Math.round(wvPts)));

                        }


                    } else if (wvPts > 50 && wvPts <= 100) {

                        waveLoadingView.setBottomTitle("");
                        waveLoadingView.setTopTitle(String.format("%d%%", Math.round(wvPts)));
                        waveLoadingView.setCenterTitle("");

                    } else {
                        Snackbar.make(view, "You have reached the goal!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
            }

        });

        mEnhacedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Enhance.isOfferwallReady()) {
                    Enhance.showOfferwall();
                }else{
                    Snackbar.make(view,"No offers are available!",Snackbar.LENGTH_SHORT)
                            .setAction("Action",null).show();
                }

            }
        });
        mRewardedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE));
//                } else {
//                    vibrator.vibrate(70);
//                }
                if (Enhance.isRewardedAdReady()) {
                    // The ad is ready, show it
                    Enhance.showRewardedAd(new Enhance.RewardCallback() {
                        // Callbacks:
                        @Override
                        public void onRewardGranted(int rewardValue, Enhance.RewardType rewardType) {
                            if (rewardType == Enhance.RewardType.ITEM) {
                                Toast.makeText(context, "Reward granted(item)", Toast.LENGTH_SHORT).show();
                            } else if (rewardType == Enhance.RewardType.COINS) {
                                mmedia = MediaPlayer.create(context, R.raw.water);
                                mmedia.start();
                                Toast.makeText(context, "Reward points : " + rewardValue, Toast.LENGTH_SHORT).show();

                                SharedPreferences pref = context.getSharedPreferences("SAVING", Context.MODE_PRIVATE);
                                setpts = pref.getString("mypoints", "0");
                                if (Double.valueOf(points.getText().toString()) >= 4.4) {
                                    Snackbar.make(view, "You have reached the goal!", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                } else {
                                    double mset = Double.valueOf(points.getText().toString()) + rewardValue * 0.00088;
                                    points.setText(String.valueOf(mset));
                                    SharedPreferences sharedPreferences = context.getSharedPreferences("SAVING", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edt = sharedPreferences.edit();
                                    edt.putString(encrypt("mypoints"), encrypt(points.getText().toString()));
                                    edt.apply();
                                    id(context);
                                    //remeber to take 2 zeroes back in this calculation!!
                                    a = (rewardValue * 0.00088) / (0.044);
                                    progress(a);
                                    //rwrd(Integer.valueOf(points.getText().toString()));
                                    radpts = radpts + (rewardValue * 0.00088) / (0.044);
                                    SharedPreferences rwpref = context.getSharedPreferences("RADS", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edtrw = rwpref.edit();
                                    edtrw.putString(encrypt("Radpoints"), encrypt(String.valueOf(radpts)));
                                    edtrw.apply();
                                    if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
                                        FirebaseDatabase.getInstance().getReference("MyUsers")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                                .child(uniqueID).child(androidId).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(String.valueOf(mset));

                                        FirebaseDatabase.getInstance().getReference("MyUsers")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                                .child(uniqueID).child(androidId).child("Rewarded")
                                                .setValue(radpts);
                                    } else {
                                        FirebaseDatabase.getInstance().getReference("MyUsers")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                                .child(uniqueID).child(androidId).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(String.valueOf(mset));

                                        FirebaseDatabase.getInstance().getReference("MyUsers")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                                .child(uniqueID).child(androidId).child("Rewarded")
                                                .setValue(radpts);
                                    }

                                }
                            }
                        }

                        @Override
                        public void onRewardDeclined() {
                            Toast.makeText(context, "Reward declined!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardUnavailable() {
                            Toast.makeText(context, "Reward unavailable :(", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    nointernet();
                }


            }
        });


        points = context.findViewById(R.id.tvpts);

        //Reading main points
        SharedPreferences pref = context.getSharedPreferences("SAVING", Context.MODE_PRIVATE);
        setpts = decrypt(pref.getString(encrypt("mypoints"), encrypt("0")));
        points.setText(setpts);

        //Reading wave data
        SharedPreferences prf = context.getSharedPreferences("WAVE", Context.MODE_PRIVATE);
        String wvpnts = decrypt(prf.getString(encrypt("wavedata"), encrypt("0")));
        wvPts = Double.valueOf(wvpnts);

        //reading clicks ads data
        SharedPreferences clickp = context.getSharedPreferences("PTS", Context.MODE_PRIVATE);
        pts = Double.valueOf(decrypt(clickp.getString(encrypt("clickpoints"), encrypt("0"))));

        //reading offer ads data
        SharedPreferences rad = context.getSharedPreferences("ADS", Context.MODE_PRIVATE);
        adpts = Double.valueOf(decrypt(rad.getString(encrypt("adpoints"), encrypt("0"))));

        //reading rewarded data
        SharedPreferences rwad = context.getSharedPreferences("RADS", Context.MODE_PRIVATE);
        radpts = Double.valueOf(decrypt(rwad.getString(encrypt("Radpoints"), encrypt("0"))));

        //reading daily data
        SharedPreferences daly = context.getSharedPreferences("DAILY", Context.MODE_PRIVATE);
        dailyperc = Double.valueOf(decrypt(daly.getString(encrypt("dailypoints"), encrypt("0"))));


    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public void nointernet() {

        MediaPlayer newmedia = MediaPlayer.create(context, R.raw.nointernet);
        newmedia.start();
        final AlertDialog malertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view3 = layoutInflater.inflate(R.layout.activity_alert, null);
        malertDialog.setCancelable(false);

        malertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        malertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!isOnline()) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(HomeFragmnet.this).attach(HomeFragmnet.this).commit();
                } else {
                    malertDialog.cancel();
                    dailyreward();
                }
            }
        });
        malertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlide;
        malertDialog.setView(view3);
        malertDialog.show();
    }

    public void dailyreward() {
        //Daily reward
        if (!currentday && isOnline()) {
            MediaPlayer mediaPlayer1 = MediaPlayer.create(context, R.raw.notifydaily);
            mediaPlayer1.start();
            mimage = context.findViewById(R.id.gift);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view2 = layoutInflater.inflate(R.layout.daily, null);
            alertDialog.setCancelable(false);
            Button btncol = view2.findViewById(R.id.collect);
            btncol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOnline()) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.collect);
                        mediaPlayer.start();
                        daily = Double.valueOf(points.getText().toString()) + 0.0176;
                        points.setText(String.valueOf(daily));
                        progress(0.4);
                        //writing
                        SharedPreferences sharedPreferences = context.getSharedPreferences("SAVING", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edt = sharedPreferences.edit();
                        edt.putString(encrypt("mypoints"), encrypt(points.getText().toString()));
                        edt.apply();
                        dailyperc= dailyperc + 0.000352;
                        //saving daily points
                        SharedPreferences dailywala = context.getSharedPreferences("DAILY", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editdaily = dailywala.edit();
                        editdaily.putString(encrypt("dailypoints"), encrypt(String.valueOf(dailyperc)));
                        editdaily.apply();

                        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
                            FirebaseDatabase.getInstance().getReference("MyUsers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                    .child(androidId).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(daily);

                            FirebaseDatabase.getInstance().getReference("MyUsers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                    .child(androidId).child("daily")
                                    .setValue(dailyperc);
                        } else {
                            FirebaseDatabase.getInstance().getReference("MyUsers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                    .child(androidId).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(String.valueOf(daily));

                            FirebaseDatabase.getInstance().getReference("MyUsers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                                    .child(androidId).child("daily")
                                    .setValue(dailyperc);
                        }
                        SharedPreferences.Editor timedaily = timepref.edit();
                        timedaily.putBoolean(todaystring, true);
                        timedaily.apply();
                        alertDialog.cancel();
                    } else {
                        nointernet();
                        alertDialog.cancel();
                    }
                }
            });
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlide;
            alertDialog.setView(view2);
            alertDialog.show();

        } else if (!isOnline()) {
            nointernet();
        } else {
            Toast.makeText(context, "Your daily reward is over!", Toast.LENGTH_SHORT).show();
        }
    }
//    //invite part
//    private void onInviteClicked() {
//        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
//                .setMessage(getString(R.string.invitation_message))
//                .build();
//        startActivityForResult(intent, REQUEST_INVITE);
//    }

public synchronized static String id(Context context) {
    if (uniqueID == null) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(PREF_UNIQUE_ID, uniqueID);
            editor.commit();
        }
    }
    return uniqueID;
}

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
//
//        if (requestCode == REQUEST_INVITE) {
//            if (resultCode == RESULT_OK) {
//                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
//                for (String id : ids) {
//                    Log.d(TAG, "onActivityResult: sent invitation" + id);
//                    Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
//                    invitbtn.setEnabled(false);
//
//                }
//            } else {
//                currentinviteday = mtimepref.getBoolean(todaystring, false);
//                invitbtn.setEnabled(true);
//                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
public static String encrypt(String input) {
    return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
}

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static boolean isZoneAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }
    public void progress(double rwrd) {
        if (Double.valueOf(points.getText().toString()) >= 4.4) {
            Toast.makeText(context, "You have reached the goal!!", Toast.LENGTH_SHORT).show();
        }
        wvPts = wvPts + rwrd;
        Toast.makeText(context, String.valueOf(wvPts), Toast.LENGTH_SHORT).show();
        //overriding the updated value of wvpts.
        SharedPreferences newPref = context.getSharedPreferences("WAVE", Context.MODE_PRIVATE);
        SharedPreferences.Editor newEdit = newPref.edit();
        newEdit.putString(encrypt("wavedata"), encrypt(String.valueOf(wvPts)));
        newEdit.apply();
        waveLoadingView.setProgressValue((int) (wvPts));
        if (wvPts < 50) {
            waveLoadingView.setBottomTitle(String.format("%d%%", Math.round(wvPts)));
            waveLoadingView.setTopTitle("");
            waveLoadingView.setCenterTitle("");

        } else if (wvPts == 50) {

            {
                waveLoadingView.setBottomTitle("");
                waveLoadingView.setTopTitle("");
                waveLoadingView.setCenterTitle(String.format("%d%%", Math.round(wvPts)));

            }
        } else if (wvPts > 50 && wvPts <= 100) {

            waveLoadingView.setBottomTitle("");
            waveLoadingView.setTopTitle(String.format("%d%%", Math.round(wvPts)));
            waveLoadingView.setCenterTitle("");

        } else {
            Toast.makeText(context, "Congo!! You have reached the goal !", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("/6499/example/rewarded-video",
                new PublisherAdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
       loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(context,"reward is : " + rewardItem,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }


    @Override
    public void onResume() {
        mRewardedVideoAd.resume(context);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(context);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(context);
        super.onDestroy();
    }
}
