package com.ymr.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Class Name: FileUtil
 *
 * @author Bear Description: Created on : 2014年11月17日 下午5:48:38 Comments:
 */
public class FileUtil {

    public static boolean isAssetsExistWithStore(String name, Context mContext) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        try {
            InputStream is = mContext.getResources().getAssets().open(name);
            FileOutputStream os = mContext.openFileOutput(name, Context.MODE_WORLD_READABLE);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > -1) {
                os.write(buffer);
            }
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 将字符串写入文件
     *
     * @param context
     * @param data
     * @param fileName
     * @return
     */
    public static boolean writeStringToFile(Context context, String data, String fileName) {
        FileOutputStream fout = null;
        BufferedWriter writer = null;
        try {
            fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fout, "UTF-8"));
            writer.write(data);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static String readStringFromFile(Context context, String fileName) {
        try {
            File file = getFileByName(context, fileName);
            if (file.exists()) {
                InputStream is = context.openFileInput(fileName);
                return readBeanFromInputStream(context, is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @NonNull
    private static File getFileByName(Context context, String fileName) {
        return new File(context.getFilesDir() + "/" + fileName);
    }

    public static <T> T readBeanFromFile(Context context, String fileName, Class<T> c) {
        String data = readStringFromFile(context, fileName);
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(data, c);
    }

    public static <T> boolean writeBeanToFile(Context context, String fileName, T t) {
        String s = new Gson().toJson(t);
        return writeStringToFile(context, s, fileName);
    }

    private static String readBeanFromInputStream(Context context, InputStream is) {
        if (is == null) {
            return null;
        }
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static <T> T readBeanFromInputStream(Context context, InputStream is, Class<T> c) {
        String data = readBeanFromInputStream(context, is);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(data, c);
    }

    public static <T> T readBeanFromRaw(Context context, int rawId, Class<T> c) {
        return readBeanFromInputStream(context, context.getResources().openRawResource(rawId), c);
    }

    public static boolean deleteByName(Context context,String filename) {
        File fileByName = getFileByName(context, filename);
        if (fileByName.exists()) {
            return fileByName.delete();
        }
        return false;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
