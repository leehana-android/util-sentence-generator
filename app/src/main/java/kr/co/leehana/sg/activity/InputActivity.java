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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.model.Words;
import kr.co.leehana.sg.service.IWordService;
import kr.co.leehana.sg.service.WordServiceImpl;
import kr.co.leehana.sg.type.InputType;
import kr.co.leehana.sg.type.WordType;
import kr.co.leehana.sg.utils.StringUtils;

public class InputActivity extends AppCompatActivity implements ActionBar.TabListener {

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

	private Fragment mFragment;
	private IWordService wordService;
	private InputType inputType = InputType.OLD;
	private List<ActionBar.Tab> tabs = new ArrayList<>();

	private static final String TAB_TITLE_FORMAT = "%s(%d)";

	private int currentTabPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);

		wordService = WordServiceImpl.getInstance();
		((WordServiceImpl) wordService).setHelper(DbHelperFactory.create(getBaseContext()));

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setBackgroundDrawable(new ColorDrawable(AppContext.ACTION_BAR_COLOR));
//		actionBar.setStackedBackgroundDrawable(new ColorDrawable(AppContext.ACTION_BAR_TAB_COLOR));

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

		int extraData = getIntent().getIntExtra(AppContext.BTN_CODE, 0);
		switch (extraData) {
			case R.id.btn_new_word_input:
				inputType = InputType.NEW;
				break;
			case R.id.btn_show_word:
				inputType = InputType.OLD;
				break;
			default:
				break;
		}

		String actionBarTitle;
		if (inputType.equals(InputType.NEW)) {
			actionBarTitle = getString(R.string.new_word_input_title, getString(AppContext.getInstance().getGenreType().getStringCode()));
		} else {
			actionBarTitle = getString(R.string.show_word_input_title, getString(AppContext.getInstance().getGenreType().getStringCode()));
		}

		actionBar.setTitle(actionBarTitle);

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
		if (inputType.equals(InputType.NEW)) {
			getMenuInflater().inflate(R.menu.menu_new_input, menu);
		} else {
			getMenuInflater().inflate(R.menu.menu_input, menu);
		}
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
		} else if (id == R.id.select_all_setting) {
			if (mFragment != null && mFragment.getView() != null) {
				ListView wordListView = (ListView) mFragment.getView().findViewById(android.R.id.list);
				int totalItemCount = wordListView.getAdapter().getCount();
				for (int i = 0; i < totalItemCount; i++) {
					wordListView.setItemChecked(i, true);
				}
			}
		} else if (id == R.id.select_none_setting) {
			if (mFragment != null && mFragment.getView() != null) {
				ListView wordListView = (ListView) mFragment.getView().findViewById(android.R.id.list);
				int totalItemCount = wordListView.getAdapter().getCount();
				for (int i = 0; i < totalItemCount; i++) {
					wordListView.setItemChecked(i, false);
				}
			}
		} else if (id == R.id.select_delete_setting) {
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
					ListView wordListView = (ListView) mFragment.getView().findViewById(android.R.id.list);
					int totalItemCount = wordListView.getAdapter().getCount();

					List<Integer> checkedItemIds = new ArrayList<>();
					List<Object> checkedItemObjects = new ArrayList<>();
					for (int i = 0; i < totalItemCount; i++) {
						if (wordListView.isItemChecked(i)) {
							checkedItemObjects.add(wordListView.getItemAtPosition(i));
							wordListView.setItemChecked(i, false);
							String selectedItemText = wordListView.getItemAtPosition(i).toString();
							String selectedItemIdText = selectedItemText.substring(selectedItemText.indexOf("["));
							String idText = selectedItemIdText.replace("[", "").replace("]", "");
							checkedItemIds.add(Integer.valueOf(idText));
						}
					}

					if (!checkedItemIds.isEmpty()) {
						wordService.delete(checkedItemIds);

						showToastMessage(getBaseContext(), "삭제 완료!");
						ArrayAdapter adapter = (ArrayAdapter) wordListView.getAdapter();
						for (Object removeTargetObject : checkedItemObjects) {
							adapter.remove(removeTargetObject);
						}

						int totalWordCount = wordListView.getAdapter().getCount();

						Locale l = Locale.getDefault();
						WordType showWordType = TypeConverter.intToWordType(currentTabPosition);
						String newTabTitle = String.format(l, TAB_TITLE_FORMAT, getString(showWordType.getStringCode()).toUpperCase(l), totalWordCount);

						tabs.get(currentTabPosition).setText(newTabTitle);
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

		builder.create();
		builder.show();
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		currentTabPosition = tab.getPosition();



//		Intent intent = new Intent(this, AdjectiveListActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);

//		if (mFragment == null) {
			mFragment = mSectionsPagerAdapter.getItem(tab.getPosition());
			fragmentTransaction.attach(mFragment);
//		}

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//		if (mFragment != null) {
//			fragmentTransaction.detach(mFragment);
//		}
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

//			PlaceholderFragment fragment = new PlaceholderFragment(position);
//			Bundle args = new Bundle();
//			args.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position);
//			fragment.setArguments(args);

			Fragment fragment;
			if (fragments.isEmpty() || fragments.size() <= position) {
				fragment = new PlaceholderFragment(position);
				Bundle args = new Bundle();
				args.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position);
				fragment.setArguments(args);

				fragments.add(fragment);
			} else {
				fragment = fragments.get(position);
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			int dataCount;
			if (inputType.equals(InputType.NEW)) {
				dataCount = 0;
			} else {
				dataCount = wordService.getCount(TypeConverter.intToWordType(position), AppContext.getInstance().getGenreType());
			}

			WordType showWordType = TypeConverter.intToWordType(position);
			return String.format(l, TAB_TITLE_FORMAT, getString(showWordType.getStringCode()).toUpperCase(l), dataCount);
		}
	}

	@SuppressLint("ValidFragment")
	class PlaceholderFragment extends ListFragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private Button inputButton;
		private EditText wordInput;

		private int position;

		public PlaceholderFragment(int position) {
			this.position = position;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			final List<Words> wordsList;

			if (inputType.equals(InputType.NEW)) {
				wordsList = Collections.emptyList();
			} else {
				AppContext appContext = AppContext.getInstance();
				wordsList = wordService.getWords(TypeConverter.intToWordType(position), appContext.getGenreType());
			}

			List<String> wordStringList = new ArrayList<>();
			for (Words words : wordsList) {
				wordStringList.add(words.getWord() + " ["+words.getId()+"]");
			}

			Collections.sort(wordStringList);

			final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.word_list_item, wordStringList);
			setListAdapter(adapter);
			View view = inflater.inflate(R.layout.word_list, container, false);

			wordInput = (EditText) view.findViewById(R.id.word_input);

			if (inputType.equals(InputType.NEW)) {
				wordInput.requestFocus();
			}

			inputButton = (Button) view.findViewById(R.id.btn_word_input);
			inputButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (StringUtils.isNotBlank(wordInput.getText().toString())) {
						Words newWords = new Words();
						newWords.setWord(wordInput.getText().toString());
						newWords.setBackup(false);
						newWords.setType(TypeConverter.intToWordType(position));
						newWords.setCreateDate(String.valueOf(System.currentTimeMillis()));
						newWords.setGenreType(AppContext.getInstance().getGenreType());

						wordService.insert(newWords);

						showToastMessage(getBaseContext(), "단어 생성 완료!");
						adapter.insert(wordInput.getText().toString() + " [" + newWords.getId() + "]", 0);
						adapter.notifyDataSetChanged();

						int totalWordCount = adapter.getCount();

						Locale l = Locale.getDefault();
						WordType showWordType = TypeConverter.intToWordType(position);
						String newTabTitle = String.format(l, TAB_TITLE_FORMAT, getString(showWordType.getStringCode()).toUpperCase(l), totalWordCount);

						tabs.get(position).setText(newTabTitle);
					}
					wordInput.setText("");
				}
			});
			return view;
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

			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	private void showToastMessage(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
