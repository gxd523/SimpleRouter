package com.demo.router.module1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.demo.router.annotation.Extra;
import com.demo.router.annotation.Route;
import com.demo.router.api.Router;
import com.demo.router.provider.MainProvider;

@Route(path = "/module1/activity")
public class Module1Activity extends Activity {
    @Extra("aaa")
    int intExtra;
    @Extra("bbb")
    String stringExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module1);
        Router.getInstance().inject(this);

        String text = String.format("%s...%s...%s", getClass().getSimpleName(), intExtra, stringExtra);
        ((TextView) findViewById(R.id.activity_module1_text_view)).setText(text);
    }

    public void jumpMain(View v) {
        Router.getInstance()
                .build("/main/activity")
                .navigation();
        finish();
    }

    public void callMainProvider(View v) {
        MainProvider mainProvider = (MainProvider) Router.getInstance()
                .build("/main/provider")
                .navigation();
        mainProvider.providerMain(this);
    }

    public void jumpModule2(View v) {
        Router.getInstance()
                .build("/module2/activity")
                .navigation();
        finish();
    }
}
