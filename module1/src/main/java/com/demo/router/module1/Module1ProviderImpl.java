package com.demo.router.module1;

import android.content.Context;
import android.widget.Toast;

import com.demo.router.annotation.Route;
import com.demo.router.provider.Module1Provider;

/**
 * Created by guoxiaodong on 2020/4/6 15:56
 */
@Route(path = "/module1/provider")
public class Module1ProviderImpl implements Module1Provider {
    @Override
    public void provideModule1(Context context) {
        Toast.makeText(context, "Module1ProviderImpl.provideModule1()调用成功!", Toast.LENGTH_SHORT).show();
    }
}
