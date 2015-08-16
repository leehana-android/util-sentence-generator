package kr.co.leehana.sg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import kr.co.leehana.sg.activity.FavoriteCategoriesActivity;
import kr.co.leehana.sg.activity.InputActivity;
import kr.co.leehana.sg.activity.SentenceActivity;
import kr.co.leehana.sg.activity.SettingsActivity;
import kr.co.leehana.sg.context.AppContext;
import kr.co.leehana.sg.converter.TypeConverter;
import kr.co.leehana.sg.model.WordStructure;
import kr.co.leehana.sg.utils.DbUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private Button newWordBtn;
	private Button showWordBtn;

	private Button favoriteBtn;
	private Button generateBtn;

	private AlertDialog generateWordSelectDialog;
	private AlertDialog newWordGenreSelectDialog;

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(AppContext.ACTION_BAR_COLOR));

		DbUtils.getInstance().prepareDatabase(getBaseContext());

		newWordBtn = (Button) findViewById(R.id.btn_new_word_input);
		newWordBtn.setOnClickListener(this);

		showWordBtn = (Button) findViewById(R.id.btn_show_word);
		showWordBtn.setOnClickListener(this);

		favoriteBtn = (Button) findViewById(R.id.btn_show_favorite);
		favoriteBtn.setOnClickListener(this);

		generateBtn = (Button) findViewById(R.id.btn_generate);
		generateBtn.setOnClickListener(this);

		makeGenerateWordSelectDialog();
		makeNewWordGenreSelectDialog();
	}

	private void makeNewWordGenreSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.new_word_genre_select_dialog_title);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.love_heart_48);

		builder.setSingleChoiceItems(R.array.genre_list, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AppContext.getInstance().setGenreType(TypeConverter.intToGenreType(which));
			}
		});

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				AppContext.getInstance().setState(AppContext.State.NEW_WORD);

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


	private void makeGenerateWordSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.generate_target_select_menu_title);
		builder.setCancelable(false);
		builder.setIcon(R.drawable.love_heart_48);

		builder.setMultiChoiceItems(R.array.word_list, WordStructure.wordChecked, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				WordStructure.wordChecked[which] = isChecked;
			}
		});

		builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int trueCount = 0;
				for (boolean checked : WordStructure.wordChecked) {
					if (checked) {
						trueCount++;
					}
				}

				if (trueCount == 0) {
					showNoSelectedAlertDialog();
				} else {
					Intent intent = new Intent(getBaseContext(), SentenceActivity.class);
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

		generateWordSelectDialog = builder.create();
	}

	private void showNoSelectedAlertDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setTitle(R.string.no_selected_alert_title);
		alertDialog.setMessage(getString(R.string.no_selected_alert_msg));
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialog.show();
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
			intent = new Intent(this, UploadToServer.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_show_word:
			case R.id.btn_new_word_input:
				intent = new Intent(this, InputActivity.class);
				intent.putExtra(AppContext.BTN_CODE, v.getId());
				newWordGenreSelectDialog.show();
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
}
