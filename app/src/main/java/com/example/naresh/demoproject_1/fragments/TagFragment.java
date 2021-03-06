package com.example.naresh.demoproject_1.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.SessionManager;
import com.example.naresh.demoproject_1.TagDetailActivity;
import com.example.naresh.demoproject_1.adapters.TagAdapter;
import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.TagItem;
import com.example.naresh.demoproject_1.retrofit.ApiClient;
import com.example.naresh.demoproject_1.retrofit.ApiInterface;
import com.example.naresh.demoproject_1.utils.Constants;
import com.example.naresh.demoproject_1.utils.Utility;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TagFragment extends Fragment {

    private static final String TAG = TagFragment.class.getSimpleName();
    private Spinner spinnerTagSearch;
    private EditText editTagSearch;
    private ListView listTag;
    private TextView textLoading;
    private ProgressBar progressBar;
    private ImageButton imageCancel;
    private View footerView;
    private TagAdapter tagAdapter;
    private int mTagPageCount = 1;
    private String tagSortBy = "popular";
    private String inname = null;
    private String[] tagArray;
    private boolean isTagLoading = false;
    private boolean hasMoreTag = true;

    public static TagFragment newInstance() {
        TagFragment tagFragment = new TagFragment();
        return tagFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tag_fragment_navigation, container, false);
        spinnerTagSearch = (Spinner) rootView.findViewById(R.id.spinner_for_tag_search);
        editTagSearch = (EditText) rootView.findViewById(R.id.edit_tag_search);
        listTag = (ListView) rootView.findViewById(R.id.list_tag);
        textLoading = (TextView) rootView.findViewById(R.id.text_loading_site_list);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_site_list);
        imageCancel = (ImageButton) rootView.findViewById(R.id.image_cancel);
        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                                        .inflate(R.layout.listview_footer, null, false);
        footerView.setVisibility(View.GONE);
        listTag.addFooterView(footerView);
        tagAdapter = new TagAdapter(getActivity());
        listTag.setAdapter(tagAdapter);

        ArrayAdapter<String> tagSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item);
        tagArray = getResources().getStringArray((R.array.tag_spinner_array));
        tagSpinnerAdapter.addAll(tagArray);
        tagSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTagSearch.setAdapter(tagSpinnerAdapter);

        getJsonTagResponse();

        spinnerTagSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tagSortBy = tagArray[position];
                mTagPageCount = 1;
                tagAdapter.removeItems();
                getJsonTagResponse();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listTag.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && (totalItemCount - 1) != 0
                        && !isTagLoading
                        && hasMoreTag) {
                    mTagPageCount = mTagPageCount + 1;
                    getJsonTagResponse();
                }
            }
        });

        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTagSearch.getText().clear();
                mTagPageCount = 1;
                inname = null;
                tagAdapter.removeItems();
                Utility.hideSoftKeyboard(getActivity());
                getJsonTagResponse();
            }
        });

        editTagSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");

                if (s.length() != 0) {
                    Log.e(TAG, "onTextChanged: " + s);
                    Log.e(TAG, "onTextChanged: " + s.length());
                    imageCancel.setVisibility(View.VISIBLE);
                } else {
                    imageCancel.setVisibility(View.GONE);
                    Utility.hideSoftKeyboard(getActivity());
                }
            }
        });

        editTagSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mTagPageCount = 1;
                    inname = editTagSearch.getText().toString().toLowerCase();
                    inname = inname.trim();
                    tagAdapter.removeItems();
                    Utility.hideSoftKeyboard(getActivity());
                    getJsonTagResponse();
                    return true;
                }
                return false;
            }
        });

        listTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tagName = tagAdapter.getItem(position).getName();
                Intent intentQuestionListActivity = TagDetailActivity.getNewIntent(getActivity(), tagName);
                startActivity(intentQuestionListActivity);

            }
        });
        return rootView;
    }

    private void getJsonTagResponse() {

        String site = SessionManager.getInstance(getActivity()).getApiSiteParameter();
        isTagLoading = true;
        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ListResponse<TagItem>> call = apiService.getTagList(mTagPageCount,
                Constants.ORDER_ASC,
                tagSortBy,
                inname,
                site);
        Log.d(TAG, "getJsonTagResponse: " + call);

        call.enqueue(new Callback<ListResponse<TagItem>>() {
            @Override
            public void onResponse(Call<ListResponse<TagItem>> call,
                                   Response<ListResponse<TagItem>> response) {
                hideProgressBar();
                isTagLoading = false;
                if (response.body() != null) {
                    hasMoreTag = response.body().isHasMore();
                    Log.e(TAG, "Has More ?: " + hasMoreTag);
                    tagAdapter.addItems(response.body().getItems());
                    Log.e(TAG, "Tag Response : " + response.body());
                }
            }
            @Override
            public void onFailure(Call<ListResponse<TagItem>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                Toast.makeText(getActivity(), "Couldn't Load Json Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hideProgressBar() {
        if (mTagPageCount == 1) {
            textLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            footerView.setVisibility(View.GONE);
        }
    }
    private void showProgressBar() {
        if (mTagPageCount == 1) {
            textLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            footerView.setVisibility(View.VISIBLE);
        }
    }
}