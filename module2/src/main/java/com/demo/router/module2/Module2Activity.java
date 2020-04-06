package com.demo.router.module2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.demo.router.annotation.Route;
import com.demo.router.api.Router;

@Route(path = "/module2/activity")
public class Module2Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module2);
    }

    public void jumpMain(View v) {
        Router.getInstance()
                .build("/main/activity")
                .navigation();
    }

    public void jumpModule1(View v) {
        Router.getInstance()
                .build("/module1/activity")
                .navigation();
    }
}