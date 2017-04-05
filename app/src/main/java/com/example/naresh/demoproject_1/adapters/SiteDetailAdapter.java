package com.example.naresh.demoproject_1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.models.SiteItem;
import com.example.naresh.demoproject_1.utils.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by naresh on 4/4/17.
 */

public class SiteDetailAdapter extends RecyclerView.Adapter<SiteDetailAdapter.MyViewHolder> {

    private Context context;
    private List<SiteItem> siteItems;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textNameSite, textAudience;
        public ImageView imageSiteList;

        public MyViewHolder(View view) {
            super(view);
            textNameSite = (TextView) view.findViewById(R.id.text_name_site);
            textAudience = (TextView) view.findViewById(R.id.text_audience);
            imageSiteList = (ImageView) view.findViewById(R.id.image_site_list);
        }
    }

    public void addItems(List<SiteItem> items) {
        siteItems.addAll(items);
        notifyDataSetChanged();
    }

    public SiteDetailAdapter(List<SiteItem> siteItems) {
        this.siteItems = siteItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_site_detail, parent, false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SiteItem site = siteItems.get(position);

        holder.textNameSite.setText(Utility.convertTextToHTML(site.getName()));
     //   holder.textNameSite.setText(site.getName());
        holder.textAudience.setText(Utility.convertTextToHTML(site.getAudience()));

        Picasso.with(context)
                .load(site.getIconUrl())
                .into(holder.imageSiteList);

    }

    @Override
    public int getItemCount() {
        return siteItems.size();
        //return moviesList.size();
    }
}
