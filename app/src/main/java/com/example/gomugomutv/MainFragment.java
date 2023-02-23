package com.example.gomugomutv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebStorage;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import jp.wasabeef.glide.transformations.BlurTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class MainFragment extends BrowseSupportFragment {
    private static final String TAG = "MainFragment";


    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private BackgroundManager mBackgroundManager = null;
    public static long selected_row;

    public static long get_selected_row(){
        return selected_row;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);;



        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setupUIElements();
        loadRows();

        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
        mMetrics = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

//        prepareBackgroundManager();
        setupEventListeners();


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void loadRows() {

        HashMap<String, ArrayList<Movie>> list = MovieList.getList();
        if (list != null)
            System.out.println("GOT MOVIES!!!!!!!!!!!!!!!!!!!!!!!!");
        int NUM_ROWS = list.size();


        ListRowPresenter check = new ListRowPresenter();
        check.setShadowEnabled(false);
        check.enableChildRoundedCorners(true);


        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(check);
        CardPresenter cardPresenter = new CardPresenter();

        ArrayList<String> Cats = MovieList.getPages();

        for (int i = 0; i < NUM_ROWS; i++)
        {

            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);

            String thisCat = Cats.get(i);
            System.out.print(thisCat + "      ");

            for (int j = 0; j < list.get(thisCat).size(); j++) {
                listRowAdapter.add(list.get(Cats.get(i)).get(j));
                System.out.println(listRowAdapter.get(j));
            }

            System.out.println();

            HeaderItem header = new HeaderItem(i, Cats.get(i));
            ListRow row = new ListRow(header, listRowAdapter);



            rowsAdapter.add(row);
        }

        setAdapter(rowsAdapter);
    }




    @SuppressLint("UseCompatLoadingForDrawables")
    private void prepareBackgroundManager() {



        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
        mMetrics = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;

//        BackgroundManager.getInstance(getActivity()).release();

        mBackgroundManager = null;

        mBackgroundManager = BackgroundManager.getInstance(getActivity());

//        mBackgroundManager = BackgroundManager.getInstance(getActivity());



        Glide.with(getActivity())
                .load("https://wallpaperaccess.com/full/1092758.jpg")
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable,
                                                @Nullable Transition<? super Drawable> transition) {

                        mBackgroundManager.setAutoReleaseOnStop(false);

                        mBackgroundManager.attach(getActivity().getWindow());

                        mBackgroundManager.setDrawable(drawable);
                    }
                });

    }

    private void setupUIElements()
    {
        // over title
        setHeadersState(HEADERS_DISABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
    }

    private void setupEventListeners() {

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
        setMenuVisibility(true);

    }

    private void updateBackground1() {

        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;


        Glide.with(getActivity())
                .load("https://wallpaperaccess.com/full/1092758.jpg")
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .error(mDefaultBackground)
                .into(new SimpleTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable,
                                                @Nullable Transition<? super Drawable> transition) {

                        mBackgroundManager.setDrawable(drawable);
                    }
                });

//        mBackgroundTimer.cancel();
    }


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;

//                Objects.requireNonNull(getActivity()).findViewById(R.id.nav_search).requestFocus();

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

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener
    {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {

            if (item instanceof Movie) {

                Movie movie = (Movie) item;

                System.out.println("Selected: " + ((Movie) item).getTitle() + " " + ((Movie) item).getStudio() + " " +((Movie) item).getVideoUrl(0));
                System.out.println("Selected: " + row.getHeaderItem().getName());

                selected_row = row.getHeaderItem().getId();


                if (movie.isRequester()) {

                    movie.setRequester(false);
                    ListRow listRow = ((ListRow) getAdapter().get((int) row.getHeaderItem().getId()));

                    ArrayObjectAdapter listRowAdapter = ((ArrayObjectAdapter) listRow.getAdapter());
                    new ADDMOVIES(row.getHeaderItem().getName(), listRowAdapter).execute();

                }

            }
        }
    }

    private final class ADDMOVIES extends AsyncTask<String, Void, ArrayList<Movie>> {

        private String header;
        private ArrayObjectAdapter listRowAdapter;
        private ArrayList<Movie> newMovies;

        public ADDMOVIES(String header, ArrayObjectAdapter listRowAdapter){
            this.header = header;
            this.listRowAdapter = listRowAdapter;
            this.newMovies = null;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... voids) {
            newMovies = MovieList.getNewMovies(header);

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> voids) {
            for (Movie thisMovie: newMovies) {
                listRowAdapter.add(thisMovie);
            }
            super.onPostExecute(voids);
        }
    }

}