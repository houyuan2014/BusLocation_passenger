package cn.cas.sict.BusLocation_passenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import cn.cas.sict.utils.HttpUtil;

public class FirstActivity extends Activity {
	SharedPreferences sP;
	String phone, passwd, routeNum;
	User user;
	ViewPager viewpager;
	// Button button;
	View chun, xia, qiu;
	FrameLayout dong;
	Button dong_button;
	ArrayList<View> viewContainter = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		readDataToUser();
		if(user.getPhone().equals("")){
			goToWelcome();
		}else{
			goToLogin();
		}

	}

	private void goToWelcome() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		chun = LayoutInflater.from(this).inflate(R.layout.first_chun, null);
		xia = LayoutInflater.from(this).inflate(R.layout.first_xia, null);
		qiu = LayoutInflater.from(this).inflate(R.layout.first_qiu, null);
		dong = (FrameLayout) LayoutInflater.from(this).inflate(
				R.layout.first_dong, null);
		dong_button = (Button) dong.findViewById(R.id.dong_button);
		dong_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToLogin();
			}
		});
		viewContainter.add(chun);
		viewContainter.add(xia);
		viewContainter.add(qiu);
		viewContainter.add(dong);

		viewpager.setAdapter(new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return viewContainter.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				((ViewPager) container).removeView(viewContainter.get(position));

			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				((ViewPager) container).addView(viewContainter.get(position));
				return viewContainter.get(position);

			}

			@Override
			public int getItemPosition(Object object) {
				// TODO Auto-generated method stub

				return super.getItemPosition(object);
			}

		});
	}

	private void readDataToUser() {
		user = new User();
		sP = getSharedPreferences(Values.SP_NAME, Context.MODE_PRIVATE);
		String phone = sP.getString("phone", "");
		String passwd = sP.getString("passwd", "");
		String name = sP.getString("name", "");
		int routeNum = sP.getInt("route", 1);
		String routePhone = sP.getString("routephone", "");
		String routeName = sP.getString("routename", "“ª∫≈œﬂ");
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
				}
			}
		});
		t.start();
	}

}
