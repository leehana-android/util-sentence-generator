package kr.co.leehana.sg.factory;

import android.content.Context;

import kr.co.leehana.sg.db.SGSQLiteDbHelper;

/**
 * Created by Hana Lee on 2015-08-13 03:38
 *
 * @author Hana Lee
 * @since 2015-08-13 03:38
 */
public final class DbHelperFactory {

	private static SGSQLiteDbHelper helper;

	public static SGSQLiteDbHelper create(Context context) {
		if (helper == null) {
			helper = new SGSQLiteDbHelper(context);
		}

		return helper;
	}
}
