package kr.co.leehana.sg.model;

/**
 * Created by Hana Lee on 2015-08-17 23:11
 *
 * @author Hana Lee
 * @since 2015-08-17 23:11
 */
public class Genre {

	private String name;
	private int iconResourceId;

	public Genre() {
	}

	public Genre(int iconResourceId, String name) {
		this.iconResourceId = iconResourceId;
		this.name = name;
	}

	public int getIconResourceId() {
		return iconResourceId;
	}

	public void setIconResourceId(int iconResourceId) {
		this.iconResourceId = iconResourceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
