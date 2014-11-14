package com.zhijian;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zhijian.activity.ZhiJianActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * 全局定义
 * @author Administrator
 *
 */
public class BaseApplication extends Application {

	private boolean ignoreBroadcast = false;
	public void setIgnoreFlag(boolean ignoreBroadcast){
		this.ignoreBroadcast = ignoreBroadcast ;
	}
	
	public boolean getIgnoreFlag(){
		return ignoreBroadcast;
	}
	private boolean isOn = false;
	private static BaseApplication instance;
	private List<Activity> activityList = new LinkedList<Activity>();

	public static BaseApplication getInstance() {
		return instance;
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public void startZhiJianActivity() {
		Intent intent = new Intent(instance, ZhiJianActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		BaseApplication.getInstance().startActivity(intent);
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void finishZhiJianActivity() {
		Log.i("TAG", "----->>>" + activityList);
		if (activityList.size() > 0) {
			for (Activity activity : activityList) {
				activity.finish();
			}
		}
	}
	
	public boolean isServiceWorked() {
		ActivityManager myManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(200);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString().equals("com.ljk.service.MediaButtonService")) {
				return true;
			}
		}
		return false;
	}
	
	
}
