package com.ymr.common.util;

/**
 * 日志工具类
 * 
 * 
 */
public class LogUtil {
	/**
	 * 生成日志TAG
	 */
	public static String makeLogTag(Class cls) {
		return cls.getSimpleName();
	}
}
