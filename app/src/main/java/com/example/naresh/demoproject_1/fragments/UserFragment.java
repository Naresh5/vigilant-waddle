package com.example.naresh.demoproject_1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.SessionManager;
import com.example.naresh.demoproject_1.UserDetailActivity;
import com.example.naresh.demoproject_1.adapters.UserAdapter;
import com.example.naresh.demoproject_1.dialog.FilterDialogFragment;
import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.User;
import com.example.naresh.demoproject_1.retrofit.ApiClient;
import com.example.naresh.demoproject_1.retrofit.ApiInterface;
import com.example.naresh.demoproject_1.utils.Constants;
import com.example.naresh.demoproject_1.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment implements FilterDialogFragment.OnInfoChangedListener {
    private String TAG = UserFragment.class.getSimpleName();

    private ProgressBar mProgressBar;
    private ListView mListView;
    private View footerView;
    private UserAdapter adapter;

    private final long DELAY = 700;
    private int pageCount = 1;
    private boolean isSearch = false;
    private boolean isLoading = false;
    private boolean hasMoreUsers = true;
    private String userName = null;
    private String site = SessionManager.getInstance(getActivity()).getApiSiteParameter();
    private String filterOrder = Constants.ORDER_DESC;
    private String filterSort = Constants.SORT_BY_REPUTATION;
    private String filterTodate = null;
    private String filterFromdate = null;

    private Handler handler;

    public static UserFragment newInstance() {
        UserFragment userFragment = new UserFragment();
        return userFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.user_fragment_navigation, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_user_fragment_nav);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_user_fragment_nav);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null, false);
        mListView.addFooterView(footerView);
        footerView.setVisibility(View.INVISIBLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) adapter.getItem(position);
                Intent intentUserDetailActivity = UserDetailActivity.startIntent(getActivity(), user);
                startActivity(intentUserDetailActivity);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && (totalItemCount - 1) != 0
                        && !isLoading
                        && !isSearch
                        && hasMoreUsers) {
                    pageCount++;
                    getUserDetail();
                    // new GetDataFromJson().execute();
                }
            }
        });
        handler = new Handler();
        adapter = new UserAdapter(getActivity());
        mListView.setAdapter(adapter);
        getUserDetail();

        boolean isNetwork = Utility.networkIsAvailable(getActivity());
        if (!isNetwork) {
            Toast.makeText((getActivity()), "Failed to Connect with Internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Network Available. . .!", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                handler.removeCallbacksAndMessages(null);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageCount = 1;
                        adapter.clearAdapter();
                        if (!newText.isEmpty()) {
                            userName = newText;
                        } else {
                            filterOrder = Constants.ORDER_DESC;
                            filterSort = Constants.SORT_BY_REPUTATION;
                            userName = null;
                        }
                        getUserDetail();
                    }
                }, DELAY);

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_option) {
            showDialogFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Implementing method of Interface

    @Override
    public void onInfoChanged(String order, String sort, String FromDate, String ToDate) {
        //reset the page index
        pageCount = 1;
        adapter.clearAdapter();

        filterOrder = order;
        filterSort = sort;
        filterFromdate = FromDate;
        filterTodate = ToDate;

        //  new LoadJsonData(order, sort, FromDate, ToDate, null).execute();
        getUserDetail();
    }

    private void getUserDetail() {
        isLoading = true;
        showProgressBar();
        final ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ListResponse<User>> call = apiService.getUserList(pageCount,
                filterOrder,
                filterSort,
                filterFromdate,
                filterTodate,
                userName,
                site);

        call.enqueue(new Callback<ListResponse<User>>() {
            @Override
            public void onResponse(Call<ListResponse<User>> call,
                                   Response<ListResponse<User>> response) {
                hideProgressBar();
                isLoading = false;
                if (response.body() != null) {
                    hasMoreUsers = response.body().isHasMore();
                    adapter.addItems(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<ListResponse<User>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void hideProgressBar() {
        if (pageCount == 1) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            footerView.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (pageCount == 1) {
            mProgressBar.setVisibility(View.VISIBLE);
            footerView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            footerView.setVisibility(View.VISIBLE);
        }
    }

    public void showDialogFragment() {
        String[] array = getResources().getStringArray(R.array.spinnerUserSort);
        FilterDialogFragment filterDialog = FilterDialogFragment.newInstance(
                array,
                filterOrder, filterSort,
                filterTodate, filterFromdate);
        filterDialog.setListener(this);
        filterDialog.show(getActivity().getSupportFragmentManager(),
                getResources().getString(R.string.dialog_tag));

    }
}
