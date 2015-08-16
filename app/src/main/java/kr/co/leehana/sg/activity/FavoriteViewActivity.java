package kr.co.leehana.sg.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.fragment.NavigationDrawerFragment;
import kr.co.leehana.sg.model.Favorite;
import kr.co.leehana.sg.service.FavoriteServiceImpl;
import kr.co.leehana.sg.service.IFavoriteService;

public class FavoriteViewActivity extends AppCompatActivity
		implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private int mSelectedCategoryId;
	private int mSelectedRateCode;
	private IFavoriteService mFavoriteService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_view);

		mFavoriteService = FavoriteServiceImpl.getInstance();
		((FavoriteServiceImpl) mFavoriteService).setHelper(DbHelperFactory.create(getBaseContext()));

		mSelectedCategoryId = getIntent().getIntExtra(AppContext.CATEGORY_ID, -1);
		mSelectedRateCode = getIntent().getIntExtra(AppContext.RATE_CODE, -1);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

		mTitle = getScreenTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	public String getScreenTitle() {
		return getIntent().getStringExtra("title");
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		PlaceholderFragment fragment = new PlaceholderFragment(position);
		Bundle args = new Bundle();
		args.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
	}

	public void onSectionAttached(int number) {
		if (AppContext.getInstance().isFavoriteCategoryView()) {
			mTitle = mNavigationDrawerFragment.getmFavoriteCategories().get(number).getName();
			mSelectedCategoryId = mNavigationDrawerFragment.getmFavoriteCategories().get(number).getId();
		} else {
			mTitle = AppContext.FAVORITE_RATE[mNavigationDrawerFragment.getmFavoriteRates().get(number).getRate() - 1] + " (" + mNavigationDrawerFragment.getmFavoriteRates().get(number).getItemCount() + ")";
			mSelectedRateCode = mNavigationDrawerFragment.getmFavoriteRates().get(number).getRate();
		}
	}

	public void onSectionAttached(String title) {
		mTitle = title;
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.favorite_view, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("ValidFragment")
	public class PlaceholderFragment extends ListFragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		private int sectionNumber;

		public PlaceholderFragment(int sectionNumber) {
			this.sectionNumber = sectionNumber;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			Context context = getActivity().getBaseContext();
			List<Favorite> favoriteList = null;

			if (mSelectedCategoryId > -1) {
				favoriteList = mFavoriteService.getFavoritesByParentId(mSelectedCategoryId);
			} else if (mSelectedRateCode > -1) {
				favoriteList = mFavoriteService.getFavoritesByRate(mSelectedRateCode);
			}
			List<Spanned> stringData = new ArrayList<>();
			if (favoriteList != null) {
				for (Favorite favorite : favoriteList) {
					stringData.add(Html.fromHtml(favorite.getSentence()));
				}
			}
			ArrayAdapter<Spanned> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_multiple_choice, stringData);
			setListAdapter(adapter);
			View rootView = inflater.inflate(R.layout.fragment_favorite_view, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((FavoriteViewActivity) activity).onSectionAttached(
					getArguments().getInt(ARG_SECTION_NUMBER));
		}

		@Override
		public void onStart() {
			super.onStart();
			getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
	}

}
