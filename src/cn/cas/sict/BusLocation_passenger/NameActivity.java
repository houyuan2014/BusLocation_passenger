package cn.cas.sict.BusLocation_passenger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class NameActivity extends Activity {
	private EditText et_Name;
	private String name;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name);
		user = (User) getIntent().getSerializableExtra("user");
		et_Name = (EditText) findViewById(R.id.name);
		et_Name.setText(user.getName());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.name, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		name = et_Name.getText().toString().trim();
		if (name.equals(user.getName())) {
			return super.onOptionsItemSelected(item);
		} else {
			user.setName(name);
		}
		return super.onOptionsItemSelected(item);
	}
}
