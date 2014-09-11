package com.wyy.myhealth.utils;

import com.wyy.myhealth.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ShareUtils {

	public static void share2Fre(Context context) {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_content));
		shareIntent.setType("text/plain");
		context.startActivity(Intent.createChooser(shareIntent, context
				.getString(R.string.tell_other)));
	}

	public static void shareFood(Context context,String content,Uri uri){
		BingLog.i("·ÖÏí", "ÄÚÈÝ:"+content+"bitmap:"+uri);
		 Intent shareIntent = new Intent(Intent.ACTION_SEND);   
		    if(uri!=null){  
		        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);  
		        shareIntent.setType("image/*");   
		    }else{  
		        shareIntent.setType("text/plain");   
		    }  
		    shareIntent.putExtra(Intent.EXTRA_TEXT, content);  
		    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		    context.startActivity(Intent.createChooser(shareIntent, context
					.getString(R.string.share)));
	}
	
	
}
