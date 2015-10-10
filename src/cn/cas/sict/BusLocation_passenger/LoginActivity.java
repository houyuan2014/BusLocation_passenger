package cn.cas.sict.BusLocation_passenger;

import cn.cas.sict.utils.SPUtil;

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
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends Activity implements
		AMapLocalWeatherListener, OnClickListener {
	EditText tel, psw;
	TextView weather;
	String tel1, psw1;
	SharedPreferences sP;
	SharedPreferences.Editor editor;
	LocationManagerProxy locManagerProxy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		locManagerProxy = LocationManagerProxy.getInstance(this);
		locManagerProxy.requestWeatherUpdates(
				LocationManagerProxy.WEATHER_TYPE_LIVE, this);
		sP = getSharedPreferences("userconfig", MODE_PRIVATE);
		editor = sP.edit();
		editor.putBoolean("hasvibrate", false).commit(); // ����Ҫcommit()������Ч
		tel = (EditText) findViewById(R.id.tel);
		psw = (EditText) findViewById(R.id.psw);
		tel.setText(sP.getString("phone", ""));
		psw.setText(sP.getString("passwd", ""));
		weather = (TextView) findViewById(R.id.tv_weather);
		Button denglu = (Button) findViewById(R.id.bt_login);
		Button zhuce = (Button) findViewById(R.id.bt_logup);
		Log.i("sP", sP.getAll() + "");
		zhuce.setOnClickListener(this);
		denglu.setOnClickListener(this);
	}

	/**
	 * ��֤������ֻ���/�����Ƿ���Ч
	 * 
	 * @return
	 */
	private boolean validate(String tel, String passwd) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * �����¼
	 * 
	 * @param tel1
	 * @param psw1
	 * @return
	 */
	private boolean loginpro(String tel1, String psw1) {
		// TODO Auto-generated method stub

		// HTTP����

		if (!tel1.equals(sP.getString(SPUtil.SP_USERPHONE, ""))) {
			editor.putString(SPUtil.SP_USERPHONE, tel1)
					.putString(SPUtil.SP_USERPASSWD, psw1)
					.putString(SPUtil.SP_USERNAME, "")
					.putInt(SPUtil.SP_ROUTENUM, 0)
					.putString(SPUtil.SP_ROUTENAME, "").commit();
		}
		return true;
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
			weather.setText(localWeatherLive.getCity() + "  "
					+ localWeatherLive.getWeather() + "  "
					+ localWeatherLive.getTemperature() + "��  "
					+ localWeatherLive.getWindDir() + "��  "
					+ localWeatherLive.getWindPower() + "��");
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locManagerProxy.destroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_logup:
			Intent intent = new Intent(LoginActivity.this, ZhuceActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_login:
			String tel1 = tel.getText().toString();
			String psw1 = psw.getText().toString();
			if (validate(tel1, psw1)) {
				if (loginpro(tel1, psw1)) {
					Intent intent1 = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent1);
					finish();// �����µ�activity��Ҫ�رյ�¼activity�������û������ؼ������ص�¼activity
				}
			}
			break;
		}

	}

}
