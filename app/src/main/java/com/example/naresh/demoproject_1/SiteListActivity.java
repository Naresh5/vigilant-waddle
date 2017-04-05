package com.example.naresh.demoproject_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.naresh.demoproject_1.adapters.SiteDetailAdapter;
import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.SiteItem;
import com.example.naresh.demoproject_1.retrofit.ApiClient;
import com.example.naresh.demoproject_1.retrofit.ApiInterface;
import com.example.naresh.demoproject_1.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSite;
    private TextView mTextLoading;
    private ProgressBar mProgressaBar;
    private TextView textLoading;
    private int sitePageNumber = 1;
    private SiteDetailAdapter recyclerViewAdapter;
    private List<SiteItem> siteItems = new ArrayList<>();

    private static final String TAG =SiteListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list);

        recyclerViewSite = (RecyclerView) findViewById(R.id.recycler_view_site);
        mTextLoading = (TextView) findViewById(R.id.text_loading_site_list);
        mProgressaBar = (ProgressBar) findViewById(R.id.progressbar_site_list);

        recyclerViewAdapter = new SiteDetailAdapter(siteItems);
        getJsonSiteDetail();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewSite.setLayoutManager(mLayoutManager);
        recyclerViewSite.setItemAnimator(new DefaultItemAnimator());

        recyclerViewSite.setAdapter(recyclerViewAdapter);




    }

    private void getJsonSiteDetail(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ListResponse<SiteItem>> call = apiInterface.getSiteList(sitePageNumber, Constants.VALUE_SITE_FILTER);

        call.enqueue(new Callback<ListResponse<SiteItem>>() {
            @Override
            public void onResponse(Call<ListResponse<SiteItem>> call, Response<ListResponse<SiteItem>> response) {
                if (response.body() != null){
                    recyclerViewAdapter.addItems(response.body().getItems());
                    Log.e(TAG, "onResponse: "+response.body());
                }
            }
            @Override
            public void onFailure(Call<ListResponse<SiteItem>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString());
            }
        });
    }
}
