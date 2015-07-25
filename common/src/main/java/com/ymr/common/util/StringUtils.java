package com.ymr.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * 
 * 判断字符串的一些方法
 * 
 */
public class StringUtils {
	/**
	 * 从 Unicode 码转换成编码前的特殊字符串。
	 * 
	 * @param in
	 *            Unicode编码的字符数组。
	 * @param off
	 *            转换的起始偏移量。
	 * @param len
	 *            转换的字符长度。
	 * @param convtBuf
	 *            转换的缓存字符数组。
	 * @return 完成转换，返回编码前的特殊字符串。
	 */
	public static String unicodeToString(char[] in, int off, int len, char[] convtBuf) {
		if (convtBuf.length < len) {
			int newLen = len * 2;
			if (newLen < 0) {
				newLen = Integer.MAX_VALUE;
			}
			convtBuf = new char[newLen];
		}
		char aChar;
		char[] out = convtBuf;
		int outLen = 0;
		int end = off + len;

		while (off < end) {
			aChar = in[off++];
			if (aChar == '\\') {
				aChar = in[off++];
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = in[off++];
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					out[outLen++] = (char) value;
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					out[outLen++] = aChar;
				}
			} else {
				out[outLen++] = aChar;
			}
		}
		return new String(out, 0, outLen);
	}

	/**
	 * 获得汉语拼音首字母
	 * 
	 * @param str
	 * @return
	 */
	public static String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("[A-Za-z]");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(Locale.US);
		} else {
			return "#";
		}
	}

	/**
	 * 将字符串编码成 Unicode 。
	 * 
	 * @param theString
	 *            待转换成Unicode编码的字符串。
	 * @param escapeSpace
	 *            是否忽略空格。
	 * @return 返回转换后Unicode编码的字符串。
	 */
	public static String stringToUnicode(String theString, boolean escapeSpace) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace)
					outBuffer.append('\\');
				outBuffer.append(' ');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			case '=': // Fall through
			case ':': // Fall through
			case '#': // Fall through
			case '!':
				outBuffer.append('\\');
				outBuffer.append(aChar);
				break;
			default:
				if ((aChar < 0x0020) || (aChar > 0x007e)) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(toHex((aChar >> 12) & 0xF));
					outBuffer.append(toHex((aChar >> 8) & 0xF));
					outBuffer.append(toHex((aChar >> 4) & 0xF));
					outBuffer.append(toHex(aChar & 0xF));
				} else {
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}

	/**
	 * Convert a nibble to a hex character
	 * 
	 * @param nibble
	 *            the nibble to convert.
	 */
	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	/** A table of hex digits */
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 将byte[]转换成十六进制字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * url后面拼接随机数
	 * 
	 * @param str
	 * @return
	 */
	public static String getURLRandom(String str) {
		if (!TextUtils.isEmpty(str)) {
			return "?" + UUID.randomUUID();
		}
		return "";
	}

	public static boolean isSpell(String str) {
		return str.matches("[a-zA-Z]+");
	}

	/**
	 * double传成字符串，如果有小数精确到一位，如果没有转成整数
	 * 
	 * @param d
	 * @return
	 */
	public static String doubleToString(double d) {
		String res = "0";
		if (d > 0) {
			int decimals = ((int) (d * 10)) % 10;
			if (decimals > 0) {
				BigDecimal b = new BigDecimal(d);
				double avgprice = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				res = String.valueOf(avgprice);
			} else {
				res = String.valueOf((int) d);
			}
		}
		return res;
	}

	/**
	 * String传成double
	 * 
	 * @param d
	 * @return
	 */
	public static double stringToDouble(String d) {
		double res = 0;
		try {
			res = Double.valueOf(d);
		} catch (Exception e) {
			LOGGER.e("StringUtil", "stringToDouble error", e);
		}

		return res;
	}

	/**
	 * double传成字符串，如果有小数精确到一位，如果没有转成整数
	 * 
	 * @param d
	 * @return
	 */
	public static String floatToString(float d) {
		String res = "";
		if (d > 0) {
			int decimals = ((int) (d * 10)) / 10;
			if (decimals > 0) {
				BigDecimal b = new BigDecimal(d);
				double avgprice = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				res = String.valueOf(avgprice);
			} else {
				res = String.valueOf((int) d);
			}
		}
		return res;
	}

	/**
	 * 正则表达式规则
	 * 
	 * @author liunz
	 * 
	 */
	public class MatchString {
		public static final String NUMBERISNOTPHONE = "\\S+#^((?!电话|手机|联系方式|号码).)*$";
		public static final String NUMBEREXCEPTZERO = "^\\S+$#^[1-9]$";
		public static final String NUMBEREPHONE = "((\\d{11})|(400\\d{7})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{1,5})|(\\d{7,8})-(\\d{1,5}))$)";
		public static final String NUMBERETITLE = "^(\\D){6,25}$";
		public static final String NUMBEREPERSON = "^(\\D){2,6}$";
		public static final String NUMBERISMOBILEPHONE = "(^1(3[4-9]|5[012789]|8[2378])\\d{8}$)|(^18[09]\\d{8}$)|(^1(3[0-2]|5[56]|8[56])\\d{8}$)|(^1[35]3\\d{8}$)";
		public static final String NUMBERISMOBILE="^1[3|4|5|7|8][0-9]{9}$";
	}

	/**
	 * 判断字符串是否为空 ps:此方法在android中有代替，TextUtils.isEmpty
	 * 
	 * @param str
	 * @return 为空或者null返回true
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	/**
	 * 判断字符串是否为空或者null字符串
	 * 
	 * @param str
	 * @return 为空或者null或者“null”返回true
	 */
	public static boolean isEmptyNull(String str) {
		return str == null || "".equals(str.trim()) || "null".equals(str);
	}

	/**
	 * 验证电话号码合法性
	 * 
	 * @param telephone
	 * @return 合法返回true
	 */
	public static boolean checkPhone(String telephone) {
		Pattern p = Pattern.compile(MatchString.NUMBEREPHONE);
		Matcher m = p.matcher(telephone);
		return m.matches();
	}

	
	/**
	 * //判断是否手机号码
 	 * @author Bear
 	 * @param mobile
	 * @return 合法返回true
 	 */
	public static boolean checkMobile(String mobile){
		Pattern p = Pattern.compile(MatchString.NUMBERISMOBILE);
		Matcher m = p.matcher(mobile);
		return m.matches();
		
	}
	
	
	
	/**
	 * 将数组转为字符串
	 * 
	 * @param array
	 *            字节数组
	 * @param encode
	 *            编码方式
	 * @return 编码后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String convertByteArrayToString(byte[] array, String encode) throws UnsupportedEncodingException {
		if (array == null || array.length == 0) {
			return null;
		}
		return new String(array, encode);
	}

	/**
	 * 参数为null或者空串，返回空串，否则原样返回
	 * 
	 * @param param
	 * @return 如果参数为null或者空串，返回空串，否则原样返回
	 */
	public static String nvl(String param) {
		if (TextUtils.isEmpty(param)) {
			return "";
		} else {
			return param;
		}
	}

	/**
	 * 返回int型参数的string值，将int转换为String
	 * 
	 * @param param
	 * @return int型参数的string值
	 */
	public static String nvl(int param) {
		return String.valueOf(param);
	}

	// ---------------------下面的方法目前未使用----------------------------//

	/**
	 * 将数组转为utf-8格式的字符串
	 * 
	 * @param array
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String convertByteArrayToString(byte[] array) throws UnsupportedEncodingException {
		return convertByteArrayToString(array, "utf-8");
	}

	/**
	 * 此方法用途是输入一个字符串，再输入一个int的值，这里每个字符的显示大小是按28像素计算的 <br/>
	 * 如“大家过年好啊”，gap=300。然后根据设备宽度像素减掉gap后，看可以显示的字符有哪些,如“大家...”。 <br/>
	 * 此方法在计算过程还考虑了“。。。”的宽度，所以再输入参数gap的时候， <br/>
	 * 为了达到你的效果，还要考虑下“。。。”的宽度。
	 * 
	 * @param srcString
	 *            要截取的字符串
	 * @param contexts
	 * @param gap
	 *            手机设备不显示字符串的像素
	 * @return 截取字符串后的值
	 */
	public static String getDisplayString(String srcString, Context contexts, int gap) {
		final int TEXT_GAP = gap;
		final int TEXT_SIZE = 28;
		srcString = (srcString == null ? "" : srcString);
		String tmpString = srcString + "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = contexts.getResources().getDisplayMetrics();
		Paint pFont = new Paint();
		pFont.setTextSize(TEXT_SIZE);
		if (pFont.measureText(srcString) > dm.widthPixels - TEXT_GAP) {
			int i = 0;
			do {
				i++;
				tmpString = srcString.substring(0, srcString.length() - i) + " ...";
				if (i >= srcString.length())
					break;
			} while (pFont.measureText(tmpString) > dm.widthPixels - TEXT_GAP);
		}
		return tmpString;
	}

	/**
	 * 此方法用途是输入int的值，看这个int的值在手机设备中如果显示字符，宽度能占多少的像素，这里每个字符的显示大小是按28像素计算的。
	 * 
	 * @param max
	 *            输入int，看这个int的值在手机设备中如果显示字符，宽度能占多少的像素
	 * @param contexts
	 * @return max的值在手机设备中如果显示字符，宽度能占的像素值
	 */
	public static int getDisplayStringLength(Context contexts, int max) {
		final int TEXT_GAP = 0;// Constant.TEXT_GAP;
		final int TEXT_SIZE = 28;
		DisplayMetrics dm = new DisplayMetrics();
		dm = contexts.getResources().getDisplayMetrics();
		Paint pFont = new Paint();
		pFont.setTextSize(TEXT_SIZE);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < max; i++) {
			sb.append("中");
		}
		int txtWidth = (int) pFont.measureText(sb.toString());
		if (txtWidth > dm.widthPixels - TEXT_GAP) {
			return dm.widthPixels - TEXT_GAP;
		}
		return txtWidth;
	}

	/**
	 * 此方法用途是输入字符串，看字符串的长度在手机设备中能占多少的像素，这里每个字符的显示大小是按28像素计算的。
	 * 
	 * @param contexts
	 * @param str
	 *            字符串
	 * @return 字符串的长度在手机设备中占的像素
	 */
	public static int getDisplayByString(Context contexts, String str) {
		final int TEXT_GAP = 0;// Constant.TEXT_GAP;
		final int TEXT_SIZE = 28;
		DisplayMetrics dm = new DisplayMetrics();
		dm = contexts.getResources().getDisplayMetrics();
		Paint pFont = new Paint();
		pFont.setTextSize(TEXT_SIZE);
		int txtWidth = (int) pFont.measureText(str);
		if (txtWidth > dm.widthPixels - TEXT_GAP) {
			return dm.widthPixels - TEXT_GAP;
		}
		return txtWidth;
	}

	private static final String TAG = "StringFormatters";
	/**
	 * 指定日格式为"yyyy-MM-dd"
	 */
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 取得相对时间,如:14小时前
	 * 
	 * @param created
	 * @return
	 */
	public static CharSequence getRelativeTimeSpanString(String created) {
		try {
			return DateUtils.getRelativeTimeSpanString(DATE_FORMAT.parse(created).getTime(), new Date().getTime(),
					DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
		} catch (ParseException e) {
			return created;
		}
	}

	/**
	 * 取得年月日字符串2010-08-24
	 * 
	 * @param created
	 * @return
	 */
	public static String getTimeString(String created) {
		try {
			return DATE_FORMAT.format(DATE_FORMAT.parse(created));
		} catch (ParseException e) {
			LOGGER.e(TAG, "getTimeString err.", e);
		}
		return "";
	}

	/**
	 * 取得现在时间的年月日字符串 2010-08-24
	 * 
	 * @return 现在时间的年月日字符串 2010-08-24
	 */
	public static String getNowTimeString() {
		Date nowDate = new Date();
		return DATE_FORMAT.format(nowDate);
	}

	/**
	 * 通过正则表达式验证文本内容
	 * 
	 * @param regex
	 *            正则表达式
	 * @param src
	 *            验证的文本内容
	 * @return true验证通过
	 */
	public static boolean isValidate(String regex, String src) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(src);
		boolean isValidate = matcher.matches();
		return isValidate;
	}

	public static Toast toast;// 显示信息
	private static FileInputStream is = null;
	private static BufferedInputStream bis = null;
	private static byte[] imageBuf = null;
	private final static int PICTURE_BYTES_SIZE = 1024 * 100;
	private final static int PICTURE_BYTES_SIZE_BIGGER = 1024 * 400;
	private final static int PICTURE_HEIGHT = 190;
	private static String mPictureFileType = "";
	private static int mPictureBytesSize = 0;
	private static String mPictureFileSize = "";
	private final String mPictureFileName = "";
	private final Bitmap mPreview_Image = null;// 导入图片的缩略图
	/**
	 * 图片压缩
	 */
	public static HashMap<Integer, String> mapCompress = new HashMap<Integer, String>();

	/**
	 * 把一个byte转化成16进制的字符串
	 * 
	 * @param by
	 *            需要转化的byte
	 * @return 转化成16进制的字符串
	 */
	public static String toHexString(byte by) {
		try {
			return Integer.toHexString((0x000000ff & by) | 0xffffff00).substring(6);
		} catch (Exception e) {
			PrintStackTrace("Utility", e);
			return null;
		}
	}

	/**
	 * 将char的数据转换为utf-8编码的String
	 * 
	 * @param ch
	 *            需要转换的char
	 * @return utf-8编码的String
	 */
	public static String toUTF8(char ch) {
		StringBuffer buffer = new StringBuffer();

		if (ch < 0x80) {
			buffer.append(ch);
		} else if (ch < '\u0800') {
			buffer.append("%" + toHexString((byte) (0xc0 | (ch >> 6))));
			buffer.append("%" + toHexString((byte) (0x80 | (ch & 0x3f))));
		} else {
			buffer.append("%" + toHexString((byte) (0xe0 | (ch >> 12))));
			buffer.append("%" + toHexString((byte) (0x80 | ((ch >> 6) & 0x3f))));
			buffer.append("%" + toHexString((byte) (0x80 | (ch & 0x3f))));
		}

		return buffer.toString();
	}

	/**
	 * 将字符串以URL编码 例如空格就加"%20"
	 * 
	 * @param sUrl
	 *            字符串
	 * @return 编码的字符串
	 */
	public static String urlEncode(String sUrl) {
		if (sUrl == null)
			return null;

		StringBuffer StrUrl = new StringBuffer();
		for (int i = 0; i < sUrl.length(); ++i) {
			char c = sUrl.charAt(i);
			switch (c) {
			case ' ':
				StrUrl.append("%20");
				break;
			case '+':
				StrUrl.append("%2b");
				break;
			case '\'':
				StrUrl.append("%27");
				break;
			case '/':
				StrUrl.append("%2F");
				break;
			case '.':
				StrUrl.append("%2E");
				break;
			case '<':
				StrUrl.append("%3c");
				break;
			case '>':
				StrUrl.append("%3e");
				break;
			case '#':
				StrUrl.append("%23");
				break;
			case '%':
				StrUrl.append("%25");
				break;
			case '&':
				StrUrl.append("%26");
				break;
			case '@':
				StrUrl.append("%40");
				break;
			case '{':
				StrUrl.append("%7b");
				break;
			case '}':
				StrUrl.append("%7d");
				break;
			case '\\':
				StrUrl.append("%5c");
				break;
			case '^':
				StrUrl.append("%5e");
				break;
			case '~':
				StrUrl.append("%73");
				break;
			case '[':
				StrUrl.append("%5b");
				break;
			case ']':
				StrUrl.append("%5d");
				break;
			default:

				if (c < 128)
					StrUrl.append(sUrl.charAt(i));
				else {
					StrUrl.append(toUTF8(c));
				}
				break;
			}
		}
		return StrUrl.toString();
	}

	/**
	 * 对字符串进行URL解码。
	 * 
	 * @param sUrl
	 *            字符串
	 * @return 已解码的字符串
	 */
	static public String urlDecode(String sUrl) {
		if (sUrl == null)
			return null;
		StringBuffer StrUrl = new StringBuffer();
		for (int i = 0; i < sUrl.length(); ++i) {
			char c = sUrl.charAt(i);
			if (c == '%') {
				i++;
				char t = 0;
				if (i < sUrl.length()) {
					t = (char) (getHexValue(sUrl.charAt(i)) << 4);
					i++;
					t += getHexValue(sUrl.charAt(i));
				}
				c = t;
			}
			StrUrl.append(c);
		}
		return StrUrl.toString();
	}

	/**
	 * 将char转化为十六进制
	 * 
	 * @param c
	 *            需要转换的char
	 * @return 转换后的值
	 */
	static private int getHexValue(char c) {
		if (c >= '0' && c <= '9')
			return c - '0';
		else if (c >= 'a' && c <= 'f')
			return c - 'a' + 10;
		else if (c >= 'A' && c <= 'F')
			return c - 'A' + 10;
		else
			return 0;
	}

	/**
	 * 分割字符串 如 original="abc", regex "b" 结果 是"a","b"
	 * 
	 * @param original
	 *            要分割的字符串
	 * @param regex
	 *            以什么字符串分割
	 * @return 分割后的字符串数组
	 */
	public static String[] stringSplit(String original, String regex) {
		int startIndex = 0;
		Vector<String> v = new Vector<String>();
		String[] str = null;
		int index = 0;

		startIndex = original.indexOf(regex);
		while (startIndex < original.length() && startIndex != -1) {
			String temp = original.substring(index, startIndex);
			v.addElement(temp);
			index = startIndex + regex.length();
			startIndex = original.indexOf(regex, startIndex + regex.length());
		}

		v.addElement(original.substring(index + 1 - regex.length()));
		str = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			str[i] = v.elementAt(i);
		}

		return str;
	}

	/**
	 * 得到网址域名
	 * 
	 * @param URL
	 *            如"http://mail.google.com/"
	 * @return 网址域名 ,如mail.google.com
	 */
	public static String parseURLDomain(String URL) {
		String re = "";
		final String http = "http://";
		String[] url;

		if (URL.startsWith(http)) {
			url = stringSplit(URL.substring(http.length(), URL.length()), "/");
		} else {
			url = stringSplit(URL, "/");
		}
		re = url[0];
		return re;
	}

	/**
	 * 截取网址的路径名 如 URL http://www.cnblogs.com/JimmyZhang/archive/2335272.html
	 * 截取后得到 JimmyZhang/archive/2335272.html
	 * 
	 * @param URL
	 *            如http://www.cnblogs.com/JimmyZhang/archive/2335272.html
	 * @return 如JimmyZhang/archive/2335272.html
	 */
	public static String parseURLPath(String URL) {
		String re = "";
		final String http = "http://";
		int idx = 0;

		if (URL.startsWith(http)) {
			idx = http.length() + URL.substring(http.length(), URL.length()).indexOf('/');
		} else {
			idx = URL.indexOf('/');
		}
		re = URL.substring(idx + 1, URL.length());
		return re;
	}

	/**
	 * 转换InputStream为String
	 * 
	 * @param is
	 *            输入流
	 * @return 输出的String
	 */
	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			PrintStackTrace("Utility", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				PrintStackTrace("Utility", e);
			}
		}
		return sb.toString();
	}

	/**
	 * 生成边界线，在写注释的时候可以用到
	 * 
	 * @return 返回边界线如 "---------------------c5e3436e99fd6115"
	 */
	public static String generateBoundary() {
		Random rand = new Random();
		int i = rand.nextInt();
		return "---------------------" + Integer.toHexString(i) + Integer.toHexString(rand.nextInt());
	}

	private static void PrintStackTrace(String tag, Throwable e) {
		// Logger.e(tag, e.getClass().getName() + ":" + e.getMessage());
		StackTraceElement[] el = e.getStackTrace();
		for (int i = 0; i < el.length; i++) {
			// Logger.e(tag, "  at " + el[i].getMethodName()
			// + "(" + el[i].getFileName() + ":" + el[i].getLineNumber() + ")");
		}
	}

	/**
	 * 抛出异常
	 * 
	 * @param tag
	 */
	public static void PrintStackTrace(String tag) {
		try {
			throw new Exception("for debug");
		} catch (Exception e) {
			PrintStackTrace(tag, e);
		}
	}

	/**
	 * 弹出toast
	 * 
	 * @param context
	 * @param s
	 *            显示的字符串
	 */
	public static void toast(Context context, String s) {
		if (s != null && s.length() > 0) {
			if (toast == null)
				toast = Toast.makeText(context, s, 0);
			else
				toast.setText(s);
			toast.show();
		}
	}

	/**
	 * 长时间弹出toast
	 * 
	 * @param context
	 * @param s
	 *            显示的字符串
	 */
	public static void toastLong(Context context, String s) {
		if (s != null && s.length() > 0) {
			if (toast == null)
				toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
			else
				toast.setText(s);
			toast.show();
		}
	}

	/**
	 * 验证检查字符串是不是电话号码
	 * 
	 * @param s
	 *            要检查的字符串
	 * @return 是电话号码 返回true
	 */
	public static boolean checkInputPhoneNumber(String s) {
		return Pattern
				.compile(
						"(^1[3458]\\d{9}$|^(0\\d{2,4}-)?[2-9]\\d{6,7}(-\\d{2,5})?$|^(?!\\d+(-\\d+){3,})[48]00(-?\\d){7,10}$)")
				.matcher(s).matches();
	}

	/**
	 * 验证检查字符串是不是ip
	 * 
	 * @param s
	 *            要检查的字符串
	 * @return 是ip 返回true
	 */
	public static boolean ipValid(String s) {
		String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
		String regex1 = "1\\d{2}";
		String regex2 = "[1-9]\\d";
		String regex3 = "\\d";
		String regex = "(" + regex0 + ")|(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")";
		regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex + ")";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		return m.matches();
	}

	/**
	 * 验证检查字符串是不是id
	 * 
	 * @param s
	 *            要检查的字符串
	 * @return 是id 返回true
	 */
	public static boolean idValid(String s) {
		String idRex = "[\\d]{6}(19|20)*[\\d]{2}((0[1-9])|(11|12))([012][\\d]|(30|31))[\\d]{3}[xX\\d]*";
		if (!StringUtils.isEmpty(s) && StringUtils.isValidate(idRex, s)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到xml节点的值
	 * 
	 * @param node
	 *            要得到xml节点的值的节点
	 * @param def
	 *            默认输出的值
	 * @return if(node.hasChildNodes()) node.getFirstChild().getNodeValue() 否则输出
	 *         def
	 */
	public static String GetXMLNodeValue(Element node, String def) {
		if (node.hasChildNodes())
			return node.getFirstChild().getNodeValue();
		else
			return def;
	}

	/**
	 * 通过tag得到xml节点的值
	 * 
	 * @param node
	 *            要得到xml节点的值的节点
	 * @param tag
	 *            The name of the tag to match on. The special value "*" matches
	 *            all tags
	 * @param def
	 *            默认输出的值
	 * @return
	 */
	public static String GetXMLChildNodeValueByTag(Element node, String tag, String def) {
		String re = def;
		NodeList nl = node.getElementsByTagName(tag);
		if (nl.getLength() != 0) {
			if (nl.item(0).hasChildNodes())
				re = nl.item(0).getFirstChild().getNodeValue();
		}
		return re;
	}

	/**
	 * 得到随机数
	 * 
	 * @return 返回随机数
	 */
	public static int getRandom() {
		Random random = new Random();
		return random.nextInt();
	}

	/**
	 * 获取随机数
	 * 
	 * @param seed
	 * @return
	 */
	public static String getRandom(int seed) {
		Random random = new Random();
		return String.valueOf(random.nextInt(seed));
	}

	/**
	 * 判断字符串是否以"n_"开头
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 如果是以"n_"开头，则返回true
	 */
	public static boolean isPostImageSuccess(String str) {
		if (str != null && !str.equals("")) {
			return str.startsWith("n_");
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * Methods Name: getSubString
	 * @author Bear
	 * Description:截截取字符
	 * @param str
	 * @param end
	 * @return
	 * Comments:
	 */
	public static String getSubString(String str, int end) {
		if (str.length()< end) {
			return str;
		}
		String string = str;
		string = string.substring(0, end);
 		return string;
	}

}
