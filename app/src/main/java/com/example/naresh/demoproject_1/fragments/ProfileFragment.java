package com.example.naresh.demoproject_1.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.naresh.demoproject_1.R;


public class ProfileFragment extends Fragment {
    private View rootView;

    private int userId;
    private TextView profileUID;
    private static final String ARG_USER_ID = "user_id";


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance(int userId) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_USER_ID, userId);
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        profileUID = (TextView) rootView.findViewById(R.id.tv_profile_fragment);

        Bundle bundle = getArguments();
        int UID = bundle.getInt("user_id");
        String userID = String.valueOf(UID);
         profileUID.setText(userID);

        return rootView;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
