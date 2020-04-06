package com.demo.router.module2;

import android.app.Activity;
import android.os.Bundle;

import com.demo.router.annotation.Route;

@Route(path = "/activity/module2")
public class Module2Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module2);
    }
}