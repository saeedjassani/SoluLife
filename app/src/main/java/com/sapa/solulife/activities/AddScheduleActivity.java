package com.sapa.solulife.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sapa.solulife.R;
import com.sapa.solulife.data.ScheduleData;

import java.util.Date;

public class AddScheduleActivity extends AppCompatActivity {

	EditText type, desc, quty;
	FloatingActionButton actionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_schedule);

		type = (EditText) findViewById(R.id.add_sch_type);
		desc = (EditText) findViewById(R.id.add_sch_desc);
		quty = (EditText) findViewById(R.id.add_sch_quty);

		actionButton = (FloatingActionButton) findViewById(R.id.add_schedule);

		actionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				FirebaseApp firebaseApp = FirebaseApp.getInstance();
				if (firebaseApp == null) {
					Toast.makeText(AddScheduleActivity.this, "Check your network connection", Toast.LENGTH_SHORT).show();
					return;
				}
				FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
				DatabaseReference databaseReference = firebaseDatabase.getReference("solulife-e655c/Schedule");

				ScheduleData scheduleData = new ScheduleData(type.getText().toString(), desc.getText().toString(), quty.getText().toString(), new Date());

				databaseReference.push().setValue(scheduleData);

			}
		});

	}
}
