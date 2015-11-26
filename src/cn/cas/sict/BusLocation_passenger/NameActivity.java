package cn.cas.sict.BusLocation_passenger;

import cn.cas.sict.utils.User;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class NameActivity extends Activity {
	private EditText et_Name;
	private String name;
	private ActionBar ab;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name);
		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		user = User.getUser();
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
		switch (item.getItemId()) {
		case R.id.menu_save_name:
			name = et_Name.getText().toString().trim();
			if (name.equals(user.getName())) {
				finish();
			} else {
				user.setName(name);
				finish();
			}
			break;
		case android.R.id.home:
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
