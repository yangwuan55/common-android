package com.ymr.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static boolean deleteByName(Context context, String filename) {
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
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static <T> void writeListToFile(Context context,List<T> list,String fileName) {
        if (list != null) {
            writeStringToFile(context,ArrayToString(list),fileName);
        }
    }

    public static <T> List<T> getListFromFile(Context context,String filename,Class<T[]> clazz) {
        String str = readStringFromFile(context, filename);
        if (!TextUtils.isEmpty(str)) {
            return stringToArray(str, clazz);
        }
        return null;
    }

    public static <T> String ArrayToString(List<T> list) {
        Gson g = new Gson();
        return g.toJson(list);
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return new ArrayList<T>(Arrays.asList(arr)); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }
}
