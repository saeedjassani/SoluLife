package com.sapa.solulife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sapa.solulife.R;
import com.sapa.solulife.data.ScheduleData;

public class ScheduleActivity extends AppCompatActivity {

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

		Toast.makeText(ScheduleActivity.this, "in sche", Toast.LENGTH_SHORT).show();

		recyclerView = (RecyclerView) findViewById(R.id.schedule_recycler);

		Firebase mRef = null;
		FirebaseRecyclerAdapter<ScheduleData, MyHolderView> adapter =
				new FirebaseRecyclerAdapter<ScheduleData, MyHolderView>(
				ScheduleData.class,
				R.layout.row_schedule,
				MyHolderView.class,
				mRef
		) {
			@Override
			protected void populateViewHolder(MyHolderView myHolderView, ScheduleData scheduleData, int i) {


				myHolderView.type.setText(scheduleData.getType());
			}
		};



//		adapter = new ScheduleRecyclerAdapter(this);
		layoutManager = new LinearLayoutManager(this);
		actionButton = (FloatingActionButton) findViewById(R.id.add_schedule);
		actionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(ScheduleActivity.this, AddScheduleActivity.class));
			}
		});
		setUpFirebase();
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
