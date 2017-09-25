package com.cruz.lantaw.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cruz.lantaw.R;
import com.cruz.lantaw.Singleton.AppSingleton;
import com.cruz.lantaw.fragments.SavedFragment;
import com.cruz.lantaw.fragments.ShowingFragment;
import com.cruz.lantaw.fragments.UpcomingFragment;
import com.qslll.library.fragments.ExpandingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ExpandingFragment.OnExpandingClickListener {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frame;
    public static final String TAG = "movies";
    private String movies[] = new String[99999];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volleyStringRequst("https://api.cinepass.de/v4/movies/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");

        findViews();
        initToolbar();
        initBottomNavigation();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame,new SavedFragment())
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
                        SavedFragment savedFragment = new SavedFragment();
                        savedFragment.setMovies(movies);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame,new SavedFragment())
                                .commit();
                        return true;
                    case R.id.action_upcoming:
                        UpcomingFragment upcomingFragment = new UpcomingFragment();
                        upcomingFragment.setMovies(movies);
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

    public void volleyStringRequst(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());



                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONArray obj = response.getJSONArray("movies");
                    for (int i = 0; i < obj.length(); i++) {

                        JSONObject jsonObject = obj.getJSONObject(i);
                        String slug = jsonObject.getString("slug");
                        String title = jsonObject.getString("title");
                        String poster_image_thumbnail = jsonObject.getString("poster_image_thumbnail");
                        poster_image_thumbnail = poster_image_thumbnail.replace("http", "https");
                        String id = jsonObject.getString("id");

                        movies[i] = poster_image_thumbnail;


                    }

                    setMovies(movies);

                    Log.d(TAG, movies[0]);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
    }

    public String[] getMovies() {
        return movies;
    }

    public void setMovies(String[] movies) {
        this.movies = movies;
    }
}
