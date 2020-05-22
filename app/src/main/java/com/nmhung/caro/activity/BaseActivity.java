package com.nmhung.caro.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.gson.Gson;

public abstract class BaseActivity extends AppCompatActivity {
    protected int layoutResID;
    protected static Gson gson;

    public BaseActivity(@LayoutRes int layoutResID) {
        this.layoutResID = layoutResID;
        gson = new Gson();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.layoutResID);
        findViews();
        setEvents();
    }

    protected abstract void findViews();

    protected abstract void setEvents();

    protected void startActivity(Class<? extends Activity> classActivity, Pair<String, Object>... pairs) {
        Intent intent = new Intent(this, classActivity);
        for (Pair<String, Object> pair : pairs) {
            String json = this.gson.toJson(pair.second);
            intent.putExtra(pair.first, json);
        }
        startActivity(intent);
    }

    public static <T> Intent newIntent(Context packageContext, Class<T> classActivity, Pair<String, Object>... pairs) {
        Intent intent = new Intent(packageContext, classActivity);
        for (Pair<String, Object> pair : pairs) {
            String json = BaseActivity.gson.toJson(pair.second);
            intent.putExtra(pair.first, json);
        }
        return intent;
    }

//    public static <T> T getObjectExtra(Intent intent, String key, Class<T> tClass){
//
//    }

    protected <T> T getObjectExtra(String key, Class<T> tClass) {
        String json = this.getIntent().getStringExtra(key);
        if (json == null) {
            throw new NullPointerException("Get String Extra Null Pointer Exception");
        }
        try {
            T rs = this.gson.fromJson(json, tClass);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }
}
