package com.sapa.solulife.activities;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sapa.solulife.R;
import com.sapa.solulife.fragments.MainFragment;

public class HomeActivity extends AppCompatActivity {

	CoordinatorLayout coordinatorLayout;

	private Context context;
	FrameLayout frameLayout;
	BottomBar bottomBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		context = this;
		switchFragment1(new MainFragment());
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatelayoutMain);
		frameLayout = (FrameLayout) findViewById(R.id.fragment_holder);

		BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
		bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelected(@IdRes int tabId) {
				switch (tabId){

					case R.id.home:
						switchFragment(new MainFragment());
						break;

					case R.id.finance:
						switchFragment(new MainFragment());
						break;

					case R.id.diet:
						switchFragment(new MainFragment());
						break;

					case R.id.breathe:
						switchFragment(new MainFragment());
						break;

				}
			}
		});

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

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){

			case R.id.settings:

				break;

			case R.id.help:

				break;

			case R.id.about:

				break;

		}
		return true;
	}

}


