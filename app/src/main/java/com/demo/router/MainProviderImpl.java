package com.demo.router;

import android.content.Context;
import android.widget.Toast;

import com.demo.router.annotation.Route;
import com.demo.router.provider.MainProvider;

/**
 * Created by guoxiaodong on 2020/4/7 10:01
 */
@Route(path = "/main/provider")
public class MainProviderImpl implements MainProvider {
    @Override
    public void providerMain(Context context) {
        Toast.makeText(context, "MainProviderImpl.providerMain()..." + context.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
    }
}
