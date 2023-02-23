package com.example.gomugomutv;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class UpdateActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Button updateButton = findViewById(R.id.updater);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveStoragePermission()){
                    UpdateApp updateTask = new UpdateApp();
                    updateTask.setContext(getApplicationContext());
                    updateTask.execute();
                }
            }
        });



    }






    private final class UpdateApp extends AsyncTask<Void, Void, String> {

        private Context context;
        String location;
        boolean got = false;

        public void setContext(Context contextf){
            context = contextf;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://raw.githubusercontent.com/arundeegutla/Gomu-Gomu-TV/main/gomugomu.apk");
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(false);
                c.connect();
                String PATH = Environment.getExternalStorageDirectory()+"/GomuGomu/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file,"gomugomu.apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    System.out.println(len1);
                }
                fos.close();
                is.close();
                got = true;
                location = PATH;


            } catch (Exception e) {

                got = false;
                Log.e("fd", "Update Error: " + e.getMessage());
                e.printStackTrace();
            }

        return null;

        }

        @Override
        protected void onPostExecute(String voids) {
            super.onPostExecute(voids);
            if (got){
                OpenNewVersion(location);
            }
        }

    }

    void OpenNewVersion(String location) {
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(location + "app-debug.apk"));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getApplicationContext().startActivity(intent);
    }


    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {
                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
                return false;
            }
        }
        else {
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            AsyncTask<Void, Void, String> updateTask = new UpdateApp();
            updateTask.execute();
        }
    }
}
