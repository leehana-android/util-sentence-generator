package kr.co.leehana.sg.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hana Lee on 2015-08-13 04:17
 *
 * @author Hana Lee
 * @since 2015-08-13 04:17
 */
public class BoolConverter {

	private static List<Boolean> booleanList = new ArrayList<>(2);

	static {
		booleanList.add(Boolean.FALSE);
		booleanList.add(Boolean.TRUE);
	}

	public static boolean intToBool(int value) {
		return booleanList.get(value);
	}

	public static int boolToInt(Boolean value) {
		return booleanList.indexOf(value);
//		if (value) {
//			return 1;
//		} else {
//			return 0;
//		}
	}
}
