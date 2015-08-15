package kr.co.leehana.sg.utils;

/**
 * Created by Hana Lee on 2015-08-15 09:51
 *
 * @author Hana Lee
 * @since 2015-08-15 09:51
 */
public class CharSequenceUtils {

	private static final int NOT_FOUND = -1;

	//-----------------------------------------------------------------------

	/**
	 * <p>Finds the first index in the {@code CharSequence} that matches the
	 * specified character.</p>
	 *
	 * @param cs         the {@code CharSequence} to be processed, not null
	 * @param searchChar the char to be searched for
	 * @param start      the start index, negative starts at the string start
	 * @return the index where the search char was found, -1 if not found
	 */
	static int indexOf(final CharSequence cs, final int searchChar, int start) {
		if (cs instanceof String) {
			return ((String) cs).indexOf(searchChar, start);
		}
		final int sz = cs.length();
		if (start < 0) {
			start = 0;
		}
		for (int i = start; i < sz; i++) {
			if (cs.charAt(i) == searchChar) {
				return i;
			}
		}
		return NOT_FOUND;
	}

	/**
	 * Used by the indexOf(CharSequence methods) as a green implementation of indexOf.
	 *
	 * @param cs         the {@code CharSequence} to be processed
	 * @param searchChar the {@code CharSequence} to be searched for
	 * @param start      the start index
	 * @return the index where the search sequence was found
	 */
	static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
		return cs.toString().indexOf(searchChar.toString(), start);
	}
}
