package kr.co.leehana.sg.type;

import kr.co.leehana.sg.R;

/**
 * Created by Hana Lee on 2015-08-17 00:06
 *
 * @author Hana Lee
 * @since 2015-08-17 00:06
 */
public enum SentenceGenerateType {

	NOUN(0, R.string.noun), VERB(1, R.string.verb), ADVERB(2, R.string.adverb), ADJECTIVE(3, R.string.adjective), RANDOM(4, R.string.random), NONE(5, R.string.empty_word);

	private int indexCode;
	private int stringCode;

	SentenceGenerateType(int indexCode, int stringCode) {
		this.indexCode = indexCode;
		this.stringCode = stringCode;
	}

	public int getIndexCode() {
		return indexCode;
	}

	public int getStringCode() {
		return stringCode;
	}
}
