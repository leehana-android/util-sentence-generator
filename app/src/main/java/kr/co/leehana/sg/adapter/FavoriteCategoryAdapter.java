package kr.co.leehana.sg.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.model.FavoriteCategory;
import kr.co.leehana.sg.service.FavoriteServiceImpl;
import kr.co.leehana.sg.service.IFavoriteService;
import kr.co.leehana.sg.utils.DbUtils;

/**
 * Created by Hana Lee on 2015-08-14 23:16
 *
 * @author Hana Lee
 * @since 2015-08-14 23:16
 */
public class FavoriteCategoryAdapter extends ArrayAdapter<FavoriteCategory> {

	private LayoutInflater layoutInflater;
	private IFavoriteService favoriteService;

	public FavoriteCategoryAdapter(Context context, int resource, List<FavoriteCategory> objects) {
		super(context, resource, objects);
		layoutInflater = LayoutInflater.from(context);
		favoriteService = FavoriteServiceImpl.getInstance();
		((FavoriteServiceImpl) favoriteService).setHelper(DbHelperFactory.create(context, DbUtils.LOCAL_DATABASE_NAME));
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Holder holder;

		FavoriteCategory favoriteCategory = getItem(position);

		if (view == null) {
			view = layoutInflater.inflate(R.layout.favorite_category_item, null);

			ImageView genreImage = (ImageView) view.findViewById(R.id.genre_image);

			switch (favoriteCategory.getGenreType()) {
				case POETRY:
					genreImage.setBackgroundResource(R.drawable.indi_poetry);
					break;
				case NURSERY_RIME:
					genreImage.setBackgroundResource(R.drawable.indi_nusery_rime);
					break;
				case ESSAY:
					genreImage.setBackgroundResource(R.drawable.indi_essay);
					break;
				case FAIRY_TALE:
					genreImage.setBackgroundResource(R.drawable.indi_fairy_tail);
					break;
				case NOVEL:
					genreImage.setBackgroundResource(R.drawable.indi_novel);
					break;
				case ETC:
					genreImage.setBackgroundResource(R.drawable.indi_star);
					break;
			}

			TextView name = (TextView) view.findViewById(R.id.category_name);
			TextView created = (TextView) view.findViewById(R.id.created_date);

			holder = new Holder();
			holder.setGenreImage(genreImage);
			holder.setName(name);
			holder.setCreated(created);

			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		holder.getName().setText(favoriteCategory.getName() + " (" + favoriteService.getFavoriteCountInCategory(favoriteCategory.getId()) + ")");
		holder.getCreated().setText(getFormattedDateTime(Long.parseLong(favoriteCategory.getCreateDate())));

		return view;
	}



	private String getFormattedDateTime(long timestamp) {
		@SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppContext.DATE_TIME_PATTERN);

		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTimeInMillis(timestamp);

		return simpleDateFormat.format(calendar.getTime());
	}

	private class Holder {
		private ImageView genreImage;
		private TextView name;
		private TextView created;

		public Holder() {
		}

		public Holder(TextView created, ImageView genreImage, TextView name) {
			this.created = created;
			this.genreImage = genreImage;
			this.name = name;
		}

		public TextView getCreated() {
			return created;
		}

		public void setCreated(TextView created) {
			this.created = created;
		}

		public ImageView getGenreImage() {
			return genreImage;
		}

		public void setGenreImage(ImageView genreImage) {
			this.genreImage = genreImage;
		}

		public TextView getName() {
			return name;
		}

		public void setName(TextView name) {
			this.name = name;
		}
	}
}
