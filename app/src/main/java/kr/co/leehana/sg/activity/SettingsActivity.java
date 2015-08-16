package kr.co.leehana.sg.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import kr.co.leehana.sg.R;
import kr.co.leehana.sg.adapter.SettingsAdapter;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.model.Setting;
import kr.co.leehana.sg.service.ISettingService;
import kr.co.leehana.sg.service.SettingServiceImpl;

public class SettingsActivity extends AppCompatActivity {

	private Setting mSetting;

	private ISettingService mSettingService;

	private ListView mSettingListView;

	private ArrayAdapter<Setting> mAdapter;
	private AlertDialog mSentenceCountSettingDialog;
	private AlertDialog mFirstWordSettingDialog;
	private AlertDialog mSecondWordSettingDialog;
	private AlertDialog mThirdWordSettingDialog;
	private AlertDialog mFourthWordSettingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		mSettingService = SettingServiceImpl.getInstance();
		((SettingServiceImpl) mSettingService).setHelper(DbHelperFactory.create(getBaseContext()));

		mSetting = AppContext.getInstance().getSetting();

		createSettingArrayAdapter();
		createSentenceCountSettingDialog();
		createWordSettingDialog(AppContext.SETTING_FIRST_WORD_IDX);
		createWordSettingDialog(AppContext.SETTING_SECOND_WORD_IDX);
		createWordSettingDialog(AppContext.SETTING_THIRD_WORD_IDX);
		createWordSettingDialog(AppContext.SETTING_FOURTH_WORD_IDX);

		mSettingListView = (ListView) findViewById(R.id.settings_list_view);
		mSettingListView.setAdapter(mAdapter);
		mSettingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case AppContext.SETTING_SENTENCE_COUNT_IDX:
						mSentenceCountSettingDialog.show();
						break;
					case AppContext.SETTING_FIRST_WORD_IDX:
						mFirstWordSettingDialog.show();
						break;
					case AppContext.SETTING_SECOND_WORD_IDX:
						mSecondWordSettingDialog.show();
						break;
					case AppContext.SETTING_THIRD_WORD_IDX:
						mThirdWordSettingDialog.show();
						break;
					case AppContext.SETTING_FOURTH_WORD_IDX:
						mFourthWordSettingDialog.show();
						break;
				}
			}
		});
	}

	private ArrayAdapter<Setting> createSettingArrayAdapter() {
		if (mAdapter == null) {
			mAdapter = new SettingsAdapter(this, R.layout.settings_list_item, new Setting[]{mSetting});
		}
		return mAdapter;

	}

	private void createSentenceCountSettingDialog() {
		int initIndex = AppContext.SENTENCE_COUNT_ARRAY.indexOf(AppContext.getInstance().getSetting().getSentenceCount());
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.settings_sentence_count_dialog_title);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.settings_icon);

		builder.setSingleChoiceItems(R.array.settings_generate_count_list, initIndex, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mSetting.setSentenceCount(AppContext.SENTENCE_COUNT_ARRAY.get(which));
//				AppContext.getInstance().getSetting().setSentenceCount(AppContext.SENTENCE_COUNT_ARRAY.get(which));
			}
		});

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mAdapter.notifyDataSetChanged();
				mSettingService.update(AppContext.getInstance().getSetting());
			}
		});

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		mSentenceCountSettingDialog = builder.create();
	}

	private void createWordSettingDialog(final int wordSettingIndex) {

		int initIndex = 0;
		switch (wordSettingIndex) {
			case AppContext.SETTING_FIRST_WORD_IDX:
				initIndex = AppContext.getInstance().getSetting().getFirstWordType().getIndexCode();
				break;
			case AppContext.SETTING_SECOND_WORD_IDX:
				initIndex = AppContext.getInstance().getSetting().getSecondWordType().getIndexCode();
				break;
			case AppContext.SETTING_THIRD_WORD_IDX:
				initIndex = AppContext.getInstance().getSetting().getThirdWordType().getIndexCode();
				break;
			case AppContext.SETTING_FOURTH_WORD_IDX:
				initIndex = AppContext.getInstance().getSetting().getFourthWordType().getIndexCode();
				break;

		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.settings_word_dialog_title);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.settings_icon);

		builder.setSingleChoiceItems(R.array.settings_word_list, initIndex, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setWordGenerateValue(wordSettingIndex, which);
			}
		});

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mAdapter.notifyDataSetChanged();
				mSettingService.update(mSetting);
			}
		});

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		switch (wordSettingIndex) {
			case AppContext.SETTING_FIRST_WORD_IDX:
				mFirstWordSettingDialog = builder.create();
				break;
			case AppContext.SETTING_SECOND_WORD_IDX:
				mSecondWordSettingDialog = builder.create();
				break;
			case AppContext.SETTING_THIRD_WORD_IDX:
				mThirdWordSettingDialog = builder.create();
				break;
			case AppContext.SETTING_FOURTH_WORD_IDX:
				mFourthWordSettingDialog = builder.create();
				break;
		}
	}

	private void setWordGenerateValue(int wordIndex, int whichWord) {
		switch (wordIndex) {
			case AppContext.SETTING_FIRST_WORD_IDX:
				mSetting.setFirstWordType(TypeConverter.intToSentenceGenerateType(whichWord));
				break;
			case AppContext.SETTING_SECOND_WORD_IDX:
				mSetting.setSecondWordType(TypeConverter.intToSentenceGenerateType(whichWord));
				break;
			case AppContext.SETTING_THIRD_WORD_IDX:
				mSetting.setThirdWordType(TypeConverter.intToSentenceGenerateType(whichWord));
				break;
			case AppContext.SETTING_FOURTH_WORD_IDX:
				mSetting.setFourthWordType(TypeConverter.intToSentenceGenerateType(whichWord));
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_settings, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
