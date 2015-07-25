package com.ymr.common.util;

import android.content.Context;
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
            File file = new File(context.getFilesDir() + "/" + fileName);
            if (file.exists()) {
                InputStream is = context.openFileInput(fileName);
                return readBeanFromInputStream(context, is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
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

    public static void cp(String sourcePath,String targetPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(sourcePath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(sourcePath); //读入原文件
                FileOutputStream fs = new FileOutputStream(targetPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
    }

    public static void cp(FileInputStream inputStream,FileOutputStream outputStream) {
        int bytesum = 0;
        int byteread = 0;
        byte[] buffer = new byte[1444];
        try {
            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                System.out.println(bytesum);
                outputStream.write(buffer, 0, byteread);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
