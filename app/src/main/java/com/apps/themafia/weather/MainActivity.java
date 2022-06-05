package com.apps.themafia.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView textview1 , textview2 , textview3 , textview4 , textview5 , textview6 , textview7;
    EditText city;

    public class DownloadTask extends AsyncTask<String , Void , String>{

        @Override
        protected String doInBackground(String... urls) {

            String json = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1){
                    char current = (char) data;
                    json += current;
                    data = reader.read();
                }

                return json;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed!!";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject location = object.getJSONObject("location");
                    JSONObject current = object.getJSONObject("current");
                    JSONObject condition = current.getJSONObject("condition");
                    Log.i("Line 64 " , "Scuccesful");
                    String name = location.getString("name");
                    String region = location.getString("region");
                    String country = location.getString("country");
                    String state = condition.getString("text");
                    int is_day_int = current.getInt("is_day");
                    String is_days = "";
                    if (is_day_int == 1){
                        is_days = "Day";
                    }else{
                        is_days = "Night";
                    }
                    String is_day = is_days;
                    String datetime = current.getString("last_updated");
                    String temp_c = current.getString("temp_c");
                    String temp_f = current.getString("temp_f");

                    Log.i("name" , "" + name);
                    Log.i("name" , "" + name);

                    textview1.setText("City: " + name);
                    textview2.setText("Region: " + region);
                    textview3.setText("Country: " + country);
                    textview4.setText("Condition: " + state);
                    textview5.setText("Day/Night: " + is_day);
                    textview6.setText("DateTime: " + datetime);
                    textview7.setText("Temperature: " + temp_c + "C  " +"/ "+ temp_f + "F");

    //                Toast.makeText(MainActivity.this, "Could Not Find Weather Information :(", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Could Not Find Weather Information :(", Toast.LENGTH_SHORT).show();
                    Log.i("Line" , "Line 80");
                }
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview1 = findViewById(R.id.textView1);
        textview2 = findViewById(R.id.textView2);
        textview3 = findViewById(R.id.textView3);
        textview4 = findViewById(R.id.textView4);
        textview5 = findViewById(R.id.textView5);
        textview6 = findViewById(R.id.textView6);
        textview7 = findViewById(R.id.textView7);
        city = findViewById(R.id.user_city_request);

        if (city.equals("")){
            Toast.makeText(MainActivity.this, "Could Not Find Weather Information :(", Toast.LENGTH_SHORT).show();
        }


        DownloadTask task = new DownloadTask();
        String result = "";


    }

    public void getWeather(View view){
        String cityx = city.getText().toString();
        DownloadTask task = new DownloadTask();
        String json_str = "";
        try {
            json_str = task.execute("http://api.weatherapi.com/v1/current.json?key=aea8dd5c545640d0b70154520222405&q=" + cityx + "&aqi=no").get();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Could Not Find Weather Information :(", Toast.LENGTH_SHORT).show();
        }
        Log.i("json" , "" + json_str);

        // To hide The Keyboard
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(city.getWindowToken() , 0);

    }

    }
