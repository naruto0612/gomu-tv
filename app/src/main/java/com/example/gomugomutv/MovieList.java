package com.example.gomugomutv;

import android.os.AsyncTask;

import androidx.leanback.widget.ArrayObjectAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.GZIPInputStream;


public final class MovieList {

    static int DEBUG = 0;

    private static long count = 0;

    private static HashMap<String, ArrayList<Movie>> map;
    private static HashMap<String, String> Cats;

    private static  HashMap<String, Movie> movies = new HashMap<>();
    private static  ArrayList<String> CATOGERIES = new ArrayList<>();
    private static  ArrayList<Thread> THREADS = new ArrayList<>();
    private static ArrayList<PageLoader> pages = new ArrayList<>();
    private static int RECENTLY_ADDED = 1;


    public static HashMap<String, ArrayList<Movie>> setupMovies() {
        map = new HashMap<>();
        Thread recents;

        pages.add(new PageLoader("Recently Added", "https://einthusan.tv/movie/results/?find=Recent&lang=telugu&page=", 65));
        pages.add(new PageLoader("Popular Today", "https://einthusan.tv/movie/results/?find=Popularity&lang=telugu&page=&ptype=view&tp=yd", 69));
        pages.add(new PageLoader("Tamil", "https://einthusan.tv/movie/results/?find=Popularity&lang=tamil&page=&ptype=view&tp=td", 68));
        pages.add(new PageLoader("Hindi", "https://einthusan.tv/movie/results/?find=Popularity&lang=hindi&page=&ptype=view&tp=td", 68));
        pages.add(new PageLoader("MovieRulz Telugu", "https://"+booting_activity.MOVIERULZLINK+"/telugu-movie/page//", 40));
        pages.add(new PageLoader("MovieRulz Hindi", "https://"+booting_activity.MOVIERULZLINK+"/bollywood-movie-free/page//", 48));


        for (PageLoader thisPageLoader: pages) {
            String header = thisPageLoader.getPageHeader();
            map.put(header, new ArrayList<>());
            CATOGERIES.add(header);

            recents = new Thread(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < 2; i++){
                        String link = thisPageLoader.getNewPageLink();

                        System.out.println("LINK: " + link);

                        if (link.contains("movierulz") && link.contains("page/1/")){
                            link = link.substring(0, link.indexOf("page/1/"));
                        }

                        System.out.println("LINK: " + link);
                        Objects.requireNonNull(map.get(header)).addAll(Objects.requireNonNull(getMovies(link)));
                    }


                }

            });

            recents.start();
            THREADS.add(recents);
        }

        recents = new Thread(new Runnable() {
            @Override
            public void run() {

                map.put("TV Shows", new ArrayList<>());
//                Objects.requireNonNull(map.get("TV Shows")).add(buildMovieInfo("Saregamapa", "test", "test", "", "https://thenewscrunch.com/wp-content/uploads/2021/11/SRGMP-Coming-Soon-1.png", ""));
                CATOGERIES.add("TV Shows");

                String saregampa = getHTML("https://manatelugumovies.cc/saregamapa-telugu-show/");

                int latest = saregampa.indexOf("<span style=\"color: #ff6600;\">Latest Episodes:</span>");
                String first = saregampa.substring(saregampa.indexOf("<p>", latest), saregampa.indexOf("</p>", latest)) + "\n";

                System.out.println("HTML\n" + first);
                System.out.println("HTMLdone\n");



                int j = 0;

                while(j < first.lastIndexOf("\n")) {
                    System.out.print(j + " " + first.lastIndexOf("\n") + " " + first.length());

                    String this_episode = first.substring(j, first.indexOf("\n", j));
                     System.out.println("Episode: " + this_episode);

                    Objects.requireNonNull(map.get("TV Shows")).add(add_movies(this_episode));

                    j += this_episode.length() + 1;
                    System.out.print(j + " " + first.lastIndexOf("\n"));


                }




                latest = saregampa.indexOf("<span style=\"color: #ff6600;\">Previous Episodes:</span>");
                String previous = saregampa.substring(saregampa.indexOf("<p>", latest), saregampa.indexOf("</p>", latest)) + "\n";
                System.out.println("HTML" + previous);

                int i = 0;
                do {
                    String this_episode = previous.substring(i, previous.indexOf("\n", i));
                     System.out.println("Episode: " + this_episode);

                    Objects.requireNonNull(map.get("TV Shows")).add(add_movies(this_episode));

                    i += this_episode.length() + 1;

                } while(i < previous.lastIndexOf("\n"));

            }

            private Movie add_movies(String first){

                String title = "Sa Re Ga Ma Pa " + first.substring(first.indexOf("E"), first.indexOf(" "));
                ArrayList<String> links = new ArrayList<>();

                int i = 0;
                while(first.indexOf("<a href=", i) != -1){
                    int x = first.indexOf("<a href=\"", i) + 9;

                    String url = first.substring(x, first.indexOf("\"", x));
                    url = url.replace("&amp;", "&");
//            url = url.replace("http", "https");
                    links.add(url);
                    i = x;
                }

                return buildMovieInfo(title, title, "ZEE5", links, "https://thenewscrunch.com/wp-content/uploads/2021/11/SRGMP-Coming-Soon-1.png", "manatelugu");
            }

        });

        recents.start();
        THREADS.add(recents);

        recents = new Thread(new Runnable() {
            @Override
            public void run() {

//                Objects.requireNonNull(map.get("TV Shows")).add(buildMovieInfo("Saregamapa", "test", "test", "", "https://thenewscrunch.com/wp-content/uploads/2021/11/SRGMP-Coming-Soon-1.png", ""));

                String saregampa = getHTML("https://manatelugumovies.cc/bigg-boss-6-telugu-show/");

                int latest = saregampa.indexOf("<span style=\"color: #ff0000;\">Latest Episode:</span>");
                String first = saregampa.substring(saregampa.indexOf("<p>", latest), saregampa.indexOf("</p>", latest)) + "\n";

                System.out.println("HTML\n" + first);
                System.out.println("HTMLdone\n");



                int j = 0;

                while(j < first.lastIndexOf("\n")) {
                    System.out.print(j + " " + first.lastIndexOf("\n") + " " + first.length());

                    String this_episode = first.substring(j, first.indexOf("\n", j));
                    System.out.println("Episode: " + this_episode);

                    Objects.requireNonNull(map.get("TV Shows")).add(add_movies(this_episode));

                    j += this_episode.length() + 1;
                    System.out.print(j + " " + first.lastIndexOf("\n"));


                }




                latest = saregampa.indexOf("<span style=\"color: #ff0000;\">Previous Episode:</span>");
                String previous = saregampa.substring(saregampa.indexOf("<p>", latest), saregampa.indexOf("</p>", latest)) + "\n";
                System.out.println("HTML" + previous);

                int i = 0;
                do {
                    String this_episode = previous.substring(i, previous.indexOf("\n", i));
                    System.out.println("Episode: " + this_episode);

                    Objects.requireNonNull(map.get("TV Shows")).add(add_movies(this_episode));

                    i += this_episode.length() + 1;

                } while(i < previous.lastIndexOf("\n"));

            }

            private Movie add_movies(String first){

                String title = "Bigg Boss" + first.substring(first.indexOf("-") + 2, first.indexOf(" "));
                ArrayList<String> links = new ArrayList<>();

                int i = 0;
                while(first.indexOf("<a href=", i) != -1){
                    int x = first.indexOf("<a href=\"", i) + 9;

                    String url = first.substring(x, first.indexOf("\"", x));
                    url = url.replace("&amp;", "&");
//            url = url.replace("http", "https");
                    links.add(url);
                    i = x;
                }

                return buildMovieInfo(title, title, "ZEE5", links, "https://english.sakshi.com/sites/default/files/styles/canvas/public/article_images/2022/08/11/bb6-1660020516.jpg?itok=dJNWQa-0", "manatelugu");
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














    public static ArrayList<Movie> getNewMovies(String header) {

        ArrayList<Movie> newMovies = new ArrayList<>();
        PageLoader currPage = null;

        for (PageLoader thisPage: pages) {
            if (thisPage.getPageHeader().equals(header)){
                currPage = thisPage;
                break;
            }
        }

        assert currPage != null;
        newMovies = Objects.requireNonNull(getMovies(currPage.getNewPageLink()));

        setUpdater(newMovies);

        Objects.requireNonNull(map.get(header)).addAll(newMovies);


        return  newMovies;
    }



    public static HashMap<String, ArrayList<Movie>> getList() {
        if (map == null) {
            System.out.print("TAKING LONGGGG HEERERER");
            map = setupMovies();
        }

        for (PageLoader x: pages) {
            setUpdater(x.getPageHeader());
        }

        return map;
    }

    private static void setUpdater(ArrayList<Movie> movies){
        movies.get(0).setRequester(true);
    }

    private static void setUpdater(String header){
        map.get(header).get(0).setRequester(true);
    }


    public static ArrayList<String> getPages() {
        if (map == null) {
            System.out.print("TAKING LONGGGG HEERERER");
            map = setupMovies();
        }
        return CATOGERIES;
    }

    private static Movie buildMovieInfo(
            String title,
            String description,
            String studio,
            String videoUrl,
            String cardImageUrl,
            String source) {


        Movie movie = new Movie();
        movie.setId(count++);
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio(studio);
        movie.setCardImageUrl(cardImageUrl);
        movie.setBackgroundImageUrl("https://wallpaperaccess.com/full/1092758.jpg");
        movie.setVideoUrl(videoUrl);
        movie.setSource(source);
        return movie;


    }

    private static Movie buildMovieInfo(
            String title,
            String description,
            String studio,
            ArrayList<String> videoUrl,
            String cardImageUrl,
            String source) {


        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio(studio);
        movie.setCardImageUrl(cardImageUrl);
        movie.setBackgroundImageUrl("https://wallpaperaccess.com/full/1092758.jpg");
        movie.setVideoUrlArrayList(videoUrl);
        movie.setSource(source);
        return movie;


    }

    private static ArrayList<Movie> getMovies(String link){

        System.out.println("Getting movies from " + link);

        if (link.contains("einthusan"))
            return enthusian(link);
        if (link.contains("movierulz"))
            return movieRulz(link);

        return null;
    }

    private static ArrayList<Movie> movieRulz(String p) {

        ArrayList<Movie> thisPageMovies = new ArrayList<>();

        String results = getHTML(p);

        results = results.substring(results.indexOf("<div class=\"entry-content\">"), results.indexOf("<nav id=\"posts-nav\" class=\"paged-navigation contain\">"));

        int i = 0;

        while(results.indexOf("href=\"", i) != -1) {


            int x = results.indexOf("href=\"", i) + 6;
            int y = results.indexOf("\"", x);
            String linkToMovie = results.substring(x, y);

            x = results.indexOf("title=\"", i) + 7;
            y = results.indexOf("\"", x);
            String title = results.substring(x, y);

            x = results.indexOf("src=\"", i) + 5;
            y = results.indexOf("\"", x);
            String img = results.substring(x, y);

            Movie thisMovie = buildMovieInfo(title, null, p,  linkToMovie, img, "MOVIERULZ");

            thisPageMovies.add(thisMovie);

            i = y;

        }

        System.out.println("MOVIERULZ DONE");
        return thisPageMovies;

    }

    private static ArrayList<Movie> enthusian(String p) {

        ArrayList<Movie> thisPageMovies = new ArrayList<>();

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

            Movie thisMovie = buildMovieInfo(title, description, p, linkToMovie, "https:" + image, "enthusan");
            thisPageMovies.add(thisMovie);

            i = mainHtml.indexOf("</h3>", x);

        }

        System.out.println("ENTHUSAN DONE");

        return thisPageMovies;

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