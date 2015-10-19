package cn.cas.sict.BusLocation_passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener {

	private RadioGroup rg_tab_menu;
	Intent intent;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rg_tab_menu = (RadioGroup) findViewById(R.id.tab_menu);
		intent = new Intent(this, MyService.class);
		startService(intent);
		init();
		Log.i("circle", "act oncreate");

	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("定位");
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_content, new LocationFragment()).commit();
		rg_tab_menu.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("circle", "act onresume");

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("circle", "act onpause");

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(intent);
		Log.i("circle", "act ondestroy");

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.rg_bus_location:
			Log.i("aaa", "rg_bus_location");
			setTitle("定位");
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new LocationFragment())
					.commit();
			break;
		case R.id.rg_book:
			Log.i("aaa", "rg_book");
			setTitle("预订");
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new BookFragment()).commit();
			break;
		case R.id.rg_me:
			setTitle("我");
			Log.i("aaa", "rg_me");
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new SetFragment()).commit();
		default:
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_bus_search:
			setTitle("test1");
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new Test1Fragment())
					.commit();
			break;
		case R.id.menu_poi_search:
			setTitle("test2");
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new Test2Fragment())
					.commit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
