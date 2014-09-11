package com.wyy.myhealth.imag.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.file.utils.FileUtils;
import com.wyy.myhealth.file.utils.SdUtils;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.utils.ImageUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * 图片工具类
 * 
 * @author lyl
 * 
 */
public class PhotoUtils {

	public static Uri imageFileUri;

	// 图片上传选择途径
	public static void MyDialog(final Activity context) {
		final CharSequence[] items = { context.getString(R.string.photo), context.getString(R.string.takepic) };
		AlertDialog dlg = new AlertDialog.Builder(context).setTitle(context.getString(R.string.changebg))
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 这里item是根据选择的方式，
						// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
						if (item == 1) {
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							context.startActivityForResult(getImageByCamera, 1);
						} else {
							Intent getImage = new Intent(
									Intent.ACTION_GET_CONTENT);
							getImage.addCategory(Intent.CATEGORY_OPENABLE);
							getImage.setType("image/jpeg");
							context.startActivityForResult(getImage, 0);
						}
					}
				}).create();
		dlg.show();
	}

	/**
	 * 保存图片到手机内存中
	 * 
	 * @param data
	 *            保存的数据
	 * @param context
	 *            上下文
	 */
	public static void saveChatCode(byte[] data, Context context) {

		File file = new File(FileUtils.WYY_PIC);
		try {
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream ops = context.openFileOutput(FileUtils.WYY_PIC,
					Context.MODE_PRIVATE);
			ObjectOutputStream outputStream = null;
			outputStream = new ObjectOutputStream(ops);
			outputStream.writeObject(data);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 保存图片到手机内存
	 * 
	 * @param bitmap
	 * @param context
	 */
	public static void saveChatCode(Bitmap bitmap, Context context) {

		File file = new File(FileUtils.HEAD_PATH);
		try {
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream ops = context.openFileOutput(FileUtils.HEAD_PATH,
					Context.MODE_PRIVATE);
			ObjectOutputStream outputStream = null;
			outputStream = new ObjectOutputStream(ops);
			outputStream.writeObject(bitmap);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 保存用户头像
	 * 
	 * @param headimage
	 * @param context
	 */
	public static void saveUserHeadimg(String headimage, Context context) {

		Bitmap mbitmap;
		try {
			mbitmap = ImageUtil.getBitmapFromUrl(headimage);
			mbitmap = Bmprcy.toRoundBitmap(mbitmap);
			saveChatCode(mbitmap, context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 图片上传选择途径
	public static void secPic(final Activity context) {
		final CharSequence[] items = {context.getString(R.string.photo), context.getString(R.string.takepic) };
		AlertDialog dlg = new AlertDialog.Builder(context).setTitle(R.string.gl_choose_title)
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 这里item是根据选择的方式，
						// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
						if (item == 1) {
							if (SdUtils.ExistSDCard()) {
								try {
									imageFileUri = context
											.getContentResolver()
											.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
													new ContentValues());
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}

							} else {
								imageFileUri = context
										.getContentResolver()
										.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
												new ContentValues());
							}

							if (imageFileUri != null) {
								Intent getImageByCamera = new Intent(
										"android.media.action.IMAGE_CAPTURE");

								getImageByCamera
										.putExtra(
												android.provider.MediaStore.EXTRA_OUTPUT,
												imageFileUri);

								context.startActivityForResult(
										getImageByCamera, 1);
							} else {
								Toast.makeText(
										context,
										context.getResources().getString(
												R.string.cant_insert_album),
										Toast.LENGTH_SHORT).show();
							}

						} else {
							Intent getImage = new Intent(
									Intent.ACTION_GET_CONTENT);
							getImage.addCategory(Intent.CATEGORY_OPENABLE);
							getImage.setType("image/jpeg");
							context.startActivityForResult(getImage, 0);
						}
					}
				}).create();
		dlg.show();
	}

	@SuppressWarnings("deprecation")
	public static String getPicPathFromUri(Uri uri, Activity activity) {
		String value = uri.getPath();

		if (value.startsWith("/external")) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else {
			return value;
		}
	}

	/**
	 * 保存用户头像
	 */
	public static void saveUserheadimg() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String headimage = HealthHttpClient.IMAGE_URL
						+ WyyApplication.getInfo().getHeadimage();
				Bitmap mbitmap;
				try {
					mbitmap = ImageUtil.getBitmapFromUrl(headimage);
					mbitmap = Bmprcy.toRoundBitmap(mbitmap);
					File file = new File(FileUtils.HEALTH_IMAG, WyyApplication
							.getInfo().getUsername() + ".png");
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(file));
					mbitmap.compress(CompressFormat.PNG, 100, bos);
					bos.flush();
					bos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * 保存用户头像
	 */
	public static void saveUserheadimg(final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String headimage = HealthHttpClient.IMAGE_URL
						+ WyyApplication.getInfo().getHeadimage();
				Bitmap mbitmap;
				try {
					mbitmap = ImageUtil.getBitmapFromUrl(headimage);
					mbitmap = Bmprcy.toRoundBitmap(mbitmap);
					File file = new File(FileUtils.HEALTH_IMAG, WyyApplication
							.getInfo().getUsername() + ".png");
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(file));
					mbitmap.compress(CompressFormat.PNG, 100, bos);
					bos.flush();
					bos.close();
					sendModifyBrocast(context);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}

	private static final void sendModifyBrocast(Context context) {
		Intent intent = new Intent();
		intent.setAction(ConstantS.ACTION_BASE_INFO_CHANGE);
		context.sendBroadcast(intent);
	}
	
	
	/**
	 * 获取listview 头像
	 */

	public static Bitmap getListHeadBg() {
		Bitmap mBitmap=null;
		try {
			mBitmap = BitmapFactory.decodeFile(FileUtils.HEALTH_IMAG
					+ "/" + WyyApplication.getInfo().getUsername() + "hps"
					+ ".jpg");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return mBitmap;
		

	}
	

	public static Bitmap getScaledBitmap(String fileName, int dstWidth) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, localOptions);
		int originWidth = localOptions.outWidth;
		int originHeight = localOptions.outHeight;

		localOptions.inSampleSize = originWidth > originHeight ? originWidth
				/ dstWidth : originHeight / dstWidth;
		localOptions.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(fileName, localOptions);
	}
	
}
