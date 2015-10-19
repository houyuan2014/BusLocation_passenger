package cn.cas.sict.BusLocation_passenger;

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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LocationFragment extends Fragment implements
		OnMarkerClickListener, OnClickListener {

	static final CameraPosition SHENYANG = new CameraPosition(new LatLng(
			41.79676, 123.429096), 11, 0, 0);

	AMap map;
	MapView mapView;
	Marker busMarker, userMarker;
	SharedPreferences sP;
	LatLng userLatLng, busLatLng;
	Double userLat, userLng, busLat, busLng;
	BroadcastReceiver receiver;
	Button btUserLoc, btBusLoc, bt_traffic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sP = getActivity().getSharedPreferences("userconfig",
				Context.MODE_PRIVATE);
		IntentFilter filter = new IntentFilter();
		filter.addAction("aaa");
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				if (arg1.hasExtra("userlat")) {
					userLat = arg1.getDoubleExtra("userlat", -1);
					userLng = arg1.getDoubleExtra("userlng", -1);
					userLatLng = new LatLng(userLat, userLng);
					userMarker.setPosition(userLatLng);
					System.out.println("receive用户" + arg1.toString()
							+ userLatLng.toString());

				}
				if (arg1.hasExtra("currentdistance")) {
					getActivity().setTitle(
							" 相距 " + arg1.getFloatExtra("currentdistance", -1)
									+ "米");
					System.out.println("receive距离" + arg1.toString()
							+ arg1.getFloatExtra("currentdistance", -1));

				}
				if (arg1.hasExtra("buslat")) {
					busLat = arg1.getDoubleExtra("buslat", -1);
					busLng = arg1.getDoubleExtra("buslng", -1);
					busLatLng = new LatLng(busLat, busLng);
					busMarker.setPosition(busLatLng);
					System.out.println("receive班车" + arg1.toString()
							+ busLatLng.toString());
				}

			}
		};
		getActivity().registerReceiver(receiver, filter);
		System.out.println("fra oncreated");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 加载布局文件
		View rootView = inflater.inflate(R.layout.fragment_location,
				container, false);
		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		if (map == null) {
			map = mapView.getMap();
			map.moveCamera(CameraUpdateFactory.newCameraPosition(SHENYANG));
			busMarker = map.addMarker(new MarkerOptions());
			userMarker = map.addMarker(new MarkerOptions());
		}
		btUserLoc = (Button) rootView.findViewById(R.id.bt_userloc);
		btBusLoc = (Button) rootView.findViewById(R.id.bt_busloc);
		bt_traffic = (Button) rootView.findViewById(R.id.bt_traffic);
		btUserLoc.setOnClickListener(this);
		btBusLoc.setOnClickListener(this);
		bt_traffic.setOnClickListener(this);
		System.out.println("fra onCreateView");
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
		System.out.println("fra onResume");

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

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		System.out.println("fra onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		busMarker.destroy();
		userMarker.destroy();
		System.out.println("fra onStop");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		getActivity().unregisterReceiver(receiver);
		System.out.println("fra onDestroy");
	}
}
