package com.example.moviecataloguefinalproject.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.moviecataloguefinalproject.model.ReminderData;

public class ReminderPreference {

    private static final String PREFS_NAME = "user_pref";
    private static final String CONDITION_DAILY = "condition_daily";
    private static final String CONDITION_RELEASE = "condition_release";

    private final SharedPreferences sharedPreferences;

    public ReminderPreference(Context context){
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setConditionDaily(ReminderData data){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(CONDITION_DAILY, data.isConditionDaily());

        editor.apply();
    }

    public void setConditionRelease(ReminderData data){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(CONDITION_RELEASE, data.isConditionRelease());

        editor.apply();
    }

    public ReminderData getCondition(){
        ReminderData data = new ReminderData();
        data.setConditionDaily(sharedPreferences.getBoolean(CONDITION_DAILY, false));
        data.setConditionRelease(sharedPreferences.getBoolean(CONDITION_RELEASE, false));
        return data;
    }
}
