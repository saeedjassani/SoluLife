package com.sapa.solulife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sapa.solulife.R;
import com.sapa.solulife.data.ScheduleData;
import com.sapa.solulife.utils.Constants;

public class ScheduleActivity extends AppCompatActivity {

	Firebase mRef;

	RecyclerView recyclerView;
//	ScheduleRecyclerAdapter adapter;
	RecyclerView.LayoutManager layoutManager;
	FirebaseApp firebaseApp;
	FirebaseDatabase firebaseDatabase;
	DatabaseReference databaseReference;
	FloatingActionButton actionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		recyclerView = (RecyclerView) findViewById(R.id.schedule_recycler);

		Firebase.setAndroidContext(this);
		mRef = new Firebase("https://solulife-e655c.firebaseio.com/Schedule");
		Log.d(Constants.LOG_TAG, "onCreate: after mref");
		FirebaseRecyclerAdapter<ScheduleData, MyHolderView> adapter =
				new FirebaseRecyclerAdapter<ScheduleData, MyHolderView>(
				ScheduleData.class,
				R.layout.row_schedule,
				MyHolderView.class,
				mRef
		) {
			@Override
			protected void populateViewHolder(MyHolderView myHolderView, ScheduleData scheduleData, int i) {
				Log.d(Constants.LOG_TAG, "populateViewHolder: ");
				Toast.makeText(ScheduleActivity.this, "in poropr", Toast.LENGTH_SHORT).show();
				myHolderView.type.setText(scheduleData.getType());
			}
		};

		mRef.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
			public void onCancelled(FirebaseError firebaseError) {

			}
		});



//		adapter = new ScheduleRecyclerAdapter(this);
		layoutManager = new LinearLayoutManager(this);
		actionButton = (FloatingActionButton) findViewById(R.id.add_schedule);
		actionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(ScheduleActivity.this, AddScheduleActivity.class));
			}
		});
//		setUpFirebase();
		recyclerView.setAdapter(adapter);


	}

	private void setUpFirebase() {
		firebaseApp = FirebaseApp.getInstance();
		if (firebaseApp != null) {
			firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
			databaseReference = firebaseDatabase.getReference("solulife-e655c/Schedule");
		} else {
			Toast.makeText(ScheduleActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
		}


	}

	static class MyHolderView extends RecyclerView.ViewHolder {

		TextView type, desc, quantity, time;

		public MyHolderView(View itemView) {
			super(itemView);
			type = (TextView) itemView.findViewById(R.id.schedule_type);
			desc = (TextView) itemView.findViewById(R.id.schedule_desc);
			quantity = (TextView) itemView.findViewById(R.id.schedule_quantity);
			time = (TextView) itemView.findViewById(R.id.schedule_time);
		}
	}

}
