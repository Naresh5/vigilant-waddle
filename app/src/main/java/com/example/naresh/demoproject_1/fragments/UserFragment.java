package com.example.naresh.demoproject_1.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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

import com.example.naresh.demoproject_1.NavigationDrawerActivity;
import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.UserDetailActivity;
import com.example.naresh.demoproject_1.adapters.UserAdapter;
import com.example.naresh.demoproject_1.dialog.FilterDialogFragment;
import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.User;
import com.example.naresh.demoproject_1.models.UserResponse;
import com.example.naresh.demoproject_1.retrofit.ApiClient;
import com.example.naresh.demoproject_1.retrofit.ApiInterface;
import com.example.naresh.demoproject_1.utils.Constants;
import com.example.naresh.demoproject_1.utils.JSONParser;
import com.example.naresh.demoproject_1.utils.Utility;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment implements FilterDialogFragment.OnInfoChangedListener {
    private View rootView;
    private String TAG = UserFragment.class.getSimpleName();
    private ProgressBar mProgressBar;
    private ListView mListView;
    private View footerView;
    private UserAdapter adapter;
    private boolean isSearch = false;
    private boolean isLoading = false;
    private boolean hasMoreData = true;
    private int pageCount = 0;
    public String orderValue = "asc", sortValue = "reputation", fromDateValue, toDateValue;

    private String filterUserOrder = Constants.ORDER_ASC;
    private String filterUserSort = Constants.SORT_BY_REPUTATION;
    private String filterUserFromdate = null;
    private String filterUserTodate = null;
    private String inname;
    Handler handler = new Handler();
    private Runnable workRunnable;
    private final long DELAY = 900;


    public static UserFragment newInstance() {
        UserFragment userFragment = new UserFragment();
        return userFragment;
    }


    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int lastInScreen = firstVisibleItem + visibleItemCount;
                String last = String.valueOf(lastInScreen);
                String first = String.valueOf(firstVisibleItem);
                String visible = String.valueOf(visibleItemCount);
                Log.e("FIRST VISIBLE ITEM   :", last);
                Log.e("VISIBLE ITEM  Count  :", first);
                Log.e("Total ITEM  Count  :", visible);

                if ((lastInScreen == totalItemCount)
                        && (totalItemCount - 1 != 0)
                        && !isSearch) {
                    if (!isLoading) {
                        Log.e("ERR -:", "Scrolling List view");
                        pageCount++;
                        new LoadJsonData(Constants.ORDER_ASC, Constants.SORT_BY_REPUTATION, null, null, null).execute();
                    }
                }
            }
        });
        adapter = new UserAdapter(getActivity());
        mListView.setAdapter(adapter);

        boolean isNetwork = Utility.networkIsAvailable(getActivity());
        if (!isNetwork) {
            Toast.makeText(getActivity(), "Failed to Connect with Internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Network Available. . .!", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((NavigationDrawerActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                pageCount = 1;
                adapter.clearAdapter();

                new LoadJsonData(Constants.ORDER_ASC, Constants.SORT_BY_REPUTATION, null, null, null).execute();

                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                //   isSearch = !newText.isEmpty();
                //   adapter.getFilter().filter(newText);

                handler.removeCallbacksAndMessages(workRunnable);
                workRunnable = new Runnable() {
                    @Override
                    public void run() {
                        pageCount = 1;
                        adapter.clearAdapter();
                        if (newText.isEmpty()) {
                            new LoadJsonData(Constants.ORDER_ASC,Constants.SORT_BY_REPUTATION,
                                                                  null, null, newText).execute();
                        } else {
                             inname = newText;
                            getJsonUserListResponse(inname);
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
            showDialogFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Implementing method of Interface

    @Override
    public void onInfoChanged(String order, String sort, String FromDate, String ToDate) {

        this.orderValue = order;
        this.sortValue = sort;
        this.fromDateValue = FromDate;
        this.toDateValue = ToDate;

        adapter.clearAdapter();
        new LoadJsonData(order, sort, FromDate, ToDate, null).execute();
    }

    public class LoadJsonData extends AsyncTask<Object, Object, List<User>> {

        String order, sort, fromDate, toDate, inname;

        public LoadJsonData(String order, String sort, String fromDate, String toDate, String inname) {
            Log.d(TAG, "LoadJsonData: ");
            this.order = order;
            this.sort = sort;
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.inname = inname;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
            showProgressBar();
        }

        @Override
        protected List<User> doInBackground(Object... arg0) {

            JSONParser sh = new JSONParser();    //Request to url and getting response
            String jsonStr = null;
            try {
                Uri.Builder uriBuilder = Uri.parse(Constants.BASE_URL).buildUpon()
                        .appendPath("2.2")
                        .appendPath("users")
                        .appendQueryParameter("pagesize", Constants.PAGE_SIZE)
                        .appendQueryParameter("page", String.valueOf(pageCount))
                        .appendQueryParameter("site", Constants.SITE)
                        .appendQueryParameter("sort", sort)
                        .appendQueryParameter("order", order);
                if (fromDate != null) {
                    uriBuilder.appendQueryParameter("fromdate", fromDate);
                }
                if (toDate != null) {
                    uriBuilder.appendQueryParameter("todate", toDate);
                }
                if (inname != null) {
                    uriBuilder.appendQueryParameter("inname", inname);
                }
                Uri uri = uriBuilder.build();
//                Log.e(TAG, "doInBackground: " + uri.toString());
                Log.e(TAG, "Call : doInBackground: " + inname);
                jsonStr = sh.makeServiceCall(uri.toString());
//                Log.e(TAG, " JsonRes for DFragment" + jsonStr);
//                Log.e(TAG, "::::--Main activity FULL  URL :::-- " + uri);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("ERROR OCCURRED", "FAILED TO LOAD Json");
            }

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                Gson gson = new Gson();
                UserResponse userResponse = gson.fromJson(jsonStr, UserResponse.class);

                hasMoreData = userResponse.isHasMore();
                Log.e(TAG, "Call : UserFragment HAS MORE " + hasMoreData);
                return userResponse.getItems();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<User> userList) {
            super.onPostExecute(userList);
            isLoading = false;
            hideProgressBar();
            if (userList != null) {
                adapter.addItems(userList);
            } else {
                if (getActivity() != null)
                    Toast.makeText(getActivity(), "Couldn't Load Json Data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getJsonUserListResponse(String inname) {
        isLoading = true;
        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ListResponse<User>>  call = apiService.getUserList(
                                                pageCount,
                                                filterUserOrder,
                                                filterUserSort,
                                                filterUserFromdate,
                                                filterUserTodate,
                                                inname,
                                                Constants.SITE);
        Log.e(TAG, "Call : getJsonUserListResponse: Search" + call);

        call.enqueue(new Callback<ListResponse<User>>() {
            @Override
            public void onResponse(Call<ListResponse<User>> call,
                                   Response<ListResponse<User>> response) {
                isLoading = false;
                hideProgressBar();
                if (response.body() != null) {
                    // = response.body().isHasMore();
                    adapter.addItems(response.body().getItems());

                    Log.e(TAG, "Call : onResponse: " + response);
                }
            }

            @Override
            public void onFailure(Call<ListResponse<User>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                Log.e(TAG, "Call : onFailure: "+call );
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
                orderValue, sortValue,
                fromDateValue, toDateValue);
        filterDialog.setListener(this);
        filterDialog.show(getActivity().getSupportFragmentManager(),
                getResources().getString(R.string.dialog_tag));
    }
}
