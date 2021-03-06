package kr.co.leehana.sg;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.co.leehana.sg.activity.DBManagerActivity;
import kr.co.leehana.sg.activity.FavoriteCategoriesActivity;
import kr.co.leehana.sg.activity.InputActivity;
import kr.co.leehana.sg.activity.SentenceActivity;
import kr.co.leehana.sg.activity.SettingsActivity;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.model.Genre;
import kr.co.leehana.sg.service.ISettingService;
import kr.co.leehana.sg.service.SettingServiceImpl;
import kr.co.leehana.sg.utils.DbUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private AlertDialog newWordGenreSelectDialog;

	private Intent intent;

	private ProgressDialog mProgressDialog = null;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;

		DbUtils.prepareDatabase(getBaseContext());

		ISettingService settingService = SettingServiceImpl.getInstance();
		((SettingServiceImpl) settingService).setHelper(DbHelperFactory.create(getBaseContext(), DbUtils.LOCAL_DATABASE_NAME));

		if (AppContext.getInstance().getSetting() == null) {
			AppContext.getInstance().setSetting(settingService.getSetting());
		}

		Button wordSyncBtn = (Button) findViewById(R.id.btn_word_sync);
		wordSyncBtn.setOnClickListener(this);

		Button showWordBtn = (Button) findViewById(R.id.btn_show_word);
		showWordBtn.setOnClickListener(this);

		Button favoriteBtn = (Button) findViewById(R.id.btn_show_favorite);
		favoriteBtn.setOnClickListener(this);

		Button generateBtn = (Button) findViewById(R.id.btn_generate);
		generateBtn.setOnClickListener(this);

		makeNewWordGenreSelectDialog();
	}

	private class ProcessDatabaseSyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (DbUtils.sync(mContext)) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showToastMessage(getString(R.string.db_sync_complete));
						}
					});
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showToastMessage(getString(R.string.db_sync_fail));
						}
					});
				}
			} catch (Exception e) {
				Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
			} finally {
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}
			return null;
		}
	}

	private void makeNewWordGenreSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.new_word_genre_select_dialog_title);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.love_heart_48);

		builder.setAdapter(makeGenreListAdapter(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AppContext.getInstance().setGenreType(TypeConverter.intToGenreType(which));
			}
		});

		builder.setSingleChoiceItems(R.array.genre_list, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AppContext.getInstance().setGenreType(TypeConverter.intToGenreType(which));
			}
		});

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (intent != null) {
					startActivity(intent);
				}
			}
		});

		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		newWordGenreSelectDialog = builder.create();
	}

	private ListAdapter makeGenreListAdapter() {
		final List<Genre> genreItems = new ArrayList<>();
		genreItems.add(new Genre(R.drawable.indi_poetry, getString(R.string.poetry)));
		genreItems.add(new Genre(R.drawable.indi_nusery_rime, getString(R.string.nursery_rime)));
		genreItems.add(new Genre(R.drawable.indi_novel, getString(R.string.novel)));
		genreItems.add(new Genre(R.drawable.indi_essay, getString(R.string.essay)));
		genreItems.add(new Genre(R.drawable.indi_fairy_tail, getString(R.string.fairy_tale)));
		genreItems.add(new Genre(R.drawable.indi_star, getString(R.string.etc)));

		return new ArrayAdapter<Genre>(this, android.R.layout.select_dialog_singlechoice, android.R.id.text1, genreItems) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				CheckedTextView tv = (CheckedTextView)v.findViewById(android.R.id.text1);

				//Put the image on the TextView
				tv.setCompoundDrawablesWithIntrinsicBounds(genreItems.get(position).getIconResourceId(), 0, 0, 0);

				//Add margin between image and text (support various screen densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				tv.setCompoundDrawablePadding(dp5);

				return v;
			}
		};
	}

	private void showSyncConfirmDialog() {
		final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
		confirmDialog.setTitle(R.string.db_sync_confirm_dialog_title);
		confirmDialog.setIcon(R.drawable.love_heart_48);
		confirmDialog.setMessage(R.string.db_sync_confirm_dialog_msg);
		confirmDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mProgressDialog = ProgressDialog.show(mContext, "", getString(R.string.progress_sync_db), true);
				new ProcessDatabaseSyncTask().execute(null, null, null);
			}
		});
		confirmDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		confirmDialog.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
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
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		} else if (id == R.id.action_upload) {
			intent = new Intent(this, DBManagerActivity.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_show_word:
				intent = new Intent(this, InputActivity.class);
				intent.putExtra(AppContext.BTN_CODE, v.getId());
				newWordGenreSelectDialog.show();
				break;
			case R.id.btn_word_sync:
				showSyncConfirmDialog();
				break;
			case R.id.btn_show_favorite:
				intent = new Intent(this, FavoriteCategoriesActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_generate:
				intent = new Intent(this, SentenceActivity.class);
				intent.putExtra(AppContext.BTN_CODE, v.getId());
				newWordGenreSelectDialog.show();
				break;
			default:
				break;
		}
	}

	private void showToastMessage(String message) {
		Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
	}
}
