package com.zk.taxi.http;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Values implements Parcelable {


    private Map<String, String> mValues;

    public Values() {
        mValues = new HashMap<String, String>(8);
    }

    public static Values newInstance() {
        return new Values();
    }

    public Values(int size) {
        mValues = new HashMap<String, String>(size, 1.0f);
    }

    public Values(Values from) {
        mValues = new HashMap<String, String>(from.mValues);
    }

    private Values(HashMap<String, String> values) {
        mValues = values;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Values)) {
            return false;
        }
        return mValues.equals(((Values) object).mValues);
    }

    @Override
    public int hashCode() {
        return mValues.hashCode();
    }

    public void put(String key, String value) {
        mValues.put(key, value);
    }

    public void putAll(Values other) {
        mValues.putAll(other.mValues);
    }

    public void putNull(String key) {
        mValues.put(key, null);
    }

    public int size() {
        return mValues.size();
    }

    public void remove(String key) {
        mValues.remove(key);
    }

    public Map<String, String> getData() {
        return mValues;
    }

    public void clear() {
        mValues.clear();
    }

    public boolean containsKey(String key) {
        return mValues.containsKey(key);
    }

    public Set<Entry<String, String>> valueSet() {
        return mValues.entrySet();
    }

    public Set<String> keySet() {
        return mValues.keySet();
    }
    public Object get(String key) {
        return mValues.get(key);
    }
    public int describeContents() {
        return 0;
    }

    public static final Creator<Values> CREATOR = new Creator<Values>() {
        public Values createFromParcel(Parcel in) {
            HashMap<String, String> values = in.readHashMap(null);
            return new Values(values);
        }

        public Values[] newArray(int size) {
            return new Values[size];
        }
    };

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeMap(mValues);
    }

    public void sort() {
        // 排序
        List<Entry<String, String>> list = new ArrayList<Entry<String, String>>(mValues.entrySet());
        Collections.sort(list, new Comparator<Entry<String, String>>() {
            @Override
            public int compare(Entry<String, String> lhs, Entry<String, String> rhs) {
                return (lhs.getKey()).toString().compareTo(rhs.getKey());
            }
        });
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<String>(mValues.keySet());
        Collections.sort(list, Collator.getInstance(java.util.Locale.getDefault()));//升序
        for (String name : list) {
            String value = getAsString(name);
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(name + "=" + value);
        }
        return sb.toString();
    }
    public String postToJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (String name : mValues.keySet()) {
            String value = getAsString(name);
            if (sb.length() > 1) {
                sb.append(",");
            }
            if (name.equals("postdata")) {
                sb.append("\"" + name + "\":" + value);
            } else {
                sb.append("\"" + name + "\":\"" + value + "\"");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    public String getAsString(String key) {
        Object value = mValues.get(key);
        return value != null ? value.toString() : null;
    }

    public String toJson() {
        return new Gson().toJson(mValues);
    }

}
