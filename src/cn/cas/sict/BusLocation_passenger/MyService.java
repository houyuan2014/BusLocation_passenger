package cn.cas.sict.BusLocation_passenger;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import cn.cas.sict.utils.HttpUtil;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

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
		IntentFilter filter = new IntentFilter(Values.BROADCASTTOSERVICE);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (arg1.getIntExtra("flag", -1)) {

				case Values.GETSERVICEINFO:
					if (userLL != null) {
						Intent in4 = new Intent(Values.BROADCASTTOUI);
						in4.putExtra("flag", Values.USERFLAG);
						in4.putExtra("lat", userLat);
						in4.putExtra("lng", userLng);
						sendBroadcast(in4);
					}
					if (busLL != null) {
						Intent in5 = new Intent(Values.BROADCASTTOUI);
						in5.putExtra("flag", Values.BUSFLAG);
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
		user = (User) intent.getSerializableExtra("user");
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
		timer.scheduleAtFixedRate(timerTask, 0, 6000);

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

	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			if (msg.what == 3) { // 执行定时任务
				
				System.out.println(user.toString());
				
				mapRoute = new HashMap<String, Object>();
				mapRoute.put("route", user.getRouteNum() + "");
				try {
					JSONObject jsonObj = new JSONObject(HttpUtil.getBusLoc(
							"urlappend", mapRoute));
					if (jsonObj.get("flag").equals("true")) {
						busLat = Double.parseDouble(jsonObj.getString("lat"));
						busLng = Double.parseDouble(jsonObj.getString("lng"));
						busLL = new LatLng(busLat, busLng);
						String busLoc = jsonObj.getString("desc");
						// 向ui线程发送消息
						Intent in0 = new Intent(Values.BROADCASTTOUI);
						in0.putExtra("flag", Values.BUSFLAG);
						in0.putExtra("lat", busLat);
						in0.putExtra("lng", busLng);
						in0.putExtra("busdesc", busLoc);
						sendBroadcast(in0);

						if (userLL != null && busLL != null) {
							distance = AMapUtils.calculateLineDistance(userLL,
									busLL);
							Intent in1 = new Intent(Values.BROADCASTTOUI);
							in1.putExtra("flag", Values.DISTANCEFLAG);
							in1.putExtra("currentdistance", distance);
							sendBroadcast(in1);

							if (user.getIsRemind() && !hasVibrate
									&& distance < user.getRemindDistance()) {
								vibrator.vibrate(new long[] { 10, 600, 60, 600,
										60, 600 }, -1);
								hasVibrate = true;
							}
						}
					} else {
						Intent in2 = new Intent(Values.BROADCASTTOUI);
						in2.putExtra("flag", Values.BUSDISABLE);
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
			Intent in3 = new Intent(Values.BROADCASTTOUI);
			in3.putExtra("flag", Values.USERFLAG);
			in3.putExtra("lat", userLat);
			in3.putExtra("lng", userLng);
			sendBroadcast(in3);
		} else {
			Log.e("AmapErr", "Location ERR:"
					+ arg0.getAMapException().getErrorCode());
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

}
