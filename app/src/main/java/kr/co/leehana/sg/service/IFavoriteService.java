package kr.co.leehana.sg.service;

import java.util.List;

import kr.co.leehana.sg.model.Favorite;
import kr.co.leehana.sg.model.FavoriteCategory;
import kr.co.leehana.sg.type.GenreType;

/**
 * Created by Hana Lee on 2015-08-13 03:19
 *
 * @author Hana Lee
 * @since 2015-08-13 03:19
 */
public interface IFavoriteService {

	List<Favorite> getFavorites();

	List<Favorite> getFavoritesByGenre(GenreType genreType);

	List<Favorite> getFavoritesByParent(FavoriteCategory parent);

	List<Favorite> getFavoritesByParentId(int parentId);

	List<Favorite> getFavoritesByRate(int rate);

	List<Favorite> getFavoritesByBackupStatus(boolean isBackup);

	List<Favorite> getFavoritesByModifyStatus(boolean isModified);

	int getFavoriteCountInRate(int rate);

	int getFavoriteCountInCategory(int parentId);

	void delete(Favorite favorite);

	void delete(int id);

	void update(Favorite favorite);

	void insert(Favorite favorite);

	void insert(List<Favorite> favorites);

	List<FavoriteCategory> getFavoriteCategories();

	List<FavoriteCategory> getFavoriteCategoriesByRate(int rate);

	List<FavoriteCategory> getFavoriteCategoriesByBackupStatus(boolean isBackup);

	List<FavoriteCategory> getFavoriteCategoriesByModifyStatus(boolean isModified);

	void insertCategory(FavoriteCategory category);

	void insertCategory(List<FavoriteCategory> categories);

	void updateCategory(FavoriteCategory category);

	void deleteCategory(FavoriteCategory category);

	void deleteCategory(int id);
}
