package com.example.loiphung.hw03_group25;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<App> appArrayList = new ArrayList<App>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetDataAsync().execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");



    }//end oncreate


    public class GetDataAsync extends AsyncTask<String, Integer, ArrayList<App>> {

        private ProgressDialog dialog;
        AlertDialog alert;
        AlertDialog.Builder builder;
        ProgressBar pb;
        ListView listview;
        Context context;

        public GetDataAsync(MainActivity activity, ListView listView, ProgressBar pb) {
            dialog = new ProgressDialog(activity);
            //alert = new AlertDialog(activity);
            pb.setProgress(0);
            pb.setMax(100);
            this.pb = pb;
            this.listview = listView;
            context = activity;

        }

        public GetDataAsync(){

        }

        @Override
        protected void onPreExecute() {
            //dialog.setMessage("Loading sources");
            //dialog.show();



        }


        @Override
        protected ArrayList<App> doInBackground(String... params) {

            ArrayList<App> result = new ArrayList<App>();

            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");


                    JSONObject root = new JSONObject(json);

                    JSONObject apps = root.optJSONObject("feed");

                    JSONArray appsArray = apps.optJSONArray("entry");


                    for (int i = 0; i < appsArray.length(); i++) {
                        JSONObject appsJSONObject = appsArray.optJSONObject(i);


                        App a = new App();

                        a.setSummary(appsJSONObject.optJSONObject("summary").getString("label"));
                        a.setName(appsJSONObject.optJSONObject("im:name").optString("label"));
                        a.setPrice(appsJSONObject.optJSONObject("im:price").getString("label"));
                        a.setImage(appsJSONObject.optJSONArray("im:image").optJSONObject(0).optString("label"));
                        a.setDate(appsJSONObject.optJSONObject("im:releaseDate").optString("label"));
                        a.setLink(appsJSONObject.optJSONObject("link").optJSONObject("attributes").optString("href"));

                        Log.d("Name", "-> " + appsJSONObject.optJSONObject("im:name").optString("label"));
                        Log.d("Summary", "-> " + appsJSONObject.optJSONObject("summary").getString("label"));
                        Log.d("Image", "->"+ appsJSONObject.optJSONArray("im:image").optJSONObject(0).optString("label"));
                        Log.d("Summary", "-> " + appsJSONObject.optJSONObject("im:releaseDate").getString("label"));
                        Log.d("App Link", "-> " + appsJSONObject.optJSONObject("link").optJSONObject("attributes").optString("href"));

                        result.add(a);

                    }

                }
            } catch (Exception e) {
                //Handle Exceptions
            } finally {
                //Close the connections
            }


            return result;
        }//end do in background

        @Override
        protected void onProgressUpdate(Integer... values) {



        }//end on progress

        protected void onPostExecute(ArrayList<App> result) {

            appArrayList = result;
            Log.d("app arraylist", "->" + appArrayList.get(0).getName());

            ListView listView = findViewById(R.id.listView);

            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.app_row, MainActivity.appArrayList);
            listView.setAdapter(adapter);

        }//end on post

    } //end getdataasync


}//end mainactivity
