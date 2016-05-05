package com.water.jxwz.utils;

import android.content.Context;

import com.loopj.android.http.HttpGet;
import com.water.jxwz.db.Photos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;


import org.apache.http.params.CoreConnectionPNames;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


/**
 * java.net.URL工具类
 */
public class HttpURLTools {

	/**
	 * 使用HTTP的POST方法提交xml数据.
	 * 
	 * @param xml
	 *            提交的xml数据
	 * @param urlPath
	 *            请求路径
	 * @return
	 */
	public static InputStream postXml(String xml, String urlPath) {
		try {
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			byte[] buff = xml.getBytes("UTF-8");
			conn.setConnectTimeout(10 * 1000);
			conn.setDoOutput(true); // 允许输出
			conn.setUseCaches(false); // 不允许缓存
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Length",
					String.valueOf(buff.length));
			conn.setRequestProperty("content-type", "text/html");
			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(buff);
			outStream.flush();
			outStream.close();
			if (conn.getResponseCode() == 200) {
				// printResponse(conn);
				return conn.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用HTTP的POST方法提交的表单.
	 * 
	 * @param urlPath
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            请求参数编码
	 * @return 返回InputStream
	 * @throws Exception
	 */
	public static InputStream postForm(String urlPath,
			Map<String, String> params, String encoding) {
		try {
			StringBuilder sb = new StringBuilder();
			for (Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), encoding));
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
			byte[] data = sb.toString().getBytes();
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(6 * 1000);
			conn.setDoOutput(true);// 发送post请求必须设置允许输出
			conn.setUseCaches(false);// 不适用Cache
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			DataOutputStream dataOutStream = new DataOutputStream(
					conn.getOutputStream());
			dataOutStream.write(data);
			dataOutStream.flush();
			dataOutStream.close();
			if (conn.getResponseCode() == 200) {
				printResponse(conn);
				return conn.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用HTTP的POST方法单个上传文件.
	 * 
	 * @param filePath
	 *            文件路径
	 * @param urlPath
	 *            请求路径
	 */
	public static void postFile(String urlPath, String filePath) {
		try {
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setChunkedStreamingMode(1024 * 1024);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			File file = new File(filePath);
			conn.setRequestProperty("Content-Type", "multipart/form-data;file="
					+ URLEncoder.encode(file.getName(), "UTF-8"));

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			out.flush();
			out.close();
			printResponse(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用HttpClient发送一个get方式的超链接请求.
	 * 
	 * @param urlpath
	 * @return
	 */
	public static HttpResponse sendHttpGet(String urlpath) {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet(urlpath);
			httpclient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 20000); // 设置请求超时时间
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 20000); // 读取超时
			HttpResponse response = httpclient.execute(httpget);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用HttpClient发送一个post方式的请求.
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static HttpResponse sendHttpPost(String url,
			Map<String, String> params) {
		try {
			List<NameValuePair> param = new ArrayList<NameValuePair>(); // 参数
			if (params != null) {
				Iterator<Entry<String, String>> iterator = params.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<String, String> entry = iterator.next();
					param.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
			}

			HttpPost request = new HttpPost(url);
			HttpEntity entity = new UrlEncodedFormEntity(param, "UTF-8");
			request.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 20000); // 设置请求超时时间
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					20000); // 读取超时
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取返回信息
	 * 
	 * @param conn
	 */
	public static String printResponse(HttpURLConnection conn) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append("\n" + line);
			}
			XSYLog.i("==>" + sb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * Http上传图片
	 * 
	 * @param urlPath上传的服务器地址
	 * @param photos上传的Image图片集合
	 * @return 结果(null表示失败)
	 * @throws IOException
	 */
	public static String uploadPhoto(String urlPath, List<Photos> photos,Context context)
			throws IOException {
		int TIME_OUT = 10 * 10000000; // 超时时间
		String CHARSET = "utf-8"; // 设置编码
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成 String
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		String PREFIX = "--", LINE_END = "\r\n";
		DataOutputStream dos = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET);
			// 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			/** * 当文件不为空，把文件包装并且上传 */
			dos = new DataOutputStream(conn.getOutputStream());
			if (photos != null && photos.size() > 0) {

				for (int i = 0; i < photos.size(); i++) {
					if (BitmapDecodeUtil.decodeBitmap(context,photos.get(i).getImgUrl())== null) {
						continue;
					}
					XSYLog.i("photoName:" + photos.get(i).getImgName());
					StringBuffer sb = new StringBuffer();
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINE_END);
					/**
					 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
					 * filename是文件的名字，包含后缀名的 比如:abc.png
					 */
					sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
							+ photos.get(i).getImgName() + "\"" + LINE_END);
					sb.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINE_END);
					sb.append(LINE_END);
					dos.write(sb.toString().getBytes());
					InputStream is = SharedMothed.Bitmap2InputStream(BitmapDecodeUtil.decodeBitmap(context, photos.get(i).getImgUrl()));
					byte[] bytes = new byte[1024];
					int len = 0;
					while ((len = is.read(bytes)) != -1) {
						dos.write(bytes, 0, len);
					}
					is.close();
					dos.write(LINE_END.getBytes());

				}

				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				if (res == 200) {
					StringBuilder returnSb = new StringBuilder();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						returnSb.append("\n" + line);
					}
					XSYLog.i("returnSb:" + returnSb.toString());
					return returnSb.toString();
				}

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return null;
	}

	/**
	 * Http上传语音
	 * 
	 * @param urlPath
	 *            上传地址
	 * @param voicePath
	 *            语音文件地址
	 * @return 结果(null表示失败)
	 * @throws IOException
	 */
	public static String uploadVoice(String urlPath, String voicePath)
			throws IOException {
		int TIME_OUT = 10 * 10000000; // 超时时间
		String CHARSET = "utf-8"; // 设置编码
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成 String
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		String PREFIX = "--", LINE_END = "\r\n";
		DataOutputStream dos = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET);
			// 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			/** * 当文件不为空，把文件包装并且上传 */
			dos = new DataOutputStream(conn.getOutputStream());
			if (voicePath != null) {

				XSYLog.i("voicePath:" + voicePath);
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append("Content-Disposition: form-data; name=\"amr\"; filename=\""
						+ SharedMothed.getFileName(voicePath) + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = FileHelper.readFile(voicePath);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());

				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				if (res == 200) {
					StringBuilder returnSb = new StringBuilder();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						returnSb.append("\n" + line);
					}
					XSYLog.i("returnSb:" + returnSb.toString());
					return returnSb.toString();
				}

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return null;
	}
	
	/**
	 * 下载，字节流下载(byte)
	 * @param urlStr
	 * @return
	 */
	public String downloadByte(String urlStr){
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5*1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			if(conn.getResponseCode() == 200){
				InputStream inStream = conn.getInputStream();
				int len = 0;
				byte[] buf = new byte[1024];
				while ((len = inStream.read(buf)) != -1) {
					sb.append(new String(buf, 0, len, "UTF-8"));
				}
				inStream.close();
			} else {
				XSYLog.i("访问网络失败");
			}
		} catch (Exception e) {
			XSYLog.e(e.getMessage());
		}
		return sb.toString();
	}
	
	/**
	 * 下载，字符流下载(char)
	 * @param urlStr
	 * @return
	 */
	public String downloadChar(String urlStr){
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5*1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			if(conn.getResponseCode() == 200){
				InputStream inStream = conn.getInputStream();
				InputStreamReader inReader = new InputStreamReader(inStream, "UTF-8");
				int len = 0;
				char [] buf = new char[1024];
				while ((len = inReader.read(buf)) != -1) {
					sb.append(new String(buf, 0, len));
				}
				inStream.close();
				inReader.close();
			} else {
				XSYLog.i("访问网络失败");
			}
			
		} catch (Exception e) {
			XSYLog.e(e.getMessage());
		}
		return sb.toString();
	}

}
