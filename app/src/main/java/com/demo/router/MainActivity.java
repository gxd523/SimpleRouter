package com.demo.router;

import android.app.Activity;
import android.os.Bundle;

import com.demo.router.annotation.Route;

@Route(path = "/main/test")
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
