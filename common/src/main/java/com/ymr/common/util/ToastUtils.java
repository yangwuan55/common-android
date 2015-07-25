package com.ymr.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * 
 * @author zhangfm
 * 
 */
public final class ToastUtils {
	private static Toast mToast;

	/**
	 * 根据字符串弹Toast通知
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, 0); // 不能使用new Toast的方式
		}
		mToast.setText(msg);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}


	/**
	 * 根据 R.String中的resId弹出Toast通知
	 * 
	 * @param context
	 * @param resId
	 */
	public static void showToast(Context context, int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(context, resId, 0); // 不能使用new Toast的方式
		}
		mToast.setText(resId);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}

	/**
	 * cancel掉Toast
	 */
	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}

	}

}
