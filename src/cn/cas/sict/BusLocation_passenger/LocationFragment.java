package cn.cas.sict.BusLocation_passenger;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import android.support.v4.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class LocationFragment extends Fragment implements
		OnMarkerClickListener, OnInfoWindowClickListener, OnClickListener {

	static final CameraPosition SHENYANG = new CameraPosition(new LatLng(
			41.79676, 123.429096), 11, 0, 0);

	private AMap map;
	private MapView mapView;
	private Marker busMarker, userMarker;
	private LatLng userLatLng = null, busLatLng = null;
	private Double userLat, userLng, busLat, busLng;
	private Button btUserLoc, btBusLoc, bt_traffic;
	private BroadcastReceiver receiver;
	User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		user = (User) getArguments().getSerializable("user");
		Intent in0 = new Intent(Values.BROADCASTTOSERVICE);
		in0.putExtra("flag", Values.GETSERVICEINFO);
		getActivity().sendBroadcast(in0);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Values.BROADCASTTOUI);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {

				switch (arg1.getIntExtra("flag", -1)) {

				case Values.BUSFLAG:
					busLat = arg1.getDoubleExtra("lat", -1);
					busLng = arg1.getDoubleExtra("lng", -1);
					busLatLng = new LatLng(busLat, busLng);
					busMarker.setPosition(busLatLng);
					break;
				case Values.USERFLAG:
					userLat = arg1.getDoubleExtra("lat", -1);
					userLng = arg1.getDoubleExtra("lng", -1);
					userLatLng = new LatLng(userLat, userLng);
					userMarker.setPosition(userLatLng);
					break;
				case Values.DISTANCEFLAG:
					getActivity().setTitle(
							" 相距 "
									+ (int) arg1.getFloatExtra(
											"currentdistance", -1) + "米");
					break;
				case Values.BUSDISABLE:
					getActivity().setTitle("班车位置不可用");
					break;
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
		View rootView = inflater.inflate(R.layout.fragment_location, container,
				false);
		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		if (map == null) {
			map = mapView.getMap();
			map.moveCamera(CameraUpdateFactory.newCameraPosition(SHENYANG));
			map.setOnInfoWindowClickListener(this);
			map.setOnMarkerClickListener(this);
			busMarker = map.addMarker(new MarkerOptions());
			userMarker = map.addMarker(new MarkerOptions());
		}
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
		userMarker.setTitle(user.getName());
		busMarker.setTitle(user.getRouteName());
		busMarker.setSnippet(user.getRoutePhone());
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
	public void onInfoWindowClick(Marker arg0) {
		if (arg0.equals(busMarker)) {

		} else {

		}
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
				Toast.makeText(getActivity(), "获取中", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.bt_userloc:
			if (userLatLng != null) {
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,
						16));
				userMarker.showInfoWindow();
			} else {
				Toast.makeText(getActivity(), "定位中", Toast.LENGTH_LONG).show();
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
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		busMarker.destroy();
		userMarker.destroy();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}

}
