package com.wyy.myhealth.imag.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import com.wyy.myhealth.file.utils.FileUtils;
import com.wyy.myhealth.ui.scan.ScanFoodActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

public class PhoneUtlis {

	/**
	 * ��bitmapת����String
	 * 
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(String filePath) {

		Bitmap bm = getSmallBitmap(filePath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);

	}

	/**
	 * ��bitmapת����String
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToString(Bitmap bitmap) {

		if (bitmap == null) {
			return "";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);

	}

	/**
	 * ��bitmapת����String��ѹ��ͼƬ��С
	 * 
	 * @param filePath
	 * @return
	 */
	public static String bitmapzoomToString(String filePath) {

		Bitmap bm = getSmall2ZoomBitmap(filePath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);

	}

	public static String bitmap_Small_ZoomToString(String filePath) {

		Bitmap bm = getSmallNoCutBitmap(filePath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);

	}

	/**
	 * ��bitmapת����String
	 * 
	 * @param filePath
	 * @return
	 */
	public static synchronized String bitmapNCutToString(String filePath) {

		Bitmap bm = getNoCutSmallBitmap(filePath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);

	}

	/**
	 * bitmapתString
	 * 
	 * @param context
	 * @return
	 */
	public static String bitmapToString(Context context) {
		Bitmap bm = comp(getBitmap(context));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/**
	 * ����ͼƬ������ֵ
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * ����·�����ͻ�Ʋ�ѹ������bitmap������ʾ
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {

		Matrix matrix = new Matrix();
		matrix.setRotate(ScanFoodActivity.angle);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);
		if (mBitmap == null) {
			return null;
		}
		float width = mBitmap.getWidth();
		float height = mBitmap.getHeight();
		float ratio = width / height;
		mBitmap = Bitmap
				.createBitmap(mBitmap, (int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() - mBitmap.getHeight()
								* ratio / 3) / 2,
						(int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() / 3 * ratio), matrix, true);

		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}

		int digree = 0;
		if (exif != null) {
			// ��ȡͼƬ�����������Ϣ
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			// ������ת�Ƕ�
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:
				digree = 0;
				break;
			}
		}

		if (digree != 0) {
			// ��תͼƬ
			Matrix m = new Matrix();
			m.postRotate(digree);
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), m, true);
		}

		SavePic.saveFoodPic2Example(mBitmap);
		return mBitmap;
	}

	public static Bitmap getSmallNoCutBitmap(String filePath) {

		Matrix matrix = new Matrix();
		matrix.setRotate(ScanFoodActivity.angle);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);
		if (mBitmap == null) {
			return null;
		}
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix,
				true);

		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}

		int digree = 0;
		if (exif != null) {
			// ��ȡͼƬ�����������Ϣ
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			// ������ת�Ƕ�
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:
				digree = 0;
				break;
			}
		}

		if (digree != 0) {
			// ��תͼƬ
			Matrix m = new Matrix();
			m.postRotate(digree);
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), m, true);
		}

		return mBitmap;
	}

	/**
	 * ����·�����ͻ�Ʋ�ѹ������bitmap������ʾ
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getNoCutSmallBitmap(String filePath) {

		// Matrix matrix = new Matrix();
		// matrix.setRotate(ScanningActivity.angle);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 400, 400);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);
		if (mBitmap == null) {
			return null;
		}
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}

		int digree = 0;
		if (exif != null) {
			// ��ȡͼƬ�����������Ϣ
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			// ������ת�Ƕ�
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:
				digree = 0;
				break;
			}
		}

		if (digree != 0) {
			// ��תͼƬ
			Matrix m = new Matrix();
			m.postRotate(digree);
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), m, true);
		}
		// float width=mBitmap.getWidth();
		// float height=mBitmap.getHeight();
		// float ratio=width/height;
		// mBitmap = Bitmap.createBitmap(mBitmap, (int) (mBitmap.getWidth()/3),
		// (int) (mBitmap.getHeight()-mBitmap.getHeight()*ratio/3)/2, (int)
		// (mBitmap.getWidth()/3),
		// (int) (mBitmap.getHeight() / 3*ratio), matrix, true);

		return mBitmap;
	}

	/**
	 * ����·�����ͻ�Ʋ�ѹ������bitmap������ʾ��ѹ����СΪ100x100
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmall100ZoomBitmap(String filePath) {

		Matrix matrix = new Matrix();
		matrix.setRotate(ScanFoodActivity.angle);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);
		if (mBitmap == null) {
			return null;
		}
		float width = mBitmap.getWidth();
		float height = mBitmap.getHeight();
		float ratio = width / height;
		mBitmap = Bitmap
				.createBitmap(mBitmap, (int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() - mBitmap.getHeight()
								* ratio / 3) / 2,
						(int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() / 3 * ratio), matrix, true);

		mBitmap = zoomImage(mBitmap, 100, 100);

		mBitmap = getORIENTATIONBitmap(filePath, mBitmap);

		SavePic.saveFoodPic20Example(mBitmap);

		return mBitmap;
	}

	/**
	 * ����·�����ͻ�Ʋ�ѹ������bitmap������ʾ��ѹ����СΪ60x60
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmall60ZoomBitmap(String filePath) {

		Matrix matrix = new Matrix();
		matrix.setRotate(ScanFoodActivity.angle);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);
		if (mBitmap == null) {
			return null;
		}
		float width = mBitmap.getWidth();
		float height = mBitmap.getHeight();
		float ratio = width / height;
		mBitmap = Bitmap
				.createBitmap(mBitmap, (int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() - mBitmap.getHeight()
								* ratio / 3) / 2,
						(int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() / 3 * ratio), matrix, true);

		mBitmap = zoomImage(mBitmap, 60, 60);
		mBitmap = getORIENTATIONBitmap(filePath, mBitmap);
		return mBitmap;
	}

	/**
	 * ����·�����ͻ�Ʋ�ѹ������bitmap������ʾ��ѹ����СΪ60x60
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmall40ZoomBitmap(String filePath) {

		Matrix matrix = new Matrix();
		matrix.setRotate(ScanFoodActivity.angle);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);
		if (mBitmap == null) {
			return null;
		}
		float width = mBitmap.getWidth();
		float height = mBitmap.getHeight();
		float ratio = width / height;
		mBitmap = Bitmap
				.createBitmap(mBitmap, (int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() - mBitmap.getHeight()
								* ratio / 3) / 2,
						(int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() / 3 * ratio), matrix, true);

		mBitmap = zoomImage(mBitmap, 40, 40);
		// SavePic.saveFoodPic20Example(mBitmap);
		return mBitmap;
	}

	public static Bitmap getSmall20Bitmap(String filePath) {

		Matrix matrix = new Matrix();
		matrix.setRotate(0);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inJustDecodeBounds = true;
		// BitmapFactory.decodeFile(filePath, options);
		//
		// // Calculate inSampleSize
		// options.inSampleSize = calculateInSampleSize(options, 480, 800);
		//
		// // Decode bitmap with inSampleSize set
		// options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);
		if (mBitmap == null) {
			return null;
		}
		float width = mBitmap.getWidth();
		float height = mBitmap.getHeight();
		// mBitmap = Bitmap.createBitmap(mBitmap, (int) (mBitmap.getWidth() /
		// 4),
		// (int) (height) / 4, (int) (width / 2), (int) (height / 2),
		// matrix, true);
		mBitmap = Bitmap.createBitmap(mBitmap, (int) (mBitmap.getWidth() / 10),
				(int) (height) / 10, (int) (width / 5 * 4),
				(int) (height / 5 * 4), matrix, true);
		SavePic.saveFoodPicHalfExample(mBitmap);
		return mBitmap;
	}

	/**
	 * ����·�����ͻ�Ʋ�ѹ������bitmap������ʾ��ѹ����СΪ30x30
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmall2ZoomBitmap(String filePath) {

		Matrix matrix = new Matrix();
		matrix.setRotate(ScanFoodActivity.angle);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);
		if (mBitmap == null) {
			return null;
		}
		float width = mBitmap.getWidth();
		float height = mBitmap.getHeight();
		float ratio = width / height;
		mBitmap = Bitmap
				.createBitmap(mBitmap, (int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() - mBitmap.getHeight()
								* ratio / 3) / 2,
						(int) (mBitmap.getWidth() / 3),
						(int) (mBitmap.getHeight() / 3 * ratio), matrix, true);

		SavePic.saveFoodPic2Example(mBitmap);

		mBitmap = zoomImage(mBitmap, 30, 30);

		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}

		int digree = 0;
		if (exif != null) {
			// ��ȡͼƬ�����������Ϣ
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			// ������ת�Ƕ�
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:
				digree = 0;
				break;
			}
		}

		if (digree != 0) {
			// ��תͼƬ
			Matrix m = new Matrix();
			m.postRotate(digree);
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), m, true);
		}

		// SavePic.saveFoodPic2Example(mBitmap);

		return mBitmap;
	}

	public static Bitmap getSmall8ZoomBitmap(String filePath) {

		Matrix matrix = new Matrix();
		matrix.setRotate(ScanFoodActivity.angle);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);
		if (mBitmap == null) {
			return null;
		}
		try {
			float width = mBitmap.getWidth();
			float height = mBitmap.getHeight();
			float ratio = width / height;
			mBitmap = Bitmap.createBitmap(mBitmap,
					(int) (mBitmap.getWidth() / 3),
					(int) (mBitmap.getHeight() - mBitmap.getHeight() * ratio
							/ 3) / 2, (int) (mBitmap.getWidth() / 3),
					(int) (mBitmap.getHeight() / 3 * ratio), matrix, true);

			mBitmap = zoomImage(mBitmap, 80, 80);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// SavePic.saveFoodPic2Example(mBitmap);

		return mBitmap;
	}

	/**
	 * ���ֻ��ڴ���ͼƬ
	 * 
	 * @param context
	 * @return
	 */
	public static Bitmap getBitmap(Context context) {
		Matrix matrix = new Matrix();
		matrix.setRotate(ScanFoodActivity.angle);
		byte[] data;
		Bitmap mBitmap;
		InputStream ies;
		try {
			ies = context.openFileInput(FileUtils.WYY_PIC);
			ObjectInputStream obi = new ObjectInputStream(ies);
			data = (byte[]) obi.readObject();
			obi.close();
			ies.close();
			mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			mBitmap = Bitmap.createBitmap(mBitmap, mBitmap.getWidth() / 3,
					mBitmap.getHeight() / 3, mBitmap.getWidth() / 3,
					mBitmap.getHeight() / 3, matrix, true);
			return mBitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * ��ȡͷ��
	 * 
	 * @param context
	 * @return
	 */
	public static Bitmap getHeadBitmap(Context context) {
		Matrix matrix = new Matrix();
		matrix.setRotate(ScanFoodActivity.angle);
		Bitmap mBitmap;
		InputStream ies;
		try {
			ies = context.openFileInput(FileUtils.HEAD_PATH);
			ObjectInputStream obi = new ObjectInputStream(ies);
			mBitmap = (Bitmap) obi.readObject();
			obi.close();
			ies.close();
			mBitmap = Bitmap.createBitmap(mBitmap, mBitmap.getWidth() / 3,
					mBitmap.getHeight() / 3, mBitmap.getWidth() / 3,
					mBitmap.getHeight() / 3, matrix, true);
			return mBitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * ����·��ɾ��ͼƬ
	 * 
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * ��ӵ�ͼ��
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	/**
	 * ��ȡ����ͼƬ��Ŀ¼
	 * 
	 * @return
	 */
	public static File getAlbumDir() {
		File dir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * ��ȡ���� ��������ͼƬ�ļ�������
	 * 
	 * @return
	 */
	public static String getAlbumName() {
		return "sheguantong";
	}

	public static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 400f;
		float ww = 300f;
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;

		newOpts.inPreferredConfig = Config.ARGB_8888;

		newOpts.inPurgeable = true;

		newOpts.inInputShareable = true;

		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return bitmap;
	}

	/***
	 * ͼƬ�����ŷ���
	 * 
	 * @param bgimage
	 *            ��ԴͼƬ��Դ
	 * @param newWidth
	 *            �����ź���
	 * @param newHeight
	 *            �����ź�߶�
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// ��ȡ���ͼƬ�Ŀ�͸�
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// ��������ͼƬ�õ�matrix����
		Matrix matrix = new Matrix();
		// ������������
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ����ͼƬ����
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	public static Bitmap getORIENTATIONBitmap(String filePath, Bitmap mBitmap) {
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}

		int digree = 0;
		if (exif != null) {
			// ��ȡͼƬ�����������Ϣ
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			// ������ת�Ƕ�
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:
				digree = 0;
				break;
			}
		}

		if (digree != 0) {
			// ��תͼƬ
			Matrix m = new Matrix();
			m.postRotate(digree);
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), m, true);
		}
		return mBitmap;
	}

}
