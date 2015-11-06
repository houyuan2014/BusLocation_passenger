package cn.cas.sict.BusLocation_passenger;

import android.app.Activity;
import android.os.Bundle;
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
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind);
		//！务必先初始化再操作
		user = (User) getIntent().getSerializableExtra("user");
		switcher = (Switch) findViewById(R.id.switcher);
		seekbar = (SeekBar) findViewById(R.id.seekbar_distance);
		tv_remindDistance = (TextView) findViewById(R.id.tv_reminddistance);
		//！务必先初始化再操作
		tv_remindDistance.setText(user.getRemindDistance() + "米");
		switcher.setChecked(user.getIsRemind());
		seekbar.setEnabled(user.getIsRemind());
		switcher.setOnCheckedChangeListener(this);
		seekbar.setOnSeekBarChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		if (isChecked) {
			isRemind = true;
			seekbar.setEnabled(true);
		} else {
			isRemind = false;
			remindDistance = seekbar.getProgress() * 500;
			seekbar.setEnabled(false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		user.setRemind(isRemind);
		user.setRemindDistance(remindDistance);
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
