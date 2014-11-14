package com.zhijian.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.zhijian.BaseApplication;
import com.zhijian.headsetkey.R;
import com.zhijian.service.MediaButtonService;
import com.zhijian.utils.HeadSetControl;
import com.zhijian.utils.Utils;

/**
 * 弹出dialog，实为Activity
 * @author Administrator
 *
 */
public class ZhiJianActivity extends Activity implements OnClickListener {
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			/**
			 * 2000毫秒过后关闭耳机模式，注：关闭耳机模式与弹出dialog选择框不相关联， 在2000毫秒内用户仍然可以选择耳机或智键
			 */
			if (msg.what == 1) {
				BaseApplication.getInstance().setIgnoreFlag(true);
				HeadSetControl.closeHeadSetmode();
			}
		};
	};
	private Button headSet, cancle;
	public static ZhiJianActivity zhijianInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.headset_layout);
		BaseApplication.getInstance().addActivity(this);
		initView();
		Message msg = new Message();
		msg.what = 1;
		handler.sendMessageDelayed(msg, 2000);
	}

	private void initView() {
		zhijianInstance = this;
		headSet = (Button) findViewById(R.id.head_set);
		cancle = (Button) findViewById(R.id.head_cancle);
		headSet.setOnClickListener(this);
		cancle.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_set:
			/**
			 * 选择智键，1.关闭耳机模式；
			 * 2.在智键没有拔出前不在开启2000毫秒关闭耳机模式；
			 * 3.启动service监听智键按钮。
			 */
			Utils.saveBoolean(this, "head_on", true);
			HeadSetControl.closeHeadSetmode();
			if (!BaseApplication.getInstance().isServiceWorked()) {
				Log.i("TAG", "----->>>onClick");
				startService(new Intent(ZhiJianActivity.this, MediaButtonService.class));
			}
			this.finish();
			break;
		case R.id.head_cancle:
			/**
			 * 选择耳机，1.打开耳机模式；
			 * 2.在智键没有拔出前有第二次开启2000毫秒关闭耳机模式；
			 */
			Utils.saveBoolean(this, "head_on", false);
			HeadSetControl.openHeadSetmode();
			BaseApplication.getInstance().setIgnoreFlag(true);
			this.finish();
			handler.removeCallbacksAndMessages(null);
			break;
		default:
			break;
		}
	}
}
