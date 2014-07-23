package com.pangers.tinyTony;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.pangers.DataService.ToDoDatabase;

public class DetailedTask extends Activity {
	TextView name, time, importance;
	int position;
	TaskData taskData;
	long timeDifference, seconds, minutes;
	CountDownTimer countDown;
	int refreshInterval = 1000;
	int msInAMinute = 60000;
	int msInASecond = 1000;
	ToDoDatabase database = new ToDoDatabase(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailedtask);
		position = getIntent().getExtras().getInt("position");

		name = (TextView) findViewById(R.id.detailedName);
		time = (TextView) findViewById(R.id.detailedTime);
		importance = (TextView) findViewById(R.id.detailedImportance);

		taskData = database.getTask(position);
		name.setText(taskData.getTask());
		importance.setText(taskData.getImportance());

		// Count Down Timer
		timeDifference = Long.valueOf(taskData.getTime())
				- System.currentTimeMillis();
		countDown = new CountDownTimer(timeDifference, refreshInterval) {
			public void onTick(long millisUntilFinished) {
				minutes = millisUntilFinished / msInAMinute;
				seconds = (millisUntilFinished / msInASecond) % 60;
				time.setText(String.valueOf(minutes) + ":"
						+ String.valueOf(seconds));
			}

			public void onFinish() {
				time.setText("Time is up!");
			}
		};
		countDown.start();
	}
}
