package com.example.naresh.demoproject_1;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.naresh.demoproject_1.fragments.QuestionFragment;

public class TagDetailActivity extends AppCompatActivity {
    public static final String ARG_TAG_NAME = "tag_name";

    public static Intent getNewIntent(Context context, String tagName) {
        Intent i = new Intent(context, TagDetailActivity.class);
        i.putExtra(ARG_TAG_NAME, tagName);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);

        Intent i = getIntent();
        String tagName = i.getStringExtra(ARG_TAG_NAME);

        getSupportActionBar().setTitle(tagName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = QuestionFragment.newInstance(tagName);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_layout_que_list, fragment);
        ft.commit();

    }
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                TagDetailActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
