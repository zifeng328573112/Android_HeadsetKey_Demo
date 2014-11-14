package com.zhijian.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.zhijian.BaseApplication;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * 核心代码：setWiredDeviceConnectionState, 模拟耳机拔出和插入
 * 
 * @author Administrator
 * 
 */
public class HeadSetControl {
	public static boolean isOpen = false;

	/**
	 * 拔出
	 */
	public static void closeHeadSetmode() {
		setWiredDeviceConnectionStateX(BaseApplication.getInstance(), 4, 0, "headset-zhijian-artificial");
	}

	/**
	 * 插入
	 */
	public static void openHeadSetmode() {
		setWiredDeviceConnectionStateX(BaseApplication.getInstance(), 8, 1, "headset-zhijian-artificial");
	}

	/**
	 * 
	 * @param paramContext
	 *            上下文
	 * @param paramInt1
	 * @param paramInt2
	 *            给系统发送拔出或插入广播（0是拔出耳机，1是插入耳机）
	 * @param paramString
	 *            智键名称
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static boolean setWiredDeviceConnectionStateX(Context paramContext, int paramInt1, int paramInt2, String paramString) {
		boolean bool;
		try {
			// 系统多媒体广播
			Class localClass2 = Class.forName("android.media.AudioManager");
			Class[] arrayOfClass2 = new Class[1];
			arrayOfClass2[0] = Context.class;
			Constructor localConstructor = localClass2.getConstructor(arrayOfClass2);
			Class[] arrayOfClass3 = new Class[3];
			arrayOfClass3[0] = Integer.TYPE;
			arrayOfClass3[1] = Integer.TYPE;
			arrayOfClass3[2] = String.class;
			// 映射系统广播，代替系统发送一个插入（拔出）智键的广播
			Method localMethod2 = localClass2.getMethod("setWiredDeviceConnectionState", arrayOfClass3);
			Object[] arrayOfObject2 = new Object[1];
			arrayOfObject2[0] = paramContext;
			Object localObject = localConstructor.newInstance(arrayOfObject2);
			Object[] arrayOfObject3 = new Object[3];
			arrayOfObject3[0] = Integer.valueOf(paramInt1);
			arrayOfObject3[1] = Integer.valueOf(paramInt2);
			arrayOfObject3[2] = paramString;
			localMethod2.invoke(localObject, arrayOfObject3);
			bool = true;
		} catch (Exception localException1) {
			Log.e("setWiredDeviceConnectionStateX 1", localException1.getMessage());
			// 如果选择智键，就发送一个拔出耳机的广播
			Intent localIntent = new Intent("android.intent.action.HEADSET_PLUG");
			localIntent.addFlags(1073741824);// 优先级设为最高
			localIntent.putExtra("state", paramInt2);// 非物理拔出，发送0
			localIntent.putExtra("name", paramString);// 识别智键或耳机，传一个name就行了
			localIntent.putExtra("microphone", 1);// 扬声器模式
			try {
				Class localClass1 = Class.forName("android.app.ActivityManagerNative");
				Class[] arrayOfClass1 = new Class[2];
				arrayOfClass1[0] = Intent.class;
				arrayOfClass1[1] = String.class;
				Method localMethod1 = localClass1.getMethod("broadcastStickyIntent", arrayOfClass1);
				Object[] arrayOfObject1 = new Object[2];
				arrayOfObject1[0] = localIntent;
				localMethod1.invoke(null, arrayOfObject1);
				bool = true;
			} catch (Exception localException2) {
				Log.e("setWiredDeviceConnectionStateX 2", localException2.getMessage());
				bool = false;
			}
		}
		return bool;
	}
}
