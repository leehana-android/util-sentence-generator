package kr.co.leehana.sg.activity;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.adapter.FavoriteCategoryAdapter;
import kr.co.leehana.sg.adapter.FavoriteRateAdapter;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.model.FavoriteCategory;
import kr.co.leehana.sg.model.FavoriteRate;
import kr.co.leehana.sg.service.FavoriteServiceImpl;
import kr.co.leehana.sg.service.IFavoriteService;

public class FavoriteCategoriesActivity extends AppCompatActivity implements ActionBar.TabListener {

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
	private List<ActionBar.Tab> tabs = new ArrayList<>(2);
	private List<FavoriteCategory> mFavoriteCategories = new ArrayList<>();
	private List<FavoriteRate> mFavoriteRates = new ArrayList<>();
	private int totalFavoriteItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_categories);

		AppContext.getInstance().setIsFavoriteCategoryView(false);
		AppContext.getInstance().setIsFavoriteRateView(false);

		mFavoriteService = FavoriteServiceImpl.getInstance();
		((FavoriteServiceImpl) mFavoriteService).setHelper(DbHelperFactory.create(getBaseContext()));

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_favorite_categories, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
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
					return getString(R.string.favorite_category_name_tab_title, mFavoriteCategories.size()).toUpperCase(l);
				case 1:
					return getString(R.string.favorite_category_rate_tab_title, totalFavoriteItem).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("ValidFragment")
	public class PlaceholderFragment extends ListFragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private int position;

		public PlaceholderFragment(int position) {
			this.position = position;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			if (position == 0) {
				mFavoriteCategories = mFavoriteService.getFavoriteCategories();
				setListAdapter(new FavoriteCategoryAdapter(getBaseContext(), R.layout.favorite_category_item, mFavoriteCategories));
				updateCategoryNameTabTitle();
			} else if (position == 1) {
				setListAdapter(new FavoriteRateAdapter(getBaseContext(), R.layout.favorite_rate_item, mFavoriteRates));
				updateCategoryRateList();
				updateRateTabTitle();
			}

			View rootView = inflater.inflate(R.layout.fragment_favorite_categories, container, false);
			return rootView;
		}

		@Override
		public void onListItemClick(ListView l, View v, int pos, long id) {
			super.onListItemClick(l, v, pos, id);

			AppContext.getInstance().setFavoriteViewIndex(pos);
			Intent intent = new Intent(getBaseContext(), FavoriteViewActivity.class);
			if (this.position == 0) {
				FavoriteCategory category = mFavoriteCategories.get(pos);
				int categoryId = category.getId();
				intent.putExtra(AppContext.CATEGORY_ID, categoryId);
				intent.putExtra("title", category.getName());
				AppContext.getInstance().setIsFavoriteCategoryView(true);
				AppContext.getInstance().setIsFavoriteRateView(false);
			} else {
				FavoriteRate rate = mFavoriteRates.get(pos);
				int rateValue = rate.getRate();
				intent.putExtra(AppContext.RATE_CODE, rateValue);
				intent.putExtra("title", AppContext.FAVORITE_RATE[pos] + " (" + rate.getItemCount() + ")");
				AppContext.getInstance().setIsFavoriteCategoryView(false);
				AppContext.getInstance().setIsFavoriteRateView(true);
			}

			startActivity(intent);
		}
	}

	private void updateCategoryRateList() {
		int favoriteCountInRate5 = mFavoriteService.getFavoriteCountInRate(5);
		mFavoriteRates.add(new FavoriteRate(5, favoriteCountInRate5));

		int favoriteCountInRate4 = mFavoriteService.getFavoriteCountInRate(4);
		mFavoriteRates.add(new FavoriteRate(4, favoriteCountInRate4));

		int favoriteCountInRate3 = mFavoriteService.getFavoriteCountInRate(3);
		mFavoriteRates.add(new FavoriteRate(3, favoriteCountInRate3));

		int favoriteCountInRate2 = mFavoriteService.getFavoriteCountInRate(2);
		mFavoriteRates.add(new FavoriteRate(2, favoriteCountInRate2));

		int favoriteCountInRate1 = mFavoriteService.getFavoriteCountInRate(1);
		mFavoriteRates.add(new FavoriteRate(1, favoriteCountInRate1));

		totalFavoriteItem = favoriteCountInRate1 + favoriteCountInRate2 + favoriteCountInRate3 + favoriteCountInRate4 + favoriteCountInRate5;
	}

	private void updateCategoryNameTabTitle() {
		Locale l = Locale.getDefault();
		tabs.get(0).setText(getString(R.string.favorite_category_name_tab_title, mFavoriteCategories.size()).toUpperCase(l));
	}

	private void updateRateTabTitle() {
		Locale l = Locale.getDefault();
		tabs.get(1).setText(getString(R.string.favorite_category_rate_tab_title, totalFavoriteItem).toUpperCase(l));
	}
}
