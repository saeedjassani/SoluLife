package com.sapa.solulife.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sapa.solulife.Expense.ExpenseActivity;
import com.sapa.solulife.Expense.NewExpenseActivity;
import com.sapa.solulife.R;
import com.sapa.solulife.fragments.BreathFragment;
import com.sapa.solulife.fragments.MainFragment;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

	CoordinatorLayout coordinatorLayout;

	private Context context;
	FrameLayout frameLayout;
	BottomBar bottomBar;
//	FloatingActionButton actionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		context = this;
		switchFragment1(new MainFragment());
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatelayoutMain);
		frameLayout = (FrameLayout) findViewById(R.id.fragment_holder);

/*
		actionButton = (FloatingActionButton) findViewById(R.id.fab_home);
		actionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
				List<String> providers = lm.getProviders(true);

				Location l = null;

				for (int i = providers.size() - 1; i >= 0; i--) {
					l = lm.getLastKnownLocation(providers.get(i));
					if (l != null && l.getLatitude() != 0.0) break;
				}

				double[] gps = new double[2];
				if (l != null) {
					gps[0] = l.getLatitude();
					gps[1] = l.getLongitude();
				}

				Toast.makeText(HomeActivity.this, gps[0] + " " + gps[1], Toast.LENGTH_SHORT).show();
			}
		});
*/

		BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
		bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelected(@IdRes int tabId) {
				switch (tabId){

					case R.id.home:
						switchFragment(new MainFragment());
						break;

					case R.id.finance:
						//switchFragment(new MainFragment());
						startActivity(new Intent(getApplicationContext(), ExpenseActivity.class));
						break;

					case R.id.breathe:
						switchFragment(new BreathFragment());
						break;

				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final CharSequence[] items_savings = {
				"Police","Ambulance","Fire","Disaster management","Women's helpline"
		};
		switch(item.getItemId()){

			case R.id.emergency:
				new AlertDialog.Builder(HomeActivity.this)
						.setTitle("Make your selection")
						.setItems(items_savings, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								switch (item){
									case 0:
										startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+100)));
										break;

									case 1:
										startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+102)));

										break;

									case 2:
										startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+101)));

										break;

									case 3:
										startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+108)));

										break;

									case 4:
										startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+181)));

										break;

								}
							}
						}).show();
				break;

		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void updateToggleButton(Toolbar toolbar) {
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);

	}

	public void switchFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
				.replace(R.id.fragment_holder, fragment)
				.commit();
	}

	public void switchFragment1(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
				.replace(R.id.fragment_holder, fragment)
				.commit();
	}



}


