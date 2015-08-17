package kr.co.leehana.sg.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.fragment.NavigationDrawerFragment;
import kr.co.leehana.sg.model.Favorite;
import kr.co.leehana.sg.model.FavoriteCategory;
import kr.co.leehana.sg.model.FavoriteRate;
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
	private PlaceholderFragment mFragment;
	private ArrayAdapter<Spanned> mAdapter;
	private List<Spanned> mFavoriteSpannedItems = new ArrayList<>();
	private List<Favorite> mFavoriteList = new ArrayList<>();
	private int mCurrentSectionNumber = 0;

	private boolean isRateView = AppContext.getInstance().isFavoriteRateView();
	private boolean isCategoryView = AppContext.getInstance().isFavoriteCategoryView();

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
		mFragment = new PlaceholderFragment(position);
		Bundle args = new Bundle();
		args.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position);
		mFragment.setArguments(args);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, mFragment)
				.commit();
	}

	public void onSectionAttached(int number) {
		mCurrentSectionNumber = number;
		if (isCategoryView) {
			FavoriteCategory currentCategory = mNavigationDrawerFragment.getFavoriteCategories().get(number);
			mSelectedCategoryId = currentCategory.getId();
			mTitle = currentCategory.getName() + " (" + mFavoriteService.getFavoriteCountInCategory(mSelectedCategoryId) + ")";
		} else {
			FavoriteRate currentRate = mNavigationDrawerFragment.getFavoriteRates().get(number);
			mSelectedRateCode = currentRate.getRate();
			mTitle = AppContext.FAVORITE_RATE[currentRate.getRate() - 1] + " (" + currentRate.getItemCount() + ")";
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	private void updateActionBarTitle() {
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.menu_favorite_view, menu);
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
		} else if (id == R.id.action_delete) {
			showDeleteConfirmAlertDialog();
		}

		return super.onOptionsItemSelected(item);
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
				if (mFragment != null && mFragment.getView() != null) {
					ListView favoriteListView = (ListView) mFragment.getView().findViewById(android.R.id.list);
					int totalItemCount = mAdapter.getCount();

					List<Spanned> removeTargetIndexList = new ArrayList<>();
					for (int i = 0; i < totalItemCount; i++) {
						if (favoriteListView.isItemChecked(i)) {
							removeTargetIndexList.add(mFavoriteSpannedItems.get(i));
							favoriteListView.setItemChecked(i, false);

							mFavoriteService.delete(mFavoriteList.get(i));
						}
					}

					mFavoriteSpannedItems.removeAll(removeTargetIndexList);

					mAdapter.notifyDataSetChanged();
					updateFavoriteList();
					updateFavoriteListInFragment();
					updateTitle();
					updateActionBarTitle();

					updateSideMenuItem();

					showToastMessage(getBaseContext(), getString(R.string.delete_done));
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

	private void updateFavoriteList() {
		if (isCategoryView) {
			mFavoriteList = mFavoriteService.getFavoritesByParentId(mSelectedCategoryId);
		} else if (isRateView) {
			mFavoriteList = mFavoriteService.getFavoritesByRate(mSelectedRateCode);
		}
	}

	private void updateFavoriteListInFragment() {
		mNavigationDrawerFragment.initializeFavoriteData();
	}

	private void updateTitle() {
		onSectionAttached(mCurrentSectionNumber);
	}

	private void showToastMessage(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	private void updateSideMenuItem() {
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) mNavigationDrawerFragment.getDrawerListView().getAdapter();
		adapter.notifyDataSetChanged();
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

			updateFavoriteList();

			mFavoriteSpannedItems.clear();

			if (mFavoriteList != null) {
				for (Favorite favorite : mFavoriteList) {
					mFavoriteSpannedItems.add((Spanned) TextUtils.replace(Html.fromHtml(favorite.getSentence()), new String[]{"\n\n"}, new String[]{""}));
				}
			}
			mAdapter = new ArrayAdapter<>(context, R.layout.favorite_view_list_item, mFavoriteSpannedItems);
			setListAdapter(mAdapter);
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
