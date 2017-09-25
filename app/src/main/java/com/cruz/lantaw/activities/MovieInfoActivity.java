package com.cruz.lantaw.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cruz.lantaw.R;
import com.cruz.lantaw.Singleton.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView mTvTitle;
    private TextView mTvCast;
    private RatingBar mRbRatingBar;
    private TextView mTvInfo;
    private TextView mTvsypno;
    private ImageView mImgImage;
    private ImageView mImgPlay;


    public static final String TAG = "movieinfoactivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id= getIntent().getStringExtra("id");

        setContentView(R.layout.activity_movie_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        volleyStringRequst("https://api.cinepass.de/v4/movies/"+id+"/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");

        Log.e(TAG, "onCreate: " + id );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void volleyStringRequst(String url){
        ProgressDialog dialog = new ProgressDialog(MovieInfoActivity.this);
        dialog.setMessage("Your message..");
        dialog.show();

        this.mTvTitle = (TextView)findViewById(R.id.movieTitle);
        this.mTvCast = (TextView)findViewById(R.id.movieCast);
        this.mRbRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        this.mTvInfo = (TextView)findViewById(R.id.infoTxt);
        this.mTvsypno = (TextView)findViewById(R.id.synopsisTxt);
        this.mImgImage = (ImageView) findViewById(R.id.movieImg);
        this.mImgPlay = (ImageView) findViewById(R.id.playBtn);

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());



                try {
                    JSONObject obj = response.getJSONObject("movie");
                    String title = obj.getString("original_title");
                    String synopsis = obj.getString("synopsis");
                    String runtime = obj.getString("runtime");
                    String poster_image_thumbnail = obj.getString("poster_image_thumbnail");




//                    JSONObject age_limits = obj.getJSONObject("age_limits");
//                    int DE = age_limits.getInt("SE");

                    JSONObject release_dates = obj.getJSONObject("release_dates");
                    JSONArray PT = release_dates.getJSONArray("PT");
                    if (PT.length() == 0){
                        PT = release_dates.getJSONArray("IT");
                        if (PT.length() == 0){
                            PT = release_dates.getJSONArray("DK");
                            if (PT.length() == 0){
                                PT = release_dates.getJSONArray("AE");
                                if (PT.length() == 0){
                                    PT = release_dates.getJSONArray("DE");
                                    if (PT.length() == 0){
                                        PT = release_dates.getJSONArray("BR");
                                        if (PT.length() == 0){
                                            PT = release_dates.getJSONArray("UA");
                                            if (PT.length() == 0){
                                                PT = release_dates.getJSONArray("ES");
                                                if (PT.length() == 0){
                                                    PT = release_dates.getJSONArray("US");
                                                    if (PT.length() == 0){
                                                        PT = release_dates.getJSONArray("CH");
                                                        if (PT.length() == 0){
                                                            PT = release_dates.getJSONArray("SE");
                                                            if (PT.length() == 0){
                                                                PT = release_dates.getJSONArray("GB");
                                                                if (PT.length() == 0){
                                                                    PT = release_dates.getJSONArray("RU");
                                                                    if (PT.length() == 0){
                                                                        PT = release_dates.getJSONArray("FR");
                                                                        if (PT.length() == 0){
                                                                            PT = release_dates.getJSONArray("IE");
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    JSONObject dateT = PT.getJSONObject(0);
                    String date = dateT.getString("date");

                    JSONArray crew = obj.getJSONArray("crew");
                    JSONObject crewT = crew.getJSONObject(0);
                    String director = crewT.getString("name");

                    JSONArray genres = obj.getJSONArray("genres");

                    JSONArray cast = obj.getJSONArray("cast");

                    JSONArray trailers = obj.getJSONArray("trailers");

                    JSONObject jsonObjectTrailer = trailers.getJSONObject(0);

                    JSONArray trailer_files = jsonObjectTrailer.getJSONArray("trailer_files");
                    JSONObject jsonObjecttrailer_files = trailer_files.getJSONObject(0);

                    final String url = jsonObjecttrailer_files.getString("url");

                    mImgPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        }
                    });

                    mTvCast.setText("Starring: ");
                    for (int i = 0; i < 3; i++) {

                        JSONObject jsonObject = cast.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        mTvCast.append(" ("+name+")");
                    }

                    mTvTitle.setText(title);

                    mTvInfo.setText("Runtime: "+runtime+" mins\n"+
//                                    "Age Limit: " + DE +"\n"+
                                    "Release Date: "+date+"\n"+"Genre: ");

                    for (int i = 0; i < genres.length(); i++) {

                        JSONObject genresT = genres.getJSONObject(i);
                        String genresr = genresT.getString("name");
                        mTvInfo.append(genresr+"  ");
                    }

                    mTvInfo.append("\n"+"Director: "+director);

                    mTvsypno.setText(synopsis);
                    Glide.with(getApplicationContext()).load(poster_image_thumbnail).into(mImgImage);

//                    Log.d(TAG, movies[0]);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                try {
                    JSONObject obj = response.getJSONObject("movie");

                    JSONObject ratings = obj.getJSONObject("ratings");
                    JSONObject imdb = ratings.getJSONObject("imdb");

                    int vote_count = imdb.getInt("vote_count");
                    mRbRatingBar.setRating(Float.parseFloat(vote_count+""));
                } catch (JSONException e) {
                    e.printStackTrace();
                    mRbRatingBar.setRating(Float.parseFloat(0+""));
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
        dialog.dismiss();
    }


}
