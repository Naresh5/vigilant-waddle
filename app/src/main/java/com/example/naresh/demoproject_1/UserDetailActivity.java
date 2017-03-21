package com.example.naresh.demoproject_1;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naresh.demoproject_1.adapters.ViewPagerAdapter;
import com.example.naresh.demoproject_1.fragments.ActivityFragment;
import com.example.naresh.demoproject_1.fragments.ProfileFragment;
import com.example.naresh.demoproject_1.models.BadgeCounts;
import com.example.naresh.demoproject_1.models.User;
import com.squareup.picasso.Picasso;

import static android.R.attr.id;
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
    private String link = "http://stackoverflow.com/questions/19253786/how-to-copy-text-to-clip-board-in-android";


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

        viewPagerAdapter.addFragments(ActivityFragment.newInstance(user.getUserId()), "PROFILE");

        viewPagerAdapter.addFragments(ProfileFragment.newInstance(user.getUserId()), "ACTIVITY");

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
        getMenuInflater().inflate(R.menu.menu_user_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                UserDetailActivity.this.finish();
                break;
            case R.id.open_in_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(link));
                startActivity(intent);
                break;
            case R.id.copy_to_clipboard:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("link",link);
                clipboard.setPrimaryClip(clip);
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item1 = abc.getItemAt(0);
                String text = item1.getText().toString();
             //   Utils.showToast(getApplicationContext(),text);
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,
                        getResources().getString(R.string.menu_open_in_browser_title)));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
       @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                UserTabActivity.this.finish();
                break;
            case R.id.open_in_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(link));
                startActivity(intent);
                break;
            case R.id.copy_to_clipboard:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(EXTRA_LINK, link);
                clipboard.setPrimaryClip(clip);
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item1 = abc.getItemAt(0);
                String text = item1.getText().toString();
                Utils.showToast(getApplicationContext(),text);
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,
                        getResources().getString(R.string.share_dialog_title)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
     */

}
