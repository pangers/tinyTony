package com.pangers.DataService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.pangers.tinyTony.TaskData;

public class ToDoDatabase {
	private final static String TAG = "ToDoSQL";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "task_name";
	public static final String KEY_RATING = "task_importance";
	public static final String KEY_DATE = "task_create_date";
	public static final String KEY_MODE = "task_mode";
	private static final String DATA_NAME = "TO_DO";
	private static final String DATA_TABLE = "peopletable";
	private static final int DATA_VERSION = 1;
	public static final String KEY_STAGEONE = "stage_one";
	public static final String KEY_STAGETWO = "stage_two";
	private static final String DATA_HEXTABLE = "hextable";

	private DbHelper ourHelper;
	private Context alexander;
	private SQLiteDatabase ourDataBase;

	public ToDoDatabase(Context context) {
		alexander = context;
	}

	private class DbHelper extends SQLiteOpenHelper {

		/**
		 * 
		 * @param context
		 *            context of the activity
		 * @param name
		 *            null
		 * @param factory
		 *            null
		 * @param version
		 *            1
		 */
		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATA_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME
					+ " TEXT NOT NULL, " + KEY_DATE + " TEXT NOT NULL, "
					+ KEY_RATING + " TEXT NOT NULL);"

			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DATA_HEXTABLE);
			onCreate(db);
		}
	}

	public ToDoDatabase open() throws SQLException {
		ourHelper = new DbHelper(alexander, null, null, 1);
		ourDataBase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (ourHelper != null) {
			ourHelper.close();
		}
	}
	private long createEntry(String name, String importance, String date) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_RATING, importance);
		cv.put(KEY_DATE, date);
		return ourDataBase.insert(DATA_TABLE, null, cv);
	}
	
	/**
	 * Insert a new task to the data base
	 * @param task the task object to add
	 * @return true if the add is successful
	 */
	public boolean insertNewTask(TaskData task){
		 boolean addSuccess = false;
		 String name = task.getTask();
		 String importance = task.getImportance();
		 String time = task.getTime();
		 createEntry(name, importance, time);
		 return addSuccess;
		
	}
	
	public TaskData getTask(int index){
		TaskData gotTask = null;
		int iRow = 0;
		int iName = 0;
		int iRating = 0;
		int iDate = 0;
		String name = "";
		String time = "";
		String importance = "";
		String columns[] = new String[] { KEY_ROWID, KEY_NAME, KEY_RATING,
				KEY_DATE };
		Cursor c = ourDataBase.query(DATA_TABLE, columns, null, null, null,
				null, null);
		iRow = c.getColumnIndex(KEY_ROWID);
		iName = c.getColumnIndex(KEY_NAME);
		iRating = c.getColumnIndex(KEY_RATING);
		iDate = c.getColumnIndex(KEY_DATE);
		//move cursor to first
		c.moveToFirst();
		//place the index at the one we want
		c.move(index);
		name = c.getString(iName);
		time = c.getString(iDate);
		importance = c.getString(iRating);
		gotTask = new TaskData(name,time);
		gotTask.setImportance(importance);
		return gotTask;
	}
}
