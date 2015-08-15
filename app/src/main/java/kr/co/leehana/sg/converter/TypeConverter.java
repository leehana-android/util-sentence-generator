package kr.co.leehana.sg.converter;

import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.type.WordType;

/**
 * Created by Hana Lee on 2015-08-13 04:20
 *
 * @author Hana Lee
 * @since 2015-08-13 04:20
 */
public class TypeConverter {

	public static WordType intToWordType(Integer value) {
		WordType selectedType = null;
		for (WordType type : WordType.values()) {
			Integer indexCode = type.getIndexCode();
			if (value.equals(indexCode)) {
				selectedType = type;
				break;
			}
		}

		return selectedType;
	}

	public static GenreType intToGenreType(Integer value) {
		GenreType selectedType = null;
		for (GenreType type : GenreType.values()) {
			Integer indexCode = type.getIndexCode();

			if (value.equals(indexCode)) {
				selectedType = type;
				break;
			}
		}

		return selectedType;
	}
}
