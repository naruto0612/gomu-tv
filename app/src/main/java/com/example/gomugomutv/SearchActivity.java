package com.example.gomugomutv;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.fragment.app.FragmentActivity;
//
//import java.util.ArrayList;
//
//public class SearchActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {
//
//    String[][] types = {{"Title", "Actor", "Director", "Year"},
//                        { "Telugu", "Hindi", "Tamil", "Malyalam",
//                            "Kannada", "Bengali", "Marathi", "Punjabi"}};
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        ArrayList<Integer> spinners = new ArrayList<>();
//        spinners.add(R.id.languages);
//        spinners.add(R.id.by);
//
//        int y = 0;
//        for (int x: spinners) {
//
//            Spinner spinner = (Spinner) findViewById(R.id.languages);
//            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types[y]);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(adapter);
//            y++;
//
//        }
//
//
////        if (savedInstanceState == null) {
////            getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.details_fragment, new SearchFragment())
////                    .commitNow();
////        }
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
//}


import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class SearchActivity extends FragmentActivity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.search_view, new SearchFragment())
                    .commitNow();
        }

    }

}

