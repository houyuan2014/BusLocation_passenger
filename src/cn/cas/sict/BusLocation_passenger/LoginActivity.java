package cn.cas.sict.BusLocation_passenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button zhuce = (Button) findViewById(R.id.bt_logup);
		zhuce.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						ZhuceActivity.class);
				startActivity(intent);

			}
		});

		Intent intent = getIntent();
		EditText tel = (EditText) findViewById(R.id.tel);
		EditText psw = (EditText) findViewById(R.id.psw);
		String tel1 = intent.getStringExtra("com.xixiwork.PHONE");
		String psw1 = intent.getStringExtra("com.xixiwork.PASSWORD");
		tel.setText(tel1);
		psw.setText(psw1);
		Button denglu = (Button) findViewById(R.id.bt_login);

		denglu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent1 = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent1);
				finish();

			}
		});
	}

}
