package com.demo.router;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.demo.router.annotation.Route;
import com.demo.router.api.Router;
import com.demo.router.provider.Module1Provider;

/**
 * 1、通过注解处理器过滤出使用注解的地方
 * 2、通过javapoet生成java类，本质是一段添加map元素的代码，key是路径，value是被启动的类
 * 3、找到本apk安装路径，将dex都转化成对象，寻找第2步生成的添加map元素的工具类
 * 4、反射出工具类实例，传入map，添加元素，就获取到了一个<路径，类>表
 * 5、跳转时在map中寻找此路径的类，添加跳转
 */
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
