package com.example.kvin.tp2_dev_mob;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MyRSSHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new MyRSSHandler();
        //handler.setUrl("http://www.nasa.gov/rss/image_of_the_day.rss");
        handler.setUrl("https://www.reddit.com/r/haskell/.rss");
        this.launchRSSProcessing();
    }

    public void loadNextItem(View v) {
        handler.setNextNumItem();
        this.launchRSSProcessing();
    }

    public void loadPreviousItem(View v) {
        handler.setPreviousNumItem();
        this.launchRSSProcessing();
    }

    private void launchRSSProcessing() {
        Toast.makeText(this, "Chargement image: " + handler.getNumber(), Toast.LENGTH_LONG).show();
        new DownloadRSSTask(this).execute(handler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void resetDisplay(String title, String date, Bitmap image, String description){
        TextView title_view = (TextView) this.findViewById(R.id.imageTitle);
        TextView date_view = (TextView) this.findViewById(R.id.imageDate);
        WebView description_view = (WebView) this.findViewById(R.id.imageDescription);
        ImageView image_view = (ImageView) this.findViewById(R.id.imageDisplay);

        title_view.setText(title);
        date_view.setText(date);
        Log.v("resetDisplay", description);
        description_view.loadData(description, "text/html", "utf-8");
        image_view.setImageBitmap(image);
    }
}
