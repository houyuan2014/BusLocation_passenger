package cn.cas.sict.BusLocation_passenger;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import cn.cas.sict.utils.HttpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Button;

public class FirstActivity extends Activity {
	SharedPreferences sP;
	String phone, passwd, routeNum;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);

		readDataToUser();
		System.out.println(user.toString());
		goToLogin();

	}

	private void readDataToUser() {
		user = new User();
		sP = getSharedPreferences(Values.SP, Context.MODE_PRIVATE);
		String phone = sP.getString("phone", "");
		String passwd = sP.getString("passwd", "");
		String name = sP.getString("name", "");
		int routeNum = sP.getInt("route", 1);
		String routePhone = sP.getString("routephone", "");
		String routeName = sP.getString("routename", "");
		boolean isRemind = sP.getBoolean("remind", true);
		float remindDistance = sP.getFloat("reminddistance", 3000);
		user.setPhone(phone);
		user.setPasswd(passwd);
		user.setName(name);
		user.setRouteNum(routeNum);
		user.setRoutePhone(routePhone);
		user.setRouteName(routeName);
		user.setRemind(isRemind);
		user.setRemindDistance(remindDistance);
	}

	private void goToMain() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("phone", phone);
		m.put("pass", passwd);
		m.put("route", routeNum);
		try {
			String str = HttpUtil.sendPost("", m);
			JSONObject js = new JSONObject(str);
			if (js.get("register").equals("success")) {
				String routePhone = js.getString("busphone");
				user.setRoutePhone(routePhone);
				Intent in = new Intent(FirstActivity.this, MainActivity.class);
				startActivity(in);
				finish();
			} else {
				goToLogin();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void goToLogin() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					Intent in = new Intent();
					in.setClass(FirstActivity.this, LoginActivity.class);
					in.putExtra("user", user);
					startActivity(in);
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
		});
		t.start();
	}

}
