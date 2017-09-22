package com.cruz.lantaw.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cruz.lantaw.R;
import com.cruz.lantaw.activities.MovieInfoActivity;
import com.cruz.lantaw.adapters.GridAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {


    private int movies[] = {
            R.drawable.movie_9,
            R.drawable.movie_8,
            R.drawable.movie_7,
            R.drawable.movie_6,
            R.drawable.movie_5,
            R.drawable.movie_4,
            R.drawable.movie_3,
            R.drawable.movie_2,
            R.drawable.movie_1,
            R.drawable.movie_9,
            R.drawable.movie_8,
            R.drawable.movie_7,
            R.drawable.movie_6,
            R.drawable.movie_5,
            R.drawable.movie_4,
            R.drawable.movie_3,
            R.drawable.movie_2,
            R.drawable.movie_1
    };

    private View rootView;
    private GridView gridView;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upcoming, container, false);

        gridView = rootView.findViewById(R.id.gridView);
        GridAdapter adapter = new GridAdapter(movies,getContext());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), MovieInfoActivity.class));
            }
        });

        return rootView;
    }

}
