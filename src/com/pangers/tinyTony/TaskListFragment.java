package com.pangers.tinyTony;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import com.pangers.DataService.ToDoDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class TaskListFragment extends Fragment implements
		NewTaskFragment.newTaskDialogListener {

	final static String TAG = "TaskListFragment";
	private TextView titleView;
	private ListView taskList;
	private TaskListAdapter adapter;
	private int msInAMinute = 60000;
	private ToDoDatabase database;
	public final static String NAV_DRAWER_POS = "navDrawerPos";
	private int editTextTaskFlag = 0;
	private int editTextTimeFlag = 0;
	ArrayList<TaskData> tasks = new ArrayList<TaskData>();
	private ActionMode mActionMode;
	private OnTaskItemSelected taskCallback;

	public interface OnTaskItemSelected {
		public void onTaskSelected(int position);
	}
	
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
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			taskCallback = (OnTaskItemSelected) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
		}
	}

	public void onResume() {
		super.onResume();
		updateListView();
		// Sets onClick listener
		onClickSetup();
		onLongClickSetup();
		Log.d(TAG, "onResume of tasklistfragment");
	}
	

	public void updateListView() {
		// Gets tasks from database in form of array
		tasks = database.getTaskList();
		// Adapter to create task list
		adapter = new TaskListAdapter(getActivity(), tasks);
		// Create the ListView
		taskList.setAdapter(adapter);
	}

	private void generateData(String newtask, String timeremaining,
			String importance) {
		TaskData task = new TaskData(newtask, timeremaining, importance);
		tasks.add(task);
		database.insertNewTask(task);
		adapter = new TaskListAdapter(getActivity(), tasks);
		taskList.setAdapter(adapter);
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
//				Intent detailedTaskIntent = new Intent(parent.getContext(),
//						DetailedTask.class);
//				detailedTaskIntent.putExtra("position", position);
//				startActivity(detailedTaskIntent);
				taskList.setItemChecked(position, true);
				taskCallback.onTaskSelected(position);
			}
		});
	}

	private void onLongClickSetup() {
		taskList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mActionMode != null) {
					return false;
				}

				// start contextual action bar
				mActionMode = getActivity().startActionMode(
						new ActionModeCallback(position));
				view.setSelected(true);
				return true;
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
				timePicker.getCurrentMinute());

		// Check radio buttons
		switch (radioGroup.getCheckedRadioButtonId()) {
		case R.id.newTaskDialogRadio0:
			importance = "0";
			break;
		case R.id.newTaskDialogRadio1:
			importance = "1";
			break;
		case R.id.newTaskDialogRadio2:
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

	private class ActionModeCallback implements ActionMode.Callback {

		private int position;

		ActionModeCallback(int position) {
			this.position = position;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.contextualactionbar, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// return false if nothing is done
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.contextualdelete:
				database.deleteNewTask(database.getTask(position));
				updateListView();
				//close contextual action bar
				mode.finish();
				return true;
			default:
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}

	}

}
