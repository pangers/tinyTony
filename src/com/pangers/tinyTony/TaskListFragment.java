package com.pangers.tinyTony;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import com.pangers.DataService.ToDoDatabase;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class TaskListFragment extends Fragment implements
		NewTaskFragment.newTaskDialogListener {

	private TextView titleView;
	private ListView taskList;
	private TaskListAdapter adapter;
	private int msInAMinute = 60000;
	ToDoDatabase database;
	public final static String NAV_DRAWER_POS = "navDrawerPos";
	private int editTextTaskFlag = 0;
	private int editTextTimeFlag = 0;
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
		// Gets tasks from database in form of array
		tasks = database.getTaskList();
		// Adapter to create task list
		adapter = new TaskListAdapter(getActivity(), tasks);
		// Create the ListView
		taskList.setAdapter(adapter);
		// Sets onClick listener
		onClickSetup();
	}

	private void generateData(String newtask, String timeremaining,
			String importance) {
		TaskData task = new TaskData(newtask, timeremaining, importance);
		tasks.add(task);
		database.insertNewTask(task);
		((TaskListAdapter) taskList.getAdapter()).notifyDataSetChanged();
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

	private void onClickSetup() {
		taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent detailedTaskIntent = new Intent(parent.getContext(),
						DetailedTask.class);
				detailedTaskIntent.putExtra("position", position);
				startActivity(detailedTaskIntent);
			}
		});
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
		DatePicker datePicker = (DatePicker) dialog.getDialog().findViewById(
				R.id.datePicker);
		TimePicker timePicker = (TimePicker) dialog.getDialog().findViewById(
				(R.id.timePicker));
		RadioGroup radioGroup = (RadioGroup) dialog.getDialog().findViewById(
				R.id.newTaskDailogRadioGroup);
		String importance = "0";

		// Initialize calendar and then set it according to time and date
		// pickers
		Calendar calendar = Calendar.getInstance();
		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentHour());

		// Check radio buttons
		switch (radioGroup.getCheckedRadioButtonId()) {
		case R.id.newTaskDialogRadio0:
			importance = "0";
			break;
		case R.id.newTaskDialogRadio1:
			importance = "1";
			break;
		case R.id.newTaskDialobRadio2:
			importance = "2";
			break;
		}
		generateData(task.getText().toString(),
				String.valueOf(calendar.getTimeInMillis()), importance);

	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().dismiss();
	}

	public void setDataBase(ToDoDatabase database2) {
		database = database2;
	}

}
