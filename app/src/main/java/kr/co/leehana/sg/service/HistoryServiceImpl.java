package kr.co.leehana.sg.service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import kr.co.leehana.sg.converter.BoolConverter;
import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.db.SGDatabases;
import kr.co.leehana.sg.db.SGSQLiteDbHelper;
import kr.co.leehana.sg.model.History;
import kr.co.leehana.sg.type.GenreType;

/**
 * Created by Hana Lee on 2015-08-13 03:31
 *
 * @author Hana Lee
 * @since 2015-08-13 03:31
 */
public class HistoryServiceImpl implements IHistoryService {

	private static HistoryServiceImpl service = new HistoryServiceImpl();

	public static HistoryServiceImpl getInstance() {
		return service;
	}

	private HistoryServiceImpl(){}

	private SGSQLiteDbHelper helper;

	public SGSQLiteDbHelper getHelper() {
		return helper;
	}

	public void setHelper(SGSQLiteDbHelper helper) {
		this.helper = helper;
	}

	@Override
	public List<History> getHistories() {
		List<History> historyList = new ArrayList<>();

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_HISTORY, null, null, null, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			History history = new History();
			history.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			history.setSentence(c.getString(c.getColumnIndex(SGDatabases._C_SENTENCE)));
			history.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			history.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			history.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			historyList.add(history);
		}
		return historyList;
	}

	@Override
	public List<History> getHistories(GenreType genreType) {
		List<History> historyList = new ArrayList<>();

		String genreCode = String.valueOf(genreType.getIndexCode());

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_HISTORY, null, SGDatabases._C_GENRE + "=?", new String[]{ genreCode }, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			History history = new History();
			history.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			history.setSentence(c.getString(c.getColumnIndex(SGDatabases._C_SENTENCE)));
			history.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			history.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			history.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			historyList.add(history);
		}
		return historyList;
	}

	@Override
	public void insert(List<History> histories) {
		if (histories != null) {
			for (History history : histories) {
				insert(history);
			}
		}
	}

	@Override
	public void insert(History history) {
		ContentValues newValues = new ContentValues();
		newValues.put(SGDatabases._C_SENTENCE, history.getSentence());
		newValues.put(SGDatabases._C_C_DATE, history.getCreateDate());
		newValues.put(SGDatabases._C_BACKUP, history.isBackup());
		newValues.put(SGDatabases._C_GENRE, history.getGenreType().getIndexCode());

		long newId = helper.getWDb().insert(SGDatabases._T_HISTORY, null, newValues);

		history.setId((int) newId);
	}

	@Override
	public void delete(History history) {
		String id = String.valueOf(history.getId());
		helper.getWDb().delete(SGDatabases._T_HISTORY, SGDatabases.CreateDB._ID + "=?", new String[]{id});
	}
}
