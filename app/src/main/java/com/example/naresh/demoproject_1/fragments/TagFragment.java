package com.example.naresh.demoproject_1.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.naresh.demoproject_1.NavigationDrawerActivity;
import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.adapters.TagAdapter;
import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.QuestionDetailItem;
import com.example.naresh.demoproject_1.models.TagItem;
import com.example.naresh.demoproject_1.models.User;
import com.example.naresh.demoproject_1.retrofit.ApiClient;
import com.example.naresh.demoproject_1.retrofit.ApiInterface;
import com.example.naresh.demoproject_1.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class TagFragment extends Fragment {
    private Spinner spinnerTagSearch;
    private EditText editTagSearch;
    private ListView listTag;
    private TextView textLoading;

    private ProgressBar progressBar;
    private View footerView;
    private TagAdapter tagAdapter;
    private int mTagPageCount = 1;
    private static final String TAG = "Tag Detail";
    private String tagSortBy = "popular";
    private boolean isTagLoading = false;
    private String[] tagArray;

    private String inname = null;
    private boolean hasMoreTag = true;


    public TagFragment() {

    }

    public static TagFragment newInstance() {
        TagFragment tagFragment = new TagFragment();
        return tagFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tag_fragment_navigation, container, false);
        spinnerTagSearch = (Spinner) rootView.findViewById(R.id.spinner_for_tag_search);
        editTagSearch = (EditText) rootView.findViewById(R.id.edit_tag_search);
        listTag = (ListView) rootView.findViewById(R.id.list_tag);
        textLoading = (TextView) rootView.findViewById(R.id.text_loading);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);

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
        listTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;

    }

    private void getJsonTagResponse() {
        isTagLoading = true;
        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ListResponse<TagItem>> call = apiService.getTagList(mTagPageCount,
                Constants.ORDER_ASC,
                tagSortBy,
                Constants.SITE);

        call.enqueue(new Callback<ListResponse<TagItem>>() {
            @Override
            public void onResponse(Call<ListResponse<TagItem>> call,
                                   Response<ListResponse<TagItem>> response) {
                hideProgressBar();
                isTagLoading = false;
                if (response.body() != null) {
                    hasMoreTag = response.body().isHasMore();
                    tagAdapter.addItems(response.body().getItems());
                    Log.e(TAG, "Tag Response : "+response.body());
                }
            }

            @Override
            public void onFailure(Call<ListResponse<TagItem>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
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