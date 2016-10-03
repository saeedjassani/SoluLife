package com.sapa.solulife.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.sapa.solulife.R;
import com.sapa.solulife.adapters.ChatBotRecyclerAdapter;
import com.sapa.solulife.async.ChatAsyncTask;
import com.sapa.solulife.data.ChatData;
import com.sapa.solulife.utils.Constants;

import java.util.Locale;


public class IAmBoredActivity extends AppCompatActivity {

	RecyclerView recyclerView;
	ChatBotRecyclerAdapter adapter;
	RecyclerView.LayoutManager layoutManager;
	EditText editText;
	FloatingActionButton button;
	FirebaseApp firebaseApp;
	FirebaseDatabase firebaseDatabase;
	DatabaseReference databaseReference;
	Toolbar toolbar;
	TextToSpeech tts;

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.chat_send_button:
					String message = editText.getText().toString();
					if (message.equals("")) return;
					if (message.contains(" ")){
						message.replace(" ", "#");
					}
//					Toast.makeText(IAmBoredActivity.this, "messafe is" + message, Toast.LENGTH_SHORT).show();
					ChatData chatData = new ChatData(Constants.user_id, message, 1);
					databaseReference.push().setValue(chatData);
					editText.setText("");
					new ChatAsyncTask(IAmBoredActivity.this, new ChatAsyncTask.ChatAsyncTaskCallback() {
						@Override
						public void onStart(boolean status) {
//							Toast.makeText(IAmBoredActivity.this, "please wait", Toast.LENGTH_SHORT).show();

						}

						@Override
						public void onResult(boolean result) {
//							Toast.makeText(IAmBoredActivity.this, "res is " + result, Toast.LENGTH_SHORT).show();
							if (result) {
								if (Constants.userData.flag == 0) {
//									Toast.makeText(IAmBoredActivity.this, Constants.userData.reply, Toast.LENGTH_LONG).show();
									tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
										@Override
										public void onInit(int status) {
											if (status != TextToSpeech.ERROR) {
												tts.setLanguage(Locale.ENGLISH);
											}
										}
									});
									ChatData chatData1 = new ChatData(Constants.user_id, Constants.userData.reply, 0);
									tts.speak(Constants.userData.reply, 0, null);
									databaseReference.push().setValue(chatData1);
								} else {
									Toast.makeText(IAmBoredActivity.this, "Playing your song", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(Intent.ACTION_VIEW);
									intent.setData(Uri.parse(Constants.userData.reply));
									startActivity(Intent.createChooser(intent, "Open via"));
								}
							}

						}
					}).execute("http://192.168.127.30:5000/bot", message);
//					Uri parse = parse_string(chatData.getMessage());
/*
					if (parse == null) {
						ChatData chatData1 = new ChatData(Constant.user_id, "Sorry result not found, try again", 0);
						databaseReference.push().setValue(chatData1);
					} else {
						ChatData chatData1 = new ChatData(Constant.user_id, "Loading please wait...", 0);
						databaseReference.push().setValue(chatData1);
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setData(parse);
						startActivity(Intent.createChooser(intent, "Open via"));
					}
*/
					break;
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iam_bored);
		firebaseApp = FirebaseApp.getInstance();

		toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
		toolbar.setTitle("Chat Bot");

		setSupportActionBar(toolbar);

		editText = (EditText) findViewById(R.id.chat_send_message);
		button = (FloatingActionButton) findViewById(R.id.chat_send_button);
		recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
		adapter = new ChatBotRecyclerAdapter(this);
		layoutManager = new LinearLayoutManager(this);
		setUpFirebase();
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);
	}

	private void setUpFirebase() {


		if (firebaseApp != null) {
			firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
			databaseReference = firebaseDatabase.getReference("solulife-e655c/chat_bot");
			button.setOnClickListener(listener);
		} else {
			Toast.makeText(IAmBoredActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
		}

		databaseReference.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				ChatData chatData = dataSnapshot.getValue(ChatData.class);
				adapter.addMessage(chatData);
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	private static final String PROPERTIES_FILENAME = "youtube.properties";
	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
//	private static YouTube youtube;


/*
	public static Uri parse_string(String msg) {
		String search_res = "";
		String search1 = "";
		int i = 5;
		//substring method to strip play out of it
		//String path_internal = Enviroment.getExternalStorageDirectory().toString+"/Music";

		//internal

		String x = msg.substring(5);
		Log.d("saeed", x);
		String path = Environment.getExternalStorageDirectory().toString();
		File f = new File(path);
		File file[] = f.listFiles();


		try {
			for (int j = 0; j < file.length; j++) {
				if (file[i].getName().contains(x)) {
					break;
				}
			}
			String newpath = "";
			if (i < file.length) {
				newpath = path + "/" + file[i].getName();
				Log.d("saeed", newpath);
				return Uri.parse(newpath);
			}


		} catch (Exception e) {


		}

		//youtube
*/
/*
		Properties properties = new Properties();
		try {
			InputStream in = YouTube.Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
					+ " : " + e.getMessage());
			System.exit(1);
		}
*//*

		try {
			youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {

				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName("SoluLife").build();
			search_res = "";
			YouTube.Search.List search = youtube.search().list("id,snippet");
			String apiKey = */
/*properties.getProperty(*//*
"AIzaSyAWS2vB7021iHFHYwZFTW5oYJ8VgqlNlgo"*/
/*)*//*
;
			search.setKey(apiKey);
			search.setQ(x);
			search.setType("video");
			search.setFields("items(id/kind,id/videoId,snippettitle,snippet/thumbnails/default/url)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			SearchListResponse searchResponse = search.execute();
			List<SearchResult> searchResultList = searchResponse.getItems();

			if (searchResultList != null) {

				if (searchResultList.isEmpty()) {
					Log.d("saeed", "parse_string: returned neiulll");

					return null;
				}

				SearchResult singleVideo = searchResultList.get(0);

				ResourceId rId = singleVideo.getId();
				if (rId.getKind().equals("youtube#video")) {

					search_res = rId.getVideoId();

				}
			}
		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}


		Log.d("saeed", search_res);

		return Uri.parse(search_res);

	}
*/

}
