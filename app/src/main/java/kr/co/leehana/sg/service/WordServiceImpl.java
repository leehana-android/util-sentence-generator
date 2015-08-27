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
import kr.co.leehana.sg.model.Word;
import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.type.WordType;

/**
 * Created by Hana Lee on 2015-08-13 03:32
 *
 * @author Hana Lee
 * @since 2015-08-13 03:32
 */
public class WordServiceImpl implements IWordService {

	private SGSQLiteDbHelper helper;

	private static WordServiceImpl service = new WordServiceImpl();

	public static WordServiceImpl getInstance() {
		return service;
	}

	private WordServiceImpl() {
	}

	public void setHelper(SGSQLiteDbHelper helper) {
		this.helper = helper;
	}

	@Override
	public List<Word> getWords(WordType type, GenreType genreType) {
		String tableName = SGDatabases._T_WORD;
		String[] selectColumns = new String[]{"*"};
		String whereClause = SGDatabases._C_TYPE + "=?" + " AND " + SGDatabases._C_GENRE + "=?";

		String typeString = String.valueOf(type.getIndexCode());
		String genreTypeString = String.valueOf(genreType.getIndexCode());
		String[] whereArgs = new String[]{typeString, genreTypeString};

		String having = "";
		String groupBy = "";
		String orderBy = SGDatabases._C_WORD + "," + SGDatabases._C_C_DATE + " DESC";

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(tableName, selectColumns, whereClause, whereArgs, groupBy, having, orderBy);

		List<Word> wordList = new ArrayList<>();
		while (c.moveToNext()) {
			Word word = new Word();
			word.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			word.setWord(c.getString(c.getColumnIndex(SGDatabases._C_WORD)));
			word.setType(TypeConverter.intToWordType(c.getInt(c.getColumnIndex(SGDatabases._C_TYPE))));
			word.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));
			word.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			word.setModified(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_MODIFIED))));
			word.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));

			wordList.add(word);
		}
		return wordList;
	}

	@Override
	public List<Word> getWords() {
		String tableName = SGDatabases._T_WORD;
		String[] selectColumns = new String[]{"*"};
		String whereClause = "";

		String[] whereArgs = new String[]{};

		String having = "";
		String groupBy = "";
		String orderBy = SGDatabases._C_WORD + "," + SGDatabases._C_C_DATE + " DESC";

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(tableName, selectColumns, whereClause, whereArgs, groupBy, having, orderBy);

		List<Word> wordList = new ArrayList<>();
		while (c.moveToNext()) {
			Word word = new Word();
			word.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			word.setWord(c.getString(c.getColumnIndex(SGDatabases._C_WORD)));
			word.setType(TypeConverter.intToWordType(c.getInt(c.getColumnIndex(SGDatabases._C_TYPE))));
			word.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));
			word.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			word.setModified(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_MODIFIED))));
			word.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));

			wordList.add(word);
		}
		return wordList;
	}

	@Override
	public List<Word> getWordsByBackupStatus(boolean isBackup) {
		String tableName = SGDatabases._T_WORD;
		String[] selectColumns = new String[]{"*"};
		String whereClause = SGDatabases._C_BACKUP + "=?";
		String[] whereArgs = new String[]{String.valueOf(BoolConverter.boolToInt(isBackup))};
		String groupBy = "";
		String having = "";
		String orderBy = SGDatabases._C_WORD + "," + SGDatabases._C_C_DATE + " DESC";

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(tableName, selectColumns, whereClause, whereArgs, groupBy, having, orderBy);

		List<Word> wordList = new ArrayList<>();
		while (c.moveToNext()) {
			Word word = new Word();
			word.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			word.setWord(c.getString(c.getColumnIndex(SGDatabases._C_WORD)));
			word.setType(TypeConverter.intToWordType(c.getInt(c.getColumnIndex(SGDatabases._C_TYPE))));
			word.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));
			word.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			word.setModified(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_MODIFIED))));
			word.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));

			wordList.add(word);
		}
		return wordList;
	}

	@Override
	public List<Word> getWordsByModifyStatus(boolean isModified) {
		String tableName = SGDatabases._T_WORD;
		String[] selectColumns = new String[]{"*"};
		String whereClause = SGDatabases._C_MODIFIED + "=?";
		String[] whereArgs = new String[]{String.valueOf(BoolConverter.boolToInt(isModified))};
		String groupBy = "";
		String having = "";
		String orderBy = SGDatabases._C_WORD + "," + SGDatabases._C_C_DATE + " DESC";

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(tableName, selectColumns, whereClause, whereArgs, groupBy, having, orderBy);

		List<Word> wordList = new ArrayList<>();
		while (c.moveToNext()) {
			Word word = new Word();
			word.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			word.setWord(c.getString(c.getColumnIndex(SGDatabases._C_WORD)));
			word.setType(TypeConverter.intToWordType(c.getInt(c.getColumnIndex(SGDatabases._C_TYPE))));
			word.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));
			word.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			word.setModified(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_MODIFIED))));
			word.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));

			wordList.add(word);
		}
		return wordList;
	}

	@Override
	public int getCount(WordType type, GenreType genreType) {
		String tableName = SGDatabases._T_WORD;
		String[] selectColumns = new String[]{"*"};
		String whereClause = SGDatabases._C_TYPE + "=?" + " AND " + SGDatabases._C_GENRE + "=?";

		String typeString = String.valueOf(type.getIndexCode());
		String genreTypeString = String.valueOf(genreType.getIndexCode());
		String[] whereArgs = new String[]{typeString, genreTypeString};

		String groupBy = "";
		String having = "";
		String orderBy = "";

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(tableName, selectColumns, whereClause, whereArgs, groupBy, having, orderBy);
		return c.getCount();
	}

	@Override
	public void insert(Word word) {
		ContentValues newValues = new ContentValues();
		newValues.put(SGDatabases._C_WORD, word.getWord());
		newValues.put(SGDatabases._C_TYPE, word.getType().getIndexCode());
		newValues.put(SGDatabases._C_C_DATE, word.getCreateDate());
		newValues.put(SGDatabases._C_BACKUP, word.isBackup());
		newValues.put(SGDatabases._C_MODIFIED, word.isModified());
		newValues.put(SGDatabases._C_GENRE, word.getGenreType().getIndexCode());

		String tableName = SGDatabases._T_WORD;
		String nullColumnHack = "";

		long newId = helper.getWDb().insert(tableName, nullColumnHack, newValues);

		word.setId((int) newId);
	}

	@Override
	public void insert(List<Word> wordList) {
		if (wordList != null) {
			for (Word word : wordList) {
				insert(word);
			}
		}
	}

	@Override
	public void delete(Word word) {
		delete(word.getId());
	}

	@Override
	public void delete(int id) {
		String tableName = SGDatabases._T_WORD;
		String whereClause = SGDatabases.CreateDB._ID + "=?";
		String[] whereArgs = {String.valueOf(id)};

		helper.getWDb().delete(tableName, whereClause, whereArgs);
	}

	@Override
	public void delete(List<Integer> ids) {
		if (ids != null) {
			for (Integer id : ids) {
				delete(id);
			}
		}
	}

	@Override
	public int update(Word word) {
		String wordIdStringValue = String.valueOf(word.getId());
		ContentValues updateValue = new ContentValues();
		updateValue.put(SGDatabases._C_WORD, word.getWord());
		updateValue.put(SGDatabases._C_TYPE, word.getType().getIndexCode());
		updateValue.put(SGDatabases._C_C_DATE, word.getCreateDate());
		updateValue.put(SGDatabases._C_BACKUP, word.isBackup());
		updateValue.put(SGDatabases._C_MODIFIED, word.isModified());
		updateValue.put(SGDatabases._C_GENRE, word.getGenreType().getIndexCode());

		String tableName = SGDatabases._T_WORD;
		String whereClause = SGDatabases.CreateDB._ID + "=?";
		String[] whereArgs = {wordIdStringValue};

		return helper.getWDb().update(tableName, updateValue, whereClause, whereArgs);
	}
}
