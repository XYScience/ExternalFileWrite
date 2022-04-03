package com.e7878a.externalfilewrite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends AppCompatActivity {

    public static final String ASSETS_TEXT = "privacy_zh_CN.html";
    public static final String ASSETS_IMAGE = "alipay.png";
    public static final String ASSETS_MP3 = "bgm.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithFileProvider(MainActivity.this,
                        Environment.DIRECTORY_DOCUMENTS, "隐私政策.html", ASSETS_TEXT);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithFileProvider(MainActivity.this,
                        Environment.DIRECTORY_PICTURES, "alipay.png", ASSETS_IMAGE);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithFileProvider(MainActivity.this,
                        Environment.DIRECTORY_MUSIC, "bgm.mp3", ASSETS_MP3);
            }
        }).start();
    }
}