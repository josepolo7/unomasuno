package com.polosoft.unomasuno;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by JAPH on 3/6/18.
 */

public class ResultListAdapter extends ArrayAdapter<CurrencyResult> {
    private Context mContext;
    private ArrayList<CurrencyResult> resultList;
    private LayoutInflater mInflater = null;
    private int lastPosition = -1;
    //**********************************************************************************************
    public ResultListAdapter(@NonNull Context context, ArrayList<CurrencyResult> list) {
        super(context, R.layout.item_resultlistarray, list);
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //------------------------------------------------------------------------------------------
        resultList = list;
    }
    //**********************************************************************************************
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = null;
        if(resultList != null) {
            final CurrencyResult mCurrentItem = (CurrencyResult) resultList.get(position);
            //--------------------------------------------------------------------------------------
            rowView = mInflater.inflate(R.layout.item_resultlistarray, null);
            //--------------------------------------------------------------------------------------
            PlaceHolder placeHolder = new PlaceHolder();
            //--------------------------------------------------------------------------------------
            placeHolder.currencycodeTextView = (TextView) rowView.findViewById(R.id.currencycode_textview);
            placeHolder.currencycodeTextView.setText(mCurrentItem.currency_cod);
            //--------------------------------------------------------------------------------------
            placeHolder.currencyvalueTextView = (TextView) rowView.findViewById(R.id.currencyvalue_textview);
            placeHolder.currencyvalueTextView.setText(String.valueOf(String.format("%,.2f", mCurrentItem.currency_value)));
            //--------------------------------------------------------------------------------------
            placeHolder.flagImageView = (ImageView)rowView.findViewById(R.id.flag_imageview);
            try{
                InputStream input = mContext.getAssets().open("flags/" + mCurrentItem.currency_cod.toLowerCase() + ".png");
                Drawable flagDrawable = Drawable.createFromStream(input, null);
                placeHolder.flagImageView.setImageDrawable(flagDrawable);
                input.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
            //
            //--------------------------------------------------------------------------------------
        }
        //--------------------------------------------------------------------------------------
        if(rowView != null){
            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.move_up : R.anim.go_down);
            rowView.startAnimation(animation);
            lastPosition = position;
        }
        //--------------------------------------------------------------------------------------
        return rowView;
    }
    //**********************************************************************************************



    //**********************************************************************************************
    protected static class PlaceHolder{
        protected static TextView  currencycodeTextView;
        protected static TextView  currencyvalueTextView;
        protected static ImageView flagImageView;
    }
    //**********************************************************************************************

}
