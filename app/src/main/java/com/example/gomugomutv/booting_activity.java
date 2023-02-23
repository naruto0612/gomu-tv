package com.example.gomugomutv;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.WebStorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class booting_activity extends FragmentActivity {

    AsyncTask<Void, Void, String> runningTask;
    private boolean updating = false;
    private String VERSION = "5";
    static String MOVIERULZLINK = "7movierulz.tc";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState == null)
        {
            setContentView(R.layout.activity_booting);
            runningTask = new RUN();
            runningTask.execute();
        }


    }


    private final class RUN extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String NEWVERSION = MovieList.getHTML("https://github.com/arundeegutla/Gomu-Gomu-TV/blob/main/README.md");
            int start = NEWVERSION.indexOf("Gomu-Gomu-TV VERSION: ") + 22;
            NEWVERSION = NEWVERSION.substring(start, NEWVERSION.indexOf("</p>", start));
            System.out.println(NEWVERSION);

            if (!NEWVERSION.equals(VERSION))
                updating = true;
            else {
                System.out.println("Running!!!!!!!");
                MovieList.setupMovies();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String voids) {
            super.onPostExecute(voids);

            if (updating){
                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                System.out.println("SETTING INTENT");
                startActivity(intent);


            }
            else{

                WebStorage.getInstance().deleteAllData();
                getCacheDir().delete();
                CookieManager.getInstance().removeAllCookies(null);
                System.out.println("COOKIES:   " + CookieManager.getInstance().hasCookies());

                new Handler().post(new Runnable() {
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(), navigation_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        booting_activity.this.finish();
                        System.out.println("DONE!!!!!!!");

                    }
                });

            }
        }
    }
}


