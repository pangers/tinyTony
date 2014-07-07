package com.pangers.tinyTony;

import com.pangers.DataService.ToDoDatabase;

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
		ListView.OnItemClickListener {
	private String[] drawerMenu;
	private CharSequence drawerTitle;
	private CharSequence title;
	private ToDoDatabase database;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;

	final static String TAG = "navDrawer";

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
				getActionBar().setTitle(title);
				invalidateOptionsMenu(); // goes to onPrepareOptionsMenu()
			}

			// Called when navigation drawer in open state
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(drawerTitle); 
				invalidateOptionsMenu();// goes to onPrepareOptionsMenu()
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainactions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// Called whenever invalidateOptionsMenu called
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		if (getSupportFragmentManager().findFragmentByTag("newTaskList") != null) {
			menu.findItem(R.id.add).setVisible(!drawerOpen);
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
			if (getSupportFragmentManager().findFragmentByTag("newTaskList") == null) {
				// Pass selection position to next fragment
				Fragment fragment = new TaskListFragment();
				((TaskListFragment) fragment).setDataBase(database);
				Bundle args = new Bundle();
				args.putInt(TaskListFragment.NAV_DRAWER_POS, position);
				fragment.setArguments(args);
				// Inflate next fragment by replace
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.contentframe, fragment, "newTaskList")
						.commit();
				// Update selected position in navigation drawer
				drawerList.setItemChecked(position, true);
				getActionBar().setTitle(drawerMenu[position]);
			}
			drawerLayout.closeDrawer(drawerList);
			break;
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

}
