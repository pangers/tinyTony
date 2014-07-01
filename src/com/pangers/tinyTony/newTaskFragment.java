package com.pangers.tinyTony;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class newTaskFragment extends DialogFragment {

	public interface newTaskDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	newTaskDialogListener newTaskListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			newTaskListener = (newTaskDialogListener) getTargetFragment();
		} catch (ClassCastException e) {
			// Fragment didn't implement interface, throw exception
			throw new ClassCastException(
					"Calling fragment must implement newTaskDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate layout for dialog
		builder.setView(inflater.inflate(R.layout.newtaskdialog, null))
				.setTitle(R.string.dialogtitle)
				.setPositiveButton(R.string.add,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								newTaskListener
										.onDialogPositiveClick(newTaskFragment.this);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								newTaskListener
										.onDialogNegativeClick(newTaskFragment.this);
							}
						});
		return builder.create();
	}
}
