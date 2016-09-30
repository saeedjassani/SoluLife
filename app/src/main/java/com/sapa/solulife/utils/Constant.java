package com.sapa.solulife.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Saeed Jassani on 30-09-2016.
 */

public class Constant {

	public static int user_id;

	public static void showToast(Context context, String s) {
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}
}
