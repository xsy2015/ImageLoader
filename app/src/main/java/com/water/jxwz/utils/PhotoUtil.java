package com.water.jxwz.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.widget.Toast;

import com.water.jxwz.base.Constant;


public class PhotoUtil {
	
	private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
	
	private static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
		if(degress==0){
			return bitmap;
		}
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress); 
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }
	
	public static byte[] compressImage(Bitmap image,Boolean ifCompress) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        if(ifCompress){
	        int options = 100;  
	        while ( baos.toByteArray().length / 1024>200) {  //循环判断如果压缩后图片是否大于1M,大于继续压缩         
	            baos.reset();//重置baos即清空baos  
	            options -= 10;//每次都减少10  
	            image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
	        }
        }
      	return baos.toByteArray();
    }  

	/**
	 * 处理图片的入口
	 * @param srcPath
	 * @return
	 */
	public static byte[] getimage(String srcPath,Boolean ifCompress) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为  
        float hh = 1280f;//这里设置高度为1280f  
        float ww = 720f;//这里设置宽度为720f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);         
        return compressImage(bitmap,ifCompress);//压缩好比例大小后再进行质量压缩  
    } 
	/**
	 * byte->file
	 * @param b
	 * @param file
	 */
	public static void saveByte2File(byte[] b, File file) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(b);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	}
	
	/**
	 * 压缩图片文件
	 * @param srcPath
	 * @param file
	 */
	public static void compressPhotoFile(String srcPath,File file){
		saveByte2File(getimage(srcPath, true), file);
	}
	/**
	 * Bitmap->file
	 * @param bmp
	 * @param file
	 * @return
	 */
	public static boolean saveBitmap2File(Bitmap bmp, File file) {
		CompressFormat format = CompressFormat.PNG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bmp.compress(format, quality, stream);
	}
	/**
	 * Bitmap->Bytes
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		if (url == null) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}	
	/**
	 * Uri->FileSrc
	 * @param context
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromURI(Context context ,Uri contentUri) {
		 String res = null;
		 String[] proj = { MediaColumns.DATA };
		 Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		 if(cursor.moveToFirst()){;
		  int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		  res = cursor.getString(column_index);
		 }
		 cursor.close();
		 return res;
	}
	
	/**
	 * 通过拍照或者相册获取照片
	 * @param activity
	 * @param file 图片保存的地址(这里一般就是临时地址，因为后面还需要裁剪)
	 */
	/*public static void getPhoto(final Activity activity,final File file){
		new ActionSheetDialog(activity).builder()
		.setCanceledOnTouchOutside(true)
		.addSheetItem("拍照", null,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						camera(activity,file);
					}
				})
		.addSheetItem("相册", null,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						gallery(activity);
					}
				}).show();
	}*/
	
	public static void camera(Context context,File file) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
		} else {
			Toast.makeText(context, "请检查手机存储卡",Toast.LENGTH_LONG).show();
		}
		((Activity)context).startActivityForResult(intent, Constant.PHOTO_REQUEST_CAMERA);
	}

	private static boolean hasSdcard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	// * 从相册获取
	public static void gallery(Activity activity) {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		activity.startActivityForResult(intent, Constant.PHOTO_REQUEST_GALLERY);
	}
	
	/**
	 * 剪切照片（从照相机拍照获取）
	 * @param file  被剪照片的file
	 * @param saveFile 剪切后保存的文件的file
	 */
	public static void crop(Activity activity,File file,File saveFile) {
		
		saveByte2File(getimage(file.getPath(),false), file);//校验图片旋转的角度，有些手机拍照后，图片角度会被旋转90°或者207°
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(file), "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
		intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
		intent.putExtra("output", Uri.fromFile(saveFile));  // 专入目标文件     
		intent.putExtra("outputFormat", "PNG"); //输入文件格式  
		activity.startActivityForResult(intent, Constant.PHOTO_REQUEST_CROP);
	}
	
	/**
	 * 剪切照片(从相册获取)
	 * @param uri  被剪照片的uri
	 * @param saveFile 剪切后保存的文件的file
	 */
	public static void crop(Activity activity,Uri uri,File saveFile) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
		intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
		intent.putExtra("output", Uri.fromFile(saveFile));  // 专入目标文件     
		intent.putExtra("outputFormat", "PNG"); //输入文件格式  
		activity.startActivityForResult(intent, Constant.PHOTO_REQUEST_CROP);
	}
	
	//获取剪切的bitmap
	public static Bitmap getCropBitmap(File file){
		return PhotoUtil.getLoacalBitmap(file.getPath());
	}
	
	/*//获取剪切的bitmap
	public static Uri getCropBitmapUri(Bundle extras){
		File tempFile =  new File(Environment.getExternalStorageDirectory(),
				PHOTO_FILE_CUT_NAME);

		return Uri.fromFile(tempFile);
	}*/
}
