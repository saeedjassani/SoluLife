package com.sapa.solulife.data;

/**
 * Created by SAGAR on 3/30/2016.
 */
public class KeyValuePairData {

    String key,value;

    public KeyValuePairData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
