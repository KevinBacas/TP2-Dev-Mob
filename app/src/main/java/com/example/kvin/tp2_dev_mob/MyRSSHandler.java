package com.example.kvin.tp2_dev_mob;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MyRSSHandler extends DefaultHandler {
    // l'URL du flux RSS à parser
    private String url = null;
    // Ensemble de drapeau permettant de savoir où en est le parseur dans le flux XML
    private boolean inTitle = false;
    private boolean inDescription = false;
    private boolean inItem = false;
    private boolean inDate = false;
    private boolean inLink = false;
    // Le titre, la description et la date extraits du flux RSS
    private String title;
    private String description;
    private String date;
    private String link;
    // Le numéro de l'item à extraire du flux RSS
    private int numItem = 0;
    // Liste des éléments extraits du flux RSS
    private ArrayList<RedditRSSItem> rssItemList = new ArrayList<>();
    private RedditRSSItem currentItem = null;
    //
    private boolean loadedRss = false;

    public void setUrl(String url) {
        this.url = url;
    }

    public void processFeed() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream inputStream = new URL(url).openStream();
            reader.parse(new InputSource(inputStream));
        } catch (Exception e) {
            Log.e("smb116rssview", "processFeed Exception " + e.getMessage());
        }
        this.currentItem = this.rssItemList.get(this.numItem);
        this.loadedRss = true;
    }

    public void startElement(String uri,  String localName, String qName, Attributes attributes)
            throws SAXException {
        Log.v("startElement", qName);
        switch (qName) {
            case "title":
                this.title = "";
                this.inTitle = true;
                break;
            case "description":
                this.description = "";
                this.inDescription = true;
                break;
            case "pubDate":
                this.date = "";
                this.inDate = true;
                break;
            case "link":
                this.link = "";
                this.inLink = true;
                break;
            case "item":
                this.inItem = true;
                break;
        }
    }

    public void characters(char ch[], int start, int length) {
        String chars = new String(ch).substring(start, start + length);
        if (this.inTitle) {
            Log.v("inTitle", chars);
            this.title = chars;
            this.inTitle = false;
        }
        if (this.inDescription) {
            Log.v("inDescription", chars);
            this.description += chars;
        }
        if (this.inDate) {
            Log.v("inDate", chars);
            this.inDate = false;
            this.date = chars;
        }
        if (this.inLink) {
            Log.v("inLink", chars);
            this.inLink = false;
            this.link = chars;
        }
        if (this.inItem) {
            Log.v("inItem", chars);
            this.inItem = false;
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("description")) {
            this.inDescription = false;
        } else if(qName.equals("item")) {
            this.rssItemList.add(new RedditRSSItem(this.title, this.description, this.date, this.link));
        }
    }

    public int getNumber() {
        return this.numItem;
    }

    public String getTitle() {
        return this.currentItem.getTitle();
    }

    public String getDescription() {
        return this.currentItem.getDescription();
    }

    public String getDate() {
        return this.currentItem.getPubDate();
    }

    public void setNextNumItem() {
        this.numItem++;
        this.currentItem = this.rssItemList.get(this.numItem);
    }

    public void setPreviousNumItem() {
        this.numItem--;
        this.currentItem = this.rssItemList.get(this.numItem);
    }

    public boolean isRssLoaded() {
        return this.loadedRss;
    }
}