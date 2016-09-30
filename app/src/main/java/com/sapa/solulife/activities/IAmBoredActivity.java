package com.sapa.solulife.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.sapa.solulife.R;
import com.sapa.solulife.adapters.ChapBotRecyclerAdapter;
import com.sapa.solulife.data.ChatData;
import com.sapa.solulife.utils.Constant;

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
}
