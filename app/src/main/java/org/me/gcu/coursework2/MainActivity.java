/*  Starter project for Mobile Platform Development in main diet 2023/2024
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 _________________
// Student ID           _________________
// Programme of Study   _________________
//

// UPDATE THE PACKAGE NAME to include your Student Identifier
package org.me.gcu.coursework2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
{
    private TextView rawDataDisplay;
    private Button startButton, ManLatest, Glas3Day, GlasLatest, Lon3Day, LonLatest, NY3Day, NYLatest, Oman3Day, OmanLatest, Mar3Day, MarLatest;

    private String result;
    private String url1="";

    private String urlSource;

    private LinkedList<WeatherData> weatherList;

    WeatherData aWeatherData;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
        ManLatest = (Button)findViewById(R.id.ManWeek);
        Glas3Day = (Button)findViewById(R.id.Glas3days);
        GlasLatest = (Button)findViewById(R.id.glasLatest);
        Lon3Day = (Button)findViewById(R.id.lon3day);
        LonLatest = (Button)findViewById(R.id.lonLatest);
        NY3Day = (Button)findViewById(R.id.NY3day);
        NYLatest = (Button)findViewById(R.id.NYLatest);
        Oman3Day = (Button)findViewById(R.id.Oman3Day);
        OmanLatest = (Button)findViewById(R.id.OmanLatest);
        Mar3Day = (Button)findViewById(R.id.Mar3day);
        MarLatest = (Button)findViewById(R.id.MarLatest);
        startButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               urlSource="https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123";
                                               startProgress();
                                           }

                                       });
        ManLatest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643123";
                startProgress();
            }
        });
        Glas3Day.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579";
                startProgress();
            }
        });
        GlasLatest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2648579";
                startProgress();
            }
        });
        Lon3Day.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743";
                startProgress();
            }
        });
        LonLatest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643743";
                startProgress();
            }
        });
        NY3Day.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581";
                startProgress();
            }
        });
        NYLatest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/5128581";
                startProgress();
            }
        });
        Oman3Day.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286";
                startProgress();
            }
        });
        OmanLatest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/287286";
                startProgress();
            }
        });
        Mar3Day.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154";
                startProgress();
            }
        });
        MarLatest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/934154";
                startProgress();
            }
        });

        weatherList = new LinkedList<WeatherData>();

        // More Code goes here
    }





    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //Get rid of the first tag <?xml version="1.0" encoding="utf-8"?>
            int i = result.indexOf(">");
            result = result.substring(i+1);
            //Get rid of the 2nd tag <rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
            i = result.indexOf(">");
            result = result.substring(i+1);
            Log.e("MyTag - cleaned",result);


            //
            // Now that you have the xml data you can parse it
            //


            parseData(result);

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(result);
                }
            });
        }

    }

    private void parseData(String dataToParse) {
        // Remove namespace prefixes
        dataToParse = dataToParse.replaceAll("xmlns:dc=\"[^\"]+\"", "");

        boolean foundFirstItem = false; // Flag to track if the first <item> tag has been found

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        foundFirstItem = true; // Set flag to true when the first <item> tag is found
                        aWeatherData = new WeatherData(); // Initialize WeatherData object
                        Log.d("MyTag", "New item found!");
                    } else if (foundFirstItem) { // If the first <item> tag is found, process subsequent tags
                        if (xpp.getName().equalsIgnoreCase("title")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.d("MyTag", "Title is " + temp);
                            aWeatherData.setTitle(temp);
                        } else if (xpp.getName().equalsIgnoreCase("Description")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.d("MyTag", "Description is " + temp);
                            //  aWeatherData.setDescription(temp);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.d("MyTag", "Publication date is " + temp);
                            //  aWeatherData.setPubDate(temp);
                        }
                    }
                }
                eventType = xpp.next(); // Move to next event
            }
        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }

        Log.d("MyTag", "End of document reached");
    }


}