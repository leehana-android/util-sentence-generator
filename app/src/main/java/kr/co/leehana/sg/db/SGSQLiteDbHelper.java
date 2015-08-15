package kr.co.leehana.sg.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hana Lee on 2015-08-13 02:47
 *
 * @author Hana Lee
 * @since 2015-08-13 02:47
 */
public class SGSQLiteDbHelper {

	public static final String DATABASE_NAME = "sg.db";
	private static final int DATABASE_VERSION = 1;
	private Context context;
	private SGOpenHelper helper;
	private SQLiteDatabase db;

	private class SGOpenHelper extends SQLiteOpenHelper {

		public SGOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// 원래 여기에 create 문이 들어가야하나 기존에 있는 DB를 사용하므로 생략
//			db.execSQL(SGDatabases.CreateDB._WORD);
//			db.execSQL(SGDatabases.CreateDB._FAVORITE);
//			db.execSQL(SGDatabases.CreateDB._HISTORY);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// 기존에 있는 DB를 사용하므로 생략
//			db.execSQL("DROP TABLE IF EXISTS " + SGDatabases.CreateDB._WORD);
//			db.execSQL("DROP TABLE IF EXISTS " + SGDatabases.CreateDB._FAVORITE);
//			db.execSQL("DROP TABLE IF EXISTS " + SGDatabases.CreateDB._HISTORY);
//			onCreate(db);
		}
	}

	public SGSQLiteDbHelper(Context context) {
		this.context = context;
		makeHelper();
	}

	private SGOpenHelper makeHelper() {
		if (this.helper == null) {
			this.helper = new SGOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		return this.helper;
	}

	public SQLiteDatabase getWDb() {
		return helper.getWritableDatabase();
	}

	public SQLiteDatabase getRDb() {
		return helper.getReadableDatabase();
	}
}
