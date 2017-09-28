package com.cruz.lantaw.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.cruz.lantaw.R;
import com.cruz.lantaw.adapters.ReviewAdapter;
import com.cruz.lantaw.models.Review;

import java.util.ArrayList;


public class ReviewFragment extends DialogFragment {

    private ArrayList<Review> reviews;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ImageButton btnAdd;
    private EditText etTxt;
    private ProgressBar progressBar;
    private View rootView;

    public void findViews(){
        reviews = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        btnAdd = rootView.findViewById(R.id.btnAdd);
        etTxt = rootView.findViewById(R.id.etText);
        progressBar = rootView.findViewById(R.id.progressBar);
    }

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_review, container, false);
        findViews();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Shit");

        // Show soft keyboard automatically and request focus to field
//        mEditText = (EditText) view.findViewById(R.id.etText);
//        mEditText.requestFocus();

//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        reviews = getReviews();
        adapter = new ReviewAdapter(rootView.getContext(),reviews);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

    }

    public ArrayList<Review> getReviews(){
        ArrayList<Review> reviews = new ArrayList<>();

        reviews.add(new Review("wala","Charles Cruz","The movie was so good. Oh my god fuck shet","Yesterday"));
        reviews.add(new Review("wala","Charles Cruz","The movie was so good. Oh my god fuck shet","Yesterday"));
        reviews.add(new Review("wala","Charles Cruz","The movie was so good. Oh my god fuck shet","Yesterday"));
        reviews.add(new Review("wala","Charles Cruz","The movie was so good. Oh my god fuck shet","Yesterday"));
        reviews.add(new Review("wala","Charles Cruz","The movie was so good. Oh my god fuck shet","Yesterday"));
        reviews.add(new Review("wala","Charles Cruz","The movie was so good. Oh my god fuck shet","Yesterday"));

        return reviews;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }

}
