package cn.cas.sict.BusLocation_passenger;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

public class RemindActivity extends Activity {
	SharedPreferences sharedpreferences;
	SharedPreferences.Editor editor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind);
//		final NumberPicker meter = (NumberPicker) findViewById(R.id.meter);
//		final NumberPicker kilo = (NumberPicker) findViewById(R.id.kilo);
//		kilo.setMinValue(1);
//		kilo.setMaxValue(4);
//		kilo.setValue(0);
//		meter.setMinValue(0);
//		meter.setMaxValue(9);
//		meter.setValue(5);
//		final Button yes = (Button) findViewById(R.id.yes);
//		yes.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				yes.setText((yes.getText().toString() == "ÊÇ") ? "·ñ" : "ÊÇ");
//				if (yes.getText().toString() == "·ñ") {
//					kilo.setEnabled(true);
//					meter.setEnabled(true);
//
//				} else {
//					kilo.setEnabled(false);
//					meter.setEnabled(false);
//
//				}
//
//			}
//
//		});
//		// editor.putInt("distance", km+100*m).commit();
//		meter.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//
//			@Override
//			public void onValueChange(NumberPicker picker, int oldVal,
//					int newVal) {
//				// TODO Auto-generated method stub
//				// m=meter.getValue();
//				// editor.putInt("m_distance", m).commit();
//				meter.setEnabled(false);
//			}
//
//		});
//		kilo.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//
//			@Override
//			public void onValueChange(NumberPicker picker, int oldVal,
//					int newVal) {
//				// TODO Auto-generated method stub
//				// km=kilo.getValue();
//				// editor.putInt("km_distance", km).commit();
//				kilo.setEnabled(false);
//			}
//		});
//
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.remind, menu);
	// return true;
	// }

}
