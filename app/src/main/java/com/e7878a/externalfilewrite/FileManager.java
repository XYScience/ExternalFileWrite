package com.e7878a.externalfilewrite;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class FileManager {

    private static final String LOG_TAG = "FileManager";
    private static final String DOCUMENTS_TXT = ".txt";
    private static final String DOCUMENTS_HTML = ".html";
    private static final String DOCUMENTS_XML = ".xml";

    /**
     * 使用 FileProvider 写外部存储，可能存在失败的情况
     *
     * @param context
     * @param fileType         the file type to write
     * @param fileName         the file name to write
     * @param fileNameAssets   the assets file to write
     */
    public static void writeAssetsToSDWithMediaStore(Context context, FileType fileType, String fileName, String fileNameAssets) {
        if (context == null) {
            Log.i(LOG_TAG, "writeAssetsToSDWithMediaStore return: context = null");
            return;
        }
        if (TextUtils.isEmpty(fileName)) {
            Log.i(LOG_TAG, "writeAssetsToSDWithMediaStore return: fileName = null");
            return;
        }
        if (TextUtils.isEmpty(fileNameAssets)) {
            Log.i(LOG_TAG, "writeAssetsToSDWithMediaStore return: fileNameAssets = null");
            return;
        }
        ContentValues value = new ContentValues();
        value.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
        Uri tableUri;
        switch (fileType) {
            case DOCUMENTS:
                tableUri = MediaStore.Files.getContentUri("external");
                if (fileName.endsWith(DOCUMENTS_TXT)) {
                    value.put(MediaStore.Files.FileColumns.MIME_TYPE, "text/plain");
                } else if (fileName.endsWith(DOCUMENTS_HTML)) {
                    value.put(MediaStore.Files.FileColumns.MIME_TYPE, "text/html");
                } else if (fileName.endsWith(DOCUMENTS_XML)) {
                    value.put(MediaStore.Files.FileColumns.MIME_TYPE, "text/xml");
                } else {
                    value.put(MediaStore.Files.FileColumns.MIME_TYPE, "text/*");
                }
                value.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);
                break;
            case PICTURES:
                tableUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                value.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/*");
                value.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                break;
            case MUSIC:
                tableUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                value.put(MediaStore.Audio.AudioColumns.MIME_TYPE, "audio/*");
                value.put(MediaStore.Audio.AudioColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC);
                break;
            case MOVIES:
                tableUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                value.put(MediaStore.Video.VideoColumns.MIME_TYPE, "video/*");
                value.put(MediaStore.Video.VideoColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES);
                break;
            default:
                tableUri = MediaStore.Files.getContentUri("external");
                value.put(MediaStore.Files.FileColumns.MIME_TYPE, "*/*");
                value.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                break;
        }

        Uri uri = null;
        try {
            uri = context.getContentResolver().insert(tableUri, value);
        } catch (Exception e) {
            Log.e(LOG_TAG, "writeAssetsToSDWithMediaStore insert: " + e);
        }
        if (uri == null) {
            return;
        }
        writeBufferToSDWithUri(context, uri, getInputStreamWithAssets(context, fileNameAssets));
    }

    private static void writeBufferToSDWithUri(Context context, Uri uri, InputStream is) {
        if (context == null || uri == null || is == null) {
            Log.i(LOG_TAG, "writeBufferToSDWithUri return: param is null");
            return;
        }
        Context contextApplication = context.getApplicationContext();
        if (contextApplication == null) {
            Log.i(LOG_TAG, "writeBufferToSDWithUri return: contextApplication is null");
            return;
        }
        ContentResolver contentResolver = contextApplication.getContentResolver();
        if (contentResolver == null) {
            Log.i(LOG_TAG, "writeBufferToSDWithUri return: contentResolver is null");
            return;
        }
        OutputStream os = null;
        try {
            os = contentResolver.openOutputStream(uri);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            Log.i(LOG_TAG, "writeBufferToSDWithUri success");
        } catch (Exception e) {
            Log.e(LOG_TAG, "writeBufferToSDWithUri: " + e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "writeBufferToSDWithUri close", e);
            }
        }
    }

    private static InputStream getInputStreamWithStr(String content) {
        if (TextUtils.isEmpty(content)) {
            Log.e(LOG_TAG, "content is null");
            return null;
        }
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Log.e(LOG_TAG, "getInputStreamWithStr: " + e);
        }
        return is;
    }

    private static InputStream getInputStreamWithAssets(Context context, String fileNameAssets) {
        if (context == null) {
            Log.e(LOG_TAG, "context is null");
            return null;
        }
        if (TextUtils.isEmpty(fileNameAssets)) {
            Log.e(LOG_TAG, "fileNameAssets is null");
            return null;
        }
        InputStream is = null;
        try {
            is = context.getAssets().open(fileNameAssets);
        } catch (Exception e) {
            Log.e(LOG_TAG, "getInputStreamWithAssets: " + e);
        }
        return is;
    }
}
