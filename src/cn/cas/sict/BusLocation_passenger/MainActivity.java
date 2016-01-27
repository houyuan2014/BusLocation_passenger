package cn.cas.sict.BusLocation_passenger;

import cn.cas.sict.domain.User;
import cn.cas.sict.service.MyService;
import cn.cas.sict.utils.Globals;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener {

	private RadioGroup rg_tab_menu;
	private Intent intent;
	private User user;
	private FragmentManager fm;
	private LocationFragment locationFragment;
	private BookFragment bookFragment;
	private SetFragment setFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//User.initUser(getSharedPreferences(Globals.SP_NAME,Context.MODE_PRIVATE));
		user = User.getUser();
		fm = getSupportFragmentManager();
		rg_tab_menu = (RadioGroup) findViewById(R.id.tab_menu);
		rg_tab_menu.setOnCheckedChangeListener(this);
		intent = new Intent(this, MyService.class);
		startService(intent);
		setDefaultFragment();

	}

	private void setDefaultFragment() {
		onCheckedChanged(rg_tab_menu, R.id.rg_bus_location);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		FragmentTransaction ft = fm.beginTransaction();
		if (locationFragment != null) {
			ft.hide(locationFragment);
		}
		if (bookFragment != null) {
			ft.hide(bookFragment);
		}
		if (setFragment != null) {
			ft.hide(setFragment);
		}
		switch (arg1) {
		case R.id.rg_bus_location:
			setTitle("定位");
			if (locationFragment == null) {
				locationFragment = new LocationFragment();
				ft.add(R.id.main_content, locationFragment);
			} else {
				ft.show(locationFragment);
			}
			break;
		case R.id.rg_book:
			setTitle("预定");
			if (bookFragment == null) {
				bookFragment = new BookFragment();
				ft.add(R.id.main_content, bookFragment);
			} else {
				ft.show(bookFragment);
			}
			break;
		case R.id.rg_me:
			setTitle("我");
			if (setFragment == null) {
				setFragment = new SetFragment();
				ft.add(R.id.main_content, setFragment);
			} else {
				ft.show(setFragment);
			}
		default:
			break;
		}
		ft.commit();
	}
	
	
	public void saveUserData() {
		SharedPreferences sP = getSharedPreferences(Globals.SP_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sP.edit();
		user.save(editor);
		

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		saveUserData();
		stopService(intent);
	}
}
