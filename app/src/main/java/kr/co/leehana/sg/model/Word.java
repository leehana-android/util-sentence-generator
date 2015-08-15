package kr.co.leehana.sg.model;

/**
 * Created by Hana Lee on 2015-08-12 23:14
 *
 * @author Hana Lee
 * @since 2015-08-12 23:14
 */
public abstract class Word {

	protected String[] data;

	public abstract String[] getData();

	public int getCount() {
		return getData().length;
	}
}
