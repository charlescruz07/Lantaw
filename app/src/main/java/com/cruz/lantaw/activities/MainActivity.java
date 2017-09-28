package com.cruz.lantaw.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.qslll.library.fragments.ExpandingFragment;

public class MainActivity extends AppCompatActivity implements ExpandingFragment.OnExpandingClickListener {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initToolbar();
        initBottomNavigation();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame,new UpcomingFragment())
                .commit();
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
}
