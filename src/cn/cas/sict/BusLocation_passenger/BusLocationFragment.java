package cn.cas.sict.BusLocation_passenger;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import cn.cas.sict.utils.HttpUtil;
import cn.cas.sict.utils.ToastUtil;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class BusLocationFragment extends Fragment implements
		AMapLocationListener {

	static final CameraPosition SHENYANG = new CameraPosition(new LatLng(
			41.79676, 123.429096), 11, 0, 0);
	Button btUserLoc, btBusLoc, bt_traffic;
	Vibrator vibrator;
	SharedPreferences sP;
	SharedPreferences.Editor editor;
	float remindDistance;
	AMap map;
	MapView mapView;
	LatLng userLatLng, busLatLng;
	String busLocDes, flag;
	boolean hasVibrate;
	int a = 1;
	Map<String, String> userLocMap;
	MarkerOptions busMarkerOption, userMarkerOption;
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
		userLocMap = new HashMap<String, String>();
		busMarkerOption = new MarkerOptions();
		userMarkerOption = new MarkerOptions();
		userLatLng = null;
		busLatLng = null;
	}

	// 每次创建，绘制该fragment的view组件时回调该方法
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 加载布局文件
		View rootView = inflater.inflate(R.layout.fragment_bus_location,
				container, false);
		btUserLoc = (Button) rootView.findViewById(R.id.bt_userloc);
		btUserLoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (userLatLng != null) {
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(
							userLatLng, 16));
				}
			}
		});
		btBusLoc = (Button) rootView.findViewById(R.id.bt_busloc);
		btBusLoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (busLatLng != null) {
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(
							busLatLng, 16));
					// busMarker.showInfoWindow();
				}
				Log.i("test", "buslatlng is null");
			}
		});
		bt_traffic = (Button) rootView.findViewById(R.id.bt_traffic);
		bt_traffic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (map.isTrafficEnabled()) {
					bt_traffic.setText("显示路况");
					map.setTrafficEnabled(false);
				} else {
					bt_traffic.setText("隐藏路况");
					map.setTrafficEnabled(true);

				}
			}
		});

		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		initMap();
		return rootView;
	}

	private void initMap() {
		// TODO Auto-generated method stub
		if (map == null) {
			map = mapView.getMap();
			map.moveCamera(CameraUpdateFactory.newCameraPosition(SHENYANG));
			busMarker = map.addMarker(busMarkerOption);
			userMarker = map.addMarker(userMarkerOption);
		}
		if (mapLocationManager == null) {
			mapLocationManager = LocationManagerProxy
					.getInstance(getActivity());
			mapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 10 * 1000, 10, this);
		}

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
			userLocMap.put("lat", String.valueOf(userLatLng.latitude));
			userLocMap.put("lng", String.valueOf(userLatLng.longitude));
			userLocMap.put("rout", "1");
			Log.i("test", "userLatLng   " + userLocMap.toString());
		} else {
			Log.e("AmapErr", "Location ERR:"
					+ amapLocation.getAMapException().getErrorCode());
		}

	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		super.onResume();
		hasVibrate = sP.getBoolean("hasvibrate", false);
		remindDistance = sP.getFloat("reminddistance", 1000);// 临近距离
		mapView.onResume();
		startMyTimer();

		Log.i("zzz", "hasvibrate" + hasVibrate);
		Log.i("zzz", "fra onResume....startMyTimer");
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
		Log.i("zzz", "fra onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		vibrator.cancel();
		timerTask.cancel();
		timer.purge();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onLocationChanged(Location amapLocation) {
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

	private void requestBusLoc() {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObj = new JSONObject(
					HttpUtil.post("url", userLocMap));
			if (jsonObj.get("flag").equals("true")) {
				busLocDes = jsonObj.getString("desc");
				busLatLng = new LatLng(
						Double.valueOf(jsonObj.getString("lat")),
						Double.valueOf(jsonObj.getDouble("lng")));
				busMarker.setPosition(busLatLng);
				getActivity().setTitle(
						busLocDes + "预计" + jsonObj.getString("time") + "到达");

				if (!hasVibrate && userLatLng != null && busLatLng != null) {
					if (AMapUtils.calculateLineDistance(userLatLng, busLatLng) <= remindDistance) {
						Log.i("zzz",
								"distance  "
										+ AMapUtils.calculateLineDistance(
												userLatLng, busLatLng));
						hasVibrate = true;
						editor.putBoolean("hasvibrate", hasVibrate).commit();
						Log.i("zzz",
								"hasVibrate write"
										+ sP.getBoolean("hasvibrate", false)
										+ "  " + sP.getAll());
						vibrator.vibrate(
								new long[] { 70, 800, 60, 800, 50, 800 }, -1);
					}
				}

				Log.i("test", "receivejson   " + jsonObj.toString());
			} else {
				ToastUtil.show(getActivity(), "班车未上传位置");
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

}
