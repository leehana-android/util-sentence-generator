package kr.co.leehana.sg.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.model.Setting;
import kr.co.leehana.sg.type.SentenceGenerateType;

/**
 * Created by Hana Lee on 2015-08-16 23:55
 *
 * @author Hana Lee
 * @since 2015-08-16 23:55
 */
public class SettingsAdapter extends ArrayAdapter<Setting> {

	private LayoutInflater layoutInflater;

	private Context context;

	public SettingsAdapter(Context context, int resource, Setting[] objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 5;
	}

	@SuppressLint({"InflateParams", "StringFormatMatches"})
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		SettingHolder settingHolder;

		Setting setting = getItem(0);

		if (view == null) {
			view = layoutInflater.inflate(R.layout.settings_list_item, null);

			ImageView iconImageView = (ImageView) view.findViewById(R.id.settings_icon_image);
			TextView title = (TextView) view.findViewById(R.id.settings_item_title);
			TextView titleSub = (TextView) view.findViewById(R.id.settings_item_title_sub);

			settingHolder = new SettingHolder();
			settingHolder.setImageView(iconImageView);
			settingHolder.setTitleView(title);
			settingHolder.setSubTitleView(titleSub);

			view.setTag(settingHolder);
		} else {
			settingHolder = (SettingHolder) view.getTag();
		}

		Drawable icon = null;
		String title = null;
		String subTitle = null;
		switch (position) {
			case AppContext.SETTING_SENTENCE_COUNT_IDX:
				String colorCode = "#4B82E6";
				icon = context.getResources().getDrawable(R.drawable.settings_sentence_count);
				title = context.getString(R.string.settings_generate_count);
				subTitle = context.getString(R.string.settings_generate_count_sub, "<b><font color='" + colorCode + "'>" + setting.getSentenceCount() + "</font></b>");
				break;
			case AppContext.SETTING_FIRST_WORD_IDX:
				colorCode = getColorCode(setting.getFirstWordType());
				icon = context.getResources().getDrawable(R.drawable.settings_word);
				title = context.getString(R.string.settings_first_word);
				subTitle = context.getString(R.string.settings_first_word_sub, "<b><font color='" + colorCode + "'>" + context.getString(setting.getFirstWordType().getStringCode()) + "</font></b>");
				break;
			case AppContext.SETTING_SECOND_WORD_IDX:
				colorCode = getColorCode(setting.getSecondWordType());
				icon = context.getResources().getDrawable(R.drawable.settings_word);
				title = context.getString(R.string.settings_second_word);
				subTitle = context.getString(R.string.settings_second_word_sub, "<b><font color='" + colorCode + "'>" + context.getString(setting.getSecondWordType().getStringCode()) + "</font></b>");
				break;
			case AppContext.SETTING_THIRD_WORD_IDX:
				colorCode = getColorCode(setting.getThirdWordType());
				icon = context.getResources().getDrawable(R.drawable.settings_word);
				title = context.getString(R.string.settings_third_word);
				subTitle = context.getString(R.string.settings_third_word_sub, "<b><font color='" + colorCode + "'>" + context.getString(setting.getThirdWordType().getStringCode()) + "</font></b>");
				break;
			case AppContext.SETTING_FOURTH_WORD_IDX:
				colorCode = getColorCode(setting.getFourthWordType());
				icon = context.getResources().getDrawable(R.drawable.settings_word);
				title = context.getString(R.string.settings_fourth_word);
				subTitle = context.getString(R.string.settings_fourth_word_sub, "<b><font color='" + colorCode + "'>" + context.getString(setting.getFourthWordType().getStringCode()) + "</font></b>");
				break;
		}

		settingHolder.getImageView().setBackgroundDrawable(icon);
		settingHolder.getTitleView().setText(title);
		settingHolder.getSubTitleView().setText(Html.fromHtml(subTitle));

		return view;
	}

	private String getColorCode(SentenceGenerateType generateType) {
		if (generateType.equals(SentenceGenerateType.RANDOM)) {
			return "#D03E7C";
		} else if (generateType.equals(SentenceGenerateType.NONE)) {
			return "#447093";
		}
		return AppContext.getInstance().getColorCodeForWord(TypeConverter.intToWordType(generateType.getIndexCode()));
	}

	private class SettingHolder {
		private ImageView imageView;
		private TextView titleView;
		private TextView subTitleView;

		public SettingHolder() {
		}

		public SettingHolder(ImageView imageView, TextView subTitleView, TextView titleView) {
			this.imageView = imageView;
			this.subTitleView = subTitleView;
			this.titleView = titleView;
		}

		public ImageView getImageView() {
			return imageView;
		}

		public void setImageView(ImageView imageView) {
			this.imageView = imageView;
		}

		public TextView getSubTitleView() {
			return subTitleView;
		}

		public void setSubTitleView(TextView subTitleView) {
			this.subTitleView = subTitleView;
		}

		public TextView getTitleView() {
			return titleView;
		}

		public void setTitleView(TextView titleView) {
			this.titleView = titleView;
		}
	}
}
