package com.example.gomugomutv;


/*
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
public class PageLoader {

    private String header;
    private String link;
    private int pageNumberIndex;
    private int lastPageLoaded;


    public PageLoader(String header, String link, int pageNumberIndex) {

        this.header = header;
        this.link = link;
        this.pageNumberIndex = pageNumberIndex;
        this.lastPageLoaded = 0;

    }

    public String getPageHeader(){
        return header;
    }

    public String getNewPageLink(){
        this.lastPageLoaded++;
        String returnVal = link.substring(0, pageNumberIndex) + "" + (lastPageLoaded) + "" + link.substring(pageNumberIndex);
//        System.out.println(returnVal);
        return returnVal;
    }

    @Override
    public String toString(){
         return "Header: " + header + "link: " + link;
    }

}