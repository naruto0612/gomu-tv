package com.example.gomugomutv;
import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.monstertechno.adblocker.AdBlockerWebView;

import java.io.IOException;
import java.io.InputStream;


public class webView extends Activity {

    public static String thisMovie = null;
    WebView browser;
    Button refreshButton;


    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activty_web);
        Toast.makeText(this, "Please wait, generating url", Toast.LENGTH_LONG).show();

        if (thisMovie != null) {
            browser = findViewById(R.id.webview);

            new AdBlockerWebView.init(this).initializeWebView(browser);



            browser.setWebViewClient(new WebViewClient() {

                @SuppressWarnings("deprecation")
                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, String url) {


                    if (!url.contains("vidfy.me") || !url.contains("vidhub.ch"))
                        System.out.println(url);
//                        return AdBlocker.createEmptyResource();



                    return AdBlockerWebView.blockAds(view,url) ? AdBlocker.createEmptyResource() :
                            super.shouldInterceptRequest(view, url);

//                    return super.shouldInterceptRequest(view, url);

//                    if (url.contains("fvs.io"))
//                    {
//
//                        System.out.println("MOVIELINK: " + url);
//                        thisMovie.setVideoUrl(url);
////                        browser.evaluateJavascript("(function() {return document.getElementsByTagName('button')[2].innerHTML;})();", value -> {
////                            JsonReader reader = new JsonReader(new StringReader(value));
////                            reader.setLenient(true);
////                            try {
////                                if (reader.peek() == JsonToken.STRING) {
////                                    String domStr = reader.nextString();
////                                    if (domStr != null) {
////                                        Log.d("BUTTON", domStr);
////                                    }
////                                }
////                            } catch (IOException e) {
////                                // handle exception
////                            } finally {
////                                try {
////                                    reader.close();
////                                } catch (IOException e) {
////                                    e.printStackTrace();
////                                }
////                            }
////                        });
//                        KillMe();
//
//                        return AdBlocker.createEmptyResource();
//                    }
//
//                    String[] GO = {"yandex", "ncdnstm", "fvs.io"};
//
//                    for (String go : GO)
//                    {
//                        if (url.toLowerCase().contains(go))
//                        {
//                            System.out.println("REQUESTING: " + url);
//                            return super.shouldInterceptRequest(view, url);
//                        }
//                    }
//


                }

//                public boolean shouldOverrideUrlLoading(WebView webview, String url) {
//                    return !url.startsWith(thisMovie);
//                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

//                    injectScriptFile(view); // see below ...
//
//                    // test if the script was loaded
//                    view.loadUrl("javascript:setTimeout(test(), 0)");
//                    view.loadUrl("javascript:setTimeout(test('svg'), 10)");

                }

                private void injectScriptFile(WebView view) {
                    InputStream input;
                    try {
                        input = getAssets().open("script.js");
                        byte[] buffer = new byte[input.available()];
                        input.read(buffer);
                        input.close();

                        // String-ify the script byte-array using BASE64 encoding !!!
                        String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                        view.loadUrl("javascript:(function() {" +
                                "var parent = document.getElementsByTagName('head').item(0);" +
                                "var script = document.createElement('script');" +
                                "script.type = 'text/javascript';" +
                                // Tell the browser to BASE64-decode the string into your script !!!
                                "script.innerHTML = window.atob('" + encoded + "');" +
                                "parent.appendChild(script)" +
                                "})()");

                    } catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }




                }


            });
//
//
//            browser.setWebViewClient(new WebViewClient());

            String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
            browser.getSettings().setUserAgentString(newUA);

            refreshButton = findViewById(R.id.refresh);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

//                    webView.this.browser.loadUrl(thisMovie);
                    webView.this.browser.reload();

                }});

            if (Build.VERSION.SDK_INT >= 19) {
                browser.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
            else {
                browser.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            WebSettings webSettings = browser.getSettings();
            webSettings.setJavaScriptEnabled(true);
            browser.setInitialScale(30);
            browser.getSettings().setLoadWithOverviewMode(true);
            browser.getSettings().setUseWideViewPort(true);

            browser.loadUrl(thisMovie);
            browser.requestFocus();
            browser.setVerticalScrollBarEnabled(true);



            super.onCreate(null);

        }
    }

    @Override
    public void onBackPressed (){

        browser.post(() -> {

            browser.removeAllViews();
            browser.destroy();
        });

        finish();

        super.onBackPressed();

    }


    public void KillMe(){

        browser.post(() -> {

            browser.removeAllViews();
            browser.destroy();
        });

        WebStorage.getInstance().deleteAllData();

        Intent intent = new Intent(webView.this, PlaybackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(DetailsActivity.MOVIE, thisMovie);

        startActivity(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
