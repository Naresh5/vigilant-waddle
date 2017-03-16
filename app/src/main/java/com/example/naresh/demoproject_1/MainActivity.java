package com.example.naresh.demoproject_1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.naresh.demoproject_1.adapters.UserAdapter;
import com.example.naresh.demoproject_1.dialog.FilterDialogFragment;
import com.example.naresh.demoproject_1.fragments.ActivityFragment;
import com.example.naresh.demoproject_1.fragments.ProfileFragment;
import com.example.naresh.demoproject_1.models.User;
import com.example.naresh.demoproject_1.models.UserResponse;
import com.example.naresh.demoproject_1.utils.Constants;
import com.example.naresh.demoproject_1.utils.JSONParser;
import com.example.naresh.demoproject_1.utils.Utility;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilterDialogFragment.OnInfoChangedListener {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressBar mProgressBar;
    private ListView mListView;
    private View footerView;
    private Context context = MainActivity.this;
    private UserAdapter adapter;
    private boolean isSearch = false;
    private boolean isLoading = false;
    private int pageCount = 0;
    private String orderValue = "desc", sortValue = "reputation", fromDateValue, toDateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_view);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.listview_footer, null, false);
        mListView.addFooterView(footerView);
        footerView.setVisibility(View.INVISIBLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User user = (User) adapter.getItem(position);
                Intent intentUserDetailActivity = UserDetailActivity.startIntent(MainActivity.this, user);
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

                if ((lastInScreen == totalItemCount) && (totalItemCount - 1 != 0) && !isSearch) {
                    if (!isLoading) {
                        Log.e("ERR -:", "Scrolling List view");
                        pageCount++;
                        new LoadJsonData(Constants.ORDER_ASC, Constants.SORT_BY_REPUTATION, null, null).execute();
                    }
                }
            }
        });

        adapter = new UserAdapter(MainActivity.this);
        mListView.setAdapter(adapter);

        boolean isNetwork = Utility.networkIsAvailable(context);
        if (!isNetwork) {
            Toast.makeText(context, "Failed to Connect with Internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Network Available. . .!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusearch, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isSearch = !newText.isEmpty();
                adapter.getFilter().filter(newText);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_option) {

            FilterDialogFragment FilterDialogFragment = new FilterDialogFragment();

            Bundle bundle = new Bundle();

            bundle.putString("orderValue", orderValue);
            bundle.putString("sortValue", sortValue);
            bundle.putString("FromDateValue", fromDateValue);
            bundle.putString("ToDateValue", toDateValue);
            FilterDialogFragment.setArguments(bundle);

            FilterDialogFragment.show(getSupportFragmentManager(), null);

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

        // Toast.makeText(context, "Order" + order + "\nSort" + sort + "\nFromDate" + FromDate + "\nToDate" + ToDate, Toast.LENGTH_LONG).show();

        adapter.clearAdapter();
        new LoadJsonData(order, sort, FromDate, ToDate).execute();

    }

    public class LoadJsonData extends AsyncTask<Object, Object, List<User>> {

        String order, sort, fromDate, toDate;


        public LoadJsonData(String order, String sort, String fromDate, String toDate) {
            Log.d(TAG, "LoadJsonData: ");
            this.order = order;
            this.sort = sort;
            this.fromDate = fromDate;
            this.toDate = toDate;
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

                Uri uri = uriBuilder.build();
                jsonStr = sh.makeServiceCall(uri.toString());
                Log.e(TAG, " JsonRes for DFragment" + jsonStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("ERROR OCCURRED", "FAILED TO LOAD Json");
            }

            Log.e(TAG, "Response from url: " + jsonStr);
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
            isLoading = false;
            hideProgressBar();
            if (userList != null) {
                adapter.addItems(userList);
            }
        }
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
}
