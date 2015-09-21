package cn.cas.sict.BusLocation_passenger;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BusLocationFragment extends Fragment implements LocationSource,
		AMapLocationListener {

	AMap aMap;
	MapView mapView;
	MarkerOptions markeroption;
	Marker marker;
	// DrivePath myDrivepath;DriveStep myDrivestep;List<LatLonPoint> myPolyline;
	OnLocationChangedListener mListener;
	LocationManagerProxy mAMapLocationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		markeroption = new MarkerOptions();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		Log.i("aaa", "111111");
		View rootView = inflater.inflate(R.layout.fragment_bus_location,
				container, false);
		Log.i("aaa", "22222");

		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		init();
		return rootView;
	}

	private void init() {
		// TODO Auto-generated method stub
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setLocationSource(this);// ���ö�λ����
			aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
			aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false

			LatLng laln = new LatLng(41.8, 123.6);
			markeroption.title("�೵λ��");
			markeroption.position(laln);
			markeroption.visible(true);
			marker = aMap.addMarker(markeroption);
			marker.showInfoWindow();
		}
	}

	/**
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����

				Log.i("aaa",
						"provider" + amapLocation.getProvider() + "\nLa"
								+ amapLocation.getLatitude() + "lg"
								+ amapLocation.getLongitude());

			} else {
				Log.e("AmapErr", "Location ERR:"
						+ amapLocation.getAMapException().getErrorCode());
			}
		}
	}

	/**
	 * ���λ
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy
					.getInstance(getActivity());
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
			// ����������ʱ��Ϊ-1����λֻ��һ��
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 10 * 1000, 10, this);
		}
	}

	/**
	 * ֹͣ��λ
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	/**
	 * ����������д
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * ����������д
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
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

}
