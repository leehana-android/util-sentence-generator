package kr.co.leehana.sg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import kr.co.leehana.sg.AppProfile;
import kr.co.leehana.sg.db.SGSQLiteDbHelper;

/**
 * Created by Hana Lee on 2015-08-13 18:50
 *
 * @author <a href="mailto:i@leehana.co.kr">Hana Lee</a>
 * @since 2015-08-13 18:50
 */
public class DbUtils {

	@SuppressLint("SdCardPath")
	public static final String SYSTEM_DB_PATH = "/data/data/" + AppProfile.PACKAGE_NAME + "/databases/";
	public static final String SYSTEM_DB_FULL_PATH = SYSTEM_DB_PATH + SGSQLiteDbHelper.DATABASE_NAME;

	private static DbUtils instance = new DbUtils();

	public static DbUtils getInstance() {
		return instance;
	}

	private DbUtils() {
	}

	public void prepareDatabase(Context context) {
		boolean bResult = isDatabaseExistOnSystemFolder();  // DB가 있는지?
		if (!bResult) {   // DB가 없으면 복사
			copyDatabaseToSystemFolder(context);
		}
		copyDatabaseToSystemFolder(context);
	}

	private boolean isDatabaseExistOnSystemFolder() {
		return new File(SYSTEM_DB_FULL_PATH).exists();
	}

	private void copyDatabaseToSystemFolder(Context context) {
		AssetManager manager = context.getAssets();
		File folder = new File(SYSTEM_DB_PATH);
		File file = new File(SYSTEM_DB_FULL_PATH);

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			is = manager.open(SGSQLiteDbHelper.DATABASE_NAME);
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

	public void localBackup(String backupFileName, Context context) {
		AssetManager manager = context.getAssets();

		File file = new File(SYSTEM_DB_PATH + backupFileName);

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			is = manager.open(SGSQLiteDbHelper.DATABASE_NAME);
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

	public boolean checkBackupFile(String backupFileName) {
		File backupFile = new File(SYSTEM_DB_PATH + backupFileName);
		return backupFile.exists();
	}
}
