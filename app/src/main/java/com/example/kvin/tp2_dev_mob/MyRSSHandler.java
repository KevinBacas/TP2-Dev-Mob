package com.example.kvin.tp2_dev_mob;

import android.graphics.Bitmap;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
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
    private int numItem = 0;
    // Le nombre total d’items dans le flux RSS
    private int numItemMax = -1;

    public void setUrl(String url) {
        this.url = url;
    }

    public void processFeed() {
        try {
            numItem = 0; // A modifier pour visualiser un autre item
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream inputStream = new URL(url).openStream();
            reader.parse(new InputSource(inputStream));
            image = getBitmap(imageURL);
            numItemMax = numItem;
        } catch (Exception e) {
            Log.e("smb116rssview", "processFeed Exception " + e.getMessage());
        }
    }

    public void startElement(String uri,  String localName, String qName, Attributes attributes)
            throws SAXException {
        Log.v("startElement", qName);
    }

    public void characters(char ch[], int start, int length) {
        String chars = new String(ch).substring(start, start + length);
        Log.v("startElement", chars);
    }

    private Bitmap getBitmap(String imageURL) {
        return null;
    }
}