package com.example.naresh.demoproject_1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naresh.demoproject_1.adapters.ViewPagerAdapter;
import com.example.naresh.demoproject_1.fragments.ActivityFragment;
import com.example.naresh.demoproject_1.fragments.ProfileFragment;
import com.example.naresh.demoproject_1.models.BadgeCounts;
import com.example.naresh.demoproject_1.models.User;
import com.example.naresh.demoproject_1.utils.Utility;
import com.squareup.picasso.Picasso;

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
    private String profileLink;


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

        profileLink = user.getProfileLink();
        Log.e(TAG, " UserDetail  profileLink " + profileLink);

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
        viewPagerAdapter.addFragments(ActivityFragment.newInstance(user.getUserId()), getString(R.string.profile_fragmet_title));
        viewPagerAdapter.addFragments(ProfileFragment.newInstance(user.getUserId()), getString(R.string.activity_fragment_title));
        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        BadgeCounts badgeCounts = user.getBadgeCounts();
        textUserName.setText(Utility.convertTextToHTML(user.getDisplayName()));
        textUserReputation.setText(user.getReputation());
        textBronzeBadge.setText(String.valueOf(badgeCounts.getBronze()));
        textSilverBadge.setText(String.valueOf(badgeCounts.getSilver()));
        textGoldBadge.setText(String.valueOf(badgeCounts.getGold()));
        Picasso.with(UserDetailActivity.this)
                .load(user.getProfileImage())
                .placeholder(R.drawable.ic_user_demo)
                .into(imageUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                UserDetailActivity.this.finish();
                break;

            case R.id.open_in_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(profileLink));
                startActivity(intent);
                break;

            case R.id.copy_to_clipboard:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("link", profileLink);
                clipboard.setPrimaryClip(clip);
                ClipData clipData = clipboard.getPrimaryClip();
                ClipData.Item data = clip.getItemAt(0);
                String text = data.getText().toString();
                if (text != null) {
                    Toast.makeText(getApplicationContext(), "Copied to ClipBoard ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to Copy on ClipBoard", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, profileLink);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.menu_open_in_browser_title)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
