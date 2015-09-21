package com.example.kvin.tp2_dev_mob;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Kévin on 21/09/2015.
 */
public class DownloadRSSTask extends AsyncTask<MyRSSHandler, Void, MyRSSHandler> {
    private MainActivity monActivity;

    public DownloadRSSTask (MainActivity monActivity){
        this.monActivity = monActivity;
    }

    protected MyRSSHandler doInBackground(MyRSSHandler... handler) {
        handler[0].processFeed();
        return handler[0];
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground()
     */
    protected void onPostExecute(MyRSSHandler handler) {
        // Mise à jours des données de notre vue à partir du titre, date, image et description lus
        monActivity.resetDisplay(handler.getTitle(), handler.getDate(),
                handler.getImage(), handler.getDescription().toString());
    }
}
