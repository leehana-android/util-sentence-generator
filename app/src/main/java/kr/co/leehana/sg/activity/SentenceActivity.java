package kr.co.leehana.sg.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.model.Favorite;
import kr.co.leehana.sg.model.FavoriteCategory;
import kr.co.leehana.sg.service.FavoriteServiceImpl;
import kr.co.leehana.sg.service.IFavoriteService;
import kr.co.leehana.sg.service.IWordService;
import kr.co.leehana.sg.service.WordServiceImpl;
import kr.co.leehana.sg.type.GenreType;
import kr.co.leehana.sg.utils.SentenceGenerator;
import kr.co.leehana.sg.utils.StringUtils;

public class SentenceActivity extends AppCompatActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	private IFavoriteService mFavoriteService;
	private IWordService mWordService;

	private Fragment mFavoriteFragment;
	private Fragment mSentenceFragment;

	private int mCurrentTabPosition = 0;

	private Menu mOptionsMenu;
	private List<Spanned> mTempFavoriteList = new ArrayList<>();
	private List<ActionBar.Tab> tabs = new ArrayList<>(2);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sentence);

		mFavoriteService = FavoriteServiceImpl.getInstance();
		((FavoriteServiceImpl) mFavoriteService).setHelper(DbHelperFactory.create(getBaseContext()));
		mWordService = WordServiceImpl.getInstance();
		((WordServiceImpl) mWordService).setHelper(DbHelperFactory.create(getBaseContext()));

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

//		actionBar.setBackgroundDrawable(new ColorDrawable(AppContext.ACTION_BAR_COLOR));
//		actionBar.setStackedBackgroundDrawable(new ColorDrawable(AppContext.ACTION_BAR_TAB_COLOR));
//		actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.RED));

		String actionBarTitle = getString(R.string.title_activity_sentence, getString(AppContext.getInstance().getGenreType().getStringCode()));
		actionBar.setTitle(actionBarTitle);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			ActionBar.Tab newTab = actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this);
			tabs.add(newTab);
			actionBar.addTab(newTab);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (getFavoriteItemCount() > 0) {
			saveFavoriteItems(getCurrentDateTime());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mOptionsMenu = menu;
		// Inflate the menu; this adds items to the action bar if it is present.
		if (mCurrentTabPosition == 0) {
			getMenuInflater().inflate(R.menu.menu_sentence, menu);

			SubMenu subMenu = menu.getItem(0).getSubMenu();
			subMenu.clear();

			for (int i = 0; i < GenreType.values().length; i++) {
				subMenu.add(0, GenreType.values()[i].hashCode(), Menu.NONE, getString(GenreType.values()[i].getStringCode()));
			}
		} else if (mCurrentTabPosition == 1) {
			getMenuInflater().inflate(R.menu.menu_sentence_favorite, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int itemId = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (itemId == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		} else if (itemId == R.id.action_refresh) {
			List<Spanned> newSentence = generateSentence();
			updateSentenceListView(newSentence);
			updateSentenceTabTitle(newSentence);
		} else if (itemId == GenreType.POETRY.hashCode()) {
			AppContext.getInstance().setGenreType(GenreType.POETRY);
			updateActionBarTitle();
		} else if (itemId == GenreType.NURSERY_RIME.hashCode()) {
			AppContext.getInstance().setGenreType(GenreType.NURSERY_RIME);
			updateActionBarTitle();
		} else if (itemId == GenreType.ESSAY.hashCode()) {
			AppContext.getInstance().setGenreType(GenreType.ESSAY);
			updateActionBarTitle();
		} else if (itemId == GenreType.ETC.hashCode()) {
			AppContext.getInstance().setGenreType(GenreType.ETC);
			updateActionBarTitle();
		} else if (itemId == GenreType.NOVEL.hashCode()) {
			AppContext.getInstance().setGenreType(GenreType.NOVEL);
			updateActionBarTitle();
		} else if (itemId == GenreType.FAIRY_TALE.hashCode()) {
			AppContext.getInstance().setGenreType(GenreType.FAIRY_TALE);
			updateActionBarTitle();
		} else if (itemId == R.id.action_save) {
			if (getFavoriteItemCount() > 0) {
				showSaveDialog();
			} else {
				showItemNotFoundWarningDialog();
			}
		} else if (itemId == R.id.action_delete) {
			if (getFavoriteItemCount() > 0) {
				showDeleteConfirmAlertDialog();
			} else {
				showNoSelectSentenceDialog();
			}
		} else if (itemId == R.id.action_rate) {
			if (getFavoriteItemCount() > 0) {
				showRateDialog();
			} else {
				showNoSelectSentenceDialog();
			}
		}

		return super.onOptionsItemSelected(item);
	}

	private void showRateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.rate_dialog_title);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.love_heart_48);

		final RatingBar ratingBar = new RatingBar(this);

		ratingBar.setMax(5);
		ratingBar.setNumStars(5);
		ratingBar.setStepSize(1.0f);
		ratingBar.setRating(1.0f);

		final LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		layout.addView(ratingBar);

		builder.setView(layout);
		builder.setPositiveButton(R.string.save_dialog_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				float ratingValue = ratingBar.getRating();

				if (mFavoriteFragment != null && mFavoriteFragment.getView() != null) {
					ListView favoriteListView = (ListView) mFavoriteFragment.getView().findViewById(android.R.id.list);
					int totalItemCount = favoriteListView.getAdapter().getCount();

					for (int i = 0; i < totalItemCount; i++) {
						if (favoriteListView.isItemChecked(i)) {
							Spanned selectedItemText = (Spanned) favoriteListView.getItemAtPosition(i);
							int itemIndex = mTempFavoriteList.indexOf(selectedItemText);
							Float floatValue = ratingValue;
							selectedItemText = (Spanned) TextUtils.concat(selectedItemText, AppContext.FAVORITE_RATE[floatValue.intValue() - 1]);
							mTempFavoriteList.remove(itemIndex);
							mTempFavoriteList.add(itemIndex, selectedItemText);

							CheckedTextView view = (CheckedTextView) favoriteListView.getAdapter().getView(i, favoriteListView.findViewById(R.id.generated_list_item), null);
							view.setTag(floatValue.intValue());

							favoriteListView.setItemChecked(i, false);
						}
					}
				}
			}
		});

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	private void showDeleteConfirmAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.delete_dialog_title);
		builder.setMessage(R.string.delete_msg);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.love_heart_48);

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mFavoriteFragment != null && mFavoriteFragment.getView() != null) {
					ListView favoriteListView = (ListView) mFavoriteFragment.getView().findViewById(android.R.id.list);
					int totalItemCount = favoriteListView.getAdapter().getCount();

					for (int i = 0; i < totalItemCount; i++) {
						if (favoriteListView.isItemChecked(i)) {
							Spanned selectedItemText = (Spanned) favoriteListView.getItemAtPosition(i);
							mTempFavoriteList.remove(selectedItemText);
						}
					}

					showToastMessage(getBaseContext(), getString(R.string.delete_done));
					updateFavoriteListView(mTempFavoriteList);
					sentenceListViewCheckedItemCleanUp();
					updateFavoriteTabTitle();
				}
			}
		});

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	private void sentenceListViewCheckedItemCleanUp() {
		if (mSentenceFragment != null && mSentenceFragment.getView() != null) {
			ListView sentenceListView = (ListView) mSentenceFragment.getView().findViewById(android.R.id.list);
			int totalItemCount = sentenceListView.getAdapter().getCount();

			for (int i = 0; i < totalItemCount; i++) {
				if (sentenceListView.isItemChecked(i)) {
					Spanned selectedItemText = (Spanned) sentenceListView.getItemAtPosition(i);
					if (!mTempFavoriteList.contains(selectedItemText)) {
						sentenceListView.setItemChecked(i, false);
					}
				}
			}
		}
	}

	private void showNoSelectSentenceDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(R.string.warning_title);
		alertDialog.setMessage(getString(R.string.no_select_sentence));
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialog.show();
	}

	private void showItemNotFoundWarningDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(R.string.warning_title);
		alertDialog.setMessage(getString(R.string.no_favorite_item));
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialog.show();
	}

	private void showSaveDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.save_dialog_title);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.love_heart_48);
		builder.setMessage(R.string.save_dialog_msg);

		final EditText input = new EditText(this);

		builder.setView(input);
		builder.setPositiveButton(R.string.save_dialog_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveFavoriteItems(input.getText().toString());
			}
		});

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	private String getCurrentDateTime() {
		@SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppContext.DATE_TIME_PATTERN);

		return simpleDateFormat.format(Calendar.getInstance(Locale.getDefault()).getTime());
	}

	private void saveFavoriteItems(String favoriteCategoryName) {
		if (mFavoriteFragment != null && mFavoriteFragment.getView() != null) {
			ListView favoriteListView = (ListView) mFavoriteFragment.getView().findViewById(android.R.id.list);
			int totalItemCount = getFavoriteItemCount();

			if (totalItemCount > 0) {

				if (StringUtils.isBlank(favoriteCategoryName)) {
					favoriteCategoryName = getCurrentDateTime();
				}

				FavoriteCategory favoriteCategory = new FavoriteCategory();
				favoriteCategory.setName(favoriteCategoryName);
				favoriteCategory.setGenreType(AppContext.getInstance().getGenreType());
				favoriteCategory.setCreateDate(String.valueOf(System.currentTimeMillis()));
				favoriteCategory.setBackup(false);
				favoriteCategory.setEnabled(true);

				mFavoriteService.insertCategory(favoriteCategory);

				long saveNewFavoriteTime = System.currentTimeMillis();
				for (int i = 0; i < totalItemCount; i++) {
					Spanned sentenceItemText = (Spanned) favoriteListView.getItemAtPosition(i);
					Favorite newFavorite = new Favorite();

					int ratingValue = StringUtils.countMatches(sentenceItemText, "★");

					if (ratingValue == 0) {
						ratingValue = 1;
					}

					String sentenceItemString = StringUtils.removeStarCharacter(sentenceItemText);

					newFavorite.setSentence(sentenceItemString.replaceAll("\n", ""));
					newFavorite.setParentId(favoriteCategory.getId());
					newFavorite.setRate(ratingValue);
					newFavorite.setEnabled(true);
					newFavorite.setBackup(false);
					newFavorite.setGenreType(AppContext.getInstance().getGenreType());
					newFavorite.setCreateDate(String.valueOf(saveNewFavoriteTime));

					mFavoriteService.insert(newFavorite);
				}

				mTempFavoriteList.clear();

				resetSentenceListView();
				updateFavoriteTabTitle();

				showToastMessage(getBaseContext(), getString(R.string.save_sentence_category_done));
			} else {
				showToastMessage(getBaseContext(), getString(R.string.save_data_empty));
			}
		}
	}

	private void resetSentenceListView() {
		if (mSentenceFragment != null && mSentenceFragment.getView() != null) {
			ListView sentenceListView = (ListView) mSentenceFragment.getView().findViewById(android.R.id.list);
			for (int i = 0; i < sentenceListView.getAdapter().getCount(); i++) {
				sentenceListView.setItemChecked(i, false);
			}
		}
	}

	private int getFavoriteItemCount() {
		if (mFavoriteFragment != null && mFavoriteFragment.getView() != null) {
			ListView favoriteListView = (ListView) mFavoriteFragment.getView().findViewById(android.R.id.list);
			return favoriteListView.getAdapter().getCount();
		}
		return 0;
	}

	private void showToastMessage(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mCurrentTabPosition = tab.getPosition();

		if (mCurrentTabPosition == 1) {
			mFavoriteFragment = mSectionsPagerAdapter.getItem(tab.getPosition());
			fragmentTransaction.attach(mFavoriteFragment);

			if (mFavoriteFragment.getView() != null) {
				((ListView) mFavoriteFragment.getView().findViewById(android.R.id.list)).setAdapter(createSentenceArrayAdapter(mFavoriteFragment.getActivity().getBaseContext(), mTempFavoriteList));
			}

			if (mOptionsMenu != null) {
				invalidateOptionsMenu();
				getMenuInflater().inflate(R.menu.menu_sentence_favorite, mOptionsMenu);
			}
		} else if (mCurrentTabPosition == 0) {
			mSentenceFragment = mSectionsPagerAdapter.getItem(tab.getPosition());
			fragmentTransaction.attach(mSentenceFragment);

			if (mOptionsMenu != null) {
				invalidateOptionsMenu();
				getMenuInflater().inflate(R.menu.menu_sentence, mOptionsMenu);
			}
		}

		mViewPager.setCurrentItem(mCurrentTabPosition);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragments = new ArrayList<>(4);

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			Fragment fragment;
			if (fragments.isEmpty() || fragments.size() <= position) {
				fragment = new PlaceholderFragment(position);
				Bundle args = new Bundle();
				args.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position);
				fragment.setArguments(args);

				fragments.add(position, fragment);
			} else {
				fragment = fragments.get(position);
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(R.string.title_generated).toUpperCase(l);
				case 1:
					return getString(R.string.title_favorite).toUpperCase(l);
				case 2:
			}
			return null;
		}
	}

	private void updateActionBarTitle() {
		String actionBarTitle = getString(R.string.title_activity_sentence, getString(AppContext.getInstance().getGenreType().getStringCode()));
		getSupportActionBar().setTitle(actionBarTitle);
	}

	private List<Spanned> generateSentence() {
		return new SentenceGenerator(AppContext.getInstance().getGenreType(), mWordService).generate();
	}

	private void updateFavoriteListView(List<Spanned> generatedSentence) {
		if (mFavoriteFragment != null && mFavoriteFragment.getView() != null) {
			updateListView(mFavoriteFragment, generatedSentence);
		}
	}

	private void updateSentenceListView(List<Spanned> generatedSentence) {
		if (mSentenceFragment != null && mSentenceFragment.getView() != null) {
			updateListView(mSentenceFragment, generatedSentence);
		}
	}

	private void updateListView(Fragment fragment, List<Spanned> data) {
		ListView listView = (ListView) fragment.getView().findViewById(android.R.id.list);
		listView.invalidate();
		listView.setAdapter(createSentenceArrayAdapter(fragment.getActivity().getBaseContext(), data));
	}

	private ArrayAdapter<Spanned> createSentenceArrayAdapter(Context context, List<Spanned> data) {
		return new ArrayAdapter<>(context, R.layout.generated_list_item, data);
	}

	private ArrayAdapter<Spanned> createFavoriteArrayAdapter(Context context, List<Spanned> data) {
		return new ArrayAdapter<>(context, R.layout.generated_list_item, data);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("ValidFragment")
	class PlaceholderFragment extends ListFragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private int currentPosition;
		private CheckedTextView checkedTextView;

		public PlaceholderFragment(int currentPosition) {
			this.currentPosition = currentPosition;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			if (currentPosition == 0) {
				List<Spanned> sentenceList = generateSentence();
				setListAdapter(createSentenceArrayAdapter(getActivity().getBaseContext(), sentenceList));
				updateSentenceTabTitle(sentenceList);
			} else if (currentPosition == 1) {
				setListAdapter(createFavoriteArrayAdapter(getActivity().getBaseContext(), mTempFavoriteList));
				updateFavoriteTabTitle();
			}

			return inflater.inflate(R.layout.fragment_sentence, container, false);
		}

		@Override
		public void onStart() {
			super.onStart();

			/** Setting the multiselect choice mode for the listview */
			getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);

			if (currentPosition == 0 && v.getId() == R.id.generated_list_item) {
				CheckedTextView checkedTextView = (CheckedTextView) v;

				boolean checked = AppContext.isGingerBread() ? !checkedTextView.isChecked() : checkedTextView.isChecked();

				if (checked) {
					Spanned spannedText = (Spanned) checkedTextView.getText();
					mTempFavoriteList.add(spannedText);
				} else {
					if (!mTempFavoriteList.isEmpty()) {
						Spanned removeTargetString = null;
						for (Spanned favoriteString : mTempFavoriteList) {
							String favoriteItemString = StringUtils.removeStarCharacter(favoriteString);

							if (favoriteItemString.equals(Html.toHtml((Spanned) checkedTextView.getText()))) {
								removeTargetString = favoriteString;
								break;
							}
						}

						if (StringUtils.isNotBlank(removeTargetString)) {
							mTempFavoriteList.remove(removeTargetString);
						}
					}
				}

				updateFavoriteTabTitle();
			}
		}
	}

	private void updateSentenceTabTitle(List<Spanned> sentenceList) {
		tabs.get(0).setText(getString(R.string.title_generated, sentenceList.size()));
	}

	private void updateFavoriteTabTitle() {
		tabs.get(1).setText(getString(R.string.title_favorite, mTempFavoriteList.size()));
	}
}
