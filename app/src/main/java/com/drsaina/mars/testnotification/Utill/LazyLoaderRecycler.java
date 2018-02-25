package com.drsaina.mars.testnotification.Utill;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by TCAdmin on 16/02/2017.
 */

public abstract class LazyLoaderRecycler extends RecyclerView.OnScrollListener {

    public static String TAG = LazyLoaderRecycler.class.getSimpleName();
    private static final int DEFAULT_THRESHOLD = 10;

    private boolean loading = true;
    private int previousTotal = 0;
    private int threshold = DEFAULT_THRESHOLD;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLinearLayoutManager;

    public LazyLoaderRecycler ( LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem,  int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                // the loading has finished
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        // check if the List needs more data
        if (!loading && ((firstVisibleItem + visibleItemCount) >= (totalItemCount - threshold))) {
            loading = true;
            // List needs more data. Go fetch !!
            loadMore(firstVisibleItem, visibleItemCount, totalItemCount);
//            current_page++;
//            onLoadMore(current_page);
        }
    }

    // public abstract void onLoadMore(int current_page);
    // Called when the user is nearing the end of the ListView
    // and the ListView is ready to add more items.

    public abstract void loadMore(int firstVisibleItem, int visibleItemCount, int totalItemCount);
}
