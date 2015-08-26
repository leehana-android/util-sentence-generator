package kr.co.leehana.sg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import kr.co.leehana.sg.AppProfile;
import kr.co.leehana.sg.R;
import kr.co.leehana.sg.db.SGDatabases;
import kr.co.leehana.sg.db.SGSQLiteDbHelper;
import kr.co.leehana.sg.factory.DbHelperFactory;
import kr.co.leehana.sg.model.Favorite;
import kr.co.leehana.sg.model.FavoriteCategory;
import kr.co.leehana.sg.model.Setting;
import kr.co.leehana.sg.model.Words;
import kr.co.leehana.sg.service.FavoriteServiceImpl;
import kr.co.leehana.sg.service.IFavoriteService;
import kr.co.leehana.sg.service.ISettingService;
import kr.co.leehana.sg.service.IWordService;
import kr.co.leehana.sg.service.SettingServiceImpl;
import kr.co.leehana.sg.service.WordServiceImpl;

/**
 * Created by Hana Lee on 2015-08-13 18:50
 *
 * @author <a href="mailto:i@leehana.co.kr">Hana Lee</a>
 * @since 2015-08-13 18:50
 */
public class DbUtils {

	public static final String LOCAL_DATABASE_NAME = "sg.db";
	public static final String SERVER_DATABASE_NAME = "sg.db_sv";

	@SuppressLint("SdCardPath")
	public static final String SYSTEM_DB_PATH = "/data/data/" + AppProfile.PACKAGE_NAME + "/databases/";
	public static final String SYSTEM_DB_FULL_PATH = SYSTEM_DB_PATH + LOCAL_DATABASE_NAME;
	public static final String SERVER_DB_FULL_PATH = SYSTEM_DB_FULL_PATH + "_sv";

	private static final String mUpLoadServerUri = "http://leehana.co.kr/UploadToServer.php";
	private static final String mDownloadServerUri = "http://leehana.co.kr/sg/data/sg.db";

	private static String localBackupFileName;

	public static void prepareDatabase(Context context) {
		boolean bResult = isDatabaseExistOnSystemFolder();  // DB가 있는지?
		if (!bResult) {   // DB가 없으면 복사
			copyDatabaseToSystemFolder(context);
		}
//		copyDatabaseToSystemFolder(context);
	}

	private static boolean isDatabaseExistOnSystemFolder() {
		return new File(SYSTEM_DB_FULL_PATH).exists();
	}

	private static void copyDatabaseToSystemFolder(Context context) {
		AssetManager manager = context.getAssets();
		File folder = new File(SYSTEM_DB_PATH);
		File file = new File(SYSTEM_DB_FULL_PATH);

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			is = manager.open(LOCAL_DATABASE_NAME);
			bis = new BufferedInputStream(is);

			if (!folder.exists()) {
				folder.mkdirs();
			}

			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int read;
			byte[] buffer = new byte[1024];
			while ((read = bis.read(buffer, 0, 1024)) != -1) {
				bos.write(buffer, 0, read);
			}

			bos.flush();
		} catch (IOException e) {
			Log.e(AppProfile.TAG, e.getMessage(), e);
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
		}
	}

	public static void localBackup(String backupFileName, Context context) {
		AssetManager manager = context.getAssets();

		File file = new File(SYSTEM_DB_PATH + backupFileName);

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			is = manager.open(LOCAL_DATABASE_NAME);
			bis = new BufferedInputStream(is);

			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int read;
			byte[] buffer = new byte[1024];
			while ((read = bis.read(buffer, 0, 1024)) != -1) {
				bos.write(buffer, 0, read);
			}

			bos.flush();
		} catch (IOException e) {
			Log.e(AppProfile.TAG, e.getMessage(), e);
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
		}
	}

	public static boolean checkBackupFile(String backupFileName) {
		File backupFile = new File(SYSTEM_DB_PATH + backupFileName);
		return backupFile.exists();
	}

	public static boolean sync(Context context) {
		localBackupFileName = null;
		if (backup(context)) {
			if (downloadDatabase(SERVER_DB_FULL_PATH)) {
				localDatabaseMigration(context);

				int resultCode = uploadDatabase(context, SYSTEM_DB_FULL_PATH);

				return resultCode == 200;
			}
		}

		return false;
	}

	private static boolean backup(Context context) {
		localBackup(getLocalBackupFileName(), context);
		if (checkBackupFile(getLocalBackupFileName())) {
			int result = uploadDatabase(context, DbUtils.SYSTEM_DB_PATH + getLocalBackupFileName());

			localBackupFileName = null;
			return result == 200;
		}
		localBackupFileName = null;
		return false;
	}

	private static void localDatabaseMigration(Context context) {
		SGSQLiteDbHelper localDbHelper = DbHelperFactory.create(context, LOCAL_DATABASE_NAME);
		SGSQLiteDbHelper serverDbHelper = DbHelperFactory.create(context, SERVER_DATABASE_NAME);

		WordServiceImpl serverWordService = WordServiceImpl.getInstance();
		FavoriteServiceImpl serverFavoriteService = FavoriteServiceImpl.getInstance();
		serverWordService.setHelper(serverDbHelper);
		serverFavoriteService.setHelper(serverDbHelper);

		List<Words> serverDbNoBackupWords = serverWordService.getNoBackupWords();
		List<Favorite> serverDbNoBackupFavorites = serverFavoriteService.getNoBackupFavorite();
		List<FavoriteCategory> serverDbNoBackupFavoriteCategories = serverFavoriteService.getNoBackupFavoriteCategory();

		WordServiceImpl localWordService = WordServiceImpl.getInstance();
		FavoriteServiceImpl localFavoriteService = FavoriteServiceImpl.getInstance();
		localWordService.setHelper(localDbHelper);
		localFavoriteService.setHelper(localDbHelper);

		wordDataMigration(localWordService, serverDbNoBackupWords);
		favoriteDataMigration(localFavoriteService, serverDbNoBackupFavorites);
		favoriteCategoryDataMigration(localFavoriteService, serverDbNoBackupFavoriteCategories);
	}

	private static void favoriteCategoryDataMigration(IFavoriteService localService, List<FavoriteCategory> serverDbNoBackupFavoriteCategories) {
		if (serverDbNoBackupFavoriteCategories != null && !serverDbNoBackupFavoriteCategories.isEmpty()) {
			for (FavoriteCategory favoriteCategory : serverDbNoBackupFavoriteCategories) {
				favoriteCategory.setBackup(true);

				localService.insertCategory(favoriteCategory);
			}
		}
	}

	private static void wordDataMigration(IWordService localService, List<Words> serverDbNoBackupWords) {
		if (serverDbNoBackupWords != null && !serverDbNoBackupWords.isEmpty()) {
			for (Words word : serverDbNoBackupWords) {
				word.setBackup(true);

				localService.insert(word);
			}
		}
	}

	private static void favoriteDataMigration(IFavoriteService localService, List<Favorite> serverDbNoBackupFavorites) {
		if (serverDbNoBackupFavorites != null && !serverDbNoBackupFavorites.isEmpty()) {
			for (Favorite favorite : serverDbNoBackupFavorites) {
				favorite.setBackup(true);

				localService.insert(favorite);
			}
		}
	}

	public static String getLocalBackupFileName() {
		if (StringUtils.isBlank(localBackupFileName)) {
			localBackupFileName = LOCAL_DATABASE_NAME + "_" + System.currentTimeMillis();
		}
		return localBackupFileName;
	}

	private static int uploadDatabase(Context context, String sourceFileUri) {
		HttpURLConnection conn;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		int responseCode = 0;
		if (!sourceFile.isFile()) {
			Log.e(AppProfile.TAG, context.getString(R.string.source_file_not_exist, sourceFileUri));
			return responseCode;
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
				responseCode = conn.getResponseCode();
			} catch (MalformedURLException ex) {
				Log.e(AppProfile.TAG, "Exception: " + ex.getMessage(), ex);
			} catch (Exception e) {
				Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
			} finally {
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

			return responseCode;
		}
	}

	public static boolean downloadDatabase(String savedTargetUri) {
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

			fos = new FileOutputStream(new File(savedTargetUri));
			bos = new BufferedOutputStream(fos);
			int read;
			byte[] buffer = new byte[1024];

			while ((read = bis.read(buffer, 0, 1024)) != -1) {
				bos.write(buffer, 0, read);
			}
			return true;
		} catch (IOException e) {
			Log.e(AppProfile.TAG, "Exception : " + e.getMessage(), e);
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

		return false;
	}
}
