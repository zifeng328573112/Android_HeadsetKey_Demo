package com.zhijian.utils;

import android.content.Context;

/**
 * 记忆dialog第一次弹出，之后非物理拔出就不弹出
 * @author Administrator
 *
 */
public class Utils {

	public static void saveBoolean(Context context, String strName, boolean booleanValue) {
		context.getSharedPreferences("HeadKey", 0).edit().putBoolean(strName, booleanValue).commit();
	}

	public static boolean getValue(Context context, String strName, boolean booleanDefault) {

		return context.getSharedPreferences("HeadKey", 0).getBoolean(strName, booleanDefault);
	}

}
