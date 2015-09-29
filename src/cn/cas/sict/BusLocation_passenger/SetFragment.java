package cn.cas.sict.BusLocation_passenger;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.view.View.OnClickListener;

public class SetFragment extends Fragment {
	TextView tv_phone;
	EditText et_UserName, et_Feedback;
	RadioGroup rg_Route;
	Button bt_Save, bt_back;
	int routeNum;
	boolean isRemind;
	CheckBox checkboxRemind;
	SharedPreferences sharedPre;
	SharedPreferences.Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sharedPre = getActivity().getSharedPreferences("userconfig",
				getActivity().MODE_PRIVATE);
		editor = sharedPre.edit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_set, container,
				false);

		// 在fragment中获取控件方法
		et_UserName = (EditText) rootView.findViewById(R.id.et_username);
		et_UserName.setText(sharedPre.getString("name", "unnamed"));
		checkboxRemind = (CheckBox) rootView.findViewById(R.id.cb_remind);
		checkboxRemind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRemind = ((CheckBox) v).isChecked();
				Log.i("zzz", "isRemind" + isRemind);
			}
		});
		rg_Route = (RadioGroup) rootView.findViewById(R.id.rg_route);
		rg_Route.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_route1:
					routeNum = 1;
					Log.i("zzz", "routeNum" + routeNum);
					break;
				case R.id.rb_route2:
					routeNum = 2;
					Log.i("zzz", "routeNum" + routeNum);
					break;
				case R.id.rb_route3:
					routeNum = 3;
					Log.i("zzz", "routeNum" + routeNum);
					break;
				case R.id.rb_route4:
					routeNum = 4;
					Log.i("zzz", "routeNum" + routeNum);
					break;
				}
			}
		});
		bt_Save = (Button) rootView.findViewById(R.id.bt_save_conf);
		bt_Save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editor.putString("name", et_UserName.getText().toString())
						.putInt("route", routeNum)
						.putBoolean("remind", isRemind).commit();
				Log.i("zzz", "" + sharedPre.getString("phone", "null"));
			}
		});
		return rootView;
	}
}
