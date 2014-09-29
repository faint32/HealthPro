package com.wyy.myhealth.support.bitmap;

import android.graphics.Bitmap;

public class BitmapRatioUtils {

	public static double ratio(Bitmap srcImage) {
		if (srcImage==null) {
			return 0;
		}
		int[] inPixels = new int[srcImage.getWidth() * srcImage.getHeight()];
		int[] intensity = new int[256];
		int mlength=intensity.length;
		for (int i = 0; i < mlength; i++) {
			intensity[i] = 0;
		}
		int width=srcImage.getWidth();
		int heigth=srcImage.getHeight();
		srcImage.getPixels(inPixels, 0,width, 0, 0, width, heigth);
		int index = 0;
		int count = 0;
		for (int row = 0; row < heigth; row++) {
			int ta = 0, tr = 0, tg = 0, tb = 0;
			for (int col = 0; col < width; col++) {
				index = row * width + col;
				ta = (inPixels[index] >> 24) & 0xff;
				tr = (inPixels[index] >> 16) & 0xff;
				tg = (inPixels[index] >> 8) & 0xff;
				tb = inPixels[index] & 0xff;
				int gray = (int) (0.299 * (double) tr + 0.587 * (double) tg + 0.114 * (double) tb);
				intensity[gray]++;

			}
		}
		for (int i = 0; i < mlength; i++) {
			if (intensity[i] > 0)
				count++;
		}
		double r = (count / 256.0);
		// System.out.println("±ÈÂÊ£º"+r);
		return r;
	}

//	/**
//	 * A convenience method for getting ARGB pixels from an image. This tries to
//	 * avoid the performance penalty of BufferedImage.getRGB unmanaging the
//	 * image.
//	 */
//	public int[] getRGB(Bitmap image, int x, int y, int width,
//			int height, int[] pixels) {
////		int type = image.get
////		if (type == BufferedImage.TYPE_INT_ARGB
////				|| type == BufferedImage.TYPE_INT_RGB)
////			return (int[]) image.getRaster().getDataElements(x, y, width,
////					height, pixels);
//		return image.getRGB(x, y, width, height, pixels, 0, width);
//	}

}
