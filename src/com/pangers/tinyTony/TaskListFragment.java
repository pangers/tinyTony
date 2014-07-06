package com.pangers.tinyTony;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListFragment extends Fragment implements
		AdapterView.OnItemClickListener, NewTaskFragment.newTaskDialogListener {

	private TextView titleView;
	private ListView taskList;
	private TaskListAdapter adapter;
	private int msInAMinute = 60000;

	public final static String NAV_DRAWER_POS = "navDrawerPos";

	ArrayList<TaskData> tasks = new ArrayList<TaskData>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Dont destroy fragment on configuration changes
		setRetainInstance(true);
		// Inflate the fragment
		View result = inflater.inflate(R.layout.taskfragment, container, false);
		return result;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Turn on action bar
		setHasOptionsMenu(true);
		// Find views
		titleView = (TextView) getActivity().findViewById(R.id.testtitle);
		taskList = (ListView) getActivity().findViewById(R.id.tasklist);
		// Adapter to create task list
		adapter = new TaskListAdapter(getActivity(), tasks);
		// Create the ListView
		taskList.setAdapter(adapter);

	}

	private ArrayList<TaskData> generateData(String newtask,
			String timeremaining) {
		tasks.add(new TaskData(newtask, timeremaining));
		return tasks;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.addtaskactions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.add:
			showNewTaskDialog();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	public void showNewTaskDialog() {
		NewTaskFragment dialog = new NewTaskFragment();
		dialog.setTargetFragment(this, 0);
		dialog.show(getFragmentManager(), "NewTaskDialog");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		EditText task = (EditText) dialog.getDialog()
				.findViewById(R.id.newtask);
		EditText timeremaining = (EditText) dialog.getDialog().findViewById(
				R.id.timeremaining);

		String taskString = task.getText().toString();
		String timeremainingString = String.valueOf(Integer
				.parseInt(timeremaining.getText().toString())
				* msInAMinute
				+ System.currentTimeMillis());

		generateData(taskString, timeremainingString);
		((TaskListAdapter) taskList.getAdapter()).notifyDataSetChanged();

	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().dismiss();
	}
}
