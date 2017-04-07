package com.example.naresh.demoproject_1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

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
    private NavigationView navigationView;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar_for_navigation);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addDrawerListener(this);
        navigationView = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(navigationView);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nvView);

        View headerView = navigationView.getHeaderView(0);
        headerRootView = (LinearLayout) headerView.findViewById(R.id.headerRootView);
        imageArrow = (ImageView) headerView.findViewById(R.id.image_arrow);
        imageNavigationIcon = (ImageView) headerView.findViewById(R.id.image_navigation_icon);
        textNavigationDescription = (TextView) headerView.findViewById(R.id.text_navigation_description);
        textNavigationSiteName = (TextView) headerView.findViewById(R.id.text_navigation_site_name);
        siteListLayout = (LinearLayout) findViewById(R.id.siteListLayout);
        listSite = (ListView) findViewById(R.id.list_site);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.item_site, listSite, false);
        imageSite = (ImageView) footerView.findViewById(R.id.image_site);
        textSite = (TextView) footerView.findViewById(R.id.text_site_name);
        imageSite.setImageResource(R.drawable.ic_more_horiz_black_24dp);
        textSite.setText(R.string.siteTitle);
        listSite.addFooterView(footerView);
        footerView.setVisibility(View.VISIBLE);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_user);
        Fragment fragment = UserFragment.newInstance();
        showFragment(R.string.nav_user_detail_title, fragment);

        mCache = ACache.get(this);
        siteItems = mCache.getAsObjectList(SplashActivity.KEY_CACHE, SiteItem.class);
        siteAdapter = new SiteAdapter(NavigationDrawerActivity.this);
        listSite.setAdapter(siteAdapter);
        showSharedPreferenceDetail();
        mDrawer.addDrawerListener(drawerToggle);

        headerRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siteListLayout.getVisibility() == View.VISIBLE) {
                    siteListLayout.setVisibility(View.GONE);
                    imageArrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                } else {
                    siteListLayout.setVisibility(View.VISIBLE);
                    footerView.setVisibility(View.VISIBLE);
                    imageArrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                }
            }
        });

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationDrawerActivity.this, SiteListActivity.class);
                startActivityForResult(intent,222);
            }
        });

        listSite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SessionManager.getInstance(NavigationDrawerActivity.this)
                        .addSiteDetail(siteAdapter.getItem(position));
                showSharedPreferenceDetail();
                changeSite();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222 && resultCode == RESULT_OK) {
            showSharedPreferenceDetail();
            changeSite();
            onDrawerClosed(mDrawer);
        }
        else {
            onDrawerClosed(mDrawer);
            Toast.makeText(this, "Bad Request Code", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showAlertDialog();
        }
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
        int id = menuItem.getItemId();
        navigationView.setCheckedItem(id);
        Fragment fragment;
        switch (id) {
            case R.id.nav_user:
                fragment = UserFragment.newInstance();
                showFragment(R.string.nav_user_detail_title, fragment);
                break;
            case R.id.nav_question:
                fragment = QuestionFragment.newInstance(null);
                showFragment(R.string.nav_question_detail_title, fragment);
                break;
            case R.id.nav_tag:
                fragment = TagFragment.newInstance();
                showFragment(R.string.nav_tag_title, fragment);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
        selectDrawerItem(item);
        return true;
    }
    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationDrawerActivity.this);
        builder.setMessage(R.string.alert_dialog_text);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_app_launcher_icon);
        builder.setPositiveButton(
                R.string.alert_dialog_positive_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        NavigationDrawerActivity.this.finish();
                    }
                });
        builder.setNegativeButton(
                R.string.alert_dialog_negative_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showFragment(int fragmentNameRes, Fragment fragment) {
        if (fragment != null) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(fragmentNameRes);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.commit();
        }
    }

    public void showSharedPreferenceDetail() {
        HashMap<String, String> siteDetail = SessionManager.getInstance(this).getSiteDetail();
        String name = siteDetail.get(SessionManager.KEY_SITE_NAME);
        String image = siteDetail.get(SessionManager.KEY_SITE_IMAGE);
        final String audience = siteDetail.get(SessionManager.KEY_SITE_AUDIENCE);
        String SITE = siteDetail.get(SessionManager.KEY_SITE_PARAMETER);
        textNavigationSiteName.setText(name);
        textNavigationDescription.setText(Utility.convertTextToHTML(audience));
        Picasso.with(getApplicationContext())
                .load(image)
                .into(imageNavigationIcon);
        siteAdapter.addItems(siteItems, SITE);
    }

    public void changeSite() {
        Menu menu = navigationView.getMenu();
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
}

