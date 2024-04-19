package org.me.gcu.coursework2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final int[] layoutIds = {
            R.layout.glasgow,
            R.layout.london,
            R.layout.man,
            R.layout.mauritius,
            R.layout.newyork,
            R.layout.oman,
            R.layout.bangladesh
    };

    private int currentIndex = 0;

    private static final String[] RSS_FEED_URLS = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579", // Glasgow
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743", // London
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123", // Manchester
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",    // Mauritius
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581", // New York
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",  // Oman
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241 ", // Bangladesh

    };

    private static final String[] RSS_FEED_URLS_LATEST = {

            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2648579",// Glasgow Latest
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643743", // London latest
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643123", // Manchester Latest
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/934154 ", // Maurititus latest
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/5128581", // NY latest
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/287286 ", // Oman latest
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/1185241"// Bangladesh latest
    };
    private static final String[] RSS_FEEDS_KEYS = {
            "Manchester 3 Day",
            "Glasgow 3 Day",
            "London 3 Day",
            "New York 3 Day",
            "Oman 3 Day",
            "Mauritius 3 Day",
            "Bangladesh 3 Day",
            "Manchester Latest",
            "Glasgow Latest",
            "London Latest",
            "New York Latest",
            "Oman Latest",
            "Mauritius Latest",
            "Bangladesh Latest"
    };

    private ArrayList<WeatherData> weatherDataList = new ArrayList<>();
    private ArrayList<ThreeDayForecastData> threeDayForecastDataList = new ArrayList<>();

    private CountDownLatch latch;

    private HashMap<String, WeatherData> locationWeatherMap = new HashMap<>();

    private HashMap<String, ThreeDayForecastData> locationForecastMap = new HashMap<>();



    private final String[] locationNames = {
            "Glasgow",
            "London",
            "Manchester",
            "Mauritius",
            "New York",
            "Oman",
            "Bangladesh"
    };

    private final String KEY_CURRENT_INDEX = "current_index";


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutIds[currentIndex]); // Set the initial layout

        Button nextButton = findViewById(R.id.next_button);

        checkNetworkConnectivity();
        latch = new CountDownLatch(RSS_FEED_URLS.length);
        startProgress();
        updateUIForLocation(locationNames[currentIndex]);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClick(view);
            }
        });


    }

    private void switchToLayout(int layoutId) {
        View view = LayoutInflater.from(this).inflate(layoutId, null);
        setContentView(view);
    }

    public void onNextButtonClick(View view) {
        currentIndex++;
        if (currentIndex >= layoutIds.length) {
            currentIndex = 0;
        }
        switchToNextLayout();
        updateUIForLocation(locationNames[currentIndex]);
    }

    private void switchToNextLayout() {
        currentIndex++;
        if (currentIndex >= locationNames.length) {
            currentIndex = 0;
        }
        int layoutId = getLayoutIdForLocation(locationNames[currentIndex]);
        switchToLayout(layoutId);
    }

    private int getLayoutIdForLocation(String locationName) {
        switch (locationName) {
            case "Glasgow":
                return R.layout.glasgow;
            case "London":
                return R.layout.london;
            case "Manchester":
                return R.layout.man;
            case "Mauritius":
                return R.layout.mauritius;
            case "New York":
                return R.layout.newyork;
            case "Oman":
                return R.layout.oman;
            case "Bangladesh":
                return R.layout.bangladesh;
            default:
                return R.layout.glasgow; // Default to Glasgow layout
        }
    }

    public void startProgress() {
        for (int i = 0; i < RSS_FEED_URLS_LATEST.length; i++) {
            new Thread(new Task(RSS_FEED_URLS_LATEST[i], i)).start();
        }
        try {
            latch.await();
            Log.d("MyTag", "All threads have finished");
            Log.d("MyTag", weatherDataList.toString());
        } catch (InterruptedException e) {
            Log.e("MyTag", "InterruptedException: " + e.getMessage());
        }
        for (int i = 0; i < RSS_FEED_URLS.length; i++) {
            new Thread(new Task3day(RSS_FEED_URLS[i], i)).start();
        }
        try {
            latch.await();
            Log.d("MyTag", "All threads have finished");
            Log.d("MyTag", weatherDataList.toString());
        } catch (InterruptedException e) {
            Log.e("MyTag", "InterruptedException: " + e.getMessage());
        }

    }


    private class Task implements Runnable {
        private String url;
        private int index;

        public Task(String aurl, int index) {
            url = aurl;
            this.index = index;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";

            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            int i = result.indexOf(">");
            result = result.substring(i+1);
            i = result.indexOf(">");
            result = result.substring(i+1);

            parseData(result, index);
            latch.countDown();
        }
    }

    private class Task3day implements Runnable {
        private String url;
        private int index;

        public Task3day(String aurl, int index) {
            url = aurl;
            this.index = index;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";

            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            int i = result.indexOf(">");
            result = result.substring(i+1);
            i = result.indexOf(">");
            result = result.substring(i+1);

            parseDataForForecast(result, index);
            latch.countDown();
        }
    }

    private void parseData(String dataToParse, int index) {
        // Remove namespace prefixes
        dataToParse = dataToParse.replaceAll("xmlns:dc=\"[^\"]+\"", "");

        boolean foundFirstItem = false; // Flag to track if the first <item> tag has been found

        WeatherData aWeatherData = null; // Declare aWeatherData locally

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            foundFirstItem = true; // Set flag to true when the first <item> tag is found
                            aWeatherData = new WeatherData(); // Initialize WeatherData object
                            Log.d("MyTag", "New item found!");
                        } else if (foundFirstItem) { // If the first <item> tag is found, process subsequent tags
                            if (xpp.getName().equalsIgnoreCase("title")) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                aWeatherData.setTitle(temp);
                                Log.d("MyTag", "Title is " + temp);
                            } else if (xpp.getName().equalsIgnoreCase("Description")) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                aWeatherData.setDescription(temp);
                                Log.d("MyTag", "Description is " + temp);
                            } else if (xpp.getName().equalsIgnoreCase("pubDate")) {

                                String temp = xpp.nextText();
                                aWeatherData.setPubDate(temp);
                                Log.d("MyTag", "Publication date is " + temp);
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        // If it's the end of the <item> tag, add the WeatherData object to the list
                        if (index < weatherDataList.size()) {
                            weatherDataList.set(index, aWeatherData);
                        } else {
                            weatherDataList.add(aWeatherData);
                        }
                        locationWeatherMap.put(locationNames[index], aWeatherData);
                        // currentIndex++;// Add to locationWeatherMap
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
        Log.d("MyTag", locationWeatherMap.toString()); // Check if locationWeatherMap is populated
    }

    private void parseDataForForecast(String dataToParse, int index) {
        // Remove namespace prefixes
        dataToParse = dataToParse.replaceAll("xmlns:dc=\"[^\"]+\"", "");

        boolean foundFirstItem = false; // Flag to track if the first <item> tag has been found

        ThreeDayForecastData forecastData = new ThreeDayForecastData(); // Initialize forecast data object

        int titleCount = 0; // Track the number of titles extracted

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        if (!foundFirstItem) {
                            foundFirstItem = true; // Set flag to true when the first <item> tag is found
                        } else {
                            // If this is not the first <item> tag, increment titleCount
                            titleCount++;
                        }
                        Log.d("MyTag", "New item found!");
                    } else if (foundFirstItem && xpp.getName().equalsIgnoreCase("title")) {
                        // Now just get the associated text
                        String title = xpp.nextText();
                        // Store title based on titleCount
                        if (titleCount == 0) {
                            forecastData.setTitle1(title);
                        } else if (titleCount == 1) {
                            forecastData.setTitle2(title);
                        } else if (titleCount == 2) {
                            forecastData.setTitle3(title);
                        }
                        // Extract minimum and maximum temperatures from the title
                        extractTemperatures(title, forecastData, titleCount);
                        extractWeatherInfo(title,forecastData,titleCount);
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    // If it's the end of the <item> tag, add the forecastData object to the list
                    if (index < threeDayForecastDataList.size()) {
                        threeDayForecastDataList.set(index, forecastData);
                    } else {
                        threeDayForecastDataList.add(forecastData);
                    }
                    // Add forecast data to the locationForecastMap
                    locationForecastMap.put(locationNames[index], forecastData);
                    // If three titles are extracted, break the loop
                    if (titleCount == 2) {
                        break;
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
        // Log.d("MyTag", locationForecastMap.toString()); // Check if locationForecastMap is populated
    }





    private void extractTemperatures(String title, ThreeDayForecastData forecastData, int titleCount) {
        // Check if the title contains both minimum and maximum temperatures
        if (title.contains("Minimum Temperature:") && title.contains("Maximum Temperature:")) {
            // Split the title to extract minimum and maximum temperatures
            String[] parts = title.split("Minimum Temperature: |Maximum Temperature: ");
            if (parts.length >= 3) {
                // Construct the min-max temperature string
                String minMaxTemp = parts[1].trim() + " - " + parts[2].trim(); // Concatenate the minimum and maximum temperatures
                // Store min-max temperature string based on titleCount
                if (titleCount == 0) {
                    forecastData.setMinMaxTemperature1(minMaxTemp);
                } else if (titleCount == 1) {
                    forecastData.setMinMaxTemperature2(minMaxTemp);
                } else if (titleCount == 2) {
                    forecastData.setMinMaxTemperature3(minMaxTemp);
                }
            }
        } else {
            // If only the minimum temperature is provided, extract it directly
            String[] parts = title.split("Minimum Temperature:");
            if (parts.length >= 2) {
                String minTemp = parts[1].trim(); // Extract the minimum temperature
                // Store min temperature based on titleCount
                if (titleCount == 0) {
                    forecastData.setMinMaxTemperature1(minTemp);
                } else if (titleCount == 1) {
                    forecastData.setMinMaxTemperature2(minTemp);
                } else if (titleCount == 2) {
                    forecastData.setMinMaxTemperature3(minTemp);
                }
            }
        }
        // Extract date from the title
        String[] dateParts = title.split(":");
        if (dateParts.length > 0) {
            String date = dateParts[0].trim(); // Extract the date part
            // Store date based on titleCount
            if (titleCount == 0) {
                forecastData.setTitle1(date);
            } else if (titleCount == 1) {
                forecastData.setTitle2(date);
            } else if (titleCount == 2) {
                forecastData.setTitle3(date);
            }
        }

    }

    private void extractWeatherInfo(String title, ThreeDayForecastData forecastData, int titleCount) {
        // Extract weather information from the title
        String[] parts = title.split(", "); // Split the title by comma and space

        if (parts.length >= 2) {
            String weatherInfo = parts[0].trim(); // Extract the weather information part

            // Store weather condition based on titleCount
            if (titleCount == 0) {
                forecastData.setWeatherCondition1(weatherInfo);
            } else if (titleCount == 1) {
                forecastData.setWeatherCondition2(weatherInfo);
            } else if (titleCount == 2) {
                forecastData.setWeatherCondition3(weatherInfo);
            }
        }
    }


    private int getImageResourceForWeather(String weatherCondition) {
        switch (weatherCondition.toLowerCase()) {
            case "sun":
                return R.drawable.day_clear;
            case "cloudy":
                return R.drawable.cloudy;
            case "rain":
                return R.drawable.day_rain;
            // Add more cases for other weather conditions as needed
            default:
                return R.drawable.cloudy; // Default image resource if weather condition is not recognized
        }
    }








    private void updateUIForLocation(String locationName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WeatherData weatherData = locationWeatherMap.get(locationName);
                if (weatherData != null) {
                    TextView titleTextView = findViewById(R.id.textView_date_latest);
                    TextView descriptionTextView = findViewById(R.id.textView_temperature_latest);
                    titleTextView.setText(weatherData.getTitle());
                    descriptionTextView.setText(weatherData.getDescription());
                }
                ThreeDayForecastData threeDayForecastData = locationForecastMap.get(locationName);
                if (threeDayForecastData != null) {
                    // Update UI elements with forecast data
                    TextView minMaxTextView = findViewById(R.id.textView_temperature);
                    minMaxTextView.setText(threeDayForecastData.getMinMaxTemperature1());
                    TextView minMaxTextView1 = findViewById(R.id.textView_temperature2);
                    minMaxTextView1.setText(threeDayForecastData.getMinMaxTemperature2());
                    TextView minMaxTextView2 = findViewById(R.id.textView_temperature3);
                    minMaxTextView2.setText(threeDayForecastData.getMinMaxTemperature3());

                    TextView date = findViewById(R.id.textView_date);
                    date.setText(threeDayForecastData.getTitle1());
                    TextView date1 = findViewById(R.id.textView_date2);
                    date1.setText(threeDayForecastData.getTitle2());
                    TextView date2 = findViewById(R.id.textView_date3); // Corrected variable name
                    date2.setText(threeDayForecastData.getTitle3());

                    // Update ImageView with corresponding weather condition
                    ImageView imageView1 = findViewById(R.id.imageView_weather_icon);
                    ImageView imageView2 = findViewById(R.id.imageView_weather_icon2);
                    ImageView imageView3 = findViewById(R.id.imageView_weather_icon3);
                    imageView1.setImageResource(getImageResourceForWeather(threeDayForecastData.getWeatherCondition1()));
                    imageView2.setImageResource(getImageResourceForWeather(threeDayForecastData.getWeatherCondition2()));
                    imageView3.setImageResource(getImageResourceForWeather(threeDayForecastData.getWeatherCondition3()));
                }
            }
        });
    }


    private void checkNetworkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            // If not connected to the internet, show a dialog
            showNoInternetDialog();
        }
    }

    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("This app requires an internet connection to function properly. Please check your network settings and try again.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Close the app or perform any other action if needed
            finish();
        });
        builder.setCancelable(false); // Prevent dialog from being dismissed by tapping outside
        builder.show();
    }

}
