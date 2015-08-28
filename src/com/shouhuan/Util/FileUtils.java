package com.shouhuan.Util;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import android.os.Environment;

public class FileUtils {
	public static String getImgSavePath() {
		String path = getRootDir() + "/shouhuanImgSave/";
		File destDir = new File(path);
		if (!destDir.exists()) {// 创建文件�?
			destDir.mkdirs();
		}
		return path;
	}

	/**
	 * 使用当前时间戳拼接一个文件名
	 * 
	 * @param format
	 * @return
	 */
	public static String getFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
		String fileName = format.format(new Timestamp(System
				.currentTimeMillis()));
		return fileName;
	}

	/**
	 * 获取根目�?
	 */
	public static String getRootDir() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static String getAppRootDir() {
		String sdpath = getRootDir();
		File destDir = new File(sdpath + "/shouhuan/");
		if (!destDir.exists()) {// 创建文件�?
			destDir.mkdirs();
		}
		createNoMediaFile();
		return destDir.getAbsolutePath();
	}

	/**
	 * 获取拍照路径
	 */
	public static String getCameraPath() {
		String sdpath = getRootDir();
		File destDir = new File(sdpath + "/shouhuan/camera");
		if (!destDir.exists()) {// 创建文件�?
			destDir.mkdirs();
		}
		createNoMediaFile();
		return destDir.getAbsolutePath();

	}

	private static void createNoMediaFile() {
		File file = new File(getRootDir() + "/shouhuan/.nomedia");
		if (!file.exists()) {// 判断文件是否存在（不存在则创建这个文件）
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 得到绝对路径
	 * 
	 * @param dir
	 * @return
	 */
	public static String getgetAbsoluteDir(String dir) {
		return getRootDir() + dir;

	}

	/**
	 * 创建根目�?
	 * 
	 * @param path
	 *            目录路径
	 */
	public static void createDirFile(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 创建的文�?
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 * 删除文件�?
	 * 
	 * @param folderPath
	 *            文件夹的路径
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件的路�?
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 * 判断SD是否可以
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
}
