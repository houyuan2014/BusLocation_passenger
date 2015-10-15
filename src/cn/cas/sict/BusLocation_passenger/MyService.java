package cn.cas.sict.BusLocation_passenger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class MyService extends Service {

	int count;
	boolean flag;

	// Service被创建时回调该方法。
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		count = 0;
		flag = false;
		System.out.println("Service is Created");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("--service onstart");
		new Thread() {

			public void run() {
				while (!flag) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					count++;
					System.out.println("" + count);
					Intent in = new Intent("aaa");
					in.putExtra("count", count);
					sendBroadcast(in);
				}
			}
		}.start();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = true;
		System.out.println("Service is Destroyed");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// /**
	// * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
	// */
	// private void acquireWakeLock() {
	// if (null == wakeLock) {
	// PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	// wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "my tag");
	// Log.i("wl", "call acquireWakeLock");
	// wakeLock.acquire();
	// } else {
	// Log.i("wl", "call acquireWakeLock");
	// wakeLock.acquire();
	// }
	//
	// }
	//
	// // 释放设备电源锁
	// private void releaseWakeLock() {
	// if (null != wakeLock && wakeLock.isHeld()) {
	// Log.i("wl", "call releaseWakeLock");
	// wakeLock.release();
	// wakeLock = null;
	// }
	// }
}
