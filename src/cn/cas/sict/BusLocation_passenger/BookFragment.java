package cn.cas.sict.BusLocation_passenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BookFragment extends Fragment {
	private TextView tv_location, tv_distance, tv_route;
	private Spinner spinner;
	private BroadcastReceiver receiver;
	User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
					tv_location.setText(arg1.getStringExtra("busdesc"));
					break;
				case Values.DISTANCEFLAG:
					tv_distance.setText((int) arg1.getFloatExtra(
							"currentdistance", -1) + "");
					break;
				case Values.BUSDISABLE:
					tv_location.setText(arg1.getStringExtra("班车位置不可用"));
					break;
				}
			}
		};
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_book, container,
				false);
		tv_route = (TextView) rootView.findViewById(R.id.tv_route);
		tv_route.setText(user.getRouteName());
		tv_location = (TextView) rootView.findViewById(R.id.tv_location);
		tv_distance = (TextView) rootView.findViewById(R.id.tv_distance);
		spinner = (Spinner) rootView.findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(getActivity(), "arg2" + arg2 + "arg3" + arg3,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}
}
