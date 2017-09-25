package com.example.harish.group14_hw08;
/*
*
* Homework 08
* SettingsActivity.java
* Group 14 : Harish Pendyala
*
* */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String PREF_KEY = "PREF_UNIT";
    public static final String CURR_EXISTS = "CURR_EXISTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
        .replace(android.R.id.content,new SettingFrag()).commit();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(PREF_KEY)) {
            Intent in = new Intent();
            in.putExtra("TEMP", "OK");
            setResult(Activity.RESULT_OK,in);
            this.finish();
        } else  {

            Intent in = new Intent();
            in.putExtra("TEMP", "NO");
            setResult(Activity.RESULT_OK,in);
            this.finish();
        }
    }

    public static class SettingFrag extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
           // addPreferencesFromResource(R.xml.pref2);
        }
    }
}
