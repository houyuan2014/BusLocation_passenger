package cn.cas.sict.BusLocation_passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener {

	private RadioGroup rg_tab_menu;
	private Intent intent;
	private User user;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		user = (User) getIntent().getSerializableExtra("user");
		rg_tab_menu = (RadioGroup) findViewById(R.id.tab_menu);
		rg_tab_menu.setOnCheckedChangeListener(this);
		intent = new Intent(this, MyService.class);
		intent.putExtra("user", user);
		startService(intent);
		goToLocationFragment();

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.rg_bus_location:
			goToLocationFragment();
			break;
		case R.id.rg_book:
			goToBookFragment();
			break;
		case R.id.rg_me:
			goToSetFragment();
		default:
			break;
		}

	}

	public void saveUserData() {
		SharedPreferences sP = getSharedPreferences(Values.SP_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sP.edit();
		editor.putString("phone", user.getPhone())
				.putString("passwd", user.getPasswd())
				.putString("name", user.getName())
				.putInt("route", user.getRouteNum())
				.putString("routephone", user.getRoutePhone())
				.putBoolean("remind", user.getIsRemind())
				.putFloat("reminddistance", user.getRemindDistance()).commit();

	}

	private void goToLocationFragment() {
		setTitle("定位");
		LocationFragment l = new LocationFragment();
		Bundle b = new Bundle();
		b.putSerializable("user", user);
		l.setArguments(b);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_content, l).commit();
	}

	private void goToBookFragment() {
		setTitle("定位");
		BookFragment fra = new BookFragment();
		Bundle b = new Bundle();
		b.putSerializable("user", user);
		fra.setArguments(b);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_content, fra).commit();
	}

	private void goToSetFragment() {
		setTitle("我");
		SetFragment fra = new SetFragment();
		Bundle b = new Bundle();
		b.putSerializable("user", user);
		fra.setArguments(b);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_content, fra).commit();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		saveUserData();
		stopService(intent);
	}
}
