package com.example.kvin.tp2_dev_mob;

public class RedditRSSItem extends RSSItem {
    private String title;
    private String description;
    private String pubDate;
    private String link;

    public RedditRSSItem(String _title, String _description, String _pubdate, String _link) {
        this.title = _title;
        this.description = _description;
        this.pubDate = _pubdate;
        this.link = _link;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPubDate() {
        return this.pubDate;
    }

    public String getLink() {
        return this.link;
    }
}
