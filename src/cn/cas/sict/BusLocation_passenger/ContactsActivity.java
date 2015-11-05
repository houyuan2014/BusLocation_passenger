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
 * ��ʾ�û��ֻ��ϵ���ϵ��
 */
public class ContactsActivity extends Activity {
	private Context ctx = ContactsActivity.this;
	private TextView topTitleTextView;
	private ListView listView = null;
	List<HashMap<String, String>> contactsList = null;
	private EditText contactsSearchView;
	private ProgressDialog progressDialog = null;
	// ���ݼ�����ɵ���Ϣ
	private final int MESSAGE_SUCC_LOAD = 0;
	// ���ݲ�ѯ��ɵ���Ϣ
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
		// ʹ��listView��ʾ��ϵ��
		listView = (ListView) findViewById(R.id.contact_list);
		loadAndSaveContacts();
		// ��listView item�ĵ����¼�
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long _id) {
				HashMap<String, String> map = (HashMap<String, String>) adapterView
						.getItemAtPosition(position);
				String phone = map.get("phone");
				// ���ֻ��������Ԥ����ȥ������ǰ��+86����β�ո񡢡�-���ŵȣ�
				phone = phone.replaceAll("^(\\+86)", "");
				phone = phone.replaceAll("^(86)", "");
				phone = phone.replaceAll("-", "");
				phone = phone.trim();
				// �����ǰ���벢�����ֻ�����
				if (!SaleUtil.isValidPhoneNumber(phone))
					SaleUtil.createDialog(ctx, R.string.dialog_title_tip,
							getString(R.string.alert_contacts_error_phone));
				else {
					Intent intent = new Intent();
					intent.putExtra("phone", phone);
					setResult(RESULT_OK, intent);
					// intent.setClass(ContactsActivity.this,
					// SendMessageActivity.class);
					// �رյ�ǰ����
					// startActivity(intent); //SEND MESSAGE

					Uri smsToUri = Uri.parse("smsto:" + phone);

					intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
					intent.putExtra(Intent.EXTRA_SUBJECT, "title");
					intent.putExtra(Intent.EXTRA_TEXT, "�೵app���صĹٷ���ַ");

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
	 * ���ز��洢��ϵ������
	 */
	private void loadAndSaveContacts() {
		progressDialog = ProgressDialog.show(ctx, null, "���ڼ�����ϵ������...");
		new Thread() {
			@Override
			public void run() {
				// ��ȡ��ϵ������
				contactsList = findContacts();
				// ��ʱ�洢��ϵ������
				// DBHelper.saveContacts(ctx, contactsList);
				// ������Ϣ֪ͨ����UI
				handler.sendEmptyMessage(MESSAGE_SUCC_LOAD);
			}
		}.start();
	}

	/**
	 * ���������ӱ�����ʱ���л�ȡ��ϵ��
	 * 
	 * @param keyWord
	 *            ��ѯ�ؼ���
	 */
	private void queryContacts(final String keyWord) {
		new Thread() {
			@Override
			public void run() {
				// ��ȡ��ϵ������
				// contactsList = DBHelper.findContactsByCond(ctx, keyWord);
				// ping bi diao
				// ������Ϣ֪ͨ����UI
				handler.sendEmptyMessage(MESSAGE_SUCC_QUERY);
			}
		}.start();
	}

	/**
	 * ��ȡ�ֻ���ϵ����Ϣ
	 * 
	 * @return List<HashMap<String, String>>
	 */
	public List<HashMap<String, String>> findContacts() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		// ��ѯ��ϵ��
		Cursor contactsCursor = ctx.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null,
				PhoneLookup.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		// ����������
		int nameIndex = 0;
		// ��ϵ������
		String name = null;
		// ��ϵ��ͷ��ID
		String photoId = null;
		// ��ϵ�˵�ID����ֵ
		String contactsId = null;
		// ��ѯ��ϵ�˵ĵ绰����
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
			// ������ϵ�˺��루һ���˶�Ӧ�ڶ���绰���룩
			while (phoneCursor.moveToNext()) {
				HashMap<String, String> phoneMap = new HashMap<String, String>();
				// �����ϵ������
				phoneMap.put("name", name);
				// �����ϵ��ͷ��
				phoneMap.put("photo", photoId);
				// ��ӵ绰����
				phoneMap.put(
						"phone",
						phoneCursor.getString(phoneCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				// ��Ӻ������ͣ�סլ�绰���ֻ����롢��λ�绰�ȣ�
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
	 * �Զ�����ϵ��Adapter
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
