package com.cruz.lantaw.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cruz.lantaw.R;
import com.cruz.lantaw.Singleton.AppSingleton;
import com.cruz.lantaw.adapters.GridAdapter;
import com.cruz.lantaw.models.Users;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChooseMovie extends AppCompatActivity {
    private ProgressBar mProgressDialog;

    private String[] listItemsFirstRow = {"item 1", "item 2", "item 3"};
    String movies[];
    String ids[];
    String slugs[];
    String titles[];
    GridAdapter adapter;
    //    int page = 0;
    String poster_image_thumbnails[];

    public static final String TAG = "movies";

    private View rootView;
    private GridView gridView;
    private static final int RC_SIGN_IN = 007;
    Firebase reference1, reference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_movie);

        ProgressDialog dialog=new ProgressDialog(ChooseMovie.this);
        dialog.setMessage("Loading Movies...");
        dialog.setCancelable(false);
        dialog.show();
        mProgressDialog = rootView.findViewById(R.id.progressBar);

        volleyStringRequst("https://api.cinepass.de/v4/movies/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");

        gridView = rootView.findViewById(R.id.gridView);
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://android-chat-app-e711d.firebaseio.com/messages/" + Users.username + "_" + Users.chatWith);
        reference2 = new Firebase("https://android-chat-app-e711d.firebaseio.com/messages/" + Users.chatWith + "_" + Users.username);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if(!isNetworkAvailable()){
                    Toast.makeText(ChooseMovie.this, "Network is not enabled!", Toast.LENGTH_SHORT).show();
                } else {

                    String messageText = ids[i].getBytes().toString();

                    if(!messageText.equals("")){
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("message", messageText);
                        map.put("user", Users.username);
                        reference1.push().setValue(map);
                        reference2.push().setValue(map);
                    }
                    Toast.makeText(ChooseMovie.this, "Movie Recommendation Send!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(Users.username)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(Users.chatWith + ":-\n" + message, 2);
                    Intent intent = new Intent(getBaseContext(), MovieInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id",message);
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(getApplication())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Recommendation Notification")
                                    .setContentText("You've got a movie recommendation");
                    Intent notificationIntent = new Intent(getApplication(), MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplication(), 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        dialog.hide();
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChooseMovie.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            textView.setBackgroundResource(R.drawable.rounded_corner2);
            Intent intent = new Intent(this, MovieInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("id",message);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Recommendation Notification")
                            .setContentText("You've got a movie recommendation");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }

    }

    public void volleyStringRequst(String url){
        mProgressDialog.setVisibility(View.VISIBLE);


        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
//        ProgressDialog progressDialog = new ProgressDialog(getContext());;
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
                    movies = new String[obj.length()];
                    ids = new String[obj.length()];
                    slugs = new String[obj.length()];
                    titles = new String[obj.length()];
                    poster_image_thumbnails = new String[obj.length()];

                    for (int i = 0; i < obj.length(); i++) {

                        JSONObject jsonObject = obj.getJSONObject(i);
                        String slug = jsonObject.getString("slug");
                        String title = jsonObject.getString("title");
                        String poster_image_thumbnail = jsonObject.getString("poster_image_thumbnail");
                        poster_image_thumbnail = poster_image_thumbnail.replace("http", "https");
                        String id = jsonObject.getString("id");

                        movies[i] = poster_image_thumbnail;
                        ids[i] = id;
                        slugs[i] = slug;
                        titles[i] = title;
                        poster_image_thumbnails[i] = poster_image_thumbnail;

                    }
//                    adapter = new GridAdapter(movies,getContext());
                    adapter = new GridAdapter(movies,ChooseMovie.this);
                    gridView.setAdapter(adapter);


                    Log.d(TAG, movies[1]);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ChooseMovie.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "Network is too slow!", Toast.LENGTH_SHORT).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(ChooseMovie.this).addToRequestQueue(jsonObjReq, REQUEST_TAG);
        mProgressDialog.setVisibility(View.GONE);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ChooseMovie.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
