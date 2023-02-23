package com.example.gomugomutv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.zip.GZIPInputStream;


public final class SearchList {

    private static long count = 0;

    private static HashMap<String, ArrayList<Movie>> map;

    private static  HashMap<String, Movie> movies = new HashMap<>();

    private static  ArrayList<String> CATOGERIES = new ArrayList<>();

    private static  ArrayList<Thread> THREADS = new ArrayList<>();
    static String QUERY;


    public static HashMap<String, ArrayList<Movie>> setupMovies()
    {
        map = new HashMap<>();
        CATOGERIES = new ArrayList<>();
        THREADS = new ArrayList<>();

        Thread recents;



        map.put("Telugu", new ArrayList<>());
        CATOGERIES.add("Telugu");

        map.put("Tamil", new ArrayList<>());
        CATOGERIES.add("Tamil");

        map.put("Hindi", new ArrayList<>());
        CATOGERIES.add("Hindi");

        map.put("Malyalam", new ArrayList<>());
        CATOGERIES.add("Malyalam");

        map.put("MovieRulz", new ArrayList<>());
        CATOGERIES.add("MovieRulz");


        recents = new Thread(new Runnable() {
            @Override
            public void run() {

                enthusian("https://einthusan.tv/movie/results/?lang=telugu&query=" + QUERY, "Telugu");

            }
        });
        recents.start();
        THREADS.add(recents);

        recents = new Thread(new Runnable() {
            @Override
            public void run() {

                enthusian("https://einthusan.tv/movie/results/?lang=tamil&query=" + QUERY, "Tamil");

            }
        });
        recents.start();
        THREADS.add(recents);



        recents = new Thread(new Runnable() {
            @Override
            public void run() {

                enthusian("https://einthusan.tv/movie/results/?lang=hindi&query=" + QUERY, "Hindi");

            }
        });
        recents.start();
        THREADS.add(recents);

        recents = new Thread(new Runnable() {
            @Override
            public void run() {

                enthusian("https://einthusan.tv/movie/results/?lang=malayalam&query=" + QUERY, "Malyalam");

            }
        });
        recents.start();
        THREADS.add(recents);

        recents = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    movieRulz("https://" + booting_activity.MOVIERULZLINK + "/?s=" + QUERY);
                } catch (Exception ex){
                    System.out.println("No Movie Rulz");
                }
            }
        });

        recents.start();
        THREADS.add(recents);

        System.out.println("ALL THREADS: " + THREADS.size());


        do {


            for (int i = 0; i < THREADS.size(); i++) {

                if (!THREADS.get(i).isAlive()) {
                    THREADS.remove(THREADS.get(i));
                }

            }


        } while (THREADS.size() != 0);

        return map;

    }













    public static HashMap<String, ArrayList<Movie>> getList() {
        if (map == null) {
            System.out.print("TAKING LONGGGG HEERERER");
            map = setupMovies();
        }
        return map;
    }

    public static void clearQuery(){
        map = null;
        CATOGERIES = null;
    }

    public static ArrayList<String> getCats() {
        if (map == null) {
            System.out.print("TAKING LONGGGG HEERERER");
            map = setupMovies();
        }
        return CATOGERIES;
    }

    private static Movie buildMovieInfo(
            String title,
            String description,
            String videoUrl,
            String cardImageUrl,
            String source) {


        Movie movie = new Movie();
        // Date timestamp1 = new java.util.Date();
        // (milliseconds/ 1000) % 60)
        // long milliseconds = (timestamp1.getTime()-timestamp.getTime());
        // System.out.println(((milliseconds/ 1000) % 60) +  " " + count);
        movie.setId(count++);
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio("STUDIO");
        movie.setCardImageUrl(cardImageUrl);
        movie.setBackgroundImageUrl("https://wallpaperaccess.com/full/1092758.jpg");
        movie.setVideoUrl(videoUrl);
        movie.setSource(source);
        return movie;


    }


//            FileInputStream fis = MyApplication.getContext().openFileInput("moviedata");


    private static void movieRulz(String p)
    {

        String results = getHTML(p);

        results = results.substring(results.indexOf("<div class=\"content home_style\">"), results.indexOf("<nav id=\"posts-nav\" class=\"paged-navigation contain\">"));

        int i = 0;

        while(results.indexOf("href=\"", i) != -1) {


            int x = results.indexOf("href=\"", i) + 6;
            int y = results.indexOf("\"", x);
            String linkToMovie = results.substring(x, y);



            String links = getHTML(linkToMovie);
            links = links.substring(links.indexOf("<span style=\"color: #ff00ff;\">"), links.indexOf("<nav id=\"post-nav\" class=\"contain\">"));
            String description;
            String link;


            if(links.toLowerCase().contains("tape")){

                try{
                    int startThis = links.indexOf("https://downsscrs.xyz/?p=", links.indexOf("tape"));
                    linkToMovie = links.substring(startThis, links.indexOf("\"", startThis));
                    String CheckHtml = getHTML(linkToMovie);

                    if (!CheckHtml.contains("<iframe")) {

                        link = "https://DIDNOTFIND";
                        description = "Not Available";


                    } else {

                        int start = CheckHtml.indexOf("<iframe");
                        int start1 = CheckHtml.indexOf("https://", start);
                        CheckHtml = CheckHtml.substring(start1, CheckHtml.indexOf("\"", start1));

                        link = CheckHtml;
                        description = "Available";

                    }

                } catch (Exception ex){
                    link = "https://DIDNOTFIND";
                    description = "Not Available";
                }

            } else {

                link = "https://DIDNOTFIND";
                description = "Not Available";

            }


            x = results.indexOf("title=\"", i) + 7;
            y = results.indexOf("\"", x);
            String title = results.substring(x, y);

            x = results.indexOf("src=\"", i) + 5;
            y = results.indexOf("\"", x);
            String img = results.substring(x, y);

            System.out.println(title + " " + link);

            map.get("MovieRulz").add(buildMovieInfo(title, description, link, img, "MOVIERULZ"));

            i = y;

        }
    }

    private static void enthusian(String p, String input){

//        System.out.println(p);

        String mainHtml = getHTML(p);

        while (mainHtml.contains("<title>Rate Limited - Einthusan</title>"))
        {
            mainHtml = getHTML(p);
        }

        int mainHtmlStart = mainHtml.indexOf("<section id=\"UIMovieSummary\">");
        mainHtml = mainHtml.substring(mainHtmlStart, mainHtml.indexOf("</section>", mainHtmlStart));

        int i = 0;
        while(mainHtml.indexOf("<div class=\"block1\">", i) != -1)
        {

            int x = mainHtml.indexOf("<div class=\"block1\">", i);

            int start = mainHtml.indexOf("<img src=\"//img.einthusan.io/etv/", x) + 10;
            String image = mainHtml.substring(start, mainHtml.indexOf("\"", start));
            String title = mainHtml.substring(mainHtml.indexOf("<h3>", x) + 4, mainHtml.indexOf("</h3>", x));

            start = mainHtml.indexOf("<a data-disabled=\"false\" href=\"/movie/watch/", x) + 44;
            String linkToMovie = "https://einthusan.tv/movie/watch/" + mainHtml.substring(start, mainHtml.indexOf("\">", start));

            String description;
            try{
                start = mainHtml.indexOf("<p class=\"synopsis\">", x) + 20;
                description = mainHtml.substring(start, mainHtml.indexOf("</p>", start));
            } catch (Exception exception){
                start = mainHtml.indexOf("<p class=\"submit-synopsis\">", x) + 27;
                description = mainHtml.substring(start, mainHtml.indexOf("</p>", start));
            }

            Movie thisMovie = movies.get(linkToMovie);
            boolean gotMovie = false;

            if (thisMovie != null){
                map.get(input).add(thisMovie);
                System.out.println(thisMovie);
                gotMovie = true;
            }


            if(!gotMovie){

                thisMovie = buildMovieInfo(title, description, linkToMovie, "https:" + image, "enthusan");
                map.get(input).add(thisMovie);
                movies.put(linkToMovie, thisMovie);

            }

            i = mainHtml.indexOf("</h3>", x);

        }

    }

    public static String getHTML(String url){


        String htmlString = "";
        StringBuilder html = new StringBuilder();
        HttpURLConnection conn;
        try {

            URL url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.connect();
            // Read and store the result line by line then return the entire string.
            InputStream in = conn.getInputStream();
            BufferedReader reader;

            if ("gzip".equals(conn.getContentEncoding())) {
                reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(in)));
            }
            else {
                reader = new  BufferedReader(new InputStreamReader(conn.getInputStream()));
            }

            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line).append("\n");
            }
            in.close();
            reader.close();
            conn.disconnect();

            htmlString = html.toString();



        } catch (IOException e) {

            System.out.println("error for " + url);

        }

        return htmlString;


    }

}