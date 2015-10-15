package cn.cas.sict.BusLocation_passenger;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdviceActivity extends Activity {
	Button tijiao;
	String yijian;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice);
		final EditText advice = (EditText) findViewById(R.id.advice);
		tijiao = (Button) findViewById(R.id.commit);
		tijiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				yijian = advice.getText().toString();
				if (!"".equals(yijian)) {
					advice.setText("");

					// HTTP«Î«Û

					finish();
				} else {
					Toast.makeText(AdviceActivity.this, "«Î ‰»Îƒ⁄»›",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.advice, menu);
	// return true;
	// }

}
