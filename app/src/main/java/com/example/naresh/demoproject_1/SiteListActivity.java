package com.example.naresh.demoproject_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.naresh.demoproject_1.adapters.SiteDetailAdapter;
import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.SiteItem;
import com.example.naresh.demoproject_1.retrofit.ApiClient;
import com.example.naresh.demoproject_1.retrofit.ApiInterface;
import com.example.naresh.demoproject_1.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteListActivity extends AppCompatActivity {
    private TextView textLoading;
    private ProgressBar progressBar;

    private RecyclerView recyclerViewSite;
    private SiteDetailAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recylerViewLayoutManager;
    private int sitePageNumber = 1;
    private static final String TAG = SiteListActivity.class.getSimpleName();

    /*public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, SiteListActivity.class);
        return intent;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list);

        getSupportActionBar().setTitle(R.string.siteTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewSite = (RecyclerView) findViewById(R.id.recycler_view_site);

        textLoading = (TextView) findViewById(R.id.text_loading_site_list);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_site_list);

        recyclerViewSite.setHasFixedSize(true);

        recylerViewLayoutManager = new LinearLayoutManager(this);

        recyclerViewSite.setLayoutManager(recylerViewLayoutManager);

        recyclerViewAdapter = new SiteDetailAdapter(recyclerViewSite, this);


        recyclerViewSite.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnLoadMoreListener(new SiteDetailAdapter.OnLoadMoreListener() {
            @Override
            public void loadItems() {
                sitePageNumber++;
                getJsonSiteResponse();
            }
        });
        recyclerViewAdapter.setOnItemClickListener(new SiteDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<SiteItem> items, View view, int position) {
                SessionManager.getInstance(SiteListActivity.this).addSiteDetail(items.get(position));
                setResult(RESULT_OK);
            //    khkljkjvkvjhhhhhhhhhhhhhhhhhh
                finish();
            }
        });


        getJsonSiteResponse();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void getJsonSiteResponse() {

        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ListResponse<SiteItem>> call = apiService.getSiteList(sitePageNumber,
                Constants.VALUE_SITE_FILTER);


        call.enqueue(new Callback<ListResponse<SiteItem>>() {
            @Override
            public void onResponse(Call<ListResponse<SiteItem>> call,
                                   Response<ListResponse<SiteItem>> response) {
                hideProgressBar();
                recyclerViewAdapter.setLoaded();
                if (response.body() != null) {
                    recyclerViewAdapter.addItems(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<ListResponse<SiteItem>> call, Throwable t) {

                Log.e(TAG, t.toString());
                recyclerViewAdapter.setLoaded();
                hideProgressBar();
            }
        });
    }

    private void hideProgressBar() {
        if (sitePageNumber == 1) {
            textLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            recyclerViewAdapter.removeProcessItem();
        }
    }

    private void showProgressBar() {
        if (sitePageNumber == 1) {
            textLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            recyclerViewAdapter.showProcessItem();
        }
    }
}
