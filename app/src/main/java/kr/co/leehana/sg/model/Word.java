package kr.co.leehana.sg.model;

import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.type.WordType;

/**
 * Created by Hana Lee on 2015-08-13 03:27
 *
 * @author Hana Lee
 * @since 2015-08-13 03:27
 */
public class Word {

	private int id;
	private String word;
	private WordType type;
	private GenreType genreType;
	private String createDate;
	private boolean enabled = true;
	private boolean modified = false;
	private boolean backup = true;

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

	public WordType getType() {
		return type;
	}

	public void setType(WordType type) {
		this.type = type;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Word word1 = (Word) o;

		if (id != word1.id) return false;
		if (enabled != word1.enabled) return false;
		if (modified != word1.modified) return false;
		if (backup != word1.backup) return false;
		if (!word.equals(word1.word)) return false;
		if (type != word1.type) return false;
		if (genreType != word1.genreType) return false;
		return createDate.equals(word1.createDate);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + word.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + genreType.hashCode();
		result = 31 * result + createDate.hashCode();
		result = 31 * result + (enabled ? 1 : 0);
		result = 31 * result + (modified ? 1 : 0);
		result = 31 * result + (backup ? 1 : 0);
		return result;
	}
}
