package cn.cas.sict.service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import cn.cas.sict.domain.User;
import cn.cas.sict.utils.HttpUtil;
import cn.cas.sict.utils.Globals;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

public class MyService extends Service implements AMapLocationListener {
	LocationManagerProxy lMP;
	Vibrator vibrator;
	boolean hasVibrate = false;
	float distance = -1;
	double userLat, userLng, busLat, busLng;
	LatLng userLL, busLL;
	Map<String, Object> mapRoute;
	Timer timer = new Timer(); // 定义定时器、定时器任务及Handler句柄
	MyTimerTask timerTask;
	MyHandler handler = new MyHandler();
	BroadcastReceiver receiver;
	User user;

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter(Globals.BROADCASTTOSERVICE);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (arg1.getIntExtra("flag", -1)) {

				case Globals.GETSERVICEINFO:
					if (userLL != null) {
						Intent in4 = new Intent(Globals.BROADCASTTOUI);
						in4.putExtra("flag", Globals.USERFLAG);
						in4.putExtra("lat", userLat);
						in4.putExtra("lng", userLng);
						sendBroadcast(in4);
					}
					if (busLL != null) {
						Intent in5 = new Intent(Globals.BROADCASTTOUI);
						in5.putExtra("flag", Globals.BUSFLAG);
						in5.putExtra("lat", busLat);
						in5.putExtra("lng", busLng);
						sendBroadcast(in5);
					}
					break;

				}
			}
		};
		registerReceiver(receiver, filter);
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

		lMP = LocationManagerProxy.getInstance(this);
		lMP.requestLocationData(LocationProviderProxy.AMapNetwork, 10 * 1000,
				10, this);
		System.out.println("service is created");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// start timer
		user = User.getUser();
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
		timer.scheduleAtFixedRate(timerTask, 0, 3000);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		vibrator.cancel();
		timerTask.cancel();
		timer.purge();
		lMP.removeUpdates(this);
		lMP.destroy();
		unregisterReceiver(receiver);
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			handler.sendEmptyMessage(3);
		}
	};

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			if (msg.what == 3) { // 执行定时任务

				try {
					String url = "GetLocationSrevlet";
					JSONObject json = new JSONObject();
					json.put("route", user.getRouteNum());
					String str = HttpUtil.sendPost(url, json);

					if (str == null) {
						System.out.println("result = null");
						return;
					}

					JSONObject result = new JSONObject(str);
					if (result.get("flag").equals("true")) {
						busLat = Double.parseDouble(result.getString("lat"));
						busLng = Double.parseDouble(result.getString("lng"));
						busLL = new LatLng(busLat, busLng);
						String busLoc = result.getString("desc");
						// 向ui线程发送消息
						Intent in0 = new Intent(Globals.BROADCASTTOUI);
						in0.putExtra("flag", Globals.BUSFLAG);
						in0.putExtra("lat", busLat);
						in0.putExtra("lng", busLng);
						in0.putExtra("busdesc", busLoc);
						sendBroadcast(in0);
						// 计算与班车距离
						if (userLL != null && busLL != null) {
							distance = AMapUtils.calculateLineDistance(userLL,
									busLL);
							Intent in1 = new Intent(Globals.BROADCASTTOUI);
							in1.putExtra("flag", Globals.DISTANCEFLAG);
							in1.putExtra("currentdistance", distance);
							sendBroadcast(in1);
							// 震动提醒
							if (user.isRemind() && !hasVibrate
									&& distance < user.getRemindDistance()) {
								vibrator.vibrate(new long[] { 10, 600, 60, 600,
										60, 600 }, -1);
								hasVibrate = true;
							}
						}
					} else {
						Intent in2 = new Intent(Globals.BROADCASTTOUI);
						in2.putExtra("flag", Globals.BUSDISABLE);
						sendBroadcast(in2);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	};

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		if (arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
			userLat = arg0.getLatitude();
			userLng = arg0.getLongitude();
			userLL = new LatLng(userLat, userLng);
			Intent in3 = new Intent(Globals.BROADCASTTOUI);
			in3.putExtra("flag", Globals.USERFLAG);
			in3.putExtra("lat", userLat);
			in3.putExtra("lng", userLng);
			sendBroadcast(in3);
		} else {
			Log.e("AmapErr", "Location ERR:"
					+ arg0.getAMapException().getErrorCode());
		}
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
