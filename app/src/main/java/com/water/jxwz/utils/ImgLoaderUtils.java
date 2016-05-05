package com.water.jxwz.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;



import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.water.jxwz.base.Constant;
import com.water.jxwz.base.MyApplication;

/**
 * 图片下载
 * 
 * @author qiaocbao
 * @version 2014-11-12 下午6:21:43
 */
public class ImgLoaderUtils {

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private File cacheDir = StorageUtils.getOwnCacheDirectory(
			MyApplication.getContext(), Constant.HHXH_CACHEDIR);

	public ImgLoaderUtils(Context context) {
		if (!imageLoader.isInited()) {
			imageLoader.init(config);
		}
	}

	private ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
			MyApplication.getContext())
			.memoryCacheExtraOptions(480, 800)
			// max width, max height，即保存的每个缓存文件的最大长宽
			// .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
			// // Can slow ImageLoader, use it carefully (Better don't use
			// it)/设置缓存的详细信息，最好不要设置这个
			.threadPoolSize(2)
			// 线程池内加载的数量
			.threadPriority(Thread.NORM_PRIORITY - 3)
			.denyCacheImageMultipleSizesInMemory()
			.memoryCache(new WeakMemoryCache())
			// implementation/你可以通过自己的内存缓存实现
			.memoryCacheSize(2 * 1024 * 1024).diskCacheSize(50 * 1024 * 1024)
			.diskCacheFileNameGenerator(new Md5FileNameGenerator())
			// 将保存的时候的URI名称用MD5 加密
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.diskCacheFileCount(100) // 缓存的文件数量
			//.diskCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
			.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
			.imageDownloader(
					new BaseImageDownloader(MyApplication.getContext(),
							10 * 1000, 30 * 1000)) // connectTimeout (5 s),
													// readTimeout (30 s)超时时间
													// .writeDebugLogs() //
													// Remove for release app
			.build();// 开始构建

	/**
	 * 加载listivew头像图片（如头像等小图片）
	 * 
	 * @param uri
	 *            图片uri地址
	 * @param imageView
	 * @param dw
	 *            默认图片
	 * @param isFillet
	 *            是否圆角
	 */
	public void imgLoader(String uri, ImageView imageView, int dw,
			boolean isFillet) {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.showImageOnLoading(null); // 加载图片时的图片
		builder.showImageOnFail(dw);// 设置图片加载或解码过程中发生错误显示的图片
		builder.showImageForEmptyUri(dw);// 设置图片Uri为空或是错误的时候显示的图片
		builder.cacheInMemory(false);// 设置下载的图片是否缓存在内存中
		builder.cacheOnDisk(true);// 启用外存缓存
		builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		// if (isFillet) {// 是否圆角
		//
		// builder.displayer(new RoundedBitmapDisplayer(5));
		// }
		builder.bitmapConfig(Bitmap.Config.RGB_565);// 设置图片的解码类型
		DisplayImageOptions listOptions = builder.build();

		try {
			imageLoader.displayImage(uri, imageView, listOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 加载listivew头像图片（如头像等小图片）
	 * 
	 * @param uri
	 *            图片uri地址
	 * @param imageView
	 * @param dw
	 *            默认图片
	 * @param isFillet
	 *            是否圆角
	 */
	public void imgLoaderNoRepeat(String uri, ImageView imageView, int dw,
			boolean isFillet) {
	    if (!uri.equals(imageView.getTag())) {
		
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.showImageOnLoading(dw); // 加载图片时的图片
		builder.showImageOnFail(dw);// 设置图片加载或解码过程中发生错误显示的图片
		builder.showImageForEmptyUri(dw);// 设置图片Uri为空或是错误的时候显示的图片
		builder.cacheInMemory(false);// 设置下载的图片是否缓存在内存中
		builder.cacheOnDisk(true);// 启用外存缓存
		builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		// if (isFillet) {// 是否圆角
		//
		// builder.displayer(new RoundedBitmapDisplayer(5));
		// }
		builder.bitmapConfig(Bitmap.Config.RGB_565);// 设置图片的解码类型
		DisplayImageOptions listOptions = builder.build();
		
		try {
		    imageLoader.displayImage(uri, imageView, listOptions);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    imageView.setTag(uri);
	}
	
	/**
	 * 清除缓存
	 */
	public void clearCache() {
		try {
			// 清除内存缓存
			imageLoader.clearMemoryCache();
			// 清除SD卡中的缓存
			imageLoader.clearDiskCache();
		} catch (Exception e) {
			XSYLog.e("clearCache:" + e.getMessage());
		}
	}
}
