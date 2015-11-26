package cn.cas.sict.BusLocation_passenger;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AdviceActivity extends Activity {
	private String yijian;
	private EditText advice;
	private ActionBar ab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice);
		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		advice = (EditText) findViewById(R.id.advice);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.advice, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.commit:
			yijian = advice.getText().toString();
			if (!"".equals(yijian)) {
				Map<String, String> m = new HashMap<String, String>();
				m.put("content", yijian);

				// HTTP请求

				Toast.makeText(AdviceActivity.this, "发送成功", Toast.LENGTH_SHORT)
						.show();
				finish();
			} else {
				Toast.makeText(AdviceActivity.this, "请输入内容", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case android.R.id.home:
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
