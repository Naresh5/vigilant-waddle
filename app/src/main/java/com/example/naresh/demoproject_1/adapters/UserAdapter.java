package com.example.naresh.demoproject_1.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.models.BadgeCounts;
import com.example.naresh.demoproject_1.models.User;
import com.example.naresh.demoproject_1.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naresh on 27/2/17.
 */

public class UserAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<User> items;
    private ArrayList<User> myStringFilterList;
    private ValueFilter valueFilter;

    public UserAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
        this.myStringFilterList = new ArrayList<>();
    }

    public void addItems(List<User> arrayList) {
        items.addAll(arrayList);
        myStringFilterList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        items.clear();
        myStringFilterList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        ImageView userImage;
        TextView userName, userReputation, userBadgeGold, userBadgeSilver, userBadgeBronze;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.userImage = (ImageView) convertView.findViewById(R.id.user_image);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.userReputation = (TextView) convertView.findViewById(R.id.user_reputation);
            viewHolder.userBadgeGold = (TextView) convertView.findViewById(R.id.user_badge_gold);
            viewHolder.userBadgeSilver = (TextView) convertView.findViewById(R.id.user_badge_silver);
            viewHolder.userBadgeBronze = (TextView) convertView.findViewById(R.id.user_badge_bronze);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User currentItem = (User) getItem(position);
        BadgeCounts badgeCount = currentItem.getBadgeCounts();

        Picasso.with(context).load(currentItem.getProfileImage()).into(viewHolder.userImage);

        viewHolder.userName.setText(Utility.convertTextToHTML(currentItem.getDisplayName()));
        viewHolder.userReputation.setText(currentItem.getReputation());
        viewHolder.userBadgeGold.setText(String.valueOf(badgeCount.getGold()));
        viewHolder.userBadgeSilver.setText(String.valueOf((badgeCount.getSilver())));
        viewHolder.userBadgeBronze.setText(String.valueOf((badgeCount.getBronze())));

        return convertView;
    }

    @Override
    public Filter getFilter() {

        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<User> filterList = new ArrayList<User>();

                for (int i = 0; i < myStringFilterList.size(); i++) {
                    if ((myStringFilterList.get(i).getDisplayName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        User user = myStringFilterList.get(i);
                        filterList.add(user);
                    }
                }

                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = myStringFilterList.size();
                results.values = myStringFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    }
}


