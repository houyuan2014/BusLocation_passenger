package cn.cas.sict.BusLocation_passenger;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import cn.cas.sict.utils.HttpUtil;
import cn.cas.sict.utils.SPUtil;
import cn.cas.sict.utils.ToastUtil;

import com.amap.api.location.LocationManagerProxy;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

public class MyService extends Service {

	Vibrator vibrator;
	boolean hasVibrate;
	SharedPreferences sP;
	SharedPreferences.Editor editor;
	float remindDistance, distance;
	int routeNum;
	Double userLat, userLng, busLat, busLng;
	LatLng userLL, busLL;
	Map<String, Object> mapRoute;
	Timer timer;; // 定义定时器、定时器任务及Handler句柄
	MyTimerTask timerTask;
	MyHandler handler;
	BroadcastReceiver receiver; //接收ui线程发来的消息
	Intent in = new Intent("aaa");//向ui线程发送消息

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(3);
		}
	};

	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			if (msg.what == 3) { // 执行定时任务
				mapRoute = new HashMap<String, Object>();
				mapRoute.put("route", routeNum + "");
				try {
					JSONObject jsonObj = new JSONObject(HttpUtil.post("url",
							mapRoute));
					if (jsonObj.get("flag").equals("true")) {
						busLat = Double.parseDouble(jsonObj.getString("lat"));
						busLng = Double.parseDouble(jsonObj.getString("lng"));
						in.putExtra("buslat", busLat);
						in.putExtra("buslng", busLng);
						System.out.println(in.toString());
						sendBroadcast(in);
						if (!hasVibrate && userLL != null && busLL != null) {
							distance = AMapUtils.calculateLineDistance(userLL,
									busLL);
							in.putExtra("currentdistance", distance);
							sendBroadcast(in);
							if (distance <= remindDistance) {
								hasVibrate = true;
								vibrator.vibrate(new long[] { 10, 600, 60, 600,
										60, 600 }, -1);
								editor.putBoolean("hasvibrate", hasVibrate)
										.commit();
							}
						}
						Log.i("test", "receivejson   " + jsonObj.toString());
					} else {
						ToastUtil.show(getApplicationContext(), "班车位置不可用,重试中");
						// timerTask.cancel();
						// timer.purge();
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	};

	// Service被创建时回调该方法。
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		timer = new Timer();
		handler = new MyHandler();
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		sP = getSharedPreferences("userconfig", Context.MODE_PRIVATE);
		editor = sP.edit();
		hasVibrate = sP.getBoolean(SPUtil.SP_HASVIBRATE, false);
		remindDistance = sP.getFloat(SPUtil.SP_REMINDDISTANCE, 1000);// 临近距离
		routeNum = sP.getInt(SPUtil.SP_ROUTENUM, 1);
		distance = -1;

		IntentFilter filter = new IntentFilter();
		filter.addAction("bbb");
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				userLat = arg1.getDoubleExtra("userlat", -1);
				userLng = arg1.getDoubleExtra("userlng", -1);
				userLL = new LatLng(userLat, userLng);
				System.out.println("bbb 设置用户坐标"+arg1.toString()+userLL.toString());
			}
		};
		registerReceiver(receiver, filter);

		System.out.println("Service is Created");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("--service onstart");
		startMyTimer();

//		Intent in = new Intent("aaa");
//		in.putExtra("count", 1);
//		sendBroadcast(in);

		return super.onStartCommand(intent, flags, startId);
	}

	private void startMyTimer() {
		// TODO Auto-generated method stub
		if (timer != null) {
			if (timerTask != null) {
				timerTask.cancel(); // 将原任务从队列中删除
			}
		}
		/**
		 * 注意： 每次放任务都要新建一个任务对象，否则出现一下错误： ERROR/AndroidRuntime(11761):
		 * java.lang.IllegalStateException: TimerTask is scheduled already
		 * 所以同一个定时器任务只能被放置一次
		 */
		timerTask = new MyTimerTask(); // 新建一个任务（必须）
		timer.scheduleAtFixedRate(timerTask, 500, 5000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		vibrator.cancel();
		timerTask.cancel();
		timer.purge();
		unregisterReceiver(receiver);
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
