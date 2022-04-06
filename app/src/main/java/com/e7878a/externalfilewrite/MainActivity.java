package com.e7878a.externalfilewrite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String ASSETS_TXT = "txt.txt";
    public static final String ASSETS_HTML = "html.html";
    public static final String ASSETS_XML = "xml.xml";
    public static final String ASSETS_CSS = "css.css";
    public static final String ASSETS_IMAGE = "image.png";
    public static final String ASSETS_MP3 = "mp3.mp3";
    public static final String ASSETS_MP4 = "mp4.mp4";
    public static final String ASSETS_JSON = "json.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithMediaStore(MainActivity.this,
                        FileType.DOCUMENTS, "txt.txt", ASSETS_TXT);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithMediaStore(MainActivity.this,
                        FileType.DOCUMENTS, "国际上网隐私政策.html", ASSETS_HTML);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithMediaStore(MainActivity.this,
                        FileType.DOCUMENTS, "xml.xml", ASSETS_XML);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithMediaStore(MainActivity.this,
                        FileType.DOCUMENTS, "css.css", ASSETS_CSS);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithMediaStore(MainActivity.this,
                        FileType.PICTURES, "image.png", ASSETS_IMAGE);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithMediaStore(MainActivity.this,
                        FileType.MUSIC, "mp3.mp3", ASSETS_MP3);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithMediaStore(MainActivity.this,
                        FileType.MOVIES, "mp4.mp4", ASSETS_MP4);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeAssetsToSDWithMediaStore(MainActivity.this,
                        FileType.OTHERS, "json.json", ASSETS_JSON);
            }
        }).start();

    }
}