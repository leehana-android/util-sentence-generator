package kr.co.leehana.sg.service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.db.SGDatabases;
import kr.co.leehana.sg.db.SGSQLiteDbHelper;
import kr.co.leehana.sg.model.Favorite;
import kr.co.leehana.sg.converter.BoolConverter;
import kr.co.leehana.sg.model.FavoriteCategory;
import kr.co.leehana.sg.type.GenreType;

/**
 * Created by Hana Lee on 2015-08-13 03:31
 *
 * @author Hana Lee
 * @since 2015-08-13 03:31
 */
public class FavoriteServiceImpl implements IFavoriteService {

	private static FavoriteServiceImpl service = new FavoriteServiceImpl();

	public static FavoriteServiceImpl getInstance() {
		return service;
	}

	private FavoriteServiceImpl(){}

	private SGSQLiteDbHelper helper;

	public SGSQLiteDbHelper getHelper() {
		return helper;
	}

	public void setHelper(SGSQLiteDbHelper helper) {
		this.helper = helper;
	}

	@Override
	public List<Favorite> getFavorites() {
		List<Favorite> favoriteList = new ArrayList<>();

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE, null, SGDatabases._C_ENABLED + "=?", new String[]{"1"}, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			Favorite favorite = new Favorite();
			favorite.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			favorite.setSentence(c.getString(c.getColumnIndex(SGDatabases._C_SENTENCE)));
			favorite.setRate(c.getInt(c.getColumnIndex(SGDatabases._C_RATE)));
			favorite.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			favorite.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			favorite.setEnabled(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_ENABLED))));
			favorite.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			favoriteList.add(favorite);
		}
		return favoriteList;
	}

	@Override
	public List<Favorite> getFavorites(GenreType genreType) {
		List<Favorite> favoriteList = new ArrayList<>();

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(SGDatabases._C_ENABLED).append("=?").append(" AND ").append(SGDatabases._C_GENRE).append("=?");

		String genreCode = String.valueOf(genreType.getIndexCode());

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE, null, whereClause.toString(), new String[]{"1", genreCode}, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			Favorite favorite = new Favorite();
			favorite.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			favorite.setParentId(c.getInt(c.getColumnIndex(SGDatabases._C_PARENT)));
			favorite.setSentence(c.getString(c.getColumnIndex(SGDatabases._C_SENTENCE)));
			favorite.setRate(c.getInt(c.getColumnIndex(SGDatabases._C_RATE)));
			favorite.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			favorite.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			favorite.setEnabled(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_ENABLED))));
			favorite.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			favoriteList.add(favorite);
		}
		return favoriteList;
	}

	@Override
	public List<Favorite> getFavoritesByParent(FavoriteCategory parent) {
		return getFavoritesByParentId(parent.getId());
	}

	@Override
	public List<Favorite> getFavoritesByParentId(int parentId) {
		List<Favorite> favoriteList = new ArrayList<>();

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(SGDatabases._C_ENABLED).append("=?").append(" AND ").append(SGDatabases._C_PARENT).append("=?");

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE, null, whereClause.toString(), new String[]{"1", String.valueOf(parentId)}, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			Favorite favorite = new Favorite();
			favorite.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			favorite.setParentId(c.getInt(c.getColumnIndex(SGDatabases._C_PARENT)));
			favorite.setSentence(c.getString(c.getColumnIndex(SGDatabases._C_SENTENCE)));
			favorite.setRate(c.getInt(c.getColumnIndex(SGDatabases._C_RATE)));
			favorite.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			favorite.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			favorite.setEnabled(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_ENABLED))));
			favorite.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			favoriteList.add(favorite);
		}
		return favoriteList;
	}

	@Override
	public List<Favorite> getFavoritesByRate(int rate) {
		List<Favorite> favoriteList = new ArrayList<>();

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(SGDatabases._C_ENABLED).append("=?").append(" AND ").append(SGDatabases._C_RATE).append("=?");

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE, null, whereClause.toString(), new String[]{"1", String.valueOf(rate)}, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			Favorite favorite = new Favorite();
			favorite.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			favorite.setParentId(c.getInt(c.getColumnIndex(SGDatabases._C_PARENT)));
			favorite.setSentence(c.getString(c.getColumnIndex(SGDatabases._C_SENTENCE)));
			favorite.setRate(c.getInt(c.getColumnIndex(SGDatabases._C_RATE)));
			favorite.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			favorite.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			favorite.setEnabled(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_ENABLED))));
			favorite.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			favoriteList.add(favorite);
		}
		return favoriteList;
	}

	@Override
	public List<Favorite> getNoBackupFavorite() {
		List<Favorite> favoriteList = new ArrayList<>();

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE, null, SGDatabases._C_BACKUP + "=?", new String[]{"0"}, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			Favorite favorite = new Favorite();
			favorite.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			favorite.setParentId(c.getInt(c.getColumnIndex(SGDatabases._C_PARENT)));
			favorite.setSentence(c.getString(c.getColumnIndex(SGDatabases._C_SENTENCE)));
			favorite.setRate(c.getInt(c.getColumnIndex(SGDatabases._C_RATE)));
			favorite.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			favorite.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			favorite.setEnabled(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_ENABLED))));
			favorite.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			favoriteList.add(favorite);
		}
		return favoriteList;
	}

	@Override
	public int getFavoriteCountInRate(int rate) {
		String whereClause = SGDatabases._C_ENABLED + "=?" + " AND " + SGDatabases._C_RATE + "=?";

		String rateValue = String.valueOf(rate);
		String[] whereArgs = new String[] { "1", rateValue };

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE, null, whereClause, whereArgs, null, null, null);
		return c.getCount();
	}

	@Override
	public int getFavoriteCountInCategory(int parentId) {
		String whereClause = SGDatabases._C_ENABLED + "=?" + " AND " + SGDatabases._C_PARENT + "=?";

		String categoryId = String.valueOf(parentId);
		String[] whereArgs = new String[] { "1", categoryId };

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE, null, whereClause, whereArgs, null, null, null);
		return c.getCount();
	}

	@Override
	public void delete(Favorite favorite) {
		delete(favorite.getId());
	}

	@Override
	public void delete(int id) {
		String favoriteId = String.valueOf(id);
		helper.getWDb().delete(SGDatabases._T_FAVORITE, SGDatabases.CreateDB._ID + "=?", new String[]{favoriteId});
	}

	@Override
	public void update(Favorite favorite) {
		ContentValues newValues = new ContentValues();
		newValues.put(SGDatabases._C_PARENT, favorite.getParentId());
		newValues.put(SGDatabases._C_SENTENCE, favorite.getSentence());
		newValues.put(SGDatabases._C_RATE, favorite.getRate());
		newValues.put(SGDatabases._C_C_DATE, favorite.getCreateDate());
		newValues.put(SGDatabases._C_BACKUP, favorite.isBackup());
		newValues.put(SGDatabases._C_ENABLED, favorite.isEnabled());
		newValues.put(SGDatabases._C_GENRE, favorite.getGenreType().getIndexCode());

		helper.getWDb().update(SGDatabases._T_FAVORITE, newValues, SGDatabases.CreateDB._ID + "=?", new String[]{String.valueOf(favorite.getId())});
	}

	@Override
	public void insert(Favorite favorite) {
		ContentValues newValues = new ContentValues();
		newValues.put(SGDatabases._C_PARENT, favorite.getParentId());
		newValues.put(SGDatabases._C_SENTENCE, favorite.getSentence());
		newValues.put(SGDatabases._C_RATE, favorite.getRate());
		newValues.put(SGDatabases._C_C_DATE, favorite.getCreateDate());
		newValues.put(SGDatabases._C_BACKUP, favorite.isBackup());
		newValues.put(SGDatabases._C_ENABLED, favorite.isEnabled());
		newValues.put(SGDatabases._C_GENRE, favorite.getGenreType().getIndexCode());

		long newId = helper.getWDb().insert(SGDatabases._T_FAVORITE, null, newValues);

		favorite.setId((int) newId);
	}

	@Override
	public void insert(List<Favorite> favorites) {
		if (favorites != null) {
			for (Favorite favorite : favorites) {
				insert(favorite);
			}
		}
	}

	@Override
	public List<FavoriteCategory> getFavoriteCategories() {
		List<FavoriteCategory> favoriteCategories = new ArrayList<>();

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE_CATEGORY, null, SGDatabases._C_ENABLED + "=?", new String[]{"1"}, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			FavoriteCategory favoriteCategory = new FavoriteCategory();
			favoriteCategory.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			favoriteCategory.setName(c.getString(c.getColumnIndex(SGDatabases._C_NAME)));
			favoriteCategory.setRate(c.getInt(c.getColumnIndex(SGDatabases._C_RATE)));
			favoriteCategory.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			favoriteCategory.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			favoriteCategory.setEnabled(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_ENABLED))));
			favoriteCategory.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			favoriteCategories.add(favoriteCategory);
		}
		return favoriteCategories;
	}

	@Override
	public List<FavoriteCategory> getFavoriteCategoriesByRate(int rate) {
		List<FavoriteCategory> favoriteCategories = new ArrayList<>();

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(SGDatabases._C_ENABLED).append("=?").append(" AND ").append(SGDatabases._C_RATE).append("=?");

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE_CATEGORY, null, whereClause.toString(), new String[]{"1", String.valueOf(rate)}, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			FavoriteCategory favoriteCategory = new FavoriteCategory();
			favoriteCategory.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			favoriteCategory.setName(c.getString(c.getColumnIndex(SGDatabases._C_NAME)));
			favoriteCategory.setRate(c.getInt(c.getColumnIndex(SGDatabases._C_RATE)));
			favoriteCategory.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			favoriteCategory.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			favoriteCategory.setEnabled(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_ENABLED))));
			favoriteCategory.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			favoriteCategories.add(favoriteCategory);
		}
		return favoriteCategories;
	}

	@Override
	public List<FavoriteCategory> getNoBackupFavoriteCategory() {
		List<FavoriteCategory> favoriteCategories = new ArrayList<>();

		@SuppressLint("Recycle")
		Cursor c = helper.getRDb().query(SGDatabases._T_FAVORITE_CATEGORY, null, SGDatabases._C_BACKUP + "=?", new String[]{"0"}, null, null, SGDatabases._C_C_DATE + " DESC");

		while(c.moveToNext()) {
			FavoriteCategory favoriteCategory = new FavoriteCategory();
			favoriteCategory.setId(c.getInt(c.getColumnIndex(SGDatabases.CreateDB._ID)));
			favoriteCategory.setName(c.getString(c.getColumnIndex(SGDatabases._C_NAME)));
			favoriteCategory.setRate(c.getInt(c.getColumnIndex(SGDatabases._C_RATE)));
			favoriteCategory.setBackup(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_BACKUP))));
			favoriteCategory.setCreateDate(c.getString(c.getColumnIndex(SGDatabases._C_C_DATE)));
			favoriteCategory.setEnabled(BoolConverter.intToBool(c.getInt(c.getColumnIndex(SGDatabases._C_ENABLED))));
			favoriteCategory.setGenreType(TypeConverter.intToGenreType(c.getInt(c.getColumnIndex(SGDatabases._C_GENRE))));

			favoriteCategories.add(favoriteCategory);
		}
		return favoriteCategories;
	}

	@Override
	public void insertCategory(FavoriteCategory category) {
		ContentValues newValues = new ContentValues();
		newValues.put(SGDatabases._C_NAME, category.getName());
		newValues.put(SGDatabases._C_C_DATE, category.getCreateDate());
		newValues.put(SGDatabases._C_BACKUP, category.isBackup());
		newValues.put(SGDatabases._C_ENABLED, category.isEnabled());
		newValues.put(SGDatabases._C_GENRE, category.getGenreType().getIndexCode());

		long newId = helper.getWDb().insert(SGDatabases._T_FAVORITE_CATEGORY, null, newValues);

		category.setId((int) newId);
	}

	@Override
	public void insertCategory(List<FavoriteCategory> categories) {
		if (categories != null) {
			for (FavoriteCategory category : categories) {
				insertCategory(category);
			}
		}
	}

	@Override
	public void updateCategory(FavoriteCategory category) {
		ContentValues newValues = new ContentValues();
		newValues.put(SGDatabases._C_NAME, category.getName());
		newValues.put(SGDatabases._C_C_DATE, category.getCreateDate());
		newValues.put(SGDatabases._C_BACKUP, category.isBackup());
		newValues.put(SGDatabases._C_ENABLED, category.isEnabled());
		newValues.put(SGDatabases._C_GENRE, category.getGenreType().getIndexCode());

		String categoryId = String.valueOf(category.getId());

		helper.getWDb().update(SGDatabases._T_FAVORITE_CATEGORY, newValues, SGDatabases.CreateDB._ID + "=?", new String[]{categoryId});
	}

	@Override
	public void deleteCategory(FavoriteCategory category) {
		deleteCategory(category.getId());
	}

	@Override
	public void deleteCategory(int id) {
		String favoriteId = String.valueOf(id);
		helper.getWDb().delete(SGDatabases._T_FAVORITE_CATEGORY, SGDatabases.CreateDB._ID + "=?", new String[]{favoriteId});
	}
}
