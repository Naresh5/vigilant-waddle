package com.example.naresh.demoproject_1.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.adapters.QuestionAdapter;
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
    private View rootView;
    private QuestionAdapter questionAdapter;
    private int userId;
    private ListView mListviewProfileFragment;
    private TextView mTextPleaseWait;
    private ProgressBar mProgressBar;
    private static final String ARG_USER_ID = "user_id";
//
    private View footerView;
    private int mQuestionPageCount = 1;
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
        mListviewProfileFragment = (ListView) rootView.findViewById(R.id.list_question_profile_fragment);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_profile_fragment);
        mTextPleaseWait = (TextView) rootView.findViewById(R.id.text_loading);

        Bundle bundle = getArguments();
        userId = bundle.getInt("user_id");

        //SETTing ADAPTER ...

        questionAdapter = new QuestionAdapter(getActivity());
        mListviewProfileFragment.setAdapter(questionAdapter);

        new getQuestionAnswerDetailFromJson().execute();

       /* mListviewProfileFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                QuestionItem item = questionAdapter.getItem(position);
                String openLink = item.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(openLink));
                startActivity(intent);
            }
        });*/

        return rootView;
    }

    public class getQuestionAnswerDetailFromJson extends AsyncTask<Object, Object, List<QuestionItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<QuestionItem> doInBackground(Object... arg0) {

            JSONParser sh = new JSONParser();    //Request to url and getting response
            String jsonStr = null;
            try {
                Uri.Builder uriBuilder = Uri.parse(Constants.BASE_URL).buildUpon()
                        .appendPath("2.2")
                        .appendPath("users")
                        .appendPath(String.valueOf(userId))
                        .appendPath("posts")
                        .appendQueryParameter("order", Constants.ORDER_DESC)
                        .appendQueryParameter("sort", Constants.POST_SORT_ACTIVITY)
                        .appendQueryParameter("site", Constants.SITE)
                        .appendQueryParameter("filter", Constants.VALUE_POST_TYPE_FILTER);

                //https://api.stackexchange.com/2.2/users/22656/posts?
                // order=desc&sort=activity&site=stackoverflow&filter=!)s6)1vyI2*_QmCjnp.fl

                Log.e(TAG, "User ID :-"+String.valueOf(userId));

                Uri uri = uriBuilder.build();

                jsonStr = sh.makeServiceCall(uri.toString());

                Log.e(TAG, "JSON RES for Profile fragment" + jsonStr);
                Log.e(TAG, "URL : " + uri);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("ERROR OCCURRED", "FAILED TO LOAD Json");
            }

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                Gson gson = new Gson();
                QuestionAnswerResponse response = gson.fromJson(jsonStr, QuestionAnswerResponse.class);
                return response.getItems();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<QuestionItem> userList) {
            super.onPostExecute(userList);

            if (userList != null) {

                questionAdapter.addItems(userList);

            } else {
                Log.d(TAG, "Failed to set up Data with Adapter");
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