package com.example.moviecataloguefinalproject.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.moviecataloguefinalproject.adapter.WidgetAdapter;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetAdapter(this.getApplicationContext());
    }
}
