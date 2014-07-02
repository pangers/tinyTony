package com.pangers.tinyTony;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

public class taskListFragment extends Fragment implements
		AdapterView.OnItemClickListener, newTaskFragment.newTaskDialogListener {

	private TextView titleView;
	private ListView taskList;
	private taskListAdapter adapter;

	ArrayList<taskData> tasks = new ArrayList<taskData>();

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
		adapter = new taskListAdapter(getActivity(), tasks);
		// Create the ListView
		taskList.setAdapter(adapter);

	}

	private ArrayList<taskData> generateData(String newtask,
			String timeremaining) {
		tasks.add(new taskData(newtask, timeremaining));
		return tasks;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			return true;

		case R.id.add:
			showNewTaskDialog();
			return true;

		case R.id.settings:

			return true;

		case R.id.about:

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
		newTaskFragment dialog = new newTaskFragment();
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
		String timeremainingString = timeremaining.getText().toString();

		generateData(taskString, timeremainingString);

	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().dismiss();
	}
}
