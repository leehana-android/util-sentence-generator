package kr.co.leehana.sg.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.model.FavoriteRate;

/**
 * Created by Hana Lee on 2015-08-14 23:16
 *
 * @author Hana Lee
 * @since 2015-08-14 23:16
 */
public class FavoriteRateAdapter extends ArrayAdapter<FavoriteRate> {

	private LayoutInflater layoutInflater;

	public FavoriteRateAdapter(Context context, int resource, List<FavoriteRate> objects) {
		super(context, resource, objects);
		layoutInflater = LayoutInflater.from(context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Holder holder;

		FavoriteRate favoriteRate = getItem(position);

		if (view == null) {
			view = layoutInflater.inflate(R.layout.favorite_rate_item, null);

			RatingBar ratingBar = (RatingBar) view.findViewById(R.id.favorite_rate);
			TextView count = (TextView) view.findViewById(R.id.favorite_count);

			holder = new Holder();
			holder.setRatingBar(ratingBar);
			holder.setCount(count);

			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		holder.getRatingBar().setRating(favoriteRate.getRate());
		holder.getCount().setText("(" + favoriteRate.getItemCount() + ")");

		return view;
	}

	private class Holder {
		private RatingBar ratingBar;
		private TextView count;

		public Holder() {
		}

		public Holder(TextView count, RatingBar ratingBar) {
			this.count = count;
			this.ratingBar = ratingBar;
		}

		public TextView getCount() {
			return count;
		}

		public void setCount(TextView count) {
			this.count = count;
		}

		public RatingBar getRatingBar() {
			return ratingBar;
		}

		public void setRatingBar(RatingBar ratingBar) {
			this.ratingBar = ratingBar;
		}
	}
}
