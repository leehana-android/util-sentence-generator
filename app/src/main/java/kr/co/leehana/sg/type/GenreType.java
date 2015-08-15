package kr.co.leehana.sg.type;

import kr.co.leehana.sg.R;

/**
 * Created by Hana Lee on 2015-08-13 18:37
 *
 * @author <a href="mailto:i@leehana.co.kr">Hana Lee</a>
 * @since 2015-08-13 18:37
 */
public enum GenreType {

	POETRY(0, R.string.poetry), NURSERY_RIME(1, R.string.nursery_rime), NOVEL(2, R.string.novel), ESSAY(3, R.string.essay), FAIRY_TALE(4, R.string.fairy_tale), ETC(5, R.string.etc);

	private final int indexCode;
	private final int stringCode;

	GenreType(int indexCode, int stringCode) {
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
