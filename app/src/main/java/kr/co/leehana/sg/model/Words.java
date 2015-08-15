package kr.co.leehana.sg.model;

import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.type.WordType;

/**
 * Created by Hana Lee on 2015-08-13 03:27
 *
 * @author Hana Lee
 * @since 2015-08-13 03:27
 */
public class Words {

	private int id;
	private String word;
	private WordType type;
	private GenreType genreType;
	private String createDate;
	private boolean backup;

	public Words() {
	}

	public Words(boolean backup, String createDate, GenreType genreType, int id, WordType type, String word) {
		this.backup = backup;
		this.createDate = createDate;
		this.genreType = genreType;
		this.id = id;
		this.type = type;
		this.word = word;
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Words words = (Words) o;

		if (id != words.id) return false;
		if (backup != words.backup) return false;
		if (!word.equals(words.word)) return false;
		if (type != words.type) return false;
		if (genreType != words.genreType) return false;
		return createDate.equals(words.createDate);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + word.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + genreType.hashCode();
		result = 31 * result + createDate.hashCode();
		result = 31 * result + (backup ? 1 : 0);
		return result;
	}
}
