package com.zhijian.brocast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

import com.zhijian.utils.KeyManager;
import com.zhijian.utils.Utils;

/**
 * 监听智键按钮点击次数
 * @author Administrator
 *
 */
public class MediaButtonIntentReceiver extends BroadcastReceiver {

	KeyManager keyManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		String intentAction = intent.getAction();
		if (Utils.getValue(context, "head_on", false)) {
			keyManager = KeyManager.getInstance(context);
			if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
				KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
				if (event == null) {
					return;
				}

				int keycode = event.getKeyCode();
				int action = event.getAction();

				switch (keycode) {
				case KeyEvent.KEYCODE_HEADSETHOOK:
				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
					if (action == KeyEvent.ACTION_UP) {
						Toast.makeText(context, "KeyEvent.ACTION_UP", Toast.LENGTH_SHORT).show();
						keyManager.handler.removeCallbacks(keyManager.runnable);
						long time = System.currentTimeMillis();
						if (keyManager.lastUpTime == 0) {
							keyManager.lastUpTime = time;
						}
						if (time - keyManager.lastUpTime < 600) {
							keyManager.count += 1;
							keyManager.lastUpTime = time;
						}
						keyManager.handler.postDelayed(keyManager.runnable, 800);
					}
					break;
				}
			}
		}
	}
}
