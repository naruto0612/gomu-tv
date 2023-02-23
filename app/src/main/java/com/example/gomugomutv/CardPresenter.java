package com.example.gomugomutv;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.RequiresApi;
import androidx.leanback.widget.BaseCardView;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/*
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 */
public class CardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    private static final int CARD_WIDTH = 240;
    private static final int CARD_HEIGHT = 378;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;


    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
//        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view"s background is temporarily visible
        // during animations.

        if (selected){

            int color = sSelectedBackgroundColor;
            view.setBackgroundColor(color);
            view.setInfoAreaBackgroundColor(color);
            view.setInfoVisibility(View.VISIBLE);
//            view.setMainImageDimensions((int) (CARD_WIDTH*1.15), (int) (CARD_HEIGHT*1.15));


        } else {
            int color = sDefaultBackgroundColor;
            view.setBackgroundColor(color);
            view.setInfoAreaBackgroundColor(color);
            view.setInfoVisibility(View.INVISIBLE);
//            view.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);


        }
//        view.setMainImageDimensions((int) (CARD_WIDTH*1.2), (int) (CARD_HEIGHT*1.2));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");

        sDefaultBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.transparent);
        sSelectedBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.tint);
        /*
          This template uses a default image in res/drawable, but the general case for Android TV
          will require your resources in xhdpi. For more information, see
          https://developer.android.com/training/tv/start/layouts.html#density-resources
         */
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);

        ImageCardView cardView =  new ImageCardView(new ContextThemeWrapper(parent.getContext(), R.style.MovieCardSimpleTheme)) {
                    @Override
                    public void setSelected(boolean selected) {
                        updateCardBackgroundColor(this, selected);
                        super.setSelected(selected);
                    }
                };



        System.out.println(cardView.getElevation());
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Movie movie = (Movie) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        Log.d(TAG, "onBindViewHolder");

        if (movie.getCardImageUrl() != null)
        {
            cardView.setTitleText(movie.getTitle());
            cardView.setInfoVisibility(View.INVISIBLE);
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            cardView.setCardType(BaseCardView.CARD_TYPE_INFO_OVER);
            Glide.with(viewHolder.view.getContext())
                    .load(movie.getCardImageUrl())
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());
        }

        cardView.setElevation(0);

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory

        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
        cardView.setElevation(0);

    }
}