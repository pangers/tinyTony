package com.pangers.tinyTony;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class taskListFragment extends Fragment implements AdapterView.OnItemClickListener {
	
	private TextView titleView;
	private ListView taskList;
	private taskListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Dont destroy fragment on configuration changes
		setRetainInstance(true);
		//Inflate the fragment
		View result = inflater.inflate(R.layout.taskfragment, container, false);
		return result;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Turn on action bar
		setHasOptionsMenu(true);
		//Find views
		titleView = (TextView) getActivity().findViewById(R.id.testtitle);
		taskList = (ListView) getActivity().findViewById(R.id.tasklist);
		//Adapter to create task list
		adapter = new taskListAdapter(getActivity(), generateData());
		//Create the ListView
		taskList.setAdapter(adapter);
		
	}
	
	private ArrayList<taskData> generateData() {
		ArrayList<taskData> tasks = new ArrayList<taskData>();
		
		tasks.add(new taskData("Pick up kids", "2:39")); 
		tasks.add(new taskData("Finish thesis", "3:48"));
		tasks.add(new taskData("Meet Charlotte", "5:22"));
		
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
}
