package kr.co.leehana.sg.model;

/**
 * Created by Hana Lee on 2015-08-15 09:03
 *
 * @author Hana Lee
 * @since 2015-08-15 09:03
 */
public class FavoriteRate {

	private int rate;
	private int itemCount;

	public FavoriteRate() {
	}

	public FavoriteRate(int rate, int itemCount) {
		this.itemCount = itemCount;
		this.rate = rate;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FavoriteRate that = (FavoriteRate) o;

		if (rate != that.rate) return false;
		return itemCount == that.itemCount;

	}

	@Override
	public int hashCode() {
		int result = rate;
		result = 31 * result + itemCount;
		return result;
	}
}
