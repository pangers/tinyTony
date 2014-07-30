package com.pangers.tinyTony;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TabHost;

public class NewTaskFragment extends DialogFragment {

	private AlertDialog dialog;
	newTaskDialogListener newTaskListener;
	private EditText task;
	private int editTextTaskFlag = 0;
	private String textTask = null;
	private final static String TASK_FLAG = "taskFlag";
	private final static String TASK_TEXT = "taskText";

	public interface newTaskDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			editTextTaskFlag = savedInstanceState.getInt(TASK_FLAG);
			textTask = savedInstanceState.getString(TASK_TEXT);
		}
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
										.onDialogPositiveClick(NewTaskFragment.this);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								newTaskListener
										.onDialogNegativeClick(NewTaskFragment.this);
							}
						});
		dialog = builder.create();
		return dialog;
	}

	@Override
	public void onStart() {
		super.onStart();
		task = (EditText) getDialog().findViewById(R.id.newtask);

		if (editTextTaskFlag == 0) {
			dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
		}
		if (textTask != null) {
			task.setText(textTask);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		watchTaskEditText(task);
	}

	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(TASK_FLAG, editTextTaskFlag);
		outState.putString(TASK_TEXT, task.getText().toString());
	}

	public void watchTaskEditText(EditText task) {
		task.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// Check if task edit text is empty.
				if (TextUtils.isEmpty(s.toString().trim()) != true) {
					editTextTaskFlag = 1;
					dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
				} else {
					editTextTaskFlag = 0;
					dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
				}
			}
		});
	}
}
