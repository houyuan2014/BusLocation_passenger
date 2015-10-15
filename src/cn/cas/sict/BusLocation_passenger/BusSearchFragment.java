package cn.cas.sict.BusLocation_passenger;

import cn.cas.sict.utils.ToastUtil;
import android.support.v4.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BusSearchFragment extends Fragment {
	Button tv_show;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction("aaa");
		BroadcastReceiver receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				Log.i("zzzz", arg1.getIntExtra("count", 0) + "");
			}
		};
		getActivity().registerReceiver(receiver, filter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_bus, container,
				false);
		tv_show = (Button) rootView.findViewById(R.id.bt_get_value);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ToastUtil.show(getActivity(), "binder.getCount()");
			}
		});
		return rootView;
	}

	@Override
	// 切换到该fragment
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("--fragment onresume--");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("--fragment onpause--");

	}

	@Override
	// 屏幕灭掉，回到桌面
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("--fragment onstop--");

	}

	@Override
	// 退出应用
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("--fragment ondestroy--");

	}

}
