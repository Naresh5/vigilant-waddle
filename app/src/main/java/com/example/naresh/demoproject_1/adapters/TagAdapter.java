package com.example.naresh.demoproject_1.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.models.TagItem;
import com.example.naresh.demoproject_1.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naresh on 28/3/17.
 */

public class TagAdapter extends BaseAdapter {

    private Context context;
    private List<TagItem> tagItems;

    public TagAdapter(Context context) {
        this.context = context;
        this.tagItems = new ArrayList<>();
    }

    public void addItems(List<TagItem> item) {
        tagItems.addAll(item);
        notifyDataSetChanged();
    }

    public void removeItems() {
        tagItems.clear();
    }

    @Override
    public int getCount() {
        return tagItems.size();
    }

    @Override
    public TagItem getItem(int position) {
        return tagItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_tag, parent, false);
            holder = new ViewHolder();

            holder.textTagName = (TextView) convertView.findViewById(R.id.text_tag_name);
            holder.textTagCount = (TextView) convertView.findViewById(R.id.text_tag_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TagItem tagItem = getItem(position);
        int tagCount = tagItem.getCount();

        holder.textTagName.setText(tagItem.getName());

        holder.textTagCount.setText(Utility.getReputation(tagCount));

        return convertView;
    }

    private class ViewHolder {
        TextView textTagName, textTagCount;
    }
}

