package com.larvaesoft.opengst.utils;

import com.larvaesoft.opengst.model.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

public class XMLParser {
    private ArrayList<Article> articles;
    private Article currentArticle;

    public XMLParser() {

        articles = new ArrayList<>();
        currentArticle = new Article();
    }

    public ArrayList<Article> parseXML(String xml) {

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            factory.setNamespaceAware(false);
            XmlPullParser xmlPullParser = factory.newPullParser();

            xmlPullParser.setInput(new StringReader(xml));
            boolean insideItem = false;
            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {

                    if (xmlPullParser.getName().equalsIgnoreCase("item")) {

                        insideItem = true;

                    } else if (xmlPullParser.getName().equalsIgnoreCase("title")) {

                        if (insideItem) {

                            String title = xmlPullParser.nextText();
                            currentArticle.setTitle(title);
                        }

                    } else if (xmlPullParser.getName().equalsIgnoreCase("link")) {

                        if (insideItem) {

                            String link = xmlPullParser.nextText();
                            currentArticle.setLink(link);
                        }

                    } else if (xmlPullParser.getName().equalsIgnoreCase("dc:creator")) {

                        if (insideItem) {

                            String author = xmlPullParser.nextText();
                            currentArticle.setAuthor(author);
                        }

                    } else if (xmlPullParser.getName().equalsIgnoreCase("content:encoded")) {

                        if (insideItem) {

                            String htmlData = xmlPullParser.nextText();
                            Document doc = Jsoup.parse(htmlData);
                            try {

                                //choose the first image found in the article
                                String pic = doc.select("img").first().attr("src");
                                currentArticle.setImage(pic);

                            } catch (NullPointerException e) {

                                currentArticle.setImage("");

                            }

                            currentArticle.setContent(htmlData);
                        }

                    } else if (xmlPullParser.getName().equalsIgnoreCase("description")) {

                        if (insideItem) {

                            String description = xmlPullParser.nextText();
                            Document doc = Jsoup.parse(description);
                            currentArticle.setDescription(description);
                            try {
                                String pic = doc.select("img").first().attr("src");
                                currentArticle.setImage(pic);
                                /*currentArticle.setDescription(doc.select("font").get(5).text());*/
                                currentArticle.setSource(doc.select("font").get(0).text());
                            } catch (NullPointerException ex) {
                                currentArticle.setImage("");
                            }

                        }

                    } else if (xmlPullParser.getName().equalsIgnoreCase("pubDate")) {

                        @SuppressWarnings("deprecation")
                        Date pubDate = new Date(xmlPullParser.nextText());
                        currentArticle.setPubDate(pubDate);
                    }

                } else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")) {

                    insideItem = false;
                    articles.add(currentArticle);
                    currentArticle = new Article();
                }
                eventType = xmlPullParser.next();
            }
            return articles;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
