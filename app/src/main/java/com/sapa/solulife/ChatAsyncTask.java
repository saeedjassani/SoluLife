package com.sapa.solulife;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.sapa.solulife.data.ChatReply;
import com.sapa.solulife.data.KeyValuePairData;
import com.sapa.solulife.utils.Constants;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saeed Jassani on 01-10-2016.
 */

public class ChatAsyncTask extends AsyncTask<String, Void, Boolean> {

	URL url;
	HttpURLConnection urlConnection;
	String response;
	InputStream inputStream;

	Context context;
	ChatAsyncTaskCallback callback;
	OutputStream outputStream;
	BufferedWriter bufferedWriter;

	public interface ChatAsyncTaskCallback {
		void onStart(boolean status);
		void onResult(boolean result);
	}

	public ChatAsyncTask(Context context, ChatAsyncTaskCallback callback) {
		this.context = context;
		this.callback = callback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		callback.onStart(true);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		Log.d(Constants.LOG_TAG, " The URL to be fetched is " + params[0]);

		if (Constants.userData == null) Constants.userData = new ChatReply();

		try {
			url = new URL(params[0]);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);

			List<KeyValuePairData> nameValuePair = new ArrayList<KeyValuePairData>();
			nameValuePair.add(new KeyValuePairData("user_id", params[1]));
			nameValuePair.add(new KeyValuePairData("occasion_id", params[2]));

			outputStream = urlConnection.getOutputStream();

			bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
			bufferedWriter.write(writeToOutputStream(nameValuePair));
			bufferedWriter.flush();

			int status_code = urlConnection.getResponseCode();


			if (status_code == 200) {
				inputStream = new BufferedInputStream(urlConnection.getInputStream());
				response = convertInputStreamToString(inputStream);

				Log.d(Constants.LOG_TAG, " The response is " + response);

//				Constants.galleryOverviewData = LoganSquare.parseList(response, GalleryOverviewData.class);
				return true;

			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String writeToOutputStream(List<KeyValuePairData> keyValuePair) throws UnsupportedEncodingException {

		String line;
		String result = "";
		boolean firstTime = true;

		for (KeyValuePairData pair : keyValuePair) {
			if (firstTime) {
				firstTime = false;
			} else {
				result = result.concat("&");
			}
			result = result + URLEncoder.encode(pair.getKey(), "UTF-8");
			result = result + "=";
			result = result + URLEncoder.encode(pair.getValue(), "UTF-8");
		}
		Log.d(Constants.LOG_TAG, " The result is " + result);
		return result;
	}

	public String convertInputStreamToString(InputStream is) throws IOException {

		String line = "";
		String result = "";
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

		while ((line = bufferedReader.readLine()) != null) {
			result += line;
		}

            /* Close Stream */
		if (null != inputStream) {
			inputStream.close();
		}

		return result;
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		super.onPostExecute(aBoolean);
		callback.onResult(true);
	}

}
