/**
 * Title: FileUtil.java
 * Description:
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company:  个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-8-14 上午10:08:26
 * Version 1.0 
 */
package com.luoxudong.app.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.luoxudong.app.utils.charset.CharsetDetector;

/** 
 * ClassName: FileUtil 
 * Description:文件操作工具类
 * Create by 罗旭东
 * Date 2013-8-14 上午10:08:26 
 */
public class FileUtil {
	public static final int BUFSIZE = 256;
	
	/** 1KB大小 */
	public static final long ONE_KB = 1024;

	/** 1MB大小 */
	public static final long ONE_MB = ONE_KB * ONE_KB;

	/** 1GB大小 */
	public static final long ONE_GB = ONE_KB * ONE_MB;

	/** 1TB大小 */
	public static final long ONE_TB = ONE_KB * ONE_GB;
	
	/** 拷贝文件时buffer大小 */
	private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

	/**
	 * 
	 * @description:检测SD卡是否可用
	 * @return 可用则返回true，不可用则返回false
	 * @throws
	 */
	public static boolean isSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 创建文件
	 * @param filePath
	 * @return
	 */
	public static File createFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		
		File file = new File(filePath);
		
		if (!file.getParentFile().exists()) {
			createDir(file.getParentFile().getAbsolutePath());
		}
		
		try {
			if (!file.exists() && !file.createNewFile()) {
				return null;
			}
		} catch (IOException e) {
			return null;
		}
		
		return file;
	}
	
	/**
	 * 在SD卡上面创建文件
	 * @param filePath
	 * @return
	 */
	public static File createSDFile(String filePath) {
		if (!isSDCardAvailable()) {
			return null;
		}
		
		return createFile(filePath);
	}
	
	/**
	 * 创建目录
	 * @param dirName
	 * @return
	 */
	public static File createDir(String filePath) {
		return createDir(filePath, null);
	}

	/**
	 * 在SD卡上面创建目录
	 * @param dirName
	 * @return
	 */
	public static File createSDDir(String filePath) {
		if (!isSDCardAvailable()) {
			return null;
		}
		
		return createDir(filePath);
	}
	
	public static File createDir(String filePath, String dirName) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		
		File file = null;
		
		if (TextUtils.isEmpty(dirName)) {
			file = new File(filePath);
		} else {
			file = new File(filePath, dirName);
		}
		
		if (!file.exists() && !file.mkdirs()) {
			return null;
		}
		
		return file;
	}
	
	public static File createSDDir(String filePath, String dirName) {
		if (!isSDCardAvailable()) {
			return null;
		}
		
		return createDir(filePath, dirName);
	}

	/**
	 * 判断指定的文件是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		
		return new File(filePath).exists();
	}

	/**
	 * 删除指定的文件或目录
	 * @param filePath
	 */
	public static void delete(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return;
		}
		
		try {
			File file = new File(filePath);
			delete(file);
		} catch (Exception e) {
		}
	}

	/**
	 * 删除指定的文件或目录
	 * @param file
	 */
	public static void delete(File file) {
		if (file == null || !file.exists()) {
			return;
		}
		
		if (file.isDirectory()) {
			deleteDirRecursive(file);
		} else {
			file.delete();
		}
	}
	
	/**
	 * 清除目录中的文件及子目录
	 * @param dir
	 */
	public static void cleanDir(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return;
		}
		
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		
		for (File file : files) {
			delete(file);
		}
	}

	/**
	 * 删除指定的目录，该目录也删除
	 * @param file
	 */
	public static void deleteDirRecursive(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return;
		}
		
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		
		for (File file : files) {
			if (file.isFile()) {
				file.delete();
			} else {
				deleteDirRecursive(file);
			}
		}
		
		dir.delete();
	}

	/**
	 * 取得文件大小
	 * @param f
	 * @return
	 */
	public static long getFileSizes(File file) {
		long size = 0;
		FileInputStream fis = null;
		
		try {
			if (file.exists()) {
				fis = new FileInputStream(file);
				size = fis.available();
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
		return size;
	}

	/**
	 * 获取文件夹大小
	 * @param filedir
	 * @return
	 */
	public static long getFileSize(File filedir) {
		long size = 0;
		if (null == filedir) {
			return size;
		}
		
		File[] files = filedir.listFiles();

		if (files == null) {
			return 0;
		}
		
		try {
			for (File file : files) {
				if (file.isDirectory()) {
					size += getFileSize(file);
				} else {
					FileInputStream fis = new FileInputStream(file);
					size += fis.available();
					fis.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;

	}
	
	/**
	 * 获取目录中所有的文件路径
	 * @return
	 */
	public static List<String> getFilePaths(File fileDir) {
		List<String> filePaths = new ArrayList<String>();
		
		if (null == fileDir) {
			return filePaths;
		}
		
		File[] files = fileDir.listFiles();
		
		if (files == null || files.length == 0) {
			return filePaths;
		}
		
		for (File f : files) {
			if (f.isDirectory()) {
				List<String> list = getFilePaths(f);
				if (list != null) {
					filePaths.addAll(list);
				}
			} else {
				filePaths.add(f.getAbsolutePath());
			}
		}
		
		return filePaths;
	}

	/**
	 * 文件大小格式化
	 * @param fileS
	 * @return
	 */
	public static String formatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.0");
		String fileSizeString = "";
		if (fileS == 0) {
			fileSizeString = "0" + "KB";
		} else if (fileS < ONE_KB) {
			fileSizeString = df.format((double) fileS) + "Byte";
		} else if (fileS < ONE_MB) {
			fileSizeString = df.format((double) fileS / ONE_KB) + "K";
		} else if (fileS < ONE_GB) {
			fileSizeString = df.format((double) fileS / ONE_MB) + "M";
		} else {
			fileSizeString = df.format((double) fileS / ONE_GB) + "G";
		}
		return fileSizeString;
	}

	public static File saveFile(String filePath, String fileName, String content) {
		createDir(filePath);
		File file = new File(filePath, fileName);
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
			outStream.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
		
		return file;
	}
	
	/**
	 * 
	 * @description:字符串写入指定文件
	 * @param filePath 文件路径
	 * @param filename 文件名称
	 * @param content 文件内容
	 * @return 
	 * @throws Exception
	 * @throws
	 */
	public static File saveFileToSDCard(String filePath, String fileName, String content) throws Exception {
		if (!isSDCardAvailable()) {
			return null;
		}

		return saveFile(filePath, fileName, content);
	}

	public static File saveFile(String filePath, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			file = createFile(new File(filePath, fileName).getAbsolutePath());
			output = new FileOutputStream(file);

			byte[] buffer = new byte[BUFSIZE];
			int readedLength = -1;
			while ((readedLength = input.read(buffer)) != -1) {
				output.write(buffer, 0, readedLength);
			}
			output.flush();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (output != null)
				{
					output.close();
				}
			} catch (IOException e) {
				return null;
			}
		}

		return file;
	}
	
	/**
	 * 已数据流的方式将文件写入SD卡
	 * @param filePath
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static File saveFileToSDCard(String filePath, String fileName, InputStream input) {
		if (!isSDCardAvailable()) {
			return null;
		}
		
		return saveFile(filePath, fileName, input);
	}
	
	/**
	 * 读文件
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static byte[] readFileForBytes(String filePath, String fileName) {
		byte[] buffer = null;
		try {
			File file = new File(filePath, fileName);
			
			if (!file.exists()) {
				return null;
			}
			
			FileInputStream fin = new FileInputStream(file);
			int length = fin.available();
			buffer = new byte[length];
			fin.read(buffer);
			fin.close();
		} catch (Exception e) {
			return null;
		}
		
		return buffer;
	}
	
	/**
	 * 读文件
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static byte[] readFileFromSDCardForBytes(String filePath, String fileName) {
		if (!isSDCardAvailable()) {
			return null;
		}
		
		return readFileForBytes(filePath, fileName);
	}
	
	public static String readFile(String filePath, String fileName, String charset) {
		String content = null;
		
		byte[] buffer = readFileForBytes(filePath, fileName);
		
		try {
			if (buffer != null) {
				if (TextUtils.isEmpty(charset)) {
					charset = CharsetDetector.detectEncoding(buffer);
				}
				
				content = new String(buffer, charset);
			}
		} catch (UnsupportedEncodingException e) {
		}
		
		return content;
	}
	
	public static String readFileFromSDCard(String filePath, String fileName, String charset) {
		if (!isSDCardAvailable()) {
			return null;
		}
		
		return readFile(filePath, fileName, charset);
	}
	
	/**
	 * 读文本文件
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static String readFileFromSDCard(String filePath, String fileName) {
		if (!isSDCardAvailable()) {
			return null;
		}
		
		return readFile(filePath, fileName, null);
	}
	
	public static String readFile(String filePath, String fileName) {
		return readFile(filePath, fileName, null);
	}
	
	/**
     * 获取内部存储缓存目录，删除app时清除
     * @param context
     * @return
     */
	public static File getIntCacheDir(Context context) {
        return context.getCacheDir();
    }

    /**
     * 获取内部存储文件目录，删除app时清除
     * @param context
     * @return
     */
    public static File getIntFileDir(Context context) {
        return context.getFilesDir();
    }

    /**
     * 获取外部存储缓存目录，删除app时清除
     * @param context
     * @return
     */
    public static File getExtCacheDir(Context context) {
        if(!isSDCardAvailable()){//未挂载，则返回默认路径
            return null;
        }

        File file = context.getExternalCacheDir();

        if (file == null) {
            return null;
        }

        return file;
    }

    /**
     * 获取外部存储文件目录，删除app时清除
     * @param context
     * @return
     */
    public static File getExtFileDir(Context context) {
        if(!isSDCardAvailable()){//未挂载，则返回默认路径
            return null;
        }

        File file = context.getExternalFilesDir(null);

        if (file == null) {
            return null;
        }

        return file;
    }

    /**
     * 获取外部存储项目根，删除app时不清除
     * @param context
     * @return
     */
    public static File getExtRootDir() {
        if(!isSDCardAvailable()){//未挂载，则使用内部存储
            return null;
        } else {
            return Environment.getExternalStorageDirectory();

        }
    }
	
	/**
	 * 根据文件路径获取文件名
	 * @param filePath 文件路径
	 * @return 文件名
	 */
	public static String getFileNameByFilePath(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		
		File file = new File(filePath);
		
		return file.getName();
	}
	
	/**
	 * 
	 * @description:拷贝文件
	 * @param srcFile 源文件
	 * @param destFile 目标文件
	 * @throws IOException
	 * @return void
	 * @throws
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		copyFile(srcFile, destFile, true);
	}
	
	/**
	 * 
	 * @description:拷贝文件
	 * @param srcFile
	 * @param destFile
	 * @param preserveFileDate
	 * @throws IOException
	 * @return void
	 * @throws
	 */
	public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		
		if (srcFile.exists() == false) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' exists but is a directory");
		}
		
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
		}
		
		File parentFile = destFile.getParentFile();
		if (parentFile != null) {
			if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
				throw new IOException("Destination '" + parentFile + "' directory cannot be created");
			}
		}
		
		if (destFile.exists() && destFile.canWrite() == false) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		}
		
		doCopyFile(srcFile, destFile, preserveFileDate);
	}
	
	/**
	 * 
	 * @description:开始拷贝文件
	 * @param srcFile
	 * @param destFile
	 * @param preserveFileDate
	 * @throws IOException
	 * @return void
	 * @throws
	 */
	private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' exists but is a directory");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			long size = input.size();
			long pos = 0;
			long count = 0;
			while (pos < size) {
				count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
				pos += output.transferFrom(input, pos, count);
			}
		} finally {
			closeQuietly(output);
			closeQuietly(fos);
			closeQuietly(input);
			closeQuietly(fis);
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
		}
		
		if (preserveFileDate) {
			destFile.setLastModified(srcFile.lastModified());
		}
	}
	
	public static void closeQuietly(OutputStream output) {
		closeQuietly((Closeable) output);
	}
	
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		}
		catch (IOException ioe) {
			// ignore
		}
	}
	
	/**
	 * 获取文件后缀,小写
	 * @param filePath
	 * @return
	 */
	public static String getFileSuffix(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		
		int pos = filePath.lastIndexOf(".");
		
		if (pos <= 0 || pos >= filePath.length() - 1) {
			return null;
		}
		
		String suffix = filePath.substring(pos + 1);
		
		return suffix.toLowerCase();
	}
}
