package com.example.gomugomutv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;


public final class MovieListRefined {


    private static long count = 0;



    private static HashMap<String, ArrayList<Movie>> map;







    public static HashMap<String, ArrayList<Movie>> setupMovies() {

        map = new HashMap<>();

        // movierulz loop
        // map.put("movierulz", new Arraylist<>())
        // map.get("movierulz").add(Movie1);

        // searching
        // map.contains("SourceName") != null // exists


        // map.add //FOR HASHSET

        Thread first = new Thread(new Runnable() {
            @Override
            public void run() {


                movieRulz("https://ww5.7movierulz.pe/telugu-movie/");

                for(int l = 2; l<4; l++){
                    System.out.println("MOVIERULZ");

                    movieRulz("https://ww5.7movierulz.pe/telugu-movie/page/" + l + "/");
                }

            }
        });

        first.start();






        Thread second = new Thread(new Runnable() {
            @Override
            public void run() {

                for(int l = 1; l<5; l++){
                    System.out.println("Enthusian 1");
                    enthusian("https://einthusan.tv/movie/results/?find=Popularity&lang=telugu&page=" + l + "&ptype=view&tp=yd", "Popular Today");
                }

            }
        });

        second.start();


        Thread third = new Thread(new Runnable() {
            @Override
            public void run() {

                for(int l = 1; l<5; l++){
                    System.out.println("Enthusian 2");


                    enthusian("https://einthusan.tv/movie/results/?decade=2020&find=Decade&lang=telugu&page=" + l, "YEAR");
                }

            }
        });

        third.start();



        Thread fourth = new Thread(new Runnable() {
            @Override
            public void run() {
                int l;

                for(l = 1; l<5; l++){

                    enthusian("https://einthusan.tv/movie/results/?find=Popularity&lang=hindi&page=" + l + "&ptype=view&tp=td", "Gindi");

                }

            }
        });

        fourth.start();



        while(true){
            if(!first.isAlive() && !second.isAlive() && !third.isAlive() && !fourth.isAlive()){

                System.out.println("FINALLLLLLLLLLLLLLLLLLLLL");
                break;
            }
        }

        return map;


    }

    public static HashMap<String, ArrayList<Movie>> getList() {
        if (map == null) {
            map = setupMovies();
        }
        return map;
    }

    private static Movie buildMovieInfo(
            String title,
            String description,
            String studio,
            String videoUrl,
            String cardImageUrl,
            String backgroundImageUrl,
            String source) {


        Movie movie = new Movie();
        movie.setId(count++);
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio(studio);
        movie.setCardImageUrl(cardImageUrl);
        movie.setBackgroundImageUrl(backgroundImageUrl);
        movie.setVideoUrl(videoUrl);
        movie.setSource(source);
        return movie;


    }



//            FileInputStream fis = MyApplication.getContext().openFileInput("moviedata");


    private static void movieRulz(String p){

        ArrayList<String> TITLES = new ArrayList<>();
        ArrayList<String> IMAGES = new ArrayList<>();
        ArrayList<String> LINKSFORVIDEO = new ArrayList<>();


        ArrayList<String> LINKTOMOVIE = new ArrayList<>();


        System.out.println(p);
        String results = getHTML(p);

        results = results.substring(results.indexOf("<div class=\"entry-content\">"), results.indexOf("<nav id=\"posts-nav\" class=\"paged-navigation contain\">"));


        int MovieRulzMovies = 0;

        int i = 0;

        while(results.indexOf("href=\"", i) != -1) {


            int x = results.indexOf("href=\"", i) + 6;
            int y = results.indexOf("\"", x);
            String linkToMovie = results.substring(x, y);
            LINKTOMOVIE.add(linkToMovie);

            x = results.indexOf("title=\"", i) + 7;
            y = results.indexOf("\"", x);
            String title = results.substring(x, y);
            TITLES.add(title);

            x = results.indexOf("src=\"", i) + 5;
            y = results.indexOf("\"", x);
            String img = results.substring(x, y);
            IMAGES.add(img);

            MovieRulzMovies++;

            i = y;


        }

        for(int j = 0; j < MovieRulzMovies; j++){

            String links = getHTML(LINKTOMOVIE.get(j));
            String linkToMovie;
            links = links.substring(links.indexOf("<span style=\"color: #ff00ff;\">"), links.indexOf("<nav id=\"post-nav\" class=\"contain\">"));




            if(links.contains("https://vupload.com")){

                linkToMovie = links.substring(links.indexOf("https://vupload.com"), links.indexOf("\"", links.indexOf("https://vupload.com")));
                String CheckHtml = getHTML(linkToMovie);

                if (!CheckHtml.contains("sources: [{src:")) {

                    LINKSFORVIDEO.add("https://DIDNOTFIND");
                    System.out.println(" FOUND VUPLOAD BUT NOT HERE "  + LINKSFORVIDEO.get(LINKSFORVIDEO.size()-1));

                } else {

                    int start = CheckHtml.indexOf("sources: [{src:");
                    int start1 = CheckHtml.indexOf("https://", start);
                    CheckHtml = CheckHtml.substring(start1, CheckHtml.indexOf("\"", start1));

                    LINKSFORVIDEO.add(CheckHtml);
                    System.out.println("FOUND " + LINKSFORVIDEO.get(LINKSFORVIDEO.size()-1));
                }


            } else {
                LINKSFORVIDEO.add("https://DIDNOTFIND");
                System.out.println(" NO VUPLOAD BUT NOT HERE "  + LINKSFORVIDEO.get(LINKSFORVIDEO.size()-1));

                // System.out.println(LINKSFORVIDEO.get(LINKSFORVIDEO.size()-1));
            }

            if (!map.containsKey("MOVIERULZ")) {
                map.put("MOVIERULZ", new ArrayList<>());
            }
            map.get("MOVIERULZ").add(buildMovieInfo(TITLES.get(j), "DESCRIPTION", "STUDIO", LINKSFORVIDEO.get(j), IMAGES.get(j), "https://wallpaperaccess.com/full/1092758.jpg", "MOVIERULZ"));

        }
    }




    private static void enthusian(String p, String input){

        System.out.println(p);

        String mainHtml = getHTML(p);

        while (mainHtml.contains("<title>Rate Limited - Einthusan</title>")) {
            mainHtml = getHTML(p);
        }


        // System.out.println("Results: " + p + " \n " + mainHtml);

        int mainHtmlStart = mainHtml.indexOf("<section id=\"UIMovieSummary\">");
        mainHtml = mainHtml.substring(mainHtmlStart, mainHtml.indexOf("</section>", mainHtmlStart));

        int i = 0;
        while(mainHtml.indexOf("<div class=\"block1\">", i) != -1){

            int x = mainHtml.indexOf("<div class=\"block1\">", i);

            String image = mainHtml.substring(mainHtml.indexOf("<img src=\"//img.einthusan.io/etv/", x) + 10, mainHtml.indexOf("\"", mainHtml.indexOf("<img src=\"//img.einthusan.io/etv/", x) +10));



            String title = mainHtml.substring(mainHtml.indexOf("<h3>", x) + 4, mainHtml.indexOf("</h3>", x));


            String linkToMovie = "https://einthusan.tv/movie/watch/" + mainHtml.substring(mainHtml.indexOf("<a data-disabled=\"false\" href=\"/movie/watch/", x) + 44, mainHtml.indexOf("\">", mainHtml.indexOf("<a data-disabled=\"false\" href=\"/movie/watch/", x) + 44));
            int y = mainHtml.indexOf("</h3>", x);



            String doc = "";

            while(doc.equals("") || doc.contains("https://cdn.jsdelivr.net/npm/jquery@3.3.1/dist/jquery.min.js")){
                doc = getHTML(linkToMovie);
                int z = doc.indexOf("data-mp4-link=\"");
                int a1 = doc.indexOf("https", z);
                doc = doc.substring(a1, doc.indexOf("\"", a1));
            }

            doc = doc.replace(doc.substring(0, doc.indexOf("/etv")), "https://cdn1.einthusan.io");
            doc = doc.replace(doc.substring(doc.indexOf("amp"), doc.indexOf(";") + 1), "");

            if (!map.containsKey(input)) {
                map.put(input, new ArrayList<>());
            }
            map.get(input).add(buildMovieInfo(title, "DESCRIPTION", "STUDIO", doc, "https:" + image, "https://wallpaperaccess.com/full/1092758.jpg", input));

            i = y;
        }


    }





    private static String getHTML(String url){


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