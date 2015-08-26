package kr.co.leehana.sg.model;

import kr.co.leehana.sg.type.SentenceGenerateType;

/**
 * Created by Hana Lee on 2015-08-16 23:56
 *
 * @author Hana Lee
 * @since 2015-08-16 23:56
 */
public class Setting {

	private int id;
	private int sentenceCount;
	private SentenceGenerateType firstWordType;
	private SentenceGenerateType secondWordType;
	private SentenceGenerateType thirdWordType;
	private SentenceGenerateType fourthWordType;
	private String created;
	private boolean backup = true;

	public Setting() {
	}

	public Setting(boolean backup, String created, SentenceGenerateType firstWordType, SentenceGenerateType fourthWordType, int id, SentenceGenerateType secondWordType, int sentenceCount, SentenceGenerateType thirdWordType) {
		this.backup = backup;
		this.created = created;
		this.firstWordType = firstWordType;
		this.fourthWordType = fourthWordType;
		this.id = id;
		this.secondWordType = secondWordType;
		this.sentenceCount = sentenceCount;
		this.thirdWordType = thirdWordType;
	}

	public boolean isBackup() {
		return backup;
	}

	public void setBackup(boolean backup) {
		this.backup = backup;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public SentenceGenerateType getFirstWordType() {
		return firstWordType;
	}

	public void setFirstWordType(SentenceGenerateType firstWordType) {
		this.firstWordType = firstWordType;
	}

	public SentenceGenerateType getFourthWordType() {
		return fourthWordType;
	}

	public void setFourthWordType(SentenceGenerateType fourthWordType) {
		this.fourthWordType = fourthWordType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SentenceGenerateType getSecondWordType() {
		return secondWordType;
	}

	public void setSecondWordType(SentenceGenerateType secondWordType) {
		this.secondWordType = secondWordType;
	}

	public int getSentenceCount() {
		return sentenceCount;
	}

	public void setSentenceCount(int sentenceCount) {
		this.sentenceCount = sentenceCount;
	}

	public SentenceGenerateType getThirdWordType() {
		return thirdWordType;
	}

	public void setThirdWordType(SentenceGenerateType thirdWordType) {
		this.thirdWordType = thirdWordType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Setting setting = (Setting) o;

		if (id != setting.id) return false;
		if (sentenceCount != setting.sentenceCount) return false;
		if (backup != setting.backup) return false;
		if (firstWordType != setting.firstWordType) return false;
		if (secondWordType != setting.secondWordType) return false;
		if (thirdWordType != setting.thirdWordType) return false;
		if (fourthWordType != setting.fourthWordType) return false;
		return created.equals(setting.created);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + sentenceCount;
		result = 31 * result + firstWordType.hashCode();
		result = 31 * result + secondWordType.hashCode();
		result = 31 * result + thirdWordType.hashCode();
		result = 31 * result + fourthWordType.hashCode();
		result = 31 * result + created.hashCode();
		result = 31 * result + (backup ? 1 : 0);
		return result;
	}
}
