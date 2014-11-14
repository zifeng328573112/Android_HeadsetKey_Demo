package com.zhijian.service;

import com.zhijian.brocast.HeadsetPlugReceiver;
import com.zhijian.brocast.MediaButtonIntentReceiver;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;

/**
 * 管理注册广播接收器的 服务
 * 
 * @author Administrator
 * 
 */
public class MediaButtonService extends Service {
	private String TAG = "myHeadsetKey";
	private AudioManager mAudioManager;
	private ComponentName rec;
	private HeadsetPlugReceiver headsetPlugReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 注册唯一 置顶广播，用于监听按钮事件
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		rec = new ComponentName(getPackageName(), MediaButtonIntentReceiver.class.getName());
		mAudioManager.registerMediaButtonEventReceiver(rec);
		// 注册耳机插拔广播
		registerHeadsetPlugReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 动态注册广播,并设为最高优先级
	 */
	private void registerHeadsetPlugReceiver() {
		headsetPlugReceiver = new HeadsetPlugReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
		intentFilter.setPriority(2147483647);
		registerReceiver(headsetPlugReceiver, intentFilter);
	}
}
