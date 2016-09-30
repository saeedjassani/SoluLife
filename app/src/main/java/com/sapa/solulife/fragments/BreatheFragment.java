package com.sapa.solulife.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.sapa.solulife.Notes.NotesActivity;
import com.sapa.solulife.R;
import com.sapa.solulife.activities.HomeActivity;

/**
 * Created by Pooja S on 9/30/2016.
 */

public class BreatheFragment extends Fragment {

    private View mainView;
    private Context context;

    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    RelativeLayout schedulec, notec, boredc, psychologyc, magnifyc;
    FloatingActionButton start, stop;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_breathe, container, false);
        context = mainView.getContext();

        toolbar = (Toolbar) mainView.findViewById(R.id.toolbar_breathe);
        setActionBar(toolbar);
        toolbar.setTitle("Breathe");
        final Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        start = (FloatingActionButton) mainView.findViewById(R.id.start_button);
        stop = (FloatingActionButton) mainView.findViewById(R.id.stop_button);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    private long time = 0;
                    @Override
                    public void run(){
                        vibrator.vibrate(6000);
                        time += 7000;
                        vibrator.cancel();
                        Log.d("Breathe", "Going for... " + time);
                        h.postDelayed(this, 7000);
                    }
                }, 7000);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                vibrator.cancel();
            }
        });

        return mainView;
    }

    private void setActionBar(Toolbar toolbar) {
        HomeActivity activity = ((HomeActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.updateToggleButton(toolbar);
    }
}