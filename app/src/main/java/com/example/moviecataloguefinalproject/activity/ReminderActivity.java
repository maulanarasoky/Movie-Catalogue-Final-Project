package com.example.moviecataloguefinalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.model.ReminderData;
import com.example.moviecataloguefinalproject.preference.ReminderPreference;
import com.example.moviecataloguefinalproject.reminder.AlarmReceiver;

import java.util.Objects;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    SwitchCompat setDailyReminder;
    SwitchCompat setReleaseReminder;

    ReminderData reminderData;

    ReminderPreference reminderPreference;

    AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        setActionBarTitle();

        setDailyReminder = findViewById(R.id.set_daily_reminder);
        setReleaseReminder = findViewById(R.id.set_release_reminder);

        reminderPreference = new ReminderPreference(this);
        alarmReceiver = new AlarmReceiver();

        showExistingPreference();

        setDailyReminder.setOnClickListener(this);
        setReleaseReminder.setOnClickListener(this);

    }


    private void setActionBarTitle() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reminder Setting");
    }

    private void showExistingPreference(){
        reminderData = reminderPreference.getCondition();

        populateView(reminderData);
    }

    private void populateView(ReminderData reminderData){
        if (reminderData.isConditionDaily()){
            setDailyReminder.setChecked(true);
        }else {
            setDailyReminder.setChecked(false);
        }

        if (reminderData.isConditionRelease()){
            setReleaseReminder.setChecked(true);
        }else {
            setReleaseReminder.setChecked(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_daily_reminder:
                if (reminderData.isConditionDaily()){
                    reminderData.setConditionDaily(false);
                    alarmReceiver.cancelDailyReminder(this, AlarmReceiver.TYPE_DAILY_REMINDER);
                    showToast("Daily Reminder Dinonaktifkan");
                }else {
                    reminderData.setConditionDaily(true);
                    String message = "This is Daily Reminder";
                    alarmReceiver.setDailyReminder(this,AlarmReceiver.TYPE_DAILY_REMINDER, message);
                    showToast("Daily Reminder Diaktifkan");
                }
                reminderPreference.setConditionDaily(reminderData);
                break;
            case R.id.set_release_reminder:
                if (reminderData.isConditionRelease()){
                    reminderData.setConditionRelease(false);
                    alarmReceiver.cancelReleaseDateTodayAlarm(this, AlarmReceiver.TYPE_RELEASE_REMINDER);
                    showToast("Release Reminder Dinonaktifkan");
                }else{
                    reminderData.setConditionRelease(true);
                    alarmReceiver.setReleaseDateTodayReminderAlarm(this, AlarmReceiver.TYPE_RELEASE_REMINDER);
                    showToast("Release Reminder Diaktifkan");
                }
                reminderPreference.setConditionRelease(reminderData);
                break;
        }
    }

    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
