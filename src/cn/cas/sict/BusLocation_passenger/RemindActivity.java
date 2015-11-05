package cn.cas.sict.BusLocation_passenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class RemindActivity extends Activity implements
		OnCheckedChangeListener, OnRatingBarChangeListener {
	private boolean isRemind;
	private float remindDistance;
	private RatingBar ra_distance;
	private Switch select;
	private TextView tv_remindDistance;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind);
		user = (User) getIntent().getSerializableExtra("user");
		select = (Switch) findViewById(R.id.select);
		select.setChecked(user.getIsRemind());
		select.setOnCheckedChangeListener(this);
		ra_distance = (RatingBar) findViewById(R.id.distance);
		ra_distance.setEnabled(user.getIsRemind());
		ra_distance.setOnRatingBarChangeListener(this);
		tv_remindDistance = (TextView) findViewById(R.id.tv_reminddistance);
		tv_remindDistance.setText(user.getRemindDistance() + "รื");

	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		remindDistance = ra_distance.getRating() * 1000;
		tv_remindDistance.setText(remindDistance + "รื");
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		if (isChecked) {
			isRemind = true;
			ra_distance.setEnabled(true);
		} else {
			isRemind = false;
			ra_distance.setEnabled(false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isRemind != user.getIsRemind()) {
			user.setRemind(isRemind);
		}
		if (remindDistance != user.getRemindDistance()) {
			user.setRemindDistance(remindDistance);
		}
	}

}
