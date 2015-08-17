package kr.co.leehana.sg.context;

import android.graphics.Color;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import kr.co.leehana.sg.model.Setting;
import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.type.SentenceGenerateType;
import kr.co.leehana.sg.type.WordType;

/**
 * Created by Hana Lee on 2015-08-13 20:59
 *
 * @author <a href="mailto:i@leehana.co.kr">Hana Lee</a>
 * @since 2015-08-13 20:59
 */
public class AppContext {

	public static final String BTN_CODE = "sg_btn";
	public static final String CATEGORY_ID = "c_id";
	public static final String RATE_CODE = "r_code";
	public static final int ACTION_BAR_COLOR = Color.rgb(255, 102, 0); //blaze orange
	public static final int ACTION_BAR_TAB_COLOR = Color.rgb(8, 157, 227); //blaze orange
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
	public static final String [] FAVORITE_RATE = {"★☆☆☆☆","★★☆☆☆","★★★☆☆","★★★★☆","★★★★★"};
	public static final int SETTING_SENTENCE_COUNT_IDX = 0;
	public static final int SETTING_FIRST_WORD_IDX = 1;
	public static final int SETTING_SECOND_WORD_IDX = 2;
	public static final int SETTING_THIRD_WORD_IDX = 3;
	public static final int SETTING_FOURTH_WORD_IDX = 4;
	public static final List<Integer> SENTENCE_COUNT_ARRAY = new ArrayList<>();

	static {
		SENTENCE_COUNT_ARRAY.add(20);
		SENTENCE_COUNT_ARRAY.add(30);
		SENTENCE_COUNT_ARRAY.add(40);
		SENTENCE_COUNT_ARRAY.add(50);
	}

	private static AppContext context = new AppContext();

	private State state = State.SHOW_MAIN;

	private GenreType genreType = GenreType.POETRY;

	private WordType wordType = WordType.NOUN;

	private boolean isFavoriteRateView = false;

	private boolean isFavoriteCategoryView = false;

	private int favoriteViewIndex = -1;

	private Setting setting;

	private AppContext() {}

	public static AppContext getInstance() {
		return context;
	}

	public String getColorCodeForWord(WordType wordType) {
		switch (wordType) {
			case NOUN:
				return "#CD812B";
			case VERB:
				return "#F17075";
			case ADVERB:
				return "#7B6A97";
			case ADJECTIVE:
				return "#608F63";
		}

		return "";
	}

	public static boolean isGingerBread() {
		return Build.VERSION.SDK_INT == Build.VERSION_CODES.GINGERBREAD_MR1;
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}

	public int getFavoriteViewIndex() {
		return favoriteViewIndex;
	}

	public void setFavoriteViewIndex(int favoriteViewIndex) {
		this.favoriteViewIndex = favoriteViewIndex;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public GenreType getGenreType() {
		return genreType;
	}

	public void setGenreType(GenreType genreType) {
		this.genreType = genreType;
	}

	public WordType getWordType() {
		return wordType;
	}

	public void setWordType(WordType wordType) {
		this.wordType = wordType;
	}

	public boolean isFavoriteCategoryView() {
		return isFavoriteCategoryView;
	}

	public void setIsFavoriteCategoryView(boolean isFavoriteCategoryView) {
		this.isFavoriteCategoryView = isFavoriteCategoryView;
	}

	public boolean isFavoriteRateView() {
		return isFavoriteRateView;
	}

	public void setIsFavoriteRateView(boolean isFavoriteRateView) {
		this.isFavoriteRateView = isFavoriteRateView;
	}

	public enum State {
		SHOW_MAIN, NEW_WORD, SHOW_OLD_WORD, GENERATE_SENTENCE, SHOW_FAVORITE, SHOW_HISTORY
	}
}
