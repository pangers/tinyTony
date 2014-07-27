package com.pangers.tinyTony;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pangers.DataService.ToDoDatabase;

public class DetailedTask extends Activity {
	TextView name, time, importance, taskDate, longTimeRemaining;
	int position;
	TaskData taskData;
	long timeDifference, seconds, minutes, hours, days;
	CountDownTimer countDown;
	int refreshInterval = 1000;
	int msInASecond = 1000;
	int msInAMinute = msInASecond * 60;
	int msInAnHour = msInAMinute * 60;
	int msInADay = msInAnHour * 24;
	String longTimeRemainingString;
	ToDoDatabase database = new ToDoDatabase(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailedtask);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);

		name = (TextView) findViewById(R.id.detailedName);
		time = (TextView) findViewById(R.id.detailedTimeRemaining);
		taskDate = (TextView) findViewById(R.id.detailedTaskDate);
		longTimeRemaining = (TextView) findViewById(R.id.detailedLongTimeRemaining);
		// Get position selected, parsed from TaskListFragment->onClickSetup()
		position = getIntent().getExtras().getInt("position");
		taskData = database.getTask(position);

		name.setText(taskData.getTask());

		// Set border colour of the top bar (background)
		View barView = findViewById(R.id.detailedElement);
		View bottomView = findViewById(R.id.detailedDetails);
		View innerView = findViewById(R.id.detailedInnerDetails);
		switch (taskData.getImportance()) {
		case "0":
			barView.setBackgroundResource(R.color.blue);
			bottomView.setBackgroundResource(R.color.blue);
			innerView.setBackgroundResource(R.color.lightblue);
			break;
		case "1":
			barView.setBackgroundResource(R.color.yellow);
			bottomView.setBackgroundResource(R.color.yellow);
			innerView.setBackgroundResource(R.color.lightyellow);
			break;
		case "2":
			barView.setBackgroundResource(R.color.red);
			bottomView.setBackgroundResource(R.color.red);
			innerView.setBackgroundResource(R.color.lightred);
			break;
		}

		// Count Down Timer
		timeDifference = Long.valueOf(taskData.getTime())
				- System.currentTimeMillis();
		countDown = new CountDownTimer(timeDifference, refreshInterval) {
			public void onTick(long millisUntilFinished) {
				days = millisUntilFinished / msInADay;
				hours = (millisUntilFinished / msInAnHour) % 24;
				minutes = (millisUntilFinished / msInAMinute) % 60;
				seconds = (millisUntilFinished / msInASecond) % 60;
				if (days < 1) {
					time.setText(hours + ":" + minutes + ":" + seconds);
				} else {
					time.setText(days + " days");
				}

				// Sets the time remaining as a setence
				longTimeRemainingString = "Time Remaining:\n";
				if (days > 0) {
					longTimeRemainingString = days + " days ";
				}
				if (hours > 0) {
					longTimeRemainingString = longTimeRemainingString + hours
							+ " hours ";
				}
				if (minutes > 0) {
					longTimeRemainingString = longTimeRemainingString + minutes
							+ " minutes ";
				}
				longTimeRemainingString = longTimeRemainingString + seconds
						+ " seconds";
				longTimeRemaining.setText(longTimeRemainingString);
			}

			public void onFinish() {
				time.setText("Time is up!");
			}
		};
		countDown.start();

		// Task Date - Convert stored linux time to dd/mm/yyyyy
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.parseLong(taskData.getTime()));
		taskDate.setText("Task Date: " + calendar.getTime().getDay() + "/"
				+ calendar.getTime().getMonth() + "/"
				+ String.valueOf(calendar.getTime().getYear() + 1900));

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detailedtaskactions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.deletedetailedtask:
			database.deleteNewTask(database.getTask(position));
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
