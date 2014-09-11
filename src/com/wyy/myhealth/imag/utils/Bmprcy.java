package com.wyy.myhealth.imag.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class Bmprcy {
	/**
	 * 圆形切图
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 二进制转换
	 * 
	 * @param bm
	 * @return
	 */
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 30, baos);
		return baos.toByteArray();
	}

	public static Bitmap bitmpCulate(byte[] data) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// opts.inJustDecodeBounds = true;
		// BitmapFactory.decodeByteArray(data, 0, data.length, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
		opts.inJustDecodeBounds = false;

		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}

	public static int computeSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);  
  
        int roundedSize;  
        if (initialSize <= 8 ) {  
            roundedSize = 1;  
            while (roundedSize < initialSize) {  
                roundedSize <<= 1;  
            }  
        } else {  
            roundedSize = (initialSize + 7) / 8 * 8;  
        }  
  
        return roundedSize;  
    }  
	
	
	 private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
	        double w = options.outWidth;  
	        double h = options.outHeight;  
	  
	        int lowerBound = (maxNumOfPixels == -1) ? 1 :  
	                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
	        int upperBound = (minSideLength == -1) ? 128 :  
	                (int) Math.min(Math.floor(w / minSideLength),  
	                Math.floor(h / minSideLength));  
	  
	        if (upperBound < lowerBound) {  
	            // return the larger one when there is no overlapping zone.  
	            return lowerBound;  
	        }  
	  
	        if ((maxNumOfPixels == -1) &&  
	                (minSideLength == -1)) {  
	            return 1;  
	        } else if (minSideLength == -1) {  
	            return lowerBound;  
	        } else {  
	            return upperBound;  
	        }  
	    }  
	
}
