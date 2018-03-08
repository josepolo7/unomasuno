package com.polosoft.unomasuno;

import android.os.AsyncTask;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JAPH on 3/6/18.
 */


public class RestFulHelper {
    //----------------------------------------------------------------------------------------------
    private Object caller;
    //----------------------------------------------------------------------------------------------
    private static String RATES_URL = "http://api.fixer.io/latest?base=USD";
    //----------------------------------------------------------------------------------------------
    //**********************************************************************************************
    public RestFulHelper(Object param_caller){
        caller = param_caller;
        //------------------------------------------------------------------------------------------
    }
    //**********************************************************************************************

    //**********************************************************************************************
    public void getRatesFromURL(){
        new GetRatesFromURL().execute();
    }
    //**********************************************************************************************
    private class GetRatesFromURL extends AsyncTask<Void, Void, Boolean> {
        //------------------------------------------------------------------------------------------
        private JSONObject ratesRecord = null;
        //******************************************************************************************
        @Override
        protected void onPreExecute() {
            //TODO
        }
        //******************************************************************************************
        protected Boolean doInBackground(Void... params) {
            Boolean result = false;
            //--------------------------------------------------------------------------------------
            OkHttpClient client = new OkHttpClient.Builder().build();
            //--------------------------------------------------------------------------------------
            Request request = new Request.Builder().url(RATES_URL).build();
            //--------------------------------------------------------------------------------------
            try {
                //----------------------------------------------------------------------------------
                Response response = client.newCall(request).execute();
                String txtResponse = response.body().string();
                //----------------------------------------------------------------------------------
                if (response.isSuccessful()) {
                    ratesRecord = new JSONObject(txtResponse);
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //--------------------------------------------------------------------------------------
            return result;
        }

        //******************************************************************************************
        @Override
        protected void onPostExecute(Boolean result) {
            RestFulHelperListener listener = (RestFulHelperListener) caller;
            if (result)
                listener.onFinishGetRates(ratesRecord);
            else
                listener.onFinishGetRates(null);
        }
    }
    //**********************************************************************************************




    //**********************************************************************************************
    public interface RestFulHelperListener{
        void onFinishGetRates(JSONObject record);
    }
    //**********************************************************************************************



}
