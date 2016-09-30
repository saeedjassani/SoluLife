package com.sapa.solulife.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.sapa.solulife.R;
import com.sapa.solulife.adapters.ScheduleRecyclerAdapter;
import com.sapa.solulife.data.ScheduleData;

public class ScheduleActivity extends AppCompatActivity {

	RecyclerView recyclerView;
	ScheduleRecyclerAdapter adapter;
	RecyclerView.LayoutManager layoutManager;
	FirebaseApp firebaseApp;
	FirebaseDatabase firebaseDatabase;
	DatabaseReference databaseReference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
		adapter = new ScheduleRecyclerAdapter(this);
		layoutManager = new LinearLayoutManager(this);
		setUpFirebase();

	}

	private void setUpFirebase() {
		firebaseApp = FirebaseApp.getInstance();
		if (firebaseApp != null) {
			firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
			databaseReference = firebaseDatabase.getReference("users");
		} else {
			Toast.makeText(ScheduleActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
		}

		databaseReference.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				ScheduleData data = dataSnapshot.getValue(ScheduleData.class);
				adapter.addMessage(data);
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
