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
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;

public class BusLocationFragment extends Fragment implements
		AMapLocationListener {

	static final CameraPosition SHENYANG = new CameraPosition(new LatLng(
			41.79676, 123.429096), 11, 0, 0);
	Button btUserLoc, btBusLoc, bt_traffic;
	AMap map;
	MapView mapView;
	LatLng userLatLng, busLatLng;
	Map<String, String> userLocMap;
	String driverLocDes, flag;
	int a = 1;
	MarkerOptions driverMarkerOption, userMarkerOption;
	Marker drvierMarker, myMarker;
	LocationManagerProxy mapLocationManager;
	private boolean isUserlocValid, hasMoveCamera;
	Timer timer;; // ���嶨ʱ������ʱ������Handler���
	MyTimerTask timerTask;
	MyHandler handler;

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for (; !isUserlocValid;)
				;
			handler.sendEmptyMessage(3);
		}
	};

	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			if (msg.what == 3) { // ִ�ж�ʱ����
				requestDriverLoc();// ����HTTp����
//				if (!hasMoveCamera) {
//					map.animateCamera(CameraUpdateFactory.newLatLngBounds(
//							new LatLngBounds.Builder().include(busLatLng)
//									.include(userLatLng).build(), 10, 10, 5));
//					hasMoveCamera = true;
//				}
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
		isUserlocValid = false;
		hasMoveCamera = false;
		userLocMap = new HashMap<String, String>();
		driverMarkerOption = new MarkerOptions();
		userMarkerOption = new MarkerOptions();
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
			// myMap.setLocationSource(this);// ���ö�λ����
			// myMap.getUiSettings().setMyLocationButtonEnabled(true);//
			// ����Ĭ�϶�λ��ť�Ƿ���ʾ
			// myMap.setMyLocationEnabled(true);//
			// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
			drvierMarker = map.addMarker(driverMarkerOption);
			myMarker = map.addMarker(userMarkerOption);
		}
		if (mapLocationManager == null) {
			mapLocationManager = LocationManagerProxy
					.getInstance(getActivity());
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
			// ����������ʱ��Ϊ-1����λֻ��һ��
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
			mapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 10 * 1000, 10, this);
			// mAMapLocationManager.setGpsEnable(false);
		}

	}

	/**
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
			userLatLng = new LatLng(amapLocation.getLatitude(),
					amapLocation.getLongitude());
			myMarker.setPosition(userLatLng);
			userLocMap.put("lat", String.valueOf(userLatLng.latitude));
			userLocMap.put("lng", String.valueOf(userLatLng.longitude));
			userLocMap.put("rout", "1");
			isUserlocValid = true;
			Log.i("test", "userLatLng   "+userLocMap.toString());
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
		mapView.onResume();
		startMyTimer();
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

	private void requestDriverLoc() {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObj = new JSONObject(HttpUtil.post("url", userLocMap));
			if (jsonObj.get("flag").equals("true")) {
				driverLocDes = jsonObj.getString("desc");
				busLatLng = new LatLng(
						Double.valueOf(jsonObj.getString("lat")),
						Double.valueOf(jsonObj.getDouble("lng")));
				drvierMarker.setTitle(driverLocDes);
				drvierMarker.setPosition(busLatLng);
				drvierMarker.showInfoWindow();
				Log.i("test", "receivejson   "+jsonObj.toString());
				Log.i("test", Thread.currentThread() + " a " + a++);
			}else{
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
			ToastUtil.show(getActivity(), "����������Ӧ");
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
