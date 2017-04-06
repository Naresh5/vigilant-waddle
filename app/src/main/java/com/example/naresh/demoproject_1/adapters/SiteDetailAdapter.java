package com.example.naresh.demoproject_1.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.models.SiteItem;
import com.example.naresh.demoproject_1.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by naresh on 4/4/17.
 */
public class SiteDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<SiteItem> siteItems;
    private final int VIEW_ITEM = 0;
    private final int VIEW_PROG = 1;
    private OnLoadMoreListener mOnLoadMoreListener;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;


    public interface OnLoadMoreListener {
        void loadItems();
    }


    public SiteDetailAdapter(RecyclerView recyclerView, Context context) {
        this.context = context;

        siteItems = new ArrayList<>();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem+ visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.loadItems();
                        }
                        loading = true;
                        Log.e(TAG, "onScrolled : totalItemCount = "+totalItemCount );
                        Log.e(TAG, "onScrolled: lastVisibleItem = "+lastVisibleItem );
                        Log.e(TAG, "onScrolled: visibleThreshold = "+visibleThreshold );
                    }
                }

            });
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return siteItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;

    }

    public void addItems(List<SiteItem> items) {
        siteItems.addAll(items);
        notifyDataSetChanged();
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView textLoading;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.prgress_bar_footer_view);
            textLoading = (TextView) itemView.findViewById(R.id.text_loading_footer_view);
        }
    }

    static class SiteViewHolder extends RecyclerView.ViewHolder {
        private TextView textNameSite, textAudience;
        private ImageView imageSiteList;

        private SiteViewHolder(View itemView) {
            super(itemView);
            textNameSite = (TextView) itemView.findViewById(R.id.text_name_site);
            textAudience = (TextView) itemView.findViewById(R.id.text_audience);
            imageSiteList = (ImageView) itemView.findViewById(R.id.image_site_list);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_site_detail, parent, false);
            return new SiteViewHolder(view);
        } else if (viewType == VIEW_PROG) {
            View view = LayoutInflater.from(context).inflate(R.layout.listview_footer, parent, false);
            if(view instanceof LinearLayout){

            }
            return new LoadingViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SiteViewHolder) {
            final SiteItem siteItem = siteItems.get(position);
            ((SiteViewHolder) holder).textNameSite.setText(Utility.convertTextToHTML(siteItem.getName()));
            ((SiteViewHolder) holder).textAudience.setText(Utility.convertTextToHTML(siteItem.getAudience()));
            Picasso.with(context)
                    .load(siteItem.getIconUrl())

                    .into(((SiteViewHolder) holder).imageSiteList);
        } else {
            ((LoadingViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
            ((LoadingViewHolder) holder).textLoading.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return siteItems.size();
    }

    public void setLoaded() {
        loading = false;
    }

    public void showProcessItem() {
        siteItems.add(null);
        notifyItemInserted(siteItems.size() - 1);
    }

    public void removeProcessItem() {
        siteItems.remove(siteItems.size() - 1);
        notifyItemRemoved(siteItems.size());
    }
}