package kr.co.leehana.sg.model;

import kr.co.leehana.sg.type.GenreType;

/**
 * Created by Hana Lee on 2015-08-13 03:22
 *
 * @author Hana Lee
 * @since 2015-08-13 03:22
 */
public class Favorite {

	private int id;
	private int parentId;
	private String sentence;
	private GenreType genreType;
	private int rate = 1;
	private String createDate;
	private boolean backup = true;
	private boolean modified = false;
	private boolean enabled;

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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
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

		Favorite favorite = (Favorite) o;

		if (id != favorite.id) return false;
		if (parentId != favorite.parentId) return false;
		if (rate != favorite.rate) return false;
		if (backup != favorite.backup) return false;
		if (modified != favorite.modified) return false;
		if (enabled != favorite.enabled) return false;
		if (!sentence.equals(favorite.sentence)) return false;
		if (genreType != favorite.genreType) return false;
		return createDate.equals(favorite.createDate);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + parentId;
		result = 31 * result + sentence.hashCode();
		result = 31 * result + genreType.hashCode();
		result = 31 * result + rate;
		result = 31 * result + createDate.hashCode();
		result = 31 * result + (backup ? 1 : 0);
		result = 31 * result + (modified ? 1 : 0);
		result = 31 * result + (enabled ? 1 : 0);
		return result;
	}
}
