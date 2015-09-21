package cn.cas.sict.BusLocation_passenger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener {

	RadioGroup rGroup;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rGroup = (RadioGroup) findViewById(R.id.tab_menu);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_content, new BusLocationFragment()).commit();
		rGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.rg_bus_location:
			Log.i("aaa","rg_bus_location");

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new BusLocationFragment())
					.commit();
			break;
		case R.id.rg_bus:
			Log.i("aaa","rg_bus");
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new BusFragment()).commit();
			break;
		case R.id.rg_poi:
			Log.i("aaa","rg_poi");

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new PoiFragment()).commit();
			break;
		case R.id.rg_book:
			Log.i("aaa","rg_book");

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, new BookFragment()).commit();
			break;
		default:
			break;
		}

	}
}
