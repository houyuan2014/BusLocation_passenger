package cn.cas.sict.BusLocation_passenger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.cas.sict.domain.User;
import cn.cas.sict.utils.Globals;
import cn.cas.sict.utils.HttpUtil;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.LocationManagerProxy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements
		AMapLocalWeatherListener, OnClickListener {
	private String username, tel, gender, email;
	private EditText et_tel, et_username, et_email;
	private RadioGroup rg;
	private TextView tv_weather;
	private LocationManagerProxy locManagerProxy;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		user = User.getUser();
		locManagerProxy = LocationManagerProxy.getInstance(this);
		locManagerProxy.requestWeatherUpdates(
				LocationManagerProxy.WEATHER_TYPE_LIVE, this);
		et_username = (EditText) findViewById(R.id.username);
		et_tel = (EditText) findViewById(R.id.tel);
		et_email = (EditText) findViewById(R.id.email);
		tv_weather = (TextView) findViewById(R.id.tv_weather);
		gender = "男";
		rg = (RadioGroup) findViewById(R.id.gender);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				gender = checkedId == R.id.male ? "男" : "女";
			}
		});
		Button dengJi = (Button) findViewById(R.id.bt_login);
		dengJi.setOnClickListener(this);
	}

	private void loginpro() {

		try {
			JSONObject json = new JSONObject();
			json.put("username", username);
			json.put("gender", gender);
			json.put("tel", tel);
			json.put("email", email);
			String url = "RegisterYGServlet";
			String str = HttpUtil.sendPost(url, json);
			System.out.println(str);
			JSONObject result = new JSONObject(str);
			if (result.getString("register").equals("success")) {
				Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
				JSONArray array = result.getJSONArray("routelist");
				JSONObject obj = array.getJSONObject(user.getRouteNum()-1);
				user.setRouteName(obj.getString("szlx"));
				user.setRoutePhone(obj.getString("cph"));
				user.setJsonRouteList(array.toString());
				System.out.println(user.toString());
				Intent in = new Intent(RegisterActivity.this,MainActivity.class);
				startActivity(in);
				finish();
			} else {
				Toast.makeText(this, result.getString("message"),
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		username = et_username.getText().toString().trim();
		tel = et_tel.getText().toString().trim();
		email = et_email.getText().toString().trim();

		if ("".equals(username)) {
			Toast.makeText(this, "请填写姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if ("".equals(tel)) {
			 Toast.makeText(this, "请填写手机号 ", Toast.LENGTH_SHORT).show();
			 return;
		}else if(!tel.matches("\\d{11}")){
			Toast.makeText(this, "手机号不合法 ", Toast.LENGTH_SHORT).show();
			 return;
		}
		if ( "".equals(email)) {
			 Toast.makeText(this, "请填写邮箱  ", Toast.LENGTH_SHORT).show();
			 return;
		}else if(!email.matches("\\w+@\\w+(\\.\\w+)+")){
			Toast.makeText(this, "邮箱不合法 ", Toast.LENGTH_SHORT).show();
			 return;
		}
		User user = User.getUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setTel(tel);
		user.setGender(gender);
		user.save(getSharedPreferences(Globals.SP_NAME, Context.MODE_PRIVATE)
				.edit());
		loginpro();
	}

}
