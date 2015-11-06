package cn.cas.sict.BusLocation_passenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
				
				// �ֻ���
				case 0:
					break;
				// ����
				case 1:
					Intent in0 = new Intent(getActivity(), NameActivity.class);
					in0.putExtra("user", user);
					startActivity(in0);
					break;
				// ·��
				case 2:
					final String[] items = new String[] { "һ����", "������", "������",
							"�ĺ���" };
					Builder builder = new AlertDialog.Builder(getActivity());
					builder.setIcon(R.drawable.che1);
					builder.setTitle("����ѡ��Ҫ��λ�İ೵·�ߣ�");
					builder.setItems(items, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int routeNum) {
							// д���û������ļ�
							user.setRouteName(items[routeNum]);
							user.setRouteNum(routeNum + 1);
							// ����ˢ�£�·�߸ı�
							onResume();
						}
					});
					builder.create().show();
					break;
				// ����
				case 3:
					Intent in1 = new Intent(getActivity(), RemindActivity.class);
					in1.putExtra("user", user);
					startActivity(in1);
					break;
				// ����
				case 4:
					Intent in2 = new Intent(getActivity(), AdviceActivity.class);
					startActivity(in2);
					break;
				// ע��
				case 5:
					Intent in3 = new Intent(getActivity(), LoginActivity.class);
					in3.putExtra("user", user);
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
		String remindStr;// �޸�
		if (user.getIsRemind()) {
			remindStr = user.getRemindDistance() + "��";
		} else {
			remindStr = "��";
		}
		String[] titles = new String[] { "�ֻ���", "����", "·��", "����", "����", "ע��" };
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

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}
}
