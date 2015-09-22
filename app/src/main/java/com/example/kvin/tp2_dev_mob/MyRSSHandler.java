package com.example.kvin.tp2_dev_mob;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Kévin on 21/09/2015.
 */
public class MyRSSHandler extends DefaultHandler {
    // l'URL du flux RSS à parser
    private String url = null;
    // Ensemble de drapeau permettant de savoir où en est le parseur dans le flux XML
    private boolean inTitle = false;
    private boolean inDescription = false;
    private boolean inItem = false;
    private boolean inDate = false;
    // L'image référencé par l'attribut url du tag <enclosure>
    private Bitmap image = null;
    private String imageURL = null;
    // Le titre, la description et la date extraits du flux RSS
    private StringBuffer title = new StringBuffer();
    private StringBuffer description = new StringBuffer();
    private StringBuffer date = new StringBuffer();
    // Le numéro de l'item à extraire du flux RSS
    private int numItem = 2;
    private int currScanNumItem = 0;

    public void setUrl(String url) {
        this.url = url;
    }

    public void processFeed() {
        currScanNumItem = 0;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream inputStream = new URL(url).openStream();
            reader.parse(new InputSource(inputStream));
            image = getBitmap(imageURL);
        } catch (Exception e) {
            Log.e("smb116rssview", "processFeed Exception " + e.getMessage());
        }
    }

    public void startElement(String uri,  String localName, String qName, Attributes attributes)
            throws SAXException {
        Log.v("startElement", qName);
        if(numItem == currScanNumItem) {
            if (qName == "title") {
                this.title = new StringBuffer();
                this.inTitle = true;
            } else if (qName == "description") {
                this.description = new StringBuffer();
                this.inDescription = true;
            } else if (qName == "pubDate") {
                this.date = new StringBuffer();
                this.inDate = true;
            }
        }
        if (qName == "item") {
            this.inItem = true;
        }
    }

    public void characters(char ch[], int start, int length) {
        String chars = new String(ch).substring(start, start + length);
        if (this.inTitle) {
            Log.v("inTitle", chars);
            this.title = new StringBuffer(chars);
            this.inTitle = false;
        }
        if (this.inDescription) {
            Log.v("inDescription", chars);
            this.description = new StringBuffer(this.description.toString() + chars);
        }
        if (this.inDate) {
            Log.v("inDate", chars);
            this.inDate = false;
            this.date = new StringBuffer(chars);
        }
        if (this.inItem) {
            Log.v("inItem", chars);
            this.currScanNumItem++;
            this.inItem = false;
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName)
            throws SAXException {
        if (qName == "description") {
            this.inDescription = false;
        }
    }

    public Bitmap getBitmap(String imageURL) {
        Log.v("getBitmap", imageURL);
        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            Log.e("IOException", e.toString());
            return null;
        }
    }

    public int getNumber() {
        return this.numItem;
    }

    public String getTitle() {
        return this.title.toString();
    }

    public String getDescription() {
        return this.description.toString();
    }

    public String getDate() {
        return this.date.toString();
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setNextNumItem() {
        this.numItem++;
    }

    public void setPreviousNumItem() {
        this.numItem--;
    }
}