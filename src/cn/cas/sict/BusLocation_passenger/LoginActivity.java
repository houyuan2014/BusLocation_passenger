package cn.cas.sict.BusLocation_passenger;

import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.LocationManagerProxy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements AMapLocalWeatherListener {
	EditText tel, psw;
	TextView weather;
	String tel1, psw1;
	SharedPreferences sharedpreferences;
	SharedPreferences.Editor editor;
	LocationManagerProxy locManagerProxy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		locManagerProxy = LocationManagerProxy.getInstance(this);
		locManagerProxy.requestWeatherUpdates(
				LocationManagerProxy.WEATHER_TYPE_LIVE, this);
		sharedpreferences = getSharedPreferences("userconfig", MODE_PRIVATE);
		editor = sharedpreferences.edit();
		editor.putBoolean("hasvibrate", false).commit(); // 必须要commit()才能生效
		weather = (TextView) findViewById(R.id.tv_weather);
		Button zhuce = (Button) findViewById(R.id.bt_logup);
		zhuce.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						ZhuceActivity.class);
				startActivity(intent);

			}
		});

		tel = (EditText) findViewById(R.id.tel);
		psw = (EditText) findViewById(R.id.psw);
		tel.setText(sharedpreferences.getString("phone", ""));
		psw.setText(sharedpreferences.getString("passwd", ""));
		Button denglu = (Button) findViewById(R.id.bt_login);
		denglu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String tel1 = tel.getText().toString();
				String psw1 = psw.getText().toString();
				if (validate(tel1, psw1)) {
					if (loginpro(tel1, psw1)) {
						Intent intent1 = new Intent(LoginActivity.this,
								MainActivity.class);
						startActivity(intent1);
						finish();// 启动新的activity后要关闭登录activity，否则用户按返回键将返回登录activity
					}

				}

			}

		});
	}

	/**
	 * 验证输入的手机号/密码是否有效
	 * 
	 * @return
	 */
	private boolean validate(String tel, String passwd) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 请求登录
	 * 
	 * @param tel1
	 * @param psw1
	 * @return
	 */
	private boolean loginpro(String tel1, String psw1) {
		// TODO Auto-generated method stub
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
					+ localWeatherLive.getTemperature() + "℃  "
					+ localWeatherLive.getWindDir() + "风  "
					+ localWeatherLive.getWindPower() + "级");
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locManagerProxy.destroy();
	}

}
