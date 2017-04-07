package com.example.naresh.demoproject_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.SiteItem;
import com.example.naresh.demoproject_1.retrofit.ApiClient;
import com.example.naresh.demoproject_1.retrofit.ApiInterface;
import com.example.naresh.demoproject_1.utils.Constants;

import org.afinal.simplecache.ACache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    public static final String KEY_CACHE = "caseKey";
    private ProgressBar mProgressbar;
    private TextView textError;
    private Button buttonTryAgain;
    private List<SiteItem> listSiteDetail = new ArrayList<>();
    private int pageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mProgressbar = (ProgressBar) findViewById(R.id.progressbar_loading_splash);
        textError = (TextView) findViewById(R.id.text_error);
        buttonTryAgain = (Button) findViewById(R.id.button_try_again);

        getAppList();

        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonTryAgain.setVisibility(View.GONE);
                textError.setVisibility(View.GONE);
                mProgressbar.setVisibility(View.VISIBLE);
                getAppList();
            }
        });
    }

    public void getAppList() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ListResponse<SiteItem>> call = apiService.getSiteList(pageNo,
                Constants.VALUE_SITE_FILTER);

        call.enqueue(new Callback<ListResponse<SiteItem>>() {
            @Override
            public void onResponse(Call<ListResponse<SiteItem>> call,
                                   Response<ListResponse<SiteItem>> response) {
                mProgressbar.setVisibility(View.GONE);
                if (response.body() != null) {
                    listSiteDetail = response.body().getItems();
                    saveDataInSharedPreference();
                    saveDataInCache();
                    openActivity();
                    Toast.makeText(SplashActivity.this, "Success...", Toast.LENGTH_SHORT).show();
                } else {
                    textError.setVisibility(View.VISIBLE);
                    buttonTryAgain.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ListResponse<SiteItem>> call, Throwable t) {
                textError.setVisibility(View.VISIBLE);
                buttonTryAgain.setVisibility(View.VISIBLE);
                mProgressbar.setVisibility(View.GONE);
                Log.e(TAG, t.toString());
            }
        });
    }
    private void saveDataInCache() {
        ACache mCache = ACache.get(this);
        mCache.put(KEY_CACHE, listSiteDetail);
    }
    private void openActivity() {
        Intent intent = NavigationDrawerActivity.getStartIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }
    private void saveDataInSharedPreference() {
        if (listSiteDetail.size() > 0) {
            SiteItem siteItem = null;
            // check for stackOverflow site item
            for (SiteItem item : listSiteDetail) {
                if (item.getApiSiteParameter() != null && item.getApiSiteParameter()
                        .equalsIgnoreCase(Constants.SITE)) {
                    siteItem = item;
                    break;
                }
            }
            // if stackOverflow not found then use first item from list
            if (siteItem == null) {
                siteItem = listSiteDetail.get(0);
            }
            SessionManager.getInstance(this).addSiteDetail(siteItem);
        }
    }
}
