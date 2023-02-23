package com.example.gomugomutv;
import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.monstertechno.adblocker.AdBlockerWebView;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;
import android.widget.Toast;

import com.monstertechno.adblocker.AdBlockerWebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/**
 * Handles video playback with media controls.
 */
public class PlaybackVideoFragment extends VideoSupportFragment {

    private static VideoPlayerGlue mTransportControlGlue;
    public static VideoSupportFragmentGlueHost glueHost;
    private static int current_video;
    private static boolean WAIT = false;
    private static MediaPlayerAdapter playerAdapter;
    private static Movie movie;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        root.setBackgroundResource(android.R.color.black);

        return root;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        current_video = 0;

        movie = (Movie) getActivity().getIntent().getSerializableExtra(DetailsActivity.MOVIE);


        glueHost = new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);
        playerAdapter = new MediaPlayerAdapter(getActivity());
        mTransportControlGlue = new VideoPlayerGlue(getActivity(), playerAdapter);

        mTransportControlGlue.setControlsOverlayAutoHideEnabled(true);
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.NONE);
        mTransportControlGlue.setHost(glueHost);
        mTransportControlGlue.setTitle(movie.getTitle());
        mTransportControlGlue.playWhenPrepared();
        mTransportControlGlue.setSeekEnabled(true);



        Thread gettingVideo = new Thread(new Runnable() {
            @Override
            public void run() {
                if (movie.getSource().equals("enthusan"))
                {
                    String doc = "";

                    while(doc.equals("") || doc.contains("https://cdn.jsdelivr.net/npm/jquery@3.3.1/dist/jquery.min.js")){
                        doc = MovieList.getHTML(movie.getVideoUrl(0));
                        int z = doc.indexOf("data-mp4-link=\"");
                        int a1 = doc.indexOf("https", z);
                        doc = doc.substring(a1, doc.indexOf("\"", a1));
                    }

                    doc = doc.replace(doc.substring(0, doc.indexOf("/etv")), "https://cdn1.einthusan.io");
                    doc = doc.replace(doc.substring(doc.indexOf("amp"), doc.indexOf(";") + 1), "");

                    movie.setVideoUrl(doc);

                    playerAdapter.setDataSource(Uri.parse(movie.getVideoUrl(0)));


                }
                else if (movie.getVideoUrl(0).contains("movierulz")){

                    String linkToMovie = movie.getVideoUrl(0);

                    String links = MovieList.getHTML(linkToMovie);

                    links = links.substring(links.indexOf("<span style=\"color: #ff00ff;\">"), links.indexOf("<nav id=\"post-nav\" class=\"contain\">"));
                    String description;
                    String link;


                    if(links.toLowerCase().contains("streamtape")){

                        int startThis = links.indexOf("https://downlscr.xyz/?p=", links.indexOf("streamtape"));
                        linkToMovie = links.substring(startThis, links.indexOf("\"", startThis));
                        String CheckHtml = MovieList.getHTML(linkToMovie);

                        if (!CheckHtml.contains("<iframe")) {

                            link = "https://DIDNOTFIND";
                            description = "Not Available";


                        } else {

                            int start = CheckHtml.indexOf("<iframe");
                            int start1 = CheckHtml.indexOf("https://", start);
                            CheckHtml = CheckHtml.substring(start1, CheckHtml.indexOf("\"", start1));

                            System.out.println("Found: " + CheckHtml);

                            link = CheckHtml;
                            description = "Available";

                            if (link.contains("streamtape")){ link = "https://DIDNOTFIND"; description = "Not Available"; }

                            else if (link.contains("ncdnstm")){

                                requireActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getContext(), "Please wait, generating url", Toast.LENGTH_LONG).show();

                                    }
                                });


                                //                    webView.thisMovie = movie;
                                //                    Intent intent = new Intent(getActivity(), webView.class);
                                //                    startActivity(intent);


                            }


                        }

                    } else {

                        link = "https://DIDNOTFIND";
                        description = "Not Available";

                    }


                    movie.setVideoUrl(link);
                    movie.setDescription(description);

                }

                if (movie.getVideoUrl(current_video).contains("ncdnstm") || movie.getVideoUrl(current_video).contains("vidhub.ch")){

                    WAIT = true;
                    new runWebView(movie.getVideoUrl(current_video)).execute();
                    requireActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "Please wait, generating url", Toast.LENGTH_LONG).show();

                        }
                    });

//                    webView.thisMovie = movie;
//                    Intent intent = new Intent(getActivity(), webView.class);
//                    startActivity(intent);


                }

            }
        });


        gettingVideo.start();
        try {
            gettingVideo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }





        if (movie.getVideoUrl(0).contains("DIDNOTFIND")){

            System.out.println("NO VIDEO");
            Toast.makeText(getActivity(), "NO SOURCES FOUND", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();

        }

        System.out.println("Playing: " + movie.getVideoUrl(0));



        playerAdapter.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                System.out.println("COMPLETION CALLED");
                if (!WAIT){
                    System.out.println("COMPLETED " + current_video + " " + movie.getVideoUrlSize());
                    if (current_video == movie.getVideoUrlSize()-1) {
                        requireActivity().onBackPressed();
                        onDestroy();
                    } else {

                        current_video++;
                        playerAdapter.setDataSource(Uri.parse(movie.getVideoUrl(current_video)));

                    }
                }
            }
        });


    }


    @Override
    protected void onVideoSizeChanged(int width, int height) {

        int screenWidth = PlaybackActivity.width;
        SurfaceView surfaceView = getSurfaceView();
        ViewGroup.LayoutParams p = surfaceView.getLayoutParams();

        p.width = screenWidth;
        p.height = screenWidth * height / width;
        System.out.println("width: " + p.width + " height: " + p.height);

        surfaceView.setLayoutParams(p);
    }

    public static void setSeekTo(long seconds) {

        System.out.println("MOVING TO: " + seconds);
        mTransportControlGlue.seekTo(seconds);

    }

    public static long getDuration()
    {
        return mTransportControlGlue.getDuration();
    }

    public static Movie getCurMovie()
    {
        return movie;
    }


    public static int getCurrent_video()
    {
        return current_video;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mTransportControlGlue != null) {
            mTransportControlGlue.pause();
        }
    }

    public final class runWebView extends AsyncTask<String, Void, ArrayList<Movie>> {

        String movie_link;
        WebView browser;

        public runWebView(String movie_link){
            this.movie_link = movie_link;
//            requireActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    browser = new WebView(getContext());
//                }
//            });
            browser = requireActivity().findViewById(R.id.webview2);
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        protected ArrayList<Movie> doInBackground(String... voids) {

            browser.post(() -> {

                new AdBlockerWebView.init(getContext()).initializeWebView(browser);

                browser.setWebViewClient(new WebViewClient() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                        if (url.contains("fvs.io") || url.contains("http://vidfy.me/media/"))
                        {

                            System.out.println("MOVIELINK: " + url);
                            movie_link = url;
                            WAIT=false;
                            KillMe();
                            return AdBlocker.createEmptyResource();
                        }

                        String[] NOGO = {"jpg", "png", "manatelugu", "google", "ad", "css", "network"};

                        for (String nogo : NOGO)
                        {
                            if (url.toLowerCase().contains(nogo))
                            {
                                System.out.println("Not doing: " + nogo);
                                return AdBlocker.createEmptyResource();
                            }
                        }

                        System.out.println(url);

                        return AdBlockerWebView.blockAds(view,url) ? AdBlocker.createEmptyResource() :
                                super.shouldInterceptRequest(view, url);

//                        return AdBlocker.createEmptyResource();


                    }

//                    public boolean shouldOverrideUrlLoading(WebView webview, String url) {
//                        return !url.startsWith(movie_link);
//                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        injectScriptFile(view);
                        view.loadUrl("javascript:setTimeout(test(), 0)");
                    }

                    private void injectScriptFile(WebView view) {
                        InputStream input;
                        try {
                            input = requireActivity().getAssets().open("script.js");
                            byte[] buffer = new byte[input.available()];
                            input.read(buffer);
                            input.close();

                            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                            view.loadUrl("javascript:(function() {" +
                                    "var parent = document.getElementsByTagName('head').item(0);" +
                                    "var script = document.createElement('script');" +
                                    "script.type = 'text/javascript';" +
                                    // Tell the browser to BASE64-decode the string into your script !!!
                                    "script.innerHTML = window.atob('" + encoded + "');" +
                                    "parent.appendChild(script)" +
                                    "})()");

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }


                });

                String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
                browser.getSettings().setUserAgentString(newUA);

                WebSettings webSettings = browser.getSettings();
                webSettings.setJavaScriptEnabled(true);

                browser.loadUrl(movie_link);
                browser.requestFocus();
                browser.setVerticalScrollBarEnabled(true);
            });
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> voids) {

            super.onPostExecute(voids);
        }

        public void KillMe(){

            browser.post(() -> {
                browser.removeAllViews();
                browser.loadUrl("");
//                browser.destroy();
            });

            WebStorage.getInstance().deleteAllData();

            requireActivity().runOnUiThread(new Runnable() {
                public void run() {

                    playerAdapter.setDataSource(Uri.parse(movie_link));
                    mTransportControlGlue.playWhenPrepared();

                    if (current_video != 0)
                        mTransportControlGlue.setTitle(movie.getTitle() + " Part" + current_video);
                    else
                        mTransportControlGlue.setTitle(movie.getTitle());

                    playerAdapter.play();
                    playerAdapter.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {

                            System.out.println("COMPLETION CALLED");
                            if (!WAIT){
                                System.out.println("COMPLETED " + current_video + " " + movie.getVideoUrlSize());
                                if (current_video == movie.getVideoUrlSize()-1) {
                                    requireActivity().onBackPressed();
                                    onDestroy();
                                } else {

                                    playerAdapter.setDataSource(Uri.parse("https://www.google.com"));

                                    current_video++;
                                    new runWebView(movie.getVideoUrl(current_video)).execute();

//                            playerAdapter.setDataSource(Uri.parse(movie.getVideoUrl(current_video)));

                                }
                            }
                        }
                    });

                }
            });


//            Intent intent = new Intent(getContext(), PlaybackActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra(DetailsActivity.MOVIE, movie);
//
//            startActivity(intent);

        }

    }


}