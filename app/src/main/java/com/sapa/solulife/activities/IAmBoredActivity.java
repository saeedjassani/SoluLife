package com.sapa.solulife.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.sapa.solulife.R;
import com.sapa.solulife.adapters.ChapBotRecyclerAdapter;
import com.sapa.solulife.data.ChatData;
import com.sapa.solulife.utils.Constant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class IAmBoredActivity extends AppCompatActivity {

	RecyclerView recyclerView;
	ChapBotRecyclerAdapter adapter;
	RecyclerView.LayoutManager layoutManager;
	EditText editText;
	Button button;
	FirebaseApp firebaseApp;
	FirebaseDatabase firebaseDatabase;
	DatabaseReference databaseReference;

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.chat_send_button:
					String message = editText.getText().toString();
					if (message.equals("")) return;
					ChatData chatData = new ChatData(Constant.user_id, message, 1);
					databaseReference.push().setValue(chatData);
					editText.setText("");
					break;
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iam_bored);

		editText = (EditText) findViewById(R.id.chat_send_message);
		button = (Button) findViewById(R.id.chat_send_button);
		recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
		adapter = new ChapBotRecyclerAdapter(this);
		layoutManager = new LinearLayoutManager(this);
		setUpFirebase();
	}

	private void setUpFirebase() {
		firebaseApp = FirebaseApp.getInstance();
		if (firebaseApp != null) {
			firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
			databaseReference = firebaseDatabase.getReference("users");
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
	private static YouTube youtube;


	public static String parse_string(String msg) {
		String search_res = "";
		String search1 = "";
		int i = 5;
		//substring method to strip play out of it
		//String path_internal = Enviroment.getExternalStorageDirectory().toString+"/Music";

		//internal

		String x = msg.substring(5);
		String path = Environment.getExternalStorageDirectory().toString() + "/Music";
		File f = new File(path);
		File file[] = f.listFiles();

		int j = 0;
		for (j = 0; j < file.length; j++) {
			if (file[i].getName().contains(x)) {
				break;
			}
		}
		String newpath = "";
		if (i < file.length) {
			newpath = path + "/" + file[i].getName();
			return newpath;
		}

		//youtube
		Properties properties = new Properties();
		try {
			InputStream in = YouTube.Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
					+ " : " + e.getMessage());
			System.exit(1);
		}

		try {
			youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {

				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName("youtube-cmdline-search-sample").build();
			String queryTerm = x;
			search_res = "";
			YouTube.Search.List search = youtube.search().list("id,snippet");
			String apiKey = properties.getProperty("youtube.apikey");
			search.setKey(apiKey);
			search.setQ(queryTerm);
			search.setType("video");
			search.setFields("items(id/kind,id/videoId,snippettitle,snippet/thumbnails/default/url)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			SearchListResponse searchResponse = search.execute();
			List<SearchResult> searchResultList = searchResponse.getItems();

			if (searchResultList != null) {
				Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
				SearchResult singleVideo = searchResultList.get(0);
				ResourceId rId = singleVideo.getId();
				if (rId.getKind().equals("youtube#video")) {
					Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

					search_res = rId.getVideoId();
					//System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
					//System.out.println(" Thumbnail: " + thumbnail.getUrl());

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


		return search_res;

	}

}
