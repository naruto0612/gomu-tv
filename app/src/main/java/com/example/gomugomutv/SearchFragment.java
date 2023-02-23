package com.example.gomugomutv;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.SearchSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SearchBar;
import androidx.leanback.widget.SpeechOrbView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchFragment extends SearchSupportFragment implements SearchSupportFragment.SearchResultProvider {
    private static final String TAG = "SearchFragment";
    private static final int SEARCH_DELAY_MS = 1000;

    private ArrayObjectAdapter mRowsAdapter;
    private Handler mHandler = new Handler();
    private String mQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        FrameLayout searchFrame = root.findViewById(R.id.lb_search_frame);
        SearchBar mSearchBar = searchFrame.findViewById(R.id.lb_search_bar);
        SpeechOrbView mSpeechOrbView = mSearchBar.findViewById(R.id.lb_search_bar_speech_orb);


        if (mSpeechOrbView != null) {
            mSpeechOrbView.setVisibility(View.GONE);
        }

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        ListRowPresenter check = new ListRowPresenter();
        check.setShadowEnabled(false);
        check.enableChildRoundedCorners(true);

        mRowsAdapter = new ArrayObjectAdapter(check);

        setTitle("Gomu");
        setSearchResultProvider(this);
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onQueryTextChange(String newQuery) {
        Log.i(TAG, String.format("Search Query Text Change %s", newQuery));
        SearchList.clearQuery();
//        mRowsAdapter.clear();
//        loadQuery(newQuery);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(TAG, String.format("Search Query Text Submit %s", query));
        System.out.println("Searched: " + query);
        mRowsAdapter.clear();
        SearchList.QUERY = query;
        loadQuery(query);
        return true;
    }

    private void loadQuery(String query) {
        mQuery = query;
        mHandler.removeCallbacks(mDelayedLoad);
        if (!TextUtils.isEmpty(query) && !query.equals("nil")) {
            mHandler.postDelayed(mDelayedLoad, SEARCH_DELAY_MS);
        }
    }

    private void loadRows() {

        HashMap<String, ArrayList<Movie>> list = SearchList.getList();

        ArrayList<String> Cats = SearchList.getCats();

        int NUM_ROWS = list.size();

        CardPresenter cardPresenter = new CardPresenter();

        for (int i = 0; i < NUM_ROWS; i++)
        {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            String thisCat = Cats.get(i);

            for (int j = 0; j < list.get(thisCat).size(); j++)
                listRowAdapter.add(list.get(Cats.get(i)).get(j));

            HeaderItem header = new HeaderItem(i, Cats.get(i));
            ListRow row = new ListRow(header, listRowAdapter);

            mRowsAdapter.add(row);

        }
    }

    private Runnable mDelayedLoad = new Runnable() {
        @Override
        public void run() {
            loadRows();
        }
    };

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @SuppressLint("LongLogTag")
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {




            if (item instanceof Movie) {
                Movie movie = (Movie) item;

                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME)
                        .toBundle();
                getActivity().startActivity(intent, bundle);

            } else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }




        }

    }
}

//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.leanback.app.BackgroundManager;
//import androidx.leanback.widget.ArrayObjectAdapter;
//import androidx.leanback.widget.HeaderItem;
//import androidx.leanback.widget.ImageCardView;
//import androidx.leanback.widget.ListRow;
//import androidx.leanback.widget.ListRowPresenter;
//import androidx.leanback.widget.OnItemViewClickedListener;
//import androidx.leanback.widget.OnItemViewSelectedListener;
//import androidx.leanback.widget.Presenter;
//import androidx.leanback.widget.Row;
//import androidx.leanback.widget.RowPresenter;
//import androidx.core.app.ActivityOptionsCompat;
//
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//
//public class SearchFragment extends SearchSupportFragment {
//
//    private static final String TAG = "SearchFragment";
//
//    private static final int BACKGROUND_UPDATE_DELAY = 300;
//    private static final int GRID_ITEM_WIDTH = 200;
//    private static final int GRID_ITEM_HEIGHT = 200;
//
////    ImageView logo;
//
//
//    private Drawable mDefaultBackground;
//    private DisplayMetrics mMetrics;
//    private BackgroundManager mBackgroundManager = null;
//
//    private static ArrayObjectAdapter rowsAdapter;
//    private static CardPresenter cardPresenter;
//    private static ArrayObjectAdapter listRowAdapter;
//    private static HeaderItem header;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(null);
//        setupUIElements();
//        loadRows();
//        prepareBackgroundManager();
//        setupEventListeners();
//    }
//
//    @Override
//    public void onDestroy() {
////        if (null != mBackgroundTimer)
////        {
////            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
////            mBackgroundTimer.cancel();
//////            mBackgroundManager = null;
////        }
//        super.onDestroy();
//    }
//
//    @Override
//    public void onStop() {
////        mBackgroundTimer.cancel();
////        mBackgroundManager.release();
////        mBackgroundManager = null;
//        super.onStop();
//    }
//
//    private void loadRows() {
//
//        HashMap<String, ArrayList<Movie>> list = MovieList.getList();
//
//        if (list != null)
//            System.out.println("GOT MOVIES!!!!!!!!!!!!!!!!!!!!!!!!");
//
//        ArrayList<String> Cats = MovieList.getCats();
//
//        int NUM_ROWS = list.size();
//
//
//        ListRowPresenter check = new ListRowPresenter();
//        check.setShadowEnabled(false);
//        check.enableChildRoundedCorners(true);
//
//        rowsAdapter = new ArrayObjectAdapter(check);
//        cardPresenter = new CardPresenter();
//
//
//        for (int i = 0; i < NUM_ROWS; i++)
//        {
//
//            listRowAdapter = new ArrayObjectAdapter(cardPresenter);
//
//            String thisCat = Cats.get(i);
//            System.out.print(thisCat + "      ");
//
//            for (int j = 0; j < list.get(thisCat).size(); j++)
//            {
//
//                listRowAdapter.add(list.get(Cats.get(i)).get(j));
//                System.out.println(listRowAdapter.get(j));
//            }
//
//            System.out.println();
//
//            header = new HeaderItem(i, Cats.get(i));
//            ListRow row = new ListRow(header, listRowAdapter);
//
//
//            rowsAdapter.add(row);
//            break;
//
//        }
//
//
//    }
//
//
//
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    private void prepareBackgroundManager() {
//
//
//
//        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
//        mMetrics = new DisplayMetrics();
//
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
//
//        int width = mMetrics.widthPixels;
//        int height = mMetrics.heightPixels;
//
////        BackgroundManager.getInstance(getActivity()).release();
//
//        mBackgroundManager = null;
//        mBackgroundManager =         BackgroundManager.getInstance((Activity) getContext());
//
//
//
//        Glide.with(getActivity())
//                .load("https://wallpaperaccess.com/full/1092758.jpg")
//                .centerCrop()
//                .error(mDefaultBackground)
//                .into(new SimpleTarget<Drawable>(width, height) {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable drawable,
//                                                @Nullable Transition<? super Drawable> transition) {
//
//                        mBackgroundManager.setAutoReleaseOnStop(false);
//                        mBackgroundManager.attach(getActivity().getWindow());
//
//                        mBackgroundManager.setDrawable(drawable);
//                    }
//                });
//
//    }
//
//    private void setupUIElements()
//    {
//        setTitle(getString(R.string.app_name));
//    }
//
//    private void setupEventListeners() {
//        setOnItemViewClickedListener(new ItemViewClickedListener());
//        setOnItemViewSelectedListener(new ItemViewSelectedListener());
//    }
//
//
//
//    private final class ItemViewClickedListener implements OnItemViewClickedListener {
//        @Override
//        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
//                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
//
//            if (item instanceof Movie) {
//                Movie movie = (Movie) item;
//
//                Log.d(TAG, "Item: " + item.toString());
//                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                intent.putExtra(DetailsActivity.MOVIE, movie);
//
//                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        getActivity(),
//                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
//                        DetailsActivity.SHARED_ELEMENT_NAME)
//                        .toBundle();
////                getActivity().startActivity(intent, bundle);
//
//            } else if (item instanceof String) {
//                if (((String) item).contains(getString(R.string.error_fragment))) {
//                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//
//    private final class ItemViewSelectedListener implements OnItemViewSelectedListener
//    {
//        @Override
//        public void onItemSelected(Presenter.ViewHolder itemViewHolder,
//                                   Object item,
//                                   RowPresenter.ViewHolder rowViewHolder,
//                                   Row row) {
//
//            if (item instanceof Movie) {
//                System.out.println("Selected: " + ((Movie) item).getTitle());
//                System.out.println("Selected: " + row.getHeaderItem().getName());
//                System.out.println("Selected: " + row.getHeaderItem().getId());
//                System.out.println("Selected: " + (ListRow)rowsAdapter.get(0));
//
//            }
//        }
//    }
//
//}