package com.example.naresh.demoproject_1.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.naresh.demoproject_1.MainActivity;
import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.adapters.QuestionAdapter;
import com.example.naresh.demoproject_1.adapters.UserAdapter;
import com.example.naresh.demoproject_1.models.QuestionAnswerResponse;
import com.example.naresh.demoproject_1.models.QuestionItem;
import com.example.naresh.demoproject_1.utils.Constants;
import com.example.naresh.demoproject_1.utils.JSONParser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class ProfileFragment extends Fragment {
    private String TAG = ProfileFragment.class.getSimpleName();
    private View rootView, mFooterView;
    private QuestionAdapter questionAdapter;
    private int userId;
    private ListView mListViewProfileFragment;
    private TextView mTextPleaseWait;
    private ProgressBar mProgressBar;
    private static final String ARG_USER_ID = "user_id";

    private int mQuestionPageCount = 0;
    public boolean isLoading = false;


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
        mListViewProfileFragment = (ListView) rootView.findViewById(R.id.list_question_profile_fragment);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_profile_fragment);
        mTextPleaseWait = (TextView) rootView.findViewById(R.id.text_loading);

        mFooterView = inflater.inflate(R.layout.listview_footer, null, false);
        mListViewProfileFragment.addFooterView(mFooterView);
        mFooterView.setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        userId = bundle.getInt("user_id");


        // new getQuestionAnswerDetailFromJson().execute();

        mListViewProfileFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                QuestionItem item = questionAdapter.getItem(position);
                String openLink = item.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(openLink));
                startActivity(intent);
            }
        });

        mListViewProfileFragment.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;

                String last = String.valueOf(lastInScreen);
                String first = String.valueOf(firstVisibleItem);
                String visible = String.valueOf(visibleItemCount);

                Log.e(TAG, "pro fragment FIRST VISIBLE ITEM   :" + last);
                Log.e(TAG, "pro fragment VISIBLE ITEM  Count  :" + first);
                Log.e(TAG, "pro fragment Total ITEM  Count  :" + visible);

                if ((lastInScreen == totalItemCount) && (totalItemCount - 1 != 0)) {
                    if (!isLoading) {
                        Log.e("ERR -:", "Scrolling List view");
                        mQuestionPageCount++;
                        new getQuestionAnswerDetailFromJson().execute();

                    }
                }
            }
        });

        //SETTING ADAPTER ...

        questionAdapter = new QuestionAdapter(getActivity());
        mListViewProfileFragment.setAdapter(questionAdapter);

        return rootView;
    }

    private class getQuestionAnswerDetailFromJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
            // showProgressBar();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                Uri.Builder uriBuilder = Uri.parse(Constants.BASE_URL).buildUpon()
                        .appendPath("2.2")
                        .appendPath("users")
                        .appendPath(String.valueOf(userId))
                        .appendPath("posts")
                        .appendQueryParameter("page", String.valueOf(mQuestionPageCount))
                        .appendQueryParameter("order", Constants.ORDER_DESC)
                        .appendQueryParameter("sort", Constants.POST_SORT_ACTIVITY)
                        .appendQueryParameter("site", Constants.SITE)
                        .appendQueryParameter("filter", Constants.VALUE_POST_TYPE_FILTER);


                String questionListUrl = uriBuilder.build().toString();
                Log.e(TAG, "::::--FULL  URL :::-- " + uriBuilder);

                URL url = new URL(questionListUrl);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                response = stringBuffer.toString();

            } catch (Exception ex) {

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            isLoading = false;
            if (s != null) {
                Gson gson = new Gson();
                QuestionAnswerResponse userResponse = gson.fromJson(s, QuestionAnswerResponse.class);
                questionAdapter.addItems(userResponse.getItems());
            } else {
                Log.d(TAG, "Failed to Load JSON response");
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void hideProgressBar() {
        if (mQuestionPageCount == 1) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            mFooterView.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (mQuestionPageCount == 1) {
            mProgressBar.setVisibility(View.VISIBLE);
            mFooterView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mFooterView.setVisibility(View.VISIBLE);
        }
    }

}

/*



 */
/*
private class getQuestionAnswerDetailFromJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
           // showProgressBar();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                Uri.Builder uriBuilder = Uri.parse(Constants.BASE_URL).buildUpon()
                        .appendPath("2.2")
                        .appendPath("posts")
                        .appendPath(String.valueOf(userId))
                        .appendQueryParameter("order", Constants.ORDER_DESC)
                        .appendQueryParameter("sort", Constants.POST_SORT_ACTIVITY)
                        .appendQueryParameter("site", Constants.SITE)
                        .appendQueryParameter("filter", Constants.VALUE_POST_TYPE_FILTER);


                String questionListUrl = uriBuilder.build().toString();

                URL url = new URL(questionListUrl);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                response = stringBuffer.toString();

            } catch (Exception ex) {

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            isLoading = false;
            if (s != null) {
                Gson gson = new Gson();
                    QuestionAnswerResponse userResponse = gson.fromJson(s, QuestionAnswerResponse.class);
                     questionAdapter.addItems(userResponse.getItems());
            }
            else {
                Log.d(TAG,"Failed to Load JSON response");
            }
        }
    }

 */