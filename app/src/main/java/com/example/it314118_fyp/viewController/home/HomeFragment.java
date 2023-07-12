package com.example.it314118_fyp.viewController.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.it314118_fyp.ChatFindActivity;
import com.example.it314118_fyp.posts.*;
import com.example.it314118_fyp.R;
import com.example.it314118_fyp.viewController.main.MainActivityBackup;

public class HomeFragment extends Fragment {

    CardView cv_navTCGDex;
    CardView upPost;
    CardView showPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cv_navTCGDex = view.findViewById(R.id.cv_goTCGDex);
        upPost = view.findViewById(R.id.uploadPost);
        showPost = view.findViewById(R.id.showPost);


        cv_navTCGDex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CardListActivity.class);
                startActivity(intent);
            }
        });

        upPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addpost.class);
                startActivity(intent);
            }
        });

        showPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), showpost.class);
                startActivity(intent);
            }
        });
        return view;
    }
}