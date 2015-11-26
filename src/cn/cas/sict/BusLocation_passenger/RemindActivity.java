package cn.cas.sict.BusLocation_passenger;

import cn.cas.sict.utils.User;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class RemindActivity extends Activity implements
		OnCheckedChangeListener, OnSeekBarChangeListener {
	private boolean isRemind;
	private int remindDistance;
	private SeekBar seekbar;
	private Switch switcher;
	private TextView tv_remindDistance;
	private ActionBar ab;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind);
		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		// ！务必先初始化再操作
		user = User.getUser();
		remindDistance = (int) user.getRemindDistance();
		isRemind = user.getIsRemind();
		switcher = (Switch) findViewById(R.id.switcher);
		seekbar = (SeekBar) findViewById(R.id.seekbar_distance);
		tv_remindDistance = (TextView) findViewById(R.id.tv_reminddistance);
		// ！务必先初始化再操作
		switcher.setOnCheckedChangeListener(this);
		seekbar.setOnSeekBarChangeListener(this);
		seekbar.setProgress(remindDistance / 500);
		switcher.setChecked(isRemind);
		tv_remindDistance.setText(remindDistance + "米");
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		if (isChecked) {
			isRemind = true;
			seekbar.setEnabled(true);
		} else {
			isRemind = false;
			seekbar.setEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.remind, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save_remind:
			user.setRemind(isRemind);
			user.setRemindDistance(remindDistance);
			finish();
			break;
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		remindDistance = progress * 500;
		tv_remindDistance.setText(remindDistance + "米");
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}

}
