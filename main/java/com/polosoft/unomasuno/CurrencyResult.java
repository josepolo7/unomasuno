package com.polosoft.unomasuno;

/**
 * Created by JAPH on 3/6/18.
 */

public class CurrencyResult {
    public String currency_cod;
    public Float currency_value;
    //**********************************************************************************************
    public CurrencyResult(String param_cod, Float param_value){
        currency_cod = param_cod;
        currency_value = param_value;
    }
    //**********************************************************************************************
}
