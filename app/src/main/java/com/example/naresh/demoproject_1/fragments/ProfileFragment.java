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

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.SessionManager;
import com.example.naresh.demoproject_1.adapters.QuestionAdapter;
import com.example.naresh.demoproject_1.models.QuestionAnswerResponse;
import com.example.naresh.demoproject_1.models.QuestionItem;
import com.example.naresh.demoproject_1.utils.Constants;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class ProfileFragment extends Fragment {
    private String TAG = ProfileFragment.class.getSimpleName();
    private View mFooterView;
    private QuestionAdapter questionAdapter;
    private ListView mListViewProfileFragment;
    private TextView mTextPleaseWait, mTextDetailNotFound;
    private ProgressBar mProgressBar;
    private int userId;
    private int mQuestionPageCount = 1;
    public boolean isLoading = false;
    public boolean hasMoreData = true;
    private static final String ARG_USER_ID = "user_id";

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
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mListViewProfileFragment = (ListView) rootView.findViewById(R.id.list_question_profile_fragment);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_profile_fragment);
        mTextPleaseWait = (TextView) rootView.findViewById(R.id.text_loading_site_list);
        mTextDetailNotFound = (TextView) rootView.findViewById(R.id.text_detail_not_available);
        mFooterView = inflater.inflate(R.layout.listview_footer, null, false);
        mListViewProfileFragment.addFooterView(mFooterView);
        mFooterView.setVisibility(View.INVISIBLE);

        Bundle bundle = getArguments();
        userId = bundle.getInt("user_id");

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
                if (hasMoreData) {
                    if ((lastInScreen == totalItemCount) && (totalItemCount - 1 != 0)) {
                        if (!isLoading) {
                            Log.e("ERROR -:", "Scrolling List view");
                            mQuestionPageCount++;
                            new getQuestionAnswerDetailFromJson().execute();
                        }
                    }
                } else {
                    hideProgressBar();
                }
            }
        });
        //Set Adapter here
        questionAdapter = new QuestionAdapter(getActivity());
        mListViewProfileFragment.setAdapter(questionAdapter);
        return rootView;
    }

    private class getQuestionAnswerDetailFromJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
            showProgressBar();
        }

        @Override
        protected String doInBackground(Void... params) {

            String site = SessionManager.getInstance(getActivity()).getApiSiteParameter();
            String response = null;
            URLConnection urlConn;
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
                        .appendQueryParameter("site", site)
                        .appendQueryParameter("filter", Constants.VALUE_POST_TYPE_FILTER);

                String questionListUrl = uriBuilder.build().toString();
                Log.e(TAG, ":--FULL  URL :-- " + uriBuilder);

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
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            isLoading = false;
            hideProgressBar();
            mFooterView.setVisibility(View.GONE);

            if (data != null) {
                Gson gson = new Gson();
                QuestionAnswerResponse userResponse = gson.fromJson(data, QuestionAnswerResponse.class);

                hasMoreData = userResponse.isHasMore();
                Log.e(TAG, "Has More : " + hasMoreData);
                questionAdapter.addItems(userResponse.getItems());

                if (questionAdapter.getCount() == 0) {
                    mTextDetailNotFound.setVisibility(View.VISIBLE);
                    mListViewProfileFragment.setVisibility(View.GONE);
                }
            } else {
                Log.d(TAG, "Failed to Load JSON response");
            }
        }
    }

    private void hideProgressBar() {
        if (mQuestionPageCount == 1) {
            mProgressBar.setVisibility(View.GONE);
            mTextPleaseWait.setVisibility(View.GONE);
        } else {
            mFooterView.setVisibility(View.GONE);
        }
    }
    private void showProgressBar() {
        if (mQuestionPageCount == 1) {
            mProgressBar.setVisibility(View.VISIBLE);
            mTextPleaseWait.setVisibility(View.VISIBLE);
            mFooterView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTextPleaseWait.setVisibility(View.GONE);
            mFooterView.setVisibility(View.VISIBLE);
        }
    }

}
