package com.pangers.tinyTony;

import com.pangers.DataService.ToDoDatabase;
import com.pangers.tinyTony.TaskListFragment.OnTaskItemSelected;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements
		ListView.OnItemClickListener, TaskListFragment.OnTaskItemSelected {
	private String[] drawerMenu;
	private CharSequence drawerTitle;
	private CharSequence title;
	private ToDoDatabase database;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;

	private final static String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		database = new ToDoDatabase(this);
		title = drawerTitle = getTitle();
		// Setting up navigation drawer
		drawerMenu = getResources().getStringArray(R.array.drawermenu);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		drawerList = (ListView) findViewById(R.id.drawerlist);

		// Shadow over main content when drawer is open
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// add data to drawerlist
		drawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_row, drawerMenu));
		// this activity is listener for nav draw selections
		drawerList.setOnItemClickListener(this);
		// enable actionbar app icon to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Whenever nav drawer open, remove action bar overflow
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.draweropen, R.string.drawerclose) {
			// Called when navigation drawer in closed state
			public void onDrawerClosed(View view) {
				Log.d(TAG, "nav drawer closed state");
				getActionBar().setTitle(title);
				invalidateOptionsMenu(); // goes to onPrepareOptionsMenu()
			}

			// Called when navigation drawer in open state
			public void onDrawerOpened(View drawerView) {
				Log.d(TAG, "nav drawer opened state");
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu();// goes to onPrepareOptionsMenu()
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		Log.d(TAG, "onCreate()");
		// if its first time opening app
		if (savedInstanceState == null) {
			Log.d(TAG, "onCreate() first time");
			selectItem(0);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");
		// drawerToggle.setDrawerIndicatorEnabled(true);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainactions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// Called whenever invalidateOptionsMenu called
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		// if (getSupportFragmentManager().findFragmentByTag("TaskList") !=
		// null) {
		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			if (getSupportFragmentManager().getBackStackEntryAt(
					getSupportFragmentManager().getBackStackEntryCount() - 1)
					.getName() == "TaskList") {
				Log.d(TAG, "made it in here!");
				menu.findItem(R.id.add).setVisible(!drawerOpen);
			} else if (getSupportFragmentManager().getBackStackEntryAt(
					getSupportFragmentManager().getBackStackEntryCount() - 1)
					.getName() == "DETAILTASK") {
				menu.findItem(R.id.deletedetailedtask).setVisible(!drawerOpen);
			}
		}
		menu.findItem(R.id.settings).setVisible(!drawerOpen);
		menu.findItem(R.id.about).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// ActionBar home/up action toggle nav drawer
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
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
		selectItem(position);
	}

	public void selectItem(int position) {
		switch (position) {
		case 0:
			Log.d(TAG, "onItemClick nav drawer case 0");
			if (getSupportFragmentManager().findFragmentByTag("newTaskList") == null) {
				// Pass selection position to next fragment
				Fragment fragment = new TaskListFragment();
				((TaskListFragment) fragment).setDataBase(database);
				Bundle args = new Bundle();
				args.putInt(TaskListFragment.NAV_DRAWER_POS, position);
				fragment.setArguments(args);
				// Inflate next fragment by add
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.contentframe, fragment, "TaskList")
						.addToBackStack("TaskList").commit();
				// Update selected position in navigation drawer
				drawerList.setItemChecked(position, true);
				getActionBar().setTitle(drawerMenu[position]);
			}
			drawerLayout.closeDrawer(drawerList);
			break;
		}
	}

	@Override
	public void onTaskSelected(int position) {
		Log.d(TAG, "onTaskSelected()");
		DetailedTask detailTaskFrag = (DetailedTask) getSupportFragmentManager()
				.findFragmentById(R.id.detailframe);
		// if we are in a two pane layout and first time showing detail task
		if ((detailTaskFrag != null)
				&& (getSupportFragmentManager().findFragmentByTag("DETAILTASK") == null)) {
			Log.d(TAG, "onTaskSelected() two pane and first time");

			DetailedTask detTask = new DetailedTask();
			Bundle args = new Bundle();
			args.putInt("POSITION", position);
			detTask.setArguments(args);
			detTask.setDataBase(database);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.detailframe, detTask, "DETAILTASK")
					.addToBackStack("DETAILTASK").commit();
			// we are in one pane layout
		} else {
			Log.d(TAG, "onTaskSelected() one pane");
			DetailedTask detTask = new DetailedTask();
			Bundle args = new Bundle();
			args.putInt("POSITION", position);
			detTask.setArguments(args);
			detTask.setDataBase(database);
			// drawerToggle.setDrawerIndicatorEnabled(false);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.contentframe, detTask, "DETAILTASK")
					.addToBackStack("DETAILTASK").commit();
		}
	}

	// When using ActionBarDrawerToggle, onPostCreate and onConfigurationChanged
	// must be called
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occured
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		drawerToggle.onConfigurationChanged(newConfig);

	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			this.finish();
		}
		super.onBackPressed();
		
	}
}
