/**
 * Title: FileUtil.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company: www.luoxudong.com
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-8-14 上午10:08:26
 * Version 1.0 
 */
package com.luoxudong.app.commonutils;

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
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.luoxudong.app.commonutils.charset.CharsetDetector;

/** 
 * ClassName: FileUtil 
 * Description:文件操作工具类
 * Create by 罗旭东
 * Date 2013-8-14 上午10:08:26 
 */
public class FileUtil {
	public static final int BUFSIZE = 256;
	
	/**
	 * 1KB大小
	 */
	public static final long ONE_KB = 1024;

	/**
	 * 1MB大小
	 */
	public static final long ONE_MB = ONE_KB * ONE_KB;

	/**
	 * 1GB大小
	 */
	public static final long ONE_GB = ONE_KB * ONE_MB;

	/**
	 * 1TB大小
	 */
	public static final long ONE_TB = ONE_KB * ONE_GB;
	
	/**
	 * 拷贝文件时buffer大小
	 */
	private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

	/**
	 * 
	 * @description:检测SD卡是否可用
	 * @return 可用则返回true，不可用则返回false
	 * @throws
	 */
	public static boolean isSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 在SD卡上面创建文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件
	 * @throws IOException
	 *             异常
	 */
	public static File createSDFile(String filePath) throws IOException {
		File file = null;
		
		if (isSDCardAvailable())
		{
			file = new File(filePath);
			file.createNewFile();
		}
		
		return file;
	}

	/**
	 * 在SD卡上面创建目录
	 * 
	 * @param dirName
	 *            目录名称
	 * @return 文件
	 */
	public static File createSDDir(String dirName) {
		File dir = null;
		if (isSDCardAvailable())
		{
			dir = new File(dirName);
			dir.mkdir();
		}
			
		return dir;
	}

	/**
	 * 判断指定的文件是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 是否存在
	 */
	public static boolean isFileExist(String filePath) {
		if (isSDCardAvailable())
		{
			File file = new File(filePath);
			return file.exists();
		}
		
		return false;
	}

	/**
	 * 准备文件夹，文件夹若不存在，则创建
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static void prepareFile(String filePath) {
		if (isSDCardAvailable())
		{
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		
	}

	/**
	 * 删除指定的文件或目录
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static void delete(String filePath) {
		if (filePath == null) {
			return;
		}
		
		if (isSDCardAvailable())
		{
			try {
				File file = new File(filePath);
				delete(file);
			} catch (Exception e) {
			}
		}
		
	}

	/**
	 * 删除指定的文件或目录
	 * 
	 * @param file
	 *            文件
	 */
	public static void delete(File file) {
		if (file == null || !file.exists()) {
			return;
		}
		
		if (isSDCardAvailable())
		{
			if (file.isDirectory()) {
				deleteDirRecursive(file);
			} else {
				file.delete();
			}
		}
		
	}

	/**
	 * 递归删除目录
	 * 
	 * @param dir
	 *            文件路径
	 */
	public static void deleteDirRecursive(File dir) {
		
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return;
		}
		
		if (isSDCardAvailable())
		{
			File[] files = dir.listFiles();
			if (files == null) {
				return;
			}
			for (File f : files) {
				if (f.isFile()) {
					f.delete();
				} else {
					deleteDirRecursive(f);
				}
			}
			dir.delete();
		}
		
	}

	/**
	 * 取得文件大小
	 * 
	 * @param f
	 *            文件
	 * @return long 大小
	 * 
	 */
	public static long getFileSizes(File f) {
		long s = 0;
		FileInputStream fis = null;
		
		try {
			if (f.exists()) {
				fis = new FileInputStream(f);
				s = fis.available();
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if (fis != null)
			{
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
		return s;
	}

	/**
	 * 递归取得文件夹大小
	 * 
	 * @param filedir
	 *            文件
	 * @return 大小
	 */
	public static long getFileSize(File filedir) {
		long size = 0;
		if (null == filedir) {
			return size;
		}
		
		if (!isSDCardAvailable())
		{
			return 0;
		}
		
		File[] files = filedir.listFiles();

		if (files == null)
		{
			return 0;
		}
		
		try {
			for (File f : files) {
				if (f.isDirectory()) {
					size += getFileSize(f);
				} else {
					FileInputStream fis = new FileInputStream(f);
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
	 * 递归获取文件路径
	 * @return
	 */
	public static List<String> getFilePaths(File fileDir)
	{
		if (null == fileDir) 
		{
			return null;
		}
		
		if (!isSDCardAvailable())
		{
			return null;
		}
		
		File[] files = fileDir.listFiles();
		
		if (files == null || files.length == 0)
		{
			return null;
		}
		
		List<String> filePaths = new ArrayList<String>();
		
		
		
		for (File f : files)
		{
			if (f.isDirectory())
			{
				List<String> list = getFilePaths(f);
				if (list != null)
				{
					filePaths.addAll(list);
				}
				
			}
			else {
				filePaths.add(f.getAbsolutePath());
			}
		}
		
		return filePaths;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 *            大小
	 * @return 转换后的文件大小
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

	/**
	 * 
	 * @description:字符串写入指定文件
	 * @param filePath 文件路径
	 * @param filename 文件名称
	 * @param content 文件内容
	 * @return 写入成功返回true，失败返回false
	 * @throws Exception
	 * @throws
	 */
	public static boolean saveFileToSDCard(String filePath, String filename, String content)
			throws Exception {
		boolean flag = false;
		if (isSDCardAvailable()) {
			File file = new File(filePath, filename);
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(content.getBytes());
			outStream.close();
			flag = true;
		}
		return flag;
	}

	/**
	 * 将文件写入SD卡
	 * 
	 * @param path
	 *            路径
	 * @param fileName
	 *            文件名称
	 * @param input
	 *            输入流
	 * @return 文件
	 */
	public static File saveFileToSDCard(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			if (isSDCardAvailable()) {
				createSDDir(path);
				file = createSDFile(path + fileName);
				output = new FileOutputStream(file);

				byte[] buffer = new byte[BUFSIZE];
				int readedLength = -1;
				while ((readedLength = input.read(buffer)) != -1) {
					output.write(buffer, 0, readedLength);
				}
				output.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null)
				{
					output.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}
	
	/**
	 * 
	 * @description: 读文件
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @return 文件内容
	 * @throws
	 */
	public static byte[] readFileFromSDCardForBytes(String filePath, String fileName) {
		byte[] buffer = null;
		try {
			if (isSDCardAvailable()) {
				if(!filePath.endsWith("/")){
					filePath += File.separatorChar;
				}
				String filePaht = filePath + fileName;
				
				File file = new File(filePaht);
				
				if (!file.exists())
				{
					file.createNewFile();
				}
				FileInputStream fin = new FileInputStream(file);
				int length = fin.available();
				buffer = new byte[length];
				fin.read(buffer);
				fin.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return buffer;
	}
	
	/**
	 * 
	 * @description: 读文件
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @return 文件内容 String类型
	 * @throws
	 */
	public static String readFileFromSDCard(String filePath, String fileName) {
		String content = null;
		
		if (isSDCardAvailable()) {
			byte[] buffer = readFileFromSDCardForBytes(filePath, fileName);
			String charset = Charset.defaultCharset().displayName();
			
			if (buffer != null) {
				charset = CharsetDetector.detectEncoding(buffer);
			}
			try {
				content = new String(buffer, charset);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return content;
	}
	
	public static String readFileFromSDCard(String filePath, String fileName, String charset)
	{
		if (filePath == null || "".equals(filePath))
		{
			return null;
		}
		
		File file = new File(filePath, fileName);
		
		if (file == null || !file.exists())
		{
			return null;
		}
		
		String ret = null;
		InputStream in = null;
		byte[] content = null;
		
		try {
			in = new FileInputStream(file);
			content = new byte[(int)file.length()];
			int by = 0;
			int count = 0;
			while ((by = in.read()) != -1) {
				content[count++] = (byte)by;
			}
			ret = new String(content, charset);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally
		{
			if (in != null)
			{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 获取内存中的目录
	 * 
	 * @param context The context to use
	 * @return The external cache path
	 */
	public static String getExternalCachePath(Context context) {
		final String cachePath = "/data/" + context.getPackageName();
		return Environment.getExternalStorageDirectory().getPath() + cachePath;
	}
	
	/**
	 * Get a usable cache directory (external if available, internal otherwise).
	 * 
	 * @param context The context to use
	 * @param uniqueName A unique directory name to append to the cache dir
	 * @return The cache path
	 */
	public static String getDiskCachePath(Context context, String uniqueName) {
		final String cachePath = isSDCardAvailable() ? getExternalCachePath(context)
				: context.getCacheDir().getPath();
		return cachePath + File.separator + uniqueName;
	}
	
	/**
	 * 根据文件路径获取文件名
	 * @param filePath 文件路径
	 * @return 文件名
	 */
	public static String getFileNameByFilePath(String filePath)
	{
		if (filePath == null || "".equals(filePath))
		{
			return "";
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
	public static void copyFile(File srcFile, File destFile) throws IOException
	{
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
	public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException
	{
		if (srcFile == null)
		{
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null)
		{
			throw new NullPointerException("Destination must not be null");
		}
		if (srcFile.exists() == false)
		{
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory())
		{
			throw new IOException("Source '" + srcFile + "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath()))
		{
			throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
		}
		File parentFile = destFile.getParentFile();
		if (parentFile != null)
		{
			if (!parentFile.mkdirs() && !parentFile.isDirectory())
			{
				throw new IOException("Destination '" + parentFile + "' directory cannot be created");
			}
		}
		if (destFile.exists() && destFile.canWrite() == false)
		{
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
	private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException
	{
		if (destFile.exists() && destFile.isDirectory())
		{
			throw new IOException("Destination '" + destFile + "' exists but is a directory");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try
		{
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			long size = input.size();
			long pos = 0;
			long count = 0;
			while (pos < size)
			{
				count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
				pos += output.transferFrom(input, pos, count);
			}
		}
		finally
		{
			closeQuietly(output);
			closeQuietly(fos);
			closeQuietly(input);
			closeQuietly(fis);
		}

		if (srcFile.length() != destFile.length())
		{
			throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
		}
		if (preserveFileDate)
		{
			destFile.setLastModified(srcFile.lastModified());
		}
	}
	
	public static void closeQuietly(OutputStream output)
	{
		closeQuietly((Closeable) output);
	}
	
	public static void closeQuietly(Closeable closeable)
	{
		try
		{
			if (closeable != null)
			{
				closeable.close();
			}
		}
		catch (IOException ioe)
		{
			// ignore
		}
	}
	
	/**
	 * 获取文件后缀,小写
	 * @param filePath
	 * @return
	 */
	public static String getFileSuffix(String filePath)
	{
		if (TextUtils.isEmpty(filePath))
		{
			return null;
		}
		
		int pos = filePath.lastIndexOf(".");
		
		if (pos <= 0 || pos >= filePath.length() - 1)
		{
			return null;
		}
		
		String suffix = filePath.substring(pos + 1);
		
		return suffix.toLowerCase();
	}
}
