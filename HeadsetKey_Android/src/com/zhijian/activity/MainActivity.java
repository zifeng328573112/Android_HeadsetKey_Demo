package com.zhijian.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhijian.BaseApplication;
import com.zhijian.brocast.HeadsetPlugReceiver;
import com.zhijian.headsetkey.R;
import com.zhijian.service.MediaButtonService;

/**
 * 首页操作
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends Activity {

	private LinearLayout[] layouts = new LinearLayout[4];
	private int[] layoutIds = { R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4 };
	private HeadsetPlugReceiver headsetPlugReceiver;
	private long exitTime = 0;
	private Context context;
	private BaseApplication mpp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mpp = BaseApplication.getInstance();
		initView();
		if (!mpp.isServiceWorked()) {
			Log.i("TAG", "----->>>onCreate");
			startService(new Intent(context, MediaButtonService.class));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Window win = this.getWindow();
		WindowManager.LayoutParams params = win.getAttributes();
		params.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
		if (!mpp.isServiceWorked()) {
			Log.i("TAG", "----->>>onResume");
			startService(new Intent(context, MediaButtonService.class));
		}
	}

	void initView() {
		context = this;
		for (int i = 0; i > layouts.length; i++) {
			layouts[i] = (LinearLayout) findViewById(layoutIds[i]);
		}
	}

	private void registerHeadsetPlugReceiver() {
		headsetPlugReceiver = new HeadsetPlugReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
		registerReceiver(headsetPlugReceiver, intentFilter);
	}

	@SuppressWarnings("deprecation")
	public static void setTitleVip(Context context) {

		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.ic_launcher, "(๑´ㅂ`๑)", System.currentTimeMillis());
		n.flags = Notification.FLAG_NO_CLEAR;
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		n.setLatestEventInfo(context, "测试", "这是一个测试代码", contentIntent);
		nm.notify(R.string.app_name, n);
		context.startService(new Intent(context, MediaButtonService.class));

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				exitTime = System.currentTimeMillis();
				Toast.makeText(context, "再按就退出", Toast.LENGTH_LONG).show();
			} else {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
			}
			return true;
		}
		return false;
	}

}
