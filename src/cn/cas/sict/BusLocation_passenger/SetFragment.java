package cn.cas.sict.BusLocation_passenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SetFragment extends Fragment {
	private ListView list;
	private SimpleAdapter sAdapter;
	private Button share;
	private User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		user = (User) getArguments().getSerializable("user");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_set, container,
				false);

		share = (Button) rootView.findViewById(R.id.share);
		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent();
				in.setClass(getActivity(), ShareActivity.class);
				startActivity(in);

			}
		});
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
					in0.putExtra("user", user);
					startActivity(in0);
					break;
				case 2:// 路线
					final String[] items = new String[] { "一号线", "二号线", "三号线",
							"四号线" };
					Builder builder = new AlertDialog.Builder(getActivity());
					builder.setIcon(R.drawable.che1);
					builder.setTitle("请您选择要定位的班车路线：");
					builder.setItems(items, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int routeNum) {
							// 写入用户配置文件
							user.setRouteName(items[routeNum]);
							user.setRouteNum(routeNum + 1);
							// 调用刷新，路线改变
							onResume();
						}
					});
					builder.create().show();
					break;
				case 3:// 提醒
					Intent in1 = new Intent(getActivity(), RemindActivity.class);
					in1.putExtra("user", user);
					startActivity(in1);
					break;
				case 4:// 反馈
					Intent in2 = new Intent(getActivity(), AdviceActivity.class);
					startActivity(in2);
					break;
				case 5:// 注销，更改用户
					Intent in3 = new Intent(getActivity(), LoginActivity.class);
					startActivity(in3);
					getActivity().finish();
				}
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		showList();
		System.out.println("set onResume");
	}

	private void showList() {
		String phone = user.getPhone();
		String name = user.getName();
		String routename = user.getRouteName();
		String remindStr;// 修改
		if (user.getIsRemind()) {
			remindStr = user.getRemindDistance() + "米";
		} else {
			remindStr = "关";
		}
		String[] titles = new String[] { "手机号", "姓名", "路线", "提醒", "反馈", "注销" };
		String[] contents = new String[] { phone, name, routename, remindStr,
				"", "" };
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("title", titles[i]);
			listItem.put("content", contents[i]);
			listItems.add(listItem);
		}
		sAdapter = new SimpleAdapter(getActivity(), listItems,
				R.layout.list_item, new String[] { "title", "content" },
				new int[] { R.id.item_title, R.id.item_content });
		list.setAdapter(sAdapter);

	}

	@Override
	public void onPause() {
		super.onPause();
		System.out.println("set onPause");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("set onDestroy");

	}
}
