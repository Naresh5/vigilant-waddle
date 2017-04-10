package com.example.naresh.demoproject_1.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.naresh.demoproject_1.HtmlImageGetter;
import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.SessionManager;
import com.example.naresh.demoproject_1.models.User;
import com.example.naresh.demoproject_1.models.UserResponse;
import com.example.naresh.demoproject_1.utils.Constants;
import com.example.naresh.demoproject_1.utils.JSONParser;
import com.example.naresh.demoproject_1.utils.Utility;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.util.List;


public class ActivityFragment extends Fragment {
    private String TAG = ActivityFragment.class.getSimpleName();
    private ProgressBar mProgressBar;
    private TextView mTextLoading, mTextAboutUser, mTextAnswerCount, mTextQuestionCount, mTextViewCount;
    private TextView mTextUserLocation, mTextWebSiteURL;
    private LinearLayout mLinerLayout;
    private int userId;
    private static final String ARG_USER_ID = "user_id";

    public static Fragment newInstance(int userId) {
        ActivityFragment activityFragment = new ActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_USER_ID, userId);
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
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_activity, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_activity_fragment);
        mTextLoading = (TextView) rootView.findViewById(R.id.text_loading_site_list);
        mTextAboutUser = (TextView) rootView.findViewById(R.id.text_about_user);
        mTextAnswerCount = (TextView) rootView.findViewById(R.id.text_answer_count);
        mTextQuestionCount = (TextView) rootView.findViewById(R.id.text_question_count);
        mTextViewCount = (TextView) rootView.findViewById(R.id.text_view_counts);
        mTextUserLocation = (TextView) rootView.findViewById(R.id.text_location);
        mTextWebSiteURL = (TextView) rootView.findViewById(R.id.text_website_url);
        mLinerLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout_main);
        mLinerLayout.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        userId = bundle.getInt(ARG_USER_ID);

        new getUserDetailFromJson().execute();
        return rootView;
    }

    private class getUserDetailFromJson extends AsyncTask<Object, Object, List<User>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected List<User> doInBackground(Object... arg0) {

            String site = SessionManager.getInstance(getActivity()).getApiSiteParameter();
            JSONParser sh = new JSONParser();    //Request to url and getting response
            String jsonStr = null;
            try {
                Uri.Builder uriBuilder = Uri.parse(Constants.BASE_URL).buildUpon()
                        .appendPath("2.2")
                        .appendPath("users")
                        .appendPath(String.valueOf(userId))
                        .appendQueryParameter("sort", Constants.SORT_BY_REPUTATION)
                        .appendQueryParameter("site", site)
                        .appendQueryParameter("filter", Constants.VALUE_USER_ACTIVITY_FILTER);

                Uri uri = uriBuilder.build();
                jsonStr = sh.makeServiceCall(uri.toString());
                Log.e(TAG, " Json Response for activity fragment" + jsonStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("ERROR OCCURRED", "FAILED TO LOAD Json");
            }
            Log.e(TAG, "Response from url ACTIVITY FRAGMENT: " + jsonStr);

            if (jsonStr != null) {
                Gson gson = new Gson();
                UserResponse userResponse = gson.fromJson(jsonStr, UserResponse.class);
                return userResponse.getItems();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<User> userList) {
            super.onPostExecute(userList);
            hideProgressBar();
            mLinerLayout.setVisibility(View.VISIBLE);
            if (userList != null && userList.size() > 0) {
                User user = userList.get(0);

                setUserDetail(user);

            } else {
                Log.e(TAG, "Couldn't Fetch Data ... Network Error");
            }
        }
    }

    private void showProgressBar() {
        mTextLoading.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mTextLoading.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void setUserDetail(User user) {

        String aboutMeException = getString(R.string.user_detail_exception_activity_fragment);
        String aboutMe = user.getAboutMe();
        String answers = String.valueOf(user.getAnswerCount());
        String questions = String.valueOf(user.getQuestionCount());
        String views = String.valueOf(user.getViewCount());
        String location = user.getLocation();
        String websiteURL = user.getWebsiteUrl();

        if ((aboutMe==null) || (aboutMe.isEmpty())) {
            mTextAboutUser.setText(aboutMeException);
        } else {
            HtmlImageGetter imageGetter = new HtmlImageGetter(
                    getActivity(),R.drawable.image_html_response_background) {
                @Override
                public void onTextUpdate() {
                    CharSequence sequence = mTextAboutUser.getText();
                    mTextAboutUser.setText(sequence);
                }
            };
            Spanned spannedBody = Utility.convertTextToHTML(aboutMe,imageGetter);
            mTextAboutUser.setText(spannedBody);
        }




       /* if (TextUtils.isEmpty(aboutMe)) {
            mTextAboutUser.setText(aboutMeException);
        } else {
            mTextAboutUser.setText(Utility.convertTextToHTML(aboutMe));
            mTextAboutUser.setMovementMethod(LinkMovementMethod.getInstance());
        }*/

        mTextAnswerCount.setText(answers);
        mTextQuestionCount.setText(questions);
        mTextViewCount.setText(views);

        if (TextUtils.isEmpty(location)) {
            mTextUserLocation.setVisibility(View.GONE);
        } else {
            mTextUserLocation.setText(Utility.convertTextToHTML(location));
            mTextUserLocation.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if (TextUtils.isEmpty(websiteURL)) {
            mTextWebSiteURL.setVisibility(View.GONE);
        } else {
            mTextWebSiteURL.setText(websiteURL);
        }
    }
}

