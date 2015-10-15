package cn.cas.sict.BusLocation_passenger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NameActivity extends Activity {
	SharedPreferences sP;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name);
		sP = this.getSharedPreferences("userconfig", Context.MODE_PRIVATE);
		editor = sP.edit();
		final EditText name = (EditText) findViewById(R.id.name);
		Button save = (Button) findViewById(R.id.button1);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String name1 = name.getText().toString();
				editor.putString("name", name1).commit();// 下次登录才显示修改的姓名？？？
				finish();

			}

		});
	}

}
