package kr.co.leehana.sg;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import kr.co.leehana.sg.utils.DbUtils;

public class UploadToServer extends AppCompatActivity {

	TextView messageText;
	Button uploadButton;
	int serverResponseCode = 0;
	ProgressDialog dialog = null;

	String upLoadServerUri = null;

	/**********
	 * File Path
	 *************/
	final String uploadFilePath = DbUtils.SYSTEM_DB_PATH;
	final String uploadFileName = "sg.db";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_to_server);

		uploadButton = (Button) findViewById(R.id.uploadButton);
		messageText = (TextView) findViewById(R.id.messageText);

		messageText.setText("Uploading file path :- '"
				+ DbUtils.SYSTEM_DB_FULL_PATH + "'");

		/************* Php script path ****************/
		upLoadServerUri = "http://52.68.183.71/UploadToServer.php";
		uploadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog = ProgressDialog.show(UploadToServer.this, "",
						"Uploading file...", true);

				new Thread(new Runnable() {
					public void run() {
						runOnUiThread(new Runnable() {
							public void run() {
								messageText.setText("uploading started.....");
							}
						});

						uploadFile(DbUtils.SYSTEM_DB_FULL_PATH);

					}
				}).start();
			}
		});
	}

	public int uploadFile(String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			Log.e(AppProfile.TAG, "Source File not exist :" + uploadFilePath + ""
					+ uploadFileName);

			runOnUiThread(new Runnable() {
				public void run() {
					messageText.setText("Source File not exist :"
							+ uploadFilePath + "" + uploadFileName);
				}
			});

			return 0;

		} else {
			FileInputStream fileInputStream = null;
			try {

				// open a URL connection to the Servlet
				fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				try {
					InetAddress address = InetAddress.getByName("52.68.183.71");
				} catch (UnknownHostException e) {
					Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
				}

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

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
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i(AppProfile.TAG, "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {

							String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
									+ " http://leehana.co.kr/"
									+ uploadFileName;

							messageText.setText(msg);
							Toast.makeText(UploadToServer.this,
									"File Upload Complete.", Toast.LENGTH_SHORT)
									.show();
						}
					});
				} else if (serverResponseCode == 500) {
					runOnUiThread(new Runnable() {
						public void run() {

							String msg = "File Upload Fail.";

							messageText.setText(msg);
						}
					});
				}
			} catch (MalformedURLException ex) {

				dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(UploadToServer.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText.setText("Got Exception : see logcat ");
						Toast.makeText(UploadToServer.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
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
			dialog.dismiss();
			return serverResponseCode;

		} // End else block
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
