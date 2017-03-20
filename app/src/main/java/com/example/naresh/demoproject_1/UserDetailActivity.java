package com.example.naresh.demoproject_1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.naresh.demoproject_1.adapters.ViewPagerAdapter;
import com.example.naresh.demoproject_1.dialog.FilterDialogFragment;
import com.example.naresh.demoproject_1.fragments.ActivityFragment;
import com.example.naresh.demoproject_1.fragments.ProfileFragment;
import com.example.naresh.demoproject_1.models.BadgeCounts;
import com.example.naresh.demoproject_1.models.User;
import com.squareup.picasso.Picasso;

import static android.R.attr.fragment;
import static com.example.naresh.demoproject_1.R.id.toolbar;

public class UserDetailActivity extends AppCompatActivity {
    private String TAG = UserDetailActivity.class.getSimpleName();

    public static final String EXTRA_USER = "data";

    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    private TextView textUserName, textUserReputation, textSilverBadge, textBronzeBadge, textGoldBadge;
    private ImageView imageUser;
    private User user;


    public static Intent startIntent(Context context, User user) {
        Intent i = new Intent(context, UserDetailActivity.class);
        i.putExtra(EXTRA_USER, user);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Intent i = getIntent();
        user = i.getParcelableExtra(EXTRA_USER);

        imageUser = (ImageView) findViewById(R.id.item_image_user);
        textUserName = (TextView) findViewById(R.id.item_user_name);
        textUserReputation = (TextView) findViewById(R.id.item_user_reputation);
        textGoldBadge = (TextView) findViewById(R.id.item_user_gold);
        textSilverBadge = (TextView) findViewById(R.id.item_user_silver);
        textBronzeBadge = (TextView) findViewById(R.id.item_user_bronze);


        mToolBar = (Toolbar) findViewById(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(mToolBar);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragments(ActivityFragment.newInstance(user.getUserId()), "ACTIVITY");

        viewPagerAdapter.addFragments(ProfileFragment.newInstance(user.getUserId()), "PROFILE");

        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        BadgeCounts badgeCounts = user.getBadgeCounts();
        textUserName.setText(user.getDisplayName());
        textUserReputation.setText(user.getReputation());
        textBronzeBadge.setText(String.valueOf(badgeCounts.getBronze()));
        textSilverBadge.setText(String.valueOf(badgeCounts.getSilver()));
        textGoldBadge.setText(String.valueOf(badgeCounts.getGold()));
        Picasso.with(UserDetailActivity.this)
                .load(user.getProfileImage())
                .placeholder(R.drawable.ic_userdemo)
                .into(imageUser);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuoption, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
