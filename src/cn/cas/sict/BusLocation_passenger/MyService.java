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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	boolean hasVibrate;
	SharedPreferences sP;
	SharedPreferences.Editor editor;
	float remindDistance, distance;
	int routeNum;
	Double userLat, userLng, busLat, busLng;
	LatLng userLL, busLL;
	Map<String, Object> mapRoute;
	Timer timer;; // ���嶨ʱ������ʱ������Handler���
	MyTimerTask timerTask;
	MyHandler handler;

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(3);
		}
	};

	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			if (msg.what == 3) { // ִ�ж�ʱ����
				mapRoute = new HashMap<String, Object>();
				mapRoute.put("route", routeNum + "");
				try {
					JSONObject jsonObj = new JSONObject(HttpUtil.post("url",
							mapRoute));
					if (jsonObj.get("flag").equals("true")) {
						busLat = Double.parseDouble(jsonObj.getString("lat"));
						busLng = Double.parseDouble(jsonObj.getString("lng"));
						busLL = new LatLng(busLat, busLng);
						Intent in = new Intent("aaa");// ��ui�̷߳�����Ϣ
						in.putExtra("buslat", busLat);
						in.putExtra("buslng", busLng);
						sendBroadcast(in);
						if (userLL != null && busLL != null) {
							distance = AMapUtils.calculateLineDistance(userLL,
									busLL);
							Intent in1 = new Intent("aaa");
							in1.putExtra("currentdistance", distance);
							sendBroadcast(in1);
							if (!hasVibrate && distance < remindDistance) {
								hasVibrate = true;
								vibrator.vibrate(new long[] { 10, 600, 60, 600,
										60, 600 }, -1);
								editor.putBoolean("hasvibrate", hasVibrate)
										.commit();
							}
						}
						Log.i("test", "receivejson   " + jsonObj.toString());
					} else {
						ToastUtil.show(getApplicationContext(), "�೵λ�ò�����,������");
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

	// Service������ʱ�ص��÷�����
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
		remindDistance = sP.getFloat(SPUtil.SP_REMINDDISTANCE, 1000);// �ٽ�����
		routeNum = sP.getInt(SPUtil.SP_ROUTENUM, 1);
		distance = -1;

		lMP = LocationManagerProxy.getInstance(this);
		lMP.requestLocationData(LocationProviderProxy.AMapNetwork, 3 * 1000,
				10, this);

		System.out.println("Service is Created");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("--service onstart");
		startMyTimer();
		return super.onStartCommand(intent, flags, startId);
	}

	private void startMyTimer() {
		// TODO Auto-generated method stub
		if (timer != null) {
			if (timerTask != null) {
				timerTask.cancel(); // ��ԭ����Ӷ�����ɾ��
			}
		}
		/**
		 * ע�⣺ ÿ�η�����Ҫ�½�һ��������󣬷������һ�´��� ERROR/AndroidRuntime(11761):
		 * java.lang.IllegalStateException: TimerTask is scheduled already
		 * ����ͬһ����ʱ������ֻ�ܱ�����һ��
		 */
		timerTask = new MyTimerTask(); // �½�һ�����񣨱��룩
		timer.scheduleAtFixedRate(timerTask, 500, 5000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		vibrator.cancel();
		timerTask.cancel();
		timer.purge();
		lMP.removeUpdates(this);
		lMP.destroy();
		System.out.println("Service is Destroyed");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		// TODO Auto-generated method stub
		if (arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
			userLat = arg0.getLatitude();
			userLng = arg0.getLongitude();
			userLL = new LatLng(userLat, userLng);
			System.out.println(userLL.toString());
			Intent in2 = new Intent("aaa");// ��ui�̷߳�����Ϣ
			in2.putExtra("userlat", userLat);
			in2.putExtra("userlng", userLng);
			sendBroadcast(in2);
		} else {
			Log.e("AmapErr", "Location ERR:"
					+ arg0.getAMapException().getErrorCode());
		}
	}

	// /**
	// * ��ȡ��Դ�������ָ÷�������ĻϨ��ʱ��Ȼ��ȡCPUʱ����������
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
	// // �ͷ��豸��Դ��
	// private void releaseWakeLock() {
	// if (null != wakeLock && wakeLock.isHeld()) {
	// Log.i("wl", "call releaseWakeLock");
	// wakeLock.release();
	// wakeLock = null;
	// }
	// }
}
