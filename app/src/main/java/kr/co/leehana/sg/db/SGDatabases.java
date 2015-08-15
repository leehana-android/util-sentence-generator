package kr.co.leehana.sg.db;

import android.provider.BaseColumns;

/**
 * Created by Hana Lee on 2015-08-13 02:31
 *
 * @author Hana Lee
 * @since 2015-08-13 02:31
 */
public final class SGDatabases {

	public static final String _T_WORD = "sg_word";
	public static final String _T_FAVORITE = "sg_favorite";
	public static final String _T_HISTORY = "sg_history";
	public static final String _T_FAVORITE_CATEGORY = "sg_fav_cate";

	public static final String _C_WORD = "word";
	public static final String _C_TYPE = "type";
	public static final String _C_SENTENCE = "sentence";
	public static final String _C_C_DATE = "created";
	public static final String _C_BACKUP = "backup";
	public static final String _C_ENABLED = "enabled";
	public static final String _C_GENRE = "genre";
	public static final String _C_PARENT = "parent";
	public static final String _C_NAME = "name";
	public static final String _C_RATE = "rate";

	public static final class CreateDB implements BaseColumns {
		public static final String _WORD = "create table " + _T_WORD + " (" + _ID + " integer primary key autoincrement, " + _C_WORD + " text not null, " + _C_TYPE + " integer not null, " + _C_GENRE + " integer not null, " + _C_C_DATE + " text not null, " + _C_BACKUP + " integer);";
		public static final String _HISTORY = "create table " + _T_HISTORY + " (" + _ID + " integer primary key autoincrement, " + _C_SENTENCE + " text not null, " + _C_GENRE + " integer not null, " + _C_C_DATE + " text not null, " + _C_BACKUP + " integer);";
		public static final String _FAVORITE = "create table " + _T_FAVORITE + " (" + _ID + " integer primary key autoincrement, " + _C_PARENT + " integer not null, " + _C_SENTENCE + " text not null, " + _C_GENRE + " integer not null, " + _C_RATE + " integer not null, " + _C_ENABLED + " integer not null, " + _C_C_DATE + " text not null, " + _C_BACKUP + " integer);";
		public static final String _FAVORITE_CATEGORY = "create table " + _T_FAVORITE_CATEGORY + " (" + _ID + " integer primary key autoincrement, " + _C_NAME + " text not null, " + _C_GENRE + " integer not null, " + _C_RATE + " integer not null, " + _C_ENABLED + " integer not null, " + _C_C_DATE + " text not null, " + _C_BACKUP + " integer);";
	}
}
