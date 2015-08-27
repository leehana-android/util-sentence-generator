package kr.co.leehana.sg.service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import kr.co.leehana.sg.converter.BoolConverter;
import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.db.SGDatabases;
import kr.co.leehana.sg.db.SGSQLiteDbHelper;
import kr.co.leehana.sg.model.Setting;

/**
 * Created by Hana Lee on 2015-08-17 00:13
 *
 * @author Hana Lee
 * @since 2015-08-17 00:13
 */
public class SettingServiceImpl implements ISettingService {

	private SGSQLiteDbHelper helper;

	private static SettingServiceImpl service = new SettingServiceImpl();

	public static SettingServiceImpl getInstance() {
		return service;
	}

	private SettingServiceImpl() {
	}

	public void setHelper(SGSQLiteDbHelper helper) {
		this.helper = helper;
	}

	@Override
	public Setting getSetting() {

		String tableName = SGDatabases._T_SETTING;
		String[] selectColumns = new String[]{"*"};
		String whereClause = "";
		String[] whereArgs = new String[]{};
		String groupBy = "";
		String having = "";
		String orderBy = "";

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(tableName, selectColumns, whereClause, whereArgs, groupBy, having, orderBy);

		Setting setting = null;

		if (c.moveToNext()) {
			setting = new Setting();
			setting.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			setting.setSentenceCount(c.getInt(c.getColumnIndex(SGDatabases._C_SENTENCE_COUNT)));
			setting.setFirstWordType(TypeConverter.intToSentenceGenerateType(c.getInt(c.getColumnIndex(SGDatabases._C_FIRST_TYPE))));
			setting.setSecondWordType(TypeConverter.intToSentenceGenerateType(c.getInt(c.getColumnIndex(SGDatabases._C_SECOND_TYPE))));
			setting.setThirdWordType(TypeConverter.intToSentenceGenerateType(c.getInt(c.getColumnIndex(SGDatabases._C_THIRD_TYPE))));
			setting.setFourthWordType(TypeConverter.intToSentenceGenerateType(c.getInt(c.getColumnIndex(SGDatabases._C_FOURTH_TYPE))));
			setting.setModified(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_MODIFIED))));
			setting.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			setting.setCreated(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
		}

		return setting;
	}

	@Override
	public Setting getNoBackupSetting() {
		String tableName = SGDatabases._T_SETTING;
		String[] selectColumns = new String[]{"*"};
		String whereClause = SGDatabases._C_BACKUP + "=?";
		String[] whereArgs = {"0"};
		String groupBy = "";
		String having = "";
		String orderBy = "";

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(tableName, selectColumns, whereClause, whereArgs, groupBy, having, orderBy);

		Setting setting = null;

		if (c.moveToNext()) {
			setting = new Setting();
			setting.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			setting.setSentenceCount(c.getInt(c.getColumnIndex(SGDatabases._C_SENTENCE_COUNT)));
			setting.setFirstWordType(TypeConverter.intToSentenceGenerateType(c.getInt(c.getColumnIndex(SGDatabases._C_FIRST_TYPE))));
			setting.setSecondWordType(TypeConverter.intToSentenceGenerateType(c.getInt(c.getColumnIndex(SGDatabases._C_SECOND_TYPE))));
			setting.setThirdWordType(TypeConverter.intToSentenceGenerateType(c.getInt(c.getColumnIndex(SGDatabases._C_THIRD_TYPE))));
			setting.setFourthWordType(TypeConverter.intToSentenceGenerateType(c.getInt(c.getColumnIndex(SGDatabases._C_FOURTH_TYPE))));
			setting.setModified(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_MODIFIED))));
			setting.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			setting.setCreated(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
		}

		return setting;
	}

	@Override
	public void update(Setting setting) {
		ContentValues newValues = new ContentValues();
		newValues.put(SGDatabases._C_SENTENCE_COUNT, setting.getSentenceCount());
		newValues.put(SGDatabases._C_FIRST_TYPE, setting.getFirstWordType().getIndexCode());
		newValues.put(SGDatabases._C_SECOND_TYPE, setting.getSecondWordType().getIndexCode());
		newValues.put(SGDatabases._C_THIRD_TYPE, setting.getThirdWordType().getIndexCode());
		newValues.put(SGDatabases._C_FOURTH_TYPE, setting.getFourthWordType().getIndexCode());
		newValues.put(SGDatabases._C_MODIFIED, setting.isModified());
		newValues.put(SGDatabases._C_C_DATE, setting.getCreated());
		newValues.put(SGDatabases._C_BACKUP, setting.isBackup());

		String tableName = SGDatabases._T_SETTING;
		String whereClause = SGDatabases.CreateDB._ID + "=?";
		String[] whereArgs = {String.valueOf(setting.getId())};
		
		helper.getWDb().update(tableName, newValues, whereClause, whereArgs);
	}
}
