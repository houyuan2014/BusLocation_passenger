package cn.cas.sict.BusLocation_passenger;

import cn.cas.sict.utils.SPUtil;
import cn.cas.sict.utils.ToastUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ZhuceActivity extends Activity {
	Button btZhuCe;
	SharedPreferences sP;
	SharedPreferences.Editor editor;
	String tel, psw, psw1,routeName;
	int routeNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuce);
		sP = getSharedPreferences("userconfig", MODE_PRIVATE);
		editor = sP.edit();
		Spinner route = (Spinner) findViewById(R.id.route);
		route.getSelectedItem();
		btZhuCe = (Button) findViewById(R.id.bt_logup);
		String[] routename = new String[] { "一号线", "二号线", "三号线", "四号线" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, routename);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		route.setAdapter(adapter);
		route.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				routeName = arg0.getItemAtPosition(arg2).toString();
				Toast.makeText(ZhuceActivity.this, routeName, Toast.LENGTH_SHORT)
						.show();
				routeNum = arg2;
				Log.i("sP", sP.getAll() + "");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		btZhuCe = (Button) findViewById(R.id.bt_logup);
		btZhuCe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tel = ((EditText) findViewById(R.id.tel)).getText().toString();
				psw = ((EditText) findViewById(R.id.psw)).getText().toString();
				psw1 = ((EditText) findViewById(R.id.psw1)).getText()
						.toString();
				if (validate(tel, psw, psw1)) {
					if (loguppro(tel, psw)) {
						editor.putString(SPUtil.SP_USERPHONE, tel)
								.putString(SPUtil.SP_USERPASSWD, psw)
								.putString(SPUtil.SP_USERNAME, "")
								.putInt(SPUtil.SP_ROUTENUM, routeNum)
								.putString(SPUtil.SP_ROUTENAME, routeName).commit();
						Intent intent = new Intent(ZhuceActivity.this,
								LoginActivity.class);
						startActivity(intent);
						finish();
					}
				}
			}
		});
	}

	private boolean validate(String tel, String psw, String psw1) {
		// TODO Auto-generated method stub
		if (!"".equals(tel) && !"".equals(psw) && !"".equals(psw1)) {
			if (tel.length() == 11) {
				if (!psw.equals(psw1)) {
					Toast.makeText(ZhuceActivity.this, "两次输入的密码不一致，请重新输入！",
							Toast.LENGTH_LONG).show();
					((EditText) findViewById(R.id.psw)).setText("");
					((EditText) findViewById(R.id.psw1)).setText("");
					((EditText) findViewById(R.id.psw)).requestFocus();
				} else {
					return true;
				}
			} else {
				Toast.makeText(ZhuceActivity.this, "请输入11位手机号！！！",
						Toast.LENGTH_LONG).show();
				((EditText) findViewById(R.id.tel)).setText("");
				((EditText) findViewById(R.id.psw)).setText("");
				((EditText) findViewById(R.id.psw1)).setText("");
				((EditText) findViewById(R.id.tel)).requestFocus();

			}
		} else {
			Toast.makeText(ZhuceActivity.this, "请将注册信息输入完整！！！",
					Toast.LENGTH_LONG).show();
		}
		return false;
	}

	private boolean loguppro(String tel, String psw) {
		// TODO Auto-generated method stub
		return true;
	}
}
