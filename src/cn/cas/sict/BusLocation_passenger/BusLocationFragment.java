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
				requestBusLoc();// ����HTTp����
			}

		}
	};

	// DrivePath myDrivepath;DriveStep myDrivestep;List<LatLonPoint> myPolyline;
	// OnLocationChangedListener mListener;

	// ����fragmentʱ���ص����÷���ֻ�ᱻ����һ��
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

	// ÿ�δ��������Ƹ�fragment��view���ʱ�ص��÷���
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// ���ز����ļ�
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
					bt_traffic.setText("��ʾ·��");
					map.setTrafficEnabled(false);
				} else {
					bt_traffic.setText("����·��");
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
	 * ��λ�ɹ���ص�����
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
	 * ����������д
	 */
	@Override
	public void onResume() {
		super.onResume();
		hasVibrate = sP.getBoolean("hasvibrate", false);
		remindDistance = sP.getFloat("reminddistance", 1000);// �ٽ�����
		mapView.onResume();
		startMyTimer();

		Log.i("zzz", "hasvibrate" + hasVibrate);
		Log.i("zzz", "fra onResume....startMyTimer");
	}

	/**
	 * ����������д
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
	 * ����������д
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * ����������д
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
						busLocDes + "Ԥ��" + jsonObj.getString("time") + "����");

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
				ToastUtil.show(getActivity(), "�೵δ�ϴ�λ��");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("err", e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("err", e.toString());
			ToastUtil.show(getActivity(), "�쳣");
		}
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

	/**
	 * �رն�λ
	 */
	public void deactivate() {
		if (mapLocationManager != null) {
			mapLocationManager.removeUpdates(this);
			mapLocationManager.destroy();
		}
		mapLocationManager = null;
	}

}
