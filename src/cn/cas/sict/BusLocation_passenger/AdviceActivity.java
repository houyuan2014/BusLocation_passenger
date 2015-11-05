package cn.cas.sict.BusLocation_passenger;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdviceActivity extends Activity {
	private String yijian;
	private EditText advice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice);
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
		// TODO Auto-generated method stub

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
		return super.onOptionsItemSelected(item);
	}

}
