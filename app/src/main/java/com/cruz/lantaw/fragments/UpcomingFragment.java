package com.cruz.lantaw.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cruz.lantaw.R;
import com.cruz.lantaw.Singleton.AppSingleton;
import com.cruz.lantaw.activities.MovieInfoActivity;
import com.cruz.lantaw.adapters.GridAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {

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

//    private Button nextBtn, prevBtn;
//    Paginator p = new Paginator();
//    private int totalPages;
//    private int currentPage = 0;

    public UpcomingFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ProgressDialog dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Loading Movies...");
        dialog.setCancelable(false);
        dialog.show();
        mProgressDialog = rootView.findViewById(R.id.progressBar);


        volleyStringRequst("https://api.cinepass.de/v4/movies/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");
        rootView = inflater.inflate(R.layout.fragment_upcoming, container, false);



        volleyStringRequst("https://api.cinepass.de/v4/movies/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");

        gridView = rootView.findViewById(R.id.gridView);


//        nextBtn = rootView.findViewById(R.id.nextBtn);
//        prevBtn = rootView.findViewById(R.id.prevBtn);
//
//        prevBtn.setEnabled(false);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if(!isNetworkAvailable()){
                    Toast.makeText(getContext(), "Network is not enabled!", Toast.LENGTH_SHORT).show();
                } else {
                                    Intent intent = new Intent(getActivity(), MovieInfoActivity.class);
                                    intent.putExtra("id", ids[i]);
                                    startActivity(intent);
                }
            }
        });
        dialog.hide();

        return rootView;
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
                    adapter = new GridAdapter(movies,getContext());
                    gridView.setAdapter(adapter);


                    Log.d(TAG, movies[1]);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),
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
        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
        mProgressDialog.setVisibility(View.GONE);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    public boolean isOnline() {
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int     exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        }
//        catch (IOException e)          { e.printStackTrace(); }
//        catch (InterruptedException e) { e.printStackTrace(); }
//
//        return false;
//    }


}
