package com.sapa.solulife.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.sapa.solulife.Notes.NotesActivity;
import com.sapa.solulife.Notes.NotesAdapter;
import com.sapa.solulife.Reminders.ReminderActivity;
import com.sapa.solulife.activities.HomeActivity;
import com.sapa.solulife.R;
import com.sapa.solulife.activities.IAmBoredActivity;

/**
 * Created by Pooja S on 9/30/2016.
 */

public class MainFragment extends Fragment{

    private View mainView;
    private Context context;

    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    RelativeLayout schedulec, notec, boredc, psychologyc, magnifyc;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_main, container, false);
        context = mainView.getContext();

        toolbar = (Toolbar) mainView.findViewById(R.id.toolbar_main);
        setActionBar(toolbar);
        toolbar.setTitle("Home");
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));

        coordinatorLayout = (CoordinatorLayout) mainView.findViewById(R.id.coordinatinglayout);

        //schedulec = (RelativeLayout) mainView.findViewById(R.id.holder_relative_layout);
        notec = (RelativeLayout) mainView.findViewById(R.id.holder_relative_layout_note);
        boredc = (RelativeLayout) mainView.findViewById(R.id.holder_relative_layout_bored);
        psychologyc = (RelativeLayout) mainView.findViewById(R.id.holder_relative_layout_reminder);
        magnifyc = (RelativeLayout) mainView.findViewById(R.id.holder_relative_layout_magnify);

        notec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotesActivity.class));
            }
        });

        psychologyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ReminderActivity.class));
            }
        });

        boredc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IAmBoredActivity.class));
            }
        });

        return mainView;
    }

    private void setActionBar(Toolbar toolbar) {
        HomeActivity activity = ((HomeActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.updateToggleButton(toolbar);
    }

    public void switchFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_holder, fragment)
                .commit();
    }


}
