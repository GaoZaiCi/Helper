package com.fmp.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HelperJSONObject extends JSONObject {

    public HelperJSONObject(){
        super();
    }

    public HelperJSONObject(String json) throws JSONException {
        super(json);
    }

    public Object get(String name,Class<?> clazz) throws JSONException {
        if (clazz.equals(String.class)){
            return super.getString(name);
        }
        if (clazz.equals(int.class)){
            return super.getInt(name);
        }
        if (clazz.equals(long.class)){
            return super.getLong(name);
        }
        if (clazz.equals(double.class)){
            return super.getDouble(name);
        }
        if (clazz.equals(boolean.class)){
            return super.getBoolean(name);
        }
        if (clazz.equals(JSONArray.class)){
            return super.getJSONArray(name);
        }
        if (clazz.equals(JSONObject.class)){
            return super.getJSONObject(name);
        }
        throw new JSONException(String.format("not object %s", clazz.getName()));
    }

    public JSONObject put(String name,Object value) throws JSONException {
        if (value instanceof String){
            super.put(name, value);
            return this;
        }
        if (value instanceof Integer){
            super.put(name, (int)value);
            return this;
        }
        if (value instanceof Long){
            super.put(name, (long) value);
            return this;
        }
        if (value instanceof Double){
            super.put(name, (double)value);
            return this;
        }
        if (value instanceof Boolean){
            super.put(name, (boolean)value);
            return this;
        }
        if (value instanceof JSONArray){
            super.put(name, (JSONArray)value);
            return this;
        }
        if (value instanceof JSONObject){
            super.put(name, (JSONObject)value);
            return this;
        }
        throw new JSONException(String.format("not object %s", value.getClass().getName()));
    }
}
