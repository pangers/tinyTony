package com.pangers.tinyTony;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pangers.DataService.ToDoDatabase;

public class DetailedTask extends Fragment {
	private TextView name, time, importance, taskDate, longTimeRemaining;
	private int position;
	private TaskData taskData;
	private long timeDifference, seconds, minutes, hours, days;
	private CountDownTimer countDown;
	private final int refreshInterval = 1000;
	private final int msInASecond = 1000;
	private final int msInAMinute = msInASecond * 60;
	private final int msInAnHour = msInAMinute * 60;
	private final int msInADay = msInAnHour * 24;
	private String longTimeRemainingString;
	private ToDoDatabase database;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Dont destroy fragment on configuration changes
		setRetainInstance(true);
		// Inflate the fragment
		View result = inflater.inflate(R.layout.detailedtask, container, false);
		return result;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Turn on action bar
		setHasOptionsMenu(true);

		name = (TextView) getActivity().findViewById(R.id.detailedName);
		time = (TextView) getActivity()
				.findViewById(R.id.detailedTimeRemaining);
		taskDate = (TextView) getActivity().findViewById(R.id.detailedTaskDate);
		longTimeRemaining = (TextView) getActivity().findViewById(
				R.id.detailedLongTimeRemaining);
		// Get position selected, parsed from TaskListFragment->onClickSetup()
		position = getArguments().getInt("POSITION");
		taskData = database.getTask(position);
		name.setText(taskData.getTask());

		// Set border colour of the top bar (background)
		View barView = getActivity().findViewById(R.id.detailedElement);
		View bottomView = getActivity().findViewById(R.id.detailedDetails);
		View innerView = getActivity().findViewById(R.id.detailedInnerDetails);

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

				// Sets the time remaining as a sentence
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

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.detailedtaskactions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().getSupportFragmentManager().popBackStack();
			return true;
		case R.id.deletedetailedtask:
			database.deleteNewTask(database.getTask(position));
			getActivity().getSupportFragmentManager().popBackStack();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setDataBase(ToDoDatabase database2) {
		database = database2;
	}
	
	
}
