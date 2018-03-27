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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }//end oncreate


    class GetDataAsync extends AsyncTask<String, Integer, ArrayList<App>> {

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

                        a.setSummary(appsJSONObject.getString("summary"));
                        a.setName(appsJSONObject.optString("title"));
                        a.setPrice(appsJSONObject.optString("im:price"));
                        if(appsJSONObject.optString("urlToImage").startsWith("https"))
                        {
                            a.setImage(appsJSONObject.optString("urlToImage"));
                        }
                        a.setDate(appsJSONObject.optString("im:releaseDate"));
                        /*Log.d("JsonName", "" + sourceJsonObject.getString("name"));
                        Log.d("Jsonid", "" + sourceJsonObject.getString("id"));*/


                        result.add(a);
                        pb.setProgress(pb.getProgress() + 1);
                        publishProgress(pb.getProgress());
                    }

                }
            } catch (Exception e) {
                //Handle Exceptions
            } finally {
                //Close the connections
            }


            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {



        }

        protected void onPostExecute(ArrayList<App> result) {


        }

    } //end getdataasync


}//end mainactivity
