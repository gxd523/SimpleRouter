package com.demo.router;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.demo.router.annotation.Route;
import com.demo.router.core.Router;

@Route(path = "/activity/home")
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpModule2(View v) {
        Router.getInstance()
                .build("/activity/module2")
                .navigation();
    }

    public void jumpModule1(View v) {
        Router.getInstance()
                .build("/activity/module1")
                .navigation();
    }
}
