package com.polosoft.unomasuno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by JAPH on 3/6/18.
 */


public class MainPresenter
                implements RestFulHelper.RestFulHelperListener{
    //----------------------------------------------------------------------------------------------
    private Context mContext;
    //----------------------------------------------------------------------------------------------
    private RestFulHelper restfulHelper;
    private DataBaseHelper databaseHelper;
    //----------------------------------------------------------------------------------------------
    private Float number;
    //**********************************************************************************************
    public MainPresenter(Context context){
        mContext = context;
        restfulHelper = new RestFulHelper(this);
        databaseHelper = new DataBaseHelper(mContext);
    }
    //**********************************************************************************************
    public String getLastUpdateStr(){
        String lU = databaseHelper.getLastUpdateDate();
        return  lU == null ? "Debe conectarte a internet para actualizar" : "Última actualización: " + lU;
    }
    //**********************************************************************************************
    public void iniConvertion(String numberStr){
        number = null;
        try{
            number = Float.parseFloat(numberStr);
        }catch(NumberFormatException e){
            number = null;
            e.printStackTrace();
            doConvertion();
            return;
        }
        //------------------------------------------------------------------------------------------
        restfulHelper.getRatesFromURL();
        //------------------------------------------------------------------------------------------
    }
    //**********************************************************************************************
    @Override
    public void onFinishGetRates(JSONObject ratesRecord){
        if(ratesRecord != null){
            JSONObject jsonRates = null;
            try {
                jsonRates = ratesRecord.getJSONObject("rates");
                //----------------------------------------------------------------------------------
                if(jsonRates != null){
                    ContentValues values = new ContentValues();
                    //------------------------------------------------------------------------------
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String dateStr = dateFormat.format(new Date());
                    //------------------------------------------------------------------------------
                    values.put("date", dateStr);
                    if(databaseHelper.numRows("last_update") <= 0)
                        databaseHelper.insert("last_update", values);
                    else
                        databaseHelper.update("last_update", values, null);
                    //------------------------------------------------------------------------------

                    //------------------------------------------------------------------------------
                    databaseHelper.deleteAll("rate");
                    Iterator<String> iter = jsonRates.keys();
                    while (iter.hasNext()) {
                        values.clear();
                        String key = iter.next();
                        values.put("rate_cod", key);
                        values.put("rate_number", jsonRates.getDouble(key));
                        //--------------------------------------------------
                        databaseHelper.insert("rate", values);
                    }
                    //------------------------------------------------------------------------------
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //------------------------------------------------------------------------------------------
        doConvertion();

    }
    //**********************************************************************************************
    private void doConvertion(){
        MainPresenterListener listener = (MainPresenterListener) mContext;
        if(number != null && number >= 0){
            ArrayList<CurrencyResult> currencyList = new ArrayList<CurrencyResult>();
            String query = "SELECT rate_cod, rate_number FROM rate ORDER BY rate_cod";
            Cursor cursor =  databaseHelper.rawSelect(query, null);
            if(cursor.getCount() > 0){
                while(cursor.moveToNext()){
                    Float value = (float)(number * cursor.getDouble(1));
                    currencyList.add(new CurrencyResult(cursor.getString(0), value));
                }
            }
            cursor.close();
            //--------------------------------------------------------------------------------------
            if(currencyList.size() > 0)
                listener.onCompleteConvertion(currencyList);
            else
                listener.onCompleteConvertion(null);
        }
        else{
            listener.onCompleteConvertion(null);
        }
    }
    //**********************************************************************************************


    //**********************************************************************************************
    public interface MainPresenterListener{
        void onCompleteConvertion(ArrayList<CurrencyResult> list);
    }
    //**********************************************************************************************

}
