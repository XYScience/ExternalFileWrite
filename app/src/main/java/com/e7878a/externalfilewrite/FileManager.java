package com.e7878a.externalfilewrite;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileManager {

    private static final String LOG_TAG = "FileManager";

    private static final String AUTHORITIES_FILE_PROVIDER_NAME = "com.e7878a.externalfilewrite.fileprovider";
    private static final String[] STANDARD_DIRECTORIES = {
            Environment.DIRECTORY_MUSIC,
            Environment.DIRECTORY_PODCASTS,
            Environment.DIRECTORY_RINGTONES,
            Environment.DIRECTORY_ALARMS,
            Environment.DIRECTORY_NOTIFICATIONS,
            Environment.DIRECTORY_PICTURES,
            Environment.DIRECTORY_MOVIES,
            Environment.DIRECTORY_DOWNLOADS,
            Environment.DIRECTORY_DCIM,
            Environment.DIRECTORY_DOCUMENTS,
            Environment.DIRECTORY_AUDIOBOOKS,
    };

    /**
     * 使用 FileProvider 写外部存储，可能存在失败的情况
     *
     * @param context
     * @param storageDirectory the storage directory to write: Environment.STANDARD_DIRECTORIES
     * @param fileName         the file name to write
     * @param fileContent      the content to write
     */
    public static void writeStrToSDWithFileProvider(Context context, String storageDirectory, String fileName, String fileContent) {
        Uri uri = getStorageDirectoryWithFileProvider(context, storageDirectory, fileName);
        if (uri == null) {
            Log.i(LOG_TAG, "writeStrToSDWithFileProvider return: uri is null");
        }
        writeBufferToStorageDirectoryWithUri(context, uri, getInputStreamWithStr(fileContent));
    }

    /**
     * 使用 FileProvider 写外部存储，可能存在失败的情况
     *
     * @param context
     * @param storageDirectory the storage directory to write: Environment.STANDARD_DIRECTORIES
     * @param fileName         the file name to write
     * @param fileNameAssets   the assets file to write
     */
    public static void writeAssetsToSDWithFileProvider(Context context, String storageDirectory, String fileName, String fileNameAssets) {
        Uri uri = getStorageDirectoryWithFileProvider(context, storageDirectory, fileName);
        if (uri == null) {
            Log.i(LOG_TAG, "writeAssetsToSDWithFileProvider return: uri is null");
        }
        writeBufferToStorageDirectoryWithUri(context, uri, getInputStreamWithAssets(context, fileNameAssets));
    }

    private static boolean isStandardDirectory(String dir) {
        for (String valid : STANDARD_DIRECTORIES) {
            if (valid.equals(dir)) {
                return true;
            }
        }
        return false;
    }

    private static Uri getStorageDirectoryWithFileProvider(Context context, String storageDirectory, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName)) {
            Log.i(LOG_TAG, "writeStorageDirectoryWithFileProvider return: param is null");
            return null;
        }
        if (!isStandardDirectory(storageDirectory)) {
            Log.i(LOG_TAG, "writeStorageDirectoryWithFileProvider return: isn`t standard directory");
            return null;
        }
        File directoryDownloads = Environment.getExternalStoragePublicDirectory(storageDirectory);
        if (directoryDownloads == null) {
            Log.i(LOG_TAG, "writeStorageDirectoryWithFileProvider return: directoryDownloads is null");
            return null;
        }
        Context contextApplication = context.getApplicationContext();
        String parentPath = directoryDownloads.getAbsolutePath();
        File file = new File(new File(parentPath), fileName);
        return FileProvider.getUriForFile(contextApplication, AUTHORITIES_FILE_PROVIDER_NAME, file);
    }

    private static void writeBufferToStorageDirectoryWithUri(Context context, Uri uri, InputStream is) {
        if (context == null || uri == null || is == null) {
            Log.i(LOG_TAG, "writeBufferToStorageDirectoryWithUri return: param is null");
            return;
        }
        Context contextApplication = context.getApplicationContext();
        if (contextApplication == null) {
            Log.i(LOG_TAG, "writeBufferToStorageDirectoryWithUri return: contextApplication is null");
            return;
        }
        ContentResolver contentResolver = contextApplication.getContentResolver();
        if (contentResolver == null) {
            Log.i(LOG_TAG, "writeBufferToStorageDirectoryWithUri return: contentResolver is null");
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
            Log.i(LOG_TAG, "writeBufferToStorageDirectoryWithUri success");
        } catch (Exception e) {
            Log.e(LOG_TAG, "writeBufferToStorageDirectoryWithUri: " + e);
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
                Log.e(LOG_TAG, "writeBufferToStorageDirectoryWithUri close", e);
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
