package cn.cas.sict.BusLocation_passenger;

import cn.cas.sict.utils.SaleUtil;

import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.LocationManagerProxy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements
		AMapLocalWeatherListener, OnClickListener {
	private EditText et_phone, et_pass;
	private TextView tv_weather;
	private LocationManagerProxy locManagerProxy;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		locManagerProxy = LocationManagerProxy.getInstance(this);
		locManagerProxy.requestWeatherUpdates(
				LocationManagerProxy.WEATHER_TYPE_LIVE, this);
		user = (User) getIntent().getSerializableExtra("user");
		System.out.println("__>" + user.toString());
		et_phone = (EditText) findViewById(R.id.tel);
		et_pass = (EditText) findViewById(R.id.psw);
		tv_weather = (TextView) findViewById(R.id.tv_weather);
		Button denglu = (Button) findViewById(R.id.bt_login);
		Button zhuce = (Button) findViewById(R.id.bt_logup);
		zhuce.setOnClickListener(this);
		denglu.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		et_phone.setText(user.getPhone());
		et_pass.setText(user.getPasswd());
	}

	/**
	 * 验证输入的手机号/密码是否有效
	 * 
	 * @return
	 */
	private boolean validate(String tel, String passwd) {
		if (SaleUtil.isValidPhoneNumber(tel)) {
			if (!passwd.equals("")) {
				return true;
			} else {
				Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "手机号不合法", Toast.LENGTH_LONG).show();
		}
		return false;
	}

	private boolean loginpro(String phone, String pass) {
		// TODO Auto-generated method stub

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("phone", phone);
		// map.put("pass", pass);
		// String url = HttpUtil.BASE_URL + "JSON_Login.ashx";
		// JSONObject jsonObject;
		// try {
		// jsonObject = new JSONObject(HttpUtil.postRequest(url, map));
		// if (jsonObject.getString("login").equals("success")) {
		if (!phone.equals(user.getPhone())) {
			user.setPhone(phone);
			user.setPasswd(pass);
		}
		return true;
		// } else if (jsonObject.getString("login").equals("fail")) {
		// Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
		// return false;
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// return false;
	}

	@Override
	public void onWeatherForecaseSearched(AMapLocalWeatherForecast arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWeatherLiveSearched(AMapLocalWeatherLive localWeatherLive) {
		// TODO Auto-generated method stub
		if (localWeatherLive != null
				&& localWeatherLive.getAMapException().getErrorCode() == 0) {
			tv_weather.setText(localWeatherLive.getCity() + "  "
					+ localWeatherLive.getWeather() + "  "
					+ localWeatherLive.getTemperature() + "℃  "
					+ localWeatherLive.getWindDir() + "风  "
					+ localWeatherLive.getWindPower() + "级");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		locManagerProxy.destroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_logup:
			Intent intent = new Intent(LoginActivity.this, ZhuceActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
			break;
		case R.id.bt_login:
			String phone = et_phone.getText().toString();
			String pass = et_pass.getText().toString();
			if (validate(phone, pass)) {
				if (loginpro(phone, pass)) {
					Intent intent1 = new Intent(LoginActivity.this,
							MainActivity.class);
					intent1.putExtra("user", user);
					startActivity(intent1);
					finish();// 启动新的activity后要关闭登录activity，否则用户按返回键将返回登录activity
				}
			}
			break;
		}

	}

}
