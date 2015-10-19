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
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class Test1Fragment extends Fragment implements
		AMapLocationListener, OnMarkerClickListener, OnClickListener {

	static final CameraPosition SHENYANG = new CameraPosition(new LatLng(
			41.79676, 123.429096), 11, 0, 0);
	private WakeLock wakeLock = null;
	Button btUserLoc, btBusLoc, bt_traffic;
	Vibrator vibrator;
	SharedPreferences sP;
	SharedPreferences.Editor editor;
	float remindDistance, distance;
	int routeNum;
	AMap map;
	MapView mapView;
	LatLng userLatLng, busLatLng;
	// String busLocDes, userDes, flag;
	boolean hasVibrate;
	Map<String, Object> userLocMap;
	Marker busMarker, userMarker;
	LocationManagerProxy mapLocationManager;
	Timer timer;; // 定义定时器、定时器任务及Handler句柄
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
			if (msg.what == 3) { // 执行定时任务
				requestBusLoc();// 发送HTTp请求
			}

		}
	};

	// DrivePath myDrivepath;DriveStep myDrivestep;List<LatLonPoint> myPolyline;
	// OnLocationChangedListener mListener;

	// 创建fragment时被回调，该方法只会被调用一次
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		timer = new Timer();
		handler = new MyHandler();
		vibrator = (Vibrator) getActivity().getSystemService(
				Service.VIBRATOR_SERVICE);
		sP = getActivity().getSharedPreferences("userconfig",
				Context.MODE_PRIVATE);
		editor = sP.edit();
		userLocMap = new HashMap<String, Object>();
		userLatLng = null;
		busLatLng = null;
		distance = -1;
	}

	// 每次创建，绘制该fragment的view组件时回调该方法
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 加载布局文件
		View rootView = inflater.inflate(R.layout.fragment_location,
				container, false);
		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		initMap();
		btUserLoc = (Button) rootView.findViewById(R.id.bt_userloc);
		btBusLoc = (Button) rootView.findViewById(R.id.bt_busloc);
		bt_traffic = (Button) rootView.findViewById(R.id.bt_traffic);
		btUserLoc.setOnClickListener(this);
		btBusLoc.setOnClickListener(this);
		bt_traffic.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		hasVibrate = sP.getBoolean(SPUtil.SP_HASVIBRATE, false);
		remindDistance = sP.getFloat(SPUtil.SP_REMINDDISTANCE, 1000);// 临近距离
		routeNum = sP.getInt(SPUtil.SP_ROUTENUM, 1);
		userMarker.setTitle(sP.getString(SPUtil.SP_USERNAME, "我"));
		busMarker.setTitle(sP.getString(SPUtil.SP_ROUTENAME, "四号线"));
		busMarker.setSnippet(sP.getString(SPUtil.SP_ROUTEPHONE, "18855665566"));
		mapView.onResume();
		startMyTimer();
		acquireWakeLock();

		Log.i("test", "sP  " + sP.getAll().toString());
		Log.i("circle", "fra onResume");

	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
		Log.i("circle", "fra onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		vibrator.cancel();
		timerTask.cancel();
		timer.purge();
		busMarker.destroy();
		userMarker.destroy();
		getActivity().setTitle(R.string.app_name);
		Log.i("circle", "fra onStop");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		releaseWakeLock();
		Log.i("circle", "fra ondestroy");

	}

	private void initMap() {
		// TODO Auto-generated method stub
		if (map == null) {
			map = mapView.getMap();
			map.moveCamera(CameraUpdateFactory.newCameraPosition(SHENYANG));
			map.setOnMarkerClickListener(this);
			busMarker = map.addMarker(new MarkerOptions());
			userMarker = map.addMarker(new MarkerOptions());
		}
		if (mapLocationManager == null) {
			mapLocationManager = LocationManagerProxy
					.getInstance(getActivity());
			mapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 10 * 1000, 10, this);
		}

	}

	private void requestBusLoc() {
		// TODO Auto-generated method stub
		if (userLatLng != null) {
			userLocMap.put("flag", true);
			userLocMap.put("lat", userLatLng.latitude);
			userLocMap.put("lng", userLatLng.longitude);
			userLocMap.put("route", routeNum);
		} else {
			userLocMap.put("flag", false);
			userLocMap.put("lat", "");
			userLocMap.put("lng", "");
			userLocMap.put("route", routeNum);
		}
		try {
			JSONObject jsonObj = new JSONObject(
					HttpUtil.post("url", userLocMap));
			if (jsonObj.get("flag").equals("true")) {
				// busLocDes = jsonObj.getString("desc");
				busLatLng = new LatLng(
						Double.valueOf(jsonObj.getString("lat")),
						Double.valueOf(jsonObj.getDouble("lng")));
				busMarker.setPosition(busLatLng);
				// busMarker.setSnippet(busLocDes);

				if (!hasVibrate && userLatLng != null && busLatLng != null) {
					distance = AMapUtils.calculateLineDistance(userLatLng,
							busLatLng);
					// busMarker.setSnippet(busLocDes + " 相距 " + distance+"米");
					getActivity().setTitle(" 相距 " + distance + "米");
					if (distance <= remindDistance) {
						hasVibrate = true;
						vibrator.vibrate(
								new long[] { 10, 600, 60, 600, 60, 600 }, -1);
						editor.putBoolean("hasvibrate", hasVibrate).commit();
					}
				}

				Log.i("test", "receivejson   " + jsonObj.toString());
			} else {
				ToastUtil.show(getActivity(), "班车位置不可用,重试中");
				// timerTask.cancel();
				// timer.purge();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("err", e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("err", e.toString());
			ToastUtil.show(getActivity(), "异常");
		}
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

	/**
	 * 关闭定位
	 */
	public void deactivate() {
		if (mapLocationManager != null) {
			mapLocationManager.removeUpdates(this);
			mapLocationManager.destroy();
		}
		mapLocationManager = null;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if (marker.isInfoWindowShown()) {
			marker.hideInfoWindow();
		} else {
			marker.showInfoWindow();
		}

		return true;
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			userLatLng = new LatLng(amapLocation.getLatitude(),
					amapLocation.getLongitude());
			userMarker.setPosition(userLatLng);
			userMarker.setSnippet(amapLocation.getRoad());
		} else {
			Log.e("AmapErr", "Location ERR:"
					+ amapLocation.getAMapException().getErrorCode());
		}

	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_busloc:
			if (busLatLng != null) {
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(busLatLng,
						16));
				busMarker.showInfoWindow();
			} else {
				ToastUtil.show(getActivity(), "获取中");
			}
			break;
		case R.id.bt_userloc:
			if (userLatLng != null) {
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,
						16));
				userMarker.showInfoWindow();
			} else {
				ToastUtil.show(getActivity(), "定位中");
			}
			break;
		case R.id.bt_traffic:
			if (map.isTrafficEnabled()) {
				bt_traffic.setText("显示路况");
				map.setTrafficEnabled(false);
			} else {
				bt_traffic.setText("隐藏路况");
				map.setTrafficEnabled(true);

			}
			break;
		}

	}

	/**
	 * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
	 */
	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) getActivity().getSystemService(
					Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "my tag");
			Log.i("wl", "call acquireWakeLock");
			wakeLock.acquire();
		} else {
			Log.i("wl", "call acquireWakeLock");
			wakeLock.acquire();
		}

	}

	// 释放设备电源锁
	private void releaseWakeLock() {
		if (null != wakeLock && wakeLock.isHeld()) {
			Log.i("wl", "call releaseWakeLock");
			wakeLock.release();
			wakeLock = null;
		}
	}
}
