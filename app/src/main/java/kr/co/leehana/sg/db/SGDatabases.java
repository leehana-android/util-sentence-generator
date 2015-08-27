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
	public static final String _T_SETTING = "sg_setting";

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
	public static final String _C_SENTENCE_COUNT = "sentence_count";
	public static final String _C_FIRST_TYPE = "first_type";
	public static final String _C_SECOND_TYPE = "second_type";
	public static final String _C_THIRD_TYPE = "third_type";
	public static final String _C_FOURTH_TYPE = "fourth_type";
	public static final String _C_MODIFIED = "modified";

	public static final class CreateDB implements BaseColumns {
		public static final String _WORD = "create table " + _T_WORD + " (" + _ID + " integer primary key autoincrement, " + _C_WORD + " text not null, " + _C_TYPE + " integer not null, " + _C_GENRE + " integer not null, " + _C_MODIFIED + " integer not null, " + _C_C_DATE + " text not null, " + _C_BACKUP + " integer);";
		public static final String _HISTORY = "create table " + _T_HISTORY + " (" + _ID + " integer primary key autoincrement, " + _C_SENTENCE + " text not null, " + _C_GENRE + " integer not null, " + _C_C_DATE + " text not null, " + _C_BACKUP + " integer);";
		public static final String _FAVORITE = "create table " + _T_FAVORITE + " (" + _ID + " integer primary key autoincrement, " + _C_PARENT + " integer not null, " + _C_SENTENCE + " text not null, " + _C_GENRE + " integer not null, " + _C_RATE + " integer not null, " + _C_ENABLED + " integer not null, " + _C_MODIFIED + " integer not null, " + _C_C_DATE + " text not null, " + _C_BACKUP + " integer);";
		public static final String _FAVORITE_CATEGORY = "create table " + _T_FAVORITE_CATEGORY + " (" + _ID + " integer primary key autoincrement, " + _C_NAME + " text not null, " + _C_GENRE + " integer not null, " + _C_RATE + " integer not null, " + _C_ENABLED + " integer not null, " + _C_MODIFIED + " integer not null, " + _C_C_DATE + " text not null, " + _C_BACKUP + " integer);";
		public static final String _SETTING = "create table " + _T_SETTING + " (" + _ID + " integer primary key autoincrement, " + _C_SENTENCE_COUNT + " integer not null, " + _C_FIRST_TYPE + " integer not null, " + _C_SECOND_TYPE + " integer not null, " + _C_THIRD_TYPE + " integer not null, " + _C_FOURTH_TYPE + " integer not null, " + _C_MODIFIED + " integer not null, " + _C_C_DATE + " text not null, " + _C_BACKUP + " integer);";
	}
}
