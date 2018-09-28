package com.mazerapp.moviecatalogapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mazerapp.moviecatalogapp.R;

import org.jetbrains.annotations.NotNull;


public class NoConnectionFragment extends Fragment {

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_connection, container, false);
    }


}
