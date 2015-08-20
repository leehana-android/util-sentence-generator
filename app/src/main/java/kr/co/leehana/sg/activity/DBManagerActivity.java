package kr.co.leehana.sg.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import kr.co.leehana.sg.AppProfile;
import kr.co.leehana.sg.R;
import kr.co.leehana.sg.utils.DbUtils;
import kr.co.leehana.sg.utils.StringUtils;

public class DBManagerActivity extends AppCompatActivity {

	private TextView mUploadMsg;
	private Button mUploadButton;
	private Button mUpgradeButton;
	private Button mLocalBackupButton;
	private ProgressDialog mProgressDialog = null;

	private int mServerResponseCode = 0;

	private String mUpLoadServerUri = "http://leehana.co.kr/UploadToServer.php";
	private String mDownloadServerUri = "http://leehana.co.kr/sg/data/sg.db";
	private String hostName = "leehana.co.kr";

	/**********
	 * File Path
	 *************/
	private final String uploadFilePath = DbUtils.SYSTEM_DB_PATH;
	private String localBackupFileName;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_manager);

		mContext = this;

		mUpgradeButton = (Button) findViewById(R.id.btn_db_upgrade);
		mUploadButton = (Button) findViewById(R.id.btn_db_upload);
		mLocalBackupButton = (Button) findViewById(R.id.btn_local_backup);
		mUploadMsg = (TextView) findViewById(R.id.db_manager_msg);

		mLocalBackupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mUploadMsg.setText(getString(R.string.start_db_local_backup, getLocalBackupFileName()));

				if (localBackup()) {
					mUploadMsg.setText(getString(R.string.db_local_backup_complete, getLocalBackupFileName()));
				} else {
					mUploadMsg.setText(R.string.db_local_backup_fail);
				}

				localBackupFileName = null;
			}
		});

		/************* Php script path ****************/
		mUpgradeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mUploadMsg.setText(getString(R.string.start_db_upgrade, mDownloadServerUri));
				mProgressDialog = ProgressDialog.show(mContext, "", getString(R.string.progress_upgrade_db), true);

				new Thread(new Runnable() {
					public void run() {
						runOnUiThread(new Runnable() {
							public void run() {
								mUploadMsg.setText(R.string.start_db_upgrade);
							}
						});

						if (checkNetworkAdapter()) {
							try {
								checkNetwork();
								if (backup()) {
									upgradeDatabase(DbUtils.SYSTEM_DB_FULL_PATH);
								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											mUploadMsg.setText(R.string.db_backup_fail);
										}
									});
								}
							} catch (UnknownHostException e) {
								Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
								runOnUiThread(new Runnable() {
									public void run() {
										mUploadMsg.setText(getString(R.string.network_error, hostName));
									}
								});
							}
						}
					}
				}).start();
			}
		});

		mUploadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mUploadMsg.setText(getString(R.string.start_db_upload, DbUtils.SYSTEM_DB_FULL_PATH));
				mProgressDialog = ProgressDialog.show(mContext, "", getString(R.string.progress_upload_db), true);

				new Thread(new Runnable() {
					public void run() {
						runOnUiThread(new Runnable() {
							public void run() {
								mUploadMsg.setText(R.string.db_upload_start);
							}
						});

						if (checkNetworkAdapter()) {
							try {
								checkNetwork();
								if (backup()) {
									uploadDatabaseFile(DbUtils.SYSTEM_DB_FULL_PATH);
								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											mUploadMsg.setText(R.string.db_backup_fail);
										}
									});
								}
							} catch (UnknownHostException e) {
								Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
								runOnUiThread(new Runnable() {
									public void run() {
										mUploadMsg.setText(getString(R.string.network_error, hostName));
									}
								});
							}
						}
					}
				}).start();
			}
		});
	}

	private boolean backup() {
		if (localBackup()) {
			int result = uploadDatabaseFile(DbUtils.SYSTEM_DB_PATH + getLocalBackupFileName());
			if (result == 500) {
				return false;
			} else if (result == 200) {
				return true;
			}
		}

		return false;
	}

	public String getLocalBackupFileName() {
		if (StringUtils.isBlank(localBackupFileName)) {
			this.localBackupFileName = "sg.db_" + System.currentTimeMillis();
		}
		return localBackupFileName;
	}

	private boolean localBackup() {
		DbUtils.getInstance().localBackup(getLocalBackupFileName(), mContext);
		return DbUtils.getInstance().checkBackupFile(getLocalBackupFileName());
	}

	private int upgradeDatabase(String targetUri) {
		try {
			if (checkNetworkAdapter()) {
				checkNetwork();
				processUpgradeDatabase(targetUri);
			}
		} catch (UnknownHostException e) {
			Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
			runOnUiThread(new Runnable() {
				public void run() {
					mUploadMsg.setText(getString(R.string.network_error, hostName));
				}
			});
		}

		mProgressDialog.dismiss();

		localBackupFileName = null;
		return 0;
	}

	private void processUpgradeDatabase(String targetUri) {
		HttpURLConnection conn = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			URL databaseFileUrl = new URL(mDownloadServerUri);
			conn = (HttpURLConnection) databaseFileUrl.openConnection();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);

			fos = new FileOutputStream(new File(targetUri));
			bos = new BufferedOutputStream(fos);
			int read;
			byte[] buffer = new byte[1024];

			while ((read = bis.read(buffer, 0, 1024)) != -1) {
				bos.write(buffer, 0, read);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					mUploadMsg.setText(getString(R.string.db_upgrade_complete));
				}
			});
		} catch (IOException e) {
			Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
			runOnUiThread(new Runnable() {
				public void run() {
					mUploadMsg.setText(getString(R.string.db_upgrade_fail, hostName));
				}
			});
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					Log.e(AppProfile.TAG, e.getMessage(), e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					Log.e(AppProfile.TAG, e.getMessage(), e);
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					Log.e(AppProfile.TAG, e.getMessage(), e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.e(AppProfile.TAG, e.getMessage(), e);
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private int uploadDatabaseFile(String sourceFileUri) {
		try {
			if (checkNetworkAdapter()) {
				checkNetwork();
				return processUploadDatabase(sourceFileUri);
			}
		} catch (UnknownHostException e) {
			Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
			runOnUiThread(new Runnable() {
				public void run() {
					mUploadMsg.setText(getString(R.string.network_error, hostName));
				}
			});
		}

		mProgressDialog.dismiss();
		return 0;
	}

	private int processUploadDatabase(String sourceFileUri) {
		HttpURLConnection conn;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {
			mProgressDialog.dismiss();

			Log.e(AppProfile.TAG, getString(R.string.source_file_not_exist, uploadFilePath + getLocalBackupFileName()));

			runOnUiThread(new Runnable() {
				public void run() {
					mUploadMsg.setText(getString(R.string.source_file_not_exist, uploadFilePath + getLocalBackupFileName()));
				}
			});

			return 0;

		} else {
			FileInputStream fileInputStream = null;
			try {

				// open a URL connection to the Servlet
				fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(mUpLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", sourceFileUri);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFileUri + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				mServerResponseCode = conn.getResponseCode();
				final String serverResponseMessage = conn.getResponseMessage();

				if (mServerResponseCode == 200) {
					runOnUiThread(new Runnable() {
						public void run() {
							mUploadMsg.setText(getString(R.string.db_upload_complete, serverResponseMessage));
							Toast.makeText(mContext, getString(R.string.db_upload_complete, serverResponseMessage), Toast.LENGTH_SHORT).show();
						}
					});
				} else if (mServerResponseCode == 500) {
					runOnUiThread(new Runnable() {
						public void run() {
							mUploadMsg.setText(getString(R.string.db_upload_fail, serverResponseMessage));
						}
					});
				}
			} catch (MalformedURLException ex) {
				mProgressDialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						mUploadMsg.setText(getString(R.string.exception_msg, "MalformedURLException"));
						Toast.makeText(mContext, getString(R.string.exception_msg, "MalformedURLException"), Toast.LENGTH_SHORT).show();
					}
				});

				Log.e(AppProfile.TAG, "Exception: " + ex.getMessage(), ex);
			} catch (Exception e) {
				mProgressDialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						mUploadMsg.setText(getString(R.string.exception_msg, "Exception"));
						Toast.makeText(mContext, getString(R.string.exception_msg, "MalformedURLException"), Toast.LENGTH_SHORT).show();
					}
				});
				Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
			} finally {
				// close the streams //
				try {
					if (fileInputStream != null) {
						fileInputStream.close();
					}
				} catch (IOException e) {
					Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
				}
				try {
					if (dos != null) {
						dos.flush();
					}
				} catch (IOException e) {
					Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
				}
				try {
					if (dos != null) {
						dos.close();
					}
				} catch (IOException e) {
					Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
				}
			}
			localBackupFileName = null;

			return mServerResponseCode;
		}
	}

	private void checkNetwork() throws UnknownHostException {
//		InetAddress.getByName("52.68.183.71");
		InetAddress.getByName(hostName);
	}

	private boolean checkNetworkAdapter() {
		ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mobileNetworkInfo.isConnected() || wifiNetworkInfo.isConnected();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_upload_to_server, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
