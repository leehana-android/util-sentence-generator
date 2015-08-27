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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.model.Word;
import kr.co.leehana.sg.service.IWordService;
import kr.co.leehana.sg.service.WordServiceImpl;
import kr.co.leehana.sg.type.InputType;
import kr.co.leehana.sg.type.WordType;
import kr.co.leehana.sg.utils.DbUtils;
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

	private int mCurrentTabPosition = 0;

	private List<List<Word>> wordsArray = new ArrayList<>();
	private List<List<Spanned>> wordSpannedArray = new ArrayList<>();
	private Map<Integer, ArrayAdapter<Spanned>> mAdapterMap = new HashMap<>(4);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);

		wordsArray.add(new ArrayList<Word>());
		wordsArray.add(new ArrayList<Word>());
		wordsArray.add(new ArrayList<Word>());
		wordsArray.add(new ArrayList<Word>());

		wordSpannedArray.add(new ArrayList<Spanned>());
		wordSpannedArray.add(new ArrayList<Spanned>());
		wordSpannedArray.add(new ArrayList<Spanned>());
		wordSpannedArray.add(new ArrayList<Spanned>());

		wordService = WordServiceImpl.getInstance();
		((WordServiceImpl) wordService).setHelper(DbHelperFactory.create(getBaseContext(), DbUtils.LOCAL_DATABASE_NAME));

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
			case R.id.btn_word_sync:
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

		if (inputType.equals(InputType.OLD)) {
			initializeWordData();
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

					List<Word> removeTargetWords = new ArrayList<>();
					for (int i = 0; i < totalItemCount; i++) {
						if (wordListView.isItemChecked(i)) {
							removeTargetWords.add(wordsArray.get(mCurrentTabPosition).get(i));
							wordListView.setItemChecked(i, false);
						}
					}

					if (!removeTargetWords.isEmpty()) {
						for (Word removeTarget : removeTargetWords) {
							wordService.delete(removeTarget);
						}

						showToastMessage(getBaseContext(), "삭제 완료!");

						initializeWordData(mCurrentTabPosition);

						updateListFragment(wordListView);

						int totalWordCount = wordSpannedArray.get(mCurrentTabPosition).size();

						Locale l = Locale.getDefault();
						WordType showWordType = TypeConverter.intToWordType(mCurrentTabPosition);
						String newTabTitle = String.format(l, TAB_TITLE_FORMAT, getString(showWordType.getStringCode()).toUpperCase(l), totalWordCount);

						tabs.get(mCurrentTabPosition).setText(newTabTitle);
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
		mCurrentTabPosition = tab.getPosition();

		mFragment = mSectionsPagerAdapter.getItem(tab.getPosition());
		fragmentTransaction.attach(mFragment);

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

	private void updateListFragment(ListView listView) {
		if (listView == null) {
			listView = (ListView) mFragment.getView().findViewById(android.R.id.list);
		}

		ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
		adapter.notifyDataSetChanged();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Map<Integer, Fragment> fragments = new HashMap<>();

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).

			Fragment fragment;
			if (fragments.containsKey(position)) {
				fragment = fragments.get(position);
			} else {
				fragment = new PlaceholderFragment(position);
				Bundle args = new Bundle();
				args.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position);
				fragment.setArguments(args);

				if (!fragments.containsKey(position)) {
					fragments.put(position, fragment);
				}
			}

			return fragment;
		}

		@Override
		public int getCount() {
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
		private boolean isInitializeAdapter = true;

		public PlaceholderFragment(int position) {
			this.position = position;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.word_list, container, false);
			if (isInitializeAdapter) {
				setListAdapter(createInputAdapter(getActivity().getBaseContext(), position));

				wordInput = (EditText) view.findViewById(R.id.word_input);

				if (inputType.equals(InputType.NEW)) {
					wordInput.requestFocus();
				}

				inputButton = (Button) view.findViewById(R.id.btn_word_input);
				inputButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (StringUtils.isNotBlank(wordInput.getText().toString())) {
							Word newWord = new Word();
							newWord.setWord(wordInput.getText().toString());
							newWord.setBackup(true);
							newWord.setType(TypeConverter.intToWordType(position));
							newWord.setCreateDate(String.valueOf(System.currentTimeMillis()));
							newWord.setGenreType(AppContext.getInstance().getGenreType());
							newWord.setModified(false);

							wordService.insert(newWord);

							showToastMessage(getBaseContext(), "단어 생성 완료!");

							wordsArray.get(position).add(0, newWord);

							Spanned newSpannedWord = convertStringToSpanned("0. " + wordInput.getText().toString() + " [" + newWord.getId() + "]", newWord.getType());
							wordSpannedArray.get(position).add(0, newSpannedWord);

							ArrayAdapter adapter = (ArrayAdapter) getListView().getAdapter();
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

				isInitializeAdapter = false;
			}
			return view;
		}

		@Override
		public void onStart() {
			super.onStart();

			/** Setting the multiSelect choice mode for the listView */
			getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

			getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					showEditWordDialog(position);
					return false;
				}
			});
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);

			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	private void showEditWordDialog(int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.title_modify_word_dialog);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.love_heart_48);
		builder.setMessage(R.string.msg_modify_word_dialog);

		final EditText input = new EditText(this);
		final Word selectedWord = wordsArray.get(mCurrentTabPosition).get(position);
		input.setText(selectedWord.getWord());
		input.setSelection(selectedWord.getWord().length());
		builder.setView(input);
		builder.setPositiveButton(R.string.save_dialog_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedWord.setWord(input.getText().toString());
				updateEditedWord(selectedWord);

				initializeWordData(mCurrentTabPosition);
				updateListFragment(null);

				showToastMessage(getBaseContext(), getString(R.string.modify_done));
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

	private void updateEditedWord(Word word) {
		wordService.update(word);
	}

	private ArrayAdapter<Spanned> createInputAdapter(Context context, int position) {
		ArrayAdapter<Spanned> adapter;
		if (mAdapterMap.containsKey(position)) {
			adapter = mAdapterMap.get(position);
		} else {
			adapter = new InputAdapter(context, R.layout.word_list_item, wordSpannedArray.get(position));
			mAdapterMap.put(position, adapter);
		}

		return adapter;
	}

	class InputAdapter extends ArrayAdapter<Spanned> {

		private LayoutInflater layoutInflater;

		public InputAdapter(Context context, int resource, List<Spanned> objects) {
			super(context, resource, objects);
			layoutInflater = LayoutInflater.from(context);
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Holder holder;

			Spanned item = getItem(position);

			if (view == null) {
				view = layoutInflater.inflate(R.layout.word_list_item, null);
				CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.generated_list_item);

				holder = new Holder();
				holder.setCheckedTextView(checkedTextView);
				view.setTag(holder);
			} else {
				holder = (Holder) view.getTag();
			}

			holder.getCheckedTextView().setText(item);

			return view;
		}

		class Holder {
			private CheckedTextView checkedTextView;

			public Holder() {}

			public CheckedTextView getCheckedTextView() {
				return checkedTextView;
			}

			public void setCheckedTextView(CheckedTextView checkedTextView) {
				this.checkedTextView = checkedTextView;
			}
		}
	}

	private void initializeWordData() {
		for (int index = 0; index < 4; index++) {
			initializeWordData(index);
		}
	}

	private void initializeWordData(int position) {
		WordType currentWordType = TypeConverter.intToWordType(position);

		List<Word> oldWordList = wordsArray.get(position);
		oldWordList.clear();
		AppContext appContext = AppContext.getInstance();
		oldWordList.addAll(wordService.getWords(currentWordType, appContext.getGenreType()));

		int count = 1;
		List<Spanned> oldList = wordSpannedArray.get(position);
		oldList.clear();
		for (Word word : wordsArray.get(position)) {
			oldList.add(convertStringToSpanned(count + ". " + word.getWord() + " [" + word.getId() + "]", currentWordType));
			count++;
		}
	}

	private Spanned convertStringToSpanned(String source, WordType wordType) {
		return Html.fromHtml("<font color='" + AppContext.getColorCodeForWord(wordType) + "'>" + source + "</font>");
	}

	private void showToastMessage(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
