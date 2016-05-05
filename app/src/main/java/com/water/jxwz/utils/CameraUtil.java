package com.water.jxwz.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by KingJA on 2016/1/24.
 */
public class CameraUtil {
    public static String getDateString() {
        return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static File createImageFile()  {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis());
        String imageFileName = "IMG_" + timeStamp;
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator + "WATER";
        File storageDir=new File(path);
        if ((null != storageDir) && (!storageDir.exists())) {
            storageDir.mkdir();
            try {
                storageDir.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       File image = null;
        try {

            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* String filePath=imageFileName+".jpg";
        File image=new File(filePath);
        if ((null != image) && (!image.exists())) {
            image.mkdir();
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
//        String filePath=imageFileName+".jpg";
//        if (image!=null){
//            image.renameTo(new File(filePath));
//        }

        return image;
    }

    public static void bitmap2Location(Bitmap bitmap, String path) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        FileOutputStream fis = null;
        try {
            fis = new FileOutputStream(path);
            fis.write(data);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void byte2Location(byte[] data,String path) {
        FileOutputStream fis=null;
        try {
            fis = new FileOutputStream(path);
            fis.write(data);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 比例压缩
     *
     * @param srcPath
     * @return
     */
    private Bitmap compressScaleFromB2B(String srcPath) {
        Log.i("比例压缩", "比例压缩");
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int imgWidth = newOpts.outWidth;
        int imgHeight = newOpts.outHeight;
        Log.i("outWidth", imgWidth + "");
        Log.i("outHeight", imgHeight + "");
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float scaleWidth = 720 / 4;
        float scaleHeight = 1280 / 4;
        Log.i("scaleWidth", scaleWidth + "");
        Log.i("scaleHeight", scaleHeight + "");
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (imgWidth > imgHeight && imgWidth > scaleWidth) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / scaleWidth);
            Log.i("if be", be + "");
        } else if (imgWidth < imgHeight && imgHeight > scaleHeight) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / scaleHeight);
            Log.i("else be", be + "");
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * 质量压缩
     *
     * @param image
     * @return 最开始使用这个来进行压缩，但是始终压缩不到32k这么小。后来看高手的解释才明白，这种压缩方法之所以称之为质量压缩，是因为它不会减少图片的像素
     * 。它是在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的。进过它压缩的图片文件大小会有改变，
     * 但是导入成bitmap后占得内存是不变的
     * 。因为要保持像素不变，所以它就无法无限压缩，到达一个值之后就不会继续变小了。显然这个方法并不适用与缩略图
     * ，其实也不适用于想通过压缩图片减少内存的适用，仅仅适用于想在保证图片质量的同时减少文件大小的情况而已。
     */
    private Bitmap compressQualityFromB2B(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        Log.i("质量压缩前", baos.toByteArray().length / 1024 + "");
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public static String SDPATH = Environment.getExternalStorageDirectory()
            .getPath() + "/aaddcc/";


    private File compressFromS2F(String srcPath) {
        File temp = new File(SDPATH);
        if (!temp.exists()) {
            boolean mkdir = temp.mkdirs();
        }
        File file = new File(srcPath);
        if (file.length() / 1024 < 300) {
            return file;
        }
        Bitmap bitmapScale = compressScaleFromB2B(srcPath);
        Bitmap bitmapQuality = compressQualityFromB2B(bitmapScale);// 压缩好比例大小后再进行质量压缩
        File scaleFile = new File(SDPATH, getUUIDString() + ".jpg");
        FileOutputStream out;
        try {
            out = new FileOutputStream(scaleFile);
            bitmapQuality.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmapQuality.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scaleFile;
    }

    public static String getUUIDString() {
        String UUIDString = UUID.randomUUID().toString();
        String newUUIDString = UUIDString.replace("-", "");
        String substring = newUUIDString.substring(0, 10);
        return substring;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
