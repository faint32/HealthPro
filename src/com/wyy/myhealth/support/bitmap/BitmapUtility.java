package com.wyy.myhealth.support.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

import com.wyy.myhealth.utils.BingLog;

public class BitmapUtility {

	public Bitmap setBlur(Bitmap bmpSource, int Blur) // Դλͼ��ģ��ǿ��
	{
		int pixels[] = new int[bmpSource.getWidth() * bmpSource.getHeight()]; // ��ɫ���飬һ�����ض�Ӧһ��Ԫ��
		int pixelsRawSource[] = new int[bmpSource.getWidth()
				* bmpSource.getHeight() * 3]; // ��ԭɫ���飬��ΪԪ���ݣ���ÿһ��ģ��ǿ�ȵ�ʱ�򲻿ɸ���
		int pixelsRawNew[] = new int[bmpSource.getWidth()
				* bmpSource.getHeight() * 3]; // ��ԭɫ���飬���ܼ��������ԭɫֵ
		bmpSource.getPixels(pixels, 0, bmpSource.getWidth(), 0, 0,
				bmpSource.getWidth(), bmpSource.getHeight()); // ��ȡ���ص�

		// ģ��ǿ�ȣ�ÿѭ��һ��ǿ������һ��
		for (int k = 1; k <= Blur; k++) {
			// ��ͼƬ�л�ȡÿ��������ԭɫ��ֵ
			for (int i = 0; i < pixels.length; i++) {
				pixelsRawSource[i * 3 + 0] = Color.red(pixels[i]);
				pixelsRawSource[i * 3 + 1] = Color.green(pixels[i]);
				pixelsRawSource[i * 3 + 2] = Color.blue(pixels[i]);
			}

			// ȡÿ�����������ҵ��ƽ��ֵ���Լ���ֵ
			int CurrentPixel = bmpSource.getWidth() * 3 + 3; // ��ǰ��������ص㣬�ӵ�(2,2)��ʼ
			for (int i = 0; i < bmpSource.getHeight() - 3; i++) // �߶�ѭ��
			{
				for (int j = 0; j < bmpSource.getWidth() * 3; j++) // ���ѭ��
				{
					CurrentPixel += 1;
					// ȡ�������ң�ȡƽ��ֵ
					int sumColor = 0; // ��ɫ��
					sumColor = pixelsRawSource[CurrentPixel
							- bmpSource.getWidth() * 3]; // ��һ��
					sumColor = sumColor + pixelsRawSource[CurrentPixel - 3]; // ��һ��
					sumColor = sumColor + pixelsRawSource[CurrentPixel + 3]; // ��һ��
					sumColor = sumColor
							+ pixelsRawSource[CurrentPixel
									+ bmpSource.getWidth() * 3]; // ��һ��
					pixelsRawNew[CurrentPixel] = Math.round(sumColor / 4); // �������ص�
				}
			}

			// ������ԭɫ��ϳ�������ɫ
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = Color.rgb(pixelsRawNew[i * 3 + 0],
						pixelsRawNew[i * 3 + 1], pixelsRawNew[i * 3 + 2]);
			}
		}

		// Ӧ�õ�ͼ��
		Bitmap bmpReturn = Bitmap.createBitmap(bmpSource.getWidth(),
				bmpSource.getHeight(), Config.ARGB_8888);
		bmpReturn.setPixels(pixels, 0, bmpSource.getWidth(), 0, 0,
				bmpSource.getWidth(), bmpSource.getHeight()); // �����½�λͼȻ����䣬����ֱ�����Դͼ�񣬷����ڴ汨��

		return bmpReturn;
	}

	/**
	 * ����ģ��
	 * 
	 * @param sentBitmap
	 *            ��ȡ��ͼƬ
	 * @param radius
	 *            ģ���̶�
	 * @return ģ��ͼƬ
	 */

	public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

		// Stack Blur v1.0 from
		// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
		//
		// Java Author: Mario Klingemann <mario at quasimondo.com>
		// http://incubator.quasimondo.com
		// created Feburary 29, 2004
		// Android port : Yahel Bouaziz <yahel at kayenko.com>
		// http://www.kayenko.com
		// ported april 5th, 2012

		// This is a compromise between Gaussian Blur and Box blur
		// It creates much better looking blurs than Box Blur, but is
		// 7x faster than my Gaussian Blur implementation.
		//
		// I called it Stack Blur because this describes best how this
		// filter works internally: it creates a kind of moving stack
		// of colors whilst scanning through the image. Thereby it
		// just has to add one new block of color to the right side
		// of the stack and remove the leftmost color. The remaining
		// colors on the topmost layer of the stack are either added on
		// or reduced by one, depending on if they are on the right or
		// on the left side of the stack.
		//
		// If you are using this algorithm in your code please add
		// the following line:
		//
		// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		BingLog.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		/*****************************/

		// int maxr=0,maxg=0,maxb=0;
		//
		// for (y = 0; y < h; y++) {
		//
		// int[] ring=new int[2*radius+10];//����
		// int[] ging=new int[2*radius+10];//����
		// int[] bing=new int[2*radius+10];//����
		//
		// rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum
		// = bsum = 0;
		// for (i = -radius; i <= radius; i++) {
		// p = pix[yi + Math.min(wm, Math.max(i, 0))];
		// sir = stack[i + radius];
		// sir[0] = (p & 0xff0000) >> 16;
		// sir[1] = (p & 0x00ff00) >> 8;
		// sir[2] = (p & 0x0000ff);
		// if (p!=0) {
		// ring[i+radius]=sir[0];
		// ging[i+radius]=sir[1];
		// bing[i+radius]=sir[2];
		// }
		//
		// }
		// for (int j = 0; j < 2*radius; j++) {
		// if (maxr>ring[i]) {
		// maxr=ring[i];
		// }
		// if (maxg>ging[i]) {
		// maxg=ging[i];
		// }
		//
		// if (maxb>bing[i]) {
		// maxb=bing[i];
		// }
		// }
		// }

		/*********** ������� ************************/
		for (y = 0; y < h; y++) {

			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				// if (p==0) {
				// sir[0]=maxr;
				// sir[1]=maxg;
				// sir[2]=maxb;
				// }
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {

					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];
				yi += w;

			}
		}

		BingLog.e("pix", w + " " + h + " " + pix.length);
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return (bitmap);
	}

	/**
	 * ���� : ��˹ģ������
	 * 
	 * @param bmp
	 *            ��Ҫ�����ͼƬ
	 * @param radiuss
	 *            ģ���뾶
	 * @return ģ����ͼƬ
	 */

	public Bitmap blurImageAmelioratebf(Bitmap bmp, int radiuss) {
		long start = System.currentTimeMillis();
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 16; // 值越小图片会越亮，越大则越暗

		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);

		/****** ģ���뾶 **********/
		for (int i0 = -radiuss; i0 < radiuss; i0++) {

			for (int i = 1, length = height - 1; i < length; i++) {
				for (int k = 1, len = width - 1; k < len; k++) {
					idx = 0;

					for (int m = -1; m <= 1; m++) {
						for (int n = -1; n <= 1; n++) {
							pixColor = pixels[(i + m) * width + k + n];
							pixR = Color.red(pixColor);
							pixG = Color.green(pixColor);
							pixB = Color.blue(pixColor);

							newR = newR + (int) (pixR * gauss[idx]);
							newG = newG + (int) (pixG * gauss[idx]);
							newB = newB + (int) (pixB * gauss[idx]);
							idx++;
						}
					}

					newR /= delta;
					newG /= delta;
					newB /= delta;

					newR = Math.min(255, Math.max(0, newR));
					newG = Math.min(255, Math.max(0, newG));
					newB = Math.min(255, Math.max(0, newB));
					pixels[i * width + k] = Color.argb(0, newR, newG, newB);

					newR = 0;
					newG = 0;
					newB = 0;
				}
			}

		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		BingLog.d("used time=" + (end - start));
		return bitmap;
	}

}
