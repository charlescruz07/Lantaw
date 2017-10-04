package com.cruz.lantaw.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cruz.lantaw.R;
import com.cruz.lantaw.fragments.SavedFragment;
import com.cruz.lantaw.fragments.ShowingFragment;
import com.cruz.lantaw.fragments.UpcomingFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.qslll.library.fragments.ExpandingFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ExpandingFragment.OnExpandingClickListener,GoogleApiClient.OnConnectionFailedListener  {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frame;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isOnline()){
            Toast.makeText(this, "Network is not enabled!", Toast.LENGTH_SHORT).show();
        }

        findViews();
        initToolbar();
        initBottomNavigation();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame,new UpcomingFragment())
                .commit();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        frame = (FrameLayout) findViewById(R.id.frame);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

    }

    private void initBottomNavigation(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_saved:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame,new SavedFragment())
                                .commit();
                        return true;
                    case R.id.action_upcoming:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame,new UpcomingFragment()
                                )
                                .commit();
                        return true;
                    case R.id.action_showing:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame,new ShowingFragment()
                                )
                                .commit();
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onExpandingClick(View view) {
        startActivity(new Intent(this, MovieInfoActivity.class));
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
