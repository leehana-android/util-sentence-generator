package kr.co.leehana.sg.fragment;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.model.FavoriteCategory;
import kr.co.leehana.sg.model.FavoriteRate;
import kr.co.leehana.sg.service.FavoriteServiceImpl;
import kr.co.leehana.sg.service.IFavoriteService;
import kr.co.leehana.sg.utils.DbUtils;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the user manually
	 * expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ListView drawerListView;
	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	private IFavoriteService mFavoriteService;

	private boolean isRateView = AppContext.getInstance().isFavoriteRateView();
	private boolean isCategoryView = AppContext.getInstance().isFavoriteCategoryView();

	private List<FavoriteCategory> favoriteCategories = new ArrayList<>();
	private List<FavoriteRate> favoriteRates = new ArrayList<>();
	private List<String> sideMenuTitleList = new ArrayList<>();

	public NavigationDrawerFragment() {
	}

	public List<FavoriteRate> getFavoriteRates() {
		return favoriteRates;
	}

	public List<FavoriteCategory> getFavoriteCategories() {
		return favoriteCategories;
	}

	public ListView getDrawerListView() {
		return this.drawerListView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mFavoriteService = FavoriteServiceImpl.getInstance();
		((FavoriteServiceImpl) mFavoriteService).setHelper(DbHelperFactory.create(getActivity().getBaseContext(), DbUtils.LOCAL_DATABASE_NAME));

		initializeFavoriteData();

		// Read in the flag indicating whether or not the user has demonstrated awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		} else {
			mCurrentSelectedPosition = AppContext.getInstance().getFavoriteViewIndex();
			AppContext.getInstance().setFavoriteViewIndex(-1);
		}

		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
	}

	public void initializeFavoriteData() {
		sideMenuTitleList.clear();

		if (isCategoryView) {
			favoriteCategories.clear();

			favoriteCategories = mFavoriteService.getFavoriteCategories();

			for (FavoriteCategory category : favoriteCategories) {
				sideMenuTitleList.add(category.getName() + " (" + mFavoriteService.getFavoriteCountInCategory(category.getId()) + ")");
			}
		} else {
			favoriteRates.clear();

			int favoriteCountInRate5 = mFavoriteService.getFavoriteCountInRate(5);
			favoriteRates.add(new FavoriteRate(5, favoriteCountInRate5));

			int favoriteCountInRate4 = mFavoriteService.getFavoriteCountInRate(4);
			favoriteRates.add(new FavoriteRate(4, favoriteCountInRate4));

			int favoriteCountInRate3 = mFavoriteService.getFavoriteCountInRate(3);
			favoriteRates.add(new FavoriteRate(3, favoriteCountInRate3));

			int favoriteCountInRate2 = mFavoriteService.getFavoriteCountInRate(2);
			favoriteRates.add(new FavoriteRate(2, favoriteCountInRate2));

			int favoriteCountInRate1 = mFavoriteService.getFavoriteCountInRate(1);
			favoriteRates.add(new FavoriteRate(1, favoriteCountInRate1));

			sideMenuTitleList.add(AppContext.FAVORITE_RATE[4] + " (" + favoriteRates.get(0).getItemCount() + ")");
			sideMenuTitleList.add(AppContext.FAVORITE_RATE[3] + " (" + favoriteRates.get(1).getItemCount() + ")");
			sideMenuTitleList.add(AppContext.FAVORITE_RATE[2] + " (" + favoriteRates.get(2).getItemCount() + ")");
			sideMenuTitleList.add(AppContext.FAVORITE_RATE[1] + " (" + favoriteRates.get(3).getItemCount() + ")");
			sideMenuTitleList.add(AppContext.FAVORITE_RATE[0] + " (" + favoriteRates.get(4).getItemCount() + ")");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		drawerListView = (ListView) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);
		drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectItem(position);
			}
		});
		drawerListView.setAdapter(new ArrayAdapter<>(
				getActionBar().getThemedContext(),
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				sideMenuTitleList
		));
		drawerListView.setItemChecked(mCurrentSelectedPosition, true);
		return drawerListView;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation drawer interactions.
	 *
	 * @param fragmentId   The android:id of this fragment in its activity's layout.
	 * @param drawerLayout The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(
				getActivity(),                    /* host Activity */
				mDrawerLayout,                    /* DrawerLayout object */
				R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
				R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
				R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (drawerListView != null) {
			drawerListView.setItemChecked(position, true);
		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar. See also
		// showGlobalContextActionBar, which controls the top-left area of the action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to show the global app
	 * 'context', rather than just what's in the current screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}

	private ActionBar getActionBar() {
		return ((AppCompatActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}
}
