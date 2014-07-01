package com.pangers.tinyTony;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class taskListAdapter extends ArrayAdapter<taskData> {

	private final Context context;
	private final ArrayList<taskData> taskData;

	public taskListAdapter(Context context, ArrayList<taskData> taskData) {
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

		//Supply what the row looks like
		rowView = inflater.inflate(R.layout.tasklistitem, parent, false);

		// Get task name and time left
		TextView taskview = (TextView) rowView.findViewById(R.id.taskname);
		TextView timeview = (TextView) rowView.findViewById(R.id.timeleft);
		
		// Fill the rowView with data
		taskview.setText(taskData.get(position).getTask());
		timeview.setText(taskData.get(position).getTime());
		
		return rowView;

	}
}
