package cn.cas.sict.BusLocation_passenger;

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
	private TextView tv_busLoc,tv_distance;
	private Spinner spinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_book, container,
				false);
		tv_busLoc = (TextView) rootView.findViewById(R.id.tv_busloc);
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
}
