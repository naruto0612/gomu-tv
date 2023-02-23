package com.example.gomugomutv;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowId;
import android.webkit.CookieManager;
import android.webkit.WebStorage;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRow;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class navigation_activity extends FragmentActivity {

    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            setContentView(R.layout.activity_nav);
            navView = findViewById(R.id.design_navigation_view);
            navView.setOnItemSelectedListener(navLister);

            findViewById(R.id.nav_home).setNextFocusLeftId(R.id.nav_home);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MainFragment())
                    .commitNowAllowingStateLoss();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        boolean retval = super.onKeyDown(keyCode, event);


        System.out.println("ACTION: " + event.getKeyCode() + " " + MainFragment.get_selected_row() + " " + getCurrentFocus().getClass().getName());
        if (event.getKeyCode() == 19 && MainFragment.get_selected_row() == 0)
            findViewById(navView.getSelectedItemId()).requestFocus();
        else if (event.getKeyCode() == 20 && MainFragment.get_selected_row() == 0 && getCurrentFocus().getClass().getName().equals("com.example.gomugomutv.CardPresenter$1"))
            findViewById(R.id.design_navigation_view).setVisibility(View.INVISIBLE);
        else if (event.getKeyCode() == 19 && MainFragment.get_selected_row() == 1)
            findViewById(R.id.design_navigation_view).setVisibility(View.VISIBLE);

        return retval;
    }



    private NavigationBarView.OnItemSelectedListener navLister = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected;
            int id = item.getItemId();

            if (id == R.id.nav_home){
                selected = new MainFragment();
            }
            else if (id == R.id.nav_search) {
                selected = new SearchFragment();
            } else {
                selected = new MainFragment();
            }

            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commitNowAllowingStateLoss();
            }

            findViewById(R.id.frame_progress_bar).setVisibility(View.VISIBLE);


            new change_fragment(selected).execute();


            System.out.println("focusing");

            return true;
        }
    };

    private final class change_fragment extends AsyncTask<String, Void, ArrayList<Movie>> {

        private Fragment selected;


        public change_fragment(Fragment selected){
            this.selected = selected;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... voids) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selected)
                            .commitAllowingStateLoss();
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> voids) {
            super.onPostExecute(voids);

            findViewById(R.id.frame_progress_bar).setVisibility(View.INVISIBLE);

        }
    }

}