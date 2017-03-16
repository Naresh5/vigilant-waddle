package com.example.naresh.demoproject_1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.models.User;


public class ActivityFragment extends Fragment {
    private View rootView;
    private TextView actiityUID;
    private User user;

    private static final String ARG_USER_ID = "user_id";


    public ActivityFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance(int key) {

        ActivityFragment activityFragment = new ActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_USER_ID, key);
        activityFragment.setArguments(bundle);
        return activityFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_activity, container, false);
        actiityUID = (TextView) rootView.findViewById(R.id.tv_activity_fragment);


        Bundle bundle = getArguments();

        int UID = bundle.getInt("user_id");

        String userID = String.valueOf(UID);

        actiityUID.setText(userID);

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
