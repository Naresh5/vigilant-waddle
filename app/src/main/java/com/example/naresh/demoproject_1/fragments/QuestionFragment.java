package com.example.naresh.demoproject_1.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.naresh.demoproject_1.NavigationDrawerActivity;
import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.adapters.QuestionDetailAdapter;
import com.example.naresh.demoproject_1.adapters.UserAdapter;
import com.example.naresh.demoproject_1.dialog.FilterDialogFragment;
import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.QuestionDetailItem;
import com.example.naresh.demoproject_1.retrofit.ApiClient;
import com.example.naresh.demoproject_1.retrofit.ApiInterface;
import com.example.naresh.demoproject_1.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuestionFragment extends Fragment implements FilterDialogFragment.OnInfoChangedListener {

    private TextView textLoading;
    private ProgressBar progressBar;
    private ListView listQuestionDetail;
    private QuestionDetailAdapter questionDetailAdapter;
    private View footerView;
    private int mQuestionPageCount = 1;
    private boolean isQuestionLoading = false;

    private boolean isSearch = false;

    private boolean hasMore = true;
    public static final String ARG_TAG = "tagName";
    private String tagName = null;
    private String titleName = null;

    private static final String TAG = "QuestionDrawerFragment";
    //   public String orderValue = "asc", sortValue = "reputation", fromDateValue, toDateValue;

    private String filterQuestionOrder = Constants.ORDER_DESC;
    private String filterQuestionSort = Constants.SORT_BY_VOTES;
    private String filterQuestionTodate = null;
    private String filterQuestionFromdate = null;

    /* private String order = Constants.ORDER_DESC;
     private String sort = Constants.SORT_BY_VOTES;
   */  private String site = Constants.SITE;

    public QuestionFragment() {

    }

    public static QuestionFragment newInstance(String tag) {
        QuestionFragment questionDrawerFragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TAG, tag);
        questionDrawerFragment.setArguments(bundle);
        return questionDrawerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.question_fragment_navigation, container, false);
        textLoading = (TextView) rootView.findViewById(R.id.text_loading);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        listQuestionDetail = (ListView) rootView.findViewById(R.id.list_question_detail);

        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.listview_footer, null, false);
        footerView.setVisibility(View.GONE);
        listQuestionDetail.addFooterView(footerView);

        //        Bundle bundle = getArguments();
        //       tagName = bundle.getString(ARG_TAG);

        questionDetailAdapter = new QuestionDetailAdapter(getActivity());
        listQuestionDetail.setAdapter(questionDetailAdapter);

        getJsonQuestionListResponse();


        listQuestionDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && (totalItemCount - 1) != 0
                        && !isQuestionLoading
                        && hasMore) {
                    mQuestionPageCount++;
                    getJsonQuestionListResponse();
                }
            }
        });

        listQuestionDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionDetailItem questionDetail = questionDetailAdapter.getItem(position);
                String openLink = questionDetail.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(openLink));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((NavigationDrawerActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setActionView(item, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isSearch = !newText.isEmpty();
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_option) {
            showFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoChanged(String order, String sort, String FromDate, String ToDate) {

        mQuestionPageCount = 1;
        questionDetailAdapter.removeItems();

        this.filterQuestionOrder = order;
        this.filterQuestionSort = sort;
        this.filterQuestionFromdate = FromDate;
        this.filterQuestionTodate = ToDate;

        getJsonQuestionListResponse();

    }

    private void getJsonQuestionListResponse() {
        isQuestionLoading = true;
        showProgressBar();
        Call<ListResponse<QuestionDetailItem>> call;
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        call = apiService.getUserDetail
                (mQuestionPageCount,
                        filterQuestionOrder,
                        filterQuestionSort,
                        filterQuestionFromdate,
                        filterQuestionTodate,
                        site);
        Log.e(TAG, "::: Retrofit URL ::: " + call);

        call.enqueue(new Callback<ListResponse<QuestionDetailItem>>() {
            @Override
            public void onResponse(Call<ListResponse<QuestionDetailItem>> call,
                                   Response<ListResponse<QuestionDetailItem>> response) {
                isQuestionLoading = false;
                hideProgressBar();

                if (response.body() != null) {

                    hasMore = response.body().isHasMore();
                    questionDetailAdapter.addItems(response.body().getItems());

                    Log.e(TAG, "Retrofit Response" + response.body());
                }
            }

            @Override
            public void onFailure(Call<ListResponse<QuestionDetailItem>> call, Throwable t) {
                hideProgressBar();
                isQuestionLoading = false;
                Log.e(TAG, t.toString());
            }
        });
    }

    private void hideProgressBar() {
        if (mQuestionPageCount == 1) {
            textLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            footerView.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (mQuestionPageCount == 1) {
            textLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            footerView.setVisibility(View.VISIBLE);
        }
    }

    public void showFilterDialog() {
        String[] array = getResources().getStringArray(R.array.spinnerQuestionSort);
        FilterDialogFragment filterDialog = FilterDialogFragment.newInstance(
                array,
                filterQuestionOrder,
                filterQuestionSort,
                filterQuestionTodate,
                filterQuestionFromdate);
        filterDialog.setListener(this);
        filterDialog.show(getActivity().getSupportFragmentManager(),
                getResources().getString(R.string.dialog_tag));
    }
}
