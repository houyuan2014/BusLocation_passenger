package cn.cas.sict.BusLocation_passenger;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.widget.FrameLayout;
import cn.cas.sict.domain.User;
import cn.cas.sict.utils.HttpUtil;
import cn.cas.sict.utils.Globals;

public class FirstActivity extends Activity {
	SharedPreferences sP;
	String phone, passwd, routeNum;
	User user;
	ViewPager viewpager;
	View welcome1, welcome2, welcome3;
	FrameLayout welcome4;
	View welcome4_button;
	ArrayList<View> viewContainter = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		User.initUser(getSharedPreferences(Globals.SP_NAME,
				Context.MODE_PRIVATE));
		user = User.getUser();
		System.out.println(user);
		if (user.getTel().equals("")) {
			welcome();
		} else {
			try {
				JSONObject json = new JSONObject();
				json.put("tel", user.getTel());
				String url = "FindAllBCServlet";
				String str = HttpUtil.sendPost(url, json);
				user.setJsonRouteList(str);
				//System.out.println(user);
				Intent in = new Intent(FirstActivity.this, MainActivity.class);
				startActivity(in);
				finish();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void welcome() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		welcome1 = LayoutInflater.from(this).inflate(R.layout.first_welcome1,
				null);
		welcome2 = LayoutInflater.from(this).inflate(R.layout.first_welcome2,
				null);
		welcome3 = LayoutInflater.from(this).inflate(R.layout.first_welcome3,
				null);
		welcome4 = (FrameLayout) LayoutInflater.from(this).inflate(
				R.layout.first_welcome4, null);
		welcome4_button = (View) welcome4.findViewById(R.id.dong_button);
		welcome4_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(FirstActivity.this,
						RegisterActivity.class);
				startActivity(in);
				finish();
			}
		});
		viewContainter.add(welcome1);
		viewContainter.add(welcome2);
		viewContainter.add(welcome3);
		viewContainter.add(welcome4);

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

}
