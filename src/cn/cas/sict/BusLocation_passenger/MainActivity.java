package cn.cas.sict.BusLocation_passenger;

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

	RadioGroup rg_tab_menu;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rg_tab_menu = (RadioGroup) findViewById(R.id.tab_menu);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_content, new BusLocationFragment()).commit();
		rg_tab_menu.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.rg_bus_location:
			Log.i("aaa", "rg_bus_location");

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new BusLocationFragment())
					.commit();
			break;
		case R.id.rg_book:
			Log.i("aaa", "rg_book");

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new BookFragment()).commit();
			break;
		case R.id.rg_me:
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
			Log.i("aaa", "rg_bus");
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new BusSearchFragment())
					.commit();
			break;
		case R.id.menu_poi_search:
			Log.i("aaa", "rg_poi");
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new PoiSearchFragment())
					.commit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
