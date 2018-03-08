package com.polosoft.unomasuno;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAPH on 3/6/18.
 */


public class MainActivity extends AppCompatActivity implements MainPresenter.MainPresenterListener{
    private MainPresenter mainPresenter;
    //----------------------------------------------------------------------------------------------
    private final int PERMISSION_REQUEST_INTERNET = 1001;
    //----------------------------------------------------------------------------------------------
    private ImageButton convertButton;
    private EditText convertEditText;
    private TextView lastupdateTextView;
    private ListView resultListView;
    private ResultListAdapter resultAdapter;
    //**********************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------------------------------------------------------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------------------------------------------------------------------------------------
        mainPresenter = new MainPresenter(this);
        //------------------------------------------------------------------------------------------
        convertEditText = (EditText)findViewById(R.id.convert_edittext);
        //------------------------------------------------------------------------------------------
        lastupdateTextView = (TextView)findViewById(R.id.last_update_textview);
        lastupdateTextView.setText(mainPresenter.getLastUpdateStr());
        //------------------------------------------------------------------------------------------
        convertButton = (ImageButton)findViewById(R.id.convert_button);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberStr = convertEditText.getText().toString();
                if(!numberStr.isEmpty() && numberStr != null)
                    mainPresenter.iniConvertion(numberStr);
            }
        });
        //------------------------------------------------------------------------------------------
        resultListView = (ListView)findViewById(R.id.result_listview);
        resultAdapter = new ResultListAdapter(this, new ArrayList<CurrencyResult>());
        resultListView.setAdapter(resultAdapter);
        //------------------------------------------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permissions[] = new String[]{Manifest.permission.INTERNET};
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_INTERNET);
            }
        }
        //------------------------------------------------------------------------------------------

    }
    //**********************************************************************************************
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case PERMISSION_REQUEST_INTERNET:{
                if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    showMessage("Debes asignar permiso de acceso a Internet para actualizar las tasas de conversión");
                }
                return;
            }
        }
    }

    //**********************************************************************************************
    @Override
    public void onCompleteConvertion(ArrayList<CurrencyResult> list) {
        lastupdateTextView.setText(mainPresenter.getLastUpdateStr());
        resultAdapter.clear();
        if(list != null){
            resultAdapter.addAll(list);
        }
        resultAdapter.notifyDataSetChanged();
        resultListView.invalidate();
    }
    //**********************************************************************************************
    private void showMessage(String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("Cerrar", null)
                .create()
                .show();
    }
    //**********************************************************************************************

}
