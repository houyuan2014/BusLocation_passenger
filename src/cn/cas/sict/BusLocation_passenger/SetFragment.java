package cn.cas.sict.BusLocation_passenger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SetFragment extends Fragment {
	TextView tv_phone;
	EditText et_UserName, et_Passwd, et_Feedback;
	RadioGroup rg_Route;
	Button bt_Save,bt_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_set, container,
				false);
		//在fragment中获取控件方法
		et_UserName = (EditText) rootView.findViewById(R.id.et_username);
		return rootView;
	}

}
