package cn.cas.sict.BusLocation_passenger;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import cn.cas.sict.utils.SPUtil;
import cn.cas.sict.utils.ToastUtil;
import android.support.v4.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BusSearchFragment extends Fragment implements
		AMapLocationListener, OnMarkerClickListener, OnClickListener {

	static final CameraPosition SHENYANG = new CameraPosition(new LatLng(
			41.79676, 123.429096), 11, 0, 0);

	Button btUserLoc, btBusLoc, bt_traffic;
	AMap map;
	MapView mapView;
	Marker busMarker, userMarker;
	LocationManagerProxy mapLocationManager;
	BroadcastReceiver receiver;
	Intent in = new Intent("bbb");
	SharedPreferences sP;
	SharedPreferences.Editor editor;
	LatLng userLatLng, busLatLng;
	Double userLat, userLng, busLat, busLng;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sP = getActivity().getSharedPreferences("userconfig",
				Context.MODE_PRIVATE);
		editor = sP.edit();
		IntentFilter filter = new IntentFilter();
		filter.addAction("aaa");
		 receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				if (arg1.hasExtra("currentdistance")) {
					getActivity().setTitle(
							" 相距 " + arg1.getStringExtra("currentdistance")
									+ "米");
					System.out.println("设置标题距离"+in.toString()+arg1.getStringExtra("currentdistance"));

				}
				if (arg1.hasExtra("buslat")) {
					busLat = arg1.getDoubleExtra("buslat", -1);
					busLng = arg1.getDoubleExtra("buslng", -1);
					busLatLng = new LatLng(busLat, busLng);
					busMarker.setPosition(busLatLng);
					System.out.println("设置班车坐标"+in.toString()+busLatLng.toString());
				}

			}
		};
		getActivity().registerReceiver(receiver, filter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 加载布局文件
		View rootView = inflater.inflate(R.layout.fragment_bus_location,
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

		mapView.onResume();
		userMarker.setTitle(sP.getString(SPUtil.SP_USERNAME, "我"));
		busMarker.setTitle(sP.getString(SPUtil.SP_ROUTENAME, "四号线"));
		busMarker.setSnippet(sP.getString(SPUtil.SP_ROUTEPHONE, "18855665566"));
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
		busMarker.destroy();
		userMarker.destroy();
		getActivity().setTitle(R.string.app_name);
		Log.i("circle", "fra onStop");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		getActivity().unregisterReceiver(receiver);
		Log.i("circle", "fra ondestroy");

	}

	private void initMap() {
		// TODO Auto-generated method stub
		if (map == null) {
			map = mapView.getMap();
			map.moveCamera(CameraUpdateFactory.newCameraPosition(SHENYANG));
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
			userLat = amapLocation.getLatitude();
			userLng = amapLocation.getLongitude();
			userLatLng = new LatLng(userLat, userLng);
			userMarker.setPosition(userLatLng);
			userMarker.setSnippet(amapLocation.getRoad());
			in.putExtra("userlat", userLat);
			in.putExtra("userlng", userLng);
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

}
