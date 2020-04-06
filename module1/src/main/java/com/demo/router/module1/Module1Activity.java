package com.demo.router.module1;

import android.app.Activity;
import android.os.Bundle;

import com.demo.router.annotation.Route;

@Route(path = "/activity/module1")
public class Module1Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module1);
    }
}
