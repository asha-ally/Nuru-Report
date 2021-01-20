package com.openinstitute.nuru.app;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.openinstitute.nuru.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class WebAsyncFetch   extends AsyncTask<Void, Void, String> {

    //OnAppAsyncFetchTaskComplete caller;
    Context context;

    private MainActivity caller_mainactivity_update;
    //private Organization caller_organization;

    private Activity activity;
    private String activityName;
    private final String url;
    private final String last_id;
    String category_id = null;


    private final static String TAG = com.openinstitute.nuru.app.WebAsyncFetch.class.getSimpleName();

    public WebAsyncFetch(Context caller, String url, String last_id, String _category_id) {
        //super();
        this.activity = activity;
        //this.activityName = activity.getLocalClassName();

        context = activity;
        this.url = url;
        this.last_id = "&last=" + last_id;
        this.category_id = _category_id;

        /*if(_category_id.equals("organization")){
            caller_organization = (Organization) caller;
        }*/

        if(_category_id.equals("update_check")){
            caller_mainactivity_update = (MainActivity) caller;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        JSONObject url_result = loadJSON(this.url + this.last_id);

        String post = (String.valueOf(url_result).equals("null") ) ? null : String.valueOf(url_result);
        return post;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //return false;
        }

        if(category_id.equals("update_check")){
            caller_mainactivity_update.asyncUpdateResponse(result);
        }

        /*if(category_id.equals("organization")){
            caller_organization.asyncResponse_b(result);
        }*/

    }

    public JSONObject loadJSON(String url) {
        // Creating JSON Parser instance
        JSONGetter jParser = new JSONGetter();

        JSONObject json = jParser.getJSONFromUrl(url);

        //Log.d(TAG,"url: " + url);
        //Log.d(TAG,"json: " + json.toString());

        return json;
    }


    private class JSONGetter {

        private final InputStream is = null;
        private final JSONObject jObj = null;
        private final String json = "";

        // constructor
        public JSONGetter() {

        }


        public JSONObject getJSONFromUrl(String url) {

            WebAsyncHandler sh = new WebAsyncHandler();
            JSONObject jsonObj = null;
            String jsonStr = sh.makeServiceCall(url);

            //Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return jsonObj;

        }

    }


}