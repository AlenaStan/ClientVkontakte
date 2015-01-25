package com.github.alenastan.clientvkontakte.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.alenastan.clientvkontakte.MainActivity;
import com.github.alenastan.clientvkontakte.R;

/**
 * Created by lena on 25.01.2015.
 */
public class FragmentWall extends Fragment {

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static FragmentWall newInstance() {
        FragmentWall fragment = new FragmentWall();
        return fragment;
    }

    public FragmentWall() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wall, container,
                false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }

}