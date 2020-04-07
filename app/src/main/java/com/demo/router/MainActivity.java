package com.demo.router;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.demo.router.annotation.Route;
import com.demo.router.api.Router;
import com.demo.router.provider.Module1Provider;

@Route(path = "/main/activity")
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpModule1(View v) {
        Router.getInstance()
                .build("/module1/activity")
                .withInt("aaa", 112233)
                .withString("bbb", "aabbcc")
                .navigation();
        finish();
    }

    public void jumpModule2(View v) {
        Router.getInstance()
                .build("/module2/activity")
                .navigation();
        finish();
    }

    public void callModule1Provider(View v) {
        Module1Provider module1Provider = (Module1Provider) Router.getInstance()
                .build("/module1/provider")
                .navigation();
        module1Provider.provideModule1(this);
    }
}
