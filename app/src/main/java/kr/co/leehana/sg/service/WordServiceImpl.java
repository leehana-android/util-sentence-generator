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

	private WordServiceImpl(){}

	public SGSQLiteDbHelper getHelper() {
		return helper;
	}

	public void setHelper(SGSQLiteDbHelper helper) {
		this.helper = helper;
	}

	@Override
	public List<Word> getWords(WordType type, GenreType genreType) {
		List<Word> wordList = new ArrayList<>();
		String whereClause = makeSelectWhereClause();

		String typeString = String.valueOf(type.getIndexCode());
		String genreTypeString = String.valueOf(genreType.getIndexCode());
		String[] whereArgs = new String[] { typeString, genreTypeString };

		String orderBy = SGDatabases._C_WORD + "," + SGDatabases._C_C_DATE + " DESC";

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_WORD, null, whereClause, whereArgs, null, null, orderBy);

		while (c.moveToNext()) {
			Word word = new Word();
			word.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			word.setWord(c.getString(c.getColumnIndex(SGDatabases._C_WORD)));
			word.setType(TypeConverter.intToWordType(c.getInt(c.getColumnIndex(SGDatabases._C_TYPE))));
			word.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));
			word.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			word.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));

			wordList.add(word);
		}
		return wordList;
	}

	@Override
	public List<Word> getNoBackupWords() {
		List<Word> wordList = new ArrayList<>();
		String whereClause = SGDatabases._C_BACKUP + "=?";

		String[] whereArgs = new String[] { "0" };

		String orderBy = SGDatabases._C_WORD + "," + SGDatabases._C_C_DATE + " DESC";

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_WORD, null, whereClause, whereArgs, null, null, orderBy);

		while (c.moveToNext()) {
			Word word = new Word();
			word.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			word.setWord(c.getString(c.getColumnIndex(SGDatabases._C_WORD)));
			word.setType(TypeConverter.intToWordType(c.getInt(c.getColumnIndex(SGDatabases._C_TYPE))));
			word.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));
			word.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			word.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));

			wordList.add(word);
		}
		return wordList;
	}

	@Override
	public int getCount(WordType type, GenreType genreType) {
		String whereClause = makeSelectWhereClause();

		String typeString = String.valueOf(type.getIndexCode());
		String genreTypeString = String.valueOf(genreType.getIndexCode());
		String[] whereArgs = new String[] { typeString, genreTypeString };

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_WORD, null, whereClause, whereArgs, null, null, null);
		return c.getCount();
	}

	private String makeSelectWhereClause() {
		return SGDatabases._C_TYPE + "=?" + " AND " + SGDatabases._C_GENRE + "=?";
	}

	@Override
	public void insert(Word word) {
		ContentValues newValues = new ContentValues();
		newValues.put(SGDatabases._C_WORD, word.getWord());
		newValues.put(SGDatabases._C_TYPE, word.getType().getIndexCode());
		newValues.put(SGDatabases._C_C_DATE, word.getCreateDate());
		newValues.put(SGDatabases._C_BACKUP, word.isBackup());
		newValues.put(SGDatabases._C_GENRE, word.getGenreType().getIndexCode());

		long newId = helper.getWDb().insert(SGDatabases._T_WORD, null, newValues);

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
		helper.getWDb().delete(SGDatabases._T_WORD, SGDatabases.CreateDB._ID + "=?", new String[]{String.valueOf(id)});
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
		updateValue.put(SGDatabases._C_GENRE, word.getGenreType().getIndexCode());

		return helper.getWDb().update(SGDatabases._T_WORD, updateValue, SGDatabases.CreateDB._ID + "=?", new String[]{wordIdStringValue});
	}
}
