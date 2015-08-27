package kr.co.leehana.sg.model;

import java.util.List;

import kr.co.leehana.sg.type.GenreType;

/**
 * Created by Hana Lee on 2015-08-14 20:54
 *
 * @author Hana Lee
 * @since 2015-08-14 20:54
 */
public class FavoriteCategory {

	private int id;
	private String name;
	private GenreType genreType;
	private int rate = 1;
	private String createDate;
	private boolean backup = true;
	private boolean modified = false;
	private boolean enabled = true;

	public boolean isBackup() {
		return backup;
	}

	public void setBackup(boolean backup) {
		this.backup = backup;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public GenreType getGenreType() {
		return genreType;
	}

	public void setGenreType(GenreType genreType) {
		this.genreType = genreType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FavoriteCategory that = (FavoriteCategory) o;

		if (id != that.id) return false;
		if (rate != that.rate) return false;
		if (backup != that.backup) return false;
		if (modified != that.modified) return false;
		if (enabled != that.enabled) return false;
		if (!name.equals(that.name)) return false;
		if (genreType != that.genreType) return false;
		return createDate.equals(that.createDate);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + name.hashCode();
		result = 31 * result + genreType.hashCode();
		result = 31 * result + rate;
		result = 31 * result + createDate.hashCode();
		result = 31 * result + (backup ? 1 : 0);
		result = 31 * result + (modified ? 1 : 0);
		result = 31 * result + (enabled ? 1 : 0);
		return result;
	}
}
