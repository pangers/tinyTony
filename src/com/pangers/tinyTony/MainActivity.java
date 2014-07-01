package com.pangers.tinyTony;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, new taskListFragment()).commit();
		}
	}
}
