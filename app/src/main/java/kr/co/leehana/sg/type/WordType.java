package kr.co.leehana.sg.type;

import kr.co.leehana.sg.R;

/**
 * Created by Hana Lee on 2015-08-13 03:01
 *
 * @author Hana Lee
 * @since 2015-08-13 03:01
 */
public enum WordType {
	NOUN(0, R.string.noun), VERB(1, R.string.verb), ADVERB(2, R.string.adverb), ADJECTIVE(3, R.string.adjective);

	private int indexCode;
	private int stringCode;

	WordType(int indexCode, int stringCode) {
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
