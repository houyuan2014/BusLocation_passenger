package cn.cas.sict.BusLocation_passenger;

import cn.cas.sict.utils.ToastUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class ZhuceActivity extends Activity {
	Button btZhuCe;
	SharedPreferences sharedpreferences;
	SharedPreferences.Editor editor;
	String tel, psw, psw1;
	RadioGroup rgRoute;
	int routeNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuce);
		sharedpreferences = getSharedPreferences("userconfig", MODE_PRIVATE);
		editor = sharedpreferences.edit();
		rgRoute = (RadioGroup) findViewById(R.id.rg_logup_route);
		rgRoute.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_route1:
					routeNum = 1;
					break;
				case R.id.rb_route2:
					routeNum = 2;
					break;
				case R.id.rb_route3:
					routeNum = 3;
					break;
				case R.id.rb_route4:
					routeNum = 4;
					break;
				}
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
						editor.putString("phone", tel).putString("passwd", psw)
								.putInt("route", routeNum).commit();
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
