package cn.cas.sict.BusLocation_passenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.cas.sict.utils.SaleUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 显示用户手机上的联系人
 */
public class ContactsActivity extends Activity {
	private Context ctx = ContactsActivity.this;
	private TextView topTitleTextView;
	private ListView listView = null;
	List<HashMap<String, String>> contactsList = null;
	private EditText contactsSearchView;
	private ProgressDialog progressDialog = null;
	// 数据加载完成的消息
	private final int MESSAGE_SUCC_LOAD = 0;
	// 数据查询完成的消息
	private final int MESSAGE_SUCC_QUERY = 1;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_SUCC_LOAD:
				listView.setAdapter(new ContactsAdapter(ctx));
				progressDialog.dismiss();
				break;
			case MESSAGE_SUCC_QUERY:
				listView.setAdapter(new ContactsAdapter(ctx));
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_contacts);
		// 使用listView显示联系人
		listView = (ListView) findViewById(R.id.contact_list);
		loadAndSaveContacts();
		// 绑定listView item的单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long _id) {
				HashMap<String, String> map = (HashMap<String, String>) adapterView
						.getItemAtPosition(position);
				String phone = map.get("phone");
				// 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
				phone = phone.replaceAll("^(\\+86)", "");
				phone = phone.replaceAll("^(86)", "");
				phone = phone.replaceAll("-", "");
				phone = phone.trim();
				// 如果当前号码并不是手机号码
				if (!SaleUtil.isValidPhoneNumber(phone))
					SaleUtil.createDialog(ctx, R.string.dialog_title_tip,
							getString(R.string.alert_contacts_error_phone));
				else {
					Intent intent = new Intent();
					intent.putExtra("phone", phone);
					setResult(RESULT_OK, intent);
					// intent.setClass(ContactsActivity.this,
					// SendMessageActivity.class);
					// 关闭当前窗口
					// startActivity(intent); //SEND MESSAGE

					Uri smsToUri = Uri.parse("smsto:" + phone);

					intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
					intent.putExtra(Intent.EXTRA_SUBJECT, "title");
					intent.putExtra(Intent.EXTRA_TEXT, "班车app下载的官方网址");

					startActivity(intent);

				}
			}
		});
		contactsSearchView = (EditText) findViewById(R.id.search_view);
		contactsSearchView.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				queryContacts(s.toString());
			}
		});
	}

	/**
	 * 加载并存储联系人数据
	 */
	private void loadAndSaveContacts() {
		progressDialog = ProgressDialog.show(ctx, null, "正在加载联系人数据...");
		new Thread() {
			@Override
			public void run() {
				// 获取联系人数据
				contactsList = findContacts();
				// 临时存储联系人数据
				// DBHelper.saveContacts(ctx, contactsList);
				// 发送消息通知更新UI
				handler.sendEmptyMessage(MESSAGE_SUCC_LOAD);
			}
		}.start();
	}

	/**
	 * 根据条件从本地临时库中获取联系人
	 * 
	 * @param keyWord
	 *            查询关键字
	 */
	private void queryContacts(final String keyWord) {
		new Thread() {
			@Override
			public void run() {
				// 获取联系人数据
				// contactsList = DBHelper.findContactsByCond(ctx, keyWord);
				// ping bi diao
				// 发送消息通知更新UI
				handler.sendEmptyMessage(MESSAGE_SUCC_QUERY);
			}
		}.start();
	}

	/**
	 * 获取手机联系人信息
	 * 
	 * @return List<HashMap<String, String>>
	 */
	public List<HashMap<String, String>> findContacts() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		// 查询联系人
		Cursor contactsCursor = ctx.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null,
				PhoneLookup.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		// 姓名的索引
		int nameIndex = 0;
		// 联系人姓名
		String name = null;
		// 联系人头像ID
		String photoId = null;
		// 联系人的ID索引值
		String contactsId = null;
		// 查询联系人的电话号码
		Cursor phoneCursor = null;
		while (contactsCursor.moveToNext()) {
			nameIndex = contactsCursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			name = contactsCursor.getString(nameIndex);
			photoId = contactsCursor.getString(contactsCursor
					.getColumnIndex(PhoneLookup.PHOTO_ID));
			contactsId = contactsCursor.getString(contactsCursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			phoneCursor = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactsId, null, null);
			// 遍历联系人号码（一个人对应于多个电话号码）
			while (phoneCursor.moveToNext()) {
				HashMap<String, String> phoneMap = new HashMap<String, String>();
				// 添加联系人姓名
				phoneMap.put("name", name);
				// 添加联系人头像
				phoneMap.put("photo", photoId);
				// 添加电话号码
				phoneMap.put(
						"phone",
						phoneCursor.getString(phoneCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				// 添加号码类型（住宅电话、手机号码、单位电话等）
				phoneMap.put(
						"type",
						getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(phoneCursor.getInt(phoneCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)))));
				list.add(phoneMap);
			}
			phoneCursor.close();
		}
		contactsCursor.close();
		return list;
	}

	/**
	 * 自定义联系人Adapter
	 */
	class ContactsAdapter extends BaseAdapter {
		private LayoutInflater inflater = null;

		public ContactsAdapter(Context ctx) {
			inflater = LayoutInflater.from(ctx);
		}

		public int getCount() {
			return contactsList.size();
		}

		public Object getItem(int position) {
			return contactsList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.activity_list, null);
				holder.text1 = (TextView) convertView.findViewById(R.id.text1);
				holder.text2 = (TextView) convertView.findViewById(R.id.text2);
				holder.text3 = (TextView) convertView.findViewById(R.id.text3);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text1.setText(contactsList.get(position).get("name"));
			holder.text2.setText(contactsList.get(position).get("type"));
			holder.text3.setText(contactsList.get(position).get("phone"));

			return convertView;
		}

		public final class ViewHolder {
			private TextView text1;
			private TextView text2;
			private TextView text3;

		}
	}
}
