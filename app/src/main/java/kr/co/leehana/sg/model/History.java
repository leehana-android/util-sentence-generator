package kr.co.leehana.sg.model;

import kr.co.leehana.sg.type.GenreType;

/**
 * Created by Hana Lee on 2015-08-13 03:22
 *
 * @author Hana Lee
 * @since 2015-08-13 03:22
 */
public class History {

	private int id;
	private String sentence;
	private GenreType genreType;
	private String createDate;
	private boolean backup = true;

	public History() {
	}

	public History(boolean backup, String createDate, GenreType genreType, int id, String sentence) {
		this.backup = backup;
		this.createDate = createDate;
		this.genreType = genreType;
		this.id = id;
		this.sentence = sentence;
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

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		History history = (History) o;

		if (id != history.id) return false;
		if (backup != history.backup) return false;
		if (!sentence.equals(history.sentence)) return false;
		if (genreType != history.genreType) return false;
		return createDate.equals(history.createDate);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + sentence.hashCode();
		result = 31 * result + genreType.hashCode();
		result = 31 * result + createDate.hashCode();
		result = 31 * result + (backup ? 1 : 0);
		return result;
	}
}
