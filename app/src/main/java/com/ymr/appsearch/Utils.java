
package com.ymr.appsearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {

    private static final String TAG = "Utils";

    public static List<String> getPinyinList(List<String> list) {
        List<String> pinyinList = new ArrayList<String>();
        for (Iterator<String> i = list.iterator(); i.hasNext(); ) {
            String str = (String) i.next();
            try {
                String pinyin = getPinYin(str);
                pinyinList.add(pinyin);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        return pinyinList;
    }

    public static String getPinYin(String zhongwen) throws BadHanyuPinyinOutputFormatCombination {

        String zhongWenPinYin = "";
        char[] chars = zhongwen.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i],
                    getDefaultOutputFormat());
            if (pinYin != null) {
                zhongWenPinYin += pinYin[0];
            } else {
                zhongWenPinYin += chars[i];
            }
        }
        return zhongWenPinYin;
    }

    private static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        return format;
    }

    @SuppressLint("DefaultLocale")
    public static String getPinyinNum(String name, boolean full) {
        try {
            if (name != null && name.length() != 0) {
                int len = name.length();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < len; i++) {
                    String tmp = name.substring(i);
                    char c = tmp.charAt(0);
                    if (c <= '9' && c >= '0') {
                        sb.append(c);
                    } else {
                        if (full) {
                            String pinyin = Utils.getPinYin(tmp).toLowerCase();
                            for (int j = 0; j < pinyin.length(); j++) {
                                sb.append(getOneNumFromAlpha(pinyin.charAt(j)));
                            }
                        } else {
                            sb.append(getOneNumFromAlpha(Utils.getPinYin(tmp).toLowerCase()
                                    .charAt(0)));
                        }
                    }
                }
                return sb.toString();
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPinyinNum(String name) {
        if (name != null && name.length() != 0) {
            int len = name.length();
            if (len >= 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < len - 1; ) {
                    i++;
                    String simple;
                    simple = name.substring(0, i);
                    String full = name.substring(i, len);
                    String simpleNum = getPinyinNum(simple, false);
                    String fullNum = getPinyinNum(full, true);
                    sb.append(simpleNum);
                    sb.append(fullNum);
                }
                String num = sb.toString();
                Log.v(TAG, "name = " + name + " num = " + num);
                return num;
            }
        }
        return null;
    }

    private static String getOneNumFromAlpha(char firstAlpha) {
        switch (firstAlpha) {
            case 'a':
            case 'b':
            case 'c':
                return "2";
            case 'd':
            case 'e':
            case 'f':
                return "3";
            case 'g':
            case 'h':
            case 'i':
                return "4";
            case 'j':
            case 'k':
            case 'l':
                return "5";
            case 'm':
            case 'n':
            case 'o':
                return "6";
            case 'p':
            case 'q':
            case 'r':
            case 's':
                return "7";
            case 't':
            case 'u':
            case 'v':
                return "8";
            case 'w':
            case 'x':
            case 'y':
            case 'z':
                return "9";
            default:
                return "0";
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }

    public static String generateFilePath(Context context,String packageName) {
        return context.getFilesDir()+"/"+packageName+".png";
    }
}
