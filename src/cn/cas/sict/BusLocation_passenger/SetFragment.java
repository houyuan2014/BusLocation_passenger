package cn.cas.sict.BusLocation_passenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cas.sict.utils.SPUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SetFragment extends Fragment {
	ListView list;
	SharedPreferences sP;
	SimpleAdapter sAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sP = getActivity().getSharedPreferences("userconfig",
				Context.MODE_PRIVATE);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_set, container,
				false);
		list = (ListView) rootView.findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:// 手机号
					break;
				case 1:// 姓名
					Intent in0 = new Intent(getActivity(), NameActivity.class);
					startActivity(in0);
					break;
				case 2:// 路线
					break;
				case 3:// 提醒
					Intent in1 = new Intent(getActivity(), RemindActivity.class);
					startActivity(in1);
					break;
				case 4:// 反馈
					Intent in2 = new Intent(getActivity(), AdviceActivity.class);
					startActivity(in2);
					break;
				}
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showList();
	}

	private void showList() {
		// TODO Auto-generated method stub
		String phone = sP.getString(SPUtil.SP_USERPHONE, "userphone");
		String name = sP.getString(SPUtil.SP_USERNAME, "username");
		String routename = sP.getString(SPUtil.SP_ROUTENAME, "routename");
		String remindStr = "关";
		if (sP.getBoolean("remind", false)) {
			remindStr = "开"
					+ String.valueOf(sP.getFloat("reminddistance", 1000));
		}
		String[] titles = new String[] { "手机号", "姓名", "路线", "提醒", "反馈" };
		String[] contents = new String[] { phone, name, routename, remindStr,
				"" };
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("title", titles[i]);
			listItem.put("content", contents[i]);
			listItems.add(listItem);
		}
		Log.i("listItems", listItems.toString());
		sAdapter = new SimpleAdapter(getActivity(), listItems,
				R.layout.list_item, new String[] { "title", "content" },
				new int[] { R.id.item_title, R.id.item_content });
		list.setAdapter(sAdapter);

	}
}
