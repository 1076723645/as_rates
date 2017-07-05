package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class SplashActivity extends AppCompatActivity {

    //url to currency codes used in this application
    public static final String URL_CODES = "http://openexchangerates.org/api/currencies.json";
    public static final String KEY_ARRAYLIST = "key_arraylist";
    //ArrayList of currencies that will be fetched and passed into MainActivity
    private ArrayList<String> mCurrencies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

//新增加的代码
        new FetchCodesTask().execute(URL_CODES);

    }

    private class FetchCodesTask extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {
            return new JSONParser().getJSONFromUrl(params[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject == null) {
                    throw new JSONException("no data available.");
                }
                Iterator iterator = jsonObject.keys();
                String key = "";
                mCurrencies = new ArrayList<String>();
                while (iterator.hasNext()) {
                    key = (String) iterator.next();
                    mCurrencies.add(key + " | " + jsonObject.getString(key));
                }
                Intent mainintent = new Intent(SplashActivity.this,MainActivity.class);
                mainintent.putExtra(KEY_ARRAYLIST,mCurrencies);
                startActivity(mainintent);
                finish();
            } catch (JSONException e) {
                Toast.makeText(
                        SplashActivity.this,
                        "There's been a JSON exception: " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                e.printStackTrace();
                finish();
            }
        }
    }

}