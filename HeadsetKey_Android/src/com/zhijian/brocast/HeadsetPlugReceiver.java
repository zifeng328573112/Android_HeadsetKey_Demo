package com.zhijian.brocast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zhijian.BaseApplication;
import com.zhijian.utils.KeyManager;
import com.zhijian.utils.Utils;

/**
 * 接收耳机插入广播
 * 
 * @author Administrator
 * 
 */
public class HeadsetPlugReceiver extends BroadcastReceiver {
	KeyManager keyManager;
	private Context context;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.i("--------->>>", "message发送过来的信息：" + msg.what);
			if (msg.what == 0) {
				Log.i("TAG", "----->>>" + "拔出耳机");
				if (BaseApplication.getInstance().getIgnoreFlag() == false) {
					BaseApplication.getInstance().finishZhiJianActivity();
				} else {
					BaseApplication.getInstance().setIgnoreFlag(false);
				}
			} else if (msg.what == 1) {
				Log.i("TAG", "----->>>" + "插入耳机");
				// 如果物理插入耳机弹出dialog,并记忆当前为物理插入让耳机图标显示2000毫秒
				if (BaseApplication.getInstance().getIgnoreFlag() == false) {
					Utils.saveBoolean(BaseApplication.getInstance(), "head_on", false);
					BaseApplication.getInstance().startZhiJianActivity();
				} else {
					BaseApplication.getInstance().setIgnoreFlag(false);
				}
			}
		}
	};

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if (intent.hasExtra("state")) {
			if (intent.getIntExtra("state", 0) == 0) {// 拔出
				handler.sendEmptyMessage(0);
			} else if (intent.getIntExtra("state", 0) == 1) {// 插入
				handler.sendEmptyMessage(1);
			}
		}

	}

}
