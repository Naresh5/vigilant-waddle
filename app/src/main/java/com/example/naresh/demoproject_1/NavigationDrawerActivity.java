package com.example.naresh.demoproject_1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.naresh.demoproject_1.adapters.SiteAdapter;
import com.example.naresh.demoproject_1.fragments.QuestionFragment;
import com.example.naresh.demoproject_1.fragments.TagFragment;
import com.example.naresh.demoproject_1.fragments.UserFragment;
import com.example.naresh.demoproject_1.models.SiteItem;
import com.example.naresh.demoproject_1.utils.Utility;
import com.squareup.picasso.Picasso;

import org.afinal.simplecache.ACache;

import java.util.HashMap;
import java.util.List;

public class NavigationDrawerActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout headerRootView, siteListLayout;
    private ImageView imageArrow, imageNavigationIcon, imageSite;
    private ListView listSite;
    private SiteAdapter siteAdapter;
    private TextView textNavigationDescription, textNavigationSiteName, textSite;
    private View footerView;
    ACache mCache;
    private List<SiteItem> siteItems;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, NavigationDrawerActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        replaceFragment(new UserFragment());

        toolbar = (Toolbar) findViewById(R.id.toolbar_for_navigation);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addDrawerListener(this);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();

        mDrawer.addDrawerListener(drawerToggle);

        drawerToggle.syncState();
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        View headerView = nvDrawer.getHeaderView(0);
        headerRootView = (LinearLayout) headerView.findViewById(R.id.headerRootView);
        imageArrow = (ImageView) headerView.findViewById(R.id.image_arrow);
        imageNavigationIcon = (ImageView) headerView.findViewById(R.id.image_navigation_icon);
        textNavigationDescription = (TextView) headerView.findViewById(R.id.text_navigation_description);
        textNavigationSiteName = (TextView) headerView.findViewById(R.id.text_navigation_site_name);
        siteListLayout = (LinearLayout) findViewById(R.id.siteListLayout);
        listSite = (ListView) findViewById(R.id.list_site);

        nvDrawer.setNavigationItemSelectedListener(this);

        nvDrawer.setCheckedItem(R.id.nav_user);
        Fragment fragment = UserFragment.newInstance();
        //showFragment(R.string.nav_user_detail_title, fragment);

        mCache = ACache.get(this);
        siteItems = mCache.getAsObjectList(SplashActivity.KEY_CACHE, SiteItem.class);


        siteAdapter = new SiteAdapter(NavigationDrawerActivity.this);

        siteAdapter.addItems(siteItems);
        listSite.setAdapter(siteAdapter);


        mDrawer.addDrawerListener(drawerToggle);

        headerRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siteListLayout.getVisibility() == View.VISIBLE) {
                    siteListLayout.setVisibility(View.GONE);
                    imageArrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                } else {
                    siteListLayout.setVisibility(View.VISIBLE);
//                    footerView.setVisibility(View.VISIBLE);
                    imageArrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                }
            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_user:
                fragmentClass = UserFragment.class;
                break;
            case R.id.nav_question:
                fragmentClass = QuestionFragment.class;
                break;
            case R.id.nav_tag:
                fragmentClass = TagFragment.class;
                break;
            default:
                fragmentClass = UserFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        replaceFragment(fragment);
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        Utility.hideSoftKeyboard(this);

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        Utility.hideSoftKeyboard(this);

        if (siteListLayout.getVisibility() == View.VISIBLE) {
            siteListLayout.setVisibility(View.GONE);
            imageArrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }
    }


    @Override
    public void onDrawerStateChanged(int newState) {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //  openSelectedFragment(item);
        selectDrawerItem(item);
        return true;

    }


    public void changeSite() {
        Menu menu = nvDrawer.getMenu();
        MenuItem selectedMenu = null;
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isChecked()) {
                selectedMenu = menuItem;
                break;
            }
        }
        if (selectedMenu != null) {
            selectDrawerItem(selectedMenu);
        }
    }

    public void showCustomListView(String selectedSite) {

        //     siteAdapter.addItems(siteItems, selectedSite);
        //   changeSite();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 222 && resultCode == 333) {
            //   showSharedPreferenceDetail();
            changeSite();
        }
    }
}
