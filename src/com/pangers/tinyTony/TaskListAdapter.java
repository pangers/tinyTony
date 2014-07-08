package com.pangers.tinyTony;

import java.util.ArrayList;

import com.pangers.DataService.ToDoDatabase;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskListAdapter extends ArrayAdapter<TaskData> {

	private final Context context;
	private final ArrayList<TaskData> taskData;
	private long timeDifference, minutes, seconds;;
	private int refreshInterval = 1000;
	private int msInAMinute = 60000;
	private int msInASecond = 1000;
	private CountDownTimer countDown;

	public TaskListAdapter(Context context, ArrayList<TaskData> taskData) {
		super(context, R.layout.tasklistitem, taskData);

		this.context = context;
		this.taskData = taskData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Create Layout inflater - inflates xml and gives us a view
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Get rowView from inflater
		View rowView = null;

		// Supply what the row looks like
		rowView = inflater.inflate(R.layout.tasklistitem, parent, false);

		// Get task name and time left
		TextView taskview = (TextView) rowView.findViewById(R.id.taskname);
		final TextView timeview = (TextView) rowView
				.findViewById(R.id.timeleft);

		// Fill the rowView with data
		taskview.setText(taskData.get(position).getTask());

		// Set border colour (background)
		// blue colour
		rowView.setBackgroundColor(0xFF0080ff);

		// Count Down Timer
		timeDifference = Long.valueOf(taskData.get(position).getTime())
				- System.currentTimeMillis();
		countDown = new CountDownTimer(timeDifference, refreshInterval) {
			public void onTick(long millisUntilFinished) {
				minutes = millisUntilFinished / msInAMinute;
				seconds = (millisUntilFinished / msInASecond) % 60;
				timeview.setText(String.valueOf(minutes) + ":"
						+ String.valueOf(seconds));
			}

			public void onFinish() {
				timeview.setText("Time is up!");
			}
		};
		countDown.start();

		return rowView;
	}
}
