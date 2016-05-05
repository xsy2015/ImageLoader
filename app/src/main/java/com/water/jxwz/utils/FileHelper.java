package com.water.jxwz.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;

/**
 * 文件操作类
 * 
 * @author qiaocbao
 * @version 2014-3-31 下午3:12:29
 */
public class FileHelper {
	private static final int FILE_BUFFER_SIZE = 51200;
	private static final String TAG = FileHelper.class.getSimpleName();
	private static final int DEFAULT_BUFFER_SIZE = 1024;
	private Context context;

	public FileHelper(Context context) {
		this.context = context;
	}

	/**
	 * @Description 获取SD卡路径
	 * @return String
	 */
	public static String Get_SDCardPath() {
		String SDPATH = "";

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 得到当前外部存储设备的目录( /SDCARD )
			SDPATH = Environment.getExternalStorageDirectory() + "";
		} else {
			SDPATH = Environment.getExternalStorageDirectory().toString();// 获取跟目录
		}
		return SDPATH;
	}

	/**
	 * @Description 获取文件的目录
	 * @param pathandname
	 *            路径加文件全名
	 * @return String
	 */
	public static String getPath(String pathandname) {
		int end = pathandname.lastIndexOf("/");
		if (end != -1) {
			return pathandname.substring(0, end);
		} else {
			return null;
		}
	}

	/**
	 * @Description 文件是否存在
	 * @param filePath
	 * @return boolean
	 */
	public static boolean fileIsExist(String filePath) {
		if (filePath == null || filePath.length() < 1) {
			XSYLog.e("param invalid, filePath: " + filePath);
			return false;
		}

		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	/**
	 * @Description 读文件
	 * @param filePath
	 * @return InputStream
	 */
	public static InputStream readFile(String filePath) {
		if (null == filePath) {
			XSYLog.e("Invalid param. filePath: " + filePath);
			return null;
		}

		InputStream is = null;

		try {
			if (fileIsExist(filePath)) {
				File f = new File(filePath);
				is = new FileInputStream(f);
			} else {
				return null;
			}
		} catch (Exception ex) {
			XSYLog.e("Exception, ex: " + ex.toString());
			return null;
		}
		return is;
	}

	/**
	 * @Description 创建目录
	 * @param filePath
	 * @return boolean
	 */
	public static boolean CreateDir(String PathandName) {
		File file = null;
		file = new File(PathandName);
		if (!file.exists()) {
			String filePath = getPath(PathandName);
			file = new File(filePath);
			if (!file.exists())
				return file.mkdirs(); // 文件不存在则创建目录
		}
		return true;
	}

	/**
	 * @Description 删除目录或文件
	 * @param filePath
	 * @return boolean
	 */
	public static boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			XSYLog.e("Invalid param. filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);

		if (file == null || !file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (int i = 0; i < list.length; i++) {
				XSYLog.d("delete filePath: " + list[i].getAbsolutePath());
				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}

		XSYLog.d("delete filePath: " + file.getAbsolutePath());
		file.delete();
		return true;
	}

	/**
	 * @Description 写文件
	 * @param filePath
	 * @param inputStream
	 * @return boolean
	 */
	public static boolean writeFile(String filePath, InputStream inputStream) {

		if (null == filePath || filePath.length() < 1) {
			XSYLog.e("Invalid param. filePath: " + filePath);
			return false;
		}

		try {
			File file = new File(filePath);
			if (file.exists()) {
				deleteDirectory(filePath);
			}

			String pth = filePath.substring(0, filePath.lastIndexOf("/"));
			boolean ret = CreateDir(pth);
			if (!ret) {
				XSYLog.e("createDirectory fail path = " + pth);
				return false;
			}

			boolean ret1 = file.createNewFile();
			if (!ret1) {
				XSYLog.e("createNewFile fail filePath = " + filePath);
				return false;
			}

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int c = inputStream.read(buf);
			while (-1 != c) {
				fileOutputStream.write(buf, 0, c);
				c = inputStream.read(buf);
			}

			fileOutputStream.flush();
			fileOutputStream.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * @Description 写文件
	 * @param filePath
	 * @param fileContent
	 * @return boolean
	 */
	public static boolean writeFile(String filePath, String fileContent) {
		return writeFile(filePath, fileContent, false);
	}

	/**
	 * @Description 写文件
	 * @param filePath
	 * @param fileContent
	 * @param append
	 * @return boolean
	 */
	public static boolean writeFile(String filePath, String fileContent,
			boolean append) {
		if (null == filePath || fileContent == null || filePath.length() < 1
				|| fileContent.length() < 1) {
			XSYLog.e("Invalid param. filePath: " + filePath
					+ ", fileContent: " + fileContent);
			return false;
		}

		try {
			File file = new File(filePath);
			if (!file.exists()) {
				if (!file.createNewFile()) {
					return false;
				}
			}

			BufferedWriter output = new BufferedWriter(new FileWriter(file,
					append));
			output.write(fileContent);
			output.flush();
			output.close();
		} catch (IOException ioe) {
			XSYLog.e("writeFile ioe: " + ioe.toString());
			return false;
		}

		return true;
	}

	/**
	 * @Description 获取文件大小
	 * @param filePath
	 * @return long 文件大小（B）
	 */
	public static long getFileSize(String filePath) {
		if (null == filePath) {
			XSYLog.e("Invalid param. filePath: " + filePath);
			return 0;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}

		return file.length();
	}

	/**
	 * @Description 获取文件最后修改时间
	 * @param filePath
	 * @return long
	 */
	public static long getFileModifyTime(String filePath) {
		if (null == filePath) {
			XSYLog.e("Invalid param. filePath: " + filePath);
			return 0;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}

		return file.lastModified();
	}

	/**
	 * @Description 设置文件最后修改时间
	 * @param filePath
	 * @param modifyTime
	 * @return boolean
	 */
	public static boolean setFileModifyTime(String filePath, long modifyTime) {
		if (null == filePath) {
			XSYLog.e("Invalid param. filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return false;
		}

		return file.setLastModified(modifyTime);
	}

	/**
	 * @Description copy文件
	 * @param cr
	 * @param fromPath
	 * @param destUri
	 * @return boolean
	 */
	public static boolean copyFile(ContentResolver cr, String fromPath,
			String destUri) {
		if (null == cr || null == fromPath || fromPath.length() < 1
				|| null == destUri || destUri.length() < 1) {
			XSYLog.e("copyFile Invalid param. cr=" + cr + ", fromPath="
					+ fromPath + ", destUri=" + destUri);
			return false;
		}

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(fromPath);

			// check output uri
			String path = null;
			Uri uri = null;

			String lwUri = destUri.toLowerCase();
			if (lwUri.startsWith("content://")) {
				uri = Uri.parse(destUri);
			} else if (lwUri.startsWith("file://")) {
				uri = Uri.parse(destUri);
				path = uri.getPath();
			} else {
				path = destUri;
			}

			// open output
			if (null != path) {
				File fl = new File(path);
				String pth = path.substring(0, path.lastIndexOf("/"));
				File pf = new File(pth);

				if (pf.exists() && !pf.isDirectory()) {
					pf.delete();
				}

				pf = new File(pth + File.separator);

				if (!pf.exists()) {
					if (!pf.mkdirs()) {
						XSYLog.e("Can't make dirs, path=" + pth);
					}
				}

				pf = new File(path);
				if (pf.exists()) {
					if (pf.isDirectory())
						deleteDirectory(path);
					else
						pf.delete();
				}

				os = new FileOutputStream(path);
				fl.setLastModified(System.currentTimeMillis());
			} else {
				os = new ParcelFileDescriptor.AutoCloseOutputStream(
						cr.openFileDescriptor(uri, "w"));
			}

			// copy file
			byte[] dat = new byte[1024];
			int i = is.read(dat);
			while (-1 != i) {
				os.write(dat, 0, i);
				i = is.read(dat);
			}

			is.close();
			is = null;

			os.flush();
			os.close();
			os = null;

			return true;

		} catch (Exception ex) {
			XSYLog.e("Exception, ex: " + ex.toString());
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (Exception ex) {
				}
				;
			}
			if (null != os) {
				try {
					os.close();
				} catch (Exception ex) {
				}
				;
			}
		}
		return false;
	}

	/**
	 * @Description InputStream转换为byte[]
	 * @param is
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] readAll(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		byte[] buf = new byte[1024];
		int c = is.read(buf);
		while (-1 != c) {
			baos.write(buf, 0, c);
			c = is.read(buf);
		}
		baos.flush();
		baos.close();
		return baos.toByteArray();
	}

	/**
	 * @Description 读文件
	 * @param ctx
	 * @param uri
	 * @return byte[]
	 */
	public static byte[] readFile(Context ctx, Uri uri) {
		if (null == ctx || null == uri) {
			XSYLog.e("Invalid param. ctx: " + ctx + ", uri: " + uri);
			return null;
		}

		InputStream is = null;
		String scheme = uri.getScheme();
		if (scheme.equalsIgnoreCase("file")) {
			is = readFile(uri.getPath());
		}

		try {
			is = ctx.getContentResolver().openInputStream(uri);
			if (null == is) {
				return null;
			}

			byte[] bret = readAll(is);
			is.close();
			is = null;

			return bret;
		} catch (FileNotFoundException fne) {
			XSYLog.e("FilNotFoundException, ex: " + fne.toString());
		} catch (Exception ex) {
			XSYLog.e("Exception, ex: " + ex.toString());
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (Exception ex) {
				}
				;
			}
		}
		return null;
	}

	/**
	 * @Description 写文件
	 * @param filePath
	 * @param content
	 * @return boolean
	 */
	public static boolean writeFile(String filePath, byte[] content) {
		if (null == filePath || null == content) {
			XSYLog.e("Invalid param. filePath: " + filePath + ", content: "
					+ content);
			return false;
		}

		FileOutputStream fos = null;
		try {
			String pth = filePath.substring(0, filePath.lastIndexOf("/"));
			File pf = null;
			pf = new File(pth);
			if (pf.exists() && !pf.isDirectory()) {
				pf.delete();
			}
			pf = new File(filePath);
			if (pf.exists()) {
				if (pf.isDirectory())
					FileHelper.deleteDirectory(filePath);
				else
					pf.delete();
			}

			pf = new File(pth + File.separator);
			if (!pf.exists()) {
				if (!pf.mkdirs()) {
					XSYLog.e("Can't make dirs, path=" + pth);
				}
			}

			fos = new FileOutputStream(filePath);
			fos.write(content);
			fos.flush();
			fos.close();
			fos = null;
			pf.setLastModified(System.currentTimeMillis());

			return true;

		} catch (Exception ex) {
			XSYLog.e("Exception, ex: " + ex.toString());
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (Exception ex) {
				}
				;
			}
		}
		return false;
	}

	/************* ZIP file operation ***************/

	/**
	 * @Description 读ZIP文件
	 * @param zipFileName
	 * @param crc
	 * @return
	 * @return boolean
	 */
	public static boolean readZipFile(String zipFileName, StringBuffer crc) {
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(
					zipFileName));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				long size = entry.getSize();
				crc.append(entry.getCrc() + ", size: " + size);
			}
			zis.close();
		} catch (Exception ex) {
			XSYLog.e("Exception: " + ex.toString());
			return false;
		}
		return true;
	}

	/**
	 * @Description 读GZIP文件
	 * @param zipFileName
	 * @return byte[]
	 */
	public static byte[] readGZipFile(String zipFileName) {
		if (fileIsExist(zipFileName)) {
			XSYLog.i("zipFileName: " + zipFileName);
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(zipFileName);
				int size;
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((size = fin.read(buffer, 0, buffer.length)) != -1) {
					baos.write(buffer, 0, size);
				}
				return baos.toByteArray();
			} catch (Exception ex) {
				XSYLog.i("read zipRecorder file error");
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	/**
	 * @Description 把文件压缩为ZIP文件
	 * @param baseDirName
	 * @param fileName
	 * @param targerFileName
	 * @throws IOException
	 * @return boolean
	 */
	public static boolean zipFile(String baseDirName, String fileName,
			String targerFileName) throws IOException {
		if (baseDirName == null || "".equals(baseDirName)) {
			return false;
		}
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			return false;
		}

		String baseDirPath = baseDir.getAbsolutePath();
		File targerFile = new File(targerFileName);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				targerFile));
		File file = new File(baseDir, fileName);

		boolean zipResult = false;
		if (file.isFile()) {
			zipResult = fileToZip(baseDirPath, file, out);
		} else {
			zipResult = dirToZip(baseDirPath, file, out);
		}
		out.close();
		return zipResult;
	}

	/**
	 * @Description 解压ZIP文件
	 * @param fileName
	 * @param unZipDir
	 * @throws Exception
	 * @return boolean
	 */
	public static boolean unZipFile(String fileName, String unZipDir)
			throws Exception {
		File f = new File(unZipDir);

		if (!f.exists()) {
			f.mkdirs();
		}

		BufferedInputStream is = null;
		ZipEntry entry;
		ZipFile zipfile = new ZipFile(fileName);
		Enumeration<?> enumeration = zipfile.entries();
		byte data[] = new byte[FILE_BUFFER_SIZE];
		XSYLog.i("unZipDir: " + unZipDir);

		while (enumeration.hasMoreElements()) {
			entry = (ZipEntry) enumeration.nextElement();

			if (entry.isDirectory()) {
				File f1 = new File(unZipDir + "/" + entry.getName());
				XSYLog.i("entry.isDirectory XXX " + f1.getPath());
				if (!f1.exists()) {
					f1.mkdirs();
				}
			} else {
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				String name = unZipDir + "/" + entry.getName();
				RandomAccessFile m_randFile = null;
				File file = new File(name);
				if (file.exists()) {
					file.delete();
				}

				file.createNewFile();
				m_randFile = new RandomAccessFile(file, "rw");
				int begin = 0;

				while ((count = is.read(data, 0, FILE_BUFFER_SIZE)) != -1) {
					try {
						m_randFile.seek(begin);
					} catch (Exception ex) {
						XSYLog.e("exception, ex: " + ex.toString());
					}

					m_randFile.write(data, 0, count);
					begin = begin + count;
				}

				file.delete();
				m_randFile.close();
				is.close();
			}
		}

		return true;
	}

	/**
	 * @Description 文件压缩为ZipOutputStream流
	 * @param baseDirPath
	 * @param file
	 * @param out
	 * @throws IOException
	 * @return boolean
	 */
	private static boolean fileToZip(String baseDirPath, File file,
			ZipOutputStream out) throws IOException {
		FileInputStream in = null;
		ZipEntry entry = null;

		byte[] buffer = new byte[FILE_BUFFER_SIZE];
		int bytes_read;
		try {
			in = new FileInputStream(file);
			entry = new ZipEntry(getEntryName(baseDirPath, file));
			out.putNextEntry(entry);

			while ((bytes_read = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes_read);
			}
			out.closeEntry();
			in.close();
		} catch (IOException e) {
			XSYLog.e("Exception, ex: " + e.toString());
			return false;
		} finally {
			if (out != null) {
				out.closeEntry();
			}

			if (in != null) {
				in.close();
			}
		}
		return true;
	}

	/**
	 * @Description 目录文件夹压缩为ZipOutputStream流
	 * @param baseDirPath
	 * @param file
	 * @param out
	 * @throws IOException
	 * @return boolean
	 */
	private static boolean dirToZip(String baseDirPath, File dir,
			ZipOutputStream out) throws IOException {
		if (!dir.isDirectory()) {
			return false;
		}

		File[] files = dir.listFiles();
		if (files.length == 0) {
			ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, dir));

			try {
				out.putNextEntry(entry);
				out.closeEntry();
			} catch (IOException e) {
				XSYLog.e("Exception, ex: " + e.toString());
			}
		}

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				fileToZip(baseDirPath, files[i], out);
			} else {
				dirToZip(baseDirPath, files[i], out);
			}
		}
		return true;
	}

	private static String getEntryName(String baseDirPath, File file) {
		if (!baseDirPath.endsWith(File.separator)) {
			baseDirPath = baseDirPath + File.separator;
		}

		String filePath = file.getAbsolutePath();
		if (file.isDirectory()) {
			filePath = filePath + "/";
		}

		int index = filePath.indexOf(baseDirPath);
		return filePath.substring(index + baseDirPath.length());
	}

	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * 保存二进制流到指定路径
	 * 
	 * @param instream
	 * @param filepath
	 */
	public void saveFile(InputStream instream, String filepath) {
		if (!isExternalStorageWritable()) {
			Log.i(TAG, "SD卡不可用，保存失败");
			return;
		}

		File file = new File(filepath);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int cnt = 0;

			while ((cnt = instream.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}

			instream.close();
			fos.close();

		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}
	}

	/**
	 * Copy file
	 * 
	 * @param from
	 *            origin file path
	 * @param to
	 *            target file path
	 */
	public void copyFile(String from, String to) {
		if (!isExternalStorageWritable()) {
			Log.i(TAG, "SD卡不可用，保存失败");
			return;
		}

		File fileFrom = new File(from);
		File fileTo = new File(to);

		try {

			FileInputStream fis = new FileInputStream(fileFrom);
			FileOutputStream fos = new FileOutputStream(fileTo);
			byte[] buffer = new byte[1024];
			int cnt = 0;

			while ((cnt = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}

			fis.close();
			fos.close();

		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}
	}

	/**
	 * 保存 JSON 字符串到指定文件
	 * 
	 * @param json
	 * @param filepath
	 */
	public boolean saveJson(String json, String filepath) {
		if (!isExternalStorageWritable()) {
			Log.i(TAG, "SD卡不可用，保存失败");
			return false;
		}

		File file = new File(filepath);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(json.getBytes());
			fos.close();

		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * 删除指定的 JSON 文件
	 * 
	 * @param filepath
	 * @return
	 */
	public boolean deleteJson(String filepath) {
		if (!isExternalStorageWritable()) {
			Log.i(TAG, "SD卡不可用，保存失败");
			return false;
		}

		File file = new File(filepath);

		if (file.exists()) {
			file.delete();
		}

		return false;
	}

	/**
	 * 从指定文件读取 JSON 字符串
	 * 
	 * @param filepath
	 * @return
	 */
	public String readJson(String filepath) {
		if (!isExternalStorageWritable()) {
			Log.i(TAG, "SD卡不可用，保存失败");
			return null;
		}

		File file = new File(filepath);
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			reader.close();

		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}

		return sb.toString();
	}

	/**
	 * 保存图片到制定路径
	 * 
	 * @param filepath
	 * @param bitmap
	 */
	public void saveBitmap(String filepath, Bitmap bitmap) {
		if (!isExternalStorageWritable()) {
			Log.i(TAG, "SD卡不可用，保存失败");
			return;
		}

		if (bitmap == null) {
			return;
		}

		try {
			File file = new File(filepath);
			FileOutputStream outputstream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputstream);
			outputstream.flush();
			outputstream.close();
		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}
	}

	/**
	 * 删除 Files 目录下所有的图片
	 * 
	 * @return
	 */
	public boolean cleanCache() {
		if (!isExternalStorageWritable()) {
			Log.i(TAG, "SD卡不可用，保存失败");
			return false;
		}

		File dir = context.getExternalFilesDir(null);

		if (dir != null) {
			for (File file : dir.listFiles()) {
				file.delete();
			}
		}

		return true;
	}

	/**
	 * 计算 Files 目录下图片的大小
	 * 
	 * @return
	 */
	public String getCacheSize() {
		if (!isExternalStorageWritable()) {
			Log.i(TAG, "SD卡不可用，保存失败");
			return null;
		}

		long sum = 0;
		File dir = context.getExternalFilesDir(null);

		if (dir != null) {
			for (File file : dir.listFiles()) {
				sum += file.length();
			}
		}

		if (sum < 1024) {
			return sum + "字节";
		} else if (sum < 1024 * 1024) {
			return (sum / 1024) + "K";
		} else {
			return (sum / (1024 * 1024)) + "M";
		}
	}

	/**
	 * 返回当前应用 SD 卡的绝对路径 like
	 * /storage/sdcard0/Android/data/com.example.test/files
	 */
	public String getAbsolutePath() {
		File root = context.getExternalFilesDir(null);

		if (root != null) {
			return root.getAbsolutePath();
		}

		return null;
	}

	/**
	 * 返回当前应用的 SD卡缓存文件夹绝对路径 like
	 * /storage/sdcard0/Android/data/com.example.test/cache
	 */
	public String getCachePath() {
		File root = context.getExternalCacheDir();

		if (root != null) {
			return root.getAbsolutePath();
		}

		return null;
	}

	public boolean isBitmapExists(String filename) {
		File dir = context.getExternalFilesDir(null);
		File file = new File(dir, filename);

		return file.exists();
	}

	public static String file2String(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file));
			} else {
				reader = new InputStreamReader(new FileInputStream(file),
						encoding);
			}
			// 将输入流写入输出流
			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return writer.toString();
	}

	public static String encodeBase64File(String path) {
		String result = null;
		FileInputStream inputFile = null;
		try {
			File file = new File(path);
			inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			inputFile.read(buffer);

			result = Base64.encodeToString(buffer, Base64.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 根据uri获取inputStream
	 * @param context
	 * @param mLocalImageUri
	 * @return
	 */
	public static InputStream getInputStreamFromUri(Context context,
			Uri mLocalImageUri) {
		InputStream in = null;
		try {
			if (mLocalImageUri.getScheme() == null) {
				// 绝对路径
				File file = new File(mLocalImageUri.toString());
				in = new FileInputStream(file);
			} else if (mLocalImageUri.getScheme().equalsIgnoreCase("content")
					|| mLocalImageUri.getScheme().equalsIgnoreCase("file")) {
				// content://xxx或file://xxx格式的本地uri
				in = context.getContentResolver().openInputStream(
						mLocalImageUri);
			}
		} catch (Exception e) {
			XSYLog.e("IOexception");
			e.printStackTrace();
		}
		return in;
	}

}