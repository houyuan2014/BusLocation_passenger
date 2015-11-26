package cn.cas.sict.BusLocation_passenger;

import cn.cas.sict.utils.SaleUtil;
import cn.cas.sict.utils.User;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ZhuceActivity extends Activity {
	private Button btZhuCe;
	private String tel, psw, psw1;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuce);
		user = User.getUser();
		Spinner spinner = (Spinner) findViewById(R.id.route);
		btZhuCe = (Button) findViewById(R.id.bt_logup);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String routeName = arg0.getItemAtPosition(arg2).toString();
				int routeNum = arg2 + 1;
				user.setRouteNum(routeNum);
				user.setRouteName(routeName);
				Toast.makeText(ZhuceActivity.this,
						"routeNum" + routeNum + routeName, Toast.LENGTH_SHORT)
						.show();
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
						user.setPhone(tel);
						user.setPasswd(psw);

						finish();
					}
				}
			}
		});
	}

	private boolean validate(String tel, String psw, String psw1) {
		if (SaleUtil.isValidPhoneNumber(tel)) {
			if (!psw1.equals("")) {
				if (psw.equals(psw1)) {
					return true;
				} else {
					Toast.makeText(ZhuceActivity.this, "密码不一致，请重新输入！",
							Toast.LENGTH_LONG).show();
					((EditText) findViewById(R.id.psw)).setText("");
					((EditText) findViewById(R.id.psw1)).setText("");
				}
			} else {
				Toast.makeText(ZhuceActivity.this, "请将注册信息输入完整！！！",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(ZhuceActivity.this, "请输入11位手机号！！！",
					Toast.LENGTH_LONG).show();
			((EditText) findViewById(R.id.tel)).setText("");
			((EditText) findViewById(R.id.tel)).requestFocus();
		}
		return false;
	}

	private boolean loguppro(String tel, String psw) {
		return true;
	}
}
