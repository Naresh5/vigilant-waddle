package com.example.naresh.demoproject_1.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.SessionManager;
import com.example.naresh.demoproject_1.adapters.QuestionDetailAdapter;
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

    private String filterQuestionOrder = Constants.ORDER_DESC;
    private String filterQuestionSort = Constants.SORT_BY_VOTES;
    private String filterQuestionTodate = null;
    private String filterQuestionFromdate = null;

    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    private Runnable workRunnable;
    private final long DELAY = 900;

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
      /*  if (getArguments() != null) {
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.question_fragment_navigation, container, false);
        textLoading = (TextView) rootView.findViewById(R.id.text_loading_site_list);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_site_list);
        listQuestionDetail = (ListView) rootView.findViewById(R.id.list_question_detail);

        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.listview_footer, null, false);
        footerView.setVisibility(View.GONE);
        listQuestionDetail.addFooterView(footerView);

        Bundle bundle = getArguments();
        if(bundle != null) {
            tagName = bundle.getString(ARG_TAG);
        }
        questionDetailAdapter = new QuestionDetailAdapter(getActivity());
        listQuestionDetail.setAdapter(questionDetailAdapter);

        getJsonQuestionListResponse(null);


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
                    getJsonQuestionListResponse(titleName);
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

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setActionView(item, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                handler.removeCallbacksAndMessages(workRunnable);

                workRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mQuestionPageCount = 1;
                        questionDetailAdapter.removeItems();
                        if (newText.isEmpty()) {
                            getJsonQuestionListResponse(null);
                        } else {
                            titleName = newText;
                            getJsonQuestionListResponse(titleName);
                        }
                    }
                };
                handler.postDelayed(workRunnable, DELAY);
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

        getJsonQuestionListResponse(null);

    }

    private void getJsonQuestionListResponse(String titleName) {

        String site = SessionManager.getInstance(getActivity()).getApiSiteParameter();

        isQuestionLoading = true;
        showProgressBar();
        Call<ListResponse<QuestionDetailItem>> call;
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        if (titleName == null) {
            call = apiService.getQuestionsList(
                    mQuestionPageCount,
                    filterQuestionOrder,
                    filterQuestionSort,
                    filterQuestionFromdate,
                    filterQuestionTodate,
                    tagName,
                    site);
        } else {
            call = apiService.getFilterQuestionList(mQuestionPageCount,
                    filterQuestionOrder,
                    filterQuestionSort,
                    filterQuestionFromdate,
                    filterQuestionTodate,
                    titleName,
                    site);

        }
        call.enqueue(new Callback<ListResponse<QuestionDetailItem>>() {
            @Override
            public void onResponse(Call<ListResponse<QuestionDetailItem>> call,
                                   Response<ListResponse<QuestionDetailItem>> response) {
                isQuestionLoading = false;
                hideProgressBar();
                if (response.body() != null) {
                    hasMore = response.body().isHasMore();
                    questionDetailAdapter.addItems(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<ListResponse<QuestionDetailItem>> call, Throwable t) {
                // Log error here since request failed
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
