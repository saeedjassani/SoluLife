package com.sapa.solulife.utils;

import android.content.Context;
import android.widget.Toast;
import com.sapa.solulife.data.ChatReply;

/**
 * Created by Saeed Jassani on 30-09-2016.
 */

public class Constants {

	public static int user_id;
	public static String LOG_TAG = "SoluLife";
	public static ChatReply userData;

	public static void showToast(Context context, String s) {
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}
}
