package com.zhijian.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.provider.Settings;

/**
 * 设置智键按钮操作
 * @author Administrator
 *
 */
public class KeyManager {
	private static volatile KeyManager keyManager;

	public long lastUpTime = 0;
	public int count = 0;
	Context context;
	public Handler handler = new Handler() {
	};
	public Runnable runnable = new Runnable() {
		public void run() {
			doWay(count);
			count = 0;
			lastUpTime = 0;
		}
	};

	private KeyManager(Context context) {
		this.context = context;
	}

	private void doWay(int i) {
		switch (i) {
		case 1:
			Uri uri = Uri.parse("http://www.baidu.com");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			break;
		case 2:
			Intent intent2 = new Intent();
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2.setAction(Intent.ACTION_VIEW);
			intent2.setData(Contacts.People.CONTENT_URI);
			context.startActivity(intent2);
			break;
		case 3:
			Intent inttPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			inttPhoto.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(inttPhoto);
			break;
		case 4:
			Intent setIntent = new Intent(Settings.ACTION_SETTINGS);
			setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(setIntent);
			break;
		}
	}

	public static synchronized KeyManager getInstance(Context context) {
		if (keyManager == null) {
			keyManager = new KeyManager(context);
		}
		return keyManager;
	}

}
